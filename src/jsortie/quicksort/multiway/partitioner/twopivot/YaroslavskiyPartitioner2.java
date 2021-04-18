package jsortie.quicksort.multiway.partitioner.twopivot;

import jsortie.quicksort.multiway.partitioner.FixedCountPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class YaroslavskiyPartitioner2
  implements FixedCountPivotPartitioner {	
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }	
  @Override
  public int[] multiPartitionRange ( int[] vArray
                              , int start, int stop
                              , int[] pivotIndices) {
    if (pivotIndices.length < 2 ) {
      return MultiPivotUtils.fakePartitions
        ( vArray, start, stop, pivotIndices, 2 );
    }
    MultiPivotUtils.movePivotsAside 
      ( vArray,  pivotIndices , new int[] { start, stop-1 } );
    int vP = vArray[start];
    int vQ = vArray[stop-1];	
    int lhs = start + 1; //first element that might be >= vP
    int rhs = stop  - 1; //last large item that is known to 
                         //be > vQ (if any found)
                         //OR the location of vQ 
                         //(if no large item yet found)
    int scan= lhs;
    if (scan<rhs) {
      do {
        int v = vArray[scan];
        if (v < vP)  {
          vArray[scan] = vArray[lhs];
          vArray[lhs]  = v;
          ++lhs;
        } else if ( vQ < v ) {
          int vRight;
          do {
            --rhs;
            vRight = vArray[rhs];
          } while ( scan<rhs && vQ < vRight );
          if ( vRight < vP ) {
            vArray[scan] = vArray[lhs];
            vArray[lhs]  = vRight;
            ++lhs;
          } else {
            vArray[scan] = vRight;
          }
          vArray[rhs] = v;
        }
        ++scan;
      } while (scan<rhs);
    } 
    //place vP 
    --lhs;
    vArray[start] = vArray[lhs];
    vArray[lhs]   = vP;
    //place vQ
    vArray[stop-1] = vArray[rhs];
    vArray[rhs]    = vQ;
    return new int[] { start, lhs
                     , (vP<vQ) ? (lhs+1) : rhs, rhs
                     , rhs+1, stop }; 
  }
  @Override
  public int getPivotCount() {
    return 2;
  }
}