package jsortie.quicksort.multiway.partitioner.holierthanthou;

import jsortie.quicksort.multiway.expander.holierthanthou.HolierThanThouExpander4;

public class HolierThanThouPartitioner4 
  extends HolierThanThouPartitionerBase  {
  public HolierThanThouPartitioner4() {
    super ( new HolierThanThouExpander4() );
  }
}
