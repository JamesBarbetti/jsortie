package jsortie.object.quicksort.selector;

import java.util.Comparator;

public class MiddleElementObjectSelector<T> 
  implements SinglePivotObjectSelector<T> {
  @Override 
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override 
  public int selectPivotIndex
    (Comparator<? super T> comparator, T[] vArray
    , int start, int stop) {
    return start + (stop-start) / 2;
  }
}
