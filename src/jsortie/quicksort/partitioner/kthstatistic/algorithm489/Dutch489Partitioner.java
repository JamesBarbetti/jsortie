package jsortie.quicksort.partitioner.kthstatistic.algorithm489;

import jsortie.helper.EgalitarianPartitionerHelper;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.SampleSelector;

public class Dutch489Partitioner 
  extends IntroSelect489Partitioner {
  public EgalitarianPartitionerHelper twpHelper
    = new EgalitarianPartitionerHelper();
  public Dutch489Partitioner() {
    super();
  }
  public Dutch489Partitioner
    ( SampleCollector collectorToUse
    , PartitionExpander leftXpanda
    , PartitionExpander rightXpanda) {
    super(collectorToUse, leftXpanda, rightXpanda);
  }
  protected class DutchImplementation 
    extends IntroImplementation {
    public DutchImplementation
      ( SampleSelector sampler ) {
      super(sampler);
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
        ; count=stop-start) {
        if (samplingThreshold < count) {
          partitionSample(expander); 
          // sets innerStart and innerStop
        } else {
          innerStart = pivotIndex;
          innerStop  = pivotIndex + 1;
        }
        int split 
          = expandInnerPartition(expander);
        int vPivot = vArray[split];
        int leftSplit 
          = twpHelper.swapEqualToRight 
            ( vArray, start, split, vPivot );
        int rightSplit 
          = twpHelper.swapEqualToLeft 
            ( vArray, split+1, stop , vPivot );
        comparisonCount += (stop-start-1);
        if (targetIndex < leftSplit) {
          expander = leftExpander;
          stop     = leftSplit;
        } else if (rightSplit <= targetIndex) {
          expander = rightExpander;
          start    = rightSplit;
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
  protected Implementation 
    getImplementation() {
    return new DutchImplementation(sampler);
  }
}
