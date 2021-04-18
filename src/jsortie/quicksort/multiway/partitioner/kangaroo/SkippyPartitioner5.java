package jsortie.quicksort.multiway.partitioner.kangaroo;

import jsortie.quicksort.expander.branchavoiding.SkippyCentripetalExpander;
import jsortie.quicksort.multiway.expander.kangaroo.SkippyExpander5;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitionerBase;

public class SkippyPartitioner5 
  extends HolierThanThouPartitionerBase {	
  public SkippyPartitioner5() {
    super ( new SkippyCentripetalExpander()
          , new SkippyExpander5()); 
  }  
}