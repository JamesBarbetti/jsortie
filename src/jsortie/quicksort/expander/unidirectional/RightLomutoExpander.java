package jsortie.quicksort.expander.unidirectional;

public class RightLomutoExpander 
  extends LomutoExpander {
  @Override
  public int expandPartitionToLeft
    ( int[] vArray
    , int start, int stopLeft, int hole ) {
    int vPivot      = vArray[hole]; 
    int lastOnRight = hole; 
    for (int scan=stopLeft-1;start<=scan;--scan) {
      int v = vArray[scan];
      if ( vPivot < v ) {
        --lastOnRight;
        vArray[scan]        = vArray[lastOnRight];
        vArray[lastOnRight] = v;
      }
    }
    vArray[hole]        = vArray[lastOnRight];
    vArray[lastOnRight] = vPivot;
    return lastOnRight;
  }
}
