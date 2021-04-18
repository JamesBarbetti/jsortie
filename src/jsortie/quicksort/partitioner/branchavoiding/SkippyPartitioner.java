package jsortie.quicksort.partitioner.branchavoiding;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class SkippyPartitioner 
  implements SinglePivotPartitioner {	
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }	
  @Override
  public int partitionRange
    ( int[] vArray, int start, int stop
    , int pivotIndex) {
    int vPivot         = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    int hole           = start;
    int scan           = start+1;  
    for (; scan<stop; ++scan) {
      int        v = vArray[scan];
      vArray[hole] = v;
      hole        += (v <= vPivot) ? 1 : 0;
      vArray[scan] = vArray[hole];
      //Tricky, eh?
    }    
    vArray[hole] = vPivot;
    return hole;
  }
}
