package jsortie.quicksort.multiway.expander.kangaroo;

import jsortie.quicksort.expander.branchavoiding.SkippyExpander;
import jsortie.quicksort.multiway.expander.holierthanthou.HolierThanThouExpanderBase;

public class SkippyExpander2 
  extends HolierThanThouExpanderBase {
  public SkippyExpander2() {
    super(2, new SkippyExpander());
  }
  @Override
  public int[] expandPartitionsToRight
    ( int[] vArray, int start
    , int[] pivotIndices, int startRight, int stop) {
    int hole1 = pivotIndices[0];
    int hole2 = pivotIndices[1];
    int vP    = vArray[hole1];
    int vQ    = vArray[hole2];
    for (int scan=startRight; scan<stop; ++scan) {
      int vScan    = vArray[scan];
      vArray[hole1] = vScan;
      hole1       += ( vScan <= vP) ? 1 : 0;
      vArray[hole2] = vArray[hole1];
      hole2       += ( vScan <= vQ) ? 1 : 0;
      vArray[scan]  = vArray[hole2];
    }    
    vArray[hole1] = vP;
    vArray[hole2] = vQ;
    return new int[] { start, hole1
      , hole1+1, hole2, hole2+1, stop };
  }
  @Override
  public int[] expandPartitionsToLeft
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int stop) {
    int hole1 = pivotIndices[0];
    int hole2 = pivotIndices[1];	    
    int vP    = vArray[hole1];
    int vQ    = vArray[hole2];
    for (int scan=stopLeft-1; start<=scan; --scan) {
      int vScan    = vArray[scan];
      vArray[hole2] = vScan;
      hole2       -= ( vQ <= vScan) ? 1 : 0;
      vArray[hole1] = vArray[hole2];
      hole1       -= ( vP <= vScan) ? 1 : 0;
      vArray[scan]  = vArray[hole1];
    }
    vArray[hole1] = vP;
    vArray[hole2] = vQ;
    return new int[] { start, hole1, hole1+1, hole2, hole2+1, stop };
  }
}
