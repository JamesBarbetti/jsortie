package jsortie.mergesort.vanilla;

import jsortie.RangeSorter;

public class Urgesort 
  implements RangeSorter {
  RangeSorter smallSort;
  int         threshold;
  public Urgesort
    ( RangeSorter janitor, int janitorThreshold ) {
    smallSort = janitor;
    threshold = janitorThreshold;
  }
  static public void sortRangeToMain
    ( int[] vAux, int start, int stop
    , int[] vMain, int offset
    , RangeSorter smallSort, int threshold) {
    int count = stop - start;
    if ( count <= threshold) {
      smallSort.sortRange(vMain, start, stop);
    } else {
      int leftCount  = count>>1;
      int middle     = start + leftCount;
      sortRangeToAux ( vMain, start,  middle
                     , vAux, offset, smallSort, threshold);
      sortRangeToAux ( vMain, middle, stop
                     , vAux, offset, smallSort, threshold);
      merge          ( vAux, start+offset, middle+offset, true
                     , middle+offset, stop+offset, true, vMain, start);
    }
  }
  public static void sortRangeToAux
    ( int[] vMain, int start, int stop
    , int[] vAux, int offset
    , RangeSorter smallSort, int threshold) {
    int count      = stop - start;
    int leftCount  = count>>1;
    int middle     = start + leftCount;
    sortRangeToMain ( vAux, start,  middle
                    , vMain, offset, smallSort, threshold);
    sortRangeToMain ( vAux, middle, stop
                    ,   vMain, offset, smallSort, threshold);
    merge ( vMain, start, middle, true
          , middle, stop, true, vAux, start+offset);
  }
  public static int merge 
    ( int vSource[] 
    , int leftStart,  int leftStop,  boolean flushLeft
    , int rightStart, int rightStop, boolean flushRight
    , int vDest[],    int w) {
    if (leftStart<leftStop && rightStart<rightStop) {
      do {
        int leftMiddle  = leftStart  + (( leftStop  - leftStart  )/2);
        int rightMiddle = rightStart + (( rightStop - rightStart )/2);
        if ( vSource[leftMiddle] <= vSource[rightMiddle] ) {
          int offset = rightStart - w + leftStart - leftMiddle - 1;
          w = merge ( vSource, leftStart, leftMiddle+1, true
                    , rightStart, rightMiddle, false, vDest, w);
          rightStart = w + offset;
          leftStart  = leftMiddle+1;
        } else {
          int offset = leftStart - w + rightStart - rightMiddle - 1;
          w = merge ( vSource, leftStart, leftMiddle, false
                    , rightStart, rightMiddle+1, true, vDest, w);
          leftStart  = w + offset;
          rightStart = rightMiddle+1;
        }
      } while (leftStart<leftStop && rightStart<rightStop);
    }
    if (flushLeft && leftStart<leftStop) {
      do {
        vDest[w]=vSource[leftStart];
        ++w;
        ++leftStart;
      } while (leftStart<leftStop);
    }
    if (flushRight && rightStart<rightStop) {
      do {
        vDest[w]=vSource[rightStart];
        ++rightStart;
        ++w;
      } while (rightStart<rightStop);
    }
    return w;
  }  
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    int[] vAux   = new int[stop-start];
    sortRangeToMain ( vAux, start, stop
                    , vArray, start
                    , smallSort, threshold);  
  }
}
