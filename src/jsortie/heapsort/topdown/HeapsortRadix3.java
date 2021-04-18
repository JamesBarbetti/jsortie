package jsortie.heapsort.topdown;

import jsortie.heapsort.HeapsortBase;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;

public class HeapsortRadix3 
  extends HeapsortBase {
  public HeapsortRadix3() {
    radix = 3;
  }  
  @Override
  public void constructHeap(int[] vArray, int start, int stop) {
    int h2 = start + ((stop - 1 - start) / radix ) * radix;
    int h  = start + (h2 - start) / radix - 1;
    for (;h>=start;--h, h2-=radix) {
      siftDown(vArray, start, h, h2, stop);
    }
  }
  @Override
  public void extractFromHeap
    ( int[] vArray, int start, int stop ) {
    int threshold = start + (radix+1)*radix;
    for (--stop;threshold<=stop;--stop) {
      siftDown(vArray, start, stop, start, stop);
    }
    InsertionSort2Way.sortSmallRange(vArray, start, stop+1);
  }
  protected  void siftDown ( int[] vArray, int start
                           , int i, int j, int stop) {
    int v = vArray[i];
    do {
      if (j<stop-2) {
      	int k = j;
        ++k; j = ( vArray[j] < vArray[k] ) ? k : j;
        ++k; j = ( vArray[j] < vArray[k] ) ? k : j;
      } else if (j<stop-1) {
    	j += ( vArray[j]   < vArray[j+1] ) ? 1 : 0;
      }
      if (vArray[j]<=v) {
        break;
      }
      vArray[i] = vArray[j];
      i = j;
      //These next three steps are equivalent to setting:
      //j = start + (i - start + 1 ) * radix, for radix=3
      //but run (quite a lot!) faster.
      j  = ( i - start + 1);
      j += j + j;
      j += start;
    } while (j<stop);
    vArray[i]=v;				  
  }			
}	
