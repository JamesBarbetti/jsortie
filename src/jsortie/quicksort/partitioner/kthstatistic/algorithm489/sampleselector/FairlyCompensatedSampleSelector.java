package jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector;

import jsortie.quicksort.partitioner.kthstatistic.KthStatisticSubproblem;

public class FairlyCompensatedSampleSelector
  implements SampleSelector {
  @Override
  public void chooseSample(KthStatisticSubproblem p) {
    double m  = p.stop       - p.start;
    double k  = p.pivotIndex - p.start + 1;
    double z  = Math.log(m);
    double c  
      = Math.floor(0.5 * Math.exp(2 * z / 3) + 0.5); 
    double delta 
      = 0.5 * Math.sqrt( z * c * (m - c) / m) *
                         Math.signum( (m+1) / 2.0 - k);
    //This bit ensures that over-compensation 
    //does not occur…
    double t       = k * (c+1) / (m+1);
    double aMiddle = (c+1)/2;    
    if ( t <= aMiddle && aMiddle < t + delta ) {
      delta = aMiddle - t;
    }
    else if ( t + delta < aMiddle && aMiddle <= t ) {
      delta = aMiddle - t;
    }
    //End of the over-compensation avoidance bit
    
    if (c<=1) {
      p.innerStart = p.pivotIndex;
      p.innerStop  = p.pivotIndex+1;
      return;
    }
    double ll = p.pivotIndex 
              - k * ( c - 1 ) / (m + 1)
              - delta; 
    double rr = p.pivotIndex 
              + ( m + 1 - k ) * ( c - 1 ) / (m + 1)
              - delta;
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
