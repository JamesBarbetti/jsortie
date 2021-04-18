package jsortie.quicksort.partitioner.bidirectional.centrepivot;

import jsortie.quicksort.partitioner.bidirectional.traditional.HoyosPartitioner;

public class CentrePivotPartitioner 
  extends HoyosPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int partitionRange
    ( int vArray[], int start, int stop, int pivotIndex ) {
    int vPivot = vArray[pivotIndex];
    int lhs    = start-1;
    int rhs    = stop;
    do { ++lhs; } while ( vArray[lhs] < vPivot);
    if (lhs<pivotIndex) {
      int vLifted = vArray[lhs];
      do { --rhs; } while ( vPivot < vArray[rhs] );
      if (pivotIndex<rhs) {
        do {
          vArray[lhs] = vArray[rhs];
          do { ++lhs; } while ( vArray[lhs] < vPivot );
          if (lhs==pivotIndex) {
            vArray[rhs] = vLifted;
            return partitionRangeFromLeft(vArray, lhs, rhs);
          }
          vArray[rhs] = vArray[lhs];
          do { --rhs; } while ( vPivot < vArray[rhs] );
        } while (pivotIndex<rhs);
      }
      vArray[lhs]=vPivot;
      vArray[rhs]=vLifted;
    }
    return partitionRangeFromLeft(vArray, lhs, rhs);
  }
}
