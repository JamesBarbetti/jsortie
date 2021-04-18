package jsortie.quicksort.selector.dirty;

import jsortie.quicksort.selector.SinglePivotSelector;

public class DirtySingletonSelector 
  implements SinglePivotSelector {	
  @Override
  public String toString() { 
    return this.getClass().getSimpleName(); 
  }
  protected int medianOf3Candidates
    ( int [] vArray
    , int lo, int middle, int hi ) {
    int vA = vArray[lo];
    int vB = vArray[middle];
    int vC = vArray[hi];
    if ( vA <= vB ) {
      if ( vB <= vC ) { //ABC
        return middle;
      } else if ( vA <= vC ) { //ACB
        vArray[middle] = vC;
        vArray[hi]     = vB;
      } else { //CAB
        vArray[lo] = vC;
        vArray[middle] = vA;
        vArray[hi] = vB;
      }
    } else if ( vC < vB ) { //CBA
      vArray[lo] = vC;
      vArray[hi] = vA;
    } else if ( vA <= vC) { //BAC
      vArray[lo]=vB;
      vArray[middle]=vA;
    } else { //BCA
      vArray[lo]=vB;
      vArray[middle]=vC;
      vArray[hi]=vA;
    }
    return middle;
  }
  @Override
  public int selectPivotIndex
    ( int [] vArray, int start, int stop ) {
    int middle = start + (stop-start)/2;
    --stop;
    int vA = vArray[start];
    int vB = vArray[middle];
    int vC = vArray[stop];
    if ( vA <= vB ) {
      if ( vB <= vC ) { //ABC
        return middle;
      } else if ( vA <= vC ) { //ACB
        vArray[middle] = vC;
        vArray[stop]   = vB;
      } else { //CAB
        vArray[start]  = vC;
        vArray[middle] = vA;
        vArray[stop]   = vB;
      }
    } else if ( vC < vB ) { //CBA
      vArray[start] = vC;
      vArray[stop]  = vA;
    } else if ( vA <= vC) { //BAC
      vArray[start]  = vB;
      vArray[middle] = vA;
    } else { //BCA
      vArray[start]  = vB;
      vArray[middle] = vC;
      vArray[stop]   = vA;
    }
    return middle;
  }
}
