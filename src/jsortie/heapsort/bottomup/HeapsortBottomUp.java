package jsortie.heapsort.bottomup;

import jsortie.heapsort.HeapsortBase;
import jsortie.janitors.insertion.InsertionSort;

public class HeapsortBottomUp 
  extends HeapsortBase {
  @Override
  public void constructHeap
    ( int[] vArray, int start, int stop ) {
    int fudge= start - 2;
    for (int h = start+(stop-start-3)/2; h>=start ; --h) {
      int i = h;
      int v = vArray[i];
      int j = i - fudge + i;	
      do {
        if (j+1<stop) {
          j += (vArray[j] < vArray[j+1]) ? 1 : 0;
        }
        if ( vArray[j] <= v ) {
          break;
        }
        vArray[i] = vArray[j];
        i = j;
        j = i - fudge + i;
      } while (j<stop);
      vArray[i]=v;			
    }
  }
  @Override  
  public void extractFromHeap
    ( int[] vArray, int start, int stop ) {
    int fudge= start - 2;
    //heap extraction phase
    int firstChild=start+2;
    for (--stop;stop>=firstChild;stop--) {
      int v = vArray[stop];		
      int i = stop;	    
      int j = start;
      //extract, assuming v will go into a bottom-level node
      do {
        if (j+1<stop) {
          j += (vArray[j] < vArray[j+1]) ? 1 : 0;
        }
        vArray[i] = vArray[j];
        i = j;
        j = i - fudge + i;	
      } while (j<stop);
      //search back up the path toward the top of the heap, to place v
      int h = (i-fudge)/2 + fudge;
      while (start<=h && vArray[h]<v) {
        vArray[i] = vArray[h];
        i = h;
        h = (i-fudge)/2+fudge;
      }	
      vArray[i] = v;
    }    
    InsertionSort.sortSmallRange(vArray, start, firstChild);
  }
}
