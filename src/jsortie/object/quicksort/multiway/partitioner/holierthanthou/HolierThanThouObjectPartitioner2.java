package jsortie.object.quicksort.multiway.partitioner.holierthanthou;

import jsortie.object.quicksort.multiway.expander.HolierThanThouObjectExpander2;

public class HolierThanThouObjectPartitioner2<T> 
  extends HolierThanThouObjectPartitionerBase <T> {
  public HolierThanThouObjectPartitioner2()
  {
    super(new HolierThanThouObjectExpander2<T>());
  }
}