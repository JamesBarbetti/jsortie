package jsortie.janitors.insertion;

import jsortie.StableRangeSorter;

public class PessimisticInsertionSort 
  implements StableRangeSorter {
  @Override
  public String toString() { 
    return this.getClass().getSimpleName(); 
  }
  public void sortRange(int[] vArray, int start, int stop) {
    for (int place=start+1; place<stop; ++place) {
      int v = vArray[place];
      if ( v < vArray[start] ) {
        for (int shift=place-1; start<=shift; --shift) {
          vArray[shift+1] = vArray[shift];
        }
        vArray[start] = v;
      } else if ( v < vArray[place-1]) {
        int scan = place-1;
        for (; v<vArray[scan]; --scan) {
          vArray[scan+1] = vArray[scan];
        }
        vArray[scan+1] = v;
      }
    }
  }
}
