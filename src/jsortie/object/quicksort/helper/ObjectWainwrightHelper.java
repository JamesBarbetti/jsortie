package jsortie.object.quicksort.helper;

import java.util.Comparator;

public class ObjectWainwrightHelper<T> {
  public boolean isObjectRangeSorted
   ( Comparator<? super T> comparator
   , T[] vArray, int start, int stop)  {
    int i=start;
    int j=stop-2;
    if (i<=j) {
      do {
        if ( comparator.compare
             ( vArray[i+1], vArray[i] ) < 0 ) {
          return false;
        }
        ++i;
        if ( comparator.compare
             ( vArray[j+1], vArray[j] ) < 0 ) {
          return false;
        }
        --j;
      }
      while (i<=j);
    }
    return true;
  }
  public boolean isRangeReverseSorted
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop)  {
    int i=start;
    int j=stop-2;
    if (i<=j) {
      do {
        if ( comparator.compare
             ( vArray[i], vArray[i+1] ) < 0 ) {
          return false;
        }
        ++i;
        if ( comparator.compare
             ( vArray[j], vArray[j+1] ) < 0 ) {
          return false;
        }
        --j;
      }
      while (i<=j);
    }
    return true;
  }
}
