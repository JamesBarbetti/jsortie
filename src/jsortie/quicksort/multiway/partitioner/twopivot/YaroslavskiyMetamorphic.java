package jsortie.quicksort.multiway.partitioner.twopivot;

import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

/**
 * This class samples some items in the array, to decide whether to use the
 * Yaroslavskiy partitioner, or its "left/right reflection".
 * 
 * Note that, sometimes, the Double Lomuto partitioner is actually better,
 * if both of the pivots are... crap (and the low pivot is very low, and the high
 * pivot is very high).  But that's pretty unlikely!
 * 
 * @author james
 * 
 */
public class YaroslavskiyMetamorphic 
  extends YaroslavskiyPartitioner2 {
  YaroslavskiyMirrorPartitioner2 mirror 
    = new YaroslavskiyMirrorPartitioner2();
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    if ( MultiPivotUtils.chooseIndexOfBestPivotIndex
         ( vArray, start, stop, pivotIndices) == 1 ) {
      return mirror.multiPartitionRange
             ( vArray, start, stop, pivotIndices );
    }
    else {
      return super.multiPartitionRange
             ( vArray, start, stop, pivotIndices );
	}
  }
}
