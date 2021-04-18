package jsortie.quicksort.selector.dirty;

public class NoShitSingletonSelector 
  extends DirtySingletonSelector {
  @Override
  public int selectPivotIndex
    ( int [] vArray, int start, int stop ) {
    int count  = stop - start;
    int middle = start + (count>>1);
    int offset = (count>>2);
    return medianOf3Candidates
           ( vArray,  middle - offset
            , middle, middle + offset );
  }
}
