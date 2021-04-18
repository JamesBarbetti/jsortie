package jsortie.object.quicksort.expander;

import java.util.Comparator;

public class ReverseSingletonObjectExpander<T> 
  extends LomutoObjectExpander<T> {
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
    T vPivot = vArray[hole];
    while (startRight<stop) {
      do { 
        --stopLeft; 
        if (stopLeft<start) {
          hole = expandPartitionToRight
                 ( comparator, vArray, hole
                 , startRight, stop);
          return hole;
        }
      } while (comparator.compare ( vArray[stopLeft] , vPivot) < 0 );
      while (comparator.compare ( vPivot , vArray[startRight]) < 0 ) {
        ++startRight;
        if (stop<=startRight) {
          hole = expandPartitionToLeft
                 ( comparator, vArray, start
                 , stopLeft, hole);
          return hole;
        }
      }
      T vTemp = vArray[stopLeft];
      vArray[stopLeft] = vArray[startRight];
      vArray[startRight] = vTemp;
      ++startRight;
    } 
    hole = expandPartitionToLeft
           ( comparator, vArray, start
           , stopLeft, hole);
    return hole;
  }
  
}
