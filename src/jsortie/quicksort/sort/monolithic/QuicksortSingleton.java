package jsortie.quicksort.sort.monolithic;

import jsortie.RangeSorter;
import jsortie.helper.RangeSortHelper;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class QuicksortSingleton implements RangeSorter {
  protected SinglePivotPartitioner partitioner;
  protected RangeSorter            janitor;
  protected int                    threshold;
  public QuicksortSingleton(SinglePivotPartitioner partitionerToUse
		  , RangeSorter janitorToUse, int janitorThreshold) {
    partitioner = partitionerToUse;
    janitor     = janitorToUse;
    threshold   = janitorThreshold;
  }
  public String toString() {
    return this.getClass().getSimpleName() + "(" + partitioner.toString() + ")";
  }
  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    while (threshold<stop-start) {
      int middle = start + (stop-start)/2;
      RangeSortHelper.compareAndSwapIntoOrder(vArray, start, middle);
      if (RangeSortHelper.compareAndSwapIntoOrder(vArray, middle, stop-1)) {
        RangeSortHelper.compareAndSwapIntoOrder(vArray, start, middle);
      }
      int pivotIndex = partitioner.partitionRange(vArray, start+1, stop-1, middle);
      if (pivotIndex-start <= stop-pivotIndex) {
        sortRange(vArray, start, pivotIndex);
        start = pivotIndex+1;
      } else {
    	sortRange(vArray, pivotIndex+1, stop);
    	stop = pivotIndex;
      }
    }
    janitor.sortRange(vArray, start, stop);
  }
}
