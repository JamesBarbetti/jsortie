package jsortie.quicksort.multiway.partitioner.fourpivot;

import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class FourPivotMetamorphicPartitioner 
extends FourPivotPartitioner {
  FourPivotMirrorPartitioner mirror 
    = new FourPivotMirrorPartitioner();
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices) {
    if ( MultiPivotUtils.chooseIndexOfBestPivotIndex
         ( vArray, start, stop, pivotIndices ) < 2 ) {
      return mirror.multiPartitionRange
             ( vArray, start, stop, pivotIndices );
    } else {
      return super.multiPartitionRange
             ( vArray, start, stop, pivotIndices );
    }
  }
}
