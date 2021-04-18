package jsortie.janitors.selection;

import jsortie.RangeSorter;
import jsortie.heapsort.topdown.HeapsortStandard;

public class CanberraBubbleSort 
  implements RangeSorter {
  //If HeapsortStandard and BubbleSort got married, their
  //firstborn might look something like this.
  int bubbleSize; //always odd. see below
  HeapsortStandard hs 
    = new HeapsortStandard();
	
  public CanberraBubbleSort
    ( int selectionBubbleSize ) {
    this.bubbleSize 
       = selectionBubbleSize 
       + ((selectionBubbleSize&1)==0 ? 1 : 0);
      //if heapSize is always odd, 
      //that saves us some boundary checks 
      //in the inner loop of siftIn. See below.
    if (this.bubbleSize<1) {
      throw new IllegalArgumentException
        ( "selectionHeapSize" 
        + " (" + selectionBubbleSize + ")" 
        + " is less than 1");
    }
  }
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    if (stop-start<bubbleSize) {
      hs.sortRange(vArray, start, stop);
    } else {
      int minHeap[] = new int[bubbleSize];
      initializeHeap(vArray, start, minHeap);
      do {
        int x=start;
        for ( int q=start, r=start+bubbleSize
            ; r<stop; ++q, ++r) {
          int v = vArray[r];
          if ( v < minHeap[0] ) {
            x = q;
            vArray[x] = v;
          } else {
            vArray[q] = minHeap[0]; 
            int i = 0;
            int j = 1;
            do {
              //don't need:... if (j<heapSize-1) 
              //...if heapSize is always odd, 
              //because: j will always be odd, and 
              //if j<heapSize, which is also odd, 
              //then j<=heapSize-2.
              j+= (minHeap[j+1]<minHeap[j]) ? 1 : 0;
              if ( v <= minHeap[j] ) {
                break;
              }
              minHeap[i] = minHeap[j];
              i = j;
              j = i + i + 1;
            } while (j<bubbleSize);
            minHeap[i] = v;
          }
        }
        drainHeap
          ( minHeap, vArray
          , stop-bubbleSize);
        stop = x + 1;
      }
      while (start+bubbleSize<stop);
      hs.sortRange(vArray, start, stop);
    }
  }	
  protected void initializeHeap
    ( int source[], int start, int minHeap[] ) {
    int h;
    for ( h = bubbleSize-1 
        ; bubbleSize < h + h + 2 ; --h) {
      minHeap[h] = source[start+h];
    }
    for (;h>=0;--h) {
      int v = source[h];
      int i = h;
      int j = i + i + 1;
      do {
        j+= (minHeap[j+1]<minHeap[j]) ? 1 : 0;
        if ( v <= minHeap[j] ) {
          break;
        }
        minHeap[i] = minHeap[j];
        i = j;
        j = i + i + 1;
      } while (j<bubbleSize);
    }
  }
  protected void drainHeap
    ( int heap[], int dest[], int w ) {
    for (int size=bubbleSize-1; size>=0; --size,++w) {
      dest[w] = heap[0];
      int v = heap[size];
      int i = 0;
      int j = 1;
      while (j<size) {
        if (j<size-1) {
          j+= (heap[j+1]<heap[j]) ? 1 : 0;
        }
        if (v<=heap[j]) {
          break;
        }
        heap[i] = heap[j];
        i = j;
        j = i + i + 1;
      }
      heap[i] = v;
    }
  }
}
