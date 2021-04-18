package jsortie.quicksort.partitioner.kthstatistic;

import jsortie.RangeSorter;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;

public class RangeSorterToKthStatisticPartitioner 
  implements KthStatisticPartitioner {
  public RangeSorter sorter;
  public RangeSorterToKthStatisticPartitioner
    ( RangeSorter sorterToUse) {
    sorter = sorterToUse;
  }
  public String toString() {
    return "ToKStatPartitioner(" + sorter.toString() + ")";
  }
  @Override
  public void partitionRangeExactly(int[] vArray, int start, int stop, int targetIndex) {
    sorter.sortRange(vArray, start, stop); 
  }
}
