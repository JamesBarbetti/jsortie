package jsortie.quicksort.multiway.partitioner.singlepivot;

public class STLTernaryPartitioner 
  implements TernarySinglePivotPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int getPivotCount() {
    return 1;
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray
    , int start, int stop //_First and _Last in the STL code
    , int[] pivotIndices) {
    int pivotIndex     = pivotIndices [ pivotIndices.length / 2 ];
    int middle         = start + (stop - start)/2; //_Mid in the STL code
    int vPivot         = vArray [ pivotIndex ];
    vArray[pivotIndex] = vArray [ middle ];
    vArray[middle]     = vPivot;
    //STL code has: _Median(start, _Mid, _Last - 1, _Pred);
    int midStart = middle;       //_Pfirst in the STL code
    int midStop  = midStart + 1; //_Plast  in the STL code
    while ( start < midStart
            && vArray[midStart-1] == vPivot) { 
      --midStart;
    }
    while ( midStop < stop
            && vArray[midStop] == vPivot) {
      ++midStop;
    }
    int rhs = midStop;  //_Gfirst in the STL code
    int lhs = midStart-1; //_Glast  in the STL code
    for ( ; ; ) { // partition
      while ( rhs < stop ) {
        int v = vArray[rhs];
        if (v <= vPivot) {
          if (v < vPivot) {
            break;
          }
          vArray[rhs]     = vArray[midStop];
          vArray[midStop] = v;
          ++midStop;
        }
        ++rhs;
      }
      while (start <= lhs) {
        int v = vArray[lhs];
        if (vPivot <= v) {
          if (vPivot < v) {
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
               ( vArray, vPivot, start, midStart
               , midStop, rhs, stop );
      } else if (rhs == stop) {
        return finishOnLeft
               ( vArray, vPivot, start, lhs
               , midStart, midStop, stop );
      } else {
        int v = vArray[rhs];
        vArray[rhs] = vArray[lhs];
        vArray[lhs] = v;
        --lhs;
        ++rhs;
      }
    }
  }
  private int[] finishOnLeft
    ( int[] vArray, int vPivot, int start, int lhs
    , int midStart, int midStop, int stop) {
    int v = vArray[lhs];
    for (;;) {
      --midStart;
      --midStop;
      vArray[lhs]      = vArray[midStart];
      vArray[midStart] = vArray[midStop];
      vArray[midStop]  = v;
      do {
        --lhs;
        if (lhs<start) {
          return new int[] 
            { start, midStart, midStop, stop };
        }
        v = vArray[lhs];
        if ( vPivot == v ) {
          --midStart;
          vArray[lhs] = vArray[midStart];
          vArray[midStart] = v;
        }
      } while (v <= vPivot );
    }
  }
  private static int[] finishOnRight
    ( int[] vArray, int vPivot,  int start
    , int midStart, int midStop, int rhs, int stop) {
    int v = vArray[rhs];
    for (;;) {
      vArray[rhs]      = vArray[midStop];
      vArray[midStop]  = vArray[midStart];
      vArray[midStart] = v;
      ++midStart;
      ++midStop;
      do {
        ++rhs;
        if (rhs==stop) {
          return new int[] 
            { start, midStart, midStop, stop };
        }
        v = vArray[rhs];
        if ( vPivot == v ) {
          vArray[rhs] = vArray[midStop];
          vArray[midStop] = v;
          ++midStop;
        }
      } while ( vPivot <= v);
    }
  }
}
