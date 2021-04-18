package jsortie.quicksort.samplesort;

import jsortie.RangeSorter;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.samplesizer.OversamplingSampleSizer;

public class SillyExternalSampleSort 
  extends ExternalSampleSort {
  public SillyExternalSampleSort
    ( OversamplingSampleSizer sizer
    , SampleCollector sampleCollector
    , RangeSorter janitorSorter, int janitorThreshold) {
    super ( sizer, sampleCollector
          , janitorSorter, janitorThreshold);
  }
  protected void scatter
    ( int[] vArray, int scan, int stop
    , int vPivots[], IntegerBucket buckets[]) {
    int pivotCount = vPivots.length;
    int pivotStop  = pivotCount-10;
    for (; scan<stop; ++scan) {
      int v = vArray[scan];
      int b = 0;
      int b2;
      for (b2=10;b2<pivotStop; b2+=10) { //stepped linear search 
        if (v<vPivots[b2])  {
          b=b2-10;
          break;
        }
      }
      if (pivotCount<b2) b2=pivotCount;
      for (;b<b2 && vPivots[b]<=v; ++b); //linear search
      buckets[b].add(v);
    }
  }
}
