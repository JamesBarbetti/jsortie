package jsortie.quicksort.selector.reselector;

import jsortie.quicksort.selector.dirty.DirtySingletonSelector;

public class STLTukeyReselector
   extends DirtySingletonSelector
  implements SinglePivotReselector {
  @Override
  public int selectPivotIndexGivenHint
    ( int[] vArray, int start
    , int hint, int stop) {
    if (stop-start<40) {
      return medianOf3Candidates
             ( vArray, start, hint, stop-1);
    } else {
      int step = (stop-start)/8;
      int last = stop-1;
      medianOf3Candidates(vArray, start,           start + step, start + 2 * step);
      medianOf3Candidates(vArray, hint - step,     hint,         hint + step);
      medianOf3Candidates(vArray, last - 2 * step, last - step,  last);
      medianOf3Candidates(vArray, start + step,    hint,         last - step);
      return hint;
    }
    
  }


}
