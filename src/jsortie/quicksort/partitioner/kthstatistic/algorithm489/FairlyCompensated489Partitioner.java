package jsortie.quicksort.partitioner.kthstatistic.algorithm489;

import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.FairlyCompensatedSampleSelector;

public class FairlyCompensated489Partitioner 
  extends IntroSelect489Partitioner {
  public FairlyCompensated489Partitioner
    ( SampleCollector collectorToUse
    , PartitionExpander leftExpander
    , PartitionExpander rightExpander) {
    super ( new FairlyCompensatedSampleSelector()
          , collectorToUse
          , leftExpander, rightExpander);
  }
}
