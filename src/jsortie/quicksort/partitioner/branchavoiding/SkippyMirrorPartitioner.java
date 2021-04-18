package jsortie.quicksort.partitioner.branchavoiding;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class SkippyMirrorPartitioner 
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
    vArray[pivotIndex] = vArray[stop-1];
    int hole           = stop-1;
    
    for (int scan = stop-2; start<=scan; --scan) {
      int        v = vArray[scan];
      vArray[hole] = v;
      hole        -= (vPivot <=v ) ? 1 : 0;
      vArray[scan] = vArray[hole];
    }
    vArray[hole] = vPivot;
    return hole;
  }
}
