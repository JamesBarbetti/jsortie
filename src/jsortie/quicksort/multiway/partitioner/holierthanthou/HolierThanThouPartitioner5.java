package jsortie.quicksort.multiway.partitioner.holierthanthou;

import jsortie.quicksort.multiway.expander.holierthanthou.HolierThanThouExpander5;

public class HolierThanThouPartitioner5 
  extends HolierThanThouPartitionerBase {
  public HolierThanThouPartitioner5() {
    super ( new HolierThanThouExpander5() );
  }
}
