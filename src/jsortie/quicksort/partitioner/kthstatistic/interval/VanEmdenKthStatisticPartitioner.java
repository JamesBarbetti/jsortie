package jsortie.quicksort.partitioner.kthstatistic.interval;

import jsortie.RangeSorter;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.interfaces.StandAlonePartitioner;
import jsortie.quicksort.partitioner.standalone.VanEmdenPartitionerRevised;
import jsortie.quicksort.samplesizer.OneItemSampleSizer;
import jsortie.quicksort.samplesizer.SampleSizer;

public class VanEmdenKthStatisticPartitioner
  implements KthStatisticPartitioner {
  protected SampleSizer           sizer = new OneItemSampleSizer(); 
  protected SampleCollector   collector = new NullSampleCollector();
  protected int        janitorThreshold = 10;
  protected RangeSorter         janitor = new InsertionSort2Way();
  protected StandAlonePartitioner party = new VanEmdenPartitionerRevised();
  public VanEmdenKthStatisticPartitioner() {
  }
  public VanEmdenKthStatisticPartitioner
    ( SampleSizer sampleSizer
    , SampleCollector collectorToUse ) {
    sizer = sampleSizer;
    collector = collectorToUse;
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + collector.toString() + ")";
  }
  @Override
  public void partitionRangeExactly
    (int[] vArray, int start, int stop, int targetIndex) {
    while ( janitorThreshold < stop-start ) {
      fold(vArray, start, stop);
      int sampleSize  = sizer.getSampleSize(stop-start, 2);
      int sampleStart = start + ( stop - start - sampleSize ) / 2;
      int sampleStop  = sampleStart + sampleSize;
      collector.moveSampleToPosition(vArray, start, stop, sampleStart, sampleStop);
      double sampleTargetFloat = sampleStart 
        + (double) ( targetIndex - start       )
        * (double) ( sampleStop  - sampleStart )
        / (double) ( stop        - start       );
      int sampleTarget = (int) Math.floor( sampleTargetFloat );
      partitionRangeExactly( vArray, sampleStart, sampleStop, sampleTarget );
      int[] partitions = party.multiPartitionRange(vArray, start, stop);
      int p = 0;
      for (; p<partitions.length; p+=2) {
        if (targetIndex<partitions[p]) {
          return;
        } else if (targetIndex<partitions[p+1]) {
          start = partitions[p];
          stop  = partitions[p+1];
          break;
        }
      }
      if (p==partitions.length) {
        return;
      }
    }
    janitor.sortRange(vArray, start, stop);
  }
  public void fold(int[] vArray, int start, int stop) {
    int i = start;
    int j = stop-1;
    for (; i<j; ++i, --j) {
      int vLeft  = vArray[i];
      int vRight = vArray[j];
      vArray[i] = (vLeft<=vRight) ? vLeft  : vRight;
      vArray[j] = (vLeft<=vRight) ? vRight : vLeft;
    }
  }
}
