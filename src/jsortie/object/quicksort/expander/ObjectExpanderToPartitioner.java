package jsortie.object.quicksort.expander;

import java.util.Comparator;

import jsortie.object.quicksort.partitioner.SinglePivotObjectPartitioner;

public class ObjectExpanderToPartitioner<T> 
  implements SinglePivotObjectPartitioner<T> {
  protected PartitionObjectExpander<T> expander;
  public ObjectExpanderToPartitioner
    ( PartitionObjectExpander<T> expanderToWrap ) {
    expander = expanderToWrap;
  }
  @Override
  public int partitionRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int pivotIndex) {
    return expander.expandPartition
           ( comparator, vArray, start, pivotIndex
           , pivotIndex, pivotIndex+1, stop);
  }
}
