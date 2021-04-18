package jsortie.quicksort.multiway.partitioner.permuting;

import jsortie.quicksort.multiway.external.ExternalEgalitarianPartitioner;
import jsortie.quicksort.multiway.external.ExternalMultiPivotPartitioner;

public class TrackAndCopyPartitioner  
  extends TrackAndPermutePartitionerWithBellsOn 
  implements ExternalMultiPivotPartitioner {
  protected ExternalEgalitarianPartitioner 
    partitionerHelper 
      = new ExternalEgalitarianPartitioner();
  public int[] externalMultiPartitionRange 
    ( int [] vSource, int start, int stop
    , int [] pivotIndices
    , int [] vDest,   int destStart) {
    int pivotCount = pivotIndices.length;
    int vPivots[]  = this.allocatePivotArray(pivotCount);
    pivotCount = copyDistinctPivots
                 ( vSource, pivotIndices, vPivots );
    if (pivotCount==1) {
      //todo: if all of the pivots are identical, 
      //fall back to three-way partitioning
      return partitionerHelper.externalMultiPartitionRange
             ( vSource, start, stop
             , pivotIndices, vDest, destStart);
    }
    int countOfPartitions 
      = pivotCount + pivotCount + 1;
    //
    //count how many items belong in each partition 
    //*and* track which partition each item is in
    //(tracking that costs us a lot of space (an 
    //array of stop-start integers) but will save us
    //half of the comparisons that 
    //CountAndPermuteObjectPartitioner has to do).
    int[] partitionCounts 
      = new int [ countOfPartitions + 1 ];
    int[] itemToPartition 
      = this.allocateItemToPartitionArray(stop - start); 
    mapItemsToPartitions
      ( vSource, start, stop, vPivots, pivotCount
      , partitionCounts, itemToPartition);
    int[] boundaries = partitionCounts; //it's an alias: 
     // the same array can be used for both jobs!
    int i = destStart;
    int p = 0;
    for (p=0; p<countOfPartitions; ++p) {
      int c = partitionCounts[p];
      boundaries[p] = i;
      i += c;
    }
    for (int scan=start; scan<stop; ++scan) {
      int v            = vSource[scan];
      p                = itemToPartition[scan-start];
      i                = boundaries[p];
      vDest      [ i ] = v;
      boundaries [ p ] = i+1;
    }
    return adjustBoundaries(destStart, boundaries);
  }
}