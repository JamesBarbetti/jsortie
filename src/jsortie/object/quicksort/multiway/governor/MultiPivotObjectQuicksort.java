package jsortie.object.quicksort.multiway.governor;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.janitor.ObjectBinaryInsertionSort;
import jsortie.object.mergesort.ArrayObjectMergesort;
import jsortie.object.quicksort.multiway.MultiPivotObjectUtils;
import jsortie.object.quicksort.multiway.partitioner.MultiPivotObjectPartitioner;
import jsortie.object.quicksort.multiway.partitioner.YaroslavskiyObjectPartitioner2;
import jsortie.object.quicksort.multiway.selector.MultiPivotObjectSelector;
import jsortie.object.quicksort.multiway.selector.PositionalPivotObjectSelector;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class MultiPivotObjectQuicksort<T>
  implements ObjectRangeSorter<T> {
  protected MultiPivotObjectSelector<T>    selector;
  protected MultiPivotObjectPartitioner<T> partitioner;
  protected ObjectRangeSorter<T>           janitor;
  protected int                            threshold;
  protected ObjectRangeSorter<T>           lastResort;
  protected String                         name;
  
  protected MultiPivotObjectUtils<T>       utils;
  protected MultiPivotUtils                typeFreeUtils;
  
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
  public MultiPivotObjectQuicksort
     ( MultiPivotObjectSelector<T> selector
     , MultiPivotObjectPartitioner<T> partitioner
     , ObjectRangeSorter<T> janitor, int threshold
     , ObjectRangeSorter<T> lastResort) {
    this.selector    = selector;
    this.partitioner = partitioner;
    this.janitor     = janitor;
    this.threshold   = threshold;		
    this.lastResort  = lastResort;
    this.utils       = new MultiPivotObjectUtils<T>();
    this.typeFreeUtils = new MultiPivotUtils();
    setName();
  }
  public MultiPivotObjectQuicksort() {
    YaroslavskiyObjectPartitioner2<T> yaro 
      = new YaroslavskiyObjectPartitioner2<T>();
    selector
      = new PositionalPivotObjectSelector<T>
            ( 2, 3 );
    partitioner   = yaro;
    janitor       = new ObjectBinaryInsertionSort<T>();
    threshold     = 32;		
    lastResort 
      = new ArrayObjectMergesort<T>(janitor, threshold);
    name = this.getClass().getSimpleName();
    utils = new MultiPivotObjectUtils<T>();	  
    typeFreeUtils = new MultiPivotUtils();
  }
  @Override 
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop ) {
    if (start+1<stop) {
      adaptiveSortRange 
        ( comparator, vArray, -1, start, stop, -1
        , (int) Math.floor(Math.log(stop-start)/Math.log(2)*3));
    }
  }
  private void adaptiveSortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int lowBoundary, int start, int stop
    , int hiBoundary, int maxDepth) {
    while ( threshold < stop-start ) {
      if ( maxDepth < 1 ) {
        lastResort.sortRange
          ( comparator, vArray, start, stop );
        return;
      }
      int[] pivots 
        = selector.selectPivotIndices
          ( comparator, vArray, start, stop );
      int[] partitions 
        = partitioner.multiPartitionRange
          ( comparator, vArray, start, stop, pivots );
      int i 
        = typeFreeUtils.indexOfLargestPartition
          ( partitions );
      --maxDepth;
      for (int j=0; j<partitions.length; j+=2) {
        if (i != j && partitions[j] < partitions[j+1]) {
          int leftBoundary  = lowBoundary;
          int rightBoundary = hiBoundary;
          if ( 0<j && partitions[j-1]<partitions[j] 
               && start<partitions[j]) {
            leftBoundary = partitions[j]-1;
          }
          if ( j+2<partitions.length 
              && partitions[j+1]<partitions[j+2] 
            && partitions[j+1]<stop) {
            rightBoundary = partitions[j+1];
          }
          if (leftBoundary==-1 || rightBoundary==-1 
            || comparator.compare
               ( vArray[leftBoundary]
               , vArray[rightBoundary] )<0) {
            adaptiveSortRange
              ( comparator, vArray, leftBoundary
              , partitions[j], partitions[j+1]
              , rightBoundary, maxDepth);
          }
        }
      }
      if ( partitions[i+1] <= partitions[i] + 1 ) {
        return;
      }
      lowBoundary 
        = start<partitions[i] ? (partitions[i]-1)  : lowBoundary;
      hiBoundary  
        = partitions[i+1]<stop ? (partitions[i+1]) : hiBoundary;
      start = partitions[i];
      stop  = partitions[i+1];
      if ( lowBoundary!=-1 && hiBoundary!=-1
            && comparator.compare
               ( vArray[lowBoundary]
               , vArray[hiBoundary])==0) {
        return;
      }
    }
    janitor.sortRange
      ( comparator, vArray, start, stop );
  }
}
