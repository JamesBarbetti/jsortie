package jsortie.quicksort.multiway.partitioner.singlepivot;

import jsortie.helper.EgalitarianPartitionerHelper;
import jsortie.quicksort.partitioner.branchavoiding.SkippyPartitioner;

public class SlowCookedDutchPartitioner
  implements TernarySinglePivotPartitioner {
  protected SkippyPartitioner part1
    = new SkippyPartitioner();
  protected EgalitarianPartitionerHelper part2
    = new EgalitarianPartitionerHelper();
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices) {
    int pivotIndex = pivotIndices[pivotIndices.length/2];
    return partitionRangeWithOnePivot
           ( vArray, start, stop, pivotIndex );
  }
  public int[] partitionRangeWithOnePivot
    ( int[] vArray, int start
    , int stop, int   pivotIndex) {
    //Note: This "knows" that SkippyPartitioner 
    //      always moves all the items that are ==
    //      to the pivot, into the left partition.
    int boundary2 
      = part1.partitionRange
        ( vArray, start, stop, pivotIndex );
    int boundary1  
      = part2.swapEqualToRight
        ( vArray, start, boundary2, vArray[boundary2] );
    return new int[] 
      { start, boundary1, boundary2+1, stop };
  }
  @Override
  public int getPivotCount() {
    return 1;
  }
}
