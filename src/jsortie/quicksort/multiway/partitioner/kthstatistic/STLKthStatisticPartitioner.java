package jsortie.quicksort.multiway.partitioner.kthstatistic;

import jsortie.janitors.insertion.InsertionSort;
import jsortie.quicksort.multiway.partitioner.singlepivot.STLTernaryPartitioner;
import jsortie.quicksort.multiway.partitioner.singlepivot.TernarySinglePivotPartitioner;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.RangeSorterToKthStatisticPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.reselector.STLTukeyReselector;
import jsortie.quicksort.selector.reselector.SelectorToReselector;
import jsortie.quicksort.selector.reselector.SinglePivotReselector;

public class STLKthStatisticPartitioner 
  implements KthStatisticPartitioner {
  protected  SinglePivotReselector         reselector;
  protected  TernarySinglePivotPartitioner partitioner;
  protected  int                           janitorThreshold = 32;
  protected  KthStatisticPartitioner       janitor;
  
  public STLKthStatisticPartitioner() {
    reselector  
      = new STLTukeyReselector();
    partitioner 
      = new STLTernaryPartitioner();
    janitor 
      = new RangeSorterToKthStatisticPartitioner
            ( new InsertionSort() );
  }
  public STLKthStatisticPartitioner
    ( SinglePivotSelector selectorToUse
    , KthStatisticPartitioner janitorToUse) {
    reselector  
      = new SelectorToReselector(selectorToUse);
    partitioner 
      = new STLTernaryPartitioner();
    janitor = janitorToUse;
  }
  public STLKthStatisticPartitioner
    ( SinglePivotSelector selectorToUse
    , TernarySinglePivotPartitioner tParty
    , KthStatisticPartitioner janitorToUse) {
    reselector 
      = new SelectorToReselector(selectorToUse);
    partitioner = tParty;
    janitor     = janitorToUse;
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + reselector.toString() 
      + ", " + janitorThreshold 
      + ", " + janitor.toString() + ")";
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start, int stop
    , int targetIndex ) {
    //Note: Can assumes the partitioner is a ternary,
    //      single pivot partitioner.  So no need to
    //      use the "know your limitations" heuristic.
    while ( janitorThreshold < stop - start ) {
      int[] pivots = new int[] { 
          reselector.selectPivotIndexGivenHint
          ( vArray, start
          , start + (stop-start)/2, stop ) };
      int[] boundaries
        = partitioner.multiPartitionRange
          ( vArray, start, stop, pivots );
      int b;
      int boundaryCount = boundaries.length;
      for (b=0; b<boundaryCount; b+=2) {
        if ( boundaries[b] <= targetIndex &&
             targetIndex < boundaries[b+1] ) {
          break;
        }
      }
      if (boundaryCount<=b) {
        return;
      }
      start = boundaries[b];
      stop  = boundaries[b+1];
    }
    janitor.partitionRangeExactly
      ( vArray, start, stop, targetIndex );
  }
}
