package jsortie.quicksort.multiway.partitioner.singlepivot;

import jsortie.helper.ShiftHelper;

//For another example, see: http://algs4.cs.princeton.edu/23quicksort/QuickX.java.html
//This version is a little different: once boundary checking is no longer needed,
//it is no longer used.  This makes the routine a little bit longer and a little bit
//faster.

public class BentleyMcIlroyPartitioner 
  implements TernarySinglePivotPartitioner {
  protected ShiftHelper shifter 
    = new ShiftHelper();
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }  
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices) {
    int middlePivotIndex = pivotIndices.length/2;
    int pivotIndex  = pivotIndices[middlePivotIndex];
    int vPivot = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    vArray[start] = vPivot; //needed as a sentinel, for
                            //the first scan from the right
    int low  = start+1; //where the next value == vPivot 
                        //will go on left
    int lhs  = start+1; //the left scanning pointer
    int rhs  = stop-1;  //the right scanning pointer
    int high  = stop-1; //where the next value == vPivot 
                        //will go on right
    while (vPivot < vArray[rhs]) {
      --rhs;
    }
    if (rhs==start) { //early bail out: pivot is 
                      //the minimum, no duplicates
      return new int[] { start+1, stop };
    }
    while (vArray[lhs]<vPivot && lhs<rhs) ++lhs;
    if (lhs==rhs) { 
      //early bail out: array[start+1..lhs-1] < pivot, 
      //array[lhs] <= pivot, array[lhs+1..stop-1] > pivot
      vArray[start] = vArray[lhs];
      vArray[lhs] = vPivot;
      return new int[] { start, lhs, lhs+1, stop };
    }
    do {
      //swap (possibly equal) items around centre
      int vTemp = vArray[lhs];
      vArray[lhs] = vArray[rhs];
      vArray[rhs] = vTemp;
      if ( vArray[lhs]==vPivot ) {
        vTemp = vArray[low];
        vArray[low] = vArray[lhs];
        vArray[lhs] = vTemp;
        ++low;
      }
      if ( vArray[rhs]==vPivot ) {
        vTemp = vArray[rhs];
        vArray[rhs] = vArray[high];
        vArray[high] = vTemp;
        --high;
      }
      //find next value >= vPivot from left
      do { 
        ++lhs; 
      } while (vArray[lhs]<vPivot);
      //find next value <= vPivot from right
      do { 
        --rhs; 
      } while (vPivot<vArray[rhs]); 
    }
    while (lhs<rhs);
    int dupesOnLeft  = low-start;
    int dupesOnRight = stop-high-1;
    shifter.moveFrontElementsToBack
      ( vArray, start, low,   lhs );
    shifter.moveBackElementsToFront
      ( vArray, lhs,   high+1,stop ); 
    return new int[] 
      { start, lhs-dupesOnLeft
      , lhs+dupesOnRight, stop };
  }
  @Override
  public int getPivotCount() {
	return 1;
  }
}
