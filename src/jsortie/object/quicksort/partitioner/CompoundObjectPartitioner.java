package jsortie.object.quicksort.partitioner;

import java.util.Comparator;

import jsortie.object.quicksort.multiway.partitioner.MultiPivotObjectPartitioner;
import jsortie.object.quicksort.multiway.partitioner.SingleToMultiObjectPartitioner;
import jsortie.object.quicksort.multiway.selector.MultiPivotObjectSelector;
import jsortie.object.quicksort.multiway.selector.SingleToMultiObjectSelector;
import jsortie.object.quicksort.selector.SinglePivotObjectSelector;

public class CompoundObjectPartitioner<T>
  implements StandaloneObjectPartitioner<T> {
  protected MultiPivotObjectSelector<T>    selector;
  protected MultiPivotObjectPartitioner<T> partitioner;
  String    name;
  public CompoundObjectPartitioner
    ( MultiPivotObjectSelector<T>   selectorToUse
    , MultiPivotObjectPartitioner<T> partitionerToUse) {
    selector    = selectorToUse;
    partitioner = partitionerToUse;
    name 
      = selectorToUse.toString() 
      + ", " + partitionerToUse.toString();
  }
  public CompoundObjectPartitioner 
    ( SinglePivotObjectSelector<T>    selectorToUse
    , SinglePivotObjectPartitioner<T> partitionerToUse) {
    selector 
      = new SingleToMultiObjectSelector<T>
            ( selectorToUse );
    partitioner 
      = new SingleToMultiObjectPartitioner<T>
            ( partitionerToUse );
    name 
      = selectorToUse.toString() 
      + ", " + partitionerToUse.toString();
  }
  public String toString() {
    return this.getClass().getSimpleName() 
           + "(" + name + ")";
  }
  @Override
  public int[] partitionRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    int[] pivots = selector.selectPivotIndices
                   ( comparator, vArray, start, stop );
    return partitioner.multiPartitionRange
           ( comparator, vArray, start, stop, pivots );
  }

}
