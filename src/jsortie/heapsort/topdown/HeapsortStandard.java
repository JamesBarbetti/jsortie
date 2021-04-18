package jsortie.heapsort.topdown;

import jsortie.heapsort.HeapsortBase;

public class HeapsortStandard 
  extends HeapsortBase {
  public void constructHeap
    ( int[] vArray, int start, int stop ) {
    int fudge = start - 1;    
    //heap construction phase
    for (int h=start+(stop-start+1)/2;h>=start;--h) { 
      int i = h;
      int v = vArray[i];
      int j = i - fudge + i;	
      while (j<stop) {
        if (j+1<stop) {
          j += (vArray[j] < vArray[j+1]) ? 1 : 0;
        }
        if (vArray[j]<=v) {
          break;
        }
        vArray[i]=vArray[j];
        i = j;
        j = i - fudge + i;
      }
      vArray[i]=v;			
    }
  }
  public void pushDownItemAtStart
    ( int[] vArray, int start, int stop) {
    int i = start;
    int j = start + 1;
    int fudge = start - 1;
    int v = vArray[start];
    while (j<stop) { //different: will, on average, iterate one extra time per value of stop
      if (j+1<stop) {
        j += (vArray[j] < vArray[j+1]) ? 1 : 0;
      }
      if (vArray[j]<=v) { 
        break;
      }
      vArray[i] = vArray[j];
      i = j;
      j = i - fudge + i;  
    }
    vArray[i] = v;
  }
  public void extractFromHeap
    ( int[] vArray, int start, int stop ) {
    int fudge = start - 1;    
    //heap extraction phase
    for (--stop;stop>start;stop--) {
      int v = vArray[stop];
      vArray[stop] = vArray[start]; 
      int i = start;
      int j = start + 1;
      while (j<stop) { //different: will, on average, iterate one extra time per value of stop
        if (j+1<stop) {
          j += (vArray[j] < vArray[j+1]) ? 1 : 0;
        }
        if (vArray[j]<=v) { 
          break;
        }
        vArray[i] = vArray[j];
        i = j;
        j = i - fudge + i;	
      }
      vArray[i] = v;
    }
  }
}
