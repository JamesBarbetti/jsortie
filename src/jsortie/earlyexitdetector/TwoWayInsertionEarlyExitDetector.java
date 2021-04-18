package jsortie.earlyexitdetector;

import jsortie.helper.RangeSortHelper;

public class TwoWayInsertionEarlyExitDetector
  implements RangeSortEarlyExitDetector {
  protected int threshold = 50;
  //The logic used here is based on ... 
  //InsertionSort2Way
  //Notes:
  // 1. long-range comparisons between 
  //    vArray[lhs] and vArray[rhs] are *not* counted 
  //    against "local inversions remaining".
  //    This trick makes this almost as good at handling
  //    (entirely or mostly) reverse-order inputs 
  //    as it is at handling  (entirely or mostly) 
  //    in-order inputs.
  // 2. Basing this on InsertionSort2Way means that, 
  //    even when it does poorly, it is (on average) 
  //    "improving the accuracy" of the middle element 
  //    as a "median estimator" (if the middle element is 
  //    to be used as a pivot, hopefully approximating 
  //    the median of the items in the range), because: 
  //    the item at vArray[start + (count/2)] will be the 
  //    median of a sample of the approximately 
  //    Math.sqrt(threshold*8) items around it.
  //
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }  
  @Override
  public boolean exitEarlyIfSorted
    ( int[] vArray, int start, int stop ) {
    if (stop<start+2) return true;
    else if (stop==start+2) {
      RangeSortHelper.compareAndSwapIntoOrder
        ( vArray, start, start+1 );
      return true;
    }
    int count = (stop-start);
    int lhs = start + (count) / 2 - 1;
    int rhs = lhs   + 2;
    int inversions = threshold; 
        //# of long-range inversions "left".
    for (; rhs<stop; --lhs, ++rhs) {
      RangeSortHelper.compareAndSwapIntoOrder
        ( vArray, lhs, rhs );
      int vLeft  = vArray[lhs];
      int vRight = vArray[rhs];
      //Insert on left
      int scan = lhs + 1;
      if ( vArray[scan] < vLeft ) {
        if (inversions<0) {
          return false;
        }
        do {
          vArray[scan-1] = vArray[scan];
          ++scan;
        } while ( vArray[scan] < vLeft );
        vArray[scan-1] = vLeft;
        inversions -= (scan-lhs-1); //limit (added)
      }
      //Insert on right
      scan = rhs - 1;
      if ( vRight < vArray[scan]) {
        if (inversions<0) {
          vArray[rhs] = vRight;
          return false;
        }
        do {
          vArray[scan+1] = vArray[scan];
          --scan;
        } while ( vRight < vArray[scan] );
        vArray[scan+1] = vRight;
        inversions -= (rhs-scan-1); //limit (added)
      }
    }
    if (lhs==start) {
      int v = vArray[start];
      for ( ++lhs; lhs < stop && vArray[lhs] < v 
          ; ++lhs ) {
        vArray[lhs-1] = vArray[lhs];
      }
      vArray[lhs-1] = v;
    }
    return true;
  }
}
