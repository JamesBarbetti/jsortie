package jsortie.quicksort.multiway.partitioner.pretend;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class FancierMultiplexingPartitioner 
  extends MultiplexingPartitioner {
  public FancierMultiplexingPartitioner
    ( SinglePivotPartitioner leftPartitioner,
      SinglePivotPartitioner rightPartitioner) {
    super(leftPartitioner, rightPartitioner);
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices) {
    //Assumes, but does *not* check, 
    //that 0<pivotCount.
    int pivotCount = pivotIndices.length;
    int[] pivotIndexCopy = new int[pivotCount];
    boolean ternary = false;
    pivotIndexCopy[0] = pivotIndices[0];
    int distinctPivotCount = 1;
    for (int i=1; i<pivotCount; ++i) {
      int pivotIndex = pivotIndices[i]; 
      if ( vArray[pivotIndices[i-1]] 
         < vArray[pivotIndex] ) {
        pivotIndexCopy[distinctPivotCount] 
          = pivotIndex;
        ++distinctPivotCount;
      } else {
        ternary = true;
      }
    }
    shifter.shiftSubsetToBack
      ( vArray, start, stop
      , pivotIndexCopy, distinctPivotCount );
    int boundCount = distinctPivotCount*2 + 2;
    int[] boundaries
      = new int[boundCount];
    if (ternary) {
      ternaryPartition
        ( vArray, start, stop
        , boundaries, 0, distinctPivotCount
        , true);
    } else {
      //As in the superclass
      boundaries[0] = start;
      boundaries[boundCount-1] = stop;
      partitionSubsetWith 
        ( vArray, start, stop
        , distinctPivotCount, true
        , left, boundaries, 1);
    }
    return boundaries;
  }
  protected void ternaryPartition
    ( int[] vArray, int start, int stop
    , int[] boundaries, int pStart, int pStop
    , boolean arePivotsOnRight) {
    do {
      int pivotCount = pStop - pStart;
      int leftPivotCount  = pivotCount/2; 
      int rightPivotCount 
        = pivotCount - 1 - leftPivotCount;
      int pivotIndex;
      if ( arePivotsOnRight ) {
        pivotIndex = stop - rightPivotCount - 1;
        shifter.moveBackElementsToFront
          ( vArray, start
          , stop-pivotCount, pivotIndex);
      } else {
        pivotIndex = start + leftPivotCount;
        shifter.moveFrontElementsToBack
          ( vArray, pivotIndex+1
          , start+pivotCount, stop);
      }
      //Now, first leftPivotCount 
      //items are pivots, last rightPivotCount 
      //items are pivots, and the pivot
      //to use NOW is at vArray[pivotIndex].
      int p = pStart + leftPivotCount;
      int b = p + p;
      boundaries[b+1] = pivotIndex
        = left.partitionRange
          ( vArray, start + leftPivotCount
          , stop - rightPivotCount, pivotIndex);
      boundaries[b+2] 
        = right.partitionRange
         ( vArray, pivotIndex
         , stop-rightPivotCount, pivotIndex);
      ternaryPartition
        ( vArray, start, pivotIndex 
        , boundaries, pStart, p, false);
      start  = boundaries[b+2];
      pStart = p + 1;
    } while (pStart<pStop); 
  }
}
