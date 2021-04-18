package jsortie.quicksort.multiway.selector.clean;

import jsortie.quicksort.indexselector.PositionalIndexSelector;

public class CleanMultiPivotPositionalSelector 
  extends PositionalIndexSelector
  implements CleanMultiPivotSelector {
  CleanMultiPivotSelectorHelper helper
    = new CleanMultiPivotSelectorHelper();
  public CleanMultiPivotPositionalSelector 
    ( int[] numerators
    , int denominator) {
    super(numerators, denominator);
    helper = new CleanMultiPivotSelectorHelper();
  }
  public CleanMultiPivotPositionalSelector
    ( int pivotCount ) {
    super (null, pivotCount+1);
    helper = new CleanMultiPivotSelectorHelper();
  }
  @Override
  public String toString() { 
    String s 
    = this.getClass().getSimpleName() + "(";
    if (numerators!=null) {
      for (int i=0; i<numerators.length; ++i) {
        s += ((i>0) ? "," : "") + numerators[i];
      }
      s+= "/";
    }
    s += denominator +")";
    return s;
  }
  @Override
  public int[] selectPivotIndices
    (int[] vArray, int start, int stop) {
    int   pivotCount = getIndexCount();
    int[] indices    = selectIndices
                       ( start, stop, pivotCount);
    helper.sortSample  ( vArray, indices);
    return indices;
  }
}
