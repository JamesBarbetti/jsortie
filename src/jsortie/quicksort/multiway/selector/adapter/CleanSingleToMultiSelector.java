package jsortie.quicksort.multiway.selector.adapter;

import jsortie.quicksort.multiway.selector.clean.CleanMultiPivotSelector;
import jsortie.quicksort.selector.clean.CleanSinglePivotSelector;

public class CleanSingleToMultiSelector implements CleanMultiPivotSelector {
  CleanSinglePivotSelector inner;
  public CleanSingleToMultiSelector(CleanSinglePivotSelector innerSelector) {
    inner = innerSelector;
  }
  @Override
  public int[] selectPivotIndices(int[] vArray, int start, int stop) {
    return new int[] { inner.selectPivotIndex(vArray, start, stop) };
  }
}
