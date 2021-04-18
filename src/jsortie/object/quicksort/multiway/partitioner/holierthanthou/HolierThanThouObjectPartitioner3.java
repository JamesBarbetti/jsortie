package jsortie.object.quicksort.multiway.partitioner.holierthanthou;

import jsortie.object.quicksort.multiway.expander.HolierThanThouObjectExpander3;

public class HolierThanThouObjectPartitioner3<T> 
  extends HolierThanThouObjectPartitionerBase<T> {
  public HolierThanThouObjectPartitioner3()
  {
    super(new HolierThanThouObjectExpander3<T>());
  }
}