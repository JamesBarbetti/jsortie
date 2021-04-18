package jsortie.janitors.exchanging;

import java.util.Arrays;

import jsortie.janitors.insertion.twoway.InsertionSort2Way;

public class BranchAvoidingAlternatingCombsort 
  extends AlternatingCombsort {
  public BranchAvoidingAlternatingCombsort() {
	super();
    this.insertionSort = new InsertionSort2Way();
  }
  public BranchAvoidingAlternatingCombsort(double ratio) {
    super(ratio);
    this.insertionSort = new InsertionSort2Way();
  }
  public BranchAvoidingAlternatingCombsort(int[] gapsToUse) {
    super(gapsToUse);
  }
  public BranchAvoidingAlternatingCombsort(double ratio, int maxGap) {
    super(ratio);
    for (int s=0; s<gaps.length; ++s) {
      if (maxGap < gaps[s]) {
        gaps = Arrays.copyOf(gaps, s);
        return;
      }
    }
  }
  @Override
  public void leftToRightCombsortPass
    ( int[] vArray, int start, int stop, int gap) {
    for (int i=start, j=start+gap; j<stop; ++i, ++j) {
      int     vLeft  = vArray[i];
      int     vRight = vArray[j];
      boolean swap   = (vRight < vLeft);
      vArray[i]      = swap ? vRight : vLeft; 
      vArray[j]      = swap ? vLeft  : vRight; 
    }
  }
  @Override
  public void rightToLeftCombsortPass 
    ( int[] vArray, int start, int stop, int gap) {
    for (int i=stop-1-gap, j=stop-1; start<=i; --i, --j) {
      int     vLeft  = vArray[i];
      int     vRight = vArray[j];
      boolean swap   = (vRight < vLeft);
      vArray[i]      = swap ? vRight : vLeft; 
      vArray[j]      = swap ? vLeft  : vRight; 
    }
  }
}
