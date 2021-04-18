package jsortie.quicksort.selector.dirty;

import jsortie.quicksort.selector.grubby.RemedianSelectorBase;

public class DirtyRemedianSelector 
  extends RemedianSelectorBase {
  public DirtyRemedianSelector 
    ( boolean uniform) {
    super(uniform );
  }
  @Override
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
}
