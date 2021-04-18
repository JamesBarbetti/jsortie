package jsortie.object.janitor;

import java.util.Comparator;

import jsortie.janitors.CoprimeGapSequenceHelper;
import jsortie.object.ObjectRangeSorter;

public class ObjectShellSort<T>
  implements ObjectRangeSorter<T>
{
  static final double defaultRatio = 2.2;
  static final int defaultGaps[] 
    = CoprimeGapSequenceHelper
      .coPrimeGapSequence(defaultRatio);
  int    gaps[];
  double ratio;
  public ObjectShellSort() {
    ratio = defaultRatio;
    gaps = defaultGaps;
  }
  public ObjectShellSort(int[] gapsToUse) {
    this.gaps = gapsToUse;
  }
  public ObjectShellSort(double shrinkFactor) {
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
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    int count = stop - start;
    int gapIndex = 0;
    while( gapIndex+1<gaps.length 
           && gaps[gapIndex+1] < count ) {
      ++gapIndex;
    }
    for ( ; 0 <= gapIndex; --gapIndex ) {
      int gap = gaps[gapIndex];
      sortSlicesOfRange
        ( comparator, vArray, start, stop, gap );
    }
    ObjectInsertionSort.insertionSortRange
      ( comparator, vArray, start, stop );
  }
  public void sortSlicesOfRange 
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop, int gap) {
    int i=start+gap;
    for (; i<stop; ++i) {
      T v = vArray[i];
      int h = i-gap;
      for (; start<=h; h-=gap) {
        if ( comparator.compare( vArray[h], v)<=0) {
          break;
        }
        vArray[h+gap] = vArray[h];
      }
      vArray[h+gap] = v;
    }
  }
}
