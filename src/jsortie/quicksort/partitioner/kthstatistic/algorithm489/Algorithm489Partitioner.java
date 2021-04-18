package jsortie.quicksort.partitioner.kthstatistic.algorithm489;

import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.PositionalSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.kthstatistic.KthStatisticExpanderBase;
import jsortie.quicksort.partitioner.kthstatistic.KthStatisticSubproblem;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.Algorithm489SampleSelector;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.CheckedSampleSelector;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.CheckingSampleSelector;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.SampleSelector;
import jsortie.quicksort.partitioner.kthstatistic.heap.KislitsynPartitioner;
import jsortie.quicksort.samplesizer.OneItemSampleSizer;
import jsortie.quicksort.selector.reselector.DefaultPivotReselector;

public class Algorithm489Partitioner
  extends KthStatisticExpanderBase {
  protected int samplingThreshold = 30;
  SampleSelector sampler;   
  public Algorithm489Partitioner
    ( SampleSelector samplerToUse
    , SampleCollector collector
    , PartitionExpander lx
    , PartitionExpander rx) {
    super ( new OneItemSampleSizer()
          , collector
          , new DefaultPivotReselector()
          , lx, rx
          , 5, new KislitsynPartitioner());
    setSampler(samplerToUse );
  }
  public Algorithm489Partitioner
    ( SampleCollector collector
    , PartitionExpander expanderOnLeft 
    , PartitionExpander expanderOnRight) {
    super ( new OneItemSampleSizer()
          , collector
          , new DefaultPivotReselector()
          , expanderOnLeft, expanderOnRight
          , 5, new KislitsynPartitioner());
    setSampler(new Algorithm489SampleSelector());
  }
  public Algorithm489Partitioner
    ( SampleSelector sector
    , PartitionExpander lx
    , PartitionExpander rx) {
    super ( new OneItemSampleSizer()
          , new PositionalSampleCollector()
          , new DefaultPivotReselector()
          , lx, rx
          , 5, new KislitsynPartitioner());
    setSampler(new Algorithm489SampleSelector());
  }
  public Algorithm489Partitioner() {
    super();
    setSampler(new Algorithm489SampleSelector());
  }
  public void setSampler
    ( SampleSelector samplerToUse ) {
    sampler = samplerToUse;
  }
  public void enableSampleSelectorChecking() {
    sampler 
      = new CheckingSampleSelector 
            ( new CheckedSampleSelector 
                  ( sampler ) );
  }
  public String toString() {
    String collectorBit = collector.toString() + ", ";
    if (collector instanceof NullSampleCollector) {
      collectorBit = "";
    }
    String expanderBit = leftExpander.toString();
    String rightExpanderBit = rightExpander.toString();
    if (rightExpanderBit != expanderBit) {
      expanderBit += ", " + rightExpanderBit;
    }
    return this.getClass().getSimpleName()
      + "(" + collectorBit 
      + reselector.toString() 
      + ", " + expanderBit + ")";
  }
  protected class Implementation 
    extends KthStatisticSubproblem {
    public int[] vArray;
    public int comparisonCount;
    public int maxComparisons;
    protected SampleSelector samplerToUse;
    public Implementation
      ( SampleSelector samplerToUse ) {
      sampler = samplerToUse;
    }
    public boolean isComparisonLimitExceeded() {
      return false;
    }
    public int expandInnerPartition
      ( PartitionExpander expander ) {
      comparisonCount += (innerStart - start) 
                      +  (stop - innerStop);
      return expander.expandPartition
             ( vArray, start, innerStart
             , pivotIndex, innerStop, stop );
    }
    public boolean partitionExactly
      ( int targetIndex
      , PartitionExpander expander) {
      pivotIndex 
        = reselector.selectPivotIndexGivenHint
          ( vArray, start, targetIndex, stop );
      int originalStart = start;
      int originalStop  = stop;
      while (janitorThreshold < stop - start) {
        if (samplingThreshold < stop - start) {
          partitionSample(expander); 
          // sets innerStart and innerStop
        } else {
          innerStart = pivotIndex;
          innerStop  = pivotIndex + 1;
          collectSample();
        }
        int split = expandInnerPartition
                    ( expander );
        int vPivot = vArray[split]; 
        if (split < targetIndex) {
          if ( stop < originalStop  
               && vPivot == vArray[stop] ) {
            return true;
          }
          expander     = leftExpander; 
          start        = split + 1;
        } else if ( targetIndex < split ) {
          if ( originalStart < start
               && vPivot == vArray[start-1] ) {
            return true;
          }
          expander      = rightExpander;
          stop          = split;
        } else {
          return true;
        }
        pivotIndex 
          = reselector.selectPivotIndexGivenHint
            ( vArray, start, targetIndex, stop );
        if ( isComparisonLimitExceeded() ) {
          return false;
        }
      }
      janitor.partitionRangeExactly
        ( vArray, start, stop, targetIndex );
      return true;
    }
    protected void partitionSample
      ( PartitionExpander expander ) {
      sampler.chooseSample(this);
      collectSample();
      if (innerStart==start && innerStop==stop) {
        innerStart = pivotIndex;
        innerStop  = pivotIndex+1;
      }      
      {
        int formerStart      = start;
        int formerStop       = stop;
        int formerInnerStart = innerStart;
        int formerInnerStop  = innerStop;
        int formerPivotIndex = pivotIndex;
        start = innerStart;
        stop = innerStop;
        partitionExactly
          ( pivotIndex, expander );
        pivotIndex = formerPivotIndex;
        innerStart = formerInnerStart;
        innerStop  = formerInnerStop;
        start      = formerStart;
        stop       = formerStop;
      }
    }
    protected void collectSample() {
      collector.moveSampleToPosition
        ( vArray, start, stop
        , innerStart, innerStop );
    }
  };
  protected Implementation 
    getImplementation() {
    Implementation imp = new Implementation(sampler);
    return imp;
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start
    , int stop, int targetIndex ) {
    Implementation p  = getImplementation();
    p.vArray = vArray;
    p.start = start;
    p.stop = stop;
    p.comparisonCount 
      += foldIfAsked(vArray, start, stop);
    PartitionExpander x =
      ( targetIndex - start < stop - targetIndex )
      ? leftExpander : rightExpander;
    p.partitionExactly
      ( targetIndex, x );
  }
}
