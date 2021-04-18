package jsortie.quicksort.multiway.partitioner.twopivot;

import jsortie.quicksort.expander.unidirectional.LomutoExpander;
import jsortie.quicksort.multiway.partitioner.FixedCountPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class DoubleLomutoPartitioner2 
  implements FixedCountPivotPartitioner {
  LomutoExpander expander = new LomutoExpander();
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }   
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices) {
    if (pivotIndices.length==1) {
      return 
        MultiPivotUtils.partitionRangeWithOnePivot 
        ( vArray, start, stop
        , pivotIndices[0], expander);
    }
    MultiPivotUtils.movePivotsAside 
      ( vArray, pivotIndices
      , new int[]{start,stop-1});
    int vP = vArray[start];
    int vQ = vArray[stop-1];
    int lhs = start + 1; //first elt that might be >= vP
    int rhs = stop  - 2; //last let that might be <= vQ
    //initial sweeps (help a little for pre-sorted input)
    while ( vArray[lhs] < vP ) {
      ++lhs;
    }
    while ( vQ < vArray[rhs] ) {
      --rhs;
    }
    //main loop
    int scan=lhs;
    while ( scan<=rhs ) {
      int v = vArray[scan];
      if ( vQ < v ) {
        vArray[scan] = vArray[rhs];
        vArray[rhs]  = v;
        --rhs;
      } else {
        if ( v < vP ) {
          vArray[scan] = vArray[lhs];
          vArray[lhs]  = v;
          ++lhs;
        }
        ++scan;
      }
    }
    //place vP
    --lhs;
    vArray[start] = vArray[lhs];
    vArray[lhs] = vP;
    //place vQ
    ++rhs;
    vArray[stop-1] = vArray[rhs];
    vArray[rhs] = vQ;
    //If vP==vQ, nothing needs to be done 
    //in the middle partition
    //(all the items in it compare equal)
    if (vP<vQ) {
      return new int[] 
        { start, lhs, lhs+1, rhs, rhs+1, stop };
    } else {
      return new int[] 
       { start, lhs, rhs+1, stop };
    }
  }
  @Override
  public int getPivotCount() {
    return 2;
  }
}
