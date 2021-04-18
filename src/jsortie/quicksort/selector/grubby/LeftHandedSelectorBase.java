package jsortie.quicksort.selector.grubby;

public abstract class LeftHandedSelectorBase
  extends RemedianSelectorBase {
  protected abstract int selectLeafCandidate
    ( int[] vArray, int start, int count );
  protected abstract int selectLeafCandidatePositionally
    ( int[] vArray, int start
    , int elementCount, int candidateCount);
  public LeftHandedSelectorBase
    ( boolean isUniform ) {
    super(isUniform);
  }
  @Override
  public int getRangeStart
    ( int start, int stop, int c ) {
    int base = start + (stop - start)/6 - c / 2;
    return (base<start) ? start : base;
  }
  @Override
  public int selectPivotIndex
    ( int [] vArray, int start, int stop ) {
    int count = stop - start;
    if ( count < 32 )         {
      return start + count/2;
    } else if ( count < 108 )   { 
      return selectCandidateFromRange
             ( vArray, start + (count-4)/2,   4   );
    } else if ( count < 972 )   { 
      return selectCandidateFromRange
             ( vArray, start + (count-12)/2,  12  );
    } else if ( count < 8748 )  { 
      return selectCandidateFromRange
             ( vArray, start + (count-36)/2,  36  );
    } else if ( count < 78732 ) {
      return selectCandidateFromRange
             ( vArray, start + (count-108)/2, 108 ); 
    } else {
      int m = 324;
      int m_squared_times_9 = 708588;
      while ( m_squared_times_9 <  count ) {
        m                 *= 3;
        m_squared_times_9 *= 9;
      }
      return selectCandidateFromRange
             ( vArray, start + (count - m ) / 2, m );
    }
  }
  @Override
  public int selectCandidateFromRange
    ( int [] vArray, int start, int count ) {
    if (4<count) {
      int a = selectCandidateFromRange
              ( vArray, start,               count/3);
      int b = selectCandidateFromRange
              ( vArray, start+count/3,       count/3);
      int c = selectCandidateFromRange
              ( vArray, start+count-count/3, count/3);
      return medianOf3Candidates(vArray, a, b, c);
    } else {
      return selectLeafCandidate(vArray, start, count);
    }
  }
  @Override
  public int selectCandidatePositionally 
    ( int [] vArray, int start
    , int elementCount, int candidateCount) {
    if (4<candidateCount) {
      int a = selectCandidatePositionally 
              ( vArray, start
              , elementCount/3, candidateCount/3);
      int b = selectCandidatePositionally
              ( vArray, start+elementCount/3
              , elementCount/3, candidateCount/3);
      int c = selectCandidatePositionally
              ( vArray, start+2*elementCount/3
              , elementCount/3, candidateCount/3);
      return medianOf3Candidates(vArray, a, b, c);
    } else {
      return selectLeafCandidatePositionally
             ( vArray, start, elementCount, candidateCount );
    }
  }
}