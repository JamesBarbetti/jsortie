package jsortie.object.quicksort.selector.reselector;

import java.util.Comparator;

import jsortie.object.quicksort.selector.SinglePivotObjectSelector;

public class ObjectSelectorToReselector<T>
  implements SinglePivotObjectReselector<T> {
  protected SinglePivotObjectSelector<T> selector;
  public String toString() {
    return this.getClass().getSimpleName()
      + "(" + selector.toString() + ")";
  }
  public ObjectSelectorToReselector
    (SinglePivotObjectSelector<T> selectorToUse) {
    selector = selectorToUse;
  }
  @Override
  public int selectPivotIndexGivenHint
    ( Comparator<? super T> comparator
    , T[] vArray
    , int start, int hint, int stop) {
    return selector.selectPivotIndex
           ( comparator, vArray, start, stop );
  }

}
