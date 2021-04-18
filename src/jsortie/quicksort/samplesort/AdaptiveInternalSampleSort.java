package jsortie.quicksort.samplesort;

import jsortie.RangeSorter;
import jsortie.earlyexitdetector.RangeSortEarlyExitDetector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.samplesizer.OversamplingSampleSizer;

public class AdaptiveInternalSampleSort 
  extends InternalSampleSort {
  RangeSortEarlyExitDetector eed;
  public AdaptiveInternalSampleSort 
    ( OversamplingSampleSizer sampleSizer
    , SampleCollector sampleCollector
    , SinglePivotPartitioner partitioner
    , RangeSorter janitor
    , int threshold
    , RangeSortEarlyExitDetector detector) {
    super ( sampleSizer, sampleCollector, partitioner
          , janitor, threshold);
    eed = detector;
  }
  protected void sortRange
  ( int[] vArray, int a, int b, int c, int d
  , int   maxDepth) {
    if (a==b && c==d) {
      if (eed.exitEarlyIfSorted(vArray, a, d)) {
        return;
      }
    }
    super.sortRange(vArray, a, b, c, d, maxDepth);
  }  
}
