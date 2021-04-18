package jsortie.janitors.insertion.twoway;

import jsortie.RangeSorter;
import jsortie.flags.QuadraticAverageCase;

public class InsertionSort2Way 
  implements RangeSorter, QuadraticAverageCase {
  @Override
  public String toString() {
    return this.getClass().getSimpleName(); 
  }
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    sortSmallRange(vArray, start, stop);
  }
  public static void sortSmallRange
    ( int[] vArray, int start, int stop ) {
    if (stop<start+2) return;
    int count = (stop-start);
    int lhs = start + (count) / 2 - 1;
    int rhs = lhs + 1 + (count&1);
    if (start<=lhs) do {
      int vLeft;
      int vRight;
      if ( vArray[lhs]<=vArray[rhs]) {
        vLeft  = vArray[lhs];
        vRight = vArray[rhs];
      } else {
        vLeft  = vArray[rhs];
        vRight = vArray[lhs];
      }
      //Insert on left
      int scan;
      scan = lhs +1;
      for (; vArray[scan]<vLeft; ++scan) {
        vArray[scan-1] = vArray[scan];
      }
      vArray[scan-1] = vLeft;
      --lhs;
      //Insert on right
      scan = rhs-1;
      for (; vRight<vArray[scan]; --scan) {
        vArray[scan+1] = vArray[scan];
      }
      vArray[scan+1] = vRight;
      ++rhs;
    } while (start<=lhs);
  }
}
