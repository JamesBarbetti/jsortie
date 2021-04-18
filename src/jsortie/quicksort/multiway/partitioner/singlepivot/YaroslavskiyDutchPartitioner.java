package jsortie.quicksort.multiway.partitioner.singlepivot;

import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class YaroslavskiyDutchPartitioner 
  implements MultiPivotPartitioner {	
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }	
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    MultiPivotUtils.movePivotsAside
      ( vArray,  pivotIndices, new int[] { start } );
    int vP = vArray[start];
    int lhs = start + 1; //first element that might be >= vP
    int rhs = stop - 1;  //last  element that might be <= vP
    int vTemp;
    for ( int scan=lhs; scan <= rhs; ++scan) {
      if ( vArray[scan] == vP ) {
      } else if ( vArray[scan] < vP ) {
        vTemp = vArray[lhs];
        vArray[lhs] = vArray[scan];
        vArray[scan] = vTemp;
        ++lhs;
      } else {
        while ( vP < vArray[rhs] && scan<rhs ) {
          --rhs;
        }
        vTemp = vArray[scan];
        vArray[scan] = vArray[rhs];
        vArray[rhs] = vTemp;
        --rhs;
        if ( vArray[scan] < vP ) {
          vTemp = vArray[lhs];
          vArray[lhs] = vArray[scan];
          vArray[scan] = vTemp;
          ++lhs;
        }
      }
    }
    --lhs;
    vArray[start] = vArray[lhs];
    vArray[lhs] = vP;
    ++rhs;
    if (rhs<stop) {
      return new int[] { start, lhs, rhs, stop }; 
    } else if (start<lhs) {
      return new int[] { start, lhs };
    } else {
      return new int[] {};
    }
  }
}
