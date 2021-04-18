package jsortie.object.quicksort.protector;

import java.util.Comparator;

import jsortie.object.quicksort.helper.ObjectPartitionHelper;
import jsortie.object.quicksort.partitioner.SinglePivotObjectPartitioner;

public class CheckedObjectPartitioner<T> 
  implements SinglePivotObjectPartitioner<T> {
  protected SinglePivotObjectPartitioner<T> innerPartitioner;
  protected ObjectPartitionHelper<T>        helper;
  protected String                          innerName;
  
  public CheckedObjectPartitioner
    ( SinglePivotObjectPartitioner<T> inner ) {
    helper           = new ObjectPartitionHelper<T>();
    innerPartitioner = inner;
    innerName        = inner.toString();
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName()
    + "(" + innerPartitioner.toString() + ")";
  } 
  @Override
  public int partitionRange
    ( Comparator<? super T> comparator
    , T [] vArray, int start, int stop
    , int pivotIndex ) {
    T[] vTrashableCopy 
      = helper.copyOfRange(vArray, start, stop);
    pivotIndex 
      = innerPartitioner.partitionRange
        ( comparator, vArray, start, stop, pivotIndex );
    helper.checkPartition
      ( innerName, comparator
      , vArray, start, pivotIndex, stop );
    helper.checkRangeIsPermutationOf
      ( innerName, comparator
      , vArray, start, stop, vTrashableCopy);
    return pivotIndex;
  }
}
