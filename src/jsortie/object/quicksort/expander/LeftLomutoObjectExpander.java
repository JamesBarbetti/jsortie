package jsortie.object.quicksort.expander;

import java.util.Comparator;

public class LeftLomutoObjectExpander<T> 
  extends LomutoObjectExpander<T> {
  @Override
  public int expandPartitionToLeft
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stopLeft
    , int hole ) {
    T   vPivot      = vArray[hole]; 
    int lastOnRight = hole; 
    for (int scan=stopLeft-1;start<=scan;--scan) {
      T v = vArray[scan];
      if ( comparator.compare(vPivot , v ) <= 0 ) {
        --lastOnRight;
        vArray[scan]        = vArray[lastOnRight];
        vArray[lastOnRight] = v;
      }
    }
    vArray[hole]        = vArray[lastOnRight];
    vArray[lastOnRight] = vPivot;
    return lastOnRight;
  }

}
