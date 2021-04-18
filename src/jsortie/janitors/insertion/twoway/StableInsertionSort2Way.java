package jsortie.janitors.insertion.twoway;

import jsortie.StableRangeSorter;

public class StableInsertionSort2Way 
  extends InsertionSort2Way
  implements StableRangeSorter {
  public static void sortSmallRange
  ( int[] vArray, int start, int stop ) {
    if (stop<start+2) return;
    int count = (stop-start);
    int lhs = start + (count) / 2 - 1;
    int rhs = lhs + 1 + (count&1);
    if (start<=lhs) do {
      int vFromLeft = vArray[lhs];
      int vFromRight = vArray[rhs];
      int scan;
      if ( vFromLeft <= vFromRight) {
        for (scan=lhs+1; vArray[scan]<vFromLeft; ++scan) {
          vArray[scan-1] = vArray[scan];
        }
        vArray[scan-1] = vFromLeft;
        for (scan=rhs-1; vFromRight<vArray[scan]; --scan) {
          vArray[scan+1] = vArray[scan];
        }
        vArray[scan+1] = vFromRight;
      } else {
        for (scan=lhs+1; vArray[scan]<=vFromRight; ++scan) {
          vArray[scan-1] = vArray[scan];
        }
        vArray[scan-1] = vFromRight;
        for (scan=rhs-1; vFromLeft<=vArray[scan]; --scan) {
          vArray[scan+1] = vArray[scan];
        }
        vArray[scan+1] = vFromLeft;
      }
      --lhs;
      ++rhs;
    } while (start<=lhs);
  }
}
