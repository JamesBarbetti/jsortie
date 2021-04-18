package jsortie.object.heapsort;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;

public class ObjectHeapSort <T> 
  implements ObjectRangeSorter<T> {
  public String toString() { 
    return this.getClass().getSimpleName(); 
  }
  public void sortRange
    ( Comparator<? super T> comparator, T vArray[]
    , int start, int stop) {
    if (stop<start+2) {
      return;
    }
    int fudge = 2 - start;
    int i;
    int j;
    T   v;
    //Construct Heap
    for (int h=start+(stop-start)/2;h>=start;--h) {
      i = h;
      v = vArray[i];
      j = i + i + fudge;	
      while (j<stop) {
        if (j<stop-1) {
          j+= ( comparator.compare
                (vArray[j], vArray[j+1])<0 )
              ? 1 : 0;
        }
        if (comparator.compare(vArray[j],v)<=0) {
          break;
        }
        vArray[i]=vArray[j];
        i = j;
        j = i + i + fudge;
      }
      vArray[i]=v;			
    }
    //Select from heap (using Floyd's 
    //bottom-up replacement technique)
    for (--stop;stop>=start;stop--) {
      v = vArray[stop];
      //Sift up, all the way down
      //to the bottom of the heap
      i = stop;
      j = start;
      while (j<stop) {
        if (j<stop-1) {
          j+= ( comparator.compare
                ( vArray[j], vArray[j+1] ) < 0 )
              ? 1 : 0;
        }
        vArray[i] = vArray[j];
        i = j;
        j = i + i + fudge;	
      }
      //Search upwards, from the bottom 
      //of the heap to reinsert v at the
      //right height in the heap.
      int h = (i-start<2) ? stop : (i+fudge)/2 - fudge;
      while (comparator.compare(vArray[h],v)<0) {
        vArray[i] = vArray[h];
        i = h;
        if (h==stop) {
          break;
        }
        h = (i+fudge)/2-fudge;
        if (i-start<2) {
          h=stop;
        }
      }
      vArray[i] = v;
    }
  }
}
