package jsortie.object.janitor;

import java.util.Comparator;

import jsortie.janitors.CoprimeGapSequenceHelper;

public class ObjectAlternatingCombsort<T>
  extends ObjectInsertionSort<T> {
  //This is a port of BranchAvoidingAlternatingCombsort
  //(and the stuff in its super-classes that it needs,
  // copied over too).
  static final int defaultGaps [] 
      = CoprimeGapSequenceHelper
        .coPrimeGapSequence(1.44); 
  protected int[] gaps;
  public ObjectAlternatingCombsort() {
    gaps = defaultGaps;
  }
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    int count = stop - start;
    int gapIndex = 0;
    boolean rightToLeft = false;
    while ( gapIndex+1<gaps.length
            && gaps[gapIndex+1] < count ) {
      ++gapIndex;
    }
    for ( ; 0 <= gapIndex; --gapIndex ) {
      int gap = gaps[gapIndex];
      if (rightToLeft) {
        rightToLeftCombsortPass
          ( comparator, vArray
          , start, stop, gap );
      } else {
        leftToRightCombsortPass
          ( comparator, vArray
          , start, stop, gap );
      }	
      rightToLeft = !rightToLeft;
    }
    super.sortRange
      ( comparator, vArray, start, stop );
  }
  public void leftToRightCombsortPass
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int gap) {
    for ( int i=start, j=start+gap
        ; j<stop; ++i, ++j) {
      T       vLeft  = vArray[i];
      T       vRight = vArray[j];
      boolean swap   = comparator.compare
                       ( vRight, vLeft ) < 0;
      vArray[i]      = swap ? vRight : vLeft; 
      vArray[j]      = swap ? vLeft  : vRight; 
    }
  }
  public void rightToLeftCombsortPass 
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int gap) {
    for ( int i = stop - 1 - gap, j = stop - 1
        ; start<=i; --i, --j) {
      T       vLeft  = vArray[i];
      T       vRight = vArray[j];
      boolean swap   = comparator.compare
                       ( vRight, vLeft ) < 0;
      vArray[i]      = swap ? vRight : vLeft; 
      vArray[j]      = swap ? vLeft  : vRight; 
    }
  }
}
