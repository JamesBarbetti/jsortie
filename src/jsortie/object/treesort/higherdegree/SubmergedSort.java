package jsortie.object.treesort.higherdegree;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.mergesort.ArrayObjectMergesort;

public class SubmergedSort<T>
  implements ObjectRangeSorter<T> {
  protected ObjectRangeSorter<T> janitor;
  protected int                  threshold;
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + janitor.toString() 
      + ", " + threshold + ")";
  }
  public SubmergedSort
    ( ObjectRangeSorter<T> janitor
    , int threshold) {
    this.janitor    = janitor;
    this.threshold  = threshold;
  }
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    ObjectRangeSorter<T> mergeSort 
      = new ArrayObjectMergesort<T>();
    SortingATree<T> tree 
      = new SortingATree<T>(comparator, mergeSort);
    tree.sort( vArray, start, stop);
  }
}
