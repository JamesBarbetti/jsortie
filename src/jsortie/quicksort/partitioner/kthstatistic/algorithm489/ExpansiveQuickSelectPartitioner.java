package jsortie.quicksort.partitioner.kthstatistic.algorithm489;

import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.branchavoiding.LeftSkippyExpander;
import jsortie.quicksort.expander.branchavoiding.RightSkippyExpander;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.OneItemSampleSelector;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.reselector.SelectorToReselector;
import jsortie.quicksort.selector.simple.MiddleElementSelector;

public class ExpansiveQuickSelectPartitioner 
  extends IntroSelect489Partitioner {
  public ExpansiveQuickSelectPartitioner() {
    super ( new OneItemSampleSelector()
          , new NullSampleCollector()
          , new LeftSkippyExpander()
          , new RightSkippyExpander());
    reselector 
      = new SelectorToReselector
            ( new MiddleElementSelector() );
    samplingThreshold = 5;
  }
  public ExpansiveQuickSelectPartitioner
    ( SampleCollector collectorToUse
    , SinglePivotSelector selectorToUse
    , PartitionExpander   leftExpander
    , PartitionExpander   rightExpander) {
    super ( new OneItemSampleSelector()
          , collectorToUse
          , leftExpander, rightExpander);
    reselector 
      = new SelectorToReselector(selectorToUse);
    samplingThreshold = 5;
  }
}
