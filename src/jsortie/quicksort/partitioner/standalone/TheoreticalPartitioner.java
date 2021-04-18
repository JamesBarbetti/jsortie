package jsortie.quicksort.partitioner.standalone;

import jsortie.helper.EgalitarianPartitionerHelper;
import jsortie.janitors.insertion.BinaryInsertionSort;
import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.bidirectional.CentripetalExpander;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.interfaces.StandAlonePartitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.Algorithm489Partitioner;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;

public class TheoreticalPartitioner
  implements StandAlonePartitioner {
  public SampleSizer       sizer;
  public SampleCollector   collector;
  public PartitionExpander leftExpander;
  public PartitionExpander rightExpander;
  public KthStatisticPartitioner samplePartitioner;
  public EgalitarianPartitionerHelper twp;
  @Override
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + leftExpander.toString() 
      + "," + rightExpander.toString() + ")";
  }
  public TheoreticalPartitioner() {
    sizer     = new SquareRootSampleSizer( 0, 1 / Math.log(2.0));
    collector = new NullSampleCollector();
    leftExpander = new CentripetalExpander();
    rightExpander = leftExpander;
    samplePartitioner 
      = new Algorithm489Partitioner
            ( collector 
            , leftExpander, rightExpander);
    twp = new EgalitarianPartitionerHelper();
  }
  public TheoreticalPartitioner
    ( PartitionExpander leftExpanderToUse
    , PartitionExpander rightExpanderToUse) {
    sizer     = new SquareRootSampleSizer( 0, 1 / Math.log(2.0));
    collector = new NullSampleCollector();
    leftExpander  = leftExpanderToUse;
    rightExpander = rightExpanderToUse;
    samplePartitioner 
      = new Algorithm489Partitioner
            ( collector
            , leftExpander, rightExpander );
    twp = new EgalitarianPartitionerHelper();
  }
  @Override
  public int[] multiPartitionRange(int[] vArray, int start, int stop) {
    int count = stop-start;
    if (count<10) {
      BinaryInsertionSort.sortSmallRange(vArray, start, stop);
      return new int[] { start, start };
    }
    int sampleSize  = sizer.getSampleSize(count, 2);
    int sampleStart = start + (count-sampleSize)/2;
    int sampleStop  = sampleStart + sampleSize;
    collector.moveSampleToPosition
      ( vArray, start, stop, sampleStart, sampleStop );
    int middleIndex = start + (count/2);
    samplePartitioner.partitionRangeExactly
      ( vArray, sampleStart, sampleStop, middleIndex );
    int split = leftExpander.expandPartition
                ( vArray, start, sampleStart
                , middleIndex, sampleStop+1, stop);
    int[] boundaries = new int[] { start, split, split+1, stop };
    return twp.fudgeBoundaries(vArray, boundaries);
  }
}
