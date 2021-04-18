package jsortie.quicksort.partitioner.interfaces;

public interface SinglePivotPartitioner {
  public int partitionRange
    ( int [] vArray, int start, int stop
    , int pivotIndex);
}
