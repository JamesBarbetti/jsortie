package jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector;

import jsortie.quicksort.partitioner.kthstatistic.KthStatisticSubproblem;

public class OneItemSampleSelector
  implements SampleSelector {
  @Override
  public void chooseSample(KthStatisticSubproblem p) {
    p.innerStart = p.pivotIndex;
    p.innerStop  = p.pivotIndex + 1;
  }
}
