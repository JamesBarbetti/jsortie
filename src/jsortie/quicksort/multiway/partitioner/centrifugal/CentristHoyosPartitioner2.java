package jsortie.quicksort.multiway.partitioner.centrifugal;

import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class CentristHoyosPartitioner2 
  extends CentrifugalPartitioner2 {
  @Override
  public int[] multiPartitionRange
               ( int[] vArray, int start
               , int stop, int[] pivotIndices) {
    if (stop-start<4) {
      return MultiPivotUtils.dummyPartitions
             ( vArray, start, stop, getPivotCount() );
    }
    int lhs = start;
    int rhs = stop-1;
    if (!MultiPivotUtils.tryToMovePivotsAside
         ( vArray,  pivotIndices
         , new int[] { lhs, rhs})) {
      return MultiPivotUtils.fakePartitions
             ( vArray, start, stop, pivotIndices
             , getPivotCount() );
    }
    int vP = vArray[lhs];
    int vQ = vArray[rhs];
    int leftHole = start;
    for (;;) {
      int vLeft;
      do {
        ++lhs;
        vLeft = vArray[lhs];
        while ( vLeft < vP ) {
          vArray[leftHole]=vLeft;
          ++leftHole;
          vArray[lhs]=vArray[leftHole];
          ++lhs;
          vLeft = vArray[lhs];
        }
      } while ( vLeft <= vQ && lhs < rhs);
      int vRight;
      do {
        --rhs;
        vRight = vArray[rhs];
      } while ( vQ < vRight );
      if (rhs<lhs) {
        //Only now, check if lhs and rhs have crossed
        break;
      }
      vArray[rhs] = vLeft;
      if ( vRight < vP) {
        vArray[leftHole]=vRight;
        ++leftHole;
        vArray[lhs]=vArray[leftHole];
      } else {
        vArray[lhs]=vRight;
      }
      //The hole at lhs is filled, 
      //and round we go again
    } 
    vArray[leftHole] = vP;
    vArray[stop-1]   = vArray[lhs];
    vArray[lhs]      = vQ;
    return new int[] 
           { start, leftHole, leftHole+1
           , lhs, lhs+1, stop }; 
  }
}
