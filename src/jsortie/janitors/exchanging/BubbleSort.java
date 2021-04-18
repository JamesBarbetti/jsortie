package jsortie.janitors.exchanging;

import jsortie.StableRangeSorter;
import jsortie.flags.QuadraticAverageCase;

public class BubbleSort implements StableRangeSorter, QuadraticAverageCase {
  @Override
  public String toString() { return this.getClass().getSimpleName(); }

  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    while (stop>start) {
      int v = vArray[start];
      for (int scan=start+1; scan<stop; ++scan) {
        if (vArray[scan]<v) {
          vArray[scan-1] = vArray[scan];
        } else {
          vArray[scan-1] = v;
          v = vArray[scan];
        }
      }
      --stop;
      vArray[stop] = v;
    }
  }
}
