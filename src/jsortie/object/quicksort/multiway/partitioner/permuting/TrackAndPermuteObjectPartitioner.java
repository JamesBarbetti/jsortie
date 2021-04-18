package jsortie.object.quicksort.multiway.partitioner.permuting;

import java.util.Comparator;

import jsortie.object.quicksort.multiway.partitioner.MultiPivotObjectPartitioner;
import jsortie.object.quicksort.multiway.partitioner.dutch.BentleyMcIlroyObjectPartitioner;

public class TrackAndPermuteObjectPartitioner<T>
  implements MultiPivotObjectPartitioner<T> {	
  protected BentleyMcIlroyObjectPartitioner<T> 
    bentleyMcIlroy;
  public TrackAndPermuteObjectPartitioner() {
    bentleyMcIlroy 
     = new BentleyMcIlroyObjectPartitioner<T>();
  }
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
    Object vPivots[]      = new Object [ pivotCount ]; 
    
    //copy out the pivots (because, the alternative, 
    //double-array look up on each binary search iteration... 
    //would be... slower, for large enough (stop-start).
    //besides, this is an opportunity to remove any pivots 
    //that compare equal to earlier pivots.
    vPivots[0] = vArray[pivotIndices[0]];
    int p=1;
    for (int r=1; r<pivotCount; ++r) {
      vPivots[p] = vArray[pivotIndices[r]];
      if ( comparator.compare
           ( (T) vPivots[p-1], (T) vPivots[p] ) < 0 ) {
        ++p;
      }
    }
    pivotCount = p;
    if (pivotCount==1) {
      //if all of the pivots are identical, 
      //fall back to Bentley-McIlroy partitioning
      return bentleyMcIlroy.multiPartitionRange
             ( comparator, vArray, start, stop
             , new int[] { pivotIndices[0] });
    }    
    int countOfPartitions 
      = pivotCount + pivotCount + 1;
    
    /*
    String dump = "Pivots ";
    for (int i=0; i<pivotCount; ++i) {
      dump += (i>0) ? ", " : " [ ";
      dump += vPivots[i].toString();
    }
    dump += "]";
    System.out.println(dump);
    */
    
    //count how many items belong in each partition *and* 
    //track which partition each item is in (tracking that 
    //costs us a lot of space (an array of stop-start integers) 
    //but will save us half of the comparisons that 
    //CountAndPermuteObjectPartitioner has to do).
    int[] partitionCounts = new int [ countOfPartitions + 1];
    int[] itemToPartition = new int [ stop - start ]; 
    mapItemsToPartitions ( comparator, vArray, start, stop
                         , vPivots, pivotCount, partitionCounts
                         , itemToPartition);
    return moveItemsToPartitions
           ( vArray, start, stop, countOfPartitions
           , partitionCounts, itemToPartition);
  }
  @SuppressWarnings("unchecked")
  protected void mapItemsToPartitions
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , Object[] vPivots, int pivotCount
    , int[] partitionCounts, int[] itemToPartition) {
    int i;
    int diff;
    for (i=start; i<stop; ++i) {
      T   v = (T) vArray[i];
      int q = 0;          //index of minimum partition in 
                          //which v might still belong
      int r = pivotCount; //number of pivots with which 
                          //v might still need to be compared
      do                  //binary search for the partition
                          //in which v belongs
      {
        int leftCount = r/2;
        //System.out.println("Dump: q=" + q + ", r=" + pivotCount + ",m=" + (q+leftCount));
        diff = comparator.compare
               ( v, (T) vPivots[q + leftCount] );
        if ( diff < 0 ) {
          r = leftCount;
        } else if ( 0 < diff ) {
          q += leftCount + 1;
          r -= leftCount + 1;
        } else {
          q += leftCount;
          break;
        }
      } while (0<r);
      q += q;
      if (diff==0) {
        ++q;
      }
      ++partitionCounts [ q ];
      itemToPartition [ i - start ] = q;
      //System.out.println("Dump: Item at [" + i + "], " + v.toString() + " was mapped to partition " + q);
    }
  }
  protected int[] moveItemsToPartitions
    ( T[] vArray, int start, int stop
    , int countOfPartitions, int[] partitionCounts
    , int[] itemToPartition) {
    //derive pointers to (as yet un-permuted 
    //portions of) partitions
    int [] partitionPointers 
      = new int [ countOfPartitions + 1]; 
    int i = start;
    for (int p=0; p<=countOfPartitions ; ++p) {
      int c = partitionCounts[p];
      partitionPointers[p] = i;
      i += c;
    }
    //DumpRangeHelper.dumpArray("partition pointers", partitionPointers);
    //Permute (this applies exactly the same permutation, 
    //to shuffle the elements into their correct partitions, 
    //as did CountAndPermuteObjectPartitioner. But instead 
    //of re-examining each item, v, by comparing it with pivots, 
    //it looks up the partition number recorded for v in the 
    //itemToPartition array.
    i = start;
    int p = 0; 
    while ( partitionCounts[p] ==0 ) { 
      ++p; 
    } //skip any empty partitions
    T   v = (T) vArray [ i ];
    int q = itemToPartition [ i - start ];
    for (;;) { 
      //here, p is the first partition which might 
      //        contain items belonging elsewhere;
      //      i is the first element in that partition 
      //        that wasn't placed right,
      //      v is an element from the permutation 
      //        cycle involving the element that was at [i], and
      //      q is the partition in which v belongs
    	
      int j     = partitionPointers[q];
      
      //the element, v, that was at [i] 
      //belongs at [j], in partition p2
      ++partitionPointers[q];
      --partitionCounts[q];
      
      if (i!=j) { //find more elements involved in 
                  //the cycle that includes [i] and [j]
        T u       = (T) vArray[j];
        vArray[j] = v;
        //System.out.println("Dump: wrote " + v.toString() + " to [" + j + "]");
        v         = u; //v now the element that was at [j]
        q         = itemToPartition[j-start];
      } else { 
        //System.out.println("Dump: end of cycle, setting [" + i + "] to " + v.toString());
        vArray[i] = v;
        //find next element not known to be in its partition
        while ( partitionCounts[p] ==0 ) { //skipping partitions 
          //where every element is in place
          ++p;
          if ( countOfPartitions <= p ) {
            break; //and exiting if every element 
                   //in the last partition is placed
          }
        }
        if (countOfPartitions<=p) {
          break;
        }
        i = partitionPointers[p];
        q = itemToPartition[i-start];
        v = (T) vArray[i];
      }
    }
    /*
    String dump = "Output ";
    for (i=start; i<stop; ++i) {
      dump += (start<i) ? ", " : " [ ";
      dump += vArray[i].toString();
    }
    dump += "]";
    System.out.println(dump);
    */    
    for (p=countOfPartitions;0<p;--p) {
      partitionPointers[p]
        = partitionPointers[p-1];
    }
    partitionPointers[0] = start;
    //DumpRangeHelper.dumpArray("boundaries", partitionPointers);
    //odd-numbered partitions contain items equal to pivots
    return partitionPointers;
  }
}
