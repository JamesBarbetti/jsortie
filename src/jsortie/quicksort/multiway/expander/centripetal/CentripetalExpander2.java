package jsortie.quicksort.multiway.expander.centripetal;


import jsortie.quicksort.multiway.expander.holierthanthou.HolierThanThouExpander2;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class CentripetalExpander2
  extends HolierThanThouExpander2 {
  @Override
  public int[] expandPartitions
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int startRight, int stop) {
    int[] partitions;
    int   leftCount  = stopLeft-start;
    int   rightCount = stop-startRight;
    if ( pivotIndices.length < 2 ) {
      partitions 
        = expandSinglePivotPartition
          ( vArray, start, stopLeft, pivotIndices[0]
          , startRight, stop);
    } else if ( leftCount < rightCount ) {
      int earlyStop = startRight + leftCount;
      partitions 
        = evenlyExpandExistingPartitions
          ( vArray, start, stopLeft
          , pivotIndices, startRight, earlyStop);
      partitions 
        = expandPartitionsToRight
          ( vArray, start
          , new int[] { partitions[1], partitions[3] }
          , earlyStop, stop );
    } else if ( rightCount < leftCount ) {
      int lateStart = stopLeft - rightCount;
      int[] pivots 
        = evenlyExpandExistingPartitions
          ( vArray, lateStart, stopLeft
          , pivotIndices, startRight, stop);
      partitions 
        = expandPartitionsToLeft
          ( vArray, start, lateStart
          , new int[] { pivots[1], pivots[3] }, stop );
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
    int leftHole  = pivotIndices[0];
    int rightHole = pivotIndices[1];
    int vP = vArray[leftHole];
    int vQ = vArray[rightHole];
    int scanLeft = stopLeft -1;
    int scanRight = startRight;
    for (; scanRight<stop; --scanLeft,++scanRight) {
      //Here cases are of the form [A-C][A-C]:
      //the first letter indicates where the left
      //item, vLeft, belongs (A=left, B=middle, C=right).
      //The second letter, where the right item,
      //vRight, belongs.
      int vLeft  = vArray[scanLeft];
      int vRight = vArray[scanRight];
      if ( vP < vLeft ) { //vLeft must move
        if ( vQ <= vRight ) { //vRight need not move
          if ( vLeft < vQ ) { 
            //Case(BC): vLeft must 
            //go to middle partition
            vArray[leftHole] = vLeft; 
          } else { 
            //Case(CC): vLeft must
            //move to right partition
            vArray[rightHole] = vLeft;
            --rightHole;
            vArray[leftHole] = vArray[rightHole];
          }
          --leftHole;
          vArray[scanLeft] = vArray[leftHole];
        } else { 
          //both vLeft and vRight will 
          //have to move
          if ( vQ < vLeft ) {
            if ( vRight < vP ) {
              //Case(CA): vLeft and vRight 
              //need to be swapped.
              vArray[scanLeft]  = vRight;
              vArray[scanRight] = vLeft;
            } else { 
              //Case(CB): vLeft goes to right, 
              //vRight goes to middle
              vArray[scanRight] = vLeft;
              vArray[leftHole] = vRight;
              --leftHole;
              vArray[scanLeft] = vArray[leftHole];
            }
          } else if ( vRight < vP ) { 
            //Case(BA), vRight to left, 
            //vLeft to middle
            vArray[scanLeft]  = vRight;
            vArray[rightHole] = vLeft;
            ++rightHole;
            vArray[scanRight] = vArray[rightHole];
          } else { 
            //Case(BB): vRight and vLeft 
            //both go to middle
            vArray[leftHole]  = vLeft;
            --leftHole;
            vArray[scanLeft] = vArray[leftHole];
            vArray[rightHole] = vRight;
            ++rightHole;
            vArray[scanRight] = vArray[rightHole];
          }
        }
      } else if ( vRight < vQ ) { 
        //vRight must move, vLeft need not
        if ( vP < vRight ) { 
          //Case(AB): vRight in middle partition
          vArray[rightHole] = vRight;
        } else { 
          //Case(AA): vRight goes 
          //to left partition
          vArray[leftHole] = vRight;
          ++leftHole;
          vArray[rightHole] = vArray[leftHole];
        }
        ++rightHole;
        vArray[scanRight] = vArray[rightHole];
      } //else... case (AC): neither 
        //vLeft nor vRight needs to move
    }		
    //Put the pivots in the holes
    vArray[leftHole]  = vP;
    vArray[rightHole] = vQ;	
    return new int[] 
      { start, leftHole, leftHole+1, rightHole
      , rightHole+1, stop };
  }
}
