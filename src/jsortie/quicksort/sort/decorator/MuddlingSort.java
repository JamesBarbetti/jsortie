package jsortie.quicksort.sort.decorator;

import jsortie.RangeSorter;

public class MuddlingSort 
  implements RangeSorter   {
  private RangeSorter innerSort;
  public MuddlingSort
  ( RangeSorter sortThatWantsScatteredInput) {
    innerSort = sortThatWantsScatteredInput;
  }			
  @Override
  public String toString() {
    return this.getClass().getSimpleName()
           + "(" + innerSort.toString() + ")";
  }
  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    muddle(vArray, start, stop);
    innerSort.sortRange(vArray,  start, stop);
  }
  public static void muddle(int[] vArray, int start, int stop) {
    //exchange the next even item with one of the "middlingest"
    //remaining odd items, until every even item has been
    //exchanged.
    double halfCount = (stop-start)/2.0;
    double halfOdd  = 0;
    double oddStep  = (stop-start)/4.0;
    for (int even = start; even < stop; ++even) {
      halfOdd += oddStep;
      if (halfCount<=halfOdd) {
        oddStep /= 2.0;
        halfOdd  =  oddStep;
      }
      int odd      = start + 2 * (int) Math.floor(halfOdd) - 1;
      if (start<odd) {
        int v        = vArray[odd];
        vArray[odd]  = vArray[even];
        vArray[even] = v;
      }
    }
  }
}
