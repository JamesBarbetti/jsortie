package jsortie.quicksort.multiway.partitioner.adapter;

import jsortie.helper.FancierEgalitarianPartitionerHelper;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class SingleToTernaryPartitioner 
  extends SingleToMultiPartitioner {
  FancierEgalitarianPartitionerHelper helper 
    = new FancierEgalitarianPartitionerHelper();
  public SingleToTernaryPartitioner
    ( SinglePivotPartitioner single ) {
    super(single);
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices) {
    int middleIndex = pivotIndices.length/2;
    int pivotIndex  = pivotIndices[middleIndex];
    int split = innerPartitioner.partitionRange
                ( vArray, start, stop, pivotIndex );
    int vPivot = vArray[split];
    int stopOne = helper.moveEqualOrGreaterToRight
                   ( vArray, start, split, vPivot);
    int startTwo = helper.moveEqualOrLessToLeft
                   ( vArray, split+1, stop, vPivot);
    return new int[] { start, stopOne, startTwo, stop }; 
  }
}
