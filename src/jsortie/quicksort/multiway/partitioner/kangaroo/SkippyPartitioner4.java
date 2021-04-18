package jsortie.quicksort.multiway.partitioner.kangaroo;

import jsortie.quicksort.expander.branchavoiding.SkippyCentripetalExpander;
import jsortie.quicksort.multiway.expander.kangaroo.SkippyExpander4;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitionerBase;

public class SkippyPartitioner4 
  extends HolierThanThouPartitionerBase {
   public SkippyPartitioner4() {
    super ( new SkippyCentripetalExpander()
          , new SkippyExpander4()); 
   }
}  
