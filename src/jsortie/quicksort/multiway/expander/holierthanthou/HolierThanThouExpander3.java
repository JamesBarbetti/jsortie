package jsortie.quicksort.multiway.expander.holierthanthou;

public class HolierThanThouExpander3
  extends HolierThanThouExpanderBase {
  public HolierThanThouExpander3() {
    super(3);
  }
  @Override
  public int[] expandPartitionsToRight 
    ( int[] vArray, int start
    , int[] pivotIndices, int startRight
    , int stop) {
    int leftHole   = pivotIndices[0];
    int middleHole = pivotIndices[1];
    int rightHole  = pivotIndices[2]; //3rd hole
    int vP = vArray[leftHole];
    int vQ = vArray[middleHole];
    int vR = vArray[rightHole]; //3rd pivot
    for ( int scan=startRight
        ; scan<stop; ++scan) {
      int v = vArray[scan];
      if ( v < vQ ) {
        vArray[scan] 
          = vArray[rightHole+1];
        vArray[rightHole]
           = vArray[middleHole+1];
        if ( v < vP ) {
          vArray[middleHole] 
            = vArray[leftHole+1];
          vArray[leftHole]   = v;
          ++leftHole;
        } else {
          vArray[middleHole] = v;
        }
        ++middleHole;
        ++rightHole;
      } else if ( v < vR ) {
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
    ( int[] vArray, int start
    , int stopLeft, int[] pivotIndices
    , int stop) {
    int leftHole   = pivotIndices[0];
    int middleHole = pivotIndices[1];
    int rightHole  = pivotIndices[2]; //3rd hole
    int vP = vArray[leftHole];
    int vQ = vArray[middleHole];
    int vR = vArray[rightHole];  //third pivot
    for ( int scan=stopLeft-1
        ; start<=scan; --scan ) {
      int v = vArray[scan];
      if ( vQ <= v ) {
        vArray[scan] 
          = vArray[leftHole-1];
        vArray[leftHole] 
          = vArray[middleHole-1];
        if ( vR <= v ) {
          vArray[middleHole] 
            = vArray[rightHole-1];
          vArray[rightHole] = v;
          --rightHole;
        } else {
          vArray[middleHole] = v;
        }
        --middleHole;
        --rightHole;
      } else if ( vP <= v ) {
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
