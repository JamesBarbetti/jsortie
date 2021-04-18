package jsortie.quicksort.partitioner.kthstatistic.heap;

import jsortie.heapsort.topdown.DualHeap;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;

public class DualHeapPartitioner 
  implements KthStatisticPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();	
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start, int stop
    , int targetIndex ) {
    fold(vArray, start, stop);
    @SuppressWarnings("unused")
    DualHeap dualHeap
      = new DualHeap
            ( vArray, start
            , targetIndex, stop);    
  }
  public void fold 
    ( int[] vArray, int start, int stop ) {
    int i = start;
    int j = stop-1;
    for (; i<j; ++i, --j) {
      int vLeft  = vArray[i];
      int vRight = vArray[j];
      boolean inOrder = (vLeft<=vRight);
      vArray[i] = inOrder ? vLeft  : vRight;
      vArray[j] = inOrder ? vRight : vLeft;
    }
  }
}

