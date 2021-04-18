package jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector;

import jsortie.quicksort.partitioner.kthstatistic.KthStatisticSubproblem;

public class ZeroDeltaSampleSelector
  implements SampleSelector {
  @Override
  public void chooseSample(KthStatisticSubproblem p) {
    double m  = p.stop       - p.start;
    double k  = p.pivotIndex - p.start + 1;
    double z  = Math.log(m);
    double c  = Math.floor(0.5 * Math.exp(2 * z / 3) + 0.5); // sample size 0.5 * n ^ (2/3)
    if (c<=1) {
      p.innerStart = p.pivotIndex;
      p.innerStop  = p.pivotIndex+1;
      return;
    }
    //Corrections: c-1, m+1, and m+1-k replace c, m, and m-k. 
    double ll = p.pivotIndex - k * ( c - 1 ) / ( m + 1); 
    double rr = p.pivotIndex + ( m + 1 - k ) * ( c - 1 ) / (m + 1);
    p.innerStart = (int)Math.floor( ll + .5);
    p.innerStop  = (int)Math.floor( rr + 1.5); //+1 coz: exclusive
    if (p.innerStart < p.start) {
      p.innerStart = p.start;
    }
    if (p.stop < p.innerStop) {
      p.innerStop = p.stop;
    }
  }
}
