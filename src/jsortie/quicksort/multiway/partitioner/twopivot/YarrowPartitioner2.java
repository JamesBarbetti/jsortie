package jsortie.quicksort.multiway.partitioner.twopivot;

import jsortie.quicksort.multiway.partitioner.FixedCountPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class YarrowPartitioner2 
  implements FixedCountPivotPartitioner{
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop, int[] pivotIndices ) {
    //todo: debug this sod.
    if (pivotIndices.length < 2 || stop-start < 4 ) {
      return MultiPivotUtils.fakePartitions
        ( vArray, start, stop, pivotIndices, 2 );
    }
    MultiPivotUtils.movePivotsAside 
      ( vArray,  pivotIndices , new int[] { start, stop-1 } );
    int vLeftPivot      = vArray[start];
    int vRightPivot     = vArray[stop-1];
    int leftHole        = start;
    int lhs             = start+1;
    int rhs             = stop-2;
    do {
      int vGoesRight    = vArray[lhs];
      int vGoesLeft     = vArray[rhs];
      vArray[leftHole]  = vGoesLeft;
      leftHole         += ( vGoesLeft <= vLeftPivot ) ? 1 : 0;
      vArray[lhs]       = vArray[leftHole];
      vArray[rhs]       = vGoesRight;
      lhs              += ( vGoesLeft   <= vRightPivot ) ? 1 : 0;
      rhs              -= ( vRightPivot <= vGoesRight  ) ? 1 : 0;	
    } while (lhs<rhs);
    if (lhs==rhs) {
      int v            = vArray[lhs];
      vArray[leftHole] = v;
      leftHole        += ( v <= vLeftPivot) ? 1 : 0;
      vArray[lhs]      = vArray[leftHole];
      lhs             += ( v <= vRightPivot) ? 1 : 0;
    }    
    vArray[stop-1]   = vArray[lhs];
    vArray[lhs]      = vRightPivot;
    vArray[leftHole] = vLeftPivot;
    return new int[] { start, leftHole, leftHole+1, lhs, lhs+1, stop };
  }
  @Override
  public int getPivotCount() {
    return 2;
  }
}
