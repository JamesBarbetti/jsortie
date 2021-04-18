package jsortie.mergesort.vanilla;

import jsortie.StableRangeSorter;
import jsortie.janitors.insertion.BinaryInsertionSort;

public class StaircaseMergesort 
  extends MergesortBase {
  public StaircaseMergesort
    ( StableRangeSorter janitor, int threshold ) {
    super(janitor, threshold);
  }	
  protected static boolean preferPingPong() { 
    return true; 
  }
  public void mergeToLeftForecasting 
    ( int[] vWorkArea, int leftStart, int leftStop
    , int[] vArray,    int rightStart, int rightStop
    , int dest) {
    // Merges the values from workarea with those
    // in array (merging from left to right)
    // This is stable, if: the (leftStop-leftStart)
    // values in workarea are the result
    // of a stable sort, the (rightStop-rightStart)
    // values in array[rightStart..rightStop-1]
    // are the result of a stable sort, 
    // and all the values in workarea appeared 
    // in the input before all the values in array.
    //
    // For performance (and space) reasons, this 
    // routine "prefers" to be called with leftCount
    // significantly lower than rightCount

    //1. Skip over any thing from workarea 
    //   that comes before anything in array
    int vRight   = vArray[rightStart];
    int leftSkip = BinaryInsertionSort.findPostInsertionPoint
                   ( vWorkArea, leftStart, leftStop, vRight);
    int leftRead = leftStart;
    for (; leftRead < leftSkip; ++leftRead) {
      vArray[dest] = vWorkArea[leftRead];
      ++dest;
    }
    if (dest==rightStart) {
      return;
    }

    //It is assumed that the left-hand input (from workarea) 
    //contains substantially fewer elements than the 
    //right-hand input (from array).  If, despite that, 
    //we knew that the last element to be written was to 
    //come from the left-hand input, we would only need
    //to check (leftStop-leftStart) times (rather than 
    //(leftStop-leftStart+rightStop-rightStart times) 
    //for an input running out.  
    int leftPause  = BinaryInsertionSort.findPostInsertionPoint
                    ( vWorkArea, leftRead, leftStop, vArray[rightStop-1] );
    int rightRead = rightStart;
    int vLeft;
    for (; leftRead < leftPause; ++leftRead) {
      vLeft = vWorkArea[leftRead];
      while ( vRight < vLeft ) {
        vArray[dest] = vRight;
        ++dest;
        ++rightRead;
        vRight = vArray[rightRead];
      }
      vArray[dest] = vLeft;
      ++dest;			
    }
		
    if (leftRead<leftStop ) {
      vLeft = vWorkArea[leftRead];
      for (; rightRead < rightStop; ++rightRead) {
        vRight = vArray[rightRead];
        while ( vLeft <= vRight ) {
          vArray[dest] = vArray[leftRead];
          ++dest;
          ++leftRead;	
          if (leftRead==leftStop) {
            return;
          }
          vLeft = vWorkArea[leftRead];					
        }
        vArray[dest] = vRight;
        ++dest;
      }
      for (; leftRead < leftStop; ++leftRead) {
        vArray[dest] = vWorkArea[leftRead];
        ++dest;
      }
    }
  }
	
  public void copyAndSortRange 
    ( int [] vSourceArray, int start, int stop
    , int vDestArray[], int destStart) {
    // Sorts the values in a range 
    // (in array[start..start+count-1]) 
    // into a destination range (in 
    // dest[destStart..destStart+(stop-start)-1]).
    // the values in the input range will be 
    // shuffled during the process.
    if (stop-start<janitorThreshold) {	
      copyAndSortSmallRange(vSourceArray, start, stop, vDestArray, destStart );
    } else {
      int leftCount = leftChildPartitionSize(stop-start);
      if  (leftCount<1) leftCount=1;
      int rightCount = stop - start- leftCount;
      int destStop   = destStart + ( stop - start );
      //Steps:
      //1. sort left of the input array, 
      //   using the right of the output 
      //   array as a work-area
      //2. sort right of input into the 
      //   right of the output
      //3. merge left of the input, 
      //   and right of the output, into 
      //   output (from left to right)
      sortRangeUsing         ( vSourceArray, start,                start+leftCount
                             , vDestArray,  destStop-rightCount,  destStop );
      copyAndSortRange       ( vSourceArray, start+leftCount,      stop
                             , vDestArray,  destStart+leftCount);
      mergeToLeftForecasting ( vSourceArray, start,                start + leftCount
                             , vDestArray,  destStart+leftCount,  destStop
                             , destStart);
    }
  }
	
  public void sortRangeUsing ( int[] vArray,    int start,     int stop
                             , int[] vWorkArea, int workStart, int workStop) {
    int count = stop-start;
    if (count<janitorThreshold) {
      sortSmallRange(vArray, start, stop );
    } else {
      int workCount = workStop - workStart;
      int leftCount = leftChildPartitionSize(count);
      if  (leftCount<1) leftCount=1;
      if (workCount < leftCount) {
        leftCount = workCount;
      } else if ( preferPingPong() && leftCount < count/2 
                  && workCount >= count / 2) {
        leftCount = count/2;
        if  (leftCount<1) leftCount=1;
      }
      //Steps:
      //1. sort the right of the array, 
      //   using the left of the work area
      //2. sort the left of the array into 
      //   the left of the work area
      //3. merge left of the work area, 
      //   and right of the array, 
      //   into the array (from left to right)			
      sortRangeUsing         ( vArray,    start+leftCount, stop
                             , vWorkArea, workStart,       workStop );
      copyAndSortRange       ( vArray,    start,           start + leftCount
                             , vWorkArea, workStart );
      mergeToLeftForecasting ( vWorkArea, workStart,       workStart + leftCount
                             , vArray,    start+leftCount, start + count
                             , start );			
    }
  }	
}
