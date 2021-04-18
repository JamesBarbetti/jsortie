package jsortie.quicksort.multiway.expander.kangaroo;

import jsortie.quicksort.expander.branchavoiding.SkippyExpander;
import jsortie.quicksort.multiway.expander.MultiPivotPartitionExpander;

public class SkippyExpander1A
  implements MultiPivotPartitionExpander {
  SkippyExpander realExpander = new SkippyExpander();
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int[] expandPartitions
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int startRight, int stop) {
    int split = realExpander.expandPartition
                ( vArray, start, stopLeft, pivotIndices[0]
                , startRight, stop);
    return new int[] { start, split, split+1, stop };
  }
}
