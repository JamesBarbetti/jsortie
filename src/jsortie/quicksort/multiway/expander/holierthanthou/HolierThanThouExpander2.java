package jsortie.quicksort.multiway.expander.holierthanthou;

public class HolierThanThouExpander2 
  extends HolierThanThouExpanderBase {
  public HolierThanThouExpander2() {
    super(2);
  }
  public int[] expandPartitionsToRight
    ( int[] vArray, int start
     , int[] pivotIndices
    , int startRight, int stop) {
    int hole1 = pivotIndices[0];
    int hole2 = pivotIndices[1];
    
    int vP = vArray[hole1];
    int vQ = vArray[hole2];

    for ( int scan=startRight
        ; scan<stop; ++scan) {
      int v = vArray[scan];
      if ( v <= vQ ) {
        vArray[scan] = vArray[hole2+1];
        if ( v <= vP) {
          vArray[hole1]   = v;
          ++hole1;
          vArray[hole2] = vArray[hole1];
        } else {
          vArray[hole2] = v;
        }
        ++hole2;
      }
    }
    vArray[hole1] = vP;
    vArray[hole2] = vQ;
    return new int[] { start, hole1
                     , hole1+1, hole2
                     , hole2+1, stop };
  }  
  public int[] expandPartitionsToLeft 
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int stop) {
    int hole1 = pivotIndices[0];
    int hole2 = pivotIndices[1];
    int vP = vArray[hole1];
    int vQ = vArray[hole2];
    for ( int scan = stopLeft - 1 
        ; start<=scan; --scan) {
      int v = vArray[scan];
      if ( vP <= v) {
        vArray[scan]=vArray[hole1-1];
        if ( vQ <= v) {
          vArray[hole2] = v;
          --hole2;
          vArray[hole1] = vArray[hole2];
        } else {
          vArray[hole1] = v;
        }
        --hole1;
      }
    }
    vArray[hole1] = vP;
    vArray[hole2] = vQ;
    return new int[] { start, hole1
                     , hole1+1, hole2
                     , hole2+1, stop };
  }
}
