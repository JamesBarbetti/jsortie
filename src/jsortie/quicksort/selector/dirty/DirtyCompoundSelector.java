package jsortie.quicksort.selector.dirty;

import jsortie.quicksort.collector.PositionalSampleCollector;
import jsortie.quicksort.collector.RandomSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.indexselector.IndexSelector;
import jsortie.quicksort.indexselector.MiddleIndexSelector;
import jsortie.quicksort.partitioner.insideout.CentripetalPartitioner;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.heap.KislitsynPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.quickselect.QuickSelectPartitioner;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.simple.MiddleElementSelector;

public class DirtyCompoundSelector
  implements SinglePivotSelector {
  protected SampleSizer      sizer;
  protected SampleCollector         collector;
  protected IndexSelector           innerIndexSelector;
  protected KthStatisticPartitioner innerPartitioner;
  public DirtyCompoundSelector
    ( SampleSizer sampleSizer
    , SampleCollector sampleCollector
    , KthStatisticPartitioner innerKthStat) {
    sizer              = sampleSizer;
    collector          = sampleCollector;
    innerPartitioner   = innerKthStat;
    innerIndexSelector = new MiddleIndexSelector();
  }
  public DirtyCompoundSelector(boolean random) {
    sizer = new SquareRootSampleSizer();
    collector
      = random 
        ? new RandomSampleCollector() 
        : new PositionalSampleCollector();
    innerPartitioner 
      = new QuickSelectPartitioner
            ( new MiddleElementSelector()
            , new CentripetalPartitioner()
            , new CentripetalPartitioner()
            , 5, new KislitsynPartitioner());
    innerIndexSelector 
      = new MiddleIndexSelector();
  }
  public DirtyCompoundSelector
    ( SampleSizer sampleSizer
    , SampleCollector sampleCollector
    , SinglePivotSelector pivotSelector) {
    sizer     = sampleSizer;
    collector = sampleCollector;
    innerPartitioner = new KthStatisticPartitioner() {
      // This is a *white lie* local class; it pretends that a single pivot selector
      // (which might even be clean) actually partitions the sample.  It doesn't!
      // But if we pretend that it does (by swapping the selected item into the 
      // target index that we *would* be doing an exact partition for, if we had
      // a KthStatisticPartitioner, that is enough to "fool" DirtyCompoundSelector's
      // selectPivotIndex() implementation.
      @Override
      public String toString() {
        return pivotSelector.toString();
      }
      @Override
      public void partitionRangeExactly
        ( int[] vArray, int start, int stop, int targetIndex ) {
        int selectedIndex = pivotSelector.selectPivotIndex(vArray, start, stop);
        int vTemp = vArray[targetIndex];
        vArray[targetIndex] = vArray[selectedIndex];
        vArray[selectedIndex] = vTemp;
      }
    };
    innerIndexSelector = new MiddleIndexSelector();
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "( " + collector.toString() + " , " + innerPartitioner.toString() + " )";
  }
  public int getSampleStart(int start, int stop, int sampleSize) {
    return start + (stop - start - sampleSize) / 2;
  }
  @Override
  public int selectPivotIndex(int[] vArray, int start, int stop) {
    if (stop <= start + 1) {
      return start;
    } else {
      int sampleSize  = sizer.getSampleSize(stop - start, 2);
      int sampleStart = getSampleStart(start, stop, sampleSize);
      int sampleStop  = sampleStart + sampleSize;
      collector.moveSampleToPosition(vArray, start, stop, sampleStart, sampleStop);
      int[] indices = innerIndexSelector.selectIndices( sampleStart, sampleStop, 1);
      int targetIndexInSample = indices[indices.length/2];
      innerPartitioner.partitionRangeExactly(vArray, sampleStart, sampleStop, targetIndexInSample);
      return targetIndexInSample;
    }
  }
}
