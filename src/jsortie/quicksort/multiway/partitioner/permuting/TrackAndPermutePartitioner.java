package jsortie.quicksort.multiway.partitioner.permuting;

public class TrackAndPermutePartitioner
  extends CountAndPermutePartitioner {	
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    int pivotCount = pivotIndices.length;
    int vPivots[]
      = allocatePivotArray(pivotCount);
    pivotCount 
      = copyDistinctPivots
        ( vArray, pivotIndices, vPivots );
    if (pivotCount==1) {
      //if all of the pivots are identical, 
      //fall back to Bentley-McIlroy partitioning
      return bentleyMcIlroy.multiPartitionRange
             ( vArray, start, stop
             , new int[] { pivotIndices[0] });
    }    
    int countOfPartitions 
      = pivotCount + pivotCount + 1;
    //count how many items belong in each partition 
    //*and* track which partition each item is in
    //(tracking that costs us a lot of space 
    //(an array of stop-start integers) but will save us
    // half of the comparisons that 
    //CountAndPermuteObjectPartitioner has to do).
    int[] partitionCounts  
      = allocatePartitionCountArray
        ( countOfPartitions + 1 );
    int[] itemToPartition
      = allocateItemToPartitionArray(stop-start);
    mapItemsToPartitions 
      ( vArray, start, stop, vPivots
      , pivotCount, partitionCounts
      , itemToPartition );
    return 
      moveItemsToPartitions
      ( vArray, start, stop, countOfPartitions
      , partitionCounts, itemToPartition );
  }
  public int[] allocatePivotArray
    ( int pivotCount ) {
    return new int [ pivotCount ];
  }
  public int[] allocatePartitionCountArray
    ( int size ) {
    return new int[size];
  }
  public int[] allocateItemToPartitionArray
    ( int count ) {
    return new int[count];
  }
  protected void mapItemsToPartitions
     ( int[] vArray, int start, int stop
     , int[] vPivots, int pivotCount
     , int[] partitionCounts
     , int[] itemToPartition) {
    int i;
    for (i=start; i<stop; ++i) {
      int v = vArray[i];
      int q = 0;          //index of minimum partition still <= v
      int r = pivotCount; //number of pivots with which
                          //v might still need to be compared
      do {         //binary search
        int x = r/2;
        if ( v <= vPivots[q+x] ) {
          r  = x;
        } else {
          q += (x + 1);
          r -= (x + 1);
        }
      } while (0<r);
      if ( q<pivotCount && vPivots[q]==v) {
        r=1;
      }
      q += q + r;
      ++partitionCounts[q];
      itemToPartition[i-start] = q; //the extra bit
    }
  }
  protected int[] moveItemsToPartitions
    ( int[] vArray, int start, int stop
    , int countOfPartitions
    , int[] partitionCounts
    , int[] itemToPartition) {
    //Derive pointers to (as yet un-permuted 
    //portions of) partitions
    int [] partitionPointers 
      = new int [ countOfPartitions + 1 ];
    int [] partitionStop 
      = partitionCounts;
    int i = start;
    int p;
    for (p=0; p<countOfPartitions; ++p) {
      partitionPointers[p] = i;
      i += partitionCounts[p];
      partitionStop[p] = i; 
      //now, partitionCounts records 
      //where each partition ends.
    }
    //Permute (this applies exactly the same permutation, 
    //to shuffle the elements into their correct partitions, 
    //as did CountAndPermuteObjectPartitioner.  But instead 
    //of re-examining each item, v, by comparing it with
    //pivots, it looks up the partition number recorded for
    //v in the itemToPartition array.
    i = start;
    p = 0; 
    while ( partitionPointers[p] == partitionStop[p] ) { 
      ++p; //skip any empty partitions 
    } 
    int v = vArray[i];
    int q = itemToPartition[i-start];
    for (;;) { 
      //here, p is the first partition which 
      //  might contain items belonging elsewhere;
      //i is the first element in that partition 
      //  that wasn't placed right,
      //v is an element from the permutation cycle 
      //  involving the element that was at [i]
      //and q is the partition in which v belongs
    	
      int j     = partitionPointers[q];
      //the element, v, that was at [i] 
      //belongs at [j], in partition p2
      ++partitionPointers[q];
      int u     = vArray[j];
      vArray[j] = v;
      if (i!=j) { //find more elements involved in 
                  //the cycle that includes [i] and [j]
        v         = u; //v now the element that was at [j]
        q         = itemToPartition[j-start];
      } else { //find next element not known 
               //to be in its partition
        ++i;
        if ( partitionPointers[p] == partitionStop[p] ) { 
          //skipping partitions where every item belongs
          do {
            ++p;
          } while ( p<countOfPartitions 
                    && partitionPointers[p]
                       == partitionStop[p] );
          if (countOfPartitions<=p) {
            break; 
            //and exiting if every element 
            //in the last partition is placed
          }
          i = partitionPointers[p]; 
          //i = index, of next element 
          //that might be misplaced
        }
        v = vArray[i]; //and now me must place 
                       //the item, v, that was at [i]
        q = itemToPartition[i-start];
      }
    }
    return adjustBoundaries 
           ( start, partitionPointers );
  }
}
