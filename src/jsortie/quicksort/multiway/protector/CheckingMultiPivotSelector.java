package jsortie.quicksort.multiway.protector;

import jsortie.quicksort.multiway.selector.MultiPivotSelector;

public class CheckingMultiPivotSelector implements MultiPivotSelector {
  protected MultiPivotSelector innerSelector;
  public CheckingMultiPivotSelector(MultiPivotSelector selector) {
    innerSelector = selector;
  }
  @Override
  public int[] selectPivotIndices(int[] vArray, int start, int stop) {
    if (vArray==null)
      throw new IllegalArgumentException("array cannot be null");
    if (start<0)
      throw new IndexOutOfBoundsException("start (" + start + ") is less than 0");
    if (vArray.length<stop)
      throw new IndexOutOfBoundsException("stop (" + start + ")"
    		  + " is >= the length (" + vArray.length + ") of the array");
    return innerSelector.selectPivotIndices(vArray, start, stop);
  }
}
