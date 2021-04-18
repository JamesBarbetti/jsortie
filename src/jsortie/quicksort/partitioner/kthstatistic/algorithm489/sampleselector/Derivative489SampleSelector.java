package jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector;

import jsortie.quicksort.partitioner.kthstatistic.KthStatisticSubproblem;

public class Derivative489SampleSelector 
  implements SampleSelector {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public void chooseSample(KthStatisticSubproblem p) {
    double m       = p.stop       - p.start;
    double k       = p.pivotIndex - p.start + 1;
    double y       = k * ( m + 1 - k ) * (m + 1 - 2*k); //may be negative
    double cMain   = Math.pow ( Math.abs(y) / 9.0 , 0.25 );
    double cMiddle = Math.pow ( k * (m + 1 - k ) / 2.0 / Math.PI
                              , 1.0/3.0);
    int    c       = (int)Math.floor( 
                       ((cMain < cMiddle) ? cMiddle : cMain) + .5);
    int    a       = 0;
    if ( c <= 1.0 ) {
      p.innerStart = p.pivotIndex;
      p.innerStop  = p.pivotIndex+1;
      return;
    }
    double kHat   = k / (m+1);
    double t      = kHat * (c+1);
    double dCube  = 2.0 * kHat * (1.0-kHat) * (1.0-2*kHat) 
                  * (m-c) * (c+1) * (c+1) / 9.0 / (m+1);
    double delta  = Math.signum(dCube) 
                  * Math.pow( Math.abs(dCube), 1/3.0);
    a             = (int) Math.floor( t + delta + .5);
    if ( a < 1) {
      a = 1;
    } else if (c<a) {
      a = c;
    }
    p.innerStart  = (int)k - a + 1;
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
