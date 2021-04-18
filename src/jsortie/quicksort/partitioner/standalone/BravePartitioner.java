package jsortie.quicksort.partitioner.standalone;

import jsortie.helper.RangeSortHelper;
import jsortie.janitors.insertion.InsertionSort;
import jsortie.quicksort.partitioner.interfaces.StandAlonePartitioner;

public class BravePartitioner 
  implements StandAlonePartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int[] multiPartitionRange(int[] vArray, int start, int stop) {
    if (stop-start<5) {
      InsertionSort.sortSmallRange(vArray, start, stop);
      return new int [] {};
    }
    int middle = start + (stop-start)/2;
    RangeSortHelper.compareAndSwapIntoOrder(vArray, middle, middle+1);
    int vPivot1 = vArray[middle];
    int vPivot2 = vArray[middle+1];
    int lhs=start;
    int rhs=stop-1;
    do {
      int vLeft  = vArray[lhs];
      int vRight = vArray[rhs];
      boolean swap = vRight < vLeft;
      vArray[lhs] = swap ? vRight : vLeft;
      vArray[rhs] = swap ? vLeft  : vRight;
      if (vPivot1<vArray[lhs]) {
        RangeSortHelper.swapElements(vArray, lhs, middle);
        vPivot1=vArray[middle];
      }
      if (vArray[rhs]<vPivot2) {
        RangeSortHelper.swapElements(vArray, middle+1, rhs);
        vPivot2=vArray[middle+1];
      }
      lhs += ( vArray[lhs] <= vPivot1 ) ? 1 : 0;
      rhs -= ( vPivot2 <= vArray[rhs] ) ? 1 : 0;
    } while (lhs<rhs);
    return new int [] { start, rhs, lhs, stop };
  }
}
