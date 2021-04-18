package jsortie.quicksort.multiway.partitioner.singlepivot;

public class AnimalFarmPartitioner 
  implements TernarySinglePivotPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int getPivotCount() {
    return 1;
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices) {
    int pivotIndex     = pivotIndices[ pivotIndices.length / 2 ];
    int vPivot         = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[stop-1];
    vArray[stop-1]     = vPivot;
    int lhs            = start;  //first unknown
    int rhs            = stop-2; //last  unknown
    while (vArray[lhs]<vPivot && lhs<rhs) {
      ++lhs;
    }
    while (vPivot<vArray[rhs] && lhs<rhs) {
      --rhs;
    }
    //System.out.println("zoomed start=" + start + ", lhs=" 
    // + lhs + ",rhs=" + rhs + ",stop=" + stop + ",pivot=" + vPivot);
    
    for (int scan=lhs; scan<=rhs; ) {
      if ( vArray[scan] != vPivot ) {
        int     v   = vArray [ scan ];
        boolean low = v < vPivot;
        vArray[ scan            ] = vArray [ low ? lhs : rhs ];
        vArray[ low ? lhs : rhs ] = v;
        scan                     += low ? 1 : 0;
        lhs                      += low ? 1 : 0;
        rhs                      -= low ? 0 : 1;
      } else {
        ++scan;
      }
    }
    vArray [ stop - 1 ] = vArray [ rhs + 1 ];
    vArray [ rhs  + 1 ] = vPivot;
    //System.out.println("start=" + start + ", lhs=" 
    // + lhs + ",rhs=" + rhs 
    // + ",stop=" + stop + ",pivot=" + vPivot);
    return new int[] { start, lhs, rhs+2, stop };
  }
}
