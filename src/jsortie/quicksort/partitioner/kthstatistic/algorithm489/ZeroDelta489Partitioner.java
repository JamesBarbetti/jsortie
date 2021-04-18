package jsortie.quicksort.partitioner.kthstatistic.algorithm489;

import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.ZeroDeltaSampleSelector;

public class ZeroDelta489Partitioner 
  extends IntroSelect489Partitioner {
  public ZeroDelta489Partitioner() {
    super ();
    sampler = new ZeroDeltaSampleSelector();
  }
  public ZeroDelta489Partitioner
    ( SampleCollector collectorToUse
    , PartitionExpander leftExpander
    , PartitionExpander rightExpander) {
    super ( new ZeroDeltaSampleSelector()
          , collectorToUse
          , leftExpander, rightExpander);
  }
}
