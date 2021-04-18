package jsortie.quicksort.partitioner.bidirectional;

import jsortie.helper.EgalitarianPartitionerHelper;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class LazyPartitioner implements SinglePivotPartitioner {
  //This promises *never* to swap equal-valued elements, ever	
  //It is a Singleton partitioner variant.
  //It pre-figures InertialPartitioner.	
  protected static EgalitarianPartitionerHelper helper = new EgalitarianPartitionerHelper();	
	
  @Override
  public String toString() { return this.getClass().getSimpleName(); }
	
  @Override
  public int partitionRange(int[] vArray, int start, int stop, int pivotIndex) {
    int vPivot = vArray[pivotIndex];
    int lhs;
    int rhs;
    for (lhs = start; lhs < stop; ++lhs) {
      if (vPivot<vArray[lhs]) {
        break;
      }
    }
    for (rhs = stop-1; start <= rhs; --rhs) {
      if (vArray[rhs]<vPivot) {
        break;
      }
    }
    if (lhs==stop) {
      if (rhs<start) { //All elements are == vPivot
        pivotIndex = start + (stop-start)/2;
      } else { //All elements are <= vPivot
        pivotIndex = helper.swapEqualToRight( vArray, start, stop, vPivot);
      } 
    } else if (rhs<start) {
      pivotIndex = helper.swapEqualToLeft( vArray, start, stop, vPivot);		
    } else {		
      while (lhs<rhs) {
        int vTemp  = vArray[lhs];
        vArray[lhs] = vArray[rhs];
        vArray[rhs] = vTemp;
        //After the first exchange, there must be at least one value <
        //vPivot left of lhs, and at least one value > vPivot right of rhs
        //So boundary checks aren't needed.
        do { ++lhs; } while ( vArray[lhs] <= vPivot);
        do { --rhs; } while ( vPivot <= vArray[rhs]);			
      }		
			
      //At this point:
      //1. rhs points to a value < vPivot (therefore rhs!=pivotIndex)
      //2. lhs points to a value > vPivot (therefore lhs!=pivotIndex)
      //3. Every value in the range [rhs+1,lhs-1] inclusive must be ==vPivot
      if ( pivotIndex < rhs) {
        vArray[pivotIndex] = vArray[rhs];
        vArray[rhs]        = vPivot;
        pivotIndex        = rhs;
      } else if ( lhs < pivotIndex ) {
        vArray[pivotIndex] = vArray[lhs];
        vArray[lhs]        = vPivot;
        pivotIndex        = lhs;
      }
    }
    return pivotIndex;		
  }	
}
