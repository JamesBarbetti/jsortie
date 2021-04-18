package jsortie.quicksort.external;

public interface ExternalSinglePivotPartitioner {
  public int partitionMainRange
    ( int[] vSourceArray, int start, int stop
    , int pivotIndex
    , int[] vDestArray, int destStart);
  public int partitionAuxiliaryRange
    ( int[] vArray, int start
    , int[] vAuxArray
    , int auxStart, int auxStop
    , int pivotIndexInAuxArray);
}
