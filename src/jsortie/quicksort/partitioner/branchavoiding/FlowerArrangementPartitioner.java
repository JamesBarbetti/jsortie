package jsortie.quicksort.partitioner.branchavoiding;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class FlowerArrangementPartitioner
  implements SinglePivotPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int partitionRange
    ( int[] vArray, int start, int stop
    , int pivotIndex ) {
    if (stop-start<2) {
      return start;
    }
    int vPivot         = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    int lhs            = start+1;
    int rhs            = stop-1;
    do {
      int vGoesRight   = vArray[lhs];
      int vGoesLeft    = vArray[rhs];
      vArray[lhs]      = vGoesLeft;
      vArray[rhs]      = vGoesRight;
      lhs += ( vGoesLeft <= vPivot     ) ? 1 : 0;
      rhs -= ( vPivot    <= vGoesRight ) ? 1 : 0;	
    } while (lhs<rhs);
    lhs -= (lhs == stop || vPivot < vArray[lhs]  ) 
         ? 1 : 0;
    vArray[start]      = vArray[lhs];
    vArray[lhs]        = vPivot;
    return lhs;
  }
}
