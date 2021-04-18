package jsortie.object.quicksort.multiway.selector;

import java.util.Comparator;

public interface MultiPivotObjectSelector<T> {
  public int[] selectPivotIndices
    ( Comparator<? super T> comparator
    , T [] vArray, int start, int stop);
}
