package jsortie.quicksort.multiway.protector;

import jsortie.helper.SinglePivotPartitionHelper;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;

public class CheckedMultiPivotPartitioner 
  implements MultiPivotPartitioner {
  protected MultiPivotPartitioner      innerPartitioner;
  protected String                     innerName;
  protected SinglePivotPartitionHelper helper  
    = new SinglePivotPartitionHelper();
  protected MultiPartitionChecker checker 
    = new MultiPartitionChecker();
  public CheckedMultiPivotPartitioner
    ( MultiPivotPartitioner inner ) {
    innerPartitioner = inner;
    innerName        = inner.toString();
  }
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + innerPartitioner.toString() + ")";
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    int[] vCopy
      = helper.copyRange(vArray, start, stop);
    int[] partitions 
      = innerPartitioner.multiPartitionRange
        ( vArray, start, stop, pivotIndices );
    checker.checkPartitionsAreValid
      ( innerName, vArray, start, stop, partitions );
    helper.checkRangeIsPermutationOf
      ( innerName, vArray, start, stop, vCopy );
    return partitions;
  }
}
