package jsortie.object.quicksort.partitioner.kthstatistic.quickselect;

import java.util.Comparator;

import jsortie.object.quicksort.expander.LeftLomutoObjectExpander;
import jsortie.object.quicksort.expander.PartitionObjectExpander;
import jsortie.object.quicksort.expander.RightLomutoObjectExpander;
import jsortie.object.quicksort.partitioner.kthstatistic.KthStatisticObjectPartitionerBase;
import jsortie.object.quicksort.selector.SinglePivotObjectSelector;
import jsortie.object.quicksort.selector.reselector.ObjectSelectorToReselector;

public class QuickSelectObjectPartitioner<T> 
  extends KthStatisticObjectPartitionerBase<T> {
  public QuickSelectObjectPartitioner
    ( PartitionObjectExpander<T> expanderToUseOnLeft
    , PartitionObjectExpander<T> expanderToUseOnRight) {
    super ( expanderToUseOnLeft, expanderToUseOnRight );
  }
  public QuickSelectObjectPartitioner() {
    super ( new LeftLomutoObjectExpander<T>()
          , new RightLomutoObjectExpander<T>());
  }
  public QuickSelectObjectPartitioner
    ( SinglePivotObjectSelector<T> sel)  {
    super ( new LeftLomutoObjectExpander<T>()
          , new RightLomutoObjectExpander<T>());
    this.reselector 
      = new ObjectSelectorToReselector<T>(sel);
  }
  //KthStatisticPartitioner
  @Override
  public void partitionRangeExactly
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop, int target) {
    int originalStart = start;
    int originalStop  = stop;
    int pivotIndex    = target;
    PartitionObjectExpander<T> expander = leftExpander;
    while (janitorThreshold<stop-start) {
      int split  = expander.expandPartition
                   ( comparator, vArray, start, pivotIndex
                   , pivotIndex, pivotIndex+1, stop );
      T   vPivot = vArray[split]; //== 
      if (split < target) {
        start        = split + 1;
        //Special handling for situations where many items equal...
        if ( stop < originalStop   
             && comparator.compare
                ( vPivot, vArray[stop] ) ==  0 ) {
          return;
        }
        expander = leftExpander;
      } else if (target < split) {
        stop          = split;
        //Special handling for situations where many items equal...
        if ( originalStart < start
             && comparator.compare
                ( vPivot, vArray[start-1] ) == 0 ) {
          return;
        }
        expander = rightExpander;
      } else {
        return;
      }
      pivotIndex = reselector
        .selectPivotIndexGivenHint
          ( comparator, vArray, start, target, stop );
    }
    janitor.partitionRangeExactly
      ( comparator, vArray, start, stop, target );
  }  
}
