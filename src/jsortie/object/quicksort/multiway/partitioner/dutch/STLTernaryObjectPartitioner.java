package jsortie.object.quicksort.multiway.partitioner.dutch;

import java.util.Comparator;

import jsortie.object.quicksort.multiway.partitioner.MultiPivotObjectPartitioner;

public class STLTernaryObjectPartitioner<T> 
  implements MultiPivotObjectPartitioner<T> {
    @Override public String toString() {
      return this.getClass().getSimpleName();
    }
  @Override
  public int[] multiPartitionRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int[] pivotIndices) {
    int pivotIndex     = pivotIndices [ pivotIndices.length / 2 ];
    int middle         = start + (stop - start)/2; //_Mid in the STL code
    T   vPivot         = vArray [ pivotIndex ];
    vArray[pivotIndex] = vArray [ middle ];
    vArray[middle]     = vPivot;
    //STL code has: _Median(start, _Mid, _Last - 1, _Pred);
    int midStart = middle;       //_Pfirst in the STL code
    int midStop  = midStart + 1; //_Plast  in the STL code
    while ( start < midStart
            && comparator.compare ( vArray[midStart-1] , vPivot ) == 0 ) { 
      --midStart;
    }
    while ( midStop < stop
            && comparator.compare ( vArray[midStop] , vPivot ) == 0 ) {
      ++midStop;
    }
    int rhs = midStop;  //_Gfirst in the STL code
    int lhs = midStart-1; //_Glast  in the STL code
    for ( ; ; ) { // partition
      while ( rhs < stop ) {
        T v = vArray[rhs];
        int c = comparator.compare ( v ,  vPivot );
        if ( c <= 0 ) {
          if ( c < 0 ) {
            break;
          }
          vArray[rhs]     = vArray[midStop];
          vArray[midStop] = v;
          ++midStop;
        }
        ++rhs;
      }
      while (start <= lhs) {
        T v = vArray[lhs];
        int c = comparator.compare ( vPivot , v);
        if ( c <= 0) {
          if ( c < 0) {
            break;
          }
          --midStart;
          vArray[lhs] = vArray[midStart];
          vArray[midStart] = v;
        }
        --lhs;
      }
      if (lhs < start) { 
        if (rhs == stop) {
          return new int[] 
            { start, midStart, midStop, stop };
        }
        return finishOnRight
               ( comparator, vArray
               , vPivot, start, midStart
               , midStop, rhs, stop );
      } else if (rhs == stop) {
        return finishOnLeft
               ( comparator, vArray
               , vPivot, start, lhs
               , midStart, midStop, stop );
      } else {
        T v = vArray[rhs];
        vArray[rhs] = vArray[lhs];
        vArray[lhs] = v;
        --lhs;
        ++rhs;
      }
    }
  }
  private int[] finishOnLeft
    ( Comparator<? super T> comparator, T[] vArray
    , T vPivot, int start, int lhs, int midStart
    , int midStop, int stop) {
    T v = vArray[lhs];
    for (;;) {
      --midStart;
      --midStop;
      vArray[lhs]      = vArray[midStart];
      vArray[midStart] = vArray[midStop];
      vArray[midStop]  = v;
      int c;
      do {
        --lhs;
        if (lhs<start) {
          return new int[] { start, midStart, midStop, stop };
        }
        v = vArray[lhs];
        c = comparator.compare ( v, vPivot );
        if ( c == 0) {
          --midStart;
          vArray[lhs] = vArray[midStart];
          vArray[midStart] = v;
        }
      } while (c <= 0);
    }
  }
  private int[] finishOnRight
    ( Comparator<? super T> comparator, T[] vArray
    , T vPivot, int start, int midStart
    , int midStop, int rhs, int stop) {
    T v = vArray[rhs];
    for (;;) {
      vArray[rhs]      = vArray[midStop];
      vArray[midStop]  = vArray[midStart];
      vArray[midStart] = v;
      ++midStart;
      ++midStop;
      int c;
      do {
        ++rhs;
        if (rhs==stop) {
          return new int[] { start, midStart, midStop, stop };
        }
        v = vArray[rhs];
        c = comparator.compare ( vPivot , v );
        if ( c == 0) {
          vArray[rhs] = vArray[midStop];
          vArray[midStop] = v;
          ++midStop;
        }
      } while ( c <= 0);
    }
  }
}
