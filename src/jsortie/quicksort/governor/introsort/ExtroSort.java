package jsortie.quicksort.governor.introsort;

import jsortie.RangeSorter;
import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.quicksort.partitioner.bidirectional.traditional.SingletonPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.clean.CleanRemedianSelector;

public class ExtroSort
  extends IntroSort {
  public ExtroSort() {
    super ( new CleanRemedianSelector(false), new SingletonPartitioner()
          , new TwoAtATimeHeapsort(), 1024, new TwoAtATimeHeapsort());
  } 
  public ExtroSort
  ( SinglePivotSelector selector
  , SinglePivotPartitioner partitioner
  , RangeSorter janitor, int janitorThreshold
  , RangeSorter sortOfLastResort) {
    super ( selector , partitioner, janitor
          , janitorThreshold, sortOfLastResort);
  }
}
