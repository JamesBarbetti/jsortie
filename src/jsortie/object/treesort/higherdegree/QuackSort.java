package jsortie.object.treesort.higherdegree;

import java.util.Comparator;
import jsortie.object.ObjectRangeSorter;
import jsortie.object.quicksort.governor.ArrayObjectQuicksort;
import jsortie.object.quicksort.partitioner.SinglePivotObjectPartitioner;
import jsortie.object.quicksort.selector.SinglePivotObjectSelector;

public class QuackSort<T> 
  implements ObjectRangeSorter<T> {	
  protected ObjectRangeSorter<T> quicksort;
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + quicksort.toString() + ")";
  }
  public QuackSort() {
    this.quicksort  = new ArrayObjectQuicksort<T>();
  }
  public QuackSort
    ( SinglePivotObjectSelector<T> selector
    , SinglePivotObjectPartitioner<T> partitioner
    , ObjectRangeSorter<T> janitor, int threshold
    , ObjectRangeSorter<T> lastResort) {
    this.quicksort 
      = new ArrayObjectQuicksort<T>
            ( selector, partitioner
            , janitor, threshold, lastResort );
  }
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    SortingATree<T> tree = new SortingATree<T>(comparator, quicksort);
    tree.sort(vArray, start, stop);
  }
}
