package jsortie.quicksort.multiway.protector;

import jsortie.helper.SinglePivotPartitionHelper;
import jsortie.quicksort.partitioner.interfaces.StandAlonePartitioner;

public class CheckedStandalonePartitioner 
  implements StandAlonePartitioner  {
  protected StandAlonePartitioner      innerPartitioner;
  protected SinglePivotPartitionHelper helper;
  protected MultiPartitionChecker      checker = new MultiPartitionChecker();
  protected String                     innerName;

  public CheckedStandalonePartitioner
    ( StandAlonePartitioner inner ) {
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
  public int[] multiPartitionRange(int[] vArray, int start, int stop) {
    int[] vCopy      = helper.copyRange(vArray, start, stop);
    int[] partitions = innerPartitioner.multiPartitionRange(vArray, start, stop);
    checker.checkPartitionsAreValid(innerName, vArray, start, stop, partitions);
    helper.checkRangeIsPermutationOf(innerName, vArray, start, stop, vCopy);
    return partitions;
  }
}
