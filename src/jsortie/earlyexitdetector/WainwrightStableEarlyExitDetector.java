package jsortie.earlyexitdetector;

import jsortie.helper.RangeSortHelper;

public class WainwrightStableEarlyExitDetector
  extends WainwrightEarlyExitDetector
  implements StableRangeSortEarlyExitDetector {
  public boolean exitEarlyIfSortedStable
         (int[] vArray, int start, int stop) {
    if (isRangeSorted(vArray,start,stop)) {
      return true;
    } else if (!isRangeReverseSorted
                ( vArray,start,stop ) ) {
      return false;
    }
    RangeSortHelper.reverseRange
    ( vArray, start, stop );
    int i=start;        
    for (int j=i+1;j<stop;++j) {
      if (vArray[i]<vArray[j]) {
        if (i<j) {
          RangeSortHelper.reverseRange
          ( vArray, i, j );
        }
        i=j;
      }
    }
    return true;
  }

}
