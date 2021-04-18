package jsortie.quicksort.expander.adapter;

import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class ExpanderToPartitioner
  implements SinglePivotPartitioner {
  protected PartitionExpander expander;
  public ExpanderToPartitioner
    ( PartitionExpander expanderToWrap ) {
    expander = expanderToWrap;
  }
  @Override
  public int partitionRange
    ( int[] vArray, int start, int stop
    , int pivotIndex) {
    return expander.expandPartition
           ( vArray, start, pivotIndex
           , pivotIndex, pivotIndex+1, stop);
  }
}
