package jsortie.heapsort.retro;

import jsortie.heapsort.topdown.HeapsortStandard;

public class HeapsortOriginal
  extends HeapsortStandard {
  public void constructHeap
    ( int[] vArray, int start, int stop ) {
    int count = stop-start;
    int base  = start - 1;
    for (int j=2;j<=count;++j) {
      int v = vArray[base+j];
      int i = j;
      do {
        int h = i/2;
        if (v < vArray[base+h] ) {
          break;
        }
        vArray[base+i] = vArray[base+h];
        i = h;
      } while (1<i);
      vArray[base+i] = v;
    }
  }
}
