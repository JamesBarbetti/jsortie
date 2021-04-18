package statistical;

public class BinomialDistribution 
  extends DiscreteDistribution {
  protected double n; //sample size         (parameter)
  protected double p; //success probability (parameter)
  protected double q; //failure probability (calculated)
  protected double t; //expected value of a (calculated)
  protected double sigmaSquared; //variance (calculated)
  protected double sigma; //standard deviation (calculated)
  public BinomialDistribution
    ( int sampleSize, double successProbability) {
    n = sampleSize;
    p = successProbability;
    q = 1-p;
    t = n * p;
    sigmaSquared = n * p * q;
    sigma = Math.sqrt(sigmaSquared);
  }
  public BinomialDistribution(HypergeometricDistribution h) {
    n = h.getSampleSize();
    p = h.getMean() / (n+1);
    q = 1 - p;
    t = n * p;
    sigmaSquared = n * p * q;
    sigma = Math.sqrt(sigmaSquared);
  }
  @Override
  public double getMean() {
    return t;
  }
  @Override
  public double getVariance() {
    return sigmaSquared;
  }
  @Override
  public double getStandardDeviation() {
    return sigma;
  }
  double getExpectedSampleRankOfQuantile(double quantile) {
    if (quantile<0 || 1.0<quantile) {
      throw new IllegalArgumentException
        ( "Quantile (" + quantile + ") must" 
        + "be between 0 and 1 inclusive");
    }
    if (0.5<quantile) {
      return 
         n + 1 - getExpectedSampleRankOfQuantile(1.0-quantile);
    }
    double stop  = Math.floor((n+3)/2);
    double start 
      = (int)Math.floor
             ( stop - 2.0 
               * Math.sqrt 
                 ( getVariance() 
                   * Math.log(1.0/quantile) ) ) ;
    if (start < 1) {
      start = 1;
    }
    if ((n+1)<start+start) {
      return (n+1)/2;
    }
    while (0.1<stop-start) {
      double middle = start + (stop-start)/2;
      double cdf = getApproximateCDF(middle);
      if ( cdf <= quantile ) {
        stop = middle;
      }
      if ( quantile <= cdf ) {
        start = middle;
      }
    }
    return stop;
  }
  public double getApproximateDensity(double a) {
    //This is rough estimate only
    return getApproximateCDF(a+.5)-getApproximateCDF(a-.5);
  }
  @Override
  public double getApproximateCDF(double a) {
    if (a<1) {
      return 0;
    } else if (n<=a) {
      return 1.0;
    }
    //but this is junk
    double leftTerm  = a     * Math.log( n / a * p  );
    double rightTerm = (n-a) * Math.log( n / (n - a ) * q);
    if (a<t) {
      return Math.exp( leftTerm + rightTerm ) * 0.46875;
    } else {
      return 1 - Math.exp( leftTerm + rightTerm ) * 0.46875;
    }
    //This was even wus: return Math.exp( -2.0 * (c*p-a)*(c-p-a) /c );
  }
  @Override
  public double getExactPMF(int rank) {
    if (rank<1 || n<rank) {
      return 0;
    }
    double logOfPMF = 0;
    for (int i=1; i<=rank; ++i) {
      logOfPMF += Math.log(n+1-i) - Math.log(i);
    }
    logOfPMF += rank     * Math.log(p);
    logOfPMF += (n-rank) * Math.log(q); 
    return Math.exp(logOfPMF);
  }
  public double getApproximatePMF(int rank) {
    if (rank<1 || n<rank) {
      return 0;
    }
    double logOfPMF 
      = L(n-rank+1, n)- L(1, rank)
      + rank * Math.log(p)
      + (n-rank) * Math.log(q);
    return Math.exp(logOfPMF);
  }
  @Override
  public double getApproximatePMF(double rank) {
    // TODO Auto-generated method stub
    return 0;
  }
  @Override
  public double getExactCDF(int rank) {
    double cdf = 0;
    for (int i=0; i<=rank; ++i) {
      cdf += getExactPMF(i);
    }
    return cdf;
  }
}
