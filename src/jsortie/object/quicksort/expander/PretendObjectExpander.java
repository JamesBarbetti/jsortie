package jsortie.object.quicksort.expander;

import java.util.Comparator;

import jsortie.object.quicksort.partitioner.SinglePivotObjectPartitioner;

public class PretendObjectExpander<T>
 implements PartitionObjectExpander<T> {
  protected SinglePivotObjectPartitioner<T> actualPartitioner; 
  public PretendObjectExpander 
    ( SinglePivotObjectPartitioner<T> partitioner ) {
    if (partitioner == null) {
      throw new IllegalArgumentException
                ( "PretendObjectExpander: partitioner was null" );
    }
    actualPartitioner = partitioner;
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName() 
           + "(" + actualPartitioner.toString() + ")";
  }
  @Override
  public int expandPartition
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stopLeft
    , int hole, int startRight, int stop) {
    if (start<stopLeft && startRight<stop) {
      hole = actualPartitioner.partitionRange
             ( comparator, vArray, start, stop, hole );
    } else if (start<stopLeft) {
      hole = actualPartitioner.partitionRange
             ( comparator, vArray, start, hole+1, hole );
    } else if (startRight<stop) {
      hole = actualPartitioner.partitionRange
             ( comparator, vArray, hole, stop, hole );
    }
    return hole;
  }
}

