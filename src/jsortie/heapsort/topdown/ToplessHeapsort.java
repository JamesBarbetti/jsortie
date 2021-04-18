package jsortie.heapsort.topdown;
import jsortie.heapsort.HeapsortBase;

public class ToplessHeapsort 
  extends HeapsortBase {
  @Override	
  public void constructHeap
    ( int[] vArray, int start, int stop ) {
    int fudge= start - 2;
    for (int h=start+(stop-start)/2;h>=start;--h) {
      int i = h;
      int v = vArray[i];
      int j = i - fudge + i;  
      while (j<stop) {
        if (j+1<stop) {
          j += (vArray[j]<vArray[j+1]) ? 1 : 0;
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
  @Override
  public void extractFromHeap(int[] vArray, int start, int stop) {
    int fudge= start - 2;
    for (--stop;start<=stop;--stop) {
      int v = vArray[stop];
      int i = stop;
      int j = start;
      while (j<stop) {
        if (j+1<stop) {
          j += (vArray[j]<vArray[j+1]) ? 1 : 0;
        }
        if (vArray[j]<=v) {
          break;
        }
        vArray[i]=vArray[j];
        i = j;
        j = i - fudge + i;  
      }
      vArray[i] = v;
    }
  }
}
