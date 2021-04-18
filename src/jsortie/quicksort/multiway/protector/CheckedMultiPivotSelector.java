package jsortie.quicksort.multiway.protector;

import java.util.Arrays;

import jsortie.exception.SortingFailureException;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;

public class CheckedMultiPivotSelector implements MultiPivotSelector {
  protected MultiPivotSelector innerSelector;
  protected String innerName;
  public CheckedMultiPivotSelector(MultiPivotSelector selectorToUse) {
    this.innerSelector = selectorToUse;
    this.innerName     = selectorToUse.toString();
  }
  
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "(" +innerName + ")";
  }
  
  @Override
  public int[] selectPivotIndices(int[] vArray, int start, int stop) {
    int[] pivots = innerSelector.selectPivotIndices(vArray, start, stop);
    if (pivots==null) {
      throw new SortingFailureException(innerName + ".selectPivotIndices returned null");
    }
    int pivotCount = pivots.length;
    int[] pivotsSorted = Arrays.copyOf(pivots, pivotCount);
    Arrays.sort(pivotsSorted);
    if (pivotsSorted[0]<start) {
      throw	new SortingFailureException
      	( innerName + " returned a pivot (" + pivotsSorted[0] + ") outside"
        + " the requested range (" + start + ".." + (stop-1) + ")");
    }
    if (stop<=pivotsSorted[pivotCount-1]) {
      throw	new SortingFailureException
      	( innerName + " returned a pivot (" + pivotsSorted[pivotCount-1] + ") outside"
        + " the requested range (" + start + ".." + (stop-1) + ")");
    }
    for (int i=1;i<pivotCount;++i) {
      if (pivotsSorted[i]==pivotsSorted[i-1]) {
        throw new SortingFailureException
          ( innerName + " returned at least two copies of the same pivot index"
          + " (" + pivotsSorted[i] + ")");
      }
      if (vArray[pivotsSorted[i]]<vArray[pivotsSorted[i-1]]) {
        throw new SortingFailureException
          ( innerName + " returned pivot indices (" + pivotsSorted[i-1] + "," 
          + " and " + pivotsSorted[i] + ") that referred to out-of-order"
          + " items (" + vArray[pivotsSorted[i-1]]
          + " and " + vArray[pivotsSorted[i]]);
      }
    }
    return pivots;
  }
}
