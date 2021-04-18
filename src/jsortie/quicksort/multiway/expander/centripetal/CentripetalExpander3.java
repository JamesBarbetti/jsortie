package jsortie.quicksort.multiway.expander.centripetal;

import jsortie.quicksort.multiway.expander.holierthanthou.HolierThanThouExpander3;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class CentripetalExpander3 
  extends HolierThanThouExpander3 {
  protected CentripetalExpander2 cx2
    = new CentripetalExpander2();
  @Override
  public int[] expandPartitions
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int startRight, int stop) {
    int[] partitions;
    int   leftCount  = stopLeft-start;
    int   rightCount = stop-startRight;
    if ( pivotIndices.length < desiredPivotCount ) {
      return cx2.expandPartitions
            ( vArray, start, stopLeft
            , pivotIndices, startRight, stop );
    } else if ( leftCount < rightCount ) {
      int earlyStop = startRight + leftCount;
      partitions 
        = evenlyExpandExistingPartitions
          ( vArray, start, stopLeft
          , pivotIndices, startRight, earlyStop);
      partitions 
        = expandPartitionsToRight
          ( vArray, start
          , new int[] 
            { partitions[1], partitions[3]
            , partitions[5] }
          , earlyStop, stop );
    } else if ( rightCount < leftCount ) {
      int lateStart = stopLeft - rightCount;
      partitions 
        = evenlyExpandExistingPartitions
          ( vArray, lateStart, stopLeft
          , pivotIndices, startRight, stop);
      partitions 
        = expandPartitionsToLeft
          ( vArray, start, lateStart
          , new int[] 
            { partitions[1], partitions[3]
            , partitions[5] }, stop );
    } else {
      partitions 
        = evenlyExpandExistingPartitions
          ( vArray, start, stopLeft
          , pivotIndices, startRight, stop);
    }
    return MultiPivotUtils
           .dropRedundantPartitions
           ( vArray, partitions );
  }
  protected int[] evenlyExpandExistingPartitions
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int startRight, int stop) {
    //Asumes pivotIndices has length 3
    int leftHole     = pivotIndices[0];
    int middleHole   = pivotIndices[1];
    int rightHole    = pivotIndices[2];
    int vLeftPivot   = vArray[leftHole];
    int vMiddlePivot = vArray[middleHole];
    int vRightPivot  = vArray[rightHole];
    int scanLeft     = stopLeft - 1;
    int scanRight    = startRight;
    for ( ; scanRight<stop
          ; --scanLeft,++scanRight) {
      int vLeft  = vArray[scanLeft];
      int vRight = vArray[scanRight];
      if ( vLeft <= vMiddlePivot ) {
        if ( vRight <= vMiddlePivot) {
          if ( vLeft < vLeftPivot) {
            if ( vRight < vLeftPivot) {
              //both vLeft and vRight in 1st partition
              vArray[leftHole]   = vRight;
              ++leftHole;
              vArray[middleHole] = vArray[leftHole];
            } else {
              //vLeft in 1st partition, vRight in 2nd
              vArray[middleHole] = vRight;
            }
          } else if ( vRight < vLeftPivot ) {
            //vRight in 1st partition, vLeft in 2nd
            vArray[scanLeft]   = vRight;
            vArray[middleHole] = vLeft;
          } else { //both vLeft and vRight in 2nd partition
            vArray[leftHole]   = vLeft;
            --leftHole;
            vArray[scanLeft]   = vArray[leftHole];
            vArray[middleHole] = vRight;
          }
          //both vLeft and vRight are left of the middle pivot
          ++middleHole;
          vArray[rightHole] = vArray[middleHole];
          ++rightHole;
          vArray[scanRight] = vArray[rightHole];
        } else { //vLeft <= vMiddlePivot < vRight
          if ( vLeftPivot <= vLeft ) {
            vArray[leftHole] = vLeft;
            --leftHole;
            vArray[scanLeft] = vArray[leftHole];
          } //else... vLeft < vLeftPivot, so vLeft stays there
          if ( vRight < vRightPivot ) {
            vArray[rightHole] = vRight;
            ++rightHole;
            vArray[scanRight] = vArray[rightHole];
          } //else... vRightPivot <= vRight, so vRight stays there
        }
      } else if ( vRight <= vMiddlePivot) {
        // vRight <= vMiddlePivot < vLeft
        if ( vLeftPivot <= vRight ) {
          vArray[leftHole] = vRight;
          --leftHole;
          vArray[scanLeft] = vArray[leftHole];
        } else {
          vArray[scanLeft] = vRight;
        }
        if ( vLeft < vRightPivot ) {
          vArray[rightHole] = vLeft;
          ++rightHole;
          vArray[scanRight] = vArray[rightHole];
        } else {
          vArray[scanRight] = vLeft;
        }
      } else {
        //both vLeft and vRight are right of the middle pivot
        if ( vLeft < vRightPivot ) {
          if ( vRight < vRightPivot ) {
            //both belong in partition 3
            vArray[middleHole] = vLeft;
            vArray[rightHole]  = vRight;
            ++rightHole;
            vArray[scanRight] = vArray[rightHole];
          } else {        
            //vLeft belongs in partition 3, vRight stays where it is
            vArray[middleHole] = vLeft;
          }
        } else if ( vRight < vRightPivot ) {
          //vRight belongs in partition 3, vLeft in partition 4
          vArray[scanRight] = vLeft;
          vArray[middleHole] = vRight;
        } else {  
          //vLeft and vRight both belong in partition 4
          vArray[rightHole]  = vLeft;
          --rightHole;
          vArray[middleHole] = vArray[rightHole];
        }
        --middleHole;
        vArray[leftHole] = vArray[middleHole];
        --leftHole;
        vArray[scanLeft] = vArray[leftHole];
      }
    }
    if (start==scanLeft) {
      //compare, and place, the last un-compared 
      //element, if (stop-start) was even.
      int vLeft = vArray[scanLeft];
      if ( vLeft <= vMiddlePivot ) {
        if ( vLeft < vLeftPivot ) {
          //All good, vLeft where it ought to be
        } else { //vLeft belongs in 2nd partition
          vArray[leftHole] = vLeft;
          --leftHole;
          vArray[scanLeft] = vArray[leftHole];
        }
      } else {
        if ( vLeft < vRightPivot ) { //vLeft to 3rd partition
          vArray[middleHole] = vLeft;
        } else { //vLeft to 4th partition
          vArray[rightHole]  = vLeft;
          --rightHole;
          vArray[middleHole] = vArray[rightHole];
        }
        --middleHole;
        vArray[leftHole]   = vArray[middleHole];
        --leftHole;
        vArray[scanLeft]   = vArray[leftHole];
      }
    }
    vArray[leftHole]   = vLeftPivot;
    vArray[middleHole] = vMiddlePivot;
    vArray[rightHole]  = vRightPivot;
    return 
      new int[] 
        { start, leftHole, leftHole+1, middleHole
        , middleHole+1, rightHole, rightHole+1, stop };
  }
}
