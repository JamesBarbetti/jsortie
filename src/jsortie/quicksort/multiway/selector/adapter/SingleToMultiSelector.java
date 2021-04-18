package jsortie.quicksort.multiway.selector.adapter;

import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.selector.SinglePivotSelector;

public class SingleToMultiSelector 
  implements MultiPivotSelector {
  SinglePivotSelector innerSelector;
  public SingleToMultiSelector
    ( SinglePivotSelector innerSelector ) {
    this.innerSelector = innerSelector;
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + innerSelector.toString() + ")";
  }
  @Override
  public int[] selectPivotIndices
    ( int[] vArray, int start, int stop )  {
    return new int[] { 
      innerSelector.selectPivotIndex
        ( vArray, start, stop ) };
  }
}
