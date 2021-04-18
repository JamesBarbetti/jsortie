package jsortie.quicksort.partitioner.kthstatistic.quickselect;

import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.reselector.SelectorToReselector;
import jsortie.quicksort.selector.simple.MiddleElementSelector;

public class SamplingQuickSelectPartitioner 
  extends QuickSelectPartitioner {
  protected class SamplingSelectorWrapper 
    extends SelectorToReselector {
    protected QuickSelectPartitioner qsel;
    public SamplingSelectorWrapper 
      ( SampleCollector collectorToUse
      , SinglePivotSelector selectorToUse) {
      super(selectorToUse);
      collector = collectorToUse;
      qsel 
        = new QuickSelectPartitioner
              ( selectorToUse
              , leftPartitioner, rightPartitioner
              , janitorThreshold, janitor);
    }
    @Override
    public int selectPivotIndexGivenHint
      ( int[] vArray, int start, int hint, int stop )
    {
      if (stop-start<janitorThreshold) {
        return super.selectPivotIndexGivenHint
               ( vArray, start, hint, stop );
      } else {
        int count       = stop -start;
        int sampleSize  = sizer.getSampleSize(count, 2);
        int sampleStart = start + (count-sampleSize) / 2;
        int sampleStop  = sampleStart + sampleSize;
        collector.moveSampleToPosition
          ( vArray, start, stop, sampleStart, sampleStop );
        int targetIndex 
          = sampleStart + (int)Math.floor((double)(hint-start)*sampleSize/count);
        qsel.partitionRangeExactly
          ( vArray, sampleStart, sampleStop, targetIndex );
        return targetIndex;
      }
    }
  }
  public SamplingQuickSelectPartitioner
    ( SampleCollector collector
    , SinglePivotPartitioner leftPartitioner
    , SinglePivotPartitioner rightPartitioner
    , int thresholdToUse
    , KthStatisticPartitioner janitorToUse) {
    super ( new MiddleElementSelector()
          , leftPartitioner
          , rightPartitioner
          , thresholdToUse, janitorToUse);
    this.reselector 
      = new SamplingSelectorWrapper
            ( collector, new MiddleElementSelector() );
  }
}
