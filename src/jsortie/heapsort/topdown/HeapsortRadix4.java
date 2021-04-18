package jsortie.heapsort.topdown;

public class HeapsortRadix4 
  extends HeapsortRadix3
{
  public HeapsortRadix4() {
    radix = 4;
  }
  @Override
  protected  void siftDown(int[] vArray, int start, int i, int j, int stop) {
    int v = vArray[i];
    do {
      int k = j;
      if ( j<stop-3) {
        ++k; j = ( vArray[j] < vArray[k] ) ? k : j;
        ++k; j = ( vArray[j] < vArray[k] ) ? k : j;
        ++k; j = ( vArray[j] < vArray[k] ) ? k : j;
      } else if (j<stop-2) {
        ++k; j = ( vArray[j] < vArray[k] ) ? k : j;
        ++k; j = ( vArray[j] < vArray[k] ) ? k : j;
      } else if (j<stop-1) {
    	j += ( vArray[j] < vArray[j+1] ) ? 1 : 0;
      }
      if (vArray[j]<=v) {
        break;
      }
      vArray[i] = vArray[j];
      i = j;
      //The following is equivalent to setting:
      //j = start + (i - start + 1 ) * radix, for radix=4...
      //but run faster.
      j = (i - start + 1);
      j += j;
      j += j;
      j += start;
    } while (j<stop);
    vArray[i]=v;
  }			  
}
