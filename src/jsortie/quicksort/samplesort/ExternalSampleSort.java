package jsortie.quicksort.samplesort;

import jsortie.RangeSorter;
import jsortie.janitors.insertion.BinaryInsertionSort;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.samplesizer.OversamplingSampleSizer;

public class ExternalSampleSort 
  implements RangeSorter
{
  OversamplingSampleSizer sizer;
  SampleCollector collector;
  RangeSorter     janitor;
  int             threshold;
  public ExternalSampleSort
    ( OversamplingSampleSizer sampleSizer 
    , SampleCollector         sampleCollector
    , RangeSorter             janitorSorter
    , int janitorThreshold) {
    this.sizer     = sampleSizer;
    this.collector = sampleCollector;
    this.janitor   = janitorSorter;
    this.threshold = janitorThreshold;
  }
  protected class PivotPlan {
    int pivotCount;
    int sampleSize;
    public PivotPlan
      ( int pivots, int candidates ) {
      pivotCount = pivots;
      sampleSize = candidates;
    }
  }
  protected PivotPlan getPlan
    ( int start, int stop ) {
    int candidates 
      = sizer.getSampleSize 
        ( stop-start, 2 );
    int oversample 
      = sizer.getOverSamplingFactor 
        ( stop-start, 2 );
    int pivots 
      = (candidates+1)/(oversample+1);
    if (pivots<1) {
      pivots=1;
    }
    if (candidates<pivots) {
      candidates=pivots;
    }
    if (stop-start<candidates) {
      candidates=stop-start;
    }
    return new PivotPlan
               ( pivots, candidates );
  }
  protected PivotPlan collectSample
    ( int data[], int start, int stop) {
    PivotPlan plan = getPlan(start, stop);
    collector.moveSampleToPosition
      ( data, start, stop
      , start, start + plan.sampleSize);
    return plan;
  }
  protected class Partition {
    int start;
    int stop;
  }
  protected void maybeSortRange(int[] vArray
    , int start, int stop
    , Partition biggest) {
    if ( stop - start 
         < biggest.start - biggest.stop ) {
      if (start<stop) {
        sortRange(vArray, start, stop);
      }
    } else {
      if (biggest.start<biggest.stop) {
        sortRange 
          (vArray, biggest.start, biggest.stop);
      }
      biggest.start = start;
      biggest.stop  = stop;
    }
  }
  protected void initializeBuckets
    ( int[] vArray
    , int sampleStart, int sampleStop
    , int nodeSize, int oversample
    , int pivots[], IntegerBucket buckets[]) {
    //Calved, to keep sortRange() short.
    int scan=sampleStart;
    int pivotCount = pivots.length;
    int b;
    for (b = 0; b < pivotCount; ++b) {
      buckets[b] = new IntegerBucket(nodeSize);
      scan 
        = buckets[b].receive
          ( vArray, scan, scan+oversample );
      pivots[b]  = vArray[scan];
      ++scan;
    }
    buckets[b] = new IntegerBucket(nodeSize);
    scan = buckets[b].receive
           ( vArray, scan, sampleStop );
  }
  protected void scatter
    ( int[] vArray, int scan, int stop
    , int pivots[], IntegerBucket buckets[]) {
    //Calved, so that it can be overridden 
    //in subclasses.
    //
    //Alternate elements are "scattered" 
    //to left, and to right (this is so that, 
    //if there are many elements equal to 
    //one - or more - of the pivots), those 
    //equal values will - hopefully! - be 
    //scattered approximately evenly into 
    //two different partitions).
    //
    int pivotCount = pivots.length;
    for (;scan<stop;++scan) {
      int v = vArray[scan];
      int b = BinaryInsertionSort
              .findPostInsertionPoint
              ( pivots, 0, pivotCount, v );
      buckets[b].add(v);
      ++scan;
      if ( scan<stop ) {
        v = vArray[scan];
        b = BinaryInsertionSort
            .findPreInsertionPoint
            ( pivots, 0, pivotCount, v );
        buckets[b].add(v);
      }
    }
  }
  protected void gather
    ( int[] pivots, IntegerBucket[] buckets
    , int[] dest, int start, int stop
    , Partition lastPartition) {
    //Calved, to keep sortRange() short.
    //And in theory, you could *also* distribute 
    //from the intermediate buckets, only copying 
    //back into the array "at the bottom" of the sort.
    int partitionStop=start;
    int partitionStart=start;
    int pivotCount = pivots.length;
    int b; //bucket number
    for ( b=0; b < pivotCount; ++b ) {
      partitionStop = buckets[b].emit
                      ( dest, partitionStop );
      buckets[b] = null;
      maybeSortRange
        ( dest, partitionStart, partitionStop
        , lastPartition);
      dest[partitionStop] = pivots[b];
      ++partitionStop;
      partitionStart = partitionStop;
    }
    partitionStop = buckets[b].emit
                    ( dest, partitionStop );
    buckets[b] = null;
    maybeSortRange
      ( dest, partitionStart, partitionStop
      , lastPartition);
  }
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    while (threshold<stop-start) {
      Partition lastPartition 
        = new Partition();
      PivotPlan plan 
        = collectSample(vArray, start, stop);
      int sampleStop = start + plan.sampleSize;
      sortRange(vArray, start, sampleStop); 
      int oversample = ( plan.sampleSize + 1) 
                     / ( plan.pivotCount + 1) - 1;
                     //always >=0
      int nodeSize = (stop - start) 
                   / (plan.pivotCount+1) / 2;
      if (nodeSize<oversample) {
        nodeSize = oversample;
      }
      int vPivots[] 
        = new int[plan.pivotCount];
      IntegerBucket buckets[] 
         = new IntegerBucket[plan.pivotCount+1];
      initializeBuckets
        ( vArray, start, sampleStop, nodeSize
        , oversample, vPivots, buckets);
      scatter ( vArray, sampleStop, stop
              , vPivots, buckets);
      gather  ( vPivots, buckets
              , vArray, start, stop
              , lastPartition);
      start = lastPartition.start;
      stop  = lastPartition.stop;
    }
    if (start<stop) {
      janitor.sortRange
        ( vArray, start, stop );
    }
  }
}
