package jsortie.quicksort.partitioner.kthstatistic.mom;

import jsortie.RangeSorter;
import jsortie.helper.ShiftHelper;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.branchavoiding.SkippyExpander;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;

public class ReallyFancyMedianOfMediansPartitioner 
  implements KthStatisticPartitioner {
  protected RangeSorter isort           = new InsertionSort2Way();
  protected int insertionSortThreshold  = 10;
  protected ShiftHelper shifty         = new ShiftHelper();
  protected PartitionExpander expander = new SkippyExpander();
  public String toString() {
    return this.getClass().getSimpleName() 
           + "(" + expander.toString() + ")";
  }
  public int getBlockCount(int count) {
    int    blockCount  =  (int) Math.floor ( Math.sqrt( count ) );
    return blockCount + (((blockCount&1)==0) ? 1 : 0);
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start, int stop, int targetIndex ) {
    while (insertionSortThreshold < stop-start) {	
      int    count       = (stop-start);
      int    blockCount  = getBlockCount(count);
      double blockSize   = (double)(stop-start)        / (double)blockCount;
      double x           = .5; //(double)(targetIndex-start) / (double)count;
      int    blockNumber = 0;
      //Find the item with fractional rank x in each block, and build a sample
      //that consists of exactly those items.
      int[]  vSample     = new int[blockCount];
      for ( double floatBlockStart = start + .5
          ; floatBlockStart + 1 < stop
          ; floatBlockStart+=blockSize) {
        int blockStart  = (int)Math.floor(floatBlockStart);
        int blockStop   = (int)Math.floor(floatBlockStart+blockSize);
        int blockMiddle = (int)Math.floor(floatBlockStart+blockSize*x);
        if (blockMiddle==blockStop) --blockMiddle;
        partitionRangeExactly(vArray, blockStart, blockStop, blockMiddle );
        vSample[blockNumber] = vArray[blockMiddle];      
        ++blockNumber;
      }
      //find the median, vPivot, of the sample
      partitionRangeExactly( vSample, 0, blockCount, blockCount/2);
      int vPivot = vSample[blockCount/2];
      //partition the array using vPivot.  But... we don't need to compare vPivot with
      //every item in the array.  Since each block has been partially sorted, using
      //an item in the middle of the block, we need only compare vPivot with 
      //that, and then with some (a fraction of either x on the left or 1.0-x on the
      //right) of the other items in the block(!).
      int split = start;
      int copiesOfPivotSeen = 0;
      for ( double floatBlockStart = start + .5
          ; floatBlockStart + 1 < stop
          ; floatBlockStart+=blockSize) {
        int blockStart  = (int)Math.floor(floatBlockStart);
        int blockStop   = (int)Math.floor(floatBlockStart+blockSize);
        int blockMiddle = (int)Math.floor(floatBlockStart+blockSize*x);
        if (blockMiddle==blockStop) --blockMiddle;
        int vMiddle = vArray[blockMiddle];
        if ( vMiddle <= vPivot ) {
          //The items [blockStart..blockMiddle] are <= vPivot, swap them to the
          //far left of the under-construction right partition, then adjust the boundary
          int countLess = blockMiddle+1-blockStart;
          shifty.moveBackElementsToFront 
            ( vArray, split, blockMiddle+1-countLess, blockMiddle+1);
          split          += countLess;
          
          if ( vMiddle < vPivot ) {        	 
        	//Some items in the right portion (fraction:1-x) of the block might be less than vPivot
        	split = expandRight(vArray, vPivot, split, blockMiddle+1, blockStop);
          } else {
            ++copiesOfPivotSeen;
          }
        } else {
          //Some items in the left portion (fraction:x) of the block might be more than vPivot
          split = expandRight(vArray, vPivot, split, blockStart, blockMiddle);
          //Skip over the items in the right portion (fraction:1-x) of the block 
          //(leave them where they are)
        }
      }
      //everything in [start..(split-1)] <= vPivot and 
      //vPivot <= everything in [split..(stop-1)].
      if (targetIndex < split) stop=split; else start=split;      
      if (1<copiesOfPivotSeen) {
        if (split==stop) {
          stop = expandLeft(vArray, vPivot, start, stop-1, stop-1);
        } else {
          start = expandRight(vArray, vPivot, start, start, stop);
        }
      }
      if (targetIndex < start || stop <= targetIndex ) {
        return;
      }
    }
    if (start<stop) {
      isort.sortRange(vArray, start, stop);    
    }
  }
  protected int expandRight(int[] vArray, int vPivot, int split, int startRight, int stop) {
    if (startRight==split) {
      ++startRight;
    }
    if (startRight<stop) {
      int vLifted   = vArray[split];
      vArray[split] = vPivot;
      split = expander.expandPartition(vArray, split, split, split, startRight, stop);
      vArray[split] = vLifted;
      if (vLifted<vPivot) {
        ++split;
      }
    }
    return split;
  }
  
  protected int expandLeft(int[] vArray, int vPivot, int start, int stopLeft, int split) {
    if (start<stopLeft) {
      int vLifted   = vArray[split];
      vArray[split] = vPivot;
      split = expander.expandPartition(vArray, start, stopLeft, split, split+1, split+1);
      vArray[split] = vLifted;
      if (vLifted<vPivot) {
        ++split;
      }
    }
    return split;
  }
}
