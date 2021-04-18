package jsortie.quicksort.multiway.expander.holierthanthou;

public class HolierThanThouExpander5 
  extends HolierThanThouExpanderBase {
  public HolierThanThouExpander5() {
    super(5);
  }
  @Override
  public int[] expandPartitionsToRight
    ( int[] vArray, int start, int[] pivotIndices
    , int startRight, int stop ) {
    int hole1 = pivotIndices[0];
    int hole2 = pivotIndices[1];
    int hole3 = pivotIndices[2];  
    int hole4 = pivotIndices[3]; 
    int hole5 = pivotIndices[4]; //fifth hole

    int vP    = vArray[hole1];
    int vQ    = vArray[hole2];
    int vR    = vArray[hole3];  
    int vS    = vArray[hole4]; 
    int vT    = vArray[hole5]; //fifth pivot
    
    for (int scan=startRight; scan<stop; ++scan) {
      int v = vArray[scan];
      if ( v <= vR ) {
        if ( v < vQ ) {
          if ( v <= vP) {
            vArray[hole1] = v;
            ++hole1;
            vArray[hole2] = vArray[hole1];
          } else {
            vArray[hole2] = v;
          }
          ++hole2;
          vArray[hole3] = vArray[hole2];
        } else {
          vArray[hole3] = v;
        }
        ++hole3;
        vArray[hole4] = vArray[hole3];
        ++hole4;
        vArray[hole5] = vArray[hole4];
        ++hole5;
        vArray[scan] = vArray[hole5];
      } else if ( v < vS) {
        vArray[hole4] = v;
        ++hole4;
        vArray[hole5] = vArray[hole4];
        ++hole5;
        vArray[scan]  = vArray[hole5];
      } else if ( v <= vT) {
        vArray[hole5] = v;
        ++hole5;
        vArray[scan]  = vArray[hole5];
      }
    }
    vArray[hole1] = vP;
    vArray[hole2] = vQ;
    vArray[hole3] = vR;
    vArray[hole4] = vS;
    return new int[]
      { start, hole1, hole1+1, hole2, hole2+1
      , hole3, hole3+1, hole4, hole4+1, stop };
  }
  
  @Override
  public int[] expandPartitionsToLeft
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int stop) {
    int hole1 = pivotIndices[0];
    int hole2 = pivotIndices[1];
    int hole3 = pivotIndices[2];
    int hole4 = pivotIndices[3];
    int hole5 = pivotIndices[4]; //fourth hole
    int vP = vArray[hole1];
    int vQ = vArray[hole2];
    int vR = vArray[hole3];
    int vS = vArray[hole4];
    int vT = vArray[hole5];
    for (int scan=stopLeft-1; start<=scan; --scan) {
      int v = vArray[scan];
      if ( vR <= v ) {
        if ( vQ < v ) {
          if ( vP <= v ) {
            vArray[hole1] = v;
            --hole1;
            vArray[hole2] = vArray[hole1];
          } else {
            vArray[hole2] = v;
          }
          --hole2;
          vArray[hole3] = vArray[hole2];
        } else {
          vArray[hole3] = v;
        }
        --hole3;
        vArray[hole4] = vArray[hole3];
        --hole4;
        vArray[hole5] = vArray[hole4];
        --hole5;
        vArray[scan] = vArray[hole5];
      } else if ( vS < v) {
        vArray[hole4] = v;
        --hole4;
        vArray[hole5] = vArray[hole4];
        --hole5;
        vArray[scan]  = vArray[hole5];
      } else if ( vT <= v) {
        vArray[hole5] = v;
        ++hole5;
        vArray[scan]  = vArray[hole5];
      }
    }
    vArray[hole1] = vP;
    vArray[hole2] = vQ;
    vArray[hole3] = vR;
    vArray[hole4] = vS;
    vArray[hole5] = vT;
    return new int[] 
      { start, hole2, hole2+1, hole3, hole3+1
      , hole4, hole4+1, stop };
  }
}
