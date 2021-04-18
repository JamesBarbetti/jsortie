package jsortie.quicksort.partitioner.kthstatistic.quickselect;

import jsortie.quicksort.partitioner.branchavoiding.SkippyMirrorPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyPartitioner;
import jsortie.quicksort.partitioner.interfaces.ComparisonCountingKthStatisticPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class ComparisonCountingQuickSelectPartitioner 
  implements ComparisonCountingKthStatisticPartitioner {
  protected SinglePivotPartitioner leftPartitioner;
  protected SinglePivotPartitioner rightPartitioner;
  public ComparisonCountingQuickSelectPartitioner() {
    this.leftPartitioner  = new SkippyMirrorPartitioner();
    this.rightPartitioner = new SkippyPartitioner();
  }
  @Override
  public int partitionRangeExactlyCountComparisons
    ( int[] vArray, int start, int stop
    , int target) {
    int comparisonCount = 0;
    int originalStart   = start;
    int originalStop    = stop;
    int pivotIndex      = target;
    SinglePivotPartitioner partitioner 
      = leftPartitioner;
    while (1<stop-start) {
      int split = partitioner.partitionRange
                  ( vArray, start, stop, pivotIndex );
      comparisonCount += (stop-start-1);
      int vPivot = vArray[split];
      if ( split < target) {
        if ( stop < originalStop ) {
          ++comparisonCount;
          if ( vPivot  == vArray[stop] ) {
            return comparisonCount;
          }
        }
        start        = split + 1;
        partitioner  = rightPartitioner;
      } else if (split==target) {
        return comparisonCount;
      } else {
        if ( originalStart < start ) {
          ++comparisonCount;
          if ( vArray[start-1] == vPivot ) {
            return comparisonCount;
          }
        }
        stop        = split;
        partitioner = leftPartitioner;
      }
      if (stop<=start) {
        return comparisonCount;
      }
    }
    return comparisonCount;
  }
}
