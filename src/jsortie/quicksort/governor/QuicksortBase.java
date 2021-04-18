package jsortie.quicksort.governor;

import jsortie.RangeSorter;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;

public abstract class QuicksortBase 
  implements RangeSorter {
  protected SinglePivotSelector    selector;
  protected SinglePivotPartitioner partitioner;
  protected RangeSorter            janitor;
  protected int                    janitorThreshold;
  protected QuicksortBase 
    ( SinglePivotSelector selectorToUse
    , SinglePivotPartitioner partitionerToUse
    , RangeSorter         janitorToUse
    , int janitorThresholdToUse) {
    this.selector         = selectorToUse;
    this.partitioner      = partitionerToUse;
    this.janitor          = janitorToUse;
    this.janitorThreshold = janitorThresholdToUse;
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName()
    + "(" + selector.toString() 
    + "," + partitioner.toString()
    + "," + janitor.toString() 
    + "," + janitorThreshold + ")";
  }
}
