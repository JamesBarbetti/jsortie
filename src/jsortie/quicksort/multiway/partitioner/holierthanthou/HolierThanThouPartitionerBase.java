package jsortie.quicksort.multiway.partitioner.holierthanthou;

import jsortie.janitors.insertion.InsertionSort;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.unidirectional.HolierThanThouExpander;
import jsortie.quicksort.multiway.expander.holierthanthou.HolierThanThouExpanderBase;
import jsortie.quicksort.multiway.partitioner.FixedCountPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class HolierThanThouPartitionerBase
  implements FixedCountPivotPartitioner {
  protected int pivotCount;
  protected int partitionBoundaryCount;
  public PartitionExpander spx;
  public HolierThanThouExpanderBase mpx;
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  } 
  public HolierThanThouPartitionerBase
    ( HolierThanThouExpanderBase expander ) {
    pivotCount = expander.getPivotCount();
    partitionBoundaryCount = 2*(1+pivotCount);
    spx =  new HolierThanThouExpander();
    mpx = expander;
  }
  public HolierThanThouPartitionerBase
    ( PartitionExpander singlePivotExpander
    , HolierThanThouExpanderBase expander) {
    pivotCount = expander.getPivotCount();
    partitionBoundaryCount = 2*(1+pivotCount);
    spx = singlePivotExpander;
    mpx = expander;
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
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
      InsertionSort.sortSmallRange(vArray, start, stop);
      partitions = new int[] { stop, stop };
    }   
    else if (MultiPivotUtils.areAllPivotsTheSame
             ( vArray, pivotIndices )) {
      partitions 
        = MultiPivotUtils.partitionRangeWithOnePivot 
          ( vArray, start, stop, pivotIndices[0], spx);
    } else {
      int frontRangeCount = pivotIndices.length;
      pivotIndices   
        = HolierThanThouUtils.moveEvenlySpacedPivotsToFront
          (vArray, pivotIndices, start, pivotCount); 
      int startRight = start + frontRangeCount;
      partitions
        = mpx.expandPartitions
          ( vArray, start, start, pivotIndices
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
