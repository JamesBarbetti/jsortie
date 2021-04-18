package jsortie.quicksort.selector.dirty;

import jsortie.quicksort.indexselector.IndexSelector;
import jsortie.quicksort.indexselector.PositionalIndexSelector;
import jsortie.quicksort.selector.SinglePivotSelector;

public class LessDirtyCompoundSelector implements SinglePivotSelector {
  protected IndexSelector       innerSelector;
  protected DirtySelectorHelper dsh = new DirtySelectorHelper();
	
  public LessDirtyCompoundSelector() {
    innerSelector = new PositionalIndexSelector();
  }

  public LessDirtyCompoundSelector(IndexSelector indexSelector) {
    innerSelector = indexSelector;
  }	
	
  protected int getSampleSize(int partitionSize) {
    int size = (int)Math.sqrt(partitionSize);
    if (size<1) size=1;
    return size;
  }
	
  @Override
  public String toString() { 
    return this.getClass().getSimpleName() + "( " + innerSelector.toString() + ")";
  }

  @Override
  public int selectPivotIndex(int[] vArray, int start, int stop) {
    int indices[] = innerSelector.selectIndices(start, stop, getSampleSize(stop-start));
    dsh.sortReferencedItems(vArray, indices, 0, indices.length);
    return indices[indices.length / 2];
  }
}
