package jsortie.object.mergesort;
import java.util.Arrays;
import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;

public class ObjectTimsortWrapper<T>
  implements ObjectRangeSorter<T> {
  public String toString() {
    return "ObjectTimsortWrapper";
  }
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    if (start<stop) {
      int count = stop-start;
      @SuppressWarnings("unchecked")
      T[] copy = (T[]) new Object[count];
      for (int i=0; i<count; ++i) {
        copy[i]=vArray[i+start];
      }
      Arrays.sort(copy, comparator);
      for (int i=0; i<count; ++i) {
        vArray[i+start]=copy[i];
      }
    }
  }
}
