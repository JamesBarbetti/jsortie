package jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector;

import jsortie.quicksort.partitioner.kthstatistic.KthStatisticSubproblem;

public class Afterthought489SampleSelector 
  implements SampleSelector {
  double twoDividedByCubeRootOfPi 
    = 2 / Math.pow( Math.PI, 1.0/3.0);
  @Override
  public void chooseSample(KthStatisticSubproblem p) {
    double m  = p.stop       - p.start;
    double k  = p.pivotIndex - p.start + 1;
    double z  = Math.log(m);
    double c  
      = Math.floor(0.5 * Math.exp(2 * z / 3) + 0.5); 
    if (c<=1) {
      p.innerStart = p.pivotIndex;
      p.innerStop  = p.pivotIndex+1;
      return;
    }
    double delta 
      = 0.5 * Math.sqrt( z * c * (m + 1 - c) 
            / Math.PI / (m+1))
            * Math.signum((m+1) / 2.0 - k);
    double t       = k * (c+1) / (m+1);
    double ll;
    double rr;
    double aMiddle = (c+1)/2;    
    if (    ( delta == 0 )
         || ( t <= aMiddle && aMiddle < t + delta ) 
         || ( t + delta < aMiddle && aMiddle <= t ) ) {
      //
      //This bit ensures that over-compensation doesn't  
      //occur… AND bumps up the sample size whenever 
      //over-compensation would otherwise occur.
      //
      c = Math.floor( Math.exp(2.0*z/3.0) 
                      * twoDividedByCubeRootOfPi );
      if (( (int)c & 1 ) == 0 ) {
        ++c;
      }
      ll = p.pivotIndex - (c-1.0)/2.0;
      rr = p.pivotIndex + (c-1.0)/2.0;
      //
      //End of the over-compensation avoidance bit
      //
    }
    else {
      ll = p.pivotIndex 
           - k * ( c - 1 ) / (m + 1)
           - delta; 
      rr = p.pivotIndex 
           + ( m + 1 - k ) * ( c - 1 ) / (m + 1)
           - delta;
    }
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
