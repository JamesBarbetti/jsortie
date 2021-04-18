package jsortie.quicksort.multiway.partitioner.holierthanthou;

import jsortie.quicksort.multiway.expander.holierthanthou.HolierThanThouExpander3;

public class HolierThanThouPartitioner3 
  extends HolierThanThouPartitionerBase {
  public HolierThanThouPartitioner3() {
    super (new HolierThanThouExpander3());
  }	
}
