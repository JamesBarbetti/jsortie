package jsortie.quicksort.selector.dirty;

import jsortie.quicksort.selector.SinglePivotSelector;

public class DirtyCombSelector 
  implements SinglePivotSelector {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int selectPivotIndex
    ( int[] vArray, int start, int stop ) {
    int i, j;
    while (start+1<stop) {
      for (i=start, j=start+(stop-start+4)/5; j<stop; ++i, ++j) {
      int     vLeft  = vArray[i];
      int     vRight = vArray[j];
      boolean swap   = (vRight < vLeft);
      vArray[i]      = swap ? vRight : vLeft; 
      vArray[j]      = swap ? vLeft  : vRight; 
    }
    stop = i;
      for (i=stop-1-(stop-start+4)/5, j=stop-1; start<=i; --i, --j) {
        int     vLeft  = vArray[i];
        int     vRight = vArray[j];
        boolean swap   = (vRight < vLeft);
        vArray[i]      = swap ? vRight : vLeft; 
        vArray[j]      = swap ? vLeft  : vRight; 
      }
      start = j;
    }
    return start;
  }
}
