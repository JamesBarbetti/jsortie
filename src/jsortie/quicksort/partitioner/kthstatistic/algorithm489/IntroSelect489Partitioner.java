package jsortie.quicksort.partitioner.kthstatistic.algorithm489;

import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.SampleSelector;
import jsortie.quicksort.partitioner.kthstatistic.remedian.RemedianPartitioner;

public class IntroSelect489Partitioner 
  extends Algorithm489Partitioner {
  protected KthStatisticPartitioner 
    lastResortPartitioner
    = new RemedianPartitioner();
  public IntroSelect489Partitioner() {
    super();
  }
  public IntroSelect489Partitioner
    ( SampleSelector selectorToUse
    , SampleCollector collector
    , PartitionExpander leftExpander
    , PartitionExpander rightExpander) {
    super ( selectorToUse, collector
          , leftExpander, rightExpander);
  }
  public IntroSelect489Partitioner
    ( SampleCollector collecta
    , PartitionExpander lx
    , PartitionExpander rx) {
    super (collecta, lx, rx);
  }
  protected class IntroImplementation 
    extends Implementation {
    public IntroImplementation
      ( SampleSelector samplerToUse ) {
      super(samplerToUse);
    }
    @Override
    public int expandInnerPartition
      ( PartitionExpander expander ) {
      if (isComparisonLimitExceeded()) {
        return pivotIndex;
      } else {
        return super.expandInnerPartition
               ( expander );
      }
    }
    @Override
    public boolean isComparisonLimitExceeded() {
      return maxComparisons < comparisonCount;
    }
  }
  @Override
  protected Implementation 
    getImplementation() {
    return new IntroImplementation(sampler);
  }
  @Override
  public void partitionRangeExactly
  ( int[] vArray, int start, int stop
  , int targetIndex) {
    int itemCount    = stop-start;
    if (1<itemCount) {
      Implementation p = getImplementation();
      p.vArray              = vArray;
      p.start               = start;
      p.stop                = stop;
      p.maxComparisons      = itemCount * 10;
      p.comparisonCount
        += foldIfAsked(vArray, start, stop);
      p.partitionExactly
        ( targetIndex, leftExpander );
      if (p.isComparisonLimitExceeded()) {
        lastResortPartitioner.partitionRangeExactly
        ( vArray, start, stop, targetIndex );
      }
    }
  }
}
