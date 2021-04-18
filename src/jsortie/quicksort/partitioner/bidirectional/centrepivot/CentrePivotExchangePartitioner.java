package jsortie.quicksort.partitioner.bidirectional.centrepivot;

import jsortie.helper.RangeSortHelper;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class CentrePivotExchangePartitioner
  implements SinglePivotPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName(); 
  }
  protected static int partitionRangeFromRight
    ( int[] vArray, int start, int stop ) {
    int rhs = stop - 1;
    int lhs = start - 1;
    int v = vArray[rhs];
    do { 
      ++lhs; 
    } while (vArray[lhs] < v);
    if (lhs < rhs) {	
      do { 
        --rhs; 
      } while (lhs<rhs && v < vArray[rhs]);
      while (lhs<rhs) {
        RangeSortHelper.swapElements
          ( vArray, lhs, rhs );
        do { 
          ++lhs; 
        } while (vArray[lhs] < v);
        do {
          --rhs; 
        } while (v < vArray[rhs]);
      }
      vArray[stop - 1] = vArray[lhs];
      vArray[lhs] = v;
    }
    return lhs;
  }
  protected static int partitionRangeFromLeft
    ( int[] vArray, int start, int stop ) {
    int lhs = start;
    int rhs = stop;
    int v = vArray[lhs];
    do {
      --rhs; 
    } while (v < vArray[rhs]);
    if (lhs<rhs) {
      do { ++lhs; } while (lhs<rhs && vArray[lhs]<v);
      while (lhs<rhs) {
        RangeSortHelper.swapElements(vArray, lhs, rhs);
        do {
          ++lhs; } while (vArray[lhs] < v);
        do {
          --rhs; } while (v < vArray[rhs]);
      }
      vArray[start] = vArray[rhs];
      vArray[rhs] = v;
    }
    return rhs;
  }
  @Override
  public int partitionRange
    ( int[] vArray, int start, int stop, int pivotIndex ) {
    int lhs = start - 1;
    int rhs = stop;
    int v = vArray[pivotIndex];
    for (;;) {
      do {
        ++lhs;
      } while (vArray[lhs] < v);
      do {
        --rhs;
      } while (v < vArray[rhs]);
      if (lhs == pivotIndex) {
        if (pivotIndex < rhs) {
          vArray[lhs] = vArray[rhs];
          vArray[rhs] = v;
          pivotIndex = partitionRangeFromRight
                       ( vArray, lhs + 1, rhs + 1 );
        }
        break;
      } else if (rhs == pivotIndex) {
        vArray[rhs] = vArray[lhs];
        vArray[lhs] = v;
        pivotIndex = partitionRangeFromLeft
                     ( vArray, lhs, rhs );
        break;
      }
      int tmp = vArray[lhs];
      vArray[lhs] = vArray[rhs];
      vArray[rhs] = tmp;
    }
    return pivotIndex;
  }
}
