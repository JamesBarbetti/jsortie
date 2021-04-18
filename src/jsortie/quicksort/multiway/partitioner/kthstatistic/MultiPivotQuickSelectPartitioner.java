package jsortie.quicksort.multiway.partitioner.kthstatistic;

import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner2;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.multiway.selector.clean.CleanMultiPivotPositionalSelector;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.quickselect.QuickSelectPartitioner;

public class MultiPivotQuickSelectPartitioner 
  implements KthStatisticPartitioner {
  protected  int                     janitorThreshold = 10;
  protected  KthStatisticPartitioner janitor;
  protected  MultiPivotSelector      selector;
  protected  MultiPivotPartitioner   partitioner;
  
  public MultiPivotQuickSelectPartitioner() {
    selector
      = new CleanMultiPivotPositionalSelector(2);
    partitioner
      = new SkippyPartitioner2();
    janitor
      = new QuickSelectPartitioner();
  }
  public MultiPivotQuickSelectPartitioner
    ( MultiPivotPartitioner party) {
    selector
      = new CleanMultiPivotPositionalSelector(2);
    partitioner = party; 
    janitor 
      = new QuickSelectPartitioner();
  }
  public MultiPivotQuickSelectPartitioner
    ( MultiPivotSelector      sellOut
    , MultiPivotPartitioner party) {
    selector    = sellOut;
    partitioner = party; 
    janitor 
      = new QuickSelectPartitioner();
  }
  public String toString() {
    return this.getClass().getSimpleName() + "(" 
      + partitioner.toString() + ")"; 
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start, int stop
    , int targetIndex ) {
     int originalStart = start;
    int originalStop  = stop;
    while ( janitorThreshold < stop - start ) {
      int[] pivots 
        = selector.selectPivotIndices
          ( vArray, start, stop );
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
      if ( originalStart+1 <= start &&
           stop < originalStop) {
        if ( vArray[start-1] == vArray[stop] ) {
          return;
        }
      }
    }
    janitor.partitionRangeExactly
      ( vArray, start, stop, targetIndex );
  }
}
