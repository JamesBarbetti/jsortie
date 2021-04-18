package jsortie.quicksort.partitioner.kthstatistic.algorithm489;

import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.branchavoiding.LeftSkippyExpander;
import jsortie.quicksort.expander.branchavoiding.RightSkippyExpander;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.Afterthought489SampleSelector;

public class Afterthought489Partitioner 
  extends IntroSelect489Partitioner {
  public Afterthought489Partitioner
    ( SampleCollector collectorToUse
    , PartitionExpander leftExpander
    , PartitionExpander rightExpander) {
    super
      ( new Afterthought489SampleSelector()
      , collectorToUse, leftExpander, rightExpander);
  }
  public Afterthought489Partitioner() {
    super
      ( new Afterthought489SampleSelector()
      , new NullSampleCollector()
      , new LeftSkippyExpander() 
      , new RightSkippyExpander() );
  }
}
