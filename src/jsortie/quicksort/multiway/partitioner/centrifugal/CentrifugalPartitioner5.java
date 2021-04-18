package jsortie.quicksort.multiway.partitioner.centrifugal;

import jsortie.quicksort.multiway.partitioner.FixedCountPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class CentrifugalPartitioner5 implements FixedCountPivotPartitioner
{
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices) {
    if (stop-start<13) {
      return MultiPivotUtils.dummyPartitions
             ( vArray, start, stop, getPivotCount() );
    }
    int leftHole1    = start; 
    int leftHole2    = start+1;
    int rightHole1   = stop-2;
    int rightHole2   = stop-1;
    int lhs          = leftHole2;
    int rhs          = stop-3;    
    if (!MultiPivotUtils.tryToMovePivotsAside 
         ( vArray,  pivotIndices 
         , new int[] { leftHole1, leftHole2
                     , rhs, rightHole1, rightHole2 })) {	
      return MultiPivotUtils.fakePartitions
             ( vArray, start, stop
             , pivotIndices, getPivotCount());
    }
    int vLeftPivot1  = vArray[leftHole1];
    int vLeftPivot2  = vArray[leftHole2];
    int vMiddlePivot = vArray[rhs];
    int vRightPivot1 = vArray[rightHole1];
    int vRightPivot2 = vArray[rightHole2];
    int vLeft;
    int vRight;
    for (;;) {
      for (;;) {
        ++lhs;
        if (rhs<=lhs) {
          vArray[leftHole1]  = vLeftPivot1;
          vArray[leftHole2]  = vLeftPivot2;
          vArray[rhs]        = vMiddlePivot;
          vArray[rightHole1] = vRightPivot1;
          vArray[rightHole2] = vRightPivot2;
          return new int[] 
            { start, leftHole1, leftHole1+1, leftHole2
            , leftHole2+1, rhs,  rhs+1, rightHole1
            , rightHole1+1, rightHole2, rightHole2+1, stop };
        }
        vLeft = vArray[lhs];
        if ( vMiddlePivot <= vLeft ) {
          break;
        } else if ( vLeft < vLeftPivot2 ) {
          if ( vLeft < vLeftPivot1) {
            vArray[leftHole1] = vLeft;
            ++leftHole1;
            vArray[leftHole2] = vArray[leftHole1];
          } else {
            vArray[leftHole2] = vLeft;
          }
          ++leftHole2;
          vArray[lhs] = vArray[leftHole2];
        }
      }
      if (vRightPivot1 < vLeft ) {
        if (vRightPivot2 < vLeft) {
          vArray[rightHole2] = vLeft;
          --rightHole2;
          vArray[rightHole1] = vArray[rightHole2];
        } else {
          vArray[rightHole1] = vLeft;  
        }
        --rightHole1;
        vArray[rhs] = vArray[rightHole1];
      } else {
        vArray[rhs] = vLeft;
      }
      //hole now at lhs
      for (;; ) {
        --rhs;
        vRight = vArray[rhs];
        if (rhs<=lhs) {
          vArray[leftHole1]  = vLeftPivot1;
          vArray[leftHole2]  = vLeftPivot2;
          vArray[lhs]        = vMiddlePivot;
          vArray[rightHole1] = vRightPivot1;
          vArray[rightHole2] = vRightPivot2;
          return new int[] 
            { start, leftHole1, leftHole1+1, leftHole2
            , leftHole2+1, lhs, lhs+1, rightHole1
            , rightHole1+1, rightHole2, rightHole2+1, stop };
        } else if ( vRight <= vMiddlePivot ) {
          break;
        } else if ( vRightPivot1 < vRight ) {
          if ( vRightPivot2 < vRight ) {
            vArray[rightHole2] = vRight;
            --rightHole2;
            vArray[rightHole1] = vArray[rightHole2];
          } else {
            vArray[rightHole1] = vRight;
          }
          --rightHole1;
          vArray[rhs] = vArray[rightHole1];
        }
      }
      if (vRight < vLeftPivot2 ) {
        if ( vRight < vLeftPivot1 ) {
          vArray[leftHole1] = vRight;
          ++leftHole1;
          vArray[leftHole2] = vArray[leftHole1];
        } else {
          vArray[leftHole2] = vRight; 
        }
        ++leftHole2;
        vArray[lhs] = vArray[leftHole2];
      } else {
        vArray[lhs] = vRight;
      }
    }
  }  
  @Override
  public int getPivotCount() {
    return 5;
  }	
}
