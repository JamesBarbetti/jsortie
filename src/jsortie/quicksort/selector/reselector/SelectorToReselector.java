package jsortie.quicksort.selector.reselector;

import jsortie.quicksort.selector.SinglePivotSelector;

public class SelectorToReselector 
  implements SinglePivotReselector {
  protected SinglePivotSelector selector;
  public String toString() {
    return this.getClass().getSimpleName()
      + "(" + selector.toString() + ")";
  }
  public SelectorToReselector
    (SinglePivotSelector selectorToUse) {
    selector = selectorToUse;
  }
  @Override
  public int selectPivotIndexGivenHint
    ( int[] vArray, int start, int hint, int stop) {
    return selector.selectPivotIndex(vArray, start, stop);
  }
}
