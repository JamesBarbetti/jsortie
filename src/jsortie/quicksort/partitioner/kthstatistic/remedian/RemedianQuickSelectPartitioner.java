package jsortie.quicksort.partitioner.kthstatistic.remedian;

import jsortie.quicksort.partitioner.branchavoiding.SkippyMirrorPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.heap.KislitsynPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.quickselect.QuickSelectPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.clean.CleanRemedianSelector;
import jsortie.quicksort.selector.clean.CleanRemedianSelectorBranchAvoiding;
import jsortie.quicksort.selector.dirty.DirtyRemedianSelector;
import jsortie.quicksort.selector.dirty.DirtyRemedianSelectorBranchAvoiding;

public class RemedianQuickSelectPartitioner
  extends QuickSelectPartitioner {
  static SinglePivotSelector getSelector
    ( boolean branchAvoiding
    , boolean clean, boolean uniform ) {
    if (clean) {
      return branchAvoiding 
        ? new CleanRemedianSelectorBranchAvoiding(uniform)
        : new CleanRemedianSelector(uniform);
    } else {
      return branchAvoiding
        ? new DirtyRemedianSelectorBranchAvoiding(uniform)
        : new DirtyRemedianSelector(uniform);
    }
  }
  public RemedianQuickSelectPartitioner
    ( boolean branchAvoiding
    , boolean clean, boolean uniform ) {
    super ( getSelector(branchAvoiding, clean, uniform)
          , new SkippyPartitioner() 
          , new SkippyMirrorPartitioner()
          , 27
          , new KislitsynPartitioner());
  }
  public RemedianQuickSelectPartitioner() {
    super ( new CleanRemedianSelectorBranchAvoiding(false)
          , new SkippyPartitioner() 
          , new SkippyMirrorPartitioner()
          , 27
          , new KislitsynPartitioner());
  }
}
