package jsortie.quicksort.partitioner.kthstatistic;

import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.branchavoiding.LeftSkippyExpander;
import jsortie.quicksort.expander.branchavoiding.RightSkippyExpander;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.protector.CheckedPartitionExpander;
import jsortie.quicksort.protector.CheckingPartitionExpander;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.selector.reselector.DefaultPivotReselector;
import jsortie.quicksort.selector.reselector.SinglePivotReselector;

public abstract class KthStatisticExpanderBase 
  extends KthStatisticBase {
  protected  PartitionExpander leftExpander;
  protected  PartitionExpander rightExpander;
  protected KthStatisticExpanderBase() {
    super();
    this.leftExpander  = new LeftSkippyExpander();
    this.rightExpander = new RightSkippyExpander();
  }
  protected KthStatisticExpanderBase
    ( SampleSizer             sampleSizer
    , SampleCollector         collectorToUse
    , SinglePivotReselector   reselectorToUse
    , PartitionExpander       expanderToUseOnLeft
    , PartitionExpander       expanderToUseOnRight
    , int                     threshold
    , KthStatisticPartitioner janitorToUse) {
    super ( sampleSizer, collectorToUse
          , reselectorToUse, threshold
          , janitorToUse);
    this.leftExpander  = expanderToUseOnLeft;
    this.rightExpander = expanderToUseOnRight;
  }
  public void enableExpanderChecking() {
    leftExpander  
      = new CheckedPartitionExpander
            ( new CheckingPartitionExpander
                  ( leftExpander ) );
    rightExpander 
      = new CheckedPartitionExpander
            ( new CheckingPartitionExpander
                  ( rightExpander ) );
  }
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
    String expanderBit 
      = leftExpander.toString();
    String rightExpanderBit 
      = rightExpander.toString();
    if (rightExpanderBit != expanderBit) {
      expanderBit += ", " + rightExpanderBit;
    }
    name += expanderBit 
      + "," + janitorThreshold;
    if (1<janitorThreshold) {
      name += "," + janitor.toString();
    }
    return name + ")";
  }
}
