package jsortie.quicksort.selector.dirty;

public class LessShitSingletonSelector 
  extends DirtySingletonSelector {
  @Override
  public int selectPivotIndex
    ( int [] vArray, int start, int stop ) {
    if (stop-start<5) {
      return start + (stop-start)/2;
    }
    return medianOf3Candidates
           ( vArray, start+1
            , start + (stop - start) / 2, stop-2 );
  }
}
