package jsortie.quicksort.multiway.partitioner;

public interface MultiPivotPartitioner {
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices);
  //Note 1: Callers promise that... 
  //        (a) each of the pivot indices 
  //            will all be >=start and <stop
  //        (b) the pivot indices will 
  //            be distinct (no two equal)
  //        (c) the values pointed to by the 
  //            pivot indices will not descend; 
  //            that array[pivotIndices[x]] 
  //            is never < array[pivotIndices[x-1]], 
  //            for any x between 1 and 
  //            pivotIndices.length-1 inclusive;
  //Note 2: Partitions are passed back... 
  //        as follows.
  //        even index = start
  //        odd  index = stop
}
