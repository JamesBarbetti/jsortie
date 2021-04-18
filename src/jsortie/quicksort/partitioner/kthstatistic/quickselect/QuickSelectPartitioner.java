package jsortie.quicksort.partitioner.kthstatistic.quickselect;

import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;

public class QuickSelectPartitioner 
  extends TernaryQuickSelectPartitioner {
  public QuickSelectPartitioner() {
    super ();
  }
  public QuickSelectPartitioner 
    ( SinglePivotSelector     selectorToUse
    , SinglePivotPartitioner  partitionerOnLeft
    , SinglePivotPartitioner  partitionerOnRight
    , int                     threshold
    , KthStatisticPartitioner janitorToUse) {
    super 
      ( selectorToUse
      , partitionerOnLeft
      , partitionerOnRight
      , 5, janitorToUse);
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start, int stop
    , int target) {
    int originalStart = start;
    int originalStop  = stop;
    int pivotIndex    = target;
    int comparisonsLeft 
      = getComparisonsBeforeDespair
        ( stop - start );
    SinglePivotPartitioner partitioner 
      = ( target - start < stop - target )
        ? leftPartitioner
        : rightPartitioner;
    while (janitorThreshold<stop-start) {
      int split 
        = partitioner.partitionRange
          ( vArray, start, stop, pivotIndex );
      comparisonsLeft -= (stop-start);
      if ( split < target) {
        --comparisonsLeft;
        if ( stop < originalStop
             && vArray[split] == vArray[stop] ) {
          return;
        }
        start        = split + 1;
        partitioner  = rightPartitioner;
      } else if (split==target) {
        return;
      } else {
        --comparisonsLeft;
        if ( originalStart < start 
             && vArray[start-1] == vArray[split] ) {
          return;
        }
        stop        = split;
        partitioner = leftPartitioner;
      }
      if (stop<=start) {
        return;
      }
      pivotIndex = reselector
        .selectPivotIndexGivenHint
          ( vArray, start, target, stop );
      if (comparisonsLeft<0) {
        despairingPartitionExactly
        ( vArray, start, stop, target );
        return;
      }
    }
    janitor.partitionRangeExactly
      ( vArray, start, stop, target );
  }
  protected void despairingPartitionExactly
    ( int[] vArray, int start, int stop
    , int target ) {
    super.partitionRangeExactly
      ( vArray, start, stop, target );
  }
  protected int getComparisonsBeforeDespair
    ( int count ) {
    return (count << 3 ); //count * 8
  }
}
