package jsortie.quicksort.governor.expansionist;

import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;

public class MedianPartitioner
  implements SamplePartitioner {
  KthStatisticPartitioner inner;
  public MedianPartitioner
    ( KthStatisticPartitioner medianFinder ) {
    inner = medianFinder;
  }
  @Override
  public int partitionSampleRange
    ( int[] vArray
    , int start, int stop, int targetIndex
    , int sampleStart, int sampleStop) {
    int c = (sampleStop - sampleStart);
    int middle = sampleStart + ( c >> 1);
    inner.partitionRangeExactly
      ( vArray, sampleStart, sampleStop, middle );
    return middle;
  }
}
