package jsortie.quicksort.protector;

import jsortie.helper.SinglePivotPartitionHelper;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;

public class CheckedKthStatisticPartitioner implements KthStatisticPartitioner {
  protected KthStatisticPartitioner    inner;
  protected String                     innerName;
  protected SinglePivotPartitionHelper checker;
  public CheckedKthStatisticPartitioner(KthStatisticPartitioner innerPartitioner) {
    inner     = innerPartitioner;
    innerName = inner.toString();
    checker   = new SinglePivotPartitionHelper();
  }
  public String toString() {
    return this.getClass().getSimpleName() + "(" + innerName + ")";
  }
  
  @Override
  public void partitionRangeExactly(int[] vArray, int start, int stop, int targetIndex) {
	int[] vTrashableCopyOfOriginal = checker.copyRange(vArray, start, stop);
	inner.partitionRangeExactly(vArray, start, stop, targetIndex);
	checker.checkPartition(innerName, vArray, start, targetIndex, stop);
	checker.checkRangeIsPermutationOf(innerName, vArray, start, stop, vTrashableCopyOfOriginal);
  }
}
