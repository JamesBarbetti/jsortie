package jsortie.mergesort.straight;

import jsortie.StableRangeSorter;
import jsortie.helper.RangeSortHelper;

public class AlternatingStraightMergesort 
  extends StraightMergesort {
  public AlternatingStraightMergesort(StableRangeSorter janitor, int threshold) {
    super(janitor, threshold);
  }	
  @Override
  public void mergeExternal2 ( int left[],  int leftStart,  int leftStop
          , int right[], int rightStart, int rightStop
          , int dest[],  int destStart) {
    mergeExternalRightToLeft ( left, leftStart, leftStop
                  , right, rightStart, rightStop
                  , dest, destStart);    
  }  
  @Override
  public void copyRange2(int[] src, int start, int stop, int[] dest, int w) {
	RangeSortHelper.copyRangeRightToLeft(src, start, stop, dest, w);
  }	
}
