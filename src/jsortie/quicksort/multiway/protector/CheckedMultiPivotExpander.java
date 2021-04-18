package jsortie.quicksort.multiway.protector;

import jsortie.quicksort.multiway.expander.MultiPivotPartitionExpander;

public class CheckedMultiPivotExpander 
  implements MultiPivotPartitionExpander {
  protected MultiPartitionChecker        checker;
  protected MultiPivotPartitionExpander  innerExpander;
  protected String                       innerName;
  public CheckedMultiPivotExpander
    ( MultiPivotPartitionExpander inner ) {
    checker       = new MultiPartitionChecker();
    innerExpander = inner;
    innerName     = inner.toString();
  }
  public String toString() {
    return this.getClass().getSimpleName() 
           + "(" + innerExpander.toString() + ")";
  }
  @Override
  public int[] expandPartitions ( int[] vArray, int start, int stopLeft
                                , int[] pivotIndices, int startRight, int stop) {
    int[] vPivots = snapshotPivots(vArray, pivotIndices);
    int[] partitions = innerExpander.expandPartitions ( vArray, start, stopLeft
                                                      , pivotIndices, startRight, stop);
    checker.checkPartitionsAreValid(innerName, vArray, start, stop, partitions);
    checkPivotsAreBetweenPartitions(innerName, vArray, vPivots, partitions);
    return partitions;
  }
  public int[] snapshotPivots(int[] vArray, int[] pivotIndices) {
    int[] vPivots = new int [pivotIndices.length];
    for (int i=0; i<pivotIndices.length; ++i) {
      vPivots[i] = vArray[pivotIndices[i]];
    }
    return vPivots;
  }
  public void checkPivotsAreBetweenPartitions 
    ( String source, int[] vArray
    , int[] vPivots, int[] partitions) {
    //The problem here is that empty, size 1, or sorted partitions 
    //need not be returned. There probably are some checks we can do here, 
    //but I haven't figured out what they are.
  }
}
