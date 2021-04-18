package jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector;

import jsortie.exception.SortingFailureException;
import jsortie.quicksort.partitioner.kthstatistic.KthStatisticSubproblem;

public class CheckingSampleSelector 
  implements SampleSelector {
  protected SampleSelector innerSelector;
  protected String name;
  public CheckingSampleSelector(SampleSelector s) {
    innerSelector = s;
    name = s.toString();
  }
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + name + ")";
  }
  @Override
  public void chooseSample
    ( KthStatisticSubproblem prob ) {
    if (prob.stop <= prob.start ) {
      throw new SortingFailureException
        ( name + " passed a zero-or-less length range. "
        + " start is " + prob.start 
        + ", but stop is " + prob.stop );
    } else if ( prob.pivotIndex < prob.start 
                || prob.stop <= prob.pivotIndex) {
      throw new SortingFailureException
        ( name + " passed an out-of-range pivot index  "
        + " start and stop are " + prob.start 
        + " and " + prob.stop 
        + " but pivotIndex is " + prob.pivotIndex );
    }
    innerSelector.chooseSample(prob);
  }
}
