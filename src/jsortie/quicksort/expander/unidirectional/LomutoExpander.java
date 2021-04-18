package jsortie.quicksort.expander.unidirectional;

import jsortie.quicksort.expander.PartitionExpander;

public class LomutoExpander 
  implements PartitionExpander {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int expandPartition
    ( int[] vArray, int start
    , int stopLeft, int hole
    , int startRight, int stop ) {
    int hole2 = expandPartitionToRight 
                ( vArray, hole,  startRight, stop );
    int hole1 = expandPartitionToLeft 
                ( vArray, start, stopLeft,  hole2);
    return hole1;
  }
  public int expandPartitionToLeft
    ( int[] vArray, int start
    , int stopLeft, int hole ) {
    int vPivot      = vArray[hole]; 
    int lastOnRight = hole; 
    for (int scan=stopLeft-1;start<=scan;--scan) {
      int v = vArray[scan];
      if ( vPivot <= v ) {
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
    ( int[] vArray, int hole
    , int startRight, int stop) {
    int vPivot = vArray[hole]; 
    int lastOnLeft = hole; 
    for (int scan=startRight;scan<stop;++scan) {
      int v = vArray[scan];
      if ( v <= vPivot ) {
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
