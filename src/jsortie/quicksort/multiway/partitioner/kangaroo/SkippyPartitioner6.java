package jsortie.quicksort.multiway.partitioner.kangaroo;

import jsortie.quicksort.expander.branchavoiding.SkippyCentripetalExpander;
import jsortie.quicksort.multiway.expander.kangaroo.SkippyExpander6;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitionerBase;

public class SkippyPartitioner6 
  extends HolierThanThouPartitionerBase {
  public SkippyPartitioner6() {
    super ( new SkippyCentripetalExpander()
          , new SkippyExpander6()); 
  }  
}
