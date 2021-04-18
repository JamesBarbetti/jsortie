package jsortie.quicksort.selector.grubby;

import jsortie.quicksort.selector.SinglePivotSelector;

public abstract class RemedianSelectorBase 
  implements SinglePivotSelector {
  protected boolean uniform;
  protected abstract int medianOf3Candidates
                         (int[] vArray, int a, int b, int c);
  @Override 
  public String toString() { 
    return this.getClass().getSimpleName()
      + (uniform ? "(uniform)" : "(non-uniform)");
  }
  public RemedianSelectorBase
    ( boolean isUniform ) {
    this.uniform = isUniform;
  }
  @Override
  public int selectPivotIndex
    ( int [] vArray, int start, int stop ) {
    int count = stop - start;
    if ( count < 10 ) { 
      return start + count/2; 
    }
    int c;
    if      ( count < 81 )    c=3;
    else if ( count < 729 )   c=9;
    else if ( count < 6561 )  c=27;
    else if ( count < 59049 ) c=81;
    else {
      c = 243;
      int m_squared_times_9 = 531441;
      while ( m_squared_times_9 <  count ) {
        c                 *= 3;
        m_squared_times_9 *= 9;
      }
    }
    return uniform
      ? selectCandidatePositionally( vArray, start, count, c)
      : selectCandidateFromRange( vArray, 
                                  getRangeStart(start, stop, c), c );
  }
  public int getRangeStart(int start, int stop, int c) {
    return start + (stop-start-c)/2;
  }
  public int selectCandidateFromRange ( int [] vArray, int start
                                      , int count) {
    //assumed: count is power(3,k), some integer k >= 0; 
    //see selectPivotIndex.
    if (3<count) {
      int a = selectCandidateFromRange(vArray, start, count/3);
      int b = selectCandidateFromRange(vArray, start+count/3, count/3);
      int c = selectCandidateFromRange(vArray, start+2*count/3, count/3);
      return medianOf3Candidates(vArray, a, b, c);
    } else if (count==3) {
      return medianOf3Candidates(vArray, start, start+1, start+2 );
    } else {
      return start + (count)/2;
    }
  }
  public int selectCandidatePositionally ( int [] vArray, int start
                                         , int elementCount
                                         , int candidateCount) {
    if (3<candidateCount) {
      int a = selectCandidatePositionally 
              ( vArray, start, elementCount/3, candidateCount/3);
      int b = selectCandidatePositionally 
              ( vArray, start+elementCount/3
              , elementCount/3, candidateCount/3);
      int c = selectCandidatePositionally 
              ( vArray, start+2*elementCount/3
              , elementCount/3, candidateCount/3);
      return medianOf3Candidates(vArray, a, b, c);
    } else if (candidateCount==3) {
      return medianOf3Candidates ( vArray, start+elementCount/4
                                 , start+2*elementCount/4
                                 , start+3*elementCount/4 );
    } else {
      return start + (elementCount)/2;
    }
  }
}
