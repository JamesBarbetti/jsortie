package jsortie.object.quicksort.expander;

import java.util.Comparator;

public class RightLomutoObjectExpander<T>
  extends LomutoObjectExpander<T> {
  @Override
  public int expandPartitionToRight
    ( Comparator<? super T> comparator
    , T[] vArray, int hole
    , int startRight, int stop) {
    T vPivot = vArray[hole]; 
    int lastOnLeft = hole; 
    for (int scan=startRight;scan<stop;++scan) {
      T v = vArray[scan];
      if ( comparator.compare(v , vPivot ) <= 0 ) {
        ++lastOnLeft;
        vArray[scan]       = vArray[lastOnLeft];
        vArray[lastOnLeft] = v;
      }
    }
    vArray[hole]       = vArray[lastOnLeft];
    vArray[lastOnLeft] = vPivot;
    return lastOnLeft;
  }
}