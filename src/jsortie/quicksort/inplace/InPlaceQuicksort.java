package jsortie.quicksort.inplace;

import jsortie.RangeSorter;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.quicksort.governor.QuicksortBase;
import jsortie.quicksort.partitioner.insideout.CentripetalPartitioner;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.RangeSorterToKthStatisticPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.quickselect.QuickSelectPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.simple.MiddleElementSelector;

public class InPlaceQuicksort 
  extends QuicksortBase {
  protected KthStatisticPartitioner kthStatisticPartitioner;
  public InPlaceQuicksort () {  	
    super ( new MiddleElementSelector()
          , new CentripetalPartitioner()
          , new InsertionSort2Way(), 64);
    this.kthStatisticPartitioner 
      = new QuickSelectPartitioner
            ( selector, new CentripetalPartitioner()
            , new CentripetalPartitioner(), janitorThreshold
            , new RangeSorterToKthStatisticPartitioner(janitor) );
  }
  public InPlaceQuicksort 
    ( SinglePivotSelector selector
    , SinglePivotPartitioner leftPartitioner
    , SinglePivotPartitioner rightPartitioner
    , int janitorThreshold, RangeSorter rangeSorter) {
    super ( selector, leftPartitioner
          , rangeSorter, janitorThreshold);
    this.kthStatisticPartitioner 
    = new QuickSelectPartitioner
          ( selector, leftPartitioner, rightPartitioner
          , janitorThreshold
          , new RangeSorterToKthStatisticPartitioner(janitor) );
  }
  public InPlaceQuicksort
    ( KthStatisticPartitioner kthStatPartitioner
    , int janitorThreshold, RangeSorter janitor ) {
    super ( new MiddleElementSelector(), new CentripetalPartitioner()
          , janitor, janitorThreshold);
    this.kthStatisticPartitioner = kthStatPartitioner;
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName()
    + "(" + kthStatisticPartitioner.toString()
    + "," + janitor.toString() + "," + janitorThreshold + ")";
  }
  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    double step = (stop-start);
    for (; step>janitorThreshold; step /= 2.0 ) {
      for (double blockStart=start; blockStart<stop; blockStart+=step) {
        int innerStart = (int) Math.floor ( blockStart            + 0.5 );
        int middle     = (int) Math.floor ( blockStart + step/2.0 + 0.5 );
        int innerStop  = (int) Math.floor ( blockStart + step     + 0.5 );
        if (stop<innerStop) {
          innerStop=stop;
        }
        if (middle<stop) {
          if ( start<innerStart && innerStop<stop //Skip if all items ==
               && vArray[innerStart-1] == vArray[innerStop]) {
            continue;
          }
          kthStatisticPartitioner.partitionRangeExactly
          ( vArray, innerStart, innerStop, middle );
        }
      }
    }
    for (double blockStart=start; blockStart<stop; blockStart+=step) {
      int innerStart = (int) Math.floor ( blockStart            + 0.5 );
      int innerStop  = (int) Math.floor ( blockStart + step     + 0.5 );
      if (stop<innerStop) innerStop=stop;
      janitor.sortRange(vArray, innerStart, innerStop);
    }
  }
}
