package jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector;

import jsortie.quicksort.partitioner.kthstatistic.KthStatisticSubproblem;

public class RauhAndArceSampleSelector 
  implements SampleSelector {
  boolean ensureFairlyCompensated;
  public RauhAndArceSampleSelector
    ( boolean fairlycompensated ) {
    ensureFairlyCompensated = fairlycompensated;
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public void chooseSample 
    ( KthStatisticSubproblem p ) {
    double m  = p.stop       - p.start;
    double k  = p.pivotIndex - p.start + 1;
    int    c  
      = (int)Math.floor
             ( Math.pow
               ( m * m / 8.0 / Math.PI
               , 1.0/3.0 )
             + .5 );
    if ( Math.floor ( m/3 ) < c ) {
      c = (int)Math.floor( m / 3 );
    }
    if ( c<= 1 ) {
      p.innerStart = p.pivotIndex;
      p.innerStop  = p.pivotIndex+1;
      return;
    }
    if (((int)c&1) == 0) {
      ++c; //Make sure c is odd.
    }
    int    a       = (c+1)/2;
    if ( m/c < Math.abs(k - (m+1)/2) ) {
      double t     = k * (c + 1) / ( m + 1); 
      double lowK  = (k < ( m + 1 )/2) 
                   ? k : ( m + 1 - k); 
      double kHat  = lowK / (m+1);
      double absDelta 
        = (c+1)
        * Math.sqrt( 2*kHat*(1-kHat)/(c+2) )
        * Math.log( (1+2*kHat)*Math.sqrt(c+2)
          / Math.sqrt(2*Math.PI*kHat*(1-kHat)));
      double delta = (k==lowK) ? absDelta : -absDelta;
      a            = (int) Math.floor( t + delta + .5);
      if (ensureFairlyCompensated) {
        int aMiddle = (c+1)/2; 
          //No rounding problem. c is odd. see above.
        if ( t <= aMiddle && aMiddle < a ) {
          a = aMiddle; 
        } else if ( a < aMiddle && aMiddle <= t ) {
          a = aMiddle;
        }
      }
      if (a<1) {
        a = 1;
      } else if (c<a) {
        a = c;
      }
    }
    p.innerStart    = (int)k - a + 1;
    if ( p.stop < p.innerStart + (int)c) {
      p.innerStart = p.stop - (int)c;
    }
    if (p.innerStart < p.start) {
      p.innerStart = p.start;
    }
    p.innerStop  = p.innerStart + (int)c;
    p.pivotIndex = p.innerStart + a - 1;
  }
}
