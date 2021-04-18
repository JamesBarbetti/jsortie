package jsortie.helper;

import java.util.Comparator;

public class FancierEgalitarianPartitionerHelper
  extends EgalitarianPartitionerHelper {
  public int moveEqualOrLessToLeft
    ( int[] vArray, int start, int stop, int vPivot) {
    //An implementation that uses a floating hole
    while (start<stop && vArray[start]<=vPivot) {
      ++start;
    }
    if (start+1<stop) {
      int vFirstTooBig = vArray[start];
      for (int scan=start+1; scan<stop; ++scan)
      {
        int v = vArray[scan];
        if (v<=vPivot) {
            vArray[start]=v;
            ++start;
            vArray[scan]=vArray[start];
    	  }
      }
      vArray[start]=vFirstTooBig;
    }
    return start;
  }
  public int moveEqualOrGreaterToRight
    ( int[] vArray, int start, int stop, int vPivot) {
    //An implementation that uses a floating hole
    int last = stop-1;
    while (start<=last && vPivot<=vArray[last]) {
      --last;
    }
    if (start<last) {
      int vFirstTooSmall = vArray[last];
      for (int scan=last-1; start<=scan; --scan)
      {
        int v = vArray[scan];
        if (vPivot<=v) {
          vArray[last]=v;
          --last;
          vArray[scan]=vArray[last];
        }
      }
      vArray[last]=vFirstTooSmall;
    }
    return last+1;
  }
  public void swapLowToLeftAndHighToRight
    ( int[] vArray, int[] boundaries, int b
    , int vLoPivot, int vHiPivot) {
    //An implementation that uses two floating holes
    int first= boundaries[b];
    int last = boundaries[b+1]-1;
    while (first<=last && vArray[first]<=vLoPivot) {
      ++first;
    }
    while (first<=last && vHiPivot<=vArray[last]) {
      --last;
    }
    if (first<=last)
    {
      int vFirstTooBig   = vArray[first];
      int vFirstTooSmall = vArray[last];
      for (int scan=first+1; scan<last; ++scan) {
        int v = vArray[scan];
        if (v<=vLoPivot) {
          vArray[first]=v;
          ++first;
          vArray[scan]=vArray[first];
        } else if (vHiPivot<=v) {
          vArray[last]=v;
          do
          {
            --last;
            v=vArray[last];
          } while (vHiPivot<=v);
          if (v<=vLoPivot) {
            vArray[first]=v;
            ++first;
            vArray[scan]=vArray[first];
          }
          else {
            vArray[scan]=v;
          }
        }
      }
      vArray[first] = vFirstTooSmall;
      vArray[last]  = vFirstTooBig;
      if (vFirstTooSmall<=vLoPivot) {
        ++first; 
      }
      if (vHiPivot<=vFirstTooBig) {
        --last;  
      }
    }
    boundaries[b]   = first;
    boundaries[b+1] = last+1;
  }
  public <T> int moveEqualOrLessToLeft
    ( Comparator<? super T> comp, T[] vArray
    , int start, int stop, T vPivot) {
    while (start<stop 
           && comp.compare
              ( vArray[start], vPivot ) <= 0 ) {
      ++start;
    }
    if (start+1<stop) {
      T vFirstTooBig = vArray[start];
      for (int scan=start+1; scan<stop; ++scan)
      {
        T v = vArray[scan];
        if ( comp.compare(v, vPivot) <= 0 ) {
            vArray[start]=v;
            ++start;
            vArray[scan]=vArray[start];
        }
      }
      vArray[start]=vFirstTooBig;
    }
    return start;
  }
  public <T> int moveEqualOrGreaterToRight
    ( Comparator<? super T> comp, T[] vArray
    , int start, int stop, T vPivot) {
    int last = stop-1;
    while ( start<=last 
            && comp.compare 
               ( vPivot , vArray[last]) <= 0 ) {
      --last;
    }
    if (start<last) {
      T vFirstTooSmall = vArray[last];
      for (int scan=last-1; start<=scan; --scan)
      {
        T v = vArray[scan];
        if ( comp.compare(vPivot, v) <= 0 ) {
          vArray[last]=v;
          --last;
          vArray[scan]=vArray[last];
        }
      }
      vArray[last]=vFirstTooSmall;
    }
    return last+1;
  }
}
