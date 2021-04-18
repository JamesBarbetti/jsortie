package jsortie.quicksort.expander.bidirectional;

import jsortie.quicksort.partitioner.bidirectional.biased.LeftHoarePartitioner;

public class LeftHoareExpander 
  extends RevisedHoareExpander {
  public LeftHoareExpander() {
    party = new LeftHoarePartitioner();
  }
}
