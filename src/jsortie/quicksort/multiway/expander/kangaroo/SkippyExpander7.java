package jsortie.quicksort.multiway.expander.kangaroo;

import jsortie.quicksort.expander.branchavoiding.SkippyExpander;
import jsortie.quicksort.multiway.expander.holierthanthou.HolierThanThouExpanderBase;

public class SkippyExpander7 
  extends HolierThanThouExpanderBase {
  public SkippyExpander7() {
    super(7, new SkippyExpander());
  }
  @Override
  public int[] expandPartitionsToLeft
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int stop) {
    int hole1 = pivotIndices[0];
    int vP    = vArray[hole1];
    int hole2 = pivotIndices[1];
    int vQ    = vArray[hole2];
    int hole3 = pivotIndices[2];
    int vR    = vArray[hole3];
    int hole4 = pivotIndices[3];
    int vS    = vArray[hole4];
    int hole5 = pivotIndices[4];
    int vT    = vArray[hole5];
    int hole6 = pivotIndices[5];
    int vU    = vArray[hole6];
    int hole7 = pivotIndices[6];
    int vV    = vArray[hole7];
    for (int scan=stopLeft-1; start<scan; --scan) {
      int vScan      = vArray[scan];
      vArray[hole7]  = vScan;
      hole7         += ( vV <= vScan ) ? 1 : 0;
      vArray[hole6]  = vArray[hole7];
      hole6         += ( vU <= vScan ) ? 1 : 0;
      vArray[hole5]  = vArray[hole6];
      hole5         += ( vT <= vScan ) ? 1 : 0;
      vArray[hole4]  = vArray[hole5];
      hole4         += ( vS <= vScan ) ? 1 : 0;
      vArray[hole3]  = vArray[hole4];
      hole3         += ( vR <= vScan ) ? 1 : 0;
      vArray[hole2]  = vArray[hole3];
      hole2         += ( vQ <= vScan ) ? 1 : 0;
      vArray[hole1]  = vArray[hole2];
      hole1         += ( vP <= vScan ) ? 1 : 0;
      vArray[scan]   = vArray[hole1];
    }
    vArray[hole1] = vP;
    vArray[hole2] = vQ;
    vArray[hole3] = vR;
    vArray[hole4] = vS;
    vArray[hole5] = vT;
    vArray[hole6] = vU;
    vArray[hole7] = vV;
    return new int[] { start,   hole1, hole1+1, hole2, hole2+1, hole3
    		         , hole3+1, hole4, hole4+1, hole5, hole5+1, hole6
    		         , hole6+1, hole7, hole7+1, stop };
  }
  @Override
  public int[] expandPartitionsToRight
    ( int[] vArray, int start
    , int[] pivotIndices, int startRight, int stop) {
    int hole1 = pivotIndices[0];
    int vP    = vArray[hole1];
    int hole2 = pivotIndices[1];
    int vQ    = vArray[hole2];
    int hole3 = pivotIndices[2];
    int vR    = vArray[hole3];
    int hole4 = pivotIndices[3];
    int vS    = vArray[hole4];
    int hole5 = pivotIndices[4];
    int vT    = vArray[hole5];
    int hole6 = pivotIndices[5];
    int vU    = vArray[hole6];
    int hole7 = pivotIndices[6];
    int vV    = vArray[hole7];
    for (int scan=startRight; scan<stop; ++scan) {
      int vScan      = vArray[scan];
      vArray[hole1]  = vScan;
      hole1         += ( vScan <= vP ) ? 1 : 0;
      vArray[hole2]  = vArray[hole1];
      hole2         += ( vScan <= vQ ) ? 1 : 0;
      vArray[hole3]  = vArray[hole2];
      hole3         += ( vScan <= vR ) ? 1 : 0;
      vArray[hole4]  = vArray[hole3];
      hole4         += ( vScan <= vS ) ? 1 : 0;
      vArray[hole5]  = vArray[hole4];
      hole5         += ( vScan <= vT ) ? 1 : 0;
      vArray[hole6]  = vArray[hole5];
      hole6         += ( vScan <= vU ) ? 1 : 0;
      vArray[hole7]  = vArray[hole6];
      hole7         += ( vScan <= vV ) ? 1 : 0;
      vArray[scan]   = vArray[hole7];
    }
    vArray[hole1] = vP;
    vArray[hole2] = vQ;
    vArray[hole3] = vR;
    vArray[hole4] = vS;
    vArray[hole5] = vT;
    vArray[hole6] = vU;
    vArray[hole7] = vV;
    return new int[] { start,   hole1, hole1+1, hole2, hole2+1, hole3
                     , hole3+1, hole4, hole4+1, hole5, hole5+1, hole6
                     , hole6+1, hole7, hole7+1, stop };
  }
}
