package jsortie.quicksort.partitioner.kthstatistic.floydrivest;


import jsortie.helper.FancierEgalitarianPartitionerHelper;
import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.branchavoiding.LeftSkippyExpander;
import jsortie.quicksort.expander.branchavoiding.RightSkippyExpander;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.KthStatisticExpanderBase;
import jsortie.quicksort.partitioner.kthstatistic.floydrivest.partitionselector.FloydRivestSamplePartitionSelector;
import jsortie.quicksort.partitioner.kthstatistic.floydrivest.partitionselector.ThreeWaySamplePartitionSelector;
import jsortie.quicksort.partitioner.kthstatistic.heap.KislitsynPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.remedian.RemedianPartitioner;
import jsortie.quicksort.samplesizer.FloydRivestSampleSizer;
import jsortie.quicksort.selector.reselector.DefaultPivotReselector;

public abstract class FloydRivestPartitionerBase
  extends KthStatisticExpanderBase {
  protected KthStatisticPartitioner
    lastResort;
  protected double  comparisonCount = 0;
  protected boolean useSafetyNet    = false;
  protected ThreeWaySamplePartitionSelector ps
    = new FloydRivestSamplePartitionSelector();
  protected boolean beLazy          = false;
  protected boolean isFolding       = false;
  protected double  alpha           = 18.6624;
  protected boolean quintary        = false;
  FancierEgalitarianPartitionerHelper quinny
    = new FancierEgalitarianPartitionerHelper();
    //Only used when quintary == true
  
  public FloydRivestPartitionerBase() {
    super ( new FloydRivestSampleSizer()
          , new NullSampleCollector()
          , new DefaultPivotReselector()
          , new LeftSkippyExpander()
          , new RightSkippyExpander(), 5
          , new KislitsynPartitioner());
    lastResort = new RemedianPartitioner();
  }
  public FloydRivestPartitionerBase
    ( PartitionExpander expanderOnLeft
    , PartitionExpander expanderOnRight) {
    super ( new FloydRivestSampleSizer()
          , new NullSampleCollector()
          , new DefaultPivotReselector()
          , expanderOnLeft, expanderOnRight, 5
          , new KislitsynPartitioner());
    lastResort = new RemedianPartitioner();    
  }
  public FloydRivestPartitionerBase
    ( SampleCollector collectorToUse
    , PartitionExpander expanderOnLeft
    , PartitionExpander expanderOnRight) {
    super ( new FloydRivestSampleSizer()
          , collectorToUse
          , new DefaultPivotReselector()
          , expanderOnLeft, expanderOnRight, 5
          , new KislitsynPartitioner());
    lastResort = new RemedianPartitioner();
  }
  public void setComparisonCount(double count) {
    comparisonCount = count;
  }
  public double getComparisonCount() {
    return comparisonCount;
  }
  public int getTimeToLive(int count) {
    return count*4;
  }
  public int collectSample
    ( int[] vArray, int start, int stop
    , int sampleStart, int sampleStop ) {
    collector.moveSampleToPosition
    ( vArray, start, stop
    , sampleStart, sampleStop );
    return 0;
  }
  protected int expandWithOnePivot
    ( int[] vArray, int start, int sampleStart
    , int hole, int sampleStop, int stop
    , PartitionExpander lx
    , PartitionExpander rx) {
    hole = lx.expandPartition
           ( vArray, start, sampleStart, hole
           , sampleStop, sampleStop);
    hole = rx.expandPartition
           ( vArray, sampleStart, sampleStart
           , hole, sampleStop, stop); 
    return hole;
  }  
}
