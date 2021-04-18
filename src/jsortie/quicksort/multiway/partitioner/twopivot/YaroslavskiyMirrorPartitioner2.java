package jsortie.quicksort.multiway.partitioner.twopivot;

import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class YaroslavskiyMirrorPartitioner2 
  extends YaroslavskiyPartitioner2 {
  public int[] mirrorPartitionRange
    ( int[] vArray, int start, int stop, int[] pivotIndices) {
    MultiPivotUtils.movePivotsAside
    (vArray,  pivotIndices, new int[] { start, stop-1 } );
	int vP = vArray[start];
	int vQ = vArray[stop-1];
	int lhs = start + 1; //first element that might be >= vP
	int rhs = stop  - 2; //last element that might be <= vQ
	int scan = rhs;
	while ( lhs <= scan ) {
	  int v = vArray[scan];
      if (vQ < v) {
        vArray[scan] = vArray[rhs];
        vArray[rhs]  = v;
        --rhs;
      } else {	
        if (v < vP ) {
          int v2 = vArray[lhs];
          while (v2 < vP && lhs<scan) {
            ++lhs;
            v2 = vArray[lhs];
          }
          if (vQ < vArray[scan]) {
        	vArray[scan] = vArray[rhs];
        	vArray[rhs]  = v2;
            --rhs;
          } else {
            vArray[scan] = v2;        	  
          }
          vArray[lhs]  = v;
          ++lhs;
        }
      }
      --scan;
	}
	--lhs;
	vArray[start] = vArray[lhs];
	vArray[lhs] = vP;
	++rhs;
	vArray[stop-1] = vArray[rhs];
	vArray[rhs] = vQ;
	if (vP<vQ)
		return new int[] { start, lhs, lhs+1, rhs, rhs+1, stop }; 
	else
		return new int[] { start, lhs, rhs,   rhs, rhs+1, stop };
  }
}
