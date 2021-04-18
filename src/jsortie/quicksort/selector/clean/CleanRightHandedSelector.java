package jsortie.quicksort.selector.clean;

import jsortie.quicksort.selector.grubby.RightHandedSelectorBase;

public class CleanRightHandedSelector
  extends RightHandedSelectorBase
  implements CleanSinglePivotSelector {
  public CleanRightHandedSelector(boolean isUniform) {
    super(isUniform);
  }
  @Override
  public int medianOf3Candidates
    ( int [] vArray
    , int a, int b, int c) {
    if (vArray[a]<=vArray[b]) 
      if (vArray[b]<=vArray[c]) 
        return b;
      else if (vArray[a]<=vArray[c])
        return c;
      else 
        return a;
    else if (vArray[c]<vArray[b])
      return b;
    else if (vArray[c]<vArray[a])
      return c;
    else
     return a;
  } 
  @Override
  protected int selectLeafCandidate
    ( int[] vArray, int start, int count ) {
    int iHighest = start;
    for ( int candidate = start+1;
          candidate < count+start; ++candidate ) {
      if ( vArray[iHighest] < vArray[candidate] ) {
        iHighest = candidate;
      }
    }
    return iHighest;  
  }
  @Override
  protected int selectLeafCandidatePositionally
    ( int[] vArray, int start
    , int elementCount, int candidateCount ) {
    int step = (elementCount + 1) / (candidateCount + 1);
    if (step<1) {
      return selectLeafCandidate(vArray, start, elementCount);
    }
    int stop = start + elementCount;
    int iHighest =start+step/2;
    for ( int candidate=iHighest+step; 
          candidate<stop; candidate+=step) {
      if ( vArray[iHighest] < vArray[candidate] ) {
        iHighest = candidate;
      }
    }
    return iHighest;
  }
}
