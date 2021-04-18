package jsortie.quicksort.selector.clean;

import jsortie.quicksort.indexselector.RandomIndexSelector;

public class CleanRandomSampleSelector 
  implements CleanSinglePivotSelector {
  protected RandomIndexSelector indexSelector 
    = new RandomIndexSelector();
  protected CleanIndexSortingHelper indexSorter 
    = new CleanIndexSortingHelper();	
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public int getSampleSize(int count) {
    int size = (int) Math.floor( Math.sqrt(count));
    //if sample size is even, add one, to ensure 
    //the sample size is odd, so the sample will 
    //have a median item (rather than a median that 
    //is the average of the values of two items).
    return size + 1 - (size&1); 
  }
  @Override
  public int selectPivotIndex
    ( int[] vArray, int start, int stop )  {
    int[] indices 
      = indexSelector.selectIndices
        ( start, stop, getSampleSize(stop-start) );
    indexSorter.sortIndices( vArray, indices );
    return indices[indices.length/2];		
  }
}
