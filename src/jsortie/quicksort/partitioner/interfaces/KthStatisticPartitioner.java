package jsortie.quicksort.partitioner.interfaces;

public interface KthStatisticPartitioner {
  public void partitionRangeExactly
    ( int[] vArray, int start, int stop
    , int targetIndex );
}
