package jsortie.quicksort.samplesort;

import jsortie.RangeSorter;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.indexselector.PositionalIndexSelector;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.samplesizer.OversamplingSampleSizer;

public class AsymmetricSampleSort 
  extends InternalSampleSort {
  public AsymmetricSampleSort 
    ( OversamplingSampleSizer sampleSizer
    , SampleCollector sampleCollector
    , SinglePivotPartitioner partitioner
    , RangeSorter janitor, int threshold)  {
    super(sampleSizer, sampleCollector, partitioner, janitor, threshold);
    selector = new PositionalIndexSelector(new int[]{1},8);
  }
}
