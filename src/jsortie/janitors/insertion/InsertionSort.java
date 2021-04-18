package jsortie.janitors.insertion;

import jsortie.StableRangeSorter;
import jsortie.flags.QuadraticAverageCase;

public class InsertionSort 
  implements StableRangeSorter, QuadraticAverageCase {
  @Override
  public String toString() { 
    return this.getClass().getSimpleName(); 
  }
  public static void sortSmallRange
    ( int[] vArray, int start, int stop ) {
    for (int place=start+1; place<stop; ++place) {
        int v = vArray[place];
        int scan = place-1;
        for (; start<=scan && v<vArray[scan]; --scan) {
          vArray[scan+1] = vArray[scan];
        }
        vArray[scan+1] = v;
      }
  }
  public static void copyAndSortSmallRange 
    ( int source[], int start, int stop
    , int dest[], int destStart) {
    if (start<stop) {
      int destStop = destStart + (stop-start);
      int offset   = start - destStart;
      dest[destStart] = source[start];
      for (int place=destStart+1; place<destStop; ++place) {
        int v = source[place + offset];
        int scan = place-1;
        for (; destStart<=scan && v<dest[scan]; --scan) {
          dest[scan+1] = dest[scan];
        }
        dest[scan+1] = v;
      }
    }
  }
  public void sortRange(int[] vArray, int start, int stop) {
    for (int place=start+1; place<stop; ++place) {
      int v = vArray[place];
      int scan = place-1;
      for (; start<=scan && v<vArray[scan]; --scan) {
        vArray[scan+1] = vArray[scan];
      }
      vArray[scan+1] = v;
    }
  }
}
