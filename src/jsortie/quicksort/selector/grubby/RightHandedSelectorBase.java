package jsortie.quicksort.selector.grubby;

public abstract class RightHandedSelectorBase 
  extends LeftHandedSelectorBase {
  public RightHandedSelectorBase(boolean isUniform) {
    super(isUniform);
  }
  @Override
  public int getRangeStart(int start, int stop, int c) {
    int base = stop - (stop - start)/6 - 1 - c / 2;
    if (stop<base+c) {
      base = stop - c;
    }
    if (base<start) {
      base = start;
    }
    return base;
  }
  @Override
  public int selectCandidateFromRange 
    ( int [] vArray, int start, int count) {
    if (4<count) {
      int a = selectCandidateFromRange(vArray, start,          count/3);
      int b = selectCandidateFromRange(vArray, start+count/3,   count/3);
      int c = selectCandidateFromRange(vArray, start+2*count/3, count/3);
      return medianOf3Candidates(vArray, a, b, c);
    } else {		
      int highest = start;
      for (int candidate = start+1 ; candidate < count+start; ++candidate ) {
        if ( vArray[highest] < vArray[candidate] ) {
          highest = candidate;
        }
      }
      return highest;
    }
  }
}
