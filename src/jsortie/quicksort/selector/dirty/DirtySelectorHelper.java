package jsortie.quicksort.selector.dirty;

import jsortie.helper.RangeSortHelper;
import jsortie.janitors.insertion.twoway.ShellSort2Way;
import jsortie.quicksort.partitioner.kthstatistic.quickselect.ComparisonCountingQuickSelectPartitioner;

public class DirtySelectorHelper {	
  protected ShellSort2Way 
    twoWayShell = new ShellSort2Way();
  protected ComparisonCountingQuickSelectPartitioner 
    q = new ComparisonCountingQuickSelectPartitioner(); 
  
  public void findMedianOf5
    ( int[] vArray, int a, int b
    , int c, int d, int e) {
    //
    //At most 6 comparisons (on average: 5.6).
    //As per... https://www.ocf.berkeley.edu/~wwu/cgi-bin
    //          /yabb/YaBB.cgi?board=riddles_cs;action=display;num=1061827085
    //                                                           Worst   Avg
    RangeSortHelper.compareAndSwapIntoOrder(vArray, a, b);     // 1      1.0
    RangeSortHelper.compareAndSwapIntoOrder(vArray, d, e);     // 2      2.0
    if ( vArray[d] < vArray[a] ) {                             // 3      3.0
      RangeSortHelper.swapElements(vArray, a, d);
      RangeSortHelper.swapElements(vArray, b, e);
    }
    if ( vArray[b] <= vArray[c] ) {                            // 4      4.0
      RangeSortHelper.compareAndSwapIntoOrder(vArray, c, d);   // 5      4.4
    } else if ( vArray[c] <= vArray[d]) {                      // 5      5.0
      RangeSortHelper.compareAndSwapIntoOrder(vArray, b, c);   // 6      5.4
    } else {
      RangeSortHelper.compareAndSwapIntoOrder(vArray, c, e);   // 6      5.6
    }
  }
  public void branchAvoidingFindMedianOf5
    ( int[] vArray, int a, int b
    , int c, int d, int e) {
    //
    //The "conditional movie" version 
    //of findMedianOf5
    //
    int vA = vArray[a];
    int vB = vArray[b];
    boolean abInOrder = vA < vB;
    vArray[a] = abInOrder ? vA : vB;
    vArray[b] = abInOrder ? vB : vA;
    
    int vD = vArray[d];
    int vE = vArray[e];
    boolean deInOrder = vD < vE;
    vArray[d] = deInOrder ? vD : vE;
    vArray[e] = deInOrder ? vE : vD;
    
    vA     = vArray[a];
    vD     = vArray[d];
    vB     = vArray[b];
    vE     = vArray[e];
    boolean dBeforeA = vD < vA;
    vArray[a] = dBeforeA ? vD : vA;
    vArray[b] = dBeforeA ? vE : vB;
    vArray[d] = dBeforeA ? vA : vD;
    vArray[e] = dBeforeA ? vB : vE;
    
    vB     = vArray[b];
    int vC = vArray[c];
    vD     = vArray[d];
    if ( vB <= vC ) {
      boolean cdInOrder = (vC <= vD);
      vArray[c] = cdInOrder ? vC : vD;
      vArray[d] = cdInOrder ? vD : vC;
    } else if ( vC <= vD ) {
      boolean bcInOrder = (vB <= vC);
      vArray[b] = bcInOrder ? vB : vC;
      vArray[c] = bcInOrder ? vC : vB;
    } else {
      vE = vArray[e];
      boolean ceInOrder = (vC <= vE);
      vArray[c] = ceInOrder ? vC : vE;
      vArray[e] = ceInOrder ? vE : vC;
    }
  }
  public void sortColumns 
    ( int[] vArray, int start
    , int stop, int step) {
    int rowCountFloor 
      = ( stop - start - 1 ) / step;
    if ( rowCountFloor < 10 ) {
      twoWayShell.sortSlicesOfRange
        ( vArray, start, stop, step );
    } else {
      int lastInColumn = start + (rowCountFloor)*step;
      for (int column=0; column<step; ++column) {
        heapSortSlice
          ( vArray, start+column
          , lastInColumn+step, step);
        ++lastInColumn;
        if (stop<=lastInColumn) {
          lastInColumn-=step;
        }
      }
    }
  }
  public void sortReferencedItems 
    ( int[] vArray, int sampleIndices[]
    , int start, int stop) {
    //
    //Heapsort, on the items, in an array, referenced
    //by a (sorted) array of indices.  Note that start and
    //stop are indices into sampleIndices, *not* into array.
    //Contrast this with 
    //CleanMultiPivotSelectorHelper.sortIndexesOfItems().
    //Usually, the latter (sortIndexesOfItems) runs 
    //a bit faster (besides which, to use sortReferencedItems 
    //you need to sort sampleIndices).
    //
    int count = stop-start;
    if (count<2) return;
    int fudge = 2 - start;
    for (int h=start+(count)/2;h>=0;--h) {
       int x = sampleIndices[h]; //index of item being moved
       int v = vArray[x]; //its value, in the array (array[x])
       int i = h;
       int j = i + i + 2;	
       while (j<stop-1) {
        if (j<stop-2) 
          j += ( vArray[sampleIndices[j]]
                 < vArray[sampleIndices[j+1]]) 
            ? 1 : 0;
        if (vArray[sampleIndices[j]]<=v) {
          break;
        }
        vArray[sampleIndices[i]]
          = vArray[sampleIndices[j]];
        i = j;
        j = i + i + fudge;
      }
      vArray[sampleIndices[i]] = v;
    }
    for (int k=stop-1;k>start;--k) {
      int x = sampleIndices[k];
      int v = vArray[x];		
      int i = k;	    
      int j = 0;      
      while (j<k) {
        if (j<k-1) {
          j+= ( vArray[sampleIndices[j]]
                < vArray[sampleIndices[j+1]]) 
            ? 1 : 0;
        }
        if (vArray[sampleIndices[j]]<=v) {
          break;
        }
        vArray[sampleIndices[i]]
          = vArray[sampleIndices[j]];
        i = j;
        j = i + i + fudge;
      }
      vArray[sampleIndices[i]] = v;
    }
  }
  public void heapSortSlice
    ( int[] vArray, int start, int stop
    , int step) {
    //Uses heapsort to fully sort a single 
    //slice in an array. Although... a 
    //partial sort of the slice that found 
    //the median would actually... be 
    //sufficient, for the purposes of 
    //this class.
    if (stop<=start+step) return;
    int fudge= start - step;
    int lastInteriorIndex 
      = start+((stop-start+1)/2/step)*step;
    for ( int h=lastInteriorIndex
        ; h>=start;h-=step) { 
      int i = h;
      int v=vArray[i];
      int j = i + i - fudge;	
      while (j<stop) {
        if (j<stop-step) {
          j += (vArray[j]<vArray[j+step]) 
            ? step : 0;
        }
        if (vArray[j]<=v) {
          break;
        }
        vArray[i]=vArray[j];
        i = j;
        j = i + i - fudge;
      }
      vArray[i]=v;
    }
    for ( stop -= step; stop >= start
        ; stop -= step ) {
      int v = vArray[stop];
      int i = start;
      int j = start + step;
      vArray[i]=vArray[start]; 
      while (j<stop) {
        if (j<stop-step) {
          j += (vArray[j]<vArray[j+step]) 
            ? step : 0;
        }
        if (vArray[j]<=v) {
          break;
        }
        vArray[i]=vArray[j];
        i = j;
        j = i + i - fudge;
      }
      vArray[i] = v;
    }
  }
  public int partiallySortSlices
    ( int[] vArray
    , int start, int stop, int step
    , int innerStart, int innerStop) {
    int rowCountFloor 
      = (stop-start-1) / step;
    int[] vSlice = new int[rowCountFloor+1];
    int comparisonCount = 0;
    for (int column=0;column<step;++column) {
      //Copy items from slice "column" into vSlice
      int w = 0;
      int r = start+column; 
      for (; r<stop; r+=step) {
        vSlice[w] = vArray[r];
        ++w;
      }
      int a = ( innerStart + step - 1 - start - column ) / step;
      int b = ( innerStop  + step - 1 - start - column ) / step;
      if (0<=a && a<w) {
        comparisonCount += 
          q.partitionRangeExactlyCountComparisons
          ( vSlice, 0, w, a );
        if (a+1<b && b-1<w) {
          comparisonCount +=
            q.partitionRangeExactlyCountComparisons
            ( vSlice, a+1, w, b-1 );
        }
      }
      //Copy partially sorted items
      //from vSlice back to their place 
      for (r-=step; start<=r; r-=step) {
        --w;
        vArray[r] = vSlice[w];
      }
    }
    return comparisonCount;
  }
}