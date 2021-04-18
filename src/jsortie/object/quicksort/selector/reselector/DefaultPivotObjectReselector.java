package jsortie.object.quicksort.selector.reselector;

import java.util.Comparator;

public class DefaultPivotObjectReselector<T> 
  implements SinglePivotObjectReselector<T> {
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public int selectPivotIndexGivenHint
    ( Comparator<? super T> comparator
    , T[] vArray
    , int start, int hint, int stop ) {
    return hint;
  }
}
