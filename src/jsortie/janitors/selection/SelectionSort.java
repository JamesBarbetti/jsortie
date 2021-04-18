package jsortie.janitors.selection;

import jsortie.RangeSorter;
import jsortie.flags.QuadraticAverageCase;

public class SelectionSort implements RangeSorter, QuadraticAverageCase {
  @Override
  public String toString() { return this.getClass().getSimpleName(); }

  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    for (;start<stop;++start) {
      int lowest = start;
      int vMin   = vArray[start];
      for (int scan=start+1; scan<stop; ++scan) {
        if ( vArray[scan]<vMin ) {
          vMin   = vArray[scan];
          lowest = scan;
        }
      }
      vArray[lowest] = vArray[start];
      vArray[start]  = vMin;
    }
  }
}
