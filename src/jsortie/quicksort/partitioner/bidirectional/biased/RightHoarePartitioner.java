package jsortie.quicksort.partitioner.bidirectional.biased;

import jsortie.quicksort.partitioner.bidirectional.traditional.RevisedHoarePartitioner;

public class RightHoarePartitioner
  extends RevisedHoarePartitioner {
  public int choosePreferredPivot
    ( int start, int stop ) {
    return start;
  }
}
