package jsortie.quicksort.multiway.expander.kangaroo;

public class SkippyCentripetalExpander2 
  extends SkippyExpander2 {
  @Override
  public int[] expandPartitions
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int startRight, int stop) {	
    int countLeft  = stopLeft - start;
    int countRight = stop     - startRight;
    if ( countLeft < countRight ) {
      int earlyStop = startRight + countLeft;
      int[] p = evenlyExpandExistingPartitions
                ( vArray, start, stopLeft
                , pivotIndices, startRight, earlyStop);
      return expandPartitionsToRight
             ( vArray, start
             , new int[] { p[1], p[3] }
             , earlyStop, stop );
    } else if ( countRight < countLeft ) {
      int lateStart = stopLeft - countRight;
      int[] p = evenlyExpandExistingPartitions
                ( vArray, lateStart, stopLeft
                , pivotIndices, startRight, stop);
      return expandPartitionsToLeft
             ( vArray, start, lateStart-1
             , new int[] { p[1], p[3] }, stop );
    } else {
      return evenlyExpandExistingPartitions
             ( vArray, start, stopLeft
             , pivotIndices, startRight, stop);
    }
  }
  protected int[] evenlyExpandExistingPartitions
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int startRight, int stop) {
    int leftHole  = pivotIndices[0];
    int rightHole = pivotIndices[1];
    int vP        = vArray[leftHole];
    int vQ        = vArray[rightHole];		
    int scanLeft  = stopLeft - 1;
    int scanRight = startRight;
    for (; scanRight<stop; --scanLeft,++scanRight) {
      int vLeft        = vArray[scanLeft];
      vArray[rightHole] = vLeft;
      rightHole       -= ( vLeft < vQ ) ? 0 : 1;
      vArray[leftHole]  = vArray[rightHole];
      leftHole        -= ( vLeft < vP)  ? 0 : 1;
      vArray[scanLeft]  = vArray[leftHole];
      
      int vRight       = vArray[scanRight];
      vArray[leftHole]  = vRight;
      leftHole        += ( vRight <= vP) ? 1 : 0;
      vArray[rightHole] = vArray[leftHole];
      rightHole       += ( vRight <= vQ) ? 1 : 0;
      vArray[scanRight] = vArray[rightHole];
    }
    vArray[leftHole]  = vP;
	vArray[rightHole] = vQ;
    return new int[] 
    { start,       leftHole
    , leftHole+1,  rightHole
    , rightHole+1, stop };
  }
}
