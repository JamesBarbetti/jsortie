package jsortie.quicksort.multiway.expander.holierthanthou;

import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.unidirectional.HolierThanThouExpander;
import jsortie.quicksort.multiway.expander.MultiPivotPartitionExpander;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public abstract class HolierThanThouExpanderBase 
  implements MultiPivotPartitionExpander {
  protected PartitionExpander singlePivotExpander;
  protected int desiredPivotCount;
  protected int desiredPartitionBoundaryCount;
  public HolierThanThouExpanderBase(int p) {
    desiredPivotCount = p;
    desiredPartitionBoundaryCount = (p+1) << 1;
    singlePivotExpander = new HolierThanThouExpander();
  }
  public HolierThanThouExpanderBase
    ( int p, PartitionExpander spx ) {
    desiredPivotCount = p;
    desiredPartitionBoundaryCount = (p+1) << 1;
    singlePivotExpander = spx;
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  } 
  @Override
  public int[] expandPartitions 
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int startRight, int stop) {
    int pivotCount = pivotIndices.length;
    int[] partitions;
    if ( pivotCount < 2 
         || pivotCount < desiredPivotCount ) {
      int pivotIndex 
        = pivotIndices[pivotIndices.length/2];
      partitions 
        = expandSinglePivotPartition
          ( vArray, start, stopLeft, pivotIndex
          , startRight, stop);
    } else if ( startRight < stop ) {
      partitions 
        = expandPartitionsToRight
          ( vArray, stopLeft, pivotIndices
          , startRight, stop);
      if ( start < stopLeft ) {
        int[] pivotIndices2  
          = new int[pivotCount];
        for (int i=1; i+2<partitions.length; i+=2) {
          pivotIndices2[i/2] = partitions[i];
        }
        partitions 
          = expandPartitionsToLeft
            ( vArray, start, stopLeft
            , pivotIndices2, stop);
      }
    } else if ( start < stopLeft ) {
      partitions 
        = expandPartitionsToLeft 
          ( vArray, start, stopLeft
          , pivotIndices, stop);
    } else {
      partitions 
        = MultiPivotUtils
          .convertFinalIndicesOfPivotsToPartitions
          (start, stop, pivotIndices);
    }
    return MultiPivotUtils.ensurePartitionCount
           ( partitions
           , desiredPartitionBoundaryCount );
  }
  public int[] expandSinglePivotPartition
    ( int[] vArray, int start, int stopLeft
    , int pivotIndex, int startRight
    , int stop) {
    pivotIndex 
      = singlePivotExpander.expandPartition
        ( vArray, start, stopLeft
        , pivotIndex, startRight, stop);
    return new int[] 
      { start, pivotIndex, pivotIndex+1, stop };
  }
  public abstract int[] expandPartitionsToRight
    ( int[] vArray, int start
     , int[] pivotIndices
    , int startRight, int stop);
  public abstract int[] expandPartitionsToLeft 
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int stop);
  public int getPivotCount() {
    return desiredPivotCount;
  }
}
