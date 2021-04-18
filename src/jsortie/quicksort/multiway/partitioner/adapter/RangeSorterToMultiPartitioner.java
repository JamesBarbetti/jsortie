package jsortie.quicksort.multiway.partitioner.adapter;

import jsortie.RangeSorter;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;

public class RangeSorterToMultiPartitioner 
  implements MultiPivotPartitioner {
  public RangeSorter innerSorter;
  public RangeSorterToMultiPartitioner
    ( RangeSorter s) {
    innerSorter = s;
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices) {
    innerSorter.sortRange(vArray, start, stop);
    return new int[] {};
  }
}
