package jsortie.quicksort.sort.decorator;

import jsortie.RangeSorter;

public class RandomizedSort extends ScatteredSort {
  public RandomizedSort(RangeSorter sortThatWantsScatteredInput) {
    super(sortThatWantsScatteredInput);
  }
  @Override
  protected void scatter(int[] vArray, int start, int stop) {
	for (int i=start+1; i<stop; ++i) {
      int j
        = start + (int) Math.floor ( Math.random() 
                                     * (i-start+1) );
      int vTemp = vArray[i];
      vArray[i] = vArray[j];
      vArray[j] = vTemp;
    }
  }    
}
