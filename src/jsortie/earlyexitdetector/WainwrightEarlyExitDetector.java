package jsortie.earlyexitdetector;

import jsortie.helper.RangeSortHelper;

public class WainwrightEarlyExitDetector
  implements RangeSortEarlyExitDetector {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public boolean isRangeSorted
    ( int[] vArray, int start, int stop ) {
    int i=start;
    int j=stop-2;
    if (i<=j) {
      do {
        if (vArray[i+1]<vArray[i]) {
          return false;
        }
        ++i;
        if (vArray[j+1]<vArray[j]) {
          return false;
        }
        --j;
      }
      while (i<=j);
    }
    return true;
  }
  public boolean isRangeReverseSorted
    ( int[] vArray, int start, int stop ) {
    int i=start;
    int j=stop-2;
    if (i<=j) {
      do {
        if (vArray[i]<vArray[i+1]) {
          return false;
        }
        ++i;
        if (vArray[j]<vArray[j+1]) {
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
    ( int[] vArray, int start, int stop) {
    if (isRangeSorted(vArray,start,stop)) {
      return true;
    } else if (!isRangeReverseSorted
                ( vArray,start,stop ) ) {
      return false;
    }
    RangeSortHelper.reverseRange
    ( vArray, start, stop );
    return true;
  }
}
