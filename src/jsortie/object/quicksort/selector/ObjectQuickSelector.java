package jsortie.object.quicksort.selector;

import java.util.Comparator;

import jsortie.object.quicksort.partitioner.CentripetalObjectPartitioner;
import jsortie.object.quicksort.partitioner.SinglePivotObjectPartitioner;

public class ObjectQuickSelector<T> 
	implements KthStatisticObjectPartitioner<T>
{
  /*todo: should also implement 
   *      SinglePivotObjectSelector<T>
   *    , SinglePivotObjectPartitioner<T> */ 
  public SinglePivotObjectSelector<T>    selector;
  public SinglePivotObjectPartitioner<T> partitioner;
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "(" 
      + selector.toString() + ", "
      + partitioner.toString() + ")";				
    }
  public ObjectQuickSelector() {
    selector    = new MiddleElementObjectSelector<T>();
    partitioner = new CentripetalObjectPartitioner<T>();
  }
  public ObjectQuickSelector 
    ( SinglePivotObjectSelector<T> selectorToUse
    , SinglePivotObjectPartitioner<T> basePartitioner) {
    selector    = selectorToUse;
    partitioner = basePartitioner;
  }
  public ObjectQuickSelector 
    ( SinglePivotObjectPartitioner<T> basePartitioner ) {
    selector    = new MiddleElementObjectSelector<T>();
    partitioner = basePartitioner;
  }
  @Override
  public void partitionRangeExactly
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int target) {
    int pivot = target;
    for (;;) {
      int split = partitioner.partitionRange
                  (comparator, vArray, start, stop, pivot);
      if (split < target) {
        start=split+1;
      } else if (target < split) {
        stop=split;
      } else { 
        return;
      }
      pivot = selector.selectPivotIndex
              (comparator, vArray, start, stop);
    }
  }  
}
