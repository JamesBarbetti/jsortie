package jsortie.quicksort.expander.branchavoiding;

public class RightSkippyExpander 
  extends SkippyExpander {
  @Override
  public int expandPartitionToLeft
    ( int[] vArray, int start
    , int stopLeft, int hole ) {
    int vPivot = vArray[hole];
    for (int scan=stopLeft-1;start<=scan;--scan) {
      int v = vArray[scan];
      vArray[hole] = v;
      hole -= ( vPivot < v ) ? 1 : 0;
      vArray[scan] = vArray[hole];
    }
    vArray[hole] = vPivot;
    return hole;
  }
}
