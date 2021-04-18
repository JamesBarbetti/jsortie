package jsortie.quicksort.partitioner.kthstatistic.algorithm489;

import jsortie.quicksort.collector.MOMCollector;

public class MOM489Partitioner 
  extends IntroSelect489Partitioner {
  int overSamplingFactor = 4; 
    //must be greater than 0.  Shouldn't be more 
    //than about 8. Needn't be even (better if it 
    //is even, though, if you're likely to be finding
    //medians or near-medians).
  public MOM489Partitioner() {
    collector = new MOMCollector(overSamplingFactor);
  }
  protected Implementation getImplementation() {
    return new IntroImplementation(sampler) {
      protected void collectSample() {
        collector.moveSampleToPosition
        ( vArray, start, stop, innerStart, innerStop );
        int g = overSamplingFactor+1;
        comparisonCount += 
          (double)(innerStop-innerStart) *
          (double)( g * g + g - 2.0 ) / 4.0;
        //Only a guess, since comparisons aren't 
        //counted directly by 
        //ShellSort.sortSlicesOfRange
      }
    };
  }
}
