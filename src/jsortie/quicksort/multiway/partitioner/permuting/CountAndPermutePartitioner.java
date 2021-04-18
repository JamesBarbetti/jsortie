package jsortie.quicksort.multiway.partitioner.permuting;

import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.singlepivot.BentleyMcIlroyPartitioner;

public class CountAndPermutePartitioner 
  implements MultiPivotPartitioner {	
  //Note: this is merely a down-port of 
  //      CountAndPermuteObjectPartitioner<T>
  //      that works on integers rather than T.
  BentleyMcIlroyPartitioner bentleyMcIlroy;
  public CountAndPermutePartitioner() {
   bentleyMcIlroy = new BentleyMcIlroyPartitioner();
  }
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices) {
    int pivotCount = pivotIndices.length;
    int vPivots[]  = new int [ pivotIndices.length ]; 
    pivotCount = copyDistinctPivots
                 ( vArray, pivotIndices, vPivots );
    if (pivotCount==1) {
      //if all of the pivots are identical, 
      //fall back to Bentley-McIlroy partitioning
      return bentleyMcIlroy.multiPartitionRange
             ( vArray, start, stop
             , new int[] { pivotIndices[0] });
    }
    int countOfPartitions = pivotCount + pivotCount + 1;
    //count how many items belong in each of partition
    int partitionCounts[] = new int [ countOfPartitions ];
    determinePartitionCounts
      ( vArray, start, stop, vPivots
      , pivotCount, partitionCounts);
    //derive pointers to (as yet un-permuted 
    //countOfPartitions portions of) partitions
    int [] partitionPointers 
      = new int [ countOfPartitions + 1 ];
    int [] partitionStop 
       = partitionCounts; //Evil trick: we can reuse!
    int i = start;
    int p;
    for (p=0; p<countOfPartitions; ++p) {
      partitionPointers[p] = i;
      i += partitionCounts[p];
      partitionStop[p] = i;
    }
    moveItemsToPartitions
      ( vArray, start, stop, vPivots, pivotCount
      , partitionStop, partitionPointers
      , countOfPartitions);
    //Each element of partitionPointers[] now points to 
    //the index of the first element in the next partition)
    //Move them one place to the right
    return adjustBoundaries(start, partitionPointers);
  }
  protected int copyDistinctPivots
    ( int[] vArray, int[] pivotIndices, int[] vPivots) {
    //copy out the pivots (because, the alternative, 
    //double-array look up on each binary
    //search iteration... would be... slower, 
    //for large enough (stop-start).
    //besides, this is an opportunity to remove 
    //any pivots that compare equal
    //to earlier pivots.
    vPivots[0] = vArray[pivotIndices[0]];
    int originalPivotCount = pivotIndices.length;
    int distinctPivotCount=1;
    for (int r=1; r<originalPivotCount; ++r) {
      vPivots[distinctPivotCount]
        = vArray[pivotIndices[r]];
      if ( vPivots[distinctPivotCount-1] 
         < vPivots[distinctPivotCount] ) {
        ++distinctPivotCount;
      }
    }
    return distinctPivotCount;
  }
  protected void determinePartitionCounts
    ( int[] vArray,   int start, int stop
    , int[] vPivots,  int pivotCount
    , int[] partitionCounts) {
    for (int i=start; i<stop; ++i) {
      int v = vArray[i];
      int q = 0;//index of minimum pivot
                //still <= v
      int r = pivotCount; //number of pivots 
        //with which v might still need to 
        //be compared, -1
      do {         //binary search
        int x = (r-1)/2;
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
      q += q+r;
      ++partitionCounts[q];
    }
  }
  protected void moveItemsToPartitions
    ( int[] vArray, int start, int stop
    , int[] vPivots, int pivotCount
    , int[] partitionStop, int[] partitionPointers
    , int countOfPartitions) {
    int i = start;
    int p = 0;
    //skip any empty partitions 
    //should be at most one
    while ( partitionPointers[p] 
            == partitionStop[p] ) {
      ++p; 
    } 
    //permute
    int v = vArray[i];
    for (;;) { //here, p is first partition whose 
               //items might not all belong in it
      int q = 0;          //index of minimum pivot
                          //still <= v
      int r = pivotCount; //number of pivots with
                          //which v might still need
                          //to be compared, -1
      do {                //binary search
        int x = (r-1)/2;
        if ( v <= vPivots[q+x] ) {
          r  = x;
        } else {
          q += (x + 1);
          r -= (x + 1);
        }
      } while (0<r);
      if ( q<pivotCount && vPivots[q]==v) {
        r = 1;
      }
      q += q+r;
      //the element that was at [i] belongs 
      //at [j], in partition q, p<=q
      int j     = partitionPointers[q];
      ++partitionPointers[q];
      int u     = vArray[j];
      vArray[j] = v; //v is now where it 
                     //should have been (at [j])
      if (i!=j) { //find more elements involved in the 
                  //cycle that includes [i] and [j]
        v = u; // and now we must place the 
               // element that was formerly at [j]
      } else { // find next item not known 
               // to be in its partition
        vArray[i] = v;
        ++i;
        if ( partitionPointers[p] 
          == partitionStop[p] ) { 
          //skipping partitions where 
          //every element is in place
          do {
            ++p;
          } while ( p < countOfPartitions 
                    && partitionPointers[p]
                       == partitionStop[p]);
          if (countOfPartitions<=p) {
            break; //exit if every item in the 
            //last partition is placed in 
            //the right partition
          }
          i = partitionPointers[p]; 
          //i = index, of next element 
          //that might be misplaced
        }
        if (countOfPartitions<=p) break; 
        v = vArray[i]; //and now me must place the 
                       //item, v, that was at [i]
      }
    }
  }
  protected int[] adjustBoundaries
    ( int start, int[] partitionPointers ) {
    for (int p=partitionPointers.length-1;0<p;--p) {
      partitionPointers[p]
        = partitionPointers[p-1];
    }
    partitionPointers[0] = start;
    return partitionPointers;
  }
}
