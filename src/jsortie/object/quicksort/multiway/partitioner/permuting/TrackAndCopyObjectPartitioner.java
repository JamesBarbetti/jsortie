package jsortie.object.quicksort.multiway.partitioner.permuting;

import java.util.Comparator;

import jsortie.object.quicksort.multiway.partitioner.ExternalMultiPivotObjectPartitioner;

public class TrackAndCopyObjectPartitioner<T> 
  extends TrackAndPermuteObjectPartitionerWithBellsOn<T> 
  implements ExternalMultiPivotObjectPartitioner<T> {
  @SuppressWarnings("unchecked")
  public int[] externalPartitionRange 
    ( Comparator<? super T> comparator
    , T[] vSource, int start, int stop
    , int[] pivotIndices, T[] vDest, int destStart) {
    int      pivotCount = pivotIndices.length;
    Object[] vPivots    = new Object [ pivotCount ]; 
    //
    //copy out the pivots (because, the alternative, 
    //double-array look up on each binary search iteration... 
    //would be... slower, for large enough (stop-start).
    //besides, this is an opportunity to remove any pivots
    //that compare equal to earlier pivots.
    vPivots[0] = vSource[pivotIndices[0]];
    int p=1;
    for (int r=1; r<pivotCount; ++r) {
      vPivots[p] = vSource[pivotIndices[r]];
      if ( comparator.compare 
           ( (T) vPivots[p-1], (T) vPivots[p] ) 
           < 0 ) {
        ++p;
      }
    }
    pivotCount     = p;
    int countOfPartitions = p + p + 1;
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
      = new int [ stop - start ]; 
    mapItemsToPartitions
      ( comparator, vSource, start, stop
      , vPivots, pivotCount
      , partitionCounts, itemToPartition);
    int[] boundaries = partitionCounts; 
    //boundaries is an "alias": 
    //the same array is used for both jobs!
    int i = destStart;
    for (p=0; p<=countOfPartitions; ++p) {
      int c = partitionCounts[p];
      boundaries[p] = i;
      i += c;
    }
    //now: partitionCounts[p] = pointer, 
    //to next unused element in partition p
    for (int scan=start; scan<stop; ++scan) {
      T v              = (T) vSource[scan];
      p                = itemToPartition[scan-start];
      i                = boundaries[p];
      vDest      [ i ] = v;
      boundaries [ p ] = i+1;
    }
    //now: boundaries[p] = first item *after* partition p
    for ( p = countOfPartitions; 0 < p ; --p ) {
      boundaries[p]=boundaries[p-1];
    }
    boundaries[0] = destStart;
    //odd numbered partitions contain items equal to pivots
    return boundaries;
  }
}