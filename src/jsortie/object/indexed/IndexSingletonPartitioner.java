package jsortie.object.indexed;

public class IndexSingletonPartitioner
  implements IndexPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int[] partitionIndexRange
    (IndexComparator comparator, int start, int stop, int[] iiPivots) {
    int[] iArray       = comparator.getIndexArray();
    int pivotIndex     = iiPivots[iiPivots.length/2];
    int iPivot         = iArray[pivotIndex];
    iArray[pivotIndex] = iArray[start];
    int lhs            = start;
    int rhs            = stop;
    //Find last value, v, from right, such that v <= vPivot
    do { 
      --rhs; 
    } while ( lhs < rhs && comparator.comparePivotToIndex(iPivot, rhs ) <0); 
    //Find first value, v, from left, such that vPivot <= v
    do {
      ++lhs; 
    } while ( lhs < rhs && comparator.compareIndexWithPivot( lhs, iPivot) < 0 );
    if ( lhs < rhs ) {    	
      do {
        int iTemp   = iArray[lhs];
        iArray[lhs] = iArray[rhs];
        iArray[rhs] = iTemp;
        do { 
          --rhs;
        } while ( comparator.comparePivotToIndex( iPivot, rhs ) < 0);
        if (rhs <= lhs) {
          break;
        }
        do {
          ++lhs;
        } while ( comparator.compareIndexWithPivot( lhs , iPivot ) < 0);
      } while ( lhs < rhs );
    }
    iArray[start] = iArray[rhs]; 
    iArray[rhs]   = iPivot; 
    return new int[] { start, rhs, rhs+1, stop };
  }
}
