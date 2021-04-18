package jsortie.quicksort.multiway.selector.dirty;

import jsortie.quicksort.indexselector.PositionalIndexSelector;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.selector.dirty.DirtySelectorHelper;

public class DirtyMultiPivotPositionalSelector 
	extends    PositionalIndexSelector
	implements MultiPivotSelector 
{
  protected DirtySelectorHelper dsh;

  public DirtyMultiPivotPositionalSelector
    (int[] numerators, int denominator) {
    super(numerators, denominator);
    dsh = new DirtySelectorHelper();
  }
	
  public DirtyMultiPivotPositionalSelector
    ( int pivotCount ) {
    super(pivotCount);
    dsh = new DirtySelectorHelper();
  }

  @Override
  public int[] selectPivotIndices
    ( int[] vArray, int start, int stop ) {
    int[] indices
      = selectIndices
        ( start, stop, numerators.length );
    dsh.sortReferencedItems
      ( vArray, indices, 0, indices.length );
    return indices;
  }
}
