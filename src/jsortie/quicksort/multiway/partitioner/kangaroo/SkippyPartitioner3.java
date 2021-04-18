package jsortie.quicksort.multiway.partitioner.kangaroo;

import jsortie.quicksort.expander.branchavoiding.SkippyCentripetalExpander;
import jsortie.quicksort.multiway.expander.kangaroo.SkippyExpander3;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitionerBase;

public class SkippyPartitioner3 
  extends HolierThanThouPartitionerBase {	
  public SkippyPartitioner3() {
    super ( new SkippyCentripetalExpander()
          , new SkippyExpander3()); 
  }  
}
