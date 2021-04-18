package jsortie.object.sort.decorator;

import java.util.Comparator;

import jsortie.object.ObjectRangeSortHelper;
import jsortie.object.ObjectRangeSorter;

public class NullFriendlySort<T> 
  implements ObjectRangeSorter<T> {
  protected ObjectRangeSorter<T> innerSort;
  public NullFriendlySort
    ( ObjectRangeSorter<T> sortToDecorate ) {
    innerSort = sortToDecorate;
  }
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    start 
      = ObjectRangeSortHelper.moveNullsOutToLeft
        ( vArray, start, stop );
    innerSort.sortRange
      ( comparator, vArray, start, stop );
  }
}
