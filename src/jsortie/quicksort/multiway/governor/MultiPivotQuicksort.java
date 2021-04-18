package jsortie.quicksort.multiway.governor;

import jsortie.RangeSorter;
import jsortie.quicksort.multiway.partitioner.CompoundPartitioner;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.interfaces.StandAlonePartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;

public class MultiPivotQuicksort 
  implements RangeSorter {
  protected RangeSorter           janitor;
  protected int                   janitorThreshold;
  protected StandAlonePartitioner party;
  protected MultiPivotUtils       utils 
    = new MultiPivotUtils();
  public MultiPivotQuicksort 
    ( MultiPivotSelector selector
    , MultiPivotPartitioner partitioner
    , RangeSorter janitorToUse, int threshold) {
    this.party = new CompoundPartitioner
                     ( selector, partitioner );
    this.janitor   = janitorToUse;
    this.janitorThreshold = threshold;
  }
  public MultiPivotQuicksort 
    ( SinglePivotSelector singlePivotSelector
    , SinglePivotPartitioner singlePivotPartitioner
    , RangeSorter janitorToUse, int threshold ) {
    this.party 
      = new CompoundPartitioner 
            ( singlePivotSelector 
            , singlePivotPartitioner );
    this.janitor   = janitorToUse;
    this.janitorThreshold = threshold;
  }
  public MultiPivotQuicksort 
    ( StandAlonePartitioner party
    , RangeSorter janitorToUse, int threshold ) {
    this.party     = party;
    this.janitor   = janitorToUse;
    this.janitorThreshold = threshold;
  }
  @Override
  public String toString() {
    return getClass().getSimpleName()
      + "( " + party.toString() 
      + ", " + janitor.toString() 
      + ", " + janitorThreshold + " )";
  }
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    while ( janitorThreshold < stop-start ) {
      int[] partitions 
        = party.multiPartitionRange
          ( vArray, start, stop );
      int boundaryLength = partitions.length;
      if (0==boundaryLength) {
        return;
      }
      int i = utils.indexOfLargestPartition
              ( partitions );
      //Recursively sort all non-empty partitions, 
      //except for the largest
      for (int j=0; j<boundaryLength; j+=2) {
        if ( i != j ) {
          start = partitions[j];
          stop  = partitions[j+1];
          if (start + 1 < stop ) {
            sortRange(vArray, start, stop);
          }
        }
      }
      //Tail-recurse to sort the largest partition.
      start = partitions[i];
      stop  = partitions[i+1];
    }
    if (start+1<stop) {
      janitor.sortRange(vArray, start, stop);
    }
  }
}
