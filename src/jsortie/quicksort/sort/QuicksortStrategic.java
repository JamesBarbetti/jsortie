package jsortie.quicksort.sort;

import jsortie.RangeSorter;
import jsortie.quicksort.governor.QuicksortGovernor;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;

public class QuicksortStrategic
  extends QuicksortGovernor
{
  public QuicksortStrategic
    ( SinglePivotSelector selector
    , SinglePivotPartitioner partitioner
    , RangeSorter rangeSorter, int janitorThresholdToUse) {
    super(selector, partitioner, rangeSorter, janitorThresholdToUse);
  }
  public void sortSubRange
    ( int [] vArray, int start, int stop ) {
    int pivotIndex;
    while ( janitorThreshold < stop - start ) {
      pivotIndex = selector.selectPivotIndex( vArray, start, stop);
      pivotIndex = partitioner.partitionRange( vArray, start, stop, pivotIndex);
      if (pivotIndex < start + (stop-start)/2) {
        sortRange( vArray, start, pivotIndex);
        start = pivotIndex + 1;
      } else {
        sortRange( vArray, pivotIndex+1, stop );
        stop = pivotIndex;
      }
    }
  }
  public void sortRange
    ( int [] vArray, int start, int stop ) {
    sortSubRange(vArray, start, stop);
    janitor.sortRange(vArray, start, stop);
  }
  public void sortArray(int [] vArray) {
    sortRange(vArray, 0, vArray.length);
  }
}
