package jsortie.mergesort.vanilla;

import jsortie.StableRangeSorter;
import jsortie.janitors.insertion.InsertionSort;

public class CylonSort extends MergesortBase
{
  int[] vTemp = new int[10000];
  public CylonSort(StableRangeSorter janitor, int threshold) {
    super(janitor, threshold);
  }	
  public void sortRange(int[] vArray, int start, int stop) {
    if (stop-start<80)
      InsertionSort.sortSmallRange(vArray, start, stop);
    else {
      int middle = start + (stop-start) / 2;
      int lhs = middle - 32;
      int rhs = middle + 32;
      InsertionSort.sortSmallRange(vArray, lhs, rhs);
      int step = 16;
      do {
        if (start<lhs) {
          middle = lhs;
          lhs -= step;
          lhs = (lhs<start) ? start : lhs;
          InsertionSort.copyAndSortSmallRange(vArray, lhs, middle, vTemp, 0);
          mergeToLeft(vTemp, 0, middle-lhs, vArray, middle, rhs, lhs);
          step += 4;
        }
        if (rhs<stop) {
          middle = rhs;
          rhs += step;
          rhs = (stop<=rhs) ? stop : rhs;
          InsertionSort.copyAndSortSmallRange(vArray, middle, rhs, vTemp, 0);
          mergeToRight(vTemp, 0, rhs-middle, vArray, lhs, middle, lhs);
          step += 4;
        }
      }
      while (start<lhs || rhs<stop);
    }
  }	
}
