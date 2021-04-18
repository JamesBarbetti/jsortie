package jsortie.object.heapsort;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;

public class ObjectHeapSortClassical<T>
  implements ObjectRangeSorter<T> {
  public String toString() { 
    return this.getClass().getSimpleName(); }
  public void sortRange
    ( Comparator<? super T> comparator, T [] vArray
    , int start, int stop) {
    if (stop<=start+1) return;
    int fudge= start - 1; //different
    int i;
    int j;
    T v;
    for (int h=start+(stop-start+1)/2;h>=start;--h) { 
      //1 additional iteration
      i = h;
      v = vArray[i];
      j = i - fudge + i;	
      while (j<stop) {
        if (j<stop-1) 
          j+= (comparator.compare(vArray[j],vArray[j+1])<0) ? 1 : 0;
        if (comparator.compare(vArray[j],v)<=0) 
          break;
        vArray[i] = vArray[j];
        i = j;
        j = i - fudge + i;
      }
      vArray[i]=v;			
    }
    for (--stop;stop>=start;stop--) {
      v = vArray[stop];
      vArray[stop]=vArray[start]; 
      i = start;
      j = start + 1;
      while (j<stop) { 
        //different: will, on average,
        // iterate one extra time per value of stop
        if (j<stop-1) 
          j+= (comparator.compare(vArray[j],vArray[j+1])<0) ? 1 : 0;
        if (comparator.compare(vArray[j],v)<=0) 
          break;
        vArray[i]=vArray[j];
        i = j;
        j = i - fudge + i;	
      }
      vArray[i] = v;
    }			
  }	
}
