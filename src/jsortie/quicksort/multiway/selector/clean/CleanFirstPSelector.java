package jsortie.quicksort.multiway.selector.clean;

public class CleanFirstPSelector 
  implements CleanMultiPivotSelector {
  CleanMultiPivotSelectorHelper helper
    = new CleanMultiPivotSelectorHelper();
  int p;
  public CleanFirstPSelector(int pivots) {
    if (pivots<1) {
      throw new IllegalArgumentException
        ( "CleanFirstPSelector, pivots must be"
        + " positive, but was " + pivots );
    }
    this.p = pivots;
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int[] selectPivotIndices 
    ( int[] vArray, int start, int stop ) {
    int indices[] = new int[p];
    for (int i=0; i<p; ++i) {
      indices[i] = start+i;
    }
    helper.sortSample
      ( vArray, indices );
    return indices;
  }
}
