package jsortie.quicksort.expander.branchavoiding;

import jsortie.quicksort.expander.PartitionExpander;

public class SkippyExpander 
  implements PartitionExpander {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int expandPartition
    ( int[] vArray, int start, int stopLeft
    , int pivotIndex, int startRight, int stop ) {
    pivotIndex 
      = expandPartitionToRight 
        ( vArray, pivotIndex,  startRight, stop);
    pivotIndex 
      = expandPartitionToLeft
        ( vArray, start, stopLeft,  pivotIndex);
    return pivotIndex;
  }
  public int expandPartitionToLeft
    ( int[] vArray, int start
    , int stopLeft, int pivotIndex ) {
    int vPivot = vArray[pivotIndex];
    for (int scan=stopLeft-1;start<=scan;--scan) {
      int v = vArray[scan];
      vArray[pivotIndex] = v;
      pivotIndex -= ( vPivot <= v ) ? 1 : 0;
      vArray[scan] = vArray[pivotIndex];
    }
    vArray[pivotIndex] = vPivot;
    return pivotIndex;
  }
  public int expandPartitionToRight
    ( int[] vArray, int pivotIndex
    , int startRight, int stop ) {
    int vPivot = vArray[pivotIndex];
    for (int scan=startRight;scan<stop;++scan) {
    int v = vArray[scan];
      vArray[pivotIndex] = v;
      pivotIndex += ( v<= vPivot ) ? 1 : 0;
      vArray[scan] = vArray[pivotIndex];
    }
    vArray[pivotIndex] = vPivot;
    return pivotIndex;
  }
}
