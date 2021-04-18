package jsortie.quicksort.partitioner.kthstatistic.floydrivest.partitionselector;

public class FridgeSamplePartitionSelector
  extends FloydRivestSamplePartitionSelector {
  boolean preventWideMiddle = true;
  boolean preventOffsidePivots = false;
  boolean useOptimisticDeltaFormula = false;
  public FridgeSamplePartitionSelector() {
  }
  public FridgeSamplePartitionSelector
    ( boolean beOptimistic ) {
    useOptimisticDeltaFormula = beOptimistic;
  }
  @Override
  public double getDelta
    ( double d, double m, double c, double t) { 
    //j=desired sample rank
    d = (d==0) ? Math.sqrt( Math.log(m)) : d;
    if (useOptimisticDeltaFormula) {
      double delta 
        = Math.sqrt( ( m + 1.0 ) * ( m + 1.0 - c) 
                     / Math.PI / (c + 1.0 ) )
           * d * ( c + 1 ) / ( m + 1 );
      //upper bound on variance(z), where z 
      //is signed "miss", (x-k)
      //
      //The original Floyd-Rivest formulae are 
      // variance(x) ~= (m+1)*(m-c)/4/c (upper bound)
      // delta        = 2 * Math.sqrt( variance ) 
      //                * d * c / m;
      // so:            Math.sqrt((m+1)*(m-c)/c)*d*c/m
      //
      //But here:
      // variance(x) ~= (m+1)*(m+1-c)/2/PI/(c+1) 
      // delta        = 2 * Math.sqrt(variance) 
      //                * d / Math.sqrt(2) * c+1)/(m+1)
      // so: Math.sqrt((m+1)(m+1-c)/c/PI)*d*(c+1)/(m+1)
      
      if (delta<.5) {
        delta = .5;
      }
      return delta;
    } else {
      return super.getDelta(d, m, c, t);
    }
  }
  public int fixLowerSampleTarget
    ( int sampleStart
    , double t1, double t2
    , double j1, double j2
    , int sampleStop) {
    double jMiddle = (j1+j2)/2.0;
    if (preventWideMiddle) {
      double squeeze 
        = (sampleStop - sampleStart ) 
        / 3.0 / (j2-j1);
      if ( squeeze < 1.0 ) {
        //Sample pivots are too far apart
        j1 = jMiddle - (jMiddle-j1)*squeeze;
        j2 = jMiddle + (j2-jMiddle)*squeeze;
      }
    }
    if (preventOffsidePivots) {
      double sampleMiddle
        = sampleStart 
        + (sampleStop-sampleStart)/2.0;
      if ( j1 <= sampleMiddle 
           && sampleMiddle <= t1 ) {
        //j1 is an off-side pivot
        j1 = t1;
      }
      if ( t2 <= sampleMiddle
           && sampleMiddle < j2) {
        //j2 is an off-side pivot
        j2 = t2;
      }
    }
    int lowerTarget
      = super.fixLowerSampleTarget
        ( sampleStart, t1, t2, j1, j2, sampleStop );
    return lowerTarget;
  }
  public int fixUpperSampleTarget
    ( int sampleStart
    , double t1, double t2
    , double j1, double j2
    , int sampleStop ) {
    double jMiddle = (j1+j2)/2.0;
    if (preventWideMiddle) {
      double squeeze 
        = (sampleStop - sampleStart ) 
        / 3.0 / (j2-j1);
      if ( squeeze < 1.0 ) {
        j1 = jMiddle - (jMiddle-j1)*squeeze;
        j2 = jMiddle + (j2-jMiddle)*squeeze;
      }
    }
    if (preventOffsidePivots) {
      double sampleMiddle
        = sampleStart 
        + (sampleStop-sampleStart)/2;
      if ( j1 <= sampleMiddle 
           && sampleMiddle <= t1) {
        j1 = t1;
      } 
      if ( t2 <= sampleMiddle
           && sampleMiddle < j2) {
        j2 = t2;
        if (j2==j1) {
          return (int)Math.floor(j1+.5);
        }
      }
    }
    return 
      super.fixUpperSampleTarget
      ( sampleStart, t1, t2, j1, j2, sampleStop );
  }
}
