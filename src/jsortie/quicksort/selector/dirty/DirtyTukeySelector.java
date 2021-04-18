package jsortie.quicksort.selector.dirty;

public class DirtyTukeySelector
  extends DirtySingletonSelector {
  @Override
  public int selectPivotIndex
    ( int [] vArray, int start, int stop ) { 
    if (stop-start<27) {
      return medianOf3Candidates
             ( vArray, start, start + (stop - start) / 2, stop-1);
    } else {
      int k = (stop-start)/8;
      int middle = start + (stop-start)/2;
      int m1 = medianOf3Candidates
              ( vArray, start,     middle-k, stop-1-2*k);
      int m2 = medianOf3Candidates
               ( vArray, start+1*k, middle,   stop-1-k);
      int m3 = medianOf3Candidates
               ( vArray, start+2*k, middle+k, stop-1);
      return   medianOf3Candidates
               ( vArray, m1,        m2,       m3);
    }
  }
}
