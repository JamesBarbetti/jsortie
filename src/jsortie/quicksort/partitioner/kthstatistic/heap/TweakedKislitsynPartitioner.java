package jsortie.quicksort.partitioner.kthstatistic.heap;

public class TweakedKislitsynPartitioner 
  extends KislitsynPartitioner {
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start, int stop
    , int targetIndex ) {
    fold( vArray, start, stop );
    int middle = start + (stop+1-start)/2;
    if (targetIndex < middle) {
      foldToLeft ( vArray, start, targetIndex, stop );
      foldToLeft ( vArray, targetIndex+1, targetIndex+2, stop );
      nOn2Sort   ( vArray, start, targetIndex+targetIndex-start );
      partitionRangeOnLeft
        ( vArray, start, stop, targetIndex );
    } else {
      foldToRight ( vArray, start, targetIndex, stop);
      foldToRight ( vArray, start, targetIndex-2, targetIndex-1);
      nOn2Sort    ( vArray, targetIndex+targetIndex-stop, stop);
      partitionRangeOnRight
        ( vArray, start, stop, targetIndex );
    }
  }
  public void fold 
    ( int[] vArray, int start, int stop ) {
    int i = start;
    int j = stop-1;
    for (; i<j; ++i, --j) {
      int vLeft  = vArray[i];
      int vRight = vArray[j];
      boolean inOrder = (vLeft<=vRight);
      vArray[i] = inOrder ? vLeft  : vRight;
      vArray[j] = inOrder ? vRight : vLeft;
    }
  }
  public void nOn2Sort
    ( int[] vArray, int start, int stop ) {
    int i = start;
    int j = stop-1;
    for (; i<j; ++i, --j) {
      int vLeft  = vArray[i];
      int vRight = vArray[j];
      boolean inOrder = (vLeft<=vRight);
      vArray[i] = inOrder ? vLeft  : vRight;
      vArray[j] = inOrder ? vRight : vLeft;
    }
  }
  public void foldToLeft
    ( int[] vArray, int start
    , int leftStop, int stop ) {
    while ( leftStop < stop && 1 < stop-start ) {
      int j = stop-(stop-start)/2;
      if (j<leftStop) {
        j=leftStop;
      }
      int i = j + j - stop;
      for (; j<stop; ++i, ++j) {
        int vLeft  = vArray[i];
        int vRight = vArray[j];
        boolean inOrder = (vLeft<=vRight);
        vArray[i] = inOrder ? vLeft  : vRight;
        vArray[j] = inOrder ? vRight : vLeft;
      }
      stop = start + (stop-start)/2;
    }
  }
  public void foldToRight
   ( int[] vArray, int start
   , int rightStart, int stop) {
    while ( start <= rightStart && 1 < stop-start ) {
      int i=start+(stop-start)/2;
      if (rightStart<i) {
        i=rightStart;
      }
      int j = i + i - start;
      for (; j<stop; ++i, ++j) {
        int vLeft  = vArray[i];
        int vRight = vArray[j];
        boolean inOrder = (vLeft<=vRight);
        vArray[i] = inOrder ? vLeft  : vRight;
        vArray[j] = inOrder ? vRight : vLeft;
      }
      start = stop - (stop-start)/2;
    }
  }
}
