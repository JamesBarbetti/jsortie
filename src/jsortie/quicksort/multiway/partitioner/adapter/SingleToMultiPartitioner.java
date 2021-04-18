package jsortie.quicksort.multiway.partitioner.adapter;
import jsortie.quicksort.multiway.partitioner.FixedCountPivotPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class SingleToMultiPartitioner
  implements FixedCountPivotPartitioner {
  protected SinglePivotPartitioner innerPartitioner;
  @Override
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + innerPartitioner.toString() + ")";
  }
  public SingleToMultiPartitioner
    ( SinglePivotPartitioner single ) {
    innerPartitioner = single;
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices) {
    int middleIndex = pivotIndices.length/2;
    int pivotIndex  = pivotIndices[middleIndex];
    int split = innerPartitioner.partitionRange
                ( vArray, start, stop, pivotIndex );
    return new int[] { start, split, split+1, stop }; 
  }
  @Override
  public int getPivotCount() {
    return 1;
  }
}
