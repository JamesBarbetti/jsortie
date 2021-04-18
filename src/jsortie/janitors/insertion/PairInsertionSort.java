package jsortie.janitors.insertion;

public class PairInsertionSort 
  extends SentinelInsertionSort {
  @Override
  public String toString() { 
    return this.getClass().getSimpleName(); 
  }
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    if (stop-start<2) {
      return;
    }
    do {
      bubbleMinimumToLeft
        ( vArray, start, stop );
      ++start;
    } while (((stop-start)&1) !=0 );
    int x;
    for (x = start; x<stop; x+=2) {
      int v1 = vArray[x];
      int v2 = vArray[x+1];
      if (v2 < v1) {
        v1 = v2;
        v2 = vArray[x];
      }
      int w=x-1;
      for (; v2<vArray[w]; --w ) {
        vArray[w+2] = vArray[w];
      }
      vArray[w+2] = v2;
      for ( ; v1<vArray[w]; --w ) {
        vArray[w+1] = vArray[w];
      }
      vArray[w+1] = v1;
    }
  }
}
