package jsortie.quicksort.inplace;

import jsortie.janitors.insertion.OrigamiInsertionSort;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.IntroSelect489Partitioner;
import jsortie.quicksort.partitioner.kthstatistic.floydrivest.FloydRivestPartitioner;

public class FixedPartitionQuicksort 
  extends InPlaceQuicksort {
  double ratio; //must be more than 0.5 and less than 1.0
  public FixedPartitionQuicksort() {
    super(new IntroSelect489Partitioner()
         , 64, new OrigamiInsertionSort());
    ratio = 2.0 / (1.0 + Math.sqrt(5.0));
  }
  public FixedPartitionQuicksort(double t) {
    super(new IntroSelect489Partitioner()
        , 64, new OrigamiInsertionSort());
    setRatio(t);
  }
  public FixedPartitionQuicksort
    ( KthStatisticPartitioner kthStat ) {
    super ( new FloydRivestPartitioner()
          , 64, new OrigamiInsertionSort());
    ratio = 2.0 / (1.0 + Math.sqrt(5.0));
  }
  public FixedPartitionQuicksort
    ( double t, KthStatisticPartitioner kStat ) {
    super ( kStat, 64, new OrigamiInsertionSort());
    setRatio(t);
  }
  protected void setRatio(double t) {
    if (t<0.5) {
      t=1.0-t;
    }
    if (1.0<=t) {
      t=0.9;
    }
    ratio = t;
  }
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    while (janitorThreshold<=stop-start) {
      int m = stop - start;
      int k = (int)Math.floor( start + (m-2)*ratio + 1.5 );
      kthStatisticPartitioner.partitionRangeExactly
      ( vArray, start, stop, k );
      sortRange(vArray, k+1, stop);
      stop = k;
    }
    janitor.sortRange(vArray, start, stop);
  }
}
