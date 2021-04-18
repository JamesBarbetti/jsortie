package jsortie.object.mergesort;

import java.lang.ref.WeakReference;
import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;

public class WeakObjectMergesort<T> 
  implements ObjectRangeSorter<T> {
  int threshold = 4; //must be >=4.  Or you're fucked!
  protected class Sorter {
    Comparator<? super T> comparator;
    T[] vArray;
    int rangeStart;
    int rangeStop;
    int count;
    Object[] wA;
    Object[] wB;
    public Sorter(Comparator<? super T> comparatorToUse
      , T[] vSortMe, int start, int stop) {
      comparator = comparatorToUse;
      vArray = vSortMe;
      rangeStart = start;
      rangeStop  = stop;
      count      = stop - start;
      wA = new Object[count];
      wB = new Object[count];
    }
    public void sort() {
      for ( int r=rangeStart, w=0
          ; w<count; ++r, ++w) {
        wA[w] = new WeakReference<T>
                ( vArray[r] );
      }
      sortRangeUsing(wA, 0, count, wB, 0);
      for ( int r=rangeStart, w=0
          ; w<count; ++r, ++w) {
        wB[w] = vArray[r]; //To keep what was here alive!
        @SuppressWarnings("unchecked")
        WeakReference<T> weak
          = (WeakReference<T>) wA[w];
        vArray[r] = weak.get();
      }
    }
    @SuppressWarnings("unchecked")
    protected void sortRangeUsing
      ( Object[] wMain,  int start, int stop
      , Object[] wAux,   int auxStart) {
      int count = stop - start;
      if (threshold < count) {
        int leftCount = (count>>1);
        sortRangeUsing( wMain, start+leftCount, stop,  wAux, auxStart);
        sortRangeTo   ( wMain, start, start+leftCount, wAux, auxStart);
        mergeRangesTo ( wAux, auxStart, auxStart+leftCount
                      , wMain, start+leftCount, stop
                      , wMain, start );
      } else {
        //Insertion sort the weak references (icky).
        for ( int place=start+1
            ; place<stop; ++place) {
          WeakReference<T> w 
            = (WeakReference<T>)wMain[place];
          T v = w.get();
          int scan = place - 1;
          for (; start<=scan 
                 && comparator.compare
                    ( v, ((WeakReference<T>)wMain[scan]).get() ) < 0
               ; --scan) {
            wMain[scan+1] = wMain[scan];
          }
          wMain[scan+1] = w;
        }   
      }
    }
    
    protected void sortRangeTo
      ( Object[] wSource, int start, int stop
      , Object[] wDest,   int destStart) {
      int count = stop - start;
      int leftCount = (count>>1);
      sortRangeUsing( wSource, start, start+leftCount, wDest, destStart);
      sortRangeUsing( wSource, start+leftCount, stop,  wDest, destStart);
      mergeRangesTo ( wSource, start, start+leftCount
                    , wSource, start+leftCount, stop
                    , wDest,   destStart);
    }
    @SuppressWarnings("unchecked")
    protected void mergeRangesTo
      ( Object[] wLeft,  int leftStart,  int leftStop
      , Object[] wRight, int rightStart, int rightStop
      , Object[] wDest,  int destStart) {
      T vA = ((WeakReference<T>)wLeft[leftStart]).get();
      T vB = ((WeakReference<T>)wRight[rightStart]).get();
      for (;;) {
        if ( comparator.compare ( vA, vB ) <=0 ) {
          wDest[destStart] = new WeakReference<T>(vA);
          ++leftStart;
          ++destStart;
          if (leftStart==leftStop) {
            copyRange(wRight, rightStart, rightStop, wDest, destStart);
            return;
          }
          vA = ((WeakReference<T>)wLeft[leftStart]).get();
        } else {
          wDest[destStart] = new WeakReference<T>(vB);
          ++rightStart;
          ++destStart;
          if (rightStart==rightStop) {
            copyRange(wLeft, leftStart, leftStop, wDest, destStart);
            return;
          }
          vB = ((WeakReference<T>)wRight[rightStart]).get();
        }
      }
    }
    private void copyRange
      ( Object[] wSource, int start, int stop
      , Object[] wDest, int destStart) {
      if (wSource==wDest && start==destStart) {
        return; //Nothin' to do. Source and dest are identical
      }
      for (; start<stop; ++start, ++destStart) {
        wDest[destStart] = wSource[start];
        //Todo: Benchmark.  Does getting a new WeakReference work better?
        //      Because... It might.
      }
    }
  }
  @Override 
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    Sorter s = new Sorter(comparator, vArray, start, stop);
    s.sort();
    
  }
}