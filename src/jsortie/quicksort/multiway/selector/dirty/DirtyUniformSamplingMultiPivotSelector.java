package jsortie.quicksort.multiway.selector.dirty;

import jsortie.quicksort.multiway.selector.MultiPivotSelector;

public class DirtyUniformSamplingMultiPivotSelector 
  implements MultiPivotSelector {
  protected MultiPivotSelector inner;
  protected int                p;
  public DirtyUniformSamplingMultiPivotSelector
    ( MultiPivotSelector innerSelector, int pivotCount ) {
    inner = innerSelector;
    p     = pivotCount;
  }
  public DirtyUniformSamplingMultiPivotSelector
  ( int sampleSize, int pivotCount ) {
	inner = new DirtyMultiPivotPositionalSelector(sampleSize);
	p     = pivotCount;
  }
  @Override
  public int[] selectPivotIndices(int[] vArray, int start, int stop) {
    int[]  sample    = inner.selectPivotIndices(vArray, start, stop);
    int[]  pivots    = new int[p];
    double step = (double)( sample.length + 1 ) / (double)( p + 1 );
    int    w    = 0;
    for (double r=step+.5;r<sample.length+1;r+=step) {
      pivots[w] = sample[(int)Math.floor(r)];
    }
    return pivots;
  }
}
