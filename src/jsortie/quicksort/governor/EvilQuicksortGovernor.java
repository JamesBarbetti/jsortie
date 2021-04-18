package jsortie.quicksort.governor;

import jsortie.RangeSorter;
import jsortie.exception.CouldntBeBotheredCodingItProperlyException;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;

public class EvilQuicksortGovernor 
  extends QuicksortGovernor {
  protected boolean 
    doIAgreeThatUnecessaryStackOverflowsAreOkay 
    = false;
  public EvilQuicksortGovernor 
    ( SinglePivotSelector selector
    , SinglePivotPartitioner partitioner
    , RangeSorter rangeSorter
    , int janitorThresholdToUse) {
    super ( selector, partitioner
          , rangeSorter, janitorThresholdToUse);
    if (janitorThreshold<1) { 
      janitorThreshold = 1;
    }
  }
  @Override
  public void sortRange
    ( int [] vArray, int start, int stop ) {
    if (!doIAgreeThatUnecessaryStackOverflowsAreOkay) {
      throw new CouldntBeBotheredCodingItProperlyException
                ( this.getClass().getSimpleName() 
                + ".sortRange allows stack overflow"
                + " for no good reason");
    }
    if ( stop-start <= janitorThreshold ) {
      janitor.sortRange(vArray, start, stop);
      return;
    } else {
      int pivotIndex 
        = selector.selectPivotIndex
          ( vArray, start, stop );
      pivotIndex 
        = partitioner.partitionRange
          ( vArray, start, stop, pivotIndex );
      sortRange(vArray, start, pivotIndex);
      sortRange(vArray, pivotIndex+1, stop);
    }
  }	
}
