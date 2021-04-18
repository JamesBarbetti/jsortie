package jsortie.quicksort.partitioner.kthstatistic;

import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.heap.KislitsynPartitioner;
import jsortie.quicksort.samplesizer.OneItemSampleSizer;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.selector.reselector.DefaultPivotReselector;
import jsortie.quicksort.selector.reselector.SinglePivotReselector;

public abstract class KthStatisticBase 
  implements KthStatisticPartitioner {
  protected  SampleSizer             sizer;
  protected  SampleCollector         collector;
  protected  SinglePivotReselector   reselector;
  protected  int                     janitorThreshold;
  protected  KthStatisticPartitioner janitor;
  protected  boolean isFoldingWanted  = false;
  public KthStatisticBase() {
    this.sizer      = new OneItemSampleSizer();
    this.collector  = new NullSampleCollector();
    this.reselector = new DefaultPivotReselector();
    this.janitorThreshold  = 5;
    this.janitor
      = new KislitsynPartitioner();
  }
  public KthStatisticBase
    ( SampleSizer sampleSizer
    , SampleCollector collectorToUse
    , SinglePivotReselector reselectorToUse
    , int threshold
    , KthStatisticPartitioner janitorToUse) {
    this.sizer             = sampleSizer;
    this.collector         = collectorToUse;
    this.reselector        = reselectorToUse;
    this.janitorThreshold  = threshold;
    this.janitor           = janitorToUse;
  }
  public void fold 
    ( int[] vArray, int start, int stop ) {
    int i = start;
    int j = stop-1;
    for (; i<j; ++i, --j) {
      int vLeft  = vArray[i];
      int vRight = vArray[j];
      boolean inOrder = (vLeft<=vRight);
      vArray[i] = inOrder ? vLeft  : vRight;
      vArray[j] = inOrder ? vRight : vLeft;
    }
  }
  public int foldIfAsked
    ( int[] vArray, int start, int stop ) {
    if (isFoldingWanted) {
      fold(vArray, start, stop);
      return (stop-start)/2;
    }
    return 0;
  }
  public void setFolding(boolean fold) {
    isFoldingWanted = fold;
  }
}
