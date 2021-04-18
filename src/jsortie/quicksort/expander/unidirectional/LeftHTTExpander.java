package jsortie.quicksort.expander.unidirectional;

public class LeftHTTExpander
  extends HolierThanThouExpander {
  public int expandPartitionToRight
    ( int[] vArray, int hole
    , int startRight, int stop ) {
    int vPivot = vArray[hole];
    for (int scan=startRight;scan<stop;++scan) {
      int v = vArray[scan];
      if ( v < vPivot ) {
        vArray[hole] = v;
        ++hole;
        vArray[scan] = vArray[hole];
      }
    }
    vArray[hole] = vPivot;
    return hole;
  }
}
