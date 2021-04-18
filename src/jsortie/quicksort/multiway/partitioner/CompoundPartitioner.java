package jsortie.quicksort.multiway.partitioner;

import jsortie.quicksort.multiway.partitioner.adapter.SingleToMultiPartitioner;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.multiway.selector.adapter.SingleToMultiSelector;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.interfaces.StandAlonePartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;

public class CompoundPartitioner 
  implements StandAlonePartitioner {
  protected MultiPivotSelector    selector;
  protected MultiPivotPartitioner partitioner;
  String    name;
  public CompoundPartitioner 
    ( MultiPivotSelector    selectorToUse
    , MultiPivotPartitioner partitionerToUse) {
    selector    = selectorToUse;
    partitioner = partitionerToUse;
    name = selectorToUse.toString() 
      + ", " + partitionerToUse.toString();
  }
  public CompoundPartitioner 
    ( SinglePivotSelector    selectorToUse
    , SinglePivotPartitioner partitionerToUse) {
    selector = new SingleToMultiSelector
                   ( selectorToUse );
    partitioner = new SingleToMultiPartitioner
                      ( partitionerToUse );
    name = selectorToUse.toString() 
      + ", " + partitionerToUse.toString();
  }
  public String toString() {
    return name;
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop ) {
    int[] pivots 
      = selector.selectPivotIndices
        ( vArray, start, stop );
    return partitioner.multiPartitionRange
           ( vArray, start, stop, pivots );
  }
}
