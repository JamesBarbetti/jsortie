package jsortie.object.quicksort.multiway.partitioner.permuting;

import java.util.Comparator;

import jsortie.object.quicksort.multiway.partitioner.MultiPivotObjectPartitioner;
import jsortie.object.quicksort.multiway.partitioner.dutch.BentleyMcIlroyObjectPartitioner;

public class CountAndPermuteObjectPartitioner<T> 
  implements MultiPivotObjectPartitioner<T> {	
  BentleyMcIlroyObjectPartitioner<T> bentleyMcIlroy
    = new BentleyMcIlroyObjectPartitioner<T>();
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
  @SuppressWarnings("unchecked")
  @Override
  public int[] multiPartitionRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int[] pivotIndices) {
    int pivotCount        = pivotIndices.length;
    
    //copy out the pivots (because, the alternative, 
    //double-array look up on each binary search iteration... 
    //would be... slower, for large enough (stop-start).
    //besides, this is an opportunity to remove any pivots 
    //that compare equal to earlier pivots.
    T vPivots[] = (T[]) new Object[pivotCount]; 
    pivotCount  
      = copyDistinctPivots
        ( comparator, vArray, pivotIndices
        , vPivots, pivotCount );
    if (pivotCount==1) {
      //if all of the pivots are identical, 
      //fall back to Bentley-McIlroy partitioning
      return bentleyMcIlroy.multiPartitionRange
             ( comparator, vArray, start, stop
             , new int[] { pivotIndices[0] } ); 
    }
    int countOfPartitions = pivotCount + pivotCount + 1;
    int partitionCounts[] = new int    [ countOfPartitions + 1 ];
    //count how many items belong in each of the (pivotCount+1) partitions
    determinePartitionCounts 
      ( comparator, vArray, start, stop
      , vPivots, pivotCount, partitionCounts);
    //derive pointers to (as yet un-permuted portions of) partitions
    int [] partitionPointers = partitionCounts; 
    //same array, new name (new purpose)
    int [] partitionStops    = new int [ countOfPartitions + 1 ];
    int i = start;
    int p;
    for (p=0; p<countOfPartitions; ++p) {
      partitionStops[p]    = partitionCounts[p]+i;
      partitionPointers[p] = i;
      i = partitionStops[p];
    }
    moveItemsToPartitions 
      ( comparator, vArray, start, stop, vPivots, pivotCount
      , partitionPointers, partitionStops, countOfPartitions );
    return adjustBoundaries(start, partitionPointers);
  }
  private int[] adjustBoundaries
    ( int start, int[] partitionPointers ) {
    //Construct an array of partition boundaries 
    //from the partition pointers (each of which 
    //now points to the index of the first element 
    //in the next partition)
    for (int p=partitionPointers.length-1; 0<p; --p) {
      partitionPointers[p] = partitionPointers[p-1];
    }
    partitionPointers[0] = start;
    return partitionPointers;
  }
  protected int copyDistinctPivots
    ( Comparator<? super T> comparator
    , T[] vArray,  int[] pivotIndices
    , T[] vPivots, int pivotCount) {
    vPivots[0] = vArray[pivotIndices[0]];
    int p=1;
    for (int r=1; r<pivotCount; ++r) {
      vPivots[p] = vArray[pivotIndices[r]];
      if ( comparator.compare
           ( vPivots[p-1], vPivots[p] ) 
           < 0 ) {
        ++p;
      }
    }
    return p;
  }
  protected void determinePartitionCounts
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , T[] vPivots, int pivotCount
    , int[] partitionCounts ) {
    int i;
    for (i=start; i<stop; ++i) {
      T   v = (T) vArray[i];
      int q = 0;          //index of minimum partition 
                          //in which v might still belong
      int r = pivotCount; //number of pivots with which v 
                          //might still need to be compared
      do
      {
        int x = (r-1)/2;
        if ( comparator.compare(v, (T) vPivots[q+x]) <= 0 ) {
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
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , T[] vPivots, int pivotCount
    , int[] partitionPointers, int[] partitionStops
    , int countOfPartitions ) {
    //permute
    int i = start;
    int p = 0;
    while ( partitionPointers[p] == partitionStops[p] ) { 
      ++p; //skip any empty partitions
    }
    T v = (T) vArray[i];
    for (;;) { //here, p is first partition whose current 
              //elements might not belong in it
      int q = 0; //index of minimum partition 
                 //in which v might still belong
      int r = pivotCount; //number of pivots with which 
                          //v might still need to be compared
      //the element that was at [i] 
      //belongs at [j], in partition q, p<=q
      do
      {
        int x = (r-1)/2;
        if ( comparator.compare
             ( v, (T) vPivots[q+x] ) <= 0 ) {
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
      int j     = partitionPointers[q];
      ++partitionPointers[q];
      T u       = (T) vArray[j];
      vArray[j] = v; //v is now where it should have been (at [j])
      if (i!=j) { //find more elements involved in the cycle 
                  //that includes [i] and [j]
        v = u; //and now we must place 
               //the item that was formerly at [j]
      } else { //find next element not known to be in its partition
        ++i;
        if ( partitionPointers[p] == partitionStops[p]) {
          do {
            ++p;
            if (p==countOfPartitions) break;
          } while (partitionPointers[p] == partitionStops[p]);
          if (p==countOfPartitions) break;
          i = partitionPointers[p];
        }
        v = vArray[i];
      }
    }
  }
}
