package jsortie.heapsort.retro;

import jsortie.heapsort.topdown.HeapsortStandard;

public class HeapsortRecursive 
  extends HeapsortStandard {
  public void constructHeap
    ( int[] vArray, int start, int stop ) {
    constructSubHeap(vArray, start, start, stop);
  }
  public void constructSubHeap ( int[] vArray
    , int start, int i, int stop) {
    //Assumes: start<=i<stop.
    int rowStart = (i-start+1)*radix + start - 1;
    int rowStop  = rowStart + radix;
    if (stop<rowStop) {
      rowStop = stop;
    }
    for (int c=rowStart; c<rowStop; ++c) {
      constructSubHeap(vArray, start, c, stop);
    }
    siftDown(vArray, start, i, stop);
  }
  public void siftDown ( int[] vArray
    , int start, int i, int stop) {    
    int v = vArray[i];
    int j = (i - start+1)*radix + start - 1;
    while (j<stop) {
      if (j+1<stop) {
        j += (vArray[j] < vArray[j+1]) ? 1 : 0;
      }
      if (vArray[j]<=v) {
        break;
      }
      vArray[i]=vArray[j];
      i = j;
      j = (i - start+1)*radix + start - 1;
    }
    vArray[i]=v;
  }
}
