package jsortie.quicksort.partitioner.kthstatistic.quickselect;

import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;

public class DutchQuickSelectPartitioner
  extends TernaryQuickSelectPartitioner {
  public DutchQuickSelectPartitioner 
    ( SinglePivotSelector     selectorToUse
    , SinglePivotPartitioner  partitionerOnLeft
    , SinglePivotPartitioner  partitionerOnRight
    , int                     threshold
    , KthStatisticPartitioner janitorToUse) {
    super 
      ( selectorToUse
      , partitionerOnLeft, partitionerOnRight
      , 5, janitorToUse);
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start
    , int stop, int target) {
    int pivotIndex    = target;
    SinglePivotPartitioner partitioner 
      = ( target - start < stop - target )
        ? leftPartitioner : rightPartitioner;
    while (janitorThreshold<stop-start) {
      int split = partitioner.partitionRange
                  ( vArray, start, stop, pivotIndex );
      int vPivot = vArray[split];
      int leftSplit 
        = feph.swapEqualToRight 
          ( vArray, start, split, vPivot );
      int rightSplit 
        = feph.swapEqualToLeft 
          ( vArray, split+1, stop , vPivot );
      if (target < leftSplit ) {
        stop = leftSplit;
        partitioner = leftPartitioner;
      } else if (rightSplit <= target ) {
        start = rightSplit;
        partitioner = rightPartitioner;
      } else {
        return;
      }
      if (stop<start+2) {
        return;
      }
      pivotIndex = reselector
        .selectPivotIndexGivenHint
          ( vArray, start, target, stop );
    }
    janitor.partitionRangeExactly
    ( vArray, start, stop, target );
  }
}
