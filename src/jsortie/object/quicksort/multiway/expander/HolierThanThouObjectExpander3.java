package jsortie.object.quicksort.multiway.expander;

import java.util.Comparator;

public class HolierThanThouObjectExpander3<T> 
  extends HolierThanThouObjectExpanderBase<T> {
  public HolierThanThouObjectExpander3() {
    super(3);
  }
  @Override
  public int[] expandPartitionsToRight 
    ( Comparator<? super T> comparator
    , T[] vArray, int start
    , int[] pivotIndices, int startRight
    , int stop) {
    int leftHole   = pivotIndices[0];
    int middleHole = pivotIndices[1];
    int rightHole  = pivotIndices[2]; //3rd hole
    T vP = vArray[leftHole];
    T vQ = vArray[middleHole];
    T vR = vArray[rightHole]; //3rd pivot
    for ( int scan=startRight
        ; scan<stop; ++scan) {
      T v = vArray[scan];
      if ( comparator.compare 
           ( v , vQ ) < 0 ) {
        vArray[scan] 
          = vArray[rightHole+1];
        vArray[rightHole]
           = vArray[middleHole+1];
        if ( comparator.compare 
             ( v , vP ) < 0) {
          vArray[middleHole] 
            = vArray[leftHole+1];
          vArray[leftHole]   = v;
          ++leftHole;
        } else {
          vArray[middleHole] = v;
        }
        ++middleHole;
        ++rightHole;
      } else if ( comparator.compare 
                 ( v , vR ) < 0) {
        vArray[scan] 
          = vArray[rightHole+1];
        vArray[rightHole] = v;
        ++rightHole;
      }
    }
    vArray[leftHole]   = vP;
    vArray[middleHole] = vQ;
    vArray[rightHole]  = vR;
    return new int[] 
      { start, leftHole
      , leftHole+1, middleHole
      , middleHole+1, rightHole
      , rightHole+1, stop };
  }
  @Override
  public int[] expandPartitionsToLeft
    ( Comparator<? super T> comparator
    , T[] vArray, int start
    , int stopLeft, int[] pivotIndices
    , int stop) {
    int leftHole   = pivotIndices[0];
    int middleHole = pivotIndices[1];
    int rightHole  = pivotIndices[2]; //3rd hole
    T   vP = vArray[leftHole];
    T   vQ = vArray[middleHole];
    T   vR = vArray[rightHole];  //third pivot
    for ( int scan=stopLeft-1
        ; start<=scan; --scan ) {
      T v = vArray[scan];
      if ( comparator.compare 
           ( vQ , v ) <= 0) {
        vArray[scan] 
          = vArray[leftHole-1];
        vArray[leftHole] 
          = vArray[middleHole-1];
        if ( comparator.compare 
             ( vR , v ) <= 0 ) {
          vArray[middleHole] 
            = vArray[rightHole-1];
          vArray[rightHole] = v;
          --rightHole;
        } else {
          vArray[middleHole] = v;
        }
        --middleHole;
        --rightHole;
      } else if ( comparator.compare 
                  ( vP , v ) <= 0 ) {
        vArray[scan] 
          = vArray[leftHole-1];
        vArray[leftHole] = v;
        --leftHole;
      }
    }
    vArray[leftHole]   = vP;
    vArray[middleHole] = vQ;
    vArray[rightHole]  = vR;
    return new int[] 
    { start, leftHole
    , leftHole+1, middleHole
    , middleHole+1, rightHole
    , rightHole+1, stop };
  }  
}


