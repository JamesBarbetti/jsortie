package jsortie.object.quicksort.partitioner;

import java.util.Comparator;

import jsortie.object.quicksort.expander.PartitionObjectExpander;
import jsortie.object.quicksort.expander.ReverseSingletonObjectExpander;

public class ReverseSingletonObjectPartitioner<T> 
  implements SinglePivotObjectPartitioner<T> {
  protected PartitionObjectExpander<T> expander 
    = new ReverseSingletonObjectExpander<T>();
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override public int partitionRange
    ( Comparator<? super T> comparator, T[] vArray
    , int start, int stop
    , int pivotIndex ) {
    int count          = stop - start;
    int hole           = start + count / 2;
    T vPivot           = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[hole];
    vArray[hole]       = vPivot;
    return expander.expandPartition
           ( comparator, vArray, start, hole
           , hole, hole+1, stop );
  } 
}
