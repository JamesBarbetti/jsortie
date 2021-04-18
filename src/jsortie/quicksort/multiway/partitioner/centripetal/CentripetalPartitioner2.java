package jsortie.quicksort.multiway.partitioner.centripetal;

import jsortie.quicksort.expander.bidirectional.CentripetalExpander;
import jsortie.quicksort.multiway.expander.centripetal.CentripetalExpander2;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitionerBase;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class CentripetalPartitioner2 
  extends HolierThanThouPartitionerBase {
  public CentripetalPartitioner2() {
    super ( new CentripetalExpander()
          , new CentripetalExpander2());
  }
  @Override
  public int[] multiPartitionRange 
    ( int[] vArray, int start, int stop
    , int[] pivotIndices) {
    if (stop-start<2) {
      return new int[] 
        { start, start+1, start+1, start+1
        , start+1, stop };
    } else if (pivotIndices.length == 1 ) {
      int pivotIndex1     = pivotIndices[0];
      int vPivot          = vArray[pivotIndex1];
      vArray[pivotIndex1] = vArray[start];
      vArray[start]       = vPivot;
      int pivotIndex2 = spx.expandPartition 
                        ( vArray, start, start
                        , start, start+1, stop);
      pivotIndex1
        = spx.expandPartition
          ( vArray, start, pivotIndex2-1
          , pivotIndex2, pivotIndex2+1, pivotIndex2+1);
      int middleIndex 
         = pivotIndex1 + (pivotIndex2-pivotIndex1)/2;
      return new int[] 
        { start, pivotIndex1, middleIndex
        , middleIndex, pivotIndex2+1, stop };
    } else {
      int rightHole  = start + (stop-start+1)/2;
      int leftHole   = rightHole - 1;
      MultiPivotUtils.movePivotsAside 
        ( vArray,  pivotIndices
        , new int[] { leftHole, rightHole} );
      return mpx.expandPartitions
        ( vArray, start, leftHole
        , new int[] { leftHole, rightHole }
        , rightHole+1, stop);
    }
  }
}
