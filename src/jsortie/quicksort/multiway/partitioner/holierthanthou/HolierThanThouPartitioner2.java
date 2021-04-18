package jsortie.quicksort.multiway.partitioner.holierthanthou;

import jsortie.quicksort.multiway.expander.holierthanthou.HolierThanThouExpander2;

public class HolierThanThouPartitioner2 
  extends HolierThanThouPartitionerBase  {
  public HolierThanThouPartitioner2()
  {
    super(new HolierThanThouExpander2());
  }
}