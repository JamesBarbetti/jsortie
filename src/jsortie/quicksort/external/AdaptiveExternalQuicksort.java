package jsortie.quicksort.external;

import jsortie.RangeSorter;
import jsortie.earlyexitdetector.StableRangeSortEarlyExitDetector;
import jsortie.earlyexitdetector.WainwrightStableEarlyExitDetector;
import jsortie.helper.EgalitarianPartitionerHelper;
import jsortie.helper.RangeSortHelper;
import jsortie.quicksort.selector.SinglePivotSelector;

public class AdaptiveExternalQuicksort 
  extends ExternalQuicksort {
  protected StableRangeSortEarlyExitDetector eed;
  protected EgalitarianPartitionerHelper helper;
  public AdaptiveExternalQuicksort 
  ( SinglePivotSelector pivotSelector
  , ExternalSinglePivotPartitioner partitioner
  , RangeSorter janitorRangeSorter
  , int janitorThreshold) {
    super ( pivotSelector, partitioner
          , janitorRangeSorter, janitorThreshold);
    eed = new WainwrightStableEarlyExitDetector();
    helper = new EgalitarianPartitionerHelper();
  }
  public void sortRangeUsing 
  ( int[] vArray, int start, int stop
  , int[] vAux,   int auxStart
  , boolean isDataInMainArray) {
    while (threshold<stop-start) {
      int count   = stop-start;
      int auxStop = auxStart + count;
      int leftCount;
      int vPivot;
      if (isDataInMainArray) {
        if ( eed.exitEarlyIfSortedStable
                 ( vArray, start, stop ) ) {
          return;
        }
        //here, pivotIndex is index into main array
        int pivotIndex 
          = selector.selectPivotIndex
            ( vArray, start, stop);
        vPivot
          = vArray[pivotIndex];
        leftCount
          = party.partitionMainRange
            ( vArray, start, stop
            , pivotIndex, vAux, auxStart);
      } else {
        if ( eed.exitEarlyIfSortedStable
            ( vAux, auxStart, auxStop ) ) {
          int w=start;
          for ( int r=auxStart
              ; r<auxStop; ++r, ++w) {
            vArray[w] = vAux[r];
          }
          return;
        }
        //here, pivotIndex is index into auxiliary array
        int pivotIndex = selector.selectPivotIndex
                         ( vAux, auxStart, auxStop);
        vPivot         = vAux[pivotIndex];
        leftCount      = party.partitionAuxiliaryRange
                         ( vArray, start
                         , vAux, auxStart, auxStop
                         , pivotIndex);
      }
      int rightCount   = stop - start - leftCount - 1;
      int auxRightStop = auxStart + rightCount;
      
      //<-- Start of the adaptive bits for handling 
      //    items that compare equal to the pivot.
      //shrink the left child partition if it has 
      //items that compare equal to the pivot, 
      //on its right.
      while ( 0<leftCount
              && vArray[start+leftCount-1]==vPivot) {
        --leftCount;
      }
      //shrink the right child partition if it has 
      //items that compare equal to the pivot on its
      //left.
      while ( 0<rightCount 
              && vAux[auxRightStop-rightCount]==vPivot) {
        vArray[stop-rightCount] 
          = vAux[auxRightStop-rightCount];
        --rightCount;
      }
      //<-- End of the adaptive bits for handling 
      //    items equal to the pivot
      if (leftCount<rightCount) {
        //recurse for the (smaller) 
        //left child partition;
        if (1<leftCount) {
          sortRangeUsing 
            ( vArray, start, start+leftCount
            , vAux,   auxStop - leftCount
            , true);
        }
        //and tail for the larger (right) partition
        start    = stop         - rightCount;
        isDataInMainArray = false;
      } else {
        //recurse for the (smaller) 
        //right child partition
        if (1<rightCount) {
          sortRangeUsing 
            ( vArray, stop-rightCount, stop
            , vAux,   auxRightStop-rightCount, false);
        }
        stop = start + leftCount;
        isDataInMainArray = true;
      }
    }
    if (!isDataInMainArray) {
     RangeSortHelper.copyRange
       ( vAux,  auxStart
       , auxStart + ( stop - start )
       , vArray, start);
    }
    janitor.sortRange(vArray, start, stop);
  }
}
