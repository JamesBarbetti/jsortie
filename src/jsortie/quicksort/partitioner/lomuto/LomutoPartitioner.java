package jsortie.quicksort.partitioner.lomuto;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class LomutoPartitioner
  implements SinglePivotPartitioner {
  @Override
  public String toString() { 
    return this.getClass().getSimpleName(); 
  }
  public int partitionRange 
    ( int vArray[], int start, int stop, int pivotIndex) {
    int vPivot         = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    int lhs            = start;
    for (int scan=start+1; scan<stop; ++scan) {
      int vScan = vArray[scan];
      if (vScan < vPivot) {
        ++lhs;
        vArray[scan] = vArray[lhs];
        vArray[lhs]  = vScan;
      }
    }
    vArray[start] = vArray[lhs];
    vArray[lhs  ] = vPivot;
    return lhs;
  }
}