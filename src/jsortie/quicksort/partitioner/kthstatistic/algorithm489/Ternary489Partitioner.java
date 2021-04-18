package jsortie.quicksort.partitioner.kthstatistic.algorithm489;

import jsortie.helper.EgalitarianPartitionerHelper;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.SampleSelector;

public class Ternary489Partitioner
  extends IntroSelect489Partitioner {
  public Ternary489Partitioner() {
    super();
  }
  public Ternary489Partitioner 
    ( SampleCollector collector
    , PartitionExpander leftExpander
    , PartitionExpander rightExpander) { 
      super ( collector
            , leftExpander
            , rightExpander );
  }
  public EgalitarianPartitionerHelper twpHelper
    = new EgalitarianPartitionerHelper();
  protected class TernaryImplementation 
    extends IntroImplementation {
    public TernaryImplementation
      ( SampleSelector samplerToUse ) {
      super(samplerToUse);
    }
    @Override
    public boolean partitionExactly
      ( int targetIndex
      , PartitionExpander expander) {
      pivotIndex 
        = reselector.selectPivotIndexGivenHint
          ( vArray, start, targetIndex, stop );
      for (int count=stop-start 
        ; janitorThreshold < count 
        ; count=stop-start ) {
        if (samplingThreshold < count) {
          partitionSample(expander); 
          //...which sets innerStart and innerStop
        } else {
          innerStart = pivotIndex;
          innerStop  = pivotIndex + 1;
        }        
        int split 
          = expandInnerPartition ( expander );
        int vPivot = vArray[split];
        if (split < targetIndex) {
          start = twpHelper.swapEqualToLeft 
                  ( vArray, split+1, stop , vPivot );
          comparisonCount += (stop-split-1);
          if ( targetIndex < start) {
            return true;
          }
          expander = rightExpander;
        } else if (targetIndex < split) {
          stop = twpHelper.swapEqualToRight 
                 ( vArray, start, split, vPivot );
          comparisonCount += (split-start);
          if ( stop <= targetIndex ) {
            return true;
          }
          expander = leftExpander;
        } else {
          return true;
        }
        pivotIndex 
          = reselector.selectPivotIndexGivenHint
            ( vArray, start, targetIndex, stop );
        if (isComparisonLimitExceeded()) {
          return false;
        }
      }
      janitor.partitionRangeExactly
      ( vArray, start, stop, targetIndex );
      return true;
    }
  }
  protected Implementation getImplementation() {
    return 
      new TernaryImplementation
          ( sampler );
  }
}
