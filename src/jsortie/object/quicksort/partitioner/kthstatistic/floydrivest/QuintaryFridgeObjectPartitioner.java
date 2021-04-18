package jsortie.object.quicksort.partitioner.kthstatistic.floydrivest;

import jsortie.object.quicksort.expander.PartitionObjectExpander;

public class QuintaryFridgeObjectPartitioner<T> 
  extends FridgeObjectPartitioner<T> {
  public QuintaryFridgeObjectPartitioner() {
    super(true);
    quintary = true;
  }
  public QuintaryFridgeObjectPartitioner
    ( PartitionObjectExpander<T> expanderOnLeft
    , PartitionObjectExpander<T> expanderOnRight) {
    super(expanderOnLeft, expanderOnRight);
    quintary = true;
  }
  public QuintaryFridgeObjectPartitioner(double alpha) {
    super(alpha);
    quintary = true;
  }
}

