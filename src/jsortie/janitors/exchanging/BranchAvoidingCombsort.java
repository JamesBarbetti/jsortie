package jsortie.janitors.exchanging;

import jsortie.janitors.insertion.twoway.InsertionSort2Way;

public class BranchAvoidingCombsort
  extends Combsort {
  public BranchAvoidingCombsort() {
    super();
    this.insertionSort 
      = new InsertionSort2Way();
  }
  public BranchAvoidingCombsort
    ( double ratio ) {
    super(ratio);
    this.insertionSort 
      = new InsertionSort2Way();
  }
  @Override 
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    int count = stop - start;
    int gapIndex = 0;
    while( gapIndex+1<gaps.length
           && gaps[gapIndex+1] < count ) {
      ++gapIndex;
    }
    for ( ; 0 <= gapIndex; --gapIndex ) {
      int gap = gaps[gapIndex];
      int i   = start;
      int j   = start+gap; 
      for (; j<stop; ++i, ++j) {
        int     vLeft  = vArray[i];
        int     vRight = vArray[j];
        boolean swap   = (vRight < vLeft);
        vArray[i]      = swap ? vRight : vLeft; 
        vArray[j]      = swap ? vLeft  : vRight;
      }
    }
    insertionSort.sortRange(vArray, start, stop);
  }
}
