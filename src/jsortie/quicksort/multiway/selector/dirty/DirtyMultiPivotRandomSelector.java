package jsortie.quicksort.multiway.selector.dirty;

import java.util.Arrays;

import jsortie.quicksort.indexselector.RandomIndexSelector;
import jsortie.quicksort.indexselector.UniformPositionalIndexSelector;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.selector.clean.CleanIndexSortingHelper;
import jsortie.quicksort.selector.dirty.DirtySelectorHelper;

public class DirtyMultiPivotRandomSelector 
  extends    RandomIndexSelector
  implements MultiPivotSelector  {
  protected int                 c; //sample count
  protected int                 p;
  protected boolean             sortItems; 
  //true  to sort the subset of items referenced by 
  //      the indices
  //false to sort the indices according to the values of
  //      the items they point to
  //
  protected DirtySelectorHelper            dsh;
  protected CleanIndexSortingHelper                    indexSorter;
  protected UniformPositionalIndexSelector upis;
  public DirtyMultiPivotRandomSelector
    ( int sampleSize, int pivotCount, boolean isToSortItems ) {
    c              = sampleSize;
    p              = pivotCount;
    this.sortItems = isToSortItems;
    dsh            = new DirtySelectorHelper();    
    indexSorter    = new CleanIndexSortingHelper();
    upis           = new UniformPositionalIndexSelector();
  }
  public int getSampleSize(int count) {
    return ( c < count ) ? c : count;
  }
  @Override
  public int[] selectPivotIndices
    ( int[] vArray, int start, int stop ) {
    int[] indices = super.selectIndices(start, stop, getSampleSize(stop-start));
    if ( sortItems ) {
      Arrays.sort(indices);
      dsh.sortReferencedItems( vArray, indices, 0, indices.length );
    } else {
      indexSorter.sortIndices( vArray, indices);
    }
    int[] pivots = upis.selectIndices(0, indices.length, p);
    for (int i=0; i<pivots.length; ++i) {
      pivots[i] = indices[pivots[i]];
    }
    return pivots;
  }
}
