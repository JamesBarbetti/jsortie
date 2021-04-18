package jsortie.object.quicksort.multiway.expander;

import java.util.Comparator;

public interface MultiPivotObjectPartitionExpander<T> {
  public int[] expandPartitions
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stopLeft
    , int[] pivotIndices, int startRight, int stop);
}
