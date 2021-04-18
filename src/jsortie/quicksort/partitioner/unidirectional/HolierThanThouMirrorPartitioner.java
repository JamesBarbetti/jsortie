package jsortie.quicksort.partitioner.unidirectional;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class HolierThanThouMirrorPartitioner
  implements SinglePivotPartitioner{
  //This is a left-right reflection of HolierThanThouPartitioner
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int partitionRange(int[] vArray, int start, int stop, int pivotIndex) {
    //Initialize
    int vPivot        = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[stop-1];
    int hole          = stop-2;

    while (start<hole && vPivot<vArray[hole]) --hole;
    ++hole;
    vArray[stop-1] = vArray[hole];
    vArray[hole]   = vPivot;
    while (vPivot < vArray[start]) ++start;
		
    for (int scan=hole-1;start<=scan;--scan) {
      int v = vArray[scan];
      if ( vPivot <= v ) {
        vArray[hole] = v;
        --hole;
        vArray[scan] = vArray[hole];
      }
    }
    vArray[hole] = vPivot;
    return hole;
  }
}
