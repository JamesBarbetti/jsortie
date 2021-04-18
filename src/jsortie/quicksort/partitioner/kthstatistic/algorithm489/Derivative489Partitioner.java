package jsortie.quicksort.partitioner.kthstatistic.algorithm489;

import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.Derivative489SampleSelector;

public class Derivative489Partitioner 
  extends IntroSelect489Partitioner {
  public Derivative489Partitioner
    ( SampleCollector collectorToUse
    , PartitionExpander leftExpander
    , PartitionExpander rightExpander) {
    super
      ( new Derivative489SampleSelector()
      , collectorToUse, leftExpander, rightExpander);
  }
}
