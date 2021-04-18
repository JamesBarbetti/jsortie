package jsortie.quicksort.selector.dirty;

import jsortie.quicksort.collector.PositionalSampleCollector;
import jsortie.quicksort.collector.RandomSampleCollector;
import jsortie.quicksort.partitioner.kthstatistic.quickselect.QuickSelectPartitioner;
import jsortie.quicksort.samplesizer.FixedSampleSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;

public class DirtyTrueMedianOfSliceSelector
  extends DirtyCompoundSelector {
  public DirtyTrueMedianOfSliceSelector() {
    super ( new SquareRootSampleSizer()
          , new PositionalSampleCollector()
          , new QuickSelectPartitioner());
  }
  public DirtyTrueMedianOfSliceSelector(boolean random)  {
    super ( new SquareRootSampleSizer()
          , random 
            ? new RandomSampleCollector()
            : new PositionalSampleCollector()
          , new QuickSelectPartitioner());
  }
  public DirtyTrueMedianOfSliceSelector(int i) {
    super ( new FixedSampleSizer(i,2)
          , new RandomSampleCollector()
          , new QuickSelectPartitioner());
  }
  public DirtyTrueMedianOfSliceSelector
    (int i, boolean random) {
    super ( new FixedSampleSizer(i,0)
          , random
            ? new RandomSampleCollector()
            : new PositionalSampleCollector()
          , new QuickSelectPartitioner());
  }
}