package jsortie.quicksort.multiway.selector.dirty;

import jsortie.janitors.insertion.InsertionSort;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;

public class DirtyFirstPSelector 
  implements MultiPivotSelector {
  int p;
  public DirtyFirstPSelector ( int pivots ) {
    if (pivots<1) {
      throw new IllegalArgumentException
        ( "DirtyFirstPSelector, pivots must be"
        + " positive, but was " + pivots );
    }
    this.p = pivots;
  }
  @Override
  public int[] selectPivotIndices
    ( int[] vArray, int start, int stop ) {
    int pToUse = p;
    if ((stop-start) < p ) {
      pToUse = stop - start;
    }
    int indices[] = new int[pToUse];
    for (int i=0; i<pToUse; ++i) {
      indices[i] = start+i;
    }
    InsertionSort.sortSmallRange
      ( vArray,  start, start + pToUse );
    return indices;
  }
}
