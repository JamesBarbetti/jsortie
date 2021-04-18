package jsortie.quicksort.selector.simple;

import jsortie.quicksort.selector.clean.CleanSinglePivotSelector;

public class CleanPositionalSelector 
  implements CleanSinglePivotSelector {
  double fractionalRank;
  public CleanPositionalSelector
    ( int numerator, int denominator ) {
    if (denominator<=0) {
      throw new IllegalArgumentException
        ( "Cannot supply a denominator (" 
          + denominator + ") less than 1");
    }
    if (denominator<=numerator) {
      throw new IllegalArgumentException
        ( "Cannot supply a denominator (" 
          + denominator + ") less than the"
          + " numerator (" + numerator + ")");
    }
    this.fractionalRank = (double)numerator
                        / (double)denominator;
  }
  @Override
  public String toString() { 
    return this.getClass().getSimpleName()
           + "(" + fractionalRank +")"; 
  }
  @Override
  public int selectPivotIndex
    ( int[] vArray, int start, int stop ) {
	double count = stop - start;
    return start 
      + (int) Math.floor ( count * fractionalRank);
  }
}
