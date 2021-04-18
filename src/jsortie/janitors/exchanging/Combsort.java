package jsortie.janitors.exchanging;

import jsortie.RangeSorter;
import jsortie.janitors.CoprimeGapSequenceHelper;
import jsortie.janitors.insertion.InsertionSort;

public class Combsort
  implements RangeSorter {
  protected int[]       gaps;
  protected RangeSorter insertionSort;
  @Override public String toString() {
    return this.getClass().getSimpleName(); 
  }
  public Combsort() {
    gaps          = CoprimeGapSequenceHelper
                    .coPrimeGapSequence(4.0/3.0);
    insertionSort = new InsertionSort();
  }
  public Combsort(double ratio) {
    if (ratio <= 1.0) {
      throw new IllegalArgumentException
        ( "Growth ratio for Combsort"
        + " (" + ratio + ") too low:" 
        + " must be 1.0 or more");
    }
    gaps          = CoprimeGapSequenceHelper
                    .coPrimeGapSequence(ratio);
    insertionSort = new InsertionSort();
  }
  @Override 
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    int count    = stop - start;
    int gapIndex = 0;
    while ( gapIndex+1<gaps.length 
            && gaps[gapIndex+1] < count ) {
      ++gapIndex;
    }
    for ( ; 0 <= gapIndex; --gapIndex ) {
      int gap = gaps[gapIndex];
      int i   = start;
      int j   = start+gap;
      for (; j<stop; ++i, ++j) {
        if ( vArray[j] < vArray[i] ) {
          int vTemp = vArray[i];
          vArray[i]  = vArray[j];
          vArray[j]  = vTemp;
        }
      }
    }
    insertionSort.sortRange
      ( vArray, start, stop );
  }
}
