package jsortie.quicksort.protector;

import jsortie.quicksort.selector.SinglePivotSelector;

public class CheckedSinglePivotSelector 
  implements SinglePivotSelector {
  protected SinglePivotSelector innerSelector;
	
  public CheckedSinglePivotSelector 
    ( SinglePivotSelector innerSelector) {
    this.innerSelector = innerSelector;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + innerSelector.toString() + ")";
  }	

  @Override
  public int selectPivotIndex(int[] vArray, int start, int stop) {
    int pivotIndex = innerSelector.selectPivotIndex(vArray, start, stop);
    if ( pivotIndex < start || ( stop <= pivotIndex && start<stop)) {
      throw new IndexOutOfBoundsException 
        ( innerSelector.toString() + ".selectPivotIndex returned a pivot index (" + pivotIndex
          + ") outside of the legal range (" + start + " to " + (stop-1) + " inclusive)");
    }
    return pivotIndex;
  }
}
