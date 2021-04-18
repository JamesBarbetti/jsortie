package statistical;

public class HypergeometricRetribution 
  extends DiscreteDistribution {
  protected double m;
  protected double c;
  protected double a;
  protected double mu;           //mean:     see ctor
  protected double sigmaSquared; //variance: see ctor
  protected double sigma;        //stddev:   see ctor
  public HypergeometricRetribution
    ( int populationSize
    , int sampleSize,     int sampleRank) {
    m  = populationSize;
    c  = sampleSize;
    a  = sampleRank;
    mu = a * ( m + 1 ) / ( c + 1);
    sigmaSquared = a * ( c + 1 - a ) * ( m + 1 ) * (m - c )
                 / (c + 1 ) / ( c + 1 ) / ( c + 2);
    sigma = Math.sqrt(sigmaSquared);
  }
  @Override
  public double getMean() {
    return mu;
  }
  @Override
  public double getStandardDeviation() {
    return sigma;
  }
  @Override
  public double getVariance() {
    return sigmaSquared;
  }
  double getDiscreteQuantile() {
    return 0; //Todo: Implement
  }
  @Override
  public double getExactPMF(int sampleRank) { //sampleRank 1 based.
    if (sampleRank<a) {
      return 0;
    } else if (m-c < sampleRank ) {
      return 0;
    }
    double logOfPMF = 0;
    for (int i=1; i<=a; ++i) {
      //We want log(binomial_coefficent(c-1,a-1))
      //  - log(binomial_coefficient(m-1,x-1)
      logOfPMF 
        += Math.log((double)(c+1-i))
        -  Math.log((double)(m+1-i));
    }        
    for (int x=(int)a+1; x<=sampleRank; ++x) {
      logOfPMF  
        += Math.log((double)(m+1-c+a-x) / (double)(x-a))
        + Math.log((double)(x-1) / (double)(m+1-x));
    } 
    return Math.exp(logOfPMF);
  }
  @Override
  public double getApproximatePMF(double x) {
    if (x<a) {
      return 0;
    } else if (m-c+a < x) {
      return 0;
    }
    double logOfPMF 
      = L(c-a+1, c) - L(m-a+1, m);
    if (a<x) {
      //Each of these, the difference is x-a-1
      //So in the up-tos L(t-x+a, t+1)
      //And in the down-tos L(t, t+x-a+1)
      logOfPMF 
        += L(m+1-c+a-x, m-c ) //down to m+1+c+a-x 
         - L(1, x-a)          //up to x-a
         + L(a, x-1)          //up to x-1 
         - L(m+1-x,m-a);      //down to m+1-x
    }
    return Math.exp(logOfPMF) * .935;
  }
  public static double getApproximatePMF
    ( double popCount,   double sampleCount
    , double sampleRank, double x ) {
    if (x<sampleRank) {
      return 0;
    } else if (popCount-sampleCount+sampleRank < x) {
      return 0;
    }
    double logOfPMF 
      = L(sampleCount-sampleRank+1, sampleCount) 
      - L(popCount-sampleRank+1, popCount);
    if (sampleRank<x) {
      logOfPMF 
        += L( popCount+1-sampleCount+sampleRank-x
            , popCount-sampleCount )
         - L( 1, x-sampleRank)
         + L( sampleRank, x-1) 
         - L( popCount+1-x,popCount-sampleRank);
    }
    return Math.exp(logOfPMF) * .9375;
  }
  @Override
  public double getExactCDF(int rank) {
    double CDF = 0;
    for (int x=1; x<=rank; ++x) {
      CDF += getExactPMF(x);
    }
    return CDF;
  }
  @Override
  public double getApproximateCDF(double rank) {
    double CDF = 0;
    for (int x=1; x<=rank; ++x) {
      CDF += getApproximatePMF(x);
    }
    return CDF;
  }
}
