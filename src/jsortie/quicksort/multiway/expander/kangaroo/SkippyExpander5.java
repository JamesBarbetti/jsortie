package jsortie.quicksort.multiway.expander.kangaroo;

import jsortie.quicksort.expander.branchavoiding.SkippyExpander;
import jsortie.quicksort.multiway.expander.holierthanthou.HolierThanThouExpanderBase;

public class SkippyExpander5 
  extends HolierThanThouExpanderBase {
  public SkippyExpander5() {
    super(5, new SkippyExpander());
  }
  @Override
  public int[] expandPartitionsToRight
    ( int[] vArray, int start
    , int[] pivotIndices, int startRight, int stop) {
    int hole1 = pivotIndices[0];
    int hole2 = pivotIndices[1];
    int hole3 = pivotIndices[2];
    int hole4 = pivotIndices[3];
    int hole5 = pivotIndices[4];
    int vP    = vArray[hole1];
    int vQ    = vArray[hole2];
    int vR    = vArray[hole3];
    int vS    = vArray[hole4];
    int vT    = vArray[hole5];
    for (int scan=startRight; scan<stop; ++scan) {
      int vScan     = vArray[scan];      
      vArray[hole1] = vScan;
      hole1        += ( vScan <= vP ) ? 1 : 0;
      vArray[hole2] = vArray[hole1];
      hole2        += ( vScan <= vQ ) ? 1 : 0;
      vArray[hole3] = vArray[hole2];
      hole3        += ( vScan <= vR ) ? 1 : 0;
      vArray[hole4] = vArray[hole3];
      hole4        += ( vScan <= vS ) ? 1 : 0;
      vArray[hole5] = vArray[hole4];
      hole5        += ( vScan <= vT ) ? 1 : 0;
      vArray[scan]  = vArray[hole5];
    }    
    vArray[hole1] = vP; 
    vArray[hole2] = vQ; 
    vArray[hole3] = vR;
    vArray[hole4] = vS;
    vArray[hole5] = vT;	
    return new int[] { start, hole1, hole1+1, hole2, hole2+1, hole3
      , hole3+1, hole4, hole4+1, hole5, hole5+1, stop };
  }
  @Override
  public int[] expandPartitionsToLeft
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int stop) {
    int hole1 = pivotIndices[0];
    int hole2 = pivotIndices[1];
    int hole3 = pivotIndices[2];
    int hole4 = pivotIndices[3];
    int hole5 = pivotIndices[4];
    int vP    = vArray[hole1];
    int vQ    = vArray[hole2];
    int vR    = vArray[hole3];
    int vS    = vArray[hole4];
    int vT    = vArray[hole5];
    for (int scan=stopLeft-1; start<=scan; --scan) {
      int vScan     = vArray[scan];
      vArray[hole5] = vScan;
      hole5        -= ( vT <= vScan ) ? 1 : 0;
      vArray[hole4] = vArray[hole5];
      hole4        -= ( vS <= vScan ) ? 1 : 0;
      vArray[hole3] = vArray[hole4];
      hole3        -= ( vR <= vScan ) ? 1 : 0;
      vArray[hole2] = vArray[hole3];
      hole2        -= ( vQ <= vScan ) ? 1 : 0;
      vArray[hole1] = vArray[hole2];
      hole1        -= ( vP <=  vScan ) ? 1 : 0;
      vArray[scan]  = vArray[hole1];
    }
    vArray[hole1] = vP; 
    vArray[hole2] = vQ; 
    vArray[hole3] = vR;
    vArray[hole4] = vS;
    vArray[hole5] = vT;	
    return new int[] { start, hole1, hole1+1, hole2, hole2+1, hole3
      , hole3+1, hole4, hole4+1, hole5, hole5+1, stop };
  }
}