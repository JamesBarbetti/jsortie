package jsortie.object.quicksort.multiway.partitioner.centrifugal;

import java.util.Comparator;

public class CentrifugalObjectPartitioner3<T> 
  extends CentrifugalObjectPartitioner2<T> {
  @Override
  public int getPivotCount() {
    return 3;
  } 
  @Override
  public int[] multiPartitionRange
    ( Comparator<? super T> c, T[] vArray
    , int start, int stop, int[] pivotIndices) {
    if (stop-start<7) {
      return utils.dummyPartitions
             ( c, vArray, start, stop, getPivotCount());
    }
    int leftHole1    = start; 
    int rightHole1   = stop-1;
    int lhs          = start;
    int rhs          = stop-2;    
    if ( !utils.tryToMovePivotsAside
         ( vArray,  pivotIndices
         , new int[] { leftHole1, rhs, rightHole1 })) {
      return utils.fakePartitions
             ( c, vArray, start, stop
             , pivotIndices, getPivotCount());
    }     
    T vMiddlePivot = vArray[rhs];
    T vLeftPivot1  = vArray[leftHole1];
    T vRightPivot1 = vArray[rightHole1];
    T vRight;
    T vLeft;
    for (;;) {
      for (;;) {
        ++lhs;
        vLeft = vArray[lhs];
        if (rhs<=lhs) {
          vArray[leftHole1]  = vLeftPivot1;
          vArray[rhs]        = vMiddlePivot;
          vArray[rightHole1] = vRightPivot1;
          return new int[] { start, leftHole1
            , leftHole1+1,  rhs
            , rhs+1, rightHole1
            , rightHole1+1, stop };
        }
        if ( c.compare 
             ( vMiddlePivot ,  vLeft ) 
             <= 0 ) {
          break;
        } else if ( c.compare
                    ( vLeft , vLeftPivot1 ) 
                    < 0 ) {
          vArray[leftHole1] = vLeft;
          ++leftHole1;
          vArray[lhs] = vArray[leftHole1];
        }
      }
      if ( c.compare( vRightPivot1, vLeft ) < 0 ) {
        vArray[rightHole1] = vLeft;  
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
          vArray[lhs]        = vMiddlePivot;
          vArray[rightHole1] = vRightPivot1;
          return new int[] { start, leftHole1
            , leftHole1+1,  lhs
            , lhs+1, rightHole1
            , rightHole1+1, stop };
        }
        if ( c.compare 
             ( vRight , vMiddlePivot ) 
             <= 0 ) {
          break;
        } else if ( c.compare 
                    ( vRightPivot1 , vRight ) 
                    < 0 ) {
          vArray[rightHole1] = vRight;
          --rightHole1;
          vArray[rhs] = vArray[rightHole1];
        }
      }
      if ( c.compare 
           ( vRight , vLeftPivot1 ) 
           < 0 ) {
      vArray[leftHole1] = vRight;  
        ++leftHole1;
        vArray[lhs] = vArray[leftHole1];
      } else {
        vArray[lhs] = vRight;
      }
    }
  } 
}
