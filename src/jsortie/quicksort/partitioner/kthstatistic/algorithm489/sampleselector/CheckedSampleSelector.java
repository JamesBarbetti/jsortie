package jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector;

import jsortie.exception.SortingFailureException;
import jsortie.quicksort.partitioner.kthstatistic.KthStatisticSubproblem;

public class CheckedSampleSelector
  implements SampleSelector {
  protected SampleSelector innerSelector;
  protected String name;
  public CheckedSampleSelector(SampleSelector s) {
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
    innerSelector.chooseSample(prob);
    if ( prob.innerStart < prob.start ) {
      throw new SortingFailureException
        ( name + " returned innerStart " + prob.innerStart 
        + " less than start: " + prob.start + "... "
        + prob.toString());
    } else if ( prob.stop < prob.innerStop ) {
      throw new SortingFailureException
        ( name + " returned innerStop " + prob.innerStop 
        + " greater than stop: " + prob.stop 
        + "... " + prob.toString());
    } else if ( prob.innerStop <= prob.innerStart) {
      throw new SortingFailureException
        ( name + " returned innerStop " + prob.innerStop 
        + " less than or equal to innerStart: " 
        + prob.innerStart + "... " + prob.toString());
    } else if ( prob.pivotIndex < prob.innerStart ) {
      throw new SortingFailureException
        ( name + " returned pivotIndex " + prob.pivotIndex 
        + " less than innerStart: " + prob.innerStart + "... "
        + prob.toString());
    } else if ( prob.innerStop <= prob.pivotIndex) {
      throw new SortingFailureException
        ( name + " returned pivotIndex " + prob.pivotIndex 
        + " greater or equal to innerStop: " + prob.innerStop + "... "
        + prob.toString());
    }
  }
}
