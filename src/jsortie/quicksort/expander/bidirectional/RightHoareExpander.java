package jsortie.quicksort.expander.bidirectional;

import jsortie.quicksort.partitioner.bidirectional.biased.RightHoarePartitioner;

public class RightHoareExpander 
  extends RevisedHoareExpander {
  public RightHoareExpander() {
    party = new RightHoarePartitioner();
  }
}
