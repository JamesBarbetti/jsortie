package jsortie.object.quicksort.multiway.partitioner.holierthanthou;

import java.util.Comparator;

import jsortie.object.janitor.ObjectInsertionSort;
import jsortie.object.quicksort.expander.HolierThanThouObjectExpander;
import jsortie.object.quicksort.expander.PartitionObjectExpander;
import jsortie.object.quicksort.multiway.expander.HolierThanThouObjectExpanderBase;
import jsortie.object.quicksort.multiway.partitioner.FixedCountPivotObjectPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class HolierThanThouObjectPartitionerBase<T>
  implements FixedCountPivotObjectPartitioner<T> {
  protected int pivotCount;
  protected int partitionBoundaryCount;
  public PartitionObjectExpander<T> spx;
  public HolierThanThouObjectExpanderBase<T> mpx;
  protected HolierThanThouObjectUtils<T> utils
    = new HolierThanThouObjectUtils<T>();
  protected ObjectInsertionSort<T> osort
    = new ObjectInsertionSort<T>();
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  } 
  public HolierThanThouObjectPartitionerBase
    ( HolierThanThouObjectExpanderBase<T> expander ) {
    pivotCount = expander.getPivotCount();
    partitionBoundaryCount = 2*(1+pivotCount);
    spx =  new HolierThanThouObjectExpander<T>();
    mpx = expander;
  }
  public HolierThanThouObjectPartitionerBase
    ( PartitionObjectExpander<T> singlePivotExpander
    , HolierThanThouObjectExpanderBase<T> expander) {
    pivotCount = expander.getPivotCount();
    partitionBoundaryCount = 2*(1+pivotCount);
    spx = singlePivotExpander;
    mpx = expander;
  }
  @Override
  public int[] multiPartitionRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int[] pivotIndices) {
    int[] partitions;
    if (stop-start<pivotCount) {
      //note: although for HolierThanThouPartitioner2, stop-start
      //      must be less than 2, so there's no sort needed, 
      //      for subclasses where getPivotCount() returns
      //      k (>3), there might be a (k-1)-item range 
      //      needing sorting; hence the call to InsertionSort's 
      //      sortSmallRange method, here. This class doesn't need it,
      //      but its subclasses do.      
      osort.sortRange
        ( comparator, vArray, start, stop );
      partitions = new int[] { stop, stop };
    }   
    else if (utils.areAllPivotsTheSame
             ( comparator, vArray, pivotIndices )) {
      partitions 
        = utils.partitionRangeWithOnePivot 
          ( comparator, vArray, start, stop, pivotIndices[0], spx);
    } else {
      int frontRangeCount = pivotIndices.length;
      pivotIndices   
        = utils.moveEvenlySpacedPivotsToFront
          ( comparator, vArray, pivotIndices
          , start, pivotCount); 
      int startRight = start + frontRangeCount;
      partitions
        = mpx.expandPartitions
          ( comparator, vArray
          , start, start, pivotIndices
          , startRight, stop);
    }
    return MultiPivotUtils.ensurePartitionCount
           ( partitions
           , getPartitionBoundaryCount());
  }
  @Override
  public int getPivotCount() {
    return pivotCount;
  }
  public int getPartitionBoundaryCount() {
    return partitionBoundaryCount; 
  }  
}
