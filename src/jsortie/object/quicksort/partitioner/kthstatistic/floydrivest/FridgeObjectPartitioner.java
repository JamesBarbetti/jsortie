package jsortie.object.quicksort.partitioner.kthstatistic.floydrivest;

import jsortie.object.quicksort.expander.PartitionObjectExpander;
import jsortie.quicksort.partitioner.kthstatistic.floydrivest.partitionselector.FridgeSamplePartitionSelector;

public class FridgeObjectPartitioner<T>
  extends FloydRivestObjectPartitioner<T> {
  public FridgeObjectPartitioner(boolean useOptimisticDeltaFormula) {
    super(8.0);
    ps     = new FridgeSamplePartitionSelector(useOptimisticDeltaFormula);
    beLazy = true;
    useSafetyNet = true;
  }
  public FridgeObjectPartitioner
    ( double alpha, boolean useOptimisticDeltaFormula ) {
    super(alpha);
    ps     = new FridgeSamplePartitionSelector(useOptimisticDeltaFormula);
    beLazy = true;
    useSafetyNet = true;
  }
  public FridgeObjectPartitioner
    ( PartitionObjectExpander<T> expanderOnLeft
    , PartitionObjectExpander<T> expanderOnRight) {
    super(expanderOnLeft, expanderOnRight);
    alpha = 8.0;
    ps = new FridgeSamplePartitionSelector(true);
    beLazy = true;
    useSafetyNet = true;
  }
  public FridgeObjectPartitioner 
    ( double alpha ) {
    super(alpha);
    ps     = new FridgeSamplePartitionSelector();
    beLazy = true; //Todo: make this work properly
    useSafetyNet = true;
  }
}


