package jsortie.object.quicksort.partitioner.kthstatistic;

import java.util.Comparator;
import jsortie.object.ObjectRangeSorter;
import jsortie.object.quicksort.selector.KthStatisticObjectPartitioner;

public class ObjectRangeSorterToKthStatisticPartitioner<T>
  implements KthStatisticObjectPartitioner<T> {
  public ObjectRangeSorter<T> sorter;
  public ObjectRangeSorterToKthStatisticPartitioner
    ( ObjectRangeSorter<T> sorterToUse) {
    sorter = sorterToUse;
  }
  public String toString() {
    return this.getClass().getSimpleName()
      + "(" + sorter.toString() + ")";
  }
  @Override
  public void partitionRangeExactly
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int targetIndex) {
    sorter.sortRange(comparator, vArray, start, stop); 
  }
}