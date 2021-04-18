package jsortie.quicksort.protector;

import jsortie.helper.SinglePivotPartitionHelper;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class CheckedSinglePivotPartitioner 
  implements SinglePivotPartitioner {
  protected SinglePivotPartitioner     innerPartitioner;
  protected SinglePivotPartitionHelper helper;
  protected String                     innerName;
  
  public CheckedSinglePivotPartitioner
    ( SinglePivotPartitioner inner ) {
    helper           = new SinglePivotPartitionHelper();
    innerPartitioner = inner;
    innerName        = inner.toString();
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName()
    + "(" + innerPartitioner.toString() + ")";
  }	
  @Override
  public int partitionRange(int[] vArray, int start, int stop, int pivotIndex) {
    int[] vTrashableCopy = helper.copyRange(vArray, start, stop);
    pivotIndex = innerPartitioner.partitionRange(vArray, start, stop, pivotIndex);
    helper.checkPartition(innerName, vArray, start, pivotIndex, stop);
    helper.checkRangeIsPermutationOf(innerName, vArray, start, stop, vTrashableCopy);
    return pivotIndex;
  }
}
