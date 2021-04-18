package jsortie.quicksort.multiway.partitioner.kangaroo;

import jsortie.quicksort.expander.branchavoiding.SkippyCentripetalExpander;
import jsortie.quicksort.multiway.expander.kangaroo.SkippyExpander2;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitionerBase;

public class SkippyPartitioner2 
  extends HolierThanThouPartitionerBase {	
  public SkippyPartitioner2() {
    super ( new SkippyCentripetalExpander()
          , new SkippyExpander2()); 
  }  
}
