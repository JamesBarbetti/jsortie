package jsortie.quicksort.partitioner.kthstatistic.floydrivest;

import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.multiway.expander.MultiPivotPartitionExpander;
import jsortie.quicksort.multiway.expander.kangaroo.SkippyExpander2;
import jsortie.quicksort.multiway.partitioner.decorator.BirdsOfAFeatherExpander;
import jsortie.quicksort.partitioner.kthstatistic.remedian.RemedianPartitioner;
import jsortie.quicksort.selector.reselector.DefaultPivotReselector;

public class SimplifiedFloydRivestPartitioner
  extends FloydRivestPartitionerBase {
  protected MultiPivotPartitionExpander 
    multiPivotExpander; 
    //for expanding existing 3-way partitions
  protected MultiPivotPartitionExpander
    panicExpander;
  public SimplifiedFloydRivestPartitioner() {
    super();
    multiPivotExpander 
      = new SkippyExpander2();
    panicExpander
      = new BirdsOfAFeatherExpander
            ( multiPivotExpander );
  }
  public SimplifiedFloydRivestPartitioner
    ( MultiPivotPartitionExpander expanderToUse ) {
    super();
    multiPivotExpander = expanderToUse;
    panicExpander      
      = new BirdsOfAFeatherExpander
            ( multiPivotExpander );
    lastResort         
      = new RemedianPartitioner();
  }
  public SimplifiedFloydRivestPartitioner
  ( SampleCollector collectorToUse
  , MultiPivotPartitionExpander expanderToUse ) {
    super();
    collector          = collectorToUse;
    multiPivotExpander = expanderToUse;
    panicExpander 
      = new BirdsOfAFeatherExpander
            ( multiPivotExpander );
  }
  public String toString() {
    String name
      = this.getClass().getSimpleName() + "(";
    name += sizer.toString() + ",";
    if (!(collector instanceof
          NullSampleCollector)) {
      name += collector.toString() + ",";
    }
    if (!(reselector instanceof
          DefaultPivotReselector)) {
      name += reselector.toString() + ",";
    }
    String expanderBit 
      = multiPivotExpander.toString();
    name += expanderBit 
      + "," + janitorThreshold;
    if (1<janitorThreshold) {
      name += "," + janitor.toString();
    }
    return name + ")";
  }
  protected int partition
    ( int[] vArray, int originalStart, int start
    , int k, int stop, int originalStop
    , double d, int timeToLive, boolean outward) {
    //subtracts 1 from timeToLive for each 
    //comparison returns the "new" timeToLive 
    while ( janitorThreshold<stop-start ) {
      if ( originalStart<start 
           && stop<originalStop)
      {
        --timeToLive;
        if (vArray[start-1] == vArray[stop]) {
          return timeToLive;
        }
      }
      int m = stop-start;
      int c = sizer.getSampleSize(m, 2);
      if (c<2) {
        c=2;
      }
      double t = (double)(k+1-start)
               * (double)(c+1)
               /(double)(m+1);
      double delta = ps.getDelta(d, m, c, t);
      int sampleStart
        = ps.getSampleStart(start, stop, k, c);
      int    sampleStop   = sampleStart + c;
      timeToLive         -= collectSample
          ( vArray, start, stop
          , sampleStart, sampleStop);
      int k1 
        = ps.fixLowerSampleTarget
          ( sampleStart, t, t, sampleStart+t-1-delta
          , sampleStart+t-1+delta, sampleStop );
      int k2 = ps.fixUpperSampleTarget
          ( sampleStart, t, t, sampleStart+t-1-delta
          , sampleStart+t-1+delta, sampleStop );
      double d2 
        = (d==0) ? Math.sqrt(Math.log(m)) : d;
      if (sampleStop-k2<k1-sampleStart) {
        timeToLive 
          = partition
            ( vArray, sampleStart, sampleStart
            , k1, sampleStop, sampleStop
            , d2, timeToLive, outward);
        if (timeToLive<0 && useSafetyNet) {
          return timeToLive;
        }
        timeToLive 
          = partition
            ( vArray, k1, k1+1
            , k2, sampleStop, sampleStop
            , d2, timeToLive, outward);
      } else {
        timeToLive 
          = partition
            ( vArray, sampleStart, sampleStart
            , k2, sampleStop, sampleStop
            , d2, timeToLive, outward);
        if (timeToLive<0 && useSafetyNet) {
          return timeToLive;
        }
        timeToLive 
          = partition
            ( vArray, sampleStart, sampleStart
            , k1, k2, k2,  d2, timeToLive, outward);
        
      }
      if (timeToLive<0 && useSafetyNet) {
        return timeToLive;
      }
      MultiPivotPartitionExpander x 
        = (0<=timeToLive) 
          ? multiPivotExpander
          : panicExpander;
      int partitions[] 
        = x.expandPartitions
          ( vArray, start, sampleStart
          , new int[] { k1, k2 }
          , sampleStop, stop);
      //
      //note: we have to guess how many comparisons 
      //      expanding the partitions takes  
      //      (partition expanders won't tell us!).  
      //      This assumes 3(m-m')/2, where m is 
      //      the range item count, and m' the inner 
      //      range count.
      //      But, it might be as little as (m-m') 
      //      (for in-order ranges and conventional 
      //      partition expander) or as much 
      //      as 2(m-m') (for a branch-avoiding
      //      partition expander).
      //
      timeToLive -= 
        3*(stop-start-sampleStop+sampleStart)/2;
      int i=0;
      for (; i<partitions.length-1; i+=2) {
        start = partitions[i];
        stop  = partitions[i+1];
        if (k<start) {
          //target index outside the partition
          return timeToLive;
        } else if (k<stop) {
          break; //in this partition
        }
      }
      //now, if i<partitions.length-1,
      //the target index is inside a partition 
      //(otherwise it's outside the last one).
      if (partitions.length-1<=i) {
        return timeToLive; //after last partition
      }
      d = 0;
      if (timeToLive<0 && useSafetyNet) {
        return timeToLive;
      }
      outward = !outward;
    }
    int count = stop-start;
    if (1<count) {
      janitor.partitionRangeExactly
        ( vArray, start, stop, k );
      timeToLive -= count*(count-1)/2;
    }
    return timeToLive;
  }
  public void partitionRangeExactly
    ( int[] vArray, int start
    , int stop, int targetIndex ) {
    int timeToLive 
      = getTimeToLive(stop-start);
    if (isFoldingWanted) {
      fold(vArray, start, stop);
      timeToLive -= (stop-start)/2;
    }
    int whatsLeft 
      = partition 
        ( vArray, start
        , start, targetIndex
        , stop, stop, 0.0
        , timeToLive, true);
    if (whatsLeft<0 && useSafetyNet) {
      lastResort.partitionRangeExactly
      ( vArray, start, stop, targetIndex );
    }
    int comparisonsDone 
      = timeToLive - whatsLeft;
    comparisonCount += comparisonsDone; 
    //doesn't count the comparisons done  
    //by the safety net!
  }
  //Finding the median
  public void findMedian
   ( int[] vArray, int start, int stop ) {
   partitionRangeExactly 
     ( vArray, start, stop
     , start + (stop-start)/2);
   }
}
