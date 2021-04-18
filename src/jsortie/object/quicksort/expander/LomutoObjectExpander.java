package jsortie.object.quicksort.expander;

import java.util.Comparator;

public class LomutoObjectExpander<T> 
  implements PartitionObjectExpander<T> {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int expandPartition
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stopLeft
    , int hole, int startRight, int stop ) {
    int hole2 = expandPartitionToRight 
                ( comparator, vArray, hole
                ,  startRight, stop );
    int hole1 = expandPartitionToLeft 
                ( comparator, vArray, start
                , stopLeft,  hole2);
    return hole1;
  }
  public int expandPartitionToLeft
    ( Comparator<? super T> comparator
    , T[] vArray, int start
    , int stopLeft, int hole ) {
    T vPivot      = vArray[hole]; 
    int lastOnRight = hole; 
    for (int scan=stopLeft-1;start<=scan;--scan) {
      T v = vArray[scan];
      if ( comparator.compare( vPivot , v ) < 0 ) {
        --lastOnRight;
        vArray[scan]        = vArray[lastOnRight];
        vArray[lastOnRight] = v;
      }
    }
    vArray[hole]        = vArray[lastOnRight];
    vArray[lastOnRight] = vPivot;
    return lastOnRight;
  }
  public int expandPartitionToRight
    ( Comparator<? super T> comparator
    , T[] vArray, int hole
    , int startRight, int stop) {
    T vPivot = vArray[hole]; 
    int lastOnLeft = hole; 
    for (int scan=startRight;scan<stop;++scan) {
      T v = vArray[scan];
      if ( comparator.compare( v , vPivot ) < 0 ) {
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
