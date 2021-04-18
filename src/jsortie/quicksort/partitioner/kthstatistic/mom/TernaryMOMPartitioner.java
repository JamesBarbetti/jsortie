package jsortie.quicksort.partitioner.kthstatistic.mom;

import jsortie.quicksort.expander.PartitionExpander;

public class TernaryMOMPartitioner 
  extends MedianOfMediansPartitioner {
  public TernaryMOMPartitioner(int g) {
    super(g);
  }
  public TernaryMOMPartitioner  
    ( int g, PartitionExpander leftExpanderToUse
    , PartitionExpander rightExpanderToUse) {
    super(g, leftExpanderToUse, rightExpanderToUse);
  }    
  @Override
  public void partitionRangeExactly 
    ( int[] vArray, int start
    , int stop, int target) {
    Double comparisonsLeft 
      = new Double(0);
    PartitionExpander expander 
      = ( target-start < stop-target )
      ? leftExpander : rightExpander;
    partitionRangeExactly
      ( vArray, start, start, target
      , stop, stop, expander
      , comparisonsLeft);
  }
}
