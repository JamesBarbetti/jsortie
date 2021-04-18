package jsortie.quicksort.selector.clean;

import jsortie.quicksort.indexselector.IndexSelector;
import jsortie.quicksort.selector.SinglePivotSelector;

public class CleanCompoundIndexingSelector 
  implements SinglePivotSelector {
  protected IndexSelector indexSelector;
  protected CleanIndexSortingHelper 
    indexSorter = new CleanIndexSortingHelper();
  public CleanCompoundIndexingSelector
    ( IndexSelector iSelector ) {
    indexSelector = iSelector;
  }
  @Override  
  public String toString() {
    return this.getClass().getSimpleName()
    + "(" + indexSelector.toString() + ")";
  }
  public int getCandidateCount(int count) {
    int c = (int) Math.floor( Math.sqrt ( count ));
    //make c odd (by adding 1, if c is even)
    return c + ( 1 - ( c & 1 ) ); 
  }
  @Override
  public int selectPivotIndex(int[] vArray, int start, int stop) {
    int c = getCandidateCount(stop-start);
    if (stop-start <= c) {
      return start + (stop-start)/2;
    }
    int indices[] = indexSelector.selectIndices(start, stop, c);
    indexSorter.mergesortIndices(vArray, indices);
    return indices[ c / 2];
  }
}
