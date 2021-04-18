package jsortie.object.indexed;

public class IndexCentripetalPartitioner
  implements IndexPartitioner  {
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int[] partitionIndexRange
    ( IndexComparator comparator, int start, int stop
    , int[] iiPivots) {
    int hole           = start + (stop - start) / 2;
    int pivotIndex     = iiPivots[iiPivots.length/2];
    comparator.swapIndices(pivotIndex, hole);
    hole = expandExistingPartition
           ( comparator, start, hole-1
           , hole, hole+1, stop );
    return new int[] { start, hole, hole+1, stop };
  }
  protected int expandExistingPartition
    ( IndexComparator comparator
    , int start, int stopLeft
    , int hole, int startRight, int stop) {
    if (stopLeft-start < stop-startRight) {
      int skip   = (stop-startRight) - (stopLeft-start);
      hole       = expandPartitionToRight 
                   ( comparator, hole
                   , startRight, startRight+skip);
      startRight += skip;
    }
    int[] iArray = comparator.getIndexArray();
    int   iPivot = iArray[hole];
    int   scanLeft = stopLeft-1;
    for ( int scanRight = startRight; 
          scanRight < stop; 
          --scanLeft, ++scanRight ) {
      int iLeft  = iArray[scanLeft];
      int iRight = iArray[scanRight];
      if ( comparator.compareItems(iLeft,iPivot) <= 0 ) {
        if ( comparator.compareItems(iRight,iPivot) < 0 ) {
          iArray[hole] = iRight;
          ++hole;
          iArray[scanRight] = iArray[hole];
        } else {
          //vLeft <= vPivot <= vRight, nothing to do
        }
      } else if ( comparator.compareItems
                  ( iRight, iPivot) <= 0 ) {
        iArray[scanLeft]  = iRight;
        iArray[scanRight] = iLeft;
      } else {
        iArray[hole] = iLeft;
        --hole;
        iArray[scanLeft] = iArray[hole];
      }
    }
    iArray[hole] = iPivot;
    hole = expandPartitionToLeft
           ( comparator, start, scanLeft+1, hole);
    return hole;
  }
  protected int expandPartitionToRight
    ( IndexComparator comparator, int hole
    , int scanRight, int stop ) {
    int[] iArray = comparator.getIndexArray();
    int   iPivot = iArray[hole];
    for ( ; scanRight < stop ; ++scanRight ) {
      int iRight = iArray[scanRight];
      if ( comparator.compareItems(iRight, iPivot) < 0 ) {
        iArray[hole] = iRight;
        ++hole;
        iArray[scanRight]=iArray[hole];
      }
    }
    iArray[hole] = iPivot;
    return hole;
  }
  protected int expandPartitionToLeft
    ( IndexComparator comparator, int start
    , int stopLeft, int hole ) {
    int[] iArray = comparator.getIndexArray();
    int   iPivot = iArray[hole];
    for ( int scanLeft=stopLeft-1; 
          start <= scanLeft; 
          --scanLeft ) {
      int iLeft = iArray[scanLeft];
      if ( comparator.compareItems(iPivot, iLeft) < 0 ) {
        iArray[hole]=iLeft;
        --hole;
        iArray[scanLeft]=iArray[hole];
      }
    }
    iArray[hole] = iPivot;
    return hole;
  }
}
