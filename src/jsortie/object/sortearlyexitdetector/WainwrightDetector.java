package jsortie.object.sortearlyexitdetector;

import java.util.Comparator;

import jsortie.object.ObjectRangeSortHelper;

public class WainwrightDetector<T>
  implements ObjectSortEarlyExitDetector<T> {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public boolean isRangeSorted
    ( Comparator<? super T> c
    , T[] vArray, int start, int stop)  {
    int i=start;
    int j=stop-2;
    if (i<=j) {
      do {
        if (c.compare(vArray[i+1],vArray[i])<0) {
          return false;
        }
        ++i;
        if (c.compare(vArray[j+1],vArray[j])<0) {
          return false;
        }
        --j;
      }
      while (i<=j);
    }
    return true;
  }
  public boolean isRangeReverseSorted
    ( Comparator<? super T> c
    , T[] vArray, int start, int stop) {
    int i=start;
    int j=stop-2;
    if (i<=j) {
      do {
        if (c.compare(vArray[i],vArray[i+1])<0) {
          return false;
        }
        ++i;
        if (c.compare(vArray[j],vArray[j+1])<0) {
          return false;
        }
        --j;
      }
      while (i<=j);
    }
    return true;
  }
  @Override
  public boolean exitEarlyIfSorted
    ( Comparator<? super T> c
    , T[] vArray, int start, int stop) {
    if (isRangeSorted(c,vArray,start,stop)) {
      return true;
    } else if (!isRangeReverseSorted
                ( c, vArray,start,stop ) ) {
      return false;
    }
    ObjectRangeSortHelper.reverseRange
      ( vArray, start, stop );
    return true;
  }
  public boolean exitEarlyIfSortedStable
    ( Comparator<? super T> c
    , T[] vArray, int start, int stop) {
    if (isRangeSorted(c, vArray,start,stop)) {
      return true;
    } else if (!isRangeReverseSorted
                ( c, vArray, start, stop ) ) {
      return false;
    }
    ObjectRangeSortHelper.reverseRange
      ( vArray, start, stop );
    int i=start;        
    for (int j=i+1;j<stop;++j) {
      if ( c.compare ( vArray[i], vArray[j] ) < 0 ) {
        if (i<j) {
          ObjectRangeSortHelper.reverseRange
            ( vArray, i, j );
        }
        i=j;
      }
    }
    return true;
  }
}
