package jsortie.quicksort.multiway.partitioner.kthstatistic;

public interface KthStatisticMultiPartitioner {
  public void partitionRangeExactly
              ( int[] vArray, int start, int stop
              , int[] targetIndices);
}

