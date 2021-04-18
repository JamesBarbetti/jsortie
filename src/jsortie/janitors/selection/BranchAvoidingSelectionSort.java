package jsortie.janitors.selection;

import jsortie.RangeSorter;
import jsortie.flags.QuadraticAverageCase;

public class BranchAvoidingSelectionSort 
  implements RangeSorter, QuadraticAverageCase {
  @Override
  public String toString() { return this.getClass().getSimpleName(); }

  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    for (;start<stop;++start) {
      int lowest = start;
      for (int scan=start+1; scan<stop; ++scan) {
        lowest = (vArray[scan] < vArray[lowest]) ? scan : lowest;
      }
      int vMin      = vArray[lowest];
      vArray[lowest] = vArray[start];
      vArray[start]  = vMin;
    }
  }
}
