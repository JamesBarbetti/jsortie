package jsortie.quicksort.expander.adapter;

import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class PartitionerToRepartitioner 
  implements PartitionExpander {
  SinglePivotPartitioner inner;
  public PartitionerToRepartitioner
    ( SinglePivotPartitioner partitionerToUse) {
    inner = partitionerToUse;
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName()
      + "(" + inner.toString() + ")";
  }
  @Override
  public int expandPartition
    ( int[] vArray, int start, int stopLeft
    , int hole, int startRight, int stop) {
    return inner.partitionRange(vArray, start, stop, hole);
  }
}
