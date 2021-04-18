package jsortie.object.quicksort.selector;

import java.util.Comparator;

public interface KthStatisticObjectPartitioner<T> {
  public void partitionRangeExactly
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int targetIndex);
}
