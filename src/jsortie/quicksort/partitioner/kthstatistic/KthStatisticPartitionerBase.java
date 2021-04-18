package jsortie.quicksort.partitioner.kthstatistic;

import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.partitioner.branchavoiding.SkippyMirrorPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyPartitioner;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.protector.CheckedSinglePivotPartitioner;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.selector.reselector.DefaultPivotReselector;
import jsortie.quicksort.selector.reselector.SinglePivotReselector;

public abstract class KthStatisticPartitionerBase
  extends KthStatisticBase {
  protected  SinglePivotPartitioner leftPartitioner;
  protected  SinglePivotPartitioner rightPartitioner;
  @Override
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
    String partitionerBit 
      = leftPartitioner.toString();
    String rightBit 
      = rightPartitioner.toString();
    if (partitionerBit != rightBit) {
      partitionerBit += ", " + rightBit;
    }
    name += partitionerBit 
      + "," + janitorThreshold;
    if (1<janitorThreshold) {
      name += "," + janitor.toString();
    }
    return name + ")";
  }
  public KthStatisticPartitionerBase() {
    this.leftPartitioner  
      = new SkippyMirrorPartitioner();
    this.rightPartitioner 
      = new SkippyPartitioner();
  }
  protected KthStatisticPartitionerBase
    ( SampleSizer            sampleSizer
    , SampleCollector        collectorToUse
    , SinglePivotReselector  reselectorToUse
    , SinglePivotPartitioner partitionerToUseOnLeft
    , SinglePivotPartitioner partitionerToUseOnRight
    , int threshold
    , KthStatisticPartitioner janitorToUse) {
    super ( sampleSizer, collectorToUse
          , reselectorToUse, threshold
          , janitorToUse);
    this.leftPartitioner   = partitionerToUseOnLeft;
    this.rightPartitioner  = partitionerToUseOnRight;
  }
  public void enablePartitionerChecking() {
    leftPartitioner 
      = new CheckedSinglePivotPartitioner
            ( leftPartitioner );
    rightPartitioner 
      = new CheckedSinglePivotPartitioner
            ( rightPartitioner );
  }
}
