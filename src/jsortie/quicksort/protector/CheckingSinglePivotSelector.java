package jsortie.quicksort.protector;

import jsortie.quicksort.selector.SinglePivotSelector;

public class CheckingSinglePivotSelector implements SinglePivotSelector
{
  protected SinglePivotSelector innerSelector;
	
  public CheckingSinglePivotSelector(SinglePivotSelector innerSelector) {
    this.innerSelector = innerSelector;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "(" + innerSelector.toString() + ")";
  }
  
  @Override
  public int selectPivotIndex(int[] vArray, int start, int stop) {
    if (vArray==null) {
      throw new NullPointerException("null array passed to selectPivotIndex");
    }
    if (start < 0) {
      throw new IndexOutOfBoundsException("start index (" + start 
                + ") passed to selectPivotIndex was less than zero");
    }
    if (vArray.length <= start) {
      throw new IndexOutOfBoundsException("start index (" + start 
				+ ") passed to selectPivotIndex was greater than the length of the array (" + vArray.length + ")");
    }
    if (vArray.length < stop) {
      throw new IndexOutOfBoundsException("stop index (" + stop 
				+ ") passed to selectPivotIndex was greater than the length of the array (" + vArray.length + ")");
    }
    return innerSelector.selectPivotIndex(vArray, start, stop);
  }
}