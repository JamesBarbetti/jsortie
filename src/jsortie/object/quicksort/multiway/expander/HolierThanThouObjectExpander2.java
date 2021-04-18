package jsortie.object.quicksort.multiway.expander;

import java.util.Comparator;


public class HolierThanThouObjectExpander2<T> 
  extends HolierThanThouObjectExpanderBase<T> {
  public HolierThanThouObjectExpander2() {
    super(2);
  }
  public int[] expandPartitionsToRight
    ( Comparator<? super T> comp
    , T[] vArray, int start
    , int[] pivotIndices
    , int startRight, int stop) {
    int hole1 = pivotIndices[0];
    int hole2 = pivotIndices[1];
    
    T vP = vArray[hole1];
    T vQ = vArray[hole2];

    for ( int scan=startRight
        ; scan<stop; ++scan) {
      T v = vArray[scan];
      if ( comp.compare ( v , vQ ) <= 0 ) {
        vArray[scan] = vArray[hole2+1];
        if ( comp.compare ( v , vP ) <= 0 ) {
          vArray[hole1]   = v;
          ++hole1;
          vArray[hole2] = vArray[hole1];
        } else {
          vArray[hole2] = v;
        }
        ++hole2;
      }
    }
    vArray[hole1] = vP;
    vArray[hole2] = vQ;
    return new int[] { start, hole1
                     , hole1+1, hole2
                     , hole2+1, stop };
  }  
  public int[] expandPartitionsToLeft 
    ( Comparator<? super T> comp
    , T[] vArray, int start, int stopLeft
    , int[] pivotIndices, int stop) {
    int hole1 = pivotIndices[0];
    int hole2 = pivotIndices[1];
    T vP = vArray[hole1];
    T vQ = vArray[hole2];
    for ( int scan = stopLeft - 1 
        ; start<=scan; --scan) {
      T v = vArray[scan];
      if ( comp.compare ( vP , v ) <= 0 ) {
        vArray[scan]=vArray[hole1-1];
        if ( comp.compare ( vQ , v ) <= 0 ) {
          vArray[hole2] = v;
          --hole2;
          vArray[hole1] = vArray[hole2];
        } else {
          vArray[hole1] = v;
        }
        --hole1;
      }
    }
    vArray[hole1] = vP;
    vArray[hole2] = vQ;
    return new int[] { start, hole1
                     , hole1+1, hole2
                     , hole2+1, stop };
  }
}
