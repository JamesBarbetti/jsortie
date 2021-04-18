package jsortie.quicksort.inplace;

import jsortie.RangeSorter;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.heap.KislitsynPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.quickselect.SamplingQuickSelectPartitioner;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.selector.dirty.DirtySingletonSelector;

public class SamplingInPlaceQuicksort
  extends InPlaceQuicksort {
  public SamplingInPlaceQuicksort
    ( SampleSizer sizer, SampleCollector collector
    , SinglePivotPartitioner leftPartitioner
    , SinglePivotPartitioner rightPartitioner
    , int threshold
    , RangeSorter janitor) {
    super ( new DirtySingletonSelector()
          , leftPartitioner, rightPartitioner
          , threshold, janitor);
    this.kthStatisticPartitioner 
      = new SamplingQuickSelectPartitioner
            ( collector, leftPartitioner
            , rightPartitioner, threshold
            , new KislitsynPartitioner() );
  }
}
