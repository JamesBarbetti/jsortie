package jsortie.object.quicksort.protector;

import java.util.Comparator;

import jsortie.object.quicksort.selector.SinglePivotObjectSelector;

public class CheckedPivotObjectSelector<T>
  implements SinglePivotObjectSelector<T> {
  SinglePivotObjectSelector<T> innerSelector;
  public CheckedPivotObjectSelector
    ( SinglePivotObjectSelector<T> selector ) {
    innerSelector = selector;
  }
  @Override
  public int selectPivotIndex
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop ) {
      int pivotIndex 
        = innerSelector.selectPivotIndex
          ( comparator, vArray, start, stop );
    if ( pivotIndex < start 
         || ( stop <= pivotIndex && start<stop)) {
      throw new IndexOutOfBoundsException 
        ( innerSelector.toString() + ".selectPivotIndex"
          + " returned a pivot index (" + pivotIndex
          + ") outside of the legal range"
          + " (" + start + " to " + (stop-1) + " inclusive)");
    }
    return pivotIndex;
  }
}
