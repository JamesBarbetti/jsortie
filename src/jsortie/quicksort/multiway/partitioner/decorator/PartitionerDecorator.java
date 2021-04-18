package jsortie.quicksort.multiway.partitioner.decorator;
import java.util.Arrays;

import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.adapter.SingleToMultiPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public abstract class PartitionerDecorator 
  implements MultiPivotPartitioner {
  protected MultiPivotPartitioner partitioner;
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + partitioner.toString() + ")";
  }
  public PartitionerDecorator
    ( SinglePivotPartitioner onePivotPartitioner ) {
    this.partitioner 
      = new SingleToMultiPartitioner
            ( onePivotPartitioner ); 
  }
  public PartitionerDecorator
    ( MultiPivotPartitioner multiPartitioner ) {
    this.partitioner = multiPartitioner; 
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start , int stop
    , int[] pivotIndices)  {
    int partitions[] 
      = partitioner.multiPartitionRange
        ( vArray,  start, stop, pivotIndices );
    return shrinkPartitions
           ( vArray, start, stop, partitions );
  }
  public abstract int[] shrinkPartitions
    ( int[] vArray, int start, int stop
    , int[] partitions );
  
  public int[] survivingPartitions
    ( int[] partitions, int w ) {
    if (w<partitions.length) {
      if (w==0) {
        return new int[] { partitions[0], partitions[0] };
      } else {
        return Arrays.copyOf(partitions, w);
      }
    } else {
      return partitions;
    }
  }
}
