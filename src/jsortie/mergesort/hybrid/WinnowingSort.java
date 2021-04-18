package jsortie.mergesort.hybrid;

import jsortie.RangeSorter;
import jsortie.earlyexitdetector.TwoWayInsertionEarlyExitDetector;
import jsortie.helper.RangeSortHelper;
import jsortie.helper.ShiftHelper;
import jsortie.janitors.insertion.InsertionSort;
import jsortie.mergesort.vanilla.MergesortBase;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.multiway.selector.clean.SamplingMultiPivotSelector;

public class WinnowingSort implements RangeSorter {
  protected RangeSorter        innerSorter;
  protected int                samplingThreshold = 30;
  protected MultiPivotSelector selector; 
  protected MergesortBase      merger;
  protected ShiftHelper       shifter;
  protected TwoWayInsertionEarlyExitDetector wainwright;
  public WinnowingSort(RangeSorter innerSorterToUse) {
    innerSorter = innerSorterToUse;
    selector    = new SamplingMultiPivotSelector
                      (samplingThreshold, false);
    merger      = new MergesortBase
                      (new InsertionSort(), 64);
    shifter     = new ShiftHelper();
    wainwright  = new TwoWayInsertionEarlyExitDetector();
  }
  public String toString() {
    return this.getClass().getSimpleName()
           + "(" + innerSorter.toString() + ")";
  }
  public class WinnowingResult {
    public boolean isLeftSorted;
    public int     startOfChaff;
  }
  @Override
  public void sortRange 
    ( int[] vArray, int start, int stop ) {
    WinnowingResult w = new WinnowingResult();
    w.isLeftSorted = false;
    w.startOfChaff = stop;
    if (start+1<stop) {
      flipIfMostlyDescending(vArray, start, stop);
      if ( wainwright.exitEarlyIfSorted 
           (vArray, start, stop ) ) {
        return;
      }
      winnow(vArray, start, stop, w);
      if (!w.isLeftSorted) {
        sortWinnowed(vArray, start, w.startOfChaff);
      }
      int rhs = w.startOfChaff;
      if (rhs==stop) {
        return;
      }
      sortChaff(vArray, rhs, stop);
      if ( start<rhs && vArray[rhs-1] <= vArray[rhs] ) {
        return;
      } 
      int[] workArea = new int[stop-rhs];
      for (int i=rhs; i<stop; ++i) {
        workArea[i-rhs] = vArray[i];
      }
      merger.mergeToRight 
        ( workArea, 0, workArea.length
        , vArray, start, w.startOfChaff
        , start);
    }
  }
  protected void sortWinnowed
    ( int[] vArray, int start, int stop ) {	  
    innerSorter.sortRange(vArray, start, stop);
  }  
  protected void sortChaff
    ( int[] vArray, int start, int stop) {
    innerSorter.sortRange(vArray, start, stop);
  }
  private void flipIfMostlyDescending
    ( int[] vArray, int start, int stop ) {
    if (samplingThreshold<stop-start) {
      int sample[] = selector.selectPivotIndices
                     (vArray, start, stop);
      //The pivot indices will be ordered by the items 
      //they refer to. So, if sample[] itself is mostly
      //in reverse order, so where the items that the
      //indices in sample[] referred to.
      int sampleSize = sample.length;
      if ( sortAndCountInversions (sample, 0, sampleSize) 
           > sampleSize * sampleSize * 3 / 8 ) {
        RangeSortHelper.reverseRange(vArray, start, stop);
      }
    }
  }
  protected int sortAndCountInversions
    ( int[] vArray, int start, int stop ) {
    int inversionsCounted = 0; 
    int count = (stop-start);
    if (1<count) {
      int lhs = start + (count) / 2 - 1;
      int rhs = lhs + 1 + (count&1);

      while (start<=lhs ) { 
        int vLeft;
        int vRight;
        boolean swapped;
        if ( vArray[lhs]<=vArray[rhs]) {
          vLeft  = vArray[lhs];
          vRight = vArray[rhs];
          swapped = false;
        } else {
          vLeft  = vArray[rhs];
          vRight = vArray[lhs];
          inversionsCounted += (rhs-lhs)*2;
          swapped = true;
        }
	      
        int scan = lhs + 1;
        for (; vArray[scan]<vLeft; ++scan) {
          vArray[scan-1] = vArray[scan];
        }
        vArray[scan-1] = vLeft;
        inversionsCounted += swapped
                           ? (lhs+1-scan)
                           : (scan+1-lhs); 
        --lhs;
	      
        scan = rhs - 1;
        for (; vRight<vArray[scan]; --scan) {
          vArray[scan+1] = vArray[scan];
        }
        vArray[scan+1] = vRight;
        inversionsCounted += swapped
                           ? (scan+1-rhs) 
                           : (rhs-1-scan);
        ++rhs;
      }
    }
    return inversionsCounted;
  }
  protected void winnow 
    ( int[] vArray, int start, int stop
    , WinnowingResult w) {
    int state = 0;
    int lhs   = start+1;
    w.isLeftSorted = false;
    w.startOfChaff = stop;
    while ( lhs<stop && 
    		vArray[lhs-1]<=vArray[lhs] ) {
      ++lhs;
    }
    if (lhs<stop-5) {
      --lhs;
      if (lhs==start) ++lhs;
      int v    = vArray[lhs];
      int scan = lhs;
      for (int i=3; i>0; --i) {
        state += state 
          + (vArray[scan]<vArray[scan-1] ? 1 : 0);
        ++scan;
      }
      for (; scan<stop; ++scan) {
        state += state 
          + (vArray[scan]<vArray[scan-1] ? 1 : 0);
        state &= 31;
        if (state==4) {
          scan+=1;
          state&=3;
        } else {
          vArray[lhs] = vArray[scan-3];
          ++lhs;
          vArray[scan-3] = vArray[lhs];
        }
      }
      vArray[lhs] = v;
      shifter.moveBackElementsToFront
        ( vArray, lhs, stop-3, stop );		
      w.startOfChaff = lhs+3;
    }
  }
}
