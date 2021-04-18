package jsortie.quicksort.partitioner.kthstatistic;

import jsortie.janitors.insertion.InsertionSort;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;

public class BrainDeadKthStatisticPartitioner 
  implements KthStatisticPartitioner {
  //This is to QuickSelectPartitioner roughly
  //as InsertionSort is to QuicksortClassic
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start
    , int stop, int targetIndex) {
    if (stop-start<2) {
      return;
    }
    if (targetIndex-start<stop-targetIndex) {
      int rhs  = targetIndex+1;
      InsertionSort.sortSmallRange
        ( vArray, start, rhs );
      int vCeiling = vArray[targetIndex];
      do {
        int v = vArray[rhs];
        if ( v < vCeiling) {
          vArray[rhs] = vCeiling;
          int lhs = targetIndex-1; 
          for ( ; start<=lhs && v<vArray[lhs]
                ; --lhs ) {
            vArray[lhs+1] = vArray[lhs];
          }
          vArray[lhs+1] = v;
          vCeiling = vArray[targetIndex];
        }
        ++rhs;
      } while (rhs<stop);
    } else {
      int lhs = targetIndex-1;
      InsertionSort.sortSmallRange
        ( vArray, targetIndex, stop );
      int vFloor = vArray[targetIndex];
      do {
        int v = vArray[lhs];
        if (vFloor<v) {
          vArray[lhs] = vFloor;
          int rhs=targetIndex+1;
          for ( ; rhs<stop && vArray[rhs]<v
                ; ++rhs) {
            vArray[rhs-1] = vArray[rhs];
          }
          vArray[rhs-1] = v;
          vFloor        = vArray[targetIndex];
        }
        --lhs;
      } while (start<=lhs);
    }
  }
}
