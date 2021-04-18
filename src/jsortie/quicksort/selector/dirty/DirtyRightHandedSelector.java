package jsortie.quicksort.selector.dirty;

import jsortie.quicksort.selector.grubby.RightHandedSelectorBase;

public class DirtyRightHandedSelector
  extends RightHandedSelectorBase {  
  public DirtyRightHandedSelector(boolean isUniform) {
    super(isUniform);
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
  @Override
  protected int selectLeafCandidate
    ( int[] vArray, int start, int count ) {
    int iLast = start + count - 1;
    if (1<count) {
      //bubble the maximum up to last element 
      //of the range
      int vHighest = vArray[start];
      for ( int scan = start+1; scan<=iLast; ++scan ) {
        int vCandidate = vArray[scan]; 
        if ( vHighest < vCandidate ) {
          vArray[scan-1] = vHighest;
          vHighest = vCandidate;
        } else {
          vArray[scan-1] = vCandidate;
        }
      }
      vArray[iLast] = vHighest;
    }
    return iLast;
  }
  @Override
  protected int selectLeafCandidatePositionally
    ( int[] vArray, int start
    , int elementCount, int candidateCount ) {
    int iStep = (elementCount + 1) 
              / (candidateCount + 1);
    if (iStep<1) {
      return selectLeafCandidate 
             ( vArray, start, elementCount );
    }
    if (1<candidateCount) {
      //bubble the maximum up to 
      //the last element in the slice
      int iStop    = start+elementCount;
      int iFirst   = iStop-1-(iStep/2);
      int vHighest = vArray[iFirst];
      int iScan    = iFirst+iStep;
      for ( ; iScan<iStop; iScan+=iStep ) {
        int vCandidate = vArray[iScan]; 
        if ( vHighest < vCandidate ) {
          vArray[iScan-iStep] = vHighest;
          vHighest = vCandidate;
        } else {
          vArray[iScan-iStep] = vCandidate;
        }
      }
      iScan -= iStep;
      vArray[iScan] = vHighest;
      return iScan;
    }
    return start + elementCount - 1;
  }
}
