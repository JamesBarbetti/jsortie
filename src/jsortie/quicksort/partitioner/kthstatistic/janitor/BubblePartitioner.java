package jsortie.quicksort.partitioner.kthstatistic.janitor;

import jsortie.flags.QuadraticAverageCase;
import jsortie.janitors.insertion.SentinelInsertionSort;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;

public class BubblePartitioner 
  implements KthStatisticPartitioner, QuadraticAverageCase {
  @Override
  public void partitionRangeExactly(int[] vArray, int start, int stop, int targetIndex) {
    if (targetIndex-start<stop-targetIndex) {
      for (;start<=targetIndex;++start) {
        SentinelInsertionSort.bubbleMinimumToLeft(vArray, start, stop);
      }
    } else {
      for (;targetIndex<stop;--stop) {
        SentinelInsertionSort.bubbleMaximumToRight(vArray, start, stop);
      }
    }
  }
}
