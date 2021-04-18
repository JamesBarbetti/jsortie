package jsortie.janitors.exchanging;

import jsortie.RangeSorter;
import jsortie.janitors.CoprimeGapSequenceHelper;
import jsortie.janitors.insertion.InsertionSort;

public class AlternatingCombsort
  implements RangeSorter {
  @Override
  public String toString() { 
    return this.getClass().getSimpleName(); 
  }
  static int defaultGaps [] 
    = CoprimeGapSequenceHelper
      .coPrimeGapSequence(1.4); 
  protected int[]       gaps;
  protected RangeSorter insertionSort;
  public AlternatingCombsort() {
    gaps = defaultGaps;
    insertionSort = new InsertionSort();
  }
  public AlternatingCombsort(double ratio) {
    if (ratio <= 1.0) {
      throw new IllegalArgumentException
        ( "Growth ratio for AlternatingCombsort"
        + " (" + ratio + ") too low: must be 1.0 or more");
    }
    gaps = CoprimeGapSequenceHelper
           .coPrimeGapSequence(ratio);
    insertionSort = new InsertionSort();
  }
  public AlternatingCombsort(int[] gapsToUse) {
    gaps = gapsToUse;
  }
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    int count = stop - start;
    if ( 0 < gaps.length && 1 < count ) {
      int gapIndex = 0;
      boolean rightToLeft = false;
      while( gapIndex+1<gaps.length 
             && gaps[gapIndex+1] < count ) {
        ++gapIndex;
      }
      for ( ; 0 <= gapIndex; --gapIndex ) {
        int gap = gaps[gapIndex];
        if (rightToLeft) {
          rightToLeftCombsortPass
            ( vArray, start, stop, gap );
        } else {
          leftToRightCombsortPass
            ( vArray, start, stop, gap );
        }	
        rightToLeft = !rightToLeft;
      }
    }
    insertionSort.sortRange
      ( vArray, start, stop );
  }
  public void leftToRightCombsortPass
    ( int[] vArray, int start, int stop, int gap) {
    int i = start;
    int j = start+gap;
    for (; j<stop; ++i, ++j) {
      if (vArray[j]<vArray[i]) {
        int vTemp = vArray[i];
        vArray[i] = vArray[j];
        vArray[j] = vTemp;
      }
    }
  }
  public void rightToLeftCombsortPass
    ( int[] vArray, int start, int stop, int gap) {
    int i = stop-1-gap;
    int j = stop-1;
    for ( ; start<=i; --i, --j) {
      if (vArray[j]<vArray[i]) {
        int vTemp = vArray[i];
        vArray[i] = vArray[j];
        vArray[j] = vTemp;
      }
    }
  }
}
