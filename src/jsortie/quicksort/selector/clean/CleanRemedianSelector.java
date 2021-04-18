package jsortie.quicksort.selector.clean;

import jsortie.quicksort.selector.grubby.RemedianSelectorBase;

public class CleanRemedianSelector 
  extends RemedianSelectorBase 
  implements CleanSinglePivotSelector {
  public CleanRemedianSelector 
    ( boolean uniform ) {
    super(uniform );
  }
  @Override
  public int medianOf3Candidates
    ( int [] vArray
    , int a, int b, int c) {
    if (vArray[a]<=vArray[b]) 
      if (vArray[b]<=vArray[c]) 
        return b;
      else if (vArray[a]<=vArray[c])
        return c;
      else 
        return a;
    else if (vArray[c]<vArray[b])
      return b;
    else if (vArray[c]<vArray[a])
      return c;
    else
     return a;
  } 
}
