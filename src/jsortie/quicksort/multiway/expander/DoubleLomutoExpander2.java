package jsortie.quicksort.multiway.expander;

import jsortie.quicksort.multiway.expander.holierthanthou.HolierThanThouExpander2;

public class DoubleLomutoExpander2 
  extends HolierThanThouExpander2 {
  @Override
  public int[] expandPartitionsToRight
    ( int[] vArray, int start, int[] pivotIndices
    , int startRight, int stop) {
    if (stop-startRight<2) {
      //Too hard.  Bail out.
      return super.expandPartitionsToRight
             ( vArray, start, pivotIndices, startRight, stop );
    }
    int i = pivotIndices.length; //Assumed: at least 2.
    int boundary1 = pivotIndices[(i-1)/3];
    int boundary2 = pivotIndices[(i+i+1)/3];
    int vP = vArray[boundary1];
    int vQ = vArray[boundary2];
    --boundary2;
    vArray[boundary1]    = vArray[boundary2];
    vArray[boundary2]    = vArray[startRight-2];
    vArray[boundary2+1]  = vArray[startRight-1];
    vArray[startRight-2] = vArray[stop-2];
    vArray[startRight-1] = vArray[stop-1];
    int stopLess2       = stop - 2;
    //For now, vArray[stop-2] and vArray[stop-1] are duplicated,
    //and vP and vQ aren't in the array.  We'll tidy that up later.
    for (int scan=startRight-2; scan<stopLess2; ++scan ) {
      int v = vArray[scan];
      if ( v <= vQ ) {
        vArray[scan] = vArray[boundary2];
        if ( v <= vP ) {
          vArray[boundary2] = vArray[boundary1];
          vArray[boundary1] = v;
          ++boundary1;
        } else {
          vArray[boundary2] = v;
        }
        ++boundary2;
      }
    }
    vArray[stop-1]      = vArray[boundary2+1];
    vArray[stop-2]      = vArray[boundary2];
    vArray[boundary2+1] = vQ;
    vArray[boundary2]   = vArray[boundary1];
    vArray[boundary1]   = vP;
    return new int[] 
      { start, boundary1, boundary1+1, boundary2+1
      , boundary2+2, stop };
    
  }
  @Override
  public int[] expandPartitionsToLeft 
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int stop) {
    if (stopLeft-start<2) {
      //Too hard. Bail out
      return super.expandPartitionsToLeft
             ( vArray, start, stopLeft, pivotIndices, stop);
    }
    int i = pivotIndices.length; //Assumed: at least 2.
    int boundary1 = pivotIndices[(i-1)/3];
    int boundary2 = pivotIndices[(i+i+1)/3];
    int vP = vArray[boundary1];
    int vQ = vArray[boundary2];
    ++boundary1;
    vArray[boundary2]   = vArray[boundary1];
    vArray[boundary1]   = vArray[stopLeft+1];
    vArray[boundary1-1] = vArray[stopLeft];
    vArray[stopLeft+1]  = vArray[start+1];
    vArray[stopLeft]    = vArray[start];
    int startPlus2 = start + 2;
    //For now, vArray[start] and vArray[start+1] are duplicated,
    //and vP and vQ aren't in the array.  We'll tidy that up later.
    for (int scan=stopLeft+1;startPlus2<=scan;--scan) {
      int v = vArray[scan];
      if ( vP <= v) {
        vArray[scan] = vArray[boundary1];
        if ( vQ <= v) {
          vArray[boundary1] = vArray[boundary2];
          vArray[boundary2] = v;
          --boundary2;
        } else {
          vArray[boundary1] = v;
        }
        --boundary1;
      }
    }
    vArray[start]       = vArray[boundary1-1];
    vArray[start+1]     = vArray[boundary1];
    vArray[boundary1-1] = vP;
    vArray[boundary1]   = vArray[boundary2];
    vArray[boundary2]   = vQ;
    return new int[] 
        { start, boundary1-1, boundary1, boundary2
        , boundary2+1, stop };
  }  
}

