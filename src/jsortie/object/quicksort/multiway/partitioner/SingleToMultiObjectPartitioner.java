package jsortie.object.quicksort.multiway.partitioner;

import java.util.Comparator;

import jsortie.object.quicksort.partitioner.SinglePivotObjectPartitioner;

public class SingleToMultiObjectPartitioner<T>
  implements MultiPivotObjectPartitioner<T> {
  SinglePivotObjectPartitioner<T> inner;
  @Override
  public String toString() {
    return inner.toString();
  }
  public SingleToMultiObjectPartitioner
    ( SinglePivotObjectPartitioner<T> singlePivotPartitioner ) {
    inner = singlePivotPartitioner;
  }
  @Override  
  public int[] multiPartitionRange
    ( Comparator<? super T> comparator, T[] vArray
    , int start, int stop, int[] pivotIndices) {
    int pivotIndex = pivotIndices[pivotIndices.length/2];
    int split      = inner.partitionRange
                     ( comparator, vArray, start, stop
                     , pivotIndex );
    return new int[] { start, split, split+1, stop };
  }
}
