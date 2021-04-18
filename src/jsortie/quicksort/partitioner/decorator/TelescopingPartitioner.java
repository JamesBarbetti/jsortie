package jsortie.quicksort.partitioner.decorator;

import jsortie.quicksort.partitioner.bidirectional.traditional.SingletonPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class TelescopingPartitioner
  implements SinglePivotPartitioner {
  protected SinglePivotPartitioner innerPartitioner;
  public TelescopingPartitioner() {
    innerPartitioner 
      = new SingletonPartitioner();
  }
  public TelescopingPartitioner
    ( SinglePivotPartitioner inner ) {
    innerPartitioner = inner;
  }
  @Override
  public String toString() { 
    return this.getClass().getSimpleName()
      + "(" + innerPartitioner.toString() + ")";
  }	
  @Override
  public int partitionRange
    ( int[] vArray, int start, int stop
    , int pivotIndex ) { 
    int vPivot = vArray[pivotIndex];
    if (vArray[start]<vPivot) {
      do {
        ++start;
      } while (vArray[start]<vPivot);
    }
    --stop;
    if (vPivot<vArray[stop]) {
      do {
        --stop;
      } while (vPivot<vArray[stop]);
    }
    ++stop;
    if (start+1==stop) {
      return start;
    }
    return innerPartitioner.partitionRange
           ( vArray, start, stop, pivotIndex );
  }
}
