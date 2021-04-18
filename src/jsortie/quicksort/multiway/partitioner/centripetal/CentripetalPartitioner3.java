package jsortie.quicksort.multiway.partitioner.centripetal;

import jsortie.quicksort.expander.bidirectional.CentripetalExpander;
import jsortie.quicksort.multiway.expander.centripetal.CentripetalExpander3;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitionerBase;

public class CentripetalPartitioner3 
  extends HolierThanThouPartitionerBase {
  public CentripetalPartitioner3() {
    super ( new CentripetalExpander()
          , new CentripetalExpander3());
  }
}
