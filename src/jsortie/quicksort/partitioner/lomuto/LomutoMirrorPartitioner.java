package jsortie.quicksort.partitioner.lomuto;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class LomutoMirrorPartitioner
  implements SinglePivotPartitioner {
  @Override
  public String toString() { 
    return this.getClass().getSimpleName(); 
  }  
  @Override
  public int partitionRange
    ( int vArray[], int start, int stop
    , int pivotIndex) { 
    int vPivot         = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[stop-1];
    int rhs            = stop-1;
    for (int scan=rhs-1; start<=scan; --scan) {
      int vScan = vArray[scan];
      if (vPivot < vScan) {
        --rhs;
        vArray[scan] = vArray[rhs];
        vArray[rhs]  = vScan;
      }
    }
    vArray[stop-1] = vArray[rhs];
    vArray[rhs  ]  = vPivot;
    return rhs;
  }
}
