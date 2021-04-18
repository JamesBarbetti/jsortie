package jsortie.quicksort.partitioner.branchavoiding;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class BalancedSkippyPartitioner 
  implements SinglePivotPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int partitionRange
    ( int[] vArray, int start, int stop
    , int pivotIndex ) {
    int vPivot         = vArray[pivotIndex];
    int hole           = pivotIndex;
    if (pivotIndex-start<stop-pivotIndex) {
      for (int scan=hole-1; start<=scan; --scan) {
        int v = vArray[scan];
        vArray[hole] = v;
        hole        -= ( vPivot <= v) ? 1 : 0;
        vArray[scan] = vArray[hole];
      }
      for (int scan=pivotIndex+1; scan<stop; ++scan) {
        int v        = vArray[scan];
        vArray[hole] = v;
        hole        += ( v <= vPivot) ? 1 : 0;
        vArray[scan] = vArray[hole];
      }
    } else {
      for (int scan=pivotIndex+1; scan<stop; ++scan) {
        int v        = vArray[scan];
        vArray[hole] = v;
        hole        += ( v <= vPivot) ? 1 : 0;
        vArray[scan] = vArray[hole];
      }
      for (int scan=hole-1; start<=scan; --scan) {
        int v = vArray[scan];
        vArray[hole] = v;
        hole        -= ( vPivot <= v) ? 1 : 0;
        vArray[scan] = vArray[hole];
      }
    }
    vArray[hole] = vPivot;
    return hole;
  }
}
