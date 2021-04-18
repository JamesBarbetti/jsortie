package jsortie.quicksort.partitioner.kthstatistic.mom;

import jsortie.helper.RangeSortHelper;
import jsortie.quicksort.expander.PartitionExpander;

public class FancierMedianOfMediansPartitioner 
  extends MedianOfMediansPartitioner {
  //note: this uses a much *larger* g.  not 5!
  public FancierMedianOfMediansPartitioner
    ( PartitionExpander leftExpanderToUse
    , PartitionExpander rightExpanderToUse) {
    super(5, leftExpanderToUse, rightExpanderToUse);
  }
  @Override
  public int selectPivotIndex
    ( int[] vArray, int start, int stop
    , PartitionExpander expander
    , Double comparisonsLeft ) {
    int    blockCount  = (int) Math.floor ( Math.sqrt( stop - start ) );
    blockCount        += ((blockCount&1)==0) ? 1 : 0;
    double blockSize   = (double)(stop-start) / (double)blockCount;
    int    arrayMiddle = start + (stop-start)/2; 
    int    middleStart = arrayMiddle - (blockCount-1)/2;
    int    blockNumber = 0;
    for ( double floatBlockStart = start + .5
        ; floatBlockStart + 1 < stop
        ; floatBlockStart += blockSize) {
      int blockStart  = (int)Math.floor(floatBlockStart);
      int blockStop   = (int)Math.floor(floatBlockStart+blockSize);
      int blockMiddle = (int)Math.floor(floatBlockStart+blockSize*.5);
      if (blockNumber + blockNumber + 1 == blockCount) {
        blockMiddle   = arrayMiddle;
      }
      partitionRangeExactly
        ( vArray, blockStart, blockStart, blockMiddle
        , blockStop, blockStop, expander, comparisonsLeft );
      ++blockNumber;
    }
    //Move medians of all the other blocks into the middle block
    //String s2 = "";
    int w = middleStart;
    for (double floatBlockMiddle = start+blockSize*.5+.5; floatBlockMiddle + 1 < stop; floatBlockMiddle+=blockSize) {
      int r = (int)Math.floor(floatBlockMiddle);      
      RangeSortHelper.swapElements(vArray, r, w);
      ++w;
    }
    //Partially sort the middle block, to determine the median of the medians
    partitionRangeExactly(vArray, middleStart, middleStart+blockCount, arrayMiddle);
    return arrayMiddle;
  }
}
