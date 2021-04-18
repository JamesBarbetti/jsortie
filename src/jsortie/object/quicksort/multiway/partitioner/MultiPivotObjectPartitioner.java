package jsortie.object.quicksort.multiway.partitioner;

import java.util.Comparator;

public interface MultiPivotObjectPartitioner<T> {
  public int[] multiPartitionRange
    ( Comparator<? super T> comparator, T[] vArray
    , int start, int stop, int[] pivotIndices);
}
