package jsortie.object.janitor;

import java.util.Comparator;

import jsortie.flags.QuadraticAverageCase;
import jsortie.object.ObjectRangeSorter;

public class ObjectInsertionSort<T> 
  implements ObjectRangeSorter<T>
           , QuadraticAverageCase {
  public String toString()  {
    return this.getClass().getSimpleName();
  }
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    insertionSortRange
      ( comparator, vArray, start, stop );
  }
  public static <T> void insertionSortRange
    ( Comparator<? super T> comparer
    , T[] vArray, int start, int stop ) {
    for ( int place=start+1
        ; place<stop; ++place) {
      T v = vArray[place];
      int scan = place - 1;
      for (; start<=scan 
             && comparer.compare
                ( v, (T) vArray[scan] ) < 0
           ; --scan) {
        vArray[scan+1] = vArray[scan];
      }
      vArray[scan+1] = v;
    }		
  }
}
