package jsortie.quicksort.multiway.partitioner.decorator;

import jsortie.helper.FancierEgalitarianPartitionerHelper;
import jsortie.helper.ShiftHelper;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class FlyingPigPartitioner
  implements MultiPivotPartitioner {
  FancierEgalitarianPartitionerHelper helper 
    = new FancierEgalitarianPartitionerHelper();
  ShiftHelper shifter
    = new ShiftHelper();
  protected SinglePivotPartitioner partitioner;
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + partitioner.toString() + ")";
  }
  public FlyingPigPartitioner
    ( SinglePivotPartitioner onePivotPartitioner ) {
    this.partitioner = onePivotPartitioner; 
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray
    , int start, int stop
    , int[] pivotIndices) {
    int pivotIndex 
      = pivotIndices[(pivotIndices.length+1)/2];
    int vPivot = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    int firstUnequal 
      = helper.swapEqualToLeft
        ( vArray, start+1, stop, vPivot);
    int equal = firstUnequal - start;
    int lastLessOrEqual;
    if (equal==1) {
      lastLessOrEqual
        = partitioner.partitionRange
          ( vArray, start, stop, start);
    } else {
      int lastEqual = firstUnequal-1;
      vArray[start] = vArray[lastEqual];
      vArray[lastEqual] = vPivot;
      lastLessOrEqual
        = partitioner.partitionRange
          ( vArray, lastEqual, stop, lastEqual);
      shifter.moveFrontElementsToBack
        ( vArray, start, lastEqual, lastLessOrEqual);
    }
    return new int[]
      { start, lastLessOrEqual+1-equal
      , lastLessOrEqual+1, stop };
  }
}
