package jsortie.janitors.insertion;

public class PairInsertionSortRevised 
  extends PairInsertionSort {
  public void sortRange(int[] vArray, int start, int stop) {
    if (stop-start<2) {
      return;
    }
    do {
      bubbleMinimumToLeft(vArray, start, stop);
      ++start;
    } while (((stop-start)&1) !=0 );
    int x;
    for (x = start; x<stop; x+=2) {
      int vLeft    = vArray[x];
      int vRight   = vArray[x+1];
      boolean swap = vRight < vLeft;
      int vLow     = swap ? vRight : vLeft;
      int vHigh    = swap ? vLeft  : vRight;
      int w=x-1;
      for (; vHigh<vArray[w]; --w ) {
        vArray[w+2] = vArray[w];
      }
      vArray[w+2] = vHigh;
      for ( ; vLow<vArray[w]; --w ) {
        vArray[w+1] = vArray[w];
      }
      vArray[w+1] = vLow;
    }
  }
}
