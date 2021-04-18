package jsortie.quicksort.multiway.partitioner.pretend;

import jsortie.helper.ShiftHelper;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class MultiplexingPartitioner 
  implements MultiPivotPartitioner {
  protected SinglePivotPartitioner left;
  protected SinglePivotPartitioner right;
  protected ShiftHelper            shifter;
  public MultiplexingPartitioner 
    ( SinglePivotPartitioner leftPartitioner
    , SinglePivotPartitioner rightPartitioner ) {
    left = leftPartitioner;
    right = rightPartitioner;
    shifter = new ShiftHelper();
  }
  public String toString() {
    String leftName = left.toString();
    String rightName = right.toString();
    if (leftName.equals(rightName)) {
      return this.getClass().getSimpleName() 
        + "(" + leftName + ")";
    }
    return this.getClass().getSimpleName() 
      + "(" + leftName + "," + rightName + ")";
  }  
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices) {
    shifter.shiftSubsetToBack
      ( vArray, start, stop, pivotIndices );
    int boundCount = pivotIndices.length*2 + 2;
    int[] boundaries
      = new int[boundCount];
    boundaries[0] = start;
    boundaries[boundaries.length-1] = stop;
    partitionSubsetWith ( vArray, start, stop
                    , pivotIndices.length, true
                    , left, boundaries, 1);
    return boundaries;
  }
  protected void partitionSubsetWith
    ( int[] vArray, int start, int stop
    , int pivotCount, boolean arePivotsOnRight
    , SinglePivotPartitioner party
    , int[] boundaries, int firstBoundary) {
    if (pivotCount==0) {
      return;
    }
    int leftPivotCount  = pivotCount/2; 
    int rightPivotCount 
      = pivotCount - 1 - leftPivotCount;
    int pivotIndex;
    if ( arePivotsOnRight ) {
      pivotIndex = stop-rightPivotCount-1;
      shifter.moveBackElementsToFront
        ( vArray, start
        , start+leftPivotCount, pivotIndex);
      pivotIndex 
        = party.partitionRange
          ( vArray, start+leftPivotCount
          , pivotIndex+1, pivotIndex);
    } else {
      pivotIndex = start+leftPivotCount;
      shifter.moveFrontElementsToBack
        ( vArray, pivotIndex+1
        , pivotIndex+1+rightPivotCount, stop);
      pivotIndex 
        = party.partitionRange
          ( vArray, pivotIndex
          , stop-rightPivotCount, pivotIndex);
    }
    int x = firstBoundary + leftPivotCount*2;
    boundaries[x]   = pivotIndex;
    boundaries[x+1] = pivotIndex+1;
    partitionSubsetWith
      ( vArray, pivotIndex+1, stop
      , rightPivotCount, true, right
      , boundaries, x+2 );
    partitionSubsetWith
      ( vArray, start, pivotIndex
      , leftPivotCount, false, left
      , boundaries, firstBoundary);
  }
}
