package jsortie.janitors.insertion;

import jsortie.StableRangeSorter;
import jsortie.flags.QuadraticAverageCase;

public class SentinelInsertionSort 
  implements StableRangeSorter, QuadraticAverageCase {
  @Override
  public String toString() {
    return this.getClass().getSimpleName(); 
  }
  public static void bubbleMinimumToLeft
    ( int[] vArray, int start, int stop ) {
    if (start+1<stop) {
      int scan;
      for (scan=stop-1; scan>start && vArray[scan-1] < vArray[scan]; --scan);
      int v = vArray[scan];
      for (--scan; scan>=start; --scan) {
        if (vArray[scan]<v) {
          vArray[scan+1] = v;
          v = vArray[scan];
        } else {
          vArray[scan+1] = vArray[scan];
        }
      }
      vArray[start] = v;	  
    }
  }
  public static void bubbleMaximumToRight
    ( int[] vArray, int start, int stop ) {
    int stopLess1 = stop-1;
    if (start<stopLess1) {
      int scan;
      for (scan=start; scan<stopLess1 && vArray[scan] < vArray[scan+1]; ++scan);
      int v = vArray[scan];
      for (++scan; scan<stop; ++scan) {
        if (vArray[scan]<v) {
          vArray[scan-1] = v;
          v = vArray[scan];
        } else {
          vArray[scan-1] = vArray[scan];
        }
      }
      vArray[stopLess1] = v;
    }
  }
  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    bubbleMinimumToLeft(vArray, start, stop);
    for (int rhs = start+2; rhs<stop; ++rhs) {
      int v = vArray[rhs];
      int scan;
      for ( scan=rhs-1 ; v<vArray[scan]; --scan) {
        vArray[scan+1] = vArray[scan];
      }
      vArray[scan+1] = v;
    }
  }
}
