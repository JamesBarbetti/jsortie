package jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector;

import jsortie.quicksort.partitioner.kthstatistic.KthStatisticSubproblem;

public class Algorithm489SampleSelector 
  implements SampleSelector {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public void chooseSample(KthStatisticSubproblem p) {
    // Note: these formulae (and the letters chosen) are 
    // based on those given in the algorithm SELECT in 
    // Floyd & Rivest's original paper, with some letter 
    // changes (m replaces n, k replaces i, c replaces s, 
    // delta replaces sd) and some minor corrections.
    double m     = p.stop       - p.start;
    double k     = p.pivotIndex - p.start + 1;
    double z     = Math.log(m);
    double c     = 0.5 * Math.exp(2 * z / 3); 
      // sample size 0.5 * n ^ (2/3): needn't take floor!
    double delta 
      = 0.5 
        * Math.sqrt(z * c * (m - c) / m) 
        * Math.signum( ( m + 1 ) / 2.0 - k);
         //Correction: not Math.signum(i - m / 2.0).
         //That biases the pivots
         //chosen when m and c are odd, and k = (m+1)/2.
    if (c<=1.0) {
      p.innerStart = p.pivotIndex;
      p.innerStop  = p.pivotIndex+1;
      return;
    }
    //Corrections: c-1, m+1, and m+1-k replace 
    //c, m, and m-k here. 
    double ll = p.pivotIndex 
              - k * ( c - 1 ) / (m + 1) - delta; 
    double rr = p.pivotIndex 
              + ( m + 1 - k ) 
              * ( c - 1 ) / (m + 1) - delta;
    p.innerStart = (int)Math.floor( ll + .5);
    p.innerStop  = (int)Math.floor( rr + 1.5); 
                   //+1 coz: exclusive
    if (p.innerStart < p.start) {
      p.innerStart = p.start;
    }
    if (p.stop < p.innerStop) {
      p.innerStop = p.stop;
    }
  }
}
