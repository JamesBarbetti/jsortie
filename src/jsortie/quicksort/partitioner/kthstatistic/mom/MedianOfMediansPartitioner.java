package jsortie.quicksort.partitioner.kthstatistic.mom;

import jsortie.helper.FancierEgalitarianPartitionerHelper;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.kthstatistic.KthStatisticExpanderBase;
import jsortie.quicksort.selector.dirty.DirtySelectorHelper;

public class MedianOfMediansPartitioner
  extends KthStatisticExpanderBase {
  FancierEgalitarianPartitionerHelper feph
    = new FancierEgalitarianPartitionerHelper();
  protected int g = 5;  //must be odd. Must be at least 5 
  protected DirtySelectorHelper 
    helper = new DirtySelectorHelper();
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "(" + g 
      + "," + leftExpander.toString() 
      + "," + rightExpander.toString() + ")";
  }
  public MedianOfMediansPartitioner() {
    super();
    this.g = 5;
    this.janitorThreshold 
      = ( g < 10 ) ? 10 : g;
  }
  public MedianOfMediansPartitioner(int gToUse) {
    if (gToUse<5) {
      gToUse=5; 
    } else if ((gToUse&1)==0) { 
      ++gToUse;
    }
    this.g = gToUse; 
    if ( janitorThreshold < g ) { 
      janitorThreshold = g; 
    }
  }
  public MedianOfMediansPartitioner 
    ( int gToUse
    , PartitionExpander expanderToUseOnLeft
    , PartitionExpander expanderToUseOnRight) {
    if (gToUse<5) {
      gToUse=5; 
    } else if ((gToUse&1)==0) { 
      ++gToUse;
    }
    this.g = gToUse; 
    this.leftExpander  = expanderToUseOnLeft;
    this.rightExpander = expanderToUseOnRight;
    if ( janitorThreshold < g ) { 
      janitorThreshold = g; 
    }
  }  
  public int selectPivotIndex
    ( int[] vArray, int start, int stop
    , PartitionExpander expander
    , Double comparisonsLeft) {
    int count      = stop - start;
    int step       = count/g;
    step          += ((step&1)==0) ? 1 : 0;
    int twoStep    = step + step;
    int innerStart = start+step*(g/2)+(count%step)/2;
    int innerStop  = innerStart+step;
    if ( g==5 ) {
      //5.6m comparisons
      int iStop = innerStop;
      if ( stop-twoStep <= iStop ) {
        iStop = stop - twoStep;
      }
      for (int i=innerStart; i<iStop; ++i) {
        helper.branchAvoidingFindMedianOf5 
          ( vArray, i - twoStep, i-step
          , i, i+step, i+twoStep);
      }
      comparisonsLeft -= (stop-innerStart)*6;
    } else {
      comparisonsLeft -=
        helper.partiallySortSlices
          ( vArray, start, stop, step
          , innerStart, innerStop );
    }
    int candidate  = innerStart+step/2;
    partitionRangeExactly 
      ( vArray, innerStart, innerStart, candidate
      , innerStop, innerStop
      , expander, comparisonsLeft );
    return candidate;
  }
  @Override
  public void partitionRangeExactly 
    ( int[] vArray, int start
    , int stop, int target) {
    Double comparisonsLeft 
      = (double)(stop-start) * 22.0;
    PartitionExpander expander 
      = ( target-start < stop-target )
      ? leftExpander : rightExpander;
    partitionRangeExactly
      ( vArray, start, start, target
      , stop, stop, expander
      , comparisonsLeft);
  }
  public void partitionRangeExactly 
    ( int[] vArray, int outerStart, int start
    , int target, int stop, int outerStop
    , PartitionExpander expander
    , Double comparisonsLeft ) {
    while ( janitorThreshold < stop-start ) {
      //
      //if g=5, 
      //   average: 2.12m comparisons at each level; and
      //   problem size reduced by 0.3, so total
      //   expected comparison: bounded above by
      //   7.07m (2.12 / 0.3), using a single pivot
      //   partitioner (e.g. via PretendExpander)
      //
      //   or: 1.92m comparisons at each level (using
      //   an expander reduces it by 0.2m),
      //   bounded *above* by 6.4m (with a partition
      //   expander).
      //
      //   worst case (with expander):
      //   2m comparisons at each level each pass; and
      //   problem size reduced by 0.1, so total
      //   worst-case comparisons bounded above by 20m.
      //   worst case (with partitioner):
      //   bound 2.2m at each level, so total below 22m.
      //
      int pivotIndex 
        = selectPivotIndex
          ( vArray, start, stop
          , expander, comparisonsLeft );
      //This bit has to match the 
      //expander.corresponding bit at the start
      //of selectPivotIndex exactly (count, step, 
      //innerStart, and innerStop have to be the same).
      int count      = stop - start;
      int step       = count/g;
      step          += ((step&1)==0) ? 1 : 0;
      int innerStart = start+step*(g/2)+(count%step)/2;
      int innerStop  = innerStart+step;
      int split = expander.expandPartition 
              ( vArray, start, innerStart
              , pivotIndex, innerStop, stop);
      comparisonsLeft -= 
          (innerStart-start) +
          (stop-innerStop);
      int vPivot = vArray[split]; 
      if (split < target) {
        if (0<comparisonsLeft) {
          if ( outerStart<start ) {
            --comparisonsLeft;
            if ( vArray[start-1]==vPivot ) {
              //every item in vArray[start-1..split]
              //must be ==vPivot.
              return;
            }
          }
          start = split + 1 ;
        } else {
          start = feph.moveEqualOrLessToLeft
                  ( vArray, split+1, stop, vPivot );
          comparisonsLeft -= (stop-split-1);
          if (target < start) {
            return;
          }
        }
        expander = rightExpander;
      } else if ( target < split) {
        if (0<comparisonsLeft) {
          if ( stop<outerStop ) { 
            --comparisonsLeft;
            if ( vArray[stop]==vPivot) {
              //every item in vArray[split..stop]
              //must be ==vPivot.
              return;
            }
          }
          stop = split;
        } else {
          stop = feph.moveEqualOrGreaterToRight
                 ( vArray, start, split, vPivot );
          comparisonsLeft -= (split-start);
          if (stop <= target) {
            return;
          }          
        }
        expander = leftExpander;
      } else {
        return;
      }
    }
    janitor.partitionRangeExactly
      ( vArray, start, stop, target);
    comparisonsLeft -= 
      (stop-start) * (stop-start-1) / 2;
      //Upper bound! Might well be less.
  }
}
