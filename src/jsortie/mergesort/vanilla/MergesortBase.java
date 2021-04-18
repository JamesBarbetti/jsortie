package jsortie.mergesort.vanilla;

import jsortie.StableRangeSorter;
import jsortie.earlyexitdetector.StableRangeSortEarlyExitDetector;
import jsortie.earlyexitdetector.WainwrightStableEarlyExitDetector;
import jsortie.helper.RangeSortHelper;
import jsortie.helper.ShiftHelper;
import jsortie.janitors.insertion.InsertionSort;

public class MergesortBase 
  implements StableRangeSorter {
  protected StableRangeSorter janitor
    = new InsertionSort();
  protected ShiftHelper shifter
    = new ShiftHelper();
  protected StableRangeSortEarlyExitDetector wainwright 
    = new WainwrightStableEarlyExitDetector();
  protected int janitorThreshold;
    //note: Can't use FancierWainwrightHelper 
    //because it isn't stable
  public MergesortBase 
    ( StableRangeSorter janitorToUse, int threshold ) {
    janitor          = janitorToUse;
    janitorThreshold = threshold;
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + janitor.toString() 
      + "," + janitorThreshold + ")";
  }	
  protected int leftChildPartitionSize
    ( int parentPartitionSize ) {
    return parentPartitionSize / 2;
  }		
  public void mergeToLeft 
    ( int[] vWorkArea, int workStart,  int workStop
    , int[] vArray,    int arrayStart, int arrayStop
    , int dest) {
    //Assumed: workStart < workStop, arrayStart < arrayStop
    //note: if an item in [] vArray is equal to one 
    //      in work[] the one from array comes first 
    //      in the merged output.
    for (;;) {
      if (vWorkArea[workStart] <= vArray[arrayStart]) {
        vArray[dest] = vWorkArea[workStart];
        ++dest;
        ++workStart;
        if (workStart==workStop) {
          if ( arrayStart != dest ) {
            shifter.copyRangeFromLeft
              ( vArray, arrayStart, arrayStop, vArray, dest);
          }
          break;
        }
      } else {
        vArray[dest] = vArray[arrayStart];
        ++dest;
        ++arrayStart;
        if (arrayStart == arrayStop) {
          if ( dest!=workStart || vArray!=vWorkArea ) {
            shifter.copyRangeFromLeft
              ( vWorkArea, workStart, workStop, vArray, dest);
          }
          break;
        }
      }
    }
  }
  public void mergeToRight
    ( int[] vWorkArea, int workStart, int workStop
    , int[] vArray,    int arrayStart, int arrayStop
    , int dest) {
    //note: if an item in [] vArray is equal 
    //      to one in work[] the one from array 
    //      comes first in the merged output.
    int w = workStop - 1;
    int a = arrayStop - 1;
    int d = dest + (workStop-workStart) 
                 + (arrayStop-arrayStart) - 1;
    for (;;) {
      if (vArray[a] <= vWorkArea[w]) {
        vArray[d] = vWorkArea[w];
        --w;
        --d;
        if (w<workStart) {
          if ( a != d ) {
            shifter.copyRangeFromRight
              ( vArray, arrayStart, a+1, vArray, dest );
          }
          break;
        }
      } else {
        vArray[d] = vArray[a];
        --a;
        --d;
        if (a<arrayStart) {
          if ( w != d || vWorkArea != vArray) {
            shifter.copyRangeFromRight
              ( vWorkArea, workStart, w+1, vArray, dest );
          }
          break;
        } // right input has run out
      } // was array[a] <= work[w]
    } // main loop
  }
  public void mergeExternal
    ( int left[],  int leftStart,  int leftStop
    , int right[], int rightStart, int rightStop
    , int dest[],  int destStart) {
    if (leftStart<leftStop && rightStart<rightStop) {
      do {
        if ( left[leftStart] <= right[rightStart] ) {
          do {
            dest[destStart] = left[leftStart];
            ++leftStart;
            ++destStart;
            if (leftStart==leftStop) {
              do {
                dest[destStart] = right[rightStart];
                ++rightStart;
                ++destStart;            	  
              } while (rightStart<rightStop);
              return;
            }
          } while (left[leftStart] <= right[rightStart]);
        } 
        dest[destStart] = right[rightStart];
        ++rightStart;
        ++destStart;
      } while (rightStart<rightStop);
      do {
        dest[destStart] = left[leftStart];
        ++leftStart;
        ++destStart;    	  
      } while (leftStart<leftStop);
    } else {
      RangeSortHelper.copyRange
        ( left, leftStart,   leftStop,  dest, destStart );
      RangeSortHelper.copyRange
        ( right, rightStart, rightStop, dest, destStart );
    }
  }
  public void mergeExternalRightToLeft 
    ( int left[], int leftStart, int leftStop
    , int right[], int rightStart, int rightStop
    , int dest[],  int destStart) {
    int leftScan  = leftStop  - 1;
    int rightScan = rightStop - 1;
    int destWrite = destStart + ( leftStop - leftStart )
                              + ( rightStop - rightStart ) - 1;
    if (leftStart<leftStop && rightStart<rightStop) {
      do {
        if ( left[leftScan] <= right[rightScan] ) {
          do {
            dest[destWrite] = right[rightScan];
            --rightScan;
            --destWrite;
            if (rightScan<rightStart) {
              do {
                dest[destWrite] = left[leftScan];
                --leftScan;
                --destWrite;
              } while (leftStart<=leftScan);
              return;
            }
          } while ( left[leftScan] <= right[rightScan] );
        } 
        dest[destWrite] = left[leftScan];
        --leftScan;
        --destWrite;
      } while (leftStart<=leftScan);
      do {
        dest[destWrite] = right[rightScan];
        --rightScan;
        --destWrite;
      } while (rightStart<=rightScan);
    } else {
      RangeSortHelper.copyRangeRightToLeft
        ( left, leftStart,   leftStop,  dest, destStart );
      RangeSortHelper.copyRangeRightToLeft
        ( right, rightStart, rightStop, dest, destStart );
    }	
  }
  @Override
  public void sortRange
    ( int [] vArray, int start, int stop ) {
    int count = stop-start;
    if (count<janitorThreshold) {
      sortSmallRange(vArray, start, stop);
    } else {
      int workArea[] = new int[count];
      sortRangeUsing ( vArray, start, stop
                     , workArea, 0, count);
    }
  }
  protected void sortRangeUsing
    ( int[] vArray,    int start,     int stop
    , int[] vWorkArea, int workStart, int workStop) {
    int count = stop-start;
    if (count<janitorThreshold) {
      sortSmallRange(vArray, start, stop);
    } else {
      int leftCount = leftChildPartitionSize(count);
      if (leftCount<1) leftCount=1;
      sortRangeInto ( vArray, start, start+leftCount
                    , vWorkArea, workStart );
      sortRangeInto ( vArray, start+leftCount, stop
                    , vWorkArea, workStart+leftCount );
      mergeExternal ( vWorkArea, workStart
      		          , workStart + leftCount
                    , vWorkArea, workStart+leftCount
                    , workStart + count, vArray
                    , start);
    }
  }
  protected void sortRangeInto
    ( int source[], int start, int stop
    , int dest[], int destStart) {
    int count = stop-start;
    if (count<janitorThreshold) {
      copyAndSortSmallRange ( source, start, stop
                            , dest, destStart);
    } else {
      int leftCount = leftChildPartitionSize(count);
      if (leftCount<1) leftCount=1;
      int rightCount = count-leftCount;
      sortRangeUsing ( source, start, start+leftCount
                     , dest, destStart, destStart+leftCount);
      sortRangeUsing ( source, start+leftCount, stop
                     ,  dest, destStart, destStart+rightCount);
      mergeExternal  ( source, start, start+leftCount
                     , source, start+leftCount, stop, dest
                     , destStart);
    }
  }
  protected void copyAndSortSmallRange 
    ( int[] source, int start, int stop
    , int[] dest, int destStart)  {
    shifter.copyRangeFromLeft
      ( source, start, stop, dest, destStart );
    int destStop = destStart + (stop-start);
    sortSmallRange(dest, destStart, destStop);
  }
  protected void sortSmallRange
    ( int[] vArray, int start, int stop ) {
    if (!wainwright.exitEarlyIfSortedStable
         ( vArray, start, stop ) ) {
      janitor.sortRange(vArray, start, stop);
    }
  }
}
