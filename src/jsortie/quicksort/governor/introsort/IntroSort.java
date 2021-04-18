package jsortie.quicksort.governor.introsort;

import jsortie.RangeSorter;
import jsortie.quicksort.governor.QuicksortBase;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;

public class IntroSort 
  extends QuicksortBase {
  protected RangeSorter lastResort;
  public IntroSort
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
    + "(" + selector.toString()    + "," + partitioner.toString()
    + "," + janitor.toString() + "," + janitorThreshold 
    + "," + lastResort.toString()  + ")";
  }
  @Override
  public void sortRange
    ( int [] vArray, int start, int stop ) {
    if (start+1<stop) {
      int w = (int)(Math.log(stop-start)*3/Math.log(2));
      sortRange(vArray, start, stop, w);
    }
  }	
  public void sortRange
    ( int [] vArray, int start, int stop, int maxDepth ) {
    while ( janitorThreshold  < stop - start ) {
      --maxDepth;
      if (maxDepth<1) {
        lastResort.sortRange(vArray, start, stop);
        return;
      } else {
        int pivotIndex = selector.selectPivotIndex
                         ( vArray, start, stop);
        pivotIndex     = partitioner.partitionRange
                         ( vArray, start, stop, pivotIndex);
        if (pivotIndex < start + (stop-start)/2) {
          sortRange( vArray, start, pivotIndex, maxDepth);
          start = pivotIndex + 1;
        } else {
          sortRange( vArray, pivotIndex+1, stop, maxDepth );
          stop = pivotIndex;
        }
      }
    }
    janitor.sortRange(vArray, start, stop);
  }
}
