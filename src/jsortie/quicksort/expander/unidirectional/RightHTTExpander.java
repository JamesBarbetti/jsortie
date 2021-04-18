package jsortie.quicksort.expander.unidirectional;

public class RightHTTExpander 
  extends HolierThanThouExpander {
  public int expandPartitionToLeft
    ( int[] vArray, int start
    , int stopLeft, int hole ) {
    int vPivot = vArray[hole];
    for (int scan=stopLeft-1;start<=scan;--scan) {
      int v = vArray[scan];
      if ( vPivot < v) {
        vArray[hole] = v;
        --hole;       
        vArray[scan] = vArray[hole];
      }
    }
    vArray[hole] = vPivot;
    return hole;
  }
}
