package jsortie.quicksort.multiway.selector.dirty;

import jsortie.quicksort.collector.PositionalSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.indexselector.IndexSelector;
import jsortie.quicksort.indexselector.PositionalIndexSelector;
import jsortie.quicksort.multiway.partitioner.kthstatistic.Algorithm489MultiPartitioner;
import jsortie.quicksort.multiway.partitioner.kthstatistic.KthStatisticMultiPartitioner;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;

public class DirtyCompoundMultiPivotSelector 
  implements MultiPivotSelector {
  int                          radix;
  int                          pivotCount;
  SampleSizer                  sizer;
  SampleCollector              collector;
  IndexSelector                indexSelector;
  KthStatisticMultiPartitioner partialSorter;
  int                          threshold;
  MultiPivotSelector           smallRangeSelector;

  public DirtyCompoundMultiPivotSelector
    ( int                          pivotCountToUse
    , SampleSizer                  sampleSizer
    , SampleCollector              sampler
    , IndexSelector                indexSelectorToUse
    , KthStatisticMultiPartitioner kthStatPartitioner
    , int                          thresholdToUse
    , MultiPivotSelector           smallRangeSelectorToUse ) {
    radix              = 2;
    pivotCount         = pivotCountToUse;
    sizer              = sampleSizer;
    sampler            = collector;
    indexSelector      = indexSelectorToUse;
    partialSorter      = kthStatPartitioner;
    threshold          = thresholdToUse;
    smallRangeSelector = smallRangeSelectorToUse;
  }
  public DirtyCompoundMultiPivotSelector
  ( int              pivotCountToUse
  , SampleSizer      sampleSizer
  , SampleCollector  sampleCollector) {
    radix              = 2;
    pivotCount         = pivotCountToUse;
    sizer              = sampleSizer;
    collector          = sampleCollector;
    indexSelector      
      = new PositionalIndexSelector
            ( pivotCountToUse );
    partialSorter      
      = new Algorithm489MultiPartitioner();
    threshold          = 64;
    smallRangeSelector 
      = new DirtyMultiPivotPositionalSelector
            ( pivotCountToUse );
  }
  public DirtyCompoundMultiPivotSelector
    ( int pivotCountToUse ) {
    radix              = 2;
    pivotCount         = pivotCountToUse;
    sizer
      = new SquareRootSampleSizer(0);
    collector
      = new PositionalSampleCollector();
    indexSelector 
      = new PositionalIndexSelector
            ( pivotCountToUse );
    partialSorter
      = new Algorithm489MultiPartitioner();
    threshold          = 64;
    smallRangeSelector 
      = new DirtyMultiPivotPositionalSelector
            ( pivotCountToUse );
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + pivotCount + "," + sizer.toString()
      + "," + collector.toString()
      + "," + indexSelector.toString()
      + "," + partialSorter.toString()
      + "," + smallRangeSelector.toString() + ")";
  }  
  @Override
  public int[] selectPivotIndices 
    ( int[] vArray, int start, int stop) {
    int count = stop - start;
    if ( count <= threshold) {
      return smallRangeSelector.selectPivotIndices
             (vArray, start, stop);
    }
    int sampleSize 
      = sizer.getSampleSize
        (stop-start, radix);
    int sampleStop = start + sampleSize;
    collector.moveSampleToPosition
    (vArray, start, stop, start, sampleStop);
    int[] indices  
      = indexSelector.selectIndices
        (start, sampleStop, pivotCount);
    partialSorter.partitionRangeExactly
      ( vArray, start, sampleStop, indices );
    return indices;
  }
}
