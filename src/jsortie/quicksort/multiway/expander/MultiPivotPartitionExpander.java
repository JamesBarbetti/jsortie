package jsortie.quicksort.multiway.expander;

public interface MultiPivotPartitionExpander {
  public int[] expandPartitions
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int startRight, int stop);
}
