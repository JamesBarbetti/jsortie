package jsortie.quicksort.selector.dirty;

import jsortie.quicksort.selector.grubby.LeftHandedSelectorBase;

public class DirtyLeftHandedSelector
  extends LeftHandedSelectorBase {
  public DirtyLeftHandedSelector
    ( boolean isUniform ) {
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
    if (1<count) {
      //bubble the minimum to start of range
      int vLowest = vArray[start+count-1];
      int iLast   = start + count - 1;
      for ( int iScan = iLast - 1;
            iScan >= start; --iScan ) {
        int vCandidate = vArray[iScan]; 
        if ( vCandidate < vLowest ) {
          vArray[iLast] = vLowest;
          vLowest = vCandidate;
        } else {
          vArray[iLast] = vCandidate;
        }
        iLast = iScan;
      }
      vArray[start] = vLowest;
    }
    return start;
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
      //bubble the minimum to first item
      //in the slice
      int iLast   = start
                  + elementCount-1-(iStep/2);
      int vLowest = vArray[iLast];
      int iScan   = iLast - iStep;
      for ( ; start <= iScan
            ; iScan -= iStep ) {
        int vCandidate = vArray[iScan]; 
        if ( vCandidate < vLowest ) {
          vArray [ iLast ] = vLowest;
          vLowest = vCandidate;
        } else {
          vArray [ iLast ] = vCandidate;
        }
        iLast = iScan;
      }
      vArray[iLast] = vLowest;
      return iLast;
    }
    return start;
  }
}
