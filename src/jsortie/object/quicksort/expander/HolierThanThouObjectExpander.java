package jsortie.object.quicksort.expander;

import java.util.Comparator;

public class HolierThanThouObjectExpander<T> 
  implements PartitionObjectExpander<T> {
  //This is the object counterpart of HolierThanThouExpander
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int expandPartition
    ( Comparator<? super T> comparator
    , T[] vArray, int start,      int stopLeft
    , int hole,   int startRight, int stop) {
    hole = expandPartitionToLeft 
           ( comparator, vArray, start, stopLeft, hole );
    return expandPartitionToRight
           ( comparator, vArray, hole, startRight, stop );
  }
  public int expandPartitionToLeft
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stopLeft
    , int hole ) {
    T vPivot = vArray[hole];
    for (int scan=stopLeft-1;start<=scan;--scan) {
      T v = vArray[scan];
      if ( comparator.compare ( vPivot , v) <= 0 ) {
        vArray[hole] = v;
        --hole;
        vArray[scan] = vArray[hole];
      }
    }
    vArray[hole] = vPivot;
    return hole;
  }
  public int expandPartitionToRight
    ( Comparator<? super T> comparator
    , T[] vArray, int hole
    , int startRight, int stop ) {
    T vPivot = vArray[hole];
    for (int scan=startRight;scan<stop;++scan) {
      T v = vArray[scan];
      if ( comparator.compare( v,  vPivot ) <= 0) {
        vArray[hole] = v;
        ++hole;
        vArray[scan] = vArray[hole];
      }
    }
    vArray[hole] = vPivot;
    return hole;
  }
}
