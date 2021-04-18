package jsortie.quicksort.partitioner.kthstatistic.algorithm489;

import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.RauhAndArceSampleSelector;

public class RauhAndArce489Partitioner 
  extends IntroSelect489Partitioner {
  public RauhAndArce489Partitioner
    ( SampleCollector collectorToUse
    , PartitionExpander leftExpander
    , PartitionExpander rightExpander) {
    super
      ( new RauhAndArceSampleSelector(true)
      , collectorToUse, leftExpander, rightExpander);
  }
}
