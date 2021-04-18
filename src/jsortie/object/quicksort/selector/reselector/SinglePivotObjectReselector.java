package jsortie.object.quicksort.selector.reselector;

import java.util.Comparator;

public interface SinglePivotObjectReselector<T> {
  public int selectPivotIndexGivenHint
  ( Comparator<? super T> comparator, T[] vArray
  , int start, int hint, int stop );
}
