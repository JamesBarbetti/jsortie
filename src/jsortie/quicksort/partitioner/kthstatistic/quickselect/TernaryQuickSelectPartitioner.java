package jsortie.quicksort.partitioner.kthstatistic.quickselect;

import jsortie.helper.FancierEgalitarianPartitionerHelper;
import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.KthStatisticPartitionerBase;
import jsortie.quicksort.samplesizer.OneItemSampleSizer;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.reselector.SelectorToReselector;
import jsortie.quicksort.selector.simple.MiddleElementSelector;

public class TernaryQuickSelectPartitioner 
  extends KthStatisticPartitionerBase {
  FancierEgalitarianPartitionerHelper feph
    = new FancierEgalitarianPartitionerHelper();
  public TernaryQuickSelectPartitioner() {
    super();
    this.reselector 
      = new SelectorToReselector 
            ( new MiddleElementSelector() );
  }
  public TernaryQuickSelectPartitioner 
    ( SinglePivotSelector     selectorToUse
    , SinglePivotPartitioner  partitionerOnLeft
    , SinglePivotPartitioner  partitionerOnRight
    , int                     threshold
    , KthStatisticPartitioner janitorToUse) {
    super 
      ( new OneItemSampleSizer()
      , new NullSampleCollector()
      , new SelectorToReselector(selectorToUse)
      , partitionerOnLeft
      , partitionerOnRight
      , 5, janitorToUse);
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start
    , int stop, int target) {
    int pivotIndex    = target;
    SinglePivotPartitioner partitioner 
      = leftPartitioner;
    while (janitorThreshold<stop-start) {
      int split = partitioner.partitionRange
                  ( vArray, start, stop, pivotIndex );
      int vPivot = vArray[split]; 
      if (split < target) {
        start = feph.moveEqualOrLessToLeft
                ( vArray, split, stop, vPivot );
        if (target < start) {
          return;
        }
        partitioner = leftPartitioner;
      } else if (target < split) {
        stop = feph.moveEqualOrGreaterToRight
               ( vArray, start, split, vPivot );
        if (stop <= target) {
          return;
        }
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
