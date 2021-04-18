package jsortie.quicksort.partitioner.kthstatistic.floydrivest.partitionselector;

public class FloydRivestSamplePartitionSelector 
  implements ThreeWaySamplePartitionSelector {
  @Override
  public int getSampleStart
    ( int start, int stop, int k, int c ) {
    int    m           = (stop -start);
    double leftOfPivot = (k-start)
                       * (double) (c-1)
                       / (double) (m-1);
    int sampleStart = 
        k - (int)Math.floor(leftOfPivot+.5);
    if (stop<sampleStart+c) {
      sampleStart=stop-c;
    }
    if (sampleStart<start) {
      sampleStart=start;
    }
    return sampleStart;
  }
  //Note, Floyd & Rivest 
  //set d = Math.sqrt ( Math.log ( count ) );
  //Because they want "misfortune" to be 
  //vanishingly unlikely!
  //(On the order of 1/count).  But... maybe not, huh
  @Override
  public double getDelta
    ( double d, double m, double c, double t) { 
    //j=desired sample rank
    d = (d==0) ? Math.sqrt( Math.log(m)) : d;
    double stddevTimes2 
      = Math.sqrt( (m+1)*(m+1-c)/(c+1) );
    double delta
      = stddevTimes2 * d * (c+1) / (m+1); 
      //Almost... the original Floyd-Rivest formula
      //(but with (c+1) and (m+1) in place of c 
      // and m, in a few places.
    if (delta<.5) {
      delta = .5;
    }
    return delta;
  }
  @Override
  public int fixLowerSampleTarget
    ( int sampleStart
    , double t1, double t2
    , double j1, double j2
    , int sampleStop) {
    double k1 = Math.floor(j1+.5);
    double k2 = Math.floor(j2+.5);
    if (k1<=sampleStart) {
      return sampleStart;
    } else if (k1==k2) {
      k1 = k2 - 1;
    }
    return (int)k1;
  }
  @Override
  public int fixUpperSampleTarget
    ( int sampleStart, double t1, double t2
    , double j1 , double j2, int sampleStop) {
    double k1 = Math.floor(j1+.5);
    double k2 = Math.floor(j2+.5);
    if (k1<=sampleStart && k2<=sampleStart) {
      return sampleStart+1;
    }
    if (sampleStop<=k2) {
      return sampleStop-1;
    } 
    return (int)k2;
  }
}
