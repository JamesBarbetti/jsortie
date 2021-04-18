package jsortie.object.quicksort.multiway.partitioner;

import java.util.Comparator;

public interface ExternalMultiPivotObjectPartitioner<T> {
  public int[] externalPartitionRange 
    ( Comparator<? super T> comparator, T[] vSource
    , int start, int stop, int[] pivotIndices
    , T[] vDest,   int destStart);
}
