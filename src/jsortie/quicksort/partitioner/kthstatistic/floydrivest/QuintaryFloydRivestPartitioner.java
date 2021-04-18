package jsortie.quicksort.partitioner.kthstatistic.floydrivest;

import jsortie.helper.FancierEgalitarianPartitionerHelper;
import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.expander.PartitionExpander;

public class QuintaryFloydRivestPartitioner 
  extends FridgePartitioner {
  FancierEgalitarianPartitionerHelper helper
    = new FancierEgalitarianPartitionerHelper(); 
  public QuintaryFloydRivestPartitioner() {
    super();
    quintary = true;
  }
  public QuintaryFloydRivestPartitioner
    ( PartitionExpander expanderOnLeft
    , PartitionExpander expanderOnRight) {
    super
      ( new NullSampleCollector()
      , expanderOnLeft, expanderOnRight);
    quintary = true;
  }
}
