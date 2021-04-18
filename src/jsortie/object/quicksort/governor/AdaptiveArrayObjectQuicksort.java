package jsortie.object.quicksort.governor;

import java.util.Comparator;

import jsortie.object.sortearlyexitdetector.ObjectSortEarlyExitDetector;
import jsortie.object.sortearlyexitdetector.WainwrightDetector;

public class AdaptiveArrayObjectQuicksort<T> 
  extends ArrayObjectQuicksort<T> {
  ObjectSortEarlyExitDetector<T> detector
    = new WainwrightDetector<T>();
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    if (stop - start < threshold) {
      janitor.sortRange
        ( comparator, vArray, start, stop );
    } else {
      int maxPartitionDepth = 
        1 + (int) Math.floor( Math.log(stop - start) 
                              / Math.log(2) * 2.0);
      sortSubRange 
        ( comparator, vArray
        , -1, start, stop, -1
        , maxPartitionDepth);
    }
  }
  private void sortSubRange
    ( Comparator<? super T> comparator
    , T[] vArray, int leftBoundary
    , int start, int stop
    , int rightBoundary
    , int maxPartitionDepth) {
    while ( threshold < stop - start 
            && 0 < maxPartitionDepth) {
      if ( detector.exitEarlyIfSorted
           ( comparator, vArray, start, stop) ) {
        return;
      }
      --maxPartitionDepth;
      int pivotIndex = 
        selector.selectPivotIndex
        ( comparator, vArray, start, stop );
      pivotIndex = 
        getPartitioner(maxPartitionDepth)
        .partitionRange
        ( comparator, vArray
        , start, stop, pivotIndex);
      T vPivot = vArray[pivotIndex];
      if ( pivotIndex - start <= stop - pivotIndex ) {
        if ( leftBoundary == -1 || 
             comparator.compare
               ( vArray[leftBoundary], vPivot ) < 0 ) {
          sortSubRange
            ( comparator, vArray
            , leftBoundary, start
            , pivotIndex, pivotIndex
            , maxPartitionDepth);
        }
        if ( rightBoundary != -1 && 
             comparator.compare
             ( vPivot, vArray[rightBoundary] ) == 0) {
          return;
        }
        start = pivotIndex + 1;
        leftBoundary = pivotIndex;
      } else {
        if ( rightBoundary == -1 || 
             comparator.compare
             ( vPivot, vArray[rightBoundary] ) < 0) {
          sortSubRange 
            ( comparator, vArray, pivotIndex
            , pivotIndex + 1, stop
            , rightBoundary, maxPartitionDepth);
        }
        if ( leftBoundary != -1 && 
             comparator.compare
             ( vArray[leftBoundary], vPivot ) == 0 ) {
          return;
        }
        stop = pivotIndex;
        rightBoundary = pivotIndex;
      }
    }
    if (start < stop) {
      if (0 < maxPartitionDepth) {
        janitor.sortRange
          ( comparator, vArray, start, stop );
      } else {
        lastResort.sortRange
          ( comparator, vArray, start, stop );
      }
    }
  }
}
