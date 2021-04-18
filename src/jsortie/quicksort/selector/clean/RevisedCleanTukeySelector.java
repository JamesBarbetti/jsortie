package jsortie.quicksort.selector.clean;

public class RevisedCleanTukeySelector
  extends CleanSingletonSelector {
  @Override
  public int selectPivotIndex
    ( int [] vArray, int start, int stop ) {
    if (stop<start+9) {
      return super.selectPivotIndex
             ( vArray, start, stop );
    }
    int count  = (stop-start);
    int step   = (count/10);
    int b      = start + count/2;
    int a      = b - step - step - step;
    int c      = b + step + step + step;
    a = medianOf3Candidates 
        ( vArray, a-step, a, a+step);
    b = medianOf3Candidates 
        ( vArray, b-step, b, b+step);
    c = medianOf3Candidates 
        ( vArray, c-step, c, c+step);
    return medianOf3Candidates  
           ( vArray, a, b, c );
  }
}
