package jsortie.mergesort.timsort;

import jsortie.StableRangeSorter;

public class IntegerTimsortWrapper implements StableRangeSorter {
  @Override
  public String toString() { 
    return this.getClass().getSimpleName(); 
  }	
  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    IntegerTimSort.sort(vArray, start, stop);
  }
}
