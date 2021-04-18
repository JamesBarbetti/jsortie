package jsortie.janitors.exchanging;

import jsortie.RangeSorter;
import jsortie.flags.QuadraticAverageCase;

public class BranchAvoidingBubbleSort
  implements RangeSorter, QuadraticAverageCase {
  @Override
  public String toString() { 
    return this.getClass().getSimpleName(); 
  }
  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    for (;stop>start+1;--stop) {
      for (int i=start, j=i+1; j<stop; ++i, ++j)
      {
        int v1 = vArray[i];
        int v2 = vArray[j];
        vArray[i] = (v1<v2) ? v1 : v2;
        vArray[j] = (v1<v2) ? v2 : v1;
      }
    }
  }
}

