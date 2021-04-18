package jsortie.quicksort.multiway.external;

public interface ExternalMultiPivotPartitioner {
  public int[] externalMultiPartitionRange 
    ( int[] vSource, int start, int stop
    , int[] pivotIndices
    , int[] vDest,   int destStart);
}
