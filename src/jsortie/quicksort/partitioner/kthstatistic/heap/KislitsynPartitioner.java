package jsortie.quicksort.partitioner.kthstatistic.heap;

import jsortie.heapsort.topdown.MaxHeap;
import jsortie.heapsort.topdown.MinHeap;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;

public class KislitsynPartitioner 
  implements KthStatisticPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start, int stop
    , int targetIndex ) {
    int middle = start + (stop-start)/2;
    if (targetIndex < middle) {
      partitionRangeOnLeft
        ( vArray, start, stop, targetIndex );
    } else {
      partitionRangeOnRight
        ( vArray, start, stop, targetIndex );
    }
  }
  protected void partitionRangeOnLeft
    ( int[] vArray, int start, int stop
    , int targetIndex) {
    //This is equivalent to what GNU's 
    //std::__introselect implementation does 
    //when its depth limit is exceeded.
    //(Except: here, the apex of the heap 
    // is at targetIndex, not at start).
    int heapStop = targetIndex+1;
    MaxHeap maxHeap 
      = new MaxHeap
            ( vArray, start, heapStop );
    int vMax = vArray[targetIndex];
    for ( int scan=targetIndex+1
        ; scan<stop; ++scan) {
      int v = vArray[scan];
      if ( v < vMax ) {
        vArray[scan]        = vMax;
        vArray[targetIndex] = v;
        maxHeap.pushDownItemAt(targetIndex);
        vMax = vArray[targetIndex];
      }
    }
  }
  protected void partitionRangeOnRight
    ( int[] vArray, int start, int stop
    , int targetIndex ) {
    //This is a left-right reflection of what
    //GNU's std::__introselect implementation does
    //(well, sort of: this time the heap's apex is
    //at targetIndex, rather than at stop-1).
    //Why build a crazy big heap when targetIndex
    //is closer to stop than it is to start?
    MinHeap minHeap
      = new MinHeap
            ( vArray, targetIndex, stop );
    int vMin = vArray[targetIndex];
    for ( int scan=targetIndex-1
        ; start<=scan; --scan) {
      int v = vArray[scan];
      if ( vMin < v ) {
        vArray[scan] = vMin;
        vArray[targetIndex] = v;
        minHeap.pushDownItemAt(targetIndex);
        vMin = vArray[targetIndex];
      }
    }
  }
}
