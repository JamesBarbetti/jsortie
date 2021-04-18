package jsortie.object.quicksort.selector;

import java.util.Comparator;

public interface SinglePivotObjectSelector <T> {
  public int selectPivotIndex
    ( Comparator<? super T> comparator
    , T [] vArray, int start, int stop);
}
