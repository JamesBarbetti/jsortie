package jsortie.quicksort.governor.introsort;

import jsortie.RangeSorter;
import jsortie.quicksort.governor.QuicksortBase;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;

public class EnteroSort 
  extends QuicksortBase {
  final static double ln2 = Math.log(2.0);
  protected RangeSorter lastResort;
  public EnteroSort
    ( SinglePivotSelector selector
    , SinglePivotPartitioner partitioner
    , RangeSorter janitor, int janitorThreshold
    , RangeSorter sortOfLastResort) {
    super ( selector , partitioner, janitor
          , janitorThreshold);
    lastResort = sortOfLastResort;
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName()
    + "(" + selector.toString() 
    + "," + partitioner.toString()
    + "," + janitor.toString() 
    + "," + janitorThreshold 
    + "," + lastResort.toString()  + ")";
  }
  public double getComparisonLimit(int m) {
    return (Math.log(m)-Math.log(janitorThreshold))
      * m * 3 / ln2;
  }
  public double getComparisonSpend
    ( int start, int pivot, int stop ) {
    return stop - start - 1;
  }
  @Override
  public void sortRange
    ( int [] vArray, int start, int stop ) {
    if (start+1<stop) {
      int    m = stop - start;
      double w = getComparisonLimit(m);
      sortSubRange
        (vArray, start, stop, w);
    }
  }
  public double sortSubRange
    ( int[] vArray, int start, int stop
    , double maxComparisons) {
    for ( int m=stop-start; janitorThreshold<m
        ;  m=stop-start ) {
      int pivotIndex  = selector.selectPivotIndex
                        ( vArray, start, stop);
      pivotIndex      = partitioner.partitionRange
                        ( vArray, start, stop, pivotIndex);
      maxComparisons -= getComparisonSpend
                        ( start, pivotIndex, stop );
      if (pivotIndex < start + (m>>1)) {
        maxComparisons 
          = sortSubRange
            ( vArray, start, pivotIndex
            , maxComparisons);
        start = pivotIndex + 1;
      } else {
        maxComparisons 
          = sortSubRange
            ( vArray, pivotIndex+1, stop
            , maxComparisons);
        stop = pivotIndex;
      }
      if ( maxComparisons < 0 ) {
        lastResort.sortRange(vArray, start, stop);
        return maxComparisons;
      }
    } 
    janitor.sortRange(vArray, start, stop);
    return maxComparisons;
  }
}
