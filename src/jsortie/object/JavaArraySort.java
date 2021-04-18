package jsortie.object;

import java.util.Arrays;
import java.util.Comparator;

public class JavaArraySort<T>
  implements ObjectRangeSorter<T> {
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    if (start==0 && stop==vArray.length) {
      Arrays.sort(vArray, comparator);
    } else {
      int count = stop - start;
      @SuppressWarnings("unchecked")
      T[] vCopy = (T[]) new Object[count];
      for (int r=start, w=0; r<stop; ++r, ++w) {
        vCopy [ w ] = vArray [ r ];
      }
      Arrays.sort(vCopy, comparator);
      for (int r=0, w=start; r<count; ++r, ++w) {
        vArray [ w ] = vCopy [ r ];
      }
    }
  }

}
