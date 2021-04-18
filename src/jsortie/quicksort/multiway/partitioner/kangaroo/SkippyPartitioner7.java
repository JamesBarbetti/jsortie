package jsortie.quicksort.multiway.partitioner.kangaroo;

import jsortie.quicksort.expander.branchavoiding.SkippyCentripetalExpander;
import jsortie.quicksort.multiway.expander.kangaroo.SkippyExpander7;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitionerBase;

public class SkippyPartitioner7 
  extends HolierThanThouPartitionerBase {
  public SkippyPartitioner7() {
    super ( new SkippyCentripetalExpander()
          , new SkippyExpander7()); 
  }  
}
