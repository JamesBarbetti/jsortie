package jsortie.quicksort.multiway.partitioner.singlepivot;

import jsortie.quicksort.multiway.partitioner.centrifugal.CentristHoyosPartitioner2;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class HoyosDutchPartitioner 
  implements TernarySinglePivotPartitioner {
  protected int pivotCount = 1;
  protected CentristHoyosPartitioner2 cp
    = new CentristHoyosPartitioner2();
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int getPivotCount() {
    return pivotCount;
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    if (stop-start<4) {
      return MultiPivotUtils.dummyPartitions
             ( vArray, start, stop, pivotCount );
    }
    int i       = pivotIndices.length/2;
    int t       = pivotIndices[i];
    int f       = (t==start) ? (stop-1) : start;
    int vLifted = vArray[f];
    int vP      = vArray[t];
    vArray[f]   = vP;
    pivotIndices = new int[] { t, f };
    int[] answer 
      = cp.multiPartitionRange
        ( vArray, start, stop, pivotIndices );
    t = answer[1];
    f = answer[3];
    if ( vLifted < vP ) {
      vArray[f] = vArray[t];
      vArray[t] = vLifted;
      ++t;
      ++f;
    } else {
      vArray[f] = vLifted;
      if ( vLifted == vP ) {
        ++f;
      }
    }
    return new int[] { start, t, f, stop };
  }
}
