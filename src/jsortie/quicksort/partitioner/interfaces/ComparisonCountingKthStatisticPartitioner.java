package jsortie.quicksort.partitioner.interfaces;

public interface ComparisonCountingKthStatisticPartitioner {
  public int partitionRangeExactlyCountComparisons
    ( int[] vArray, int start, int stop, int targetIndex );
}
