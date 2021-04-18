package jsortie.quicksort.selector.clean;

public class CleanTukeySelector 
  extends CleanSingletonSelector {
  @Override
  public int selectPivotIndex
    ( int [] vArray, int start, int stop ) {
    if (stop<start+9) {
      return super.selectPivotIndex
             ( vArray, start, stop );
    }
    int count   = (stop-start);
    int last    = stop - 1;
    int eighth  = count/8;
    int quarter = count/4;
    int middle  = start + (count/2);
    int a = medianOf3Candidates 
            ( vArray, start
            , start+eighth, start+quarter );
    int b = medianOf3Candidates 
            ( vArray, middle - eighth
            , middle, middle + eighth );
    int c = medianOf3Candidates 
            ( vArray, last - quarter
            , last - eighth, last );
    return medianOf3Candidates  
           ( vArray, a, b, c );
  }
}
