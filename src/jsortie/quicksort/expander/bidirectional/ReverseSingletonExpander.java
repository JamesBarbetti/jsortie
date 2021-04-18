package jsortie.quicksort.expander.bidirectional;

import jsortie.quicksort.expander.unidirectional.LomutoExpander;

public class ReverseSingletonExpander 
  extends LomutoExpander {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int expandPartition
    ( int[] vArray, int start, int stopLeft
    , int hole, int startRight, int stop) {
    int vPivot = vArray[hole];
    while (startRight<stop) {
      do { 
        --stopLeft; 
        if (stopLeft<start) {
          return expandPartitionToRight
                 ( vArray, hole
                 , startRight, stop);
        }
      } while (vArray[stopLeft] < vPivot);
      while (vPivot < vArray[startRight]) {
        ++startRight;
        if (stop<=startRight) {
          int vTemp    = vArray[stopLeft];
          vArray[hole] = vTemp;
          --hole;
          vArray[stopLeft] = vArray[hole];
          vArray[hole]     = vPivot;
          return expandPartitionToLeft
                 ( vArray, start
                 , stopLeft, hole);
        }
      }
      int vTemp = vArray[stopLeft];
      vArray[stopLeft] = vArray[startRight];
      vArray[startRight] = vTemp;
      ++startRight;
    } 
    hole = expandPartitionToLeft
           ( vArray, start
           , stopLeft, hole);
    return hole;
  }
}
