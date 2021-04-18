package jsortie.object.quicksort.multiway.expander;

import java.util.Comparator;

import jsortie.object.quicksort.expander.HolierThanThouObjectExpander;
import jsortie.object.quicksort.expander.PartitionObjectExpander;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public abstract class HolierThanThouObjectExpanderBase<T> 
  implements MultiPivotObjectPartitionExpander<T> {
  protected PartitionObjectExpander<T> singlePivotExpander;
  protected int desiredPivotCount;
  protected int desiredPartitionBoundaryCount;
  public HolierThanThouObjectExpanderBase(int p) {
    desiredPivotCount = p;
    desiredPartitionBoundaryCount = (p+1) << 1;
    singlePivotExpander 
      = new HolierThanThouObjectExpander<T>();
  }
  public HolierThanThouObjectExpanderBase
    ( int p, PartitionObjectExpander<T> spx ) {
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
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stopLeft
    , int[] pivotIndices, int startRight, int stop) {
    int pivotCount = pivotIndices.length;
    int[] partitions;
    if ( pivotCount < 2 
         || pivotCount < desiredPivotCount ) {
      int pivotIndex 
        = pivotIndices[pivotIndices.length/2];
      partitions 
        = expandSinglePivotPartition
          ( comparator, vArray
          , start, stopLeft, pivotIndex
          , startRight, stop);
    } else if ( startRight < stop ) {
      partitions 
        = expandPartitionsToRight
          ( comparator, vArray
          , stopLeft, pivotIndices
          , startRight, stop);
      if ( start < stopLeft ) {
        int[] pivotIndices2  
          = new int[pivotCount];
        for (int i=1; i+2<partitions.length; i+=2) {
          pivotIndices2[i/2] = partitions[i];
        }
        partitions 
          = expandPartitionsToLeft
            ( comparator, vArray
            , start, stopLeft
            , pivotIndices2, stop);
      }
    } else if ( start < stopLeft ) {
      partitions 
        = expandPartitionsToLeft 
          ( comparator, vArray, start, stopLeft
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
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stopLeft
    , int pivotIndex, int startRight
    , int stop) {
    pivotIndex 
      = singlePivotExpander.expandPartition
        ( comparator, vArray, start, stopLeft
        , pivotIndex, startRight, stop);
    return new int[] 
      { start, pivotIndex, pivotIndex+1, stop };
  }
  public abstract int[] expandPartitionsToRight
    ( Comparator<? super T> comparator
    , T[] vArray, int start
    , int[] pivotIndices
    , int startRight, int stop);
  public abstract int[] expandPartitionsToLeft 
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stopLeft
    , int[] pivotIndices, int stop);
  public int getPivotCount() {
    return desiredPivotCount;
  }
}
