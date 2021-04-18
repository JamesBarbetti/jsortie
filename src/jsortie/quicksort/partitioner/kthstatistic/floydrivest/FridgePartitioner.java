package jsortie.quicksort.partitioner.kthstatistic.floydrivest;

import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.kthstatistic.floydrivest.partitionselector.FridgeSamplePartitionSelector;

public class FridgePartitioner
  extends FloydRivestPartitioner {
  public FridgePartitioner() {
    alpha        = 8.0;
    beLazy       = true;
    useSafetyNet = true;
    ps           
      = new FridgeSamplePartitionSelector(true);
  }
  public FridgePartitioner
    ( SampleCollector sc
    , PartitionExpander lx, PartitionExpander rx) {
    super(sc, lx, rx);
    alpha        = 8.0;
    beLazy       = true;
    useSafetyNet = true;
    ps           
      = new FridgeSamplePartitionSelector(true);
  }
  public FridgePartitioner
    ( SampleCollector sc
    , PartitionExpander lx, PartitionExpander rx
    , boolean goQuintary) {
    super(sc, lx, rx);
    alpha        = 8.0;
    beLazy       = true;
    quintary     = goQuintary;
    useSafetyNet = true;
    ps           
      = new FridgeSamplePartitionSelector(true);
  }
}
