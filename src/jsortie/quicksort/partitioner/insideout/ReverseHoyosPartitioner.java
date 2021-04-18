package jsortie.quicksort.partitioner.insideout;

import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.bidirectional.HoyosExpander;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class ReverseHoyosPartitioner 
  implements SinglePivotPartitioner{
  protected PartitionExpander expander 
    = new HoyosExpander();
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override public int partitionRange
    ( int[] vArray, int start, int stop
    , int pivotIndex ) {
    int count          = stop - start;
    int hole           = start + count / 2;
    int vPivot         = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[hole];
    vArray[hole]       = vPivot;
    return expander.expandPartition
           ( vArray, start, hole
           , hole, hole+1, stop );
  } 
}
