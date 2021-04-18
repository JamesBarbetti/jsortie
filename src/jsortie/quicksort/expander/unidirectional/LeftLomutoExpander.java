package jsortie.quicksort.expander.unidirectional;

public class LeftLomutoExpander 
  extends LomutoExpander {
  @Override
  public int expandPartitionToRight
    ( int[] vArray
    , int hole, int startRight, int stop) {
    int vPivot = vArray[hole]; 
    int lastOnLeft = hole; 
    for (int scan=startRight;scan<stop;++scan) {
      int v = vArray[scan];
      if ( v < vPivot ) {
        ++lastOnLeft;
        vArray[scan]       = vArray[lastOnLeft];
        vArray[lastOnLeft] = v;
      }
    }
    vArray[hole]       = vArray[lastOnLeft];
    vArray[lastOnLeft] = vPivot;
    return lastOnLeft;
  }
}
