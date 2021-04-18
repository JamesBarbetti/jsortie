package jsortie.object;

import java.util.Comparator;

public class NullObjectRangeSorter<T> 
  implements ObjectRangeSorter<T> {
  @Override
  public void sortRange
    ( Comparator<? super T> comparator, T[] vArray
    , int start, int stop) { //do *nothing*
  }
}
