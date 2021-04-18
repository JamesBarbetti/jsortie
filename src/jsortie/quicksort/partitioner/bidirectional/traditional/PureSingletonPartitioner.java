package jsortie.quicksort.partitioner.bidirectional.traditional;

import jsortie.helper.RangeSortHelper;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class PureSingletonPartitioner 
  implements SinglePivotPartitioner {
  @Override
  public String toString() { 
    return this.getClass().getSimpleName(); 
  }	
  @Override
  public int partitionRange
    ( int[] vArray, int start, int stop, int pivotIndex ) {
    //Assumes: pivotIndex == start + 1.  Doesn't *check* that though.
    int v             = vArray[pivotIndex];
    int lhs           = start+1;
    int rhs           = stop-1;
    for (;;) {
      do { 
        --rhs; 
      } while (v<vArray[rhs]);
      if (rhs<=lhs) {
        break;
      }
      do { 
        ++lhs; 
      } while (vArray[lhs]<v);
      if (rhs<=lhs) {
        break;
      }
      RangeSortHelper.swapElements(vArray, lhs, rhs);
    }
    if (start<stop) { 
      vArray[start+1] = vArray[rhs]; 
      vArray[rhs]     = v; 
    }
    return rhs;
  }
}
