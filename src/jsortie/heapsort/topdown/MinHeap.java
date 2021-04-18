package jsortie.heapsort.topdown;

public class MinHeap {
  public int[] vArray;
  public int   start; 
  public int   stop;
  public int   fudge;
  public MinHeap
    ( int[] vArrayToUse
    , int rangeStart, int rangeStop ) {
    vArray = vArrayToUse;
    start  = rangeStart;
    fudge  = start - 1;
    stop   = rangeStop;
    constructHeap();
  }
  public void constructHeap() {
    for (int h=start+(stop-start+1)/2;h>=start;--h) {
      int i = h;
      int v = vArray[i];
      int j = i - fudge + i;  
      while (j<stop) {
        if (j+1<stop) {
          j += ( vArray[j+1] < vArray[j]) ? 1 : 0;
        }
        if ( v <= vArray[j] ) {
          break;
        }
        vArray[i]=vArray[j];
        i = j;
        j = i - fudge + i;
      }
      vArray[i]=v;
    }
  }
  public boolean pushDownItemAt(int i) {
    //Note: assumes start<=i<stop
    int h = i;
    int v = vArray[i];
    for ( int j = i + i - fudge; j<stop
        ; j = i + i - fudge ) {
      if (j+1<stop) {
        j += ( vArray[j+1] < vArray[j] ) ? 1 : 0;
      }
      if ( v <= vArray[j] ) { 
        break;
      }
      vArray[i] = vArray[j];
      i = j;
    }
    vArray[i] = v;
    return (h!=i);
  }
}
