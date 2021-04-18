package jsortie.quicksort.multiway.selector.dirty;

import jsortie.quicksort.indexselector.UniformPositionalIndexSelector;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.selector.dirty.DirtySelectorHelper;

public class DirtyUniformSliceSelector 
  implements MultiPivotSelector {
  protected int                            c;
  protected int                            p;
  protected DirtySelectorHelper            helper;
  protected UniformPositionalIndexSelector upis;
  
  public DirtyUniformSliceSelector
    ( int sampleCount, int pivotCount ) {
    c = sampleCount;
    p = pivotCount;
    helper = new DirtySelectorHelper();
    upis   = new UniformPositionalIndexSelector();
  }
  public int getSampleSize(int count) {
    return (count<p) ? count : p;
  }
  
  @Override
  public int[] selectPivotIndices
    ( int[] vArray, int start, int stop ) {
    int count      = stop-start;
    int sampleSize = getSampleSize(count); //desired 
    int step       = (count+1)/(sampleSize+1);
    if (step<1) step=1; else start+=step;
    helper.heapSortSlice(vArray, start, stop, step);
    sampleSize = ( stop - start ) / step; //actual 
    int[] pivots = upis.selectIndices(0, sampleSize, p);
    for (int i=0; i<pivots.length; ++i) {
      pivots[i] = start + i*step;
    }
    return pivots;
  }
}
