package jsortie.quicksort.governor.adaptive;

import jsortie.RangeSorter;
import jsortie.earlyexitdetector.RangeSortEarlyExitDetector;
import jsortie.helper.EgalitarianPartitionerHelper;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;

public class QuicksortAdaptive1a extends QuicksortAdaptive {
  public QuicksortAdaptive1a 
    ( SinglePivotSelector selector
    , SinglePivotPartitioner partitioner
    , RangeSorter rangeSorter
    , int janitorThreshold
    , RangeSorter sortOfLastResort
    , RangeSortEarlyExitDetector detectorToUse) {
    super ( selector, partitioner, rangeSorter, janitorThreshold
          , sortOfLastResort, detectorToUse);
  }
  protected EgalitarianPartitionerHelper twp 
    = new EgalitarianPartitionerHelper();
  protected void sortRangeAdaptive
    ( int[] vArray, int leftBoundary
    , int start, int stop
    , int rightBoundary, int maxDepth) {
    for ( --maxDepth; 0<maxDepth; --maxDepth) {
      if (detector.exitEarlyIfSorted(vArray, start, stop)) {
        return;
      }
      SinglePivotSelector sel = chooseSelector       ( start, stop,  maxDepth );
      int pivotIndex          = sel.selectPivotIndex ( vArray, start, stop );
      int vPivot = vArray[pivotIndex];
      if ( leftBoundary!=-1 && vArray[leftBoundary]== vPivot ) { 
        //preempt the partitioner if the pivot compares equal to one
      	//already used further to the left in the input.
        start = twp.moveUnequalToRight(vArray, start, stop, vPivot);
        leftBoundary = start-1;
      } else if (rightBoundary!=-1 && vPivot==vArray[rightBoundary]) {
        //preempt the partitioner if the pivot compares equal to one
        //already used further to the right in the input.
        stop  = twp.moveUnequalToLeft(vArray, start, stop, vPivot);
        rightBoundary = stop;
      } else {
        //	
        //these bits aren't geared for inputs where many items compare equal!
        //they're actually intended for mostly ordered inputs where a few
        //"nearby" items compare equal!
        //
        if (leftBoundary!=-1 && vArray[start]==vArray[leftBoundary]) {
          do {
            ++start;
          } while (start<stop && vArray[start]==vArray[leftBoundary]);
        }
        if (rightBoundary!=-1 && vArray[stop-1]==vArray[rightBoundary]) {
          do {
            --stop;
          } while (start<stop && vArray[stop-1]==vArray[rightBoundary]);
        }
        
        if (stop-start <= janitorThreshold) {
          break;
        }
        
        SinglePivotPartitioner party 
          = choosePartitioner ( start, stop,  maxDepth );
        pivotIndex = party.partitionRange 
                     ( vArray, start, stop, pivotIndex);
        vPivot = vArray[pivotIndex];
        if ( pivotIndex - start  < stop - pivotIndex - 1 ) {
          sortRangeAdaptive( vArray, leftBoundary, start, pivotIndex, pivotIndex, maxDepth);
          leftBoundary = pivotIndex;
          start = pivotIndex+1;
        } else {
          sortRangeAdaptive( vArray, pivotIndex, pivotIndex+1, stop, rightBoundary, maxDepth);
          rightBoundary = pivotIndex;
          stop = pivotIndex;
        }
      }
    }
    if (start+1<stop)  {
      if (0==maxDepth) {
        lastResort.sortRange ( vArray, start, stop );
      } else {
        chooseJanitor ( start, stop).sortRange   ( vArray, start, stop);
      }
    }
  }
}
