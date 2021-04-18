package jsortie.object.quicksort.multiway.partitioner.permuting;

public class TrackAndStablyPermuteObjectPartitioner<T>
  extends TrackAndPermuteObjectPartitionerWithBellsOn<T> {
  @Override
  protected int[] moveItemsToPartitions
    ( T[] vArray, int start, int stop
    , int countOfPartitions
    , int[] partitionCounts
    , int[] itemToPartition) {
    //derive pointers to (as yet un-permuted 
    //portions of) partitions
    //but these are 0-based not start-based.
    int[] partitionPointers 
      = new int [ countOfPartitions + 1]; 
    int i = start;
    for (int p=0; p<=countOfPartitions ; ++p) {
      int c = partitionCounts[p];
      partitionPointers[p] = i;
      i += c;
    }
    int[] result = partitionCounts; 
          //this is what we'll return
          //but it's actually just an alias!
    for (int p=0; p<=countOfPartitions; ++p) {
      result[p] = partitionPointers[p];
    }
    int count=stop-start;
    for (i=0; i<count; ++i) {
      int p = itemToPartition[i];
      int w = partitionPointers[p];
      itemToPartition[i]   = w;
      partitionPointers[p] = w+1;
    }
    //and now, itemToPartition specifies a 
    //destination permutation; we will be 
    //"dividing" (not "multiplying") the range 
    //vArray [ start .. (stop-1) ] by that
    //permutation. Like so:
    for (i=start; i<stop; ++i) {
      int j = itemToPartition[i - start];
      if (i<j) {
    	T v1 = vArray[i];
    	T v2;
        do {
          int k = itemToPartition [ j - start ];
          v2 = vArray [ j ];
          vArray [ j ] = v1;
          itemToPartition [ j - start] = j;
          //now vArray[j] is done, v2 has 
          //what was at j, and k is the place 
          //that v2 should go.
          if (k<=i) {
            v1 = v2;
            break;
          }
          j  = itemToPartition [ k - start ];
          v1 = vArray [ k ];
          vArray [ k ] = v2;
          itemToPartition [ k - start ] = k;
          //now vArray[k] is done, v1 has
          //what was at k, and j is the place that
          //v1 should go.
        } while (i<j);
        vArray[i] = v1;
        //now vArray[i] is done, and we move on 
      }
    }
    return result;
  }
}
