package jsortie.quicksort.partitioner.kthstatistic.quickselect;

import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.remedian.RemedianPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;

public class IntroSelectPartitioner
  extends QuickSelectPartitioner {
  protected KthStatisticPartitioner lastResort; 
  public IntroSelectPartitioner
    ( SinglePivotSelector s
    , SinglePivotPartitioner leftPartitioner
    , SinglePivotPartitioner rightPartitioner
    , int thresh, KthStatisticPartitioner janitor) {
    super ( s, leftPartitioner, rightPartitioner
          , thresh,janitor);
    lastResort 
      = new RemedianPartitioner();
  }
  public IntroSelectPartitioner
    ( SinglePivotSelector s
    , SinglePivotPartitioner leftPartitioner
    , SinglePivotPartitioner rightPartitioner
    , int thresh, KthStatisticPartitioner janitor
    , KthStatisticPartitioner safetyNet) {
    super ( s, leftPartitioner, rightPartitioner
          , thresh, janitor);
    lastResort = safetyNet;
  }
  protected void despairingPartitionExactly
    ( int[] vArray, int start, int stop
    , int target ) {
    lastResort.partitionRangeExactly
      ( vArray, start, stop, target );
  }
}
