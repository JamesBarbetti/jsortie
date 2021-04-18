package jsortie.quicksort.selector.dirty;

public class NoShitDirtyTukeySelector
  extends NoShitSingletonSelector {
  @Override
  public int selectPivotIndex
    ( int [] vArray, int start, int stop ) {
    int count = stop - start;
    if (count < 27) {
      return super.selectPivotIndex
             ( vArray, start, stop );
    }
    int halfCount = count >> 1;
    int k  = count / 10;
    int k3 = k + k + k;
    int b  = start + halfCount;
    int a  = start - k;
    int c  = start + k;
    a = medianOf3Candidates
        ( vArray, a - k3, a, a + k3);
    b = medianOf3Candidates
        ( vArray, b - k3, b, b + k3);
    c = medianOf3Candidates
        ( vArray, c - k3, c, c + k3);
    return   medianOf3Candidates
             ( vArray, a, b, c );
  }
}
