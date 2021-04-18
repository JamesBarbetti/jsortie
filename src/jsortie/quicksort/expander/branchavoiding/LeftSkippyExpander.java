package jsortie.quicksort.expander.branchavoiding;

public class LeftSkippyExpander 
  extends SkippyExpander {
  @Override
  public int expandPartitionToRight
    ( int[] vArray, int hole
    , int startRight, int stop ) {
    int vPivot = vArray[hole];
    for (int scan=startRight;scan<stop;++scan) {
      int v = vArray[scan];
      vArray[hole] = v;
      hole += ( v < vPivot ) ? 1 : 0;
      vArray[scan] = vArray[hole];
    }
    vArray[hole] = vPivot;
    return hole;
  }
}
