package jsortie.quicksort.multiway.partitioner.centrifugal;

import jsortie.quicksort.multiway.partitioner.FixedCountPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class CentrifugalPartitioner3 
  implements FixedCountPivotPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int getPivotCount() {
    return 3;
  } 
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    if (stop-start<7) {
      return MultiPivotUtils.dummyPartitions
             ( vArray, start, stop, getPivotCount() );
    }
    int leftHole1    = start; 
    int rightHole1   = stop-1;
    int lhs          = start;
    int rhs          = stop-2;
    if ( !MultiPivotUtils.tryToMovePivotsAside
         ( vArray,  pivotIndices
         , new int[] { leftHole1, rhs, rightHole1 })) {
      return MultiPivotUtils.fakePartitions
        ( vArray, start, stop
        , pivotIndices, getPivotCount());
    }    	
    int vMiddlePivot = vArray[rhs];
    int vLeftPivot1  = vArray[leftHole1];
    int vRightPivot1 = vArray[rightHole1];
    int vRight;
    int vLeft;
    for (;;) {
      for (;;) {
        ++lhs;
        if (rhs<=lhs) {
          vArray[leftHole1]  = vLeftPivot1;
          vArray[rhs]        = vMiddlePivot;
          vArray[rightHole1] = vRightPivot1;
          return new int[] 
            { start, leftHole1
            , leftHole1+1, rhs
            , rhs+1, rightHole1
            , rightHole1+1,  stop };
        }
        vLeft = vArray[lhs];
        if ( vMiddlePivot <= vLeft ) {
          break;
        } else if ( vLeft < vLeftPivot1 ) {
          vArray[leftHole1] = vLeft;
          ++leftHole1;
          vArray[lhs] = vArray[leftHole1];
        }
      }
      if (vRightPivot1 < vLeft ) {
      	vArray[rightHole1] = vLeft;  
        --rightHole1;
        vArray[rhs] = vArray[rightHole1];
      } else {
	    vArray[rhs] = vLeft;
      }
      
      //hole now at lhs      
      for (;; ) {
        --rhs;
        if (rhs<=lhs) {
          vArray[leftHole1]  = vLeftPivot1;
          vArray[lhs]        = vMiddlePivot;
          vArray[rightHole1] = vRightPivot1;
          return new int[] 
            { start, leftHole1
            ,  leftHole1+1,   lhs
            , lhs+1, rightHole1
            , rightHole1+1,  stop };
        }
        vRight = vArray[rhs];
        if ( vRight <= vMiddlePivot ) {
          break;
        } else if ( vRightPivot1 < vRight ) {
          vArray[rightHole1] = vRight;
          --rightHole1;
          vArray[rhs] = vArray[rightHole1];
        }
      }
      if (vRight < vLeftPivot1 ) {
    	vArray[leftHole1] = vRight;  
        ++leftHole1;
        vArray[lhs] = vArray[leftHole1];
      } else {
        vArray[lhs] = vRight;
      }
    }
  }	
}
