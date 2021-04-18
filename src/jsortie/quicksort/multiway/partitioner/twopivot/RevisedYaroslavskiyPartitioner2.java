package jsortie.quicksort.multiway.partitioner.twopivot;

import jsortie.helper.RangeSortHelper;
import jsortie.quicksort.multiway.partitioner.FixedCountPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class RevisedYaroslavskiyPartitioner2 
  implements FixedCountPivotPartitioner {
  //This is a re-write of the Yaroslavskiy partitioner that has been
  //rigged to avoid some of the boundary-checking done in the original
  //(since we move the low pivot to the far left and the high pivot
  // to the far right, we've got sentinels from the get-go, which
  // helps).
  //
  //Note:
  //1. The left  partition contains only values <vP
  //2. The centre partition contains values >=vP and <=vQ
  //3. The right partition contains values  >=vQ
  //   (i.e. values ==vQ can end up in *either* the centre or
  //    right partitions)
  //4. lhs is one place left of where it was in Yaroslavskiy's code
  //5. rhs is one place right of where it was in Yaroslavskiy's code
  //6. during the search with "scan" for the next element >=vQ,
  //   there will always be one at rhs.
  //	
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int[] multiPartitionRange 
    ( int[] vArray, int start, int stop, int[] pivotIndices)  {
    int desiredPivotCount = getPivotCount();
    if (pivotIndices.length < desiredPivotCount ) {
      return MultiPivotUtils.fakePartitions
        ( vArray, start, stop, pivotIndices, desiredPivotCount );
    }
    MultiPivotUtils.movePivotsAside
      ( vArray,  pivotIndices, new int[] { start, stop-1 } );
    int lhs = start;    //rightmost element in left partition  (that is, <vP)
    int rhs = stop-1;   //leftmost  element on right partition (that is, >=vQ)
    int vP = vArray[lhs];
    int vQ = vArray[rhs];
    
    int scan=start;     //element being checked from left
    int vL;             //element read from the left, from array[scan]
    int vR;             //element read from the right, from array[rhs]
    
    for (;;)
    {
      do
      {
        ++scan;
        while ((vL=vArray[scan])<vP) {
          ++lhs;
          vArray[scan] = vArray[lhs];
          vArray[lhs]  = vL;
          ++scan;
        }
      }
      while (vL<vQ);
      for (--rhs;vQ<(vR=vArray[rhs]);--rhs) {}
      if (rhs<scan) { 
        rhs=scan;
        break;
      }
      vArray[rhs]  = vL;
      vArray[scan] = vR;
      if (vR < vP) {
        ++lhs;
        vArray[scan] = vArray[lhs];
        vArray[lhs]  = vR;
      }
    }
    
    RangeSortHelper.swapElements(vArray, start, lhs);
    RangeSortHelper.swapElements(vArray, rhs,   stop-1);
    
    return new int[] { start, lhs, lhs+1, rhs, rhs+1, stop }; 
    
  }

  @Override
  public int getPivotCount() 
  {
    return 2;
  }
}
