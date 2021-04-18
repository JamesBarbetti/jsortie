package jsortie.janitors.insertion;

import jsortie.RangeSorter;
import jsortie.janitors.CoprimeGapSequenceHelper;

public class ShellSort
  implements RangeSorter {
  static final double defaultRatio = 2.2;
  static final int defaultGaps[] 
    = CoprimeGapSequenceHelper
      .coPrimeGapSequence(defaultRatio);
  int    gaps[];
  double ratio;
  public ShellSort() {
    ratio = defaultRatio;
    gaps = defaultGaps;
  }
  public ShellSort(int[] gapsToUse) {
    this.gaps = gapsToUse;
  }
  public ShellSort(double shrinkFactor) {
    ratio = shrinkFactor;
    gaps  = CoprimeGapSequenceHelper
            .coPrimeGapSequence(shrinkFactor);
  }
  @Override public String toString() {
    String name 
      = this.getClass().getSimpleName() + "(";
    for (int i=0; i<gaps.length; ++i) {
      name += ((i>0) ? "," : "") + gaps[i];
    }
    name += ")";
    return name;
  }
  @Override public void sortRange
    ( int[] vArray, int start, int stop) {
    int count = stop - start;
    int gapIndex = 0;
    while( gapIndex+1<gaps.length 
           && gaps[gapIndex+1] < count ) {
      ++gapIndex;
    }
    for ( ; 0 <= gapIndex; --gapIndex ) {
      int gap = gaps[gapIndex];
      sortSlicesOfRange
        ( vArray, start, stop, gap );
    }
    InsertionSort.sortSmallRange
      ( vArray, start, stop );
  }
  public void sortSlicesOfRange 
    ( int[] vArray, int start
    , int stop, int gap) {
    int i=start+gap;
    for (; i<stop; ++i) {
      int v = vArray[i];
      int h = i-gap;
      for (; start<=h; h-=gap) {
        if ( vArray[h]<=v) {
          break;
        }
        vArray[h+gap] = vArray[h];
      }
      vArray[h+gap] = v;
    }
  }
}
