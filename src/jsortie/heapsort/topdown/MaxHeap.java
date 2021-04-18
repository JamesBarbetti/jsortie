package jsortie.heapsort.topdown;

public class MaxHeap {
  public    int[] vArray;
  public    int   start;
  public    int   stop;
  protected int   size;
  protected int   stopLess1 = stop-1;
  public MaxHeap
    ( int[] vArrayToUse
    , int rangeStart, int rangeStop ) {
    vArray    = vArrayToUse;
    start     = rangeStart;
    stop      = rangeStop;
    size      = stop - start;
    stopLess1 = stop -1;
    constructHeap();
  }
  public void constructHeap() {
    for (int h=size/2;h>=1;--h) { 
      int i = h;
      int v = vArray[stop-i];
      int j = i + i;  
      while (j<=size) {
        if (j<size) {
          j += (vArray[stop-j] < vArray[stop-j-1]) ? 1 : 0;
        }
        if ( vArray[stop-j] <= v ) {
          break;
        }
        vArray[stop-i] = vArray[stop-j];
        i = j;
        j = i + i;
      }
      vArray[stop-i] = v;
    }
  }
  public boolean pushDownItemAt(int x) {
    int i = stop-x;
    int v = vArray[x];
    for (int j=i+i; j<=size; j = i + i) { 
      if (j<size) {
        j += (vArray[stop-j] < vArray[stopLess1-j]) ? 1 : 0;
      }
      if ( vArray[stop-j] <= v ) {
        break;
      }
      vArray[stop-i] = vArray[stop-j];
      i = j;
    }
    vArray[stop-i] = v;
    return (i!=stop-x);
  }
}
