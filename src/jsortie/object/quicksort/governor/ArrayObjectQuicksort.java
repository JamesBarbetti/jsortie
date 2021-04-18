package jsortie.object.quicksort.governor;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.heapsort.ObjectHeapSort;
import jsortie.object.mergesort.ArrayObjectMergesort;
import jsortie.object.quicksort.multiway.selector.RemedianPivotObjectSelector;
import jsortie.object.quicksort.partitioner.SinglePivotObjectPartitioner;
import jsortie.object.quicksort.partitioner.SingletonObjectPartitioner;
import jsortie.object.quicksort.protector.CheckedObjectPartitioner;
import jsortie.object.quicksort.protector.CheckedObjectRangeSorter;
import jsortie.object.quicksort.protector.CheckedPivotObjectSelector;
import jsortie.object.quicksort.selector.SinglePivotObjectSelector;

public class ArrayObjectQuicksort<T>
  implements ObjectRangeSorter<T> {
  protected SinglePivotObjectSelector<T>    selector;
  protected SinglePivotObjectPartitioner<T> partitioner;
  protected ObjectRangeSorter<T> janitor;
  protected int                  threshold;
  protected ObjectRangeSorter<T> lastResort;
  protected String               name;
  public String toString() {
    return name;
  }
  public void setName() {
    name = this.getClass().getSimpleName() 
         + "(" + selector.toString() 
         + ", " + partitioner.toString()
         + ", " + janitor.toString() 
         + ", " + threshold 
         + ", " + lastResort.toString() + ")"; 
  }
  public ArrayObjectQuicksort
    ( SinglePivotObjectSelector<T> selector
    , SinglePivotObjectPartitioner<T> partitioner
    , ObjectRangeSorter<T> janitor, int threshold
    , ObjectRangeSorter<T> lastResort) {
    this.selector    = selector;
    this.partitioner = partitioner;
    this.janitor     = janitor;
    this.threshold   = threshold;		
    this.lastResort  = lastResort;
    setName();
  }
  public ArrayObjectQuicksort() {
    this.selector
      = new RemedianPivotObjectSelector<T>();
    this.partitioner 
      = new SingletonObjectPartitioner<T>();
    this.janitor
      = new ObjectHeapSort<T>();
    this.threshold = 32;		
    this.lastResort
      = new ArrayObjectMergesort<T>
            ( janitor, threshold );
    this.name 
      = this.getClass().getSimpleName();
  }
  @Override public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    if (stop-start<threshold) {
      janitor.sortRange
        ( comparator, vArray,  start, stop );
    } else {
      int maxPartitionDepth = 
        1 + (int) Math.floor
                  ( Math.log(stop-start) 
                    / Math.log(2) * 2.0);
      sortSubRange
      ( comparator, vArray
      , start, stop, maxPartitionDepth);
    }
  }
  protected void sortSubRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int maxPartitionDepth) {
    while ( threshold<stop-start 
            && 0<maxPartitionDepth) {
      --maxPartitionDepth;
      int pivotIndex 
        = selector.selectPivotIndex
          ( comparator, vArray, start, stop );
      pivotIndex 
        = getPartitioner(maxPartitionDepth)
          .partitionRange
          ( comparator, vArray
          , start, stop, pivotIndex );
      if ( pivotIndex-start <= stop-pivotIndex ) {
        sortSubRange 
          ( comparator, vArray, start
          , pivotIndex, maxPartitionDepth);
        start = pivotIndex+1;
      } else {
        sortSubRange 
          ( comparator, vArray, pivotIndex+1, stop
          , maxPartitionDepth);
        stop = pivotIndex;
      }
    }
    if (start<stop) {
      if (0<maxPartitionDepth) {
        janitor.sortRange
          ( comparator, vArray, start, stop );
      } else {
        lastResort.sortRange
          ( comparator, vArray, start, stop );
      }
    }
  }
  protected SinglePivotObjectPartitioner<T> 
    getPartitioner ( int maxPartitionDepth ) {
    return partitioner;
  }
  public ArrayObjectQuicksort<T> 
    getParanoidVersion() {
    ArrayObjectQuicksort<T> paranoid 
      = new ArrayObjectQuicksort<T>(); 
    paranoid.selector
      = new CheckedPivotObjectSelector<T>(selector);
    paranoid.partitioner 
      = new CheckedObjectPartitioner<T>(partitioner);
    paranoid.janitor
      = new CheckedObjectRangeSorter<T>(janitor);
    paranoid.threshold = threshold;
    paranoid.lastResort 
      = new CheckedObjectRangeSorter<T>(lastResort);
    return paranoid;
  }
}
