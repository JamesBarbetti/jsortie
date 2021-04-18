package jsortie.object.quicksort.expander;

import java.util.Comparator;

public interface PartitionObjectExpander<T> {
  public int expandPartition 
    ( Comparator<? super T> comparator
    , T[] vArray, int start,      int stopLeft
    , int hole,   int startRight, int stop);
}
