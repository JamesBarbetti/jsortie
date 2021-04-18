package jsortie.quicksort.multiway.partitioner.kangaroo;

import jsortie.quicksort.expander.branchavoiding.SkippyExpander;
import jsortie.quicksort.multiway.partitioner.FixedCountPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class SkippyCrossoverPartitioner2 
  implements FixedCountPivotPartitioner {
  SkippyExpander kx = new SkippyExpander();
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop, int[] pivotIndices) {
    if (pivotIndices.length<2) {
      return partitionWithOnePivot
             ( vArray, start, stop, pivotIndices[0] );
    } else if (stop-start<10) {
      return MultiPivotUtils.dummyPartitions
             ( vArray, start, stop, 2 );
    }
    int leftHole = start;
    int rightHole = stop-1;
    MultiPivotUtils.movePivotsAside
      ( vArray, pivotIndices
      , new int[]{ leftHole, rightHole });
    int lhs = leftHole  + 1;
    int rhs = rightHole - 1;
    int vLeftPivot =  vArray[leftHole];
    int vRightPivot = vArray[rightHole];
    if (vLeftPivot==vRightPivot) {
      return partitionWithOnePivot
             ( vArray, start, stop, start );
    }
    //Partition the left half with vLeftPivot,
    //and the right half with vRightPivot
    do {
      int v1            = vArray[lhs];
      vArray[leftHole]  = v1;
      leftHole         += ( v1 < vLeftPivot ) ? 1 : 0;
      vArray[lhs]       = vArray[leftHole];
      ++lhs;
      int v2            = vArray[rhs];
      vArray[rightHole] = v2;
      rightHole        -= ( vRightPivot < v2) ? 1 : 0;
      vArray[rhs]       = vArray[rightHole];
      --rhs;
    } while (lhs<rhs);
    if (lhs==rhs) {
      int v1           = vArray[lhs];
      vArray[leftHole] = v1;
      leftHole        += ( v1 < vLeftPivot ) ? 1 : 0;
      vArray[lhs]      = vArray[leftHole];
      ++lhs;      
    }
    vArray[leftHole]  = vLeftPivot;
    vArray[rightHole] = vRightPivot;
    leftHole  = kx.expandPartitionToRight
                (vArray, leftHole, lhs, rightHole);
    rightHole = kx.expandPartitionToLeft
                (vArray, leftHole+1, rightHole, rightHole);
    return new int[] 
      { start, leftHole, leftHole+1, rightHole
      , rightHole+1, stop };
  }
  public int[] partitionWithOnePivot
    ( int[] vArray, int start, int stop, int pivotIndex) {
    int vPivot         = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    vArray[start]      = vPivot;
    int rhs = kx.expandPartitionToRight
              ( vArray, start, start+1, stop );
    int lhs = kx.expandPartitionToLeft
              ( vArray, start, rhs, rhs);
    return new int[] 
      { start, lhs, rhs, rhs+1, rhs+1, stop };
  }
  @Override
  public int getPivotCount() {
    return 2;
  }
}
