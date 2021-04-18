package jsortie.quicksort.governor.expansionist;

import jsortie.RangeSorter;
import jsortie.earlyexitdetector.RangeSortEarlyExitDetector;
import jsortie.earlyexitdetector.TwoWayInsertionEarlyExitDetector;
import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.janitors.exchanging.BranchAvoidingAlternatingCombsort;
import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.branchavoiding.LeftSkippyExpander;
import jsortie.quicksort.expander.branchavoiding.RightSkippyExpander;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.Afterthought489Partitioner;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.simple.MiddleElementSelector;

public class ExpansionistQuicksort 
  implements RangeSorter {
  static protected RangeSortEarlyExitDetector defaultEED 
    = new TwoWayInsertionEarlyExitDetector();
  static protected SampleSizer       defaultSampleSizer
    = new SquareRootSampleSizer();
  static protected SinglePivotSelector defaultSelector
    = new MiddleElementSelector();
  static protected SampleCollector   defaultCollector
    = new NullSampleCollector();
  static protected SamplePartitioner defaultPartitioner
      = new MedianPartitioner
            ( new Afterthought489Partitioner() );
  static protected PartitionExpander defaultLeftExpander
    = new LeftSkippyExpander();
  static protected PartitionExpander defaultRightExpander 
    = new RightSkippyExpander();
  static protected RangeSorter defaultJanitor
    = new BranchAvoidingAlternatingCombsort(1.4, 256);
  static protected int defaultJanitorThreshold = 512;
  static protected RangeSorter defaultLastResort
    = new TwoAtATimeHeapsort();
  
  protected RangeSortEarlyExitDetector eed;
  protected SampleSizer                sizer;
  protected SinglePivotSelector        selector;
  protected SampleCollector            collector;
  protected SamplePartitioner          partitioner;
  protected PartitionExpander          leftExpander;
  protected PartitionExpander          rightExpander;
  protected int                        janitorThreshold;
  protected RangeSorter                janitor;
  protected RangeSorter                lastResort;
  public ExpansionistQuicksort() {
    eed              = defaultEED;
    sizer            = defaultSampleSizer;
    collector        = defaultCollector;
    selector         = defaultSelector;
    partitioner      = defaultPartitioner;
    leftExpander     = defaultLeftExpander;
    rightExpander    = defaultRightExpander;
    janitorThreshold = defaultJanitorThreshold;
    janitor          = defaultJanitor;
    lastResort       = defaultLastResort;
  }
  public ExpansionistQuicksort
    ( RangeSortEarlyExitDetector detector
    , SampleCollector collectorToUse
    , SamplePartitioner samplePartitionerToUse
    , PartitionExpander leftExpanderToUse
    , PartitionExpander rightExpanderToUse
    , int janitorThresholdToUse
    , RangeSorter janitorToUse
    , RangeSorter sortOfLastResort) {
    eed              = detector;
    sizer            = defaultSampleSizer;
    collector        = collectorToUse;
    selector         = defaultSelector;
    partitioner      = samplePartitionerToUse;
    leftExpander     = leftExpanderToUse;
    rightExpander    = rightExpanderToUse;
    janitorThreshold = janitorThresholdToUse;
    janitor          = janitorToUse;
    lastResort       = sortOfLastResort;
  }
  public String toString() {
    return this.getClass().getSimpleName()
      + "( " + eed.toString() 
      + ", " + sizer.toString()
      + ", " + collector.toString()
      + ", " + selector.toString()
      + ", " + partitioner.toString()
      + ", " + leftExpander.toString()
      + ", " + rightExpander.toString()
      + ", " + janitorThreshold
      + ", " + janitor.toString()
      + ", " + lastResort.toString()
      + ")";
  }
  public void setSamplePartitioner
    (SamplePartitioner sp) {
    if (sp==null) {
      throw new IllegalArgumentException
        ( "Cannot pass null to setSamplePartitioner");
    }
    partitioner = sp;
  }
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop) {
    int maxDepth 
      = getMaxPartitioningDepth(stop-start);
    sortRangeWith
     ( vArray, start, start
     , stop, stop, leftExpander, maxDepth);
  }
  protected int getMaxPartitioningDepth
    ( int m) {
    return (int) Math.floor 
                 ( (Math.log(m+1) * 4.0) );
  }
  public void sortRangeWith
    ( int[] vArray, int originalStart
    , int start, int stop, int originalStop
    , PartitionExpander expander, int maxDepth) {
    for ( int m=stop-start
        ; janitorThreshold < m
        ; m=stop-start) {
      if ( originalStart < start 
           && stop < originalStop ) {
        if ( vArray[start-1] 
              == vArray[stop] ) {
          return;
        }
      }
      if ( eed.exitEarlyIfSorted 
           ( vArray, start, stop ) ) {
        return;
      }
      if (maxDepth<0) {
        lastResort.sortRange(vArray, start, stop);
        return;
      }
      int c = sizer.getSampleSize(m, 2);
      int pivotIndex, sampleStart, sampleStop;
      if ( c < 2 || m <= c ) {
        pivotIndex 
          = selector.selectPivotIndex
            ( vArray, start, stop );
        sampleStart = pivotIndex;
        sampleStop  = sampleStart + 1;
      } else {
        int targetIndex = start + (m>>1);
        sampleStart = targetIndex - (c>>1);
        sampleStop  = sampleStart + c;
        if (stop<sampleStop) {
          sampleStop = stop;
        }
        collector.moveSampleToPosition
          ( vArray, start, stop
          , sampleStart, sampleStop );
        pivotIndex
          = partitioner.partitionSampleRange
            ( vArray, start, stop, targetIndex
            , sampleStart, sampleStop);
      }
      int split 
        = expander.expandPartition
          ( vArray, start, sampleStart
          , pivotIndex, sampleStop, stop);
      --maxDepth;
      if (split-start < stop-split) {
        sortRangeWith
          ( vArray, originalStart, start
          , split, originalStop
          , leftExpander, maxDepth);
        start    = split + 1;
        expander = rightExpander;
      } else {
        sortRangeWith
          ( vArray, originalStart, split+1
          , stop,   originalStop
          , rightExpander, maxDepth);
        stop     = split;
        expander = leftExpander;
      }
    }
    if ( originalStart < start 
         && stop < originalStop) {
      if (vArray[start-1] == vArray[stop]) {
        return;
      }
    }
    if ( eed.exitEarlyIfSorted 
         ( vArray, start, stop ) ) {
      return;
    }
    janitor.sortRange(vArray, start, stop);
  }
}
