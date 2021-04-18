package jsortie.quicksort.expander.adapter;

import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class PretendExpander
  implements PartitionExpander {
  protected SinglePivotPartitioner actualPartitioner; 
  public PretendExpander 
    ( SinglePivotPartitioner partitioner ) {
    if (partitioner == null) {
      throw 
        new IllegalArgumentException
        ( "PretendExpander: partitioner was null" );
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
    ( int[] vArray, int start, int stopLeft, int hole
    , int startRight, int stop) {
    if (start<stopLeft) {
      if (startRight<stop) {
        hole = actualPartitioner.partitionRange
               ( vArray, start, stop, hole );
      } else {
        hole = actualPartitioner.partitionRange
               ( vArray, start, hole+1, hole );
      }
    } else if (startRight<stop) {
      hole = actualPartitioner.partitionRange
             ( vArray, hole, stop, hole );
    }
    return hole;
  }
}
