package jsortie.object.quicksort.multiway.selector;

import java.util.Comparator;

import jsortie.object.quicksort.selector.SinglePivotObjectSelector;

public class SingleToMultiObjectSelector<T> 
  implements MultiPivotObjectSelector<T> {
  protected  SinglePivotObjectSelector<T> inner;
  @Override
  public String toString() {
    return inner.toString();
  }
  public SingleToMultiObjectSelector
    ( SinglePivotObjectSelector<T> singlePivotSelector ) {
    inner = singlePivotSelector;
  }
  @Override
  public int[] selectPivotIndices
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    return new int[] { 
        inner.selectPivotIndex
        (comparator, vArray, start, stop) };
  }
}
