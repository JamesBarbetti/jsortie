package jsortie.object;

import java.util.Comparator;

public interface ObjectRangeSorter<T> {
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop);
}
