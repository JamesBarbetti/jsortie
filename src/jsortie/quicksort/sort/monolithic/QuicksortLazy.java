package jsortie.quicksort.sort.monolithic;

import jsortie.RangeSorter;
import jsortie.helper.EgalitarianPartitionerHelper;
import jsortie.janitors.insertion.InsertionSort;
import jsortie.quicksort.governor.QuicksortBase;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.simple.MiddleElementSelector;

public class QuicksortLazy extends QuicksortBase
{
  //Purpose: Fast sorting of arrays consisting 
  //         mostly of equal-valued items, 
  //         without paying too high a price 
  //         (typically, 25%) for other inputs.
  //
  protected static EgalitarianPartitionerHelper helper 
    = new EgalitarianPartitionerHelper();
  public QuicksortLazy
    ( SinglePivotSelector selector
    , SinglePivotPartitioner partitioner
    , RangeSorter rangeSorter,
    int janitorThresholdToUse) {
    super ( selector, partitioner
          , rangeSorter, janitorThresholdToUse );
  }
  public QuicksortLazy() {
    super ( new MiddleElementSelector()
          , null, new InsertionSort(), 5 );
  }
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    while ( this.janitorThreshold < stop-start ) {
      int pivotIndex = selector.selectPivotIndex
                       ( vArray, start, stop );
      int vPivot = vArray[pivotIndex];
      int lhs;
      int rhs;
      for (lhs = start; lhs < stop; ++lhs) {
        if (vPivot<vArray[lhs]) {
          break;
        }
      }
      for (rhs = stop-1; start <= rhs; --rhs) {
        if (vArray[rhs]<vPivot) {
          break;
        }
      }
      if (lhs==stop) {
        if (rhs<start) { //All elements are == vPivot
          return;
        } else { //All elements are <= vPivot
          stop = helper.swapEqualToRight
                 ( vArray, start, stop, vPivot);
        }
      }
      else if (rhs<start) {
        start = helper.swapEqualToLeft
                ( vArray, start, stop, vPivot) + 1;
      } else {
        while (lhs<rhs) {
          int vTemp = vArray[lhs];
          vArray[lhs]=vArray[rhs];
          vArray[rhs]=vTemp;
          do { ++lhs; } while ( vArray[lhs]<=vPivot);
          do { --rhs; } while ( vPivot<=vArray[rhs]);
        }
        //Everything in the range [rhs+1,lhs-1] is == pivot
        if ( pivotIndex < rhs) {
          vArray[pivotIndex]=vArray[rhs];
          vArray[rhs] = vPivot;
          --rhs;
        } else if ( lhs < pivotIndex ) {
          vArray[pivotIndex]=vArray[lhs];
          vArray[lhs] = vPivot;
          ++lhs;
        }
        if (rhs-start < stop-lhs) {
          sortRange(vArray, start, rhs+1);
          start = lhs;
        } else {
          sortRange(vArray, lhs, stop);
          stop = rhs + 1;
        }
      }
    }
    this.janitor.sortRange(vArray, start, stop);
  }
  public void sortArray(int [] vArray) {
    sortRange(vArray, 0, vArray.length);
  }
  public static void sort(int [] vArray) {
    ( new QuicksortLazy()).sortRange(vArray,  0,  vArray.length );
  }
}
