package jsortie.quicksort.governor.adaptive;

import jsortie.RangeSorter;
import jsortie.earlyexitdetector.RangeSortEarlyExitDetector;
import jsortie.quicksort.governor.QuicksortBase;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;

public class QuicksortAdaptive extends QuicksortBase
{
  public    RangeSorter                lastResort;
  protected RangeSortEarlyExitDetector detector;
  public QuicksortAdaptive
    ( SinglePivotSelector selector
    , SinglePivotPartitioner partitioner
    , RangeSorter rangeSorter, int janitorThreshold
    , RangeSorter sortOfLastResort
    , RangeSortEarlyExitDetector detectorToUse) {
    super ( selector, partitioner
          , rangeSorter, janitorThreshold);
    this.lastResort  = sortOfLastResort;
    this.detector    = detectorToUse;
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName()
      + "(" + selector.toString() 
      + ", " + partitioner.toString()
      + ", " + janitor.toString()
      + ", " + lastResort.toString() 
      + ", " + detector.toString() + ")";
  }
  protected SinglePivotSelector chooseSelector
    ( int start, int stop, int maxDepth ) {
    return this.selector;
  }
  protected SinglePivotPartitioner choosePartitioner
    ( int start, int stop, int maxDepth ) {
    return this.partitioner;
  }
  protected RangeSorter chooseJanitor
    ( int start, int stop ) {
    return this.janitor;
  }
  protected void sortRangeAdaptive
    ( int[] vArray, int leftBoundary
    , int start, int stop
    , int rightBoundary, int maxDepth) {
    while ( janitorThreshold < stop-start
            && 0<maxDepth) {
      --maxDepth;
      if (detector.exitEarlyIfSorted
          (vArray, start, stop)) {
        return;
      }
      int selectedPivotIndex 
        = chooseSelector    ( start, stop,  maxDepth )
        . selectPivotIndex  ( vArray, start, stop    );
      int pivotIndex 
        = choosePartitioner ( start, stop,  maxDepth )
        . partitionRange    ( vArray, start, stop
                            , selectedPivotIndex     );
      int vPivot = vArray[pivotIndex];
      //Note: the pivot can't compare equal to the
      //      previous pivots on *both* sides, because
      //      when the second of those previous pivots
      //      was used, that would have identified a
      //      large rage (containing the current range)
      //      as consisting entirely of items equal 
      //      to (both of) those two previous pivots.  
      //      But it could compare equal to a previous r
      //      pivot on one side or the other.
      
      if ( leftBoundary!=-1 
           && vArray[leftBoundary]== vPivot ) { 
        //All items in left child partition compare
        //equal to the pivot.
        start = pivotIndex+1;
        leftBoundary = pivotIndex;
        continue;
      }
      if ( rightBoundary!=-1
           && vPivot == vArray[rightBoundary] ) {
        //All items in right child partition compare
        //equal to the pivot.
        stop = pivotIndex;
        rightBoundary = pivotIndex;
        continue;
      }
      //recursively sort the smaller partition
      if ( pivotIndex - start 
           < stop - pivotIndex - 1 ) {
        //if the left partition is smaller
        //...after skipping over any items
        // at the extreme right of the 
        // left partition that are == vPivot...
        int scan=pivotIndex-1;
        for (; scan>=start; --scan) {
          if ( vArray[scan] < vPivot ) break;
        }
        //...sort the left partition recursively
        if ( start<scan ) { 
          sortRangeAdaptive 
          ( vArray, leftBoundary, start, scan + 1
          , scan+1, maxDepth - 1);
        }
        //...then skip over any items at the extreme
        //left of the right partition that are ==vPivot.
        leftBoundary = pivotIndex;
        start = pivotIndex + 1;
        for (; start<stop; ++start) {
          if ( vPivot < vArray[start] ) break;
        }
      } else { //the right partition is smaller
        //...after skipping over any items at 
        //   the extreme left of the right 
        //   partition that are == vPivot...
        int scan=pivotIndex+1;
        for (; scan<stop; ++scan) {
          if ( vPivot < vArray[scan] ) break;
        }
        if (scan+1<stop) { 
          //sort the right partition
          sortRangeAdaptive 
          ( vArray, pivotIndex, pivotIndex+1, stop
          , rightBoundary, maxDepth-1);
        }
        //...then skip over any items at the 
        //   extreme right of the left partition
        //   that are ==vPivot.
        rightBoundary = pivotIndex;
        stop = pivotIndex-1;
        for (; start<=stop; --stop) {
          if ( vArray[stop] < vPivot ) break;
        }
        ++stop;
      }
    }
    if (start+1<stop)  {
      if (0==maxDepth) {
        //avoiding quadratic worst case
        lastResort.sortRange
        ( vArray, start, stop );
      } else {
        chooseJanitor ( start, stop)
        . sortRange   ( vArray, start, stop);
      }
    }
  }
  @Override
  public void sortRange
    (int[] vArray, int start, int stop) {
    if (start+1<stop)  {
      //asking for log(0) would result in 
      //an exception, and log(1) is zero
      double d = Math.log(stop-start)*3/Math.log(2); 
      int maxDepth = (int) Math.floor (d  + 1.0);
      sortRangeAdaptive ( vArray, -1, start
                        , stop, -1, maxDepth );
    }
  }
}
