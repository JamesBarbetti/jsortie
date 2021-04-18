package jsortie.quicksort.sort.decorator;

import jsortie.RangeSorter;

public class SlamTiltSort 
  extends AnimalFarmSort
  implements RangeSorter {
  protected RangeSorter degenerateSort;
  public SlamTiltSort
    ( RangeSorter sortToUseOnDegenerateInputs
    , RangeSorter sortToUseOnOtherInputs) {
    super(sortToUseOnOtherInputs);
    degenerateSort = sortToUseOnDegenerateInputs;
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName()
           + "(" + degenerateSort.toString() 
           + "," + innerSort.toString() + ")";
  }
  public RangeSorter chooseSort
    ( int[] vSample, int start, int stop ) {
    int count = stop - start;
    int ascending = 0;
    int equal = 0;
    for (int i=start; i<stop; ++i) {
      int j = start + (int) Math.floor(Math.random() * count);
      int a = (i<j) ? i : j;
      int b = (i<j) ? j : i;
      ascending  += ( vSample[a] < vSample[b] ) ? 1 : 0;
      if (vSample[a]==vSample[b]) {
        ++equal;
      }
    }
    int descending = count - ascending - equal;
    int nineTenths = count*9/10;
    boolean degenerate 
      = ( nineTenths < ascending
          || nineTenths < descending ); 
    return degenerate ? degenerateSort : innerSort;
  }  
}
