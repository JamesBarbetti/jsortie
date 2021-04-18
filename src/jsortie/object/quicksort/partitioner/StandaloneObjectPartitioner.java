package jsortie.object.quicksort.partitioner;

import java.util.Comparator;

public interface StandaloneObjectPartitioner<T> {
  public int[] partitionRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop );

}
