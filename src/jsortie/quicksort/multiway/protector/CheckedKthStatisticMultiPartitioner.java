package jsortie.quicksort.multiway.protector;

import jsortie.helper.SinglePivotPartitionHelper;
import jsortie.quicksort.multiway.partitioner.kthstatistic.KthStatisticMultiPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class CheckedKthStatisticMultiPartitioner 
  implements KthStatisticMultiPartitioner{
  protected KthStatisticMultiPartitioner   innerPartitioner;
	  protected String                     innerName;
	  protected SinglePivotPartitionHelper helper  = new SinglePivotPartitionHelper();
	  protected MultiPartitionChecker      checker = new MultiPartitionChecker();
	  public CheckedKthStatisticMultiPartitioner(KthStatisticMultiPartitioner inner) {
	    innerPartitioner = inner;
	    innerName        = inner.toString();
	  }
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + innerPartitioner.toString() + ")";
  }
  @Override
  public void partitionRangeExactly(int[] vArray, int start, int stop, int[] targetIndices) {
    int[] vCopy      = helper.copyRange(vArray, start, stop);
    innerPartitioner.partitionRangeExactly(vArray, start, stop, targetIndices);
    int[] partitions = MultiPivotUtils.convertFinalIndicesOfPivotsToPartitions(start, stop, targetIndices);
    checker.checkPartitionsAreValid(innerName, vArray, start, stop, partitions);
    helper.checkRangeIsPermutationOf(innerName, vArray, start, stop, vCopy);	
  }
}
