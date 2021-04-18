package statistical;

public class HypergeometricDistribution
  extends DiscreteDistribution {
  protected double n;
  protected double k;
  protected double c;
  protected double mu;
  protected double sigmaSquared;
  protected double sigma;
  public HypergeometricDistribution
    ( int populationSize, int successCount
    , int sampleSize) {
    n = populationSize;
    k = successCount;
    c = sampleSize;
    if (n<1) {
      throw new IllegalArgumentException
                ( "n must be > 0 but is " + n);
    } else if (k<1) {
      throw new IllegalArgumentException
                ( "k must be > 0 but is " + k);
    } else if (n<k) {
      throw new IllegalArgumentException
                ( "k must be between 1 and n (" 
                + n + ") inclusive, but is " + k);
    } else if (c<1) {
      throw new IllegalArgumentException
                ( "c must be > 0 but is " + c);
    } else if (n<c) {
      throw new IllegalArgumentException
                ( "c must be <= n but c is " + c
                + " and n is " + n);
    }
    mu = k*c/n;
    sigmaSquared 
       = ( n < 2 || c==n ) 
       ? 0
       : c*k*(n-k)*(n-c)/n/n/(n-1);
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
  public double discreteQuantile() {
    return 0; //Todo: Implement
  }
  @Override
  public double getExactPMF(int a) {
    double logOfPMF = 0;
    for (int i=1; i<=c-a; ++i) {
      logOfPMF +=  Math.log(n-k+1-i) - Math.log(i);
    }
    for (int i=1; i<=a; ++i) {
      logOfPMF += Math.log(k+1-i) - Math.log(a+1-i);
    }
    double logOfCFromM 
      = exactLogOfNChooseR ( n, c );
    logOfPMF -= logOfCFromM;
    if (logOfPMF < -100) {
      return 0;
    }
    return Math.exp ( logOfPMF );
  }
  static double getExactPMFHypergeometric
    ( int popCount, int popRank
    , int sampleCount, int sampleRank ) {
    double logOfCMinus1FromMinus1 
      = exactLogOfNChooseR(popCount, sampleCount);
    double logOfPMF = 0;
    for (int i=1; i<=sampleCount-sampleRank; ++i) {
      logOfPMF  
        +=  Math.log(popCount-popRank+1-i) - Math.log(i);
    }
    for (int i=1; i<sampleRank; ++i) {
      logOfPMF
        += Math.log(popRank+1-i)   - Math.log(sampleRank+1-i);
    }
    logOfPMF -= logOfCMinus1FromMinus1;
    if (logOfPMF < -100) {
      return 0;
    }
    return Math.exp ( logOfPMF );
  }
  @Override
  public double getApproximatePMF(double a) {
    double logOfPMF = 0
      + logOfNChooseR(k,a)
      + logOfNChooseR(n-k, c-a)
      - logOfNChooseR(n, c);
    return Math.exp(logOfPMF);
  }
  public static double getApproximatePMFHypergeometric
    ( int popCount,   int popRank
    , int sampleSize, int sampleRank ) {
    double logOfPMF = 0
      + logOfNChooseR(popRank,sampleRank)
      + logOfNChooseR(popCount-popRank, sampleSize-sampleRank)
      - logOfNChooseR(popCount, sampleSize);
    return Math.exp(logOfPMF);
  }
  @Override
  public double getExactCDF(int a) {
    if (a<0) {
      return 0;
    } else if (c<=a) {
      return 1;
    }
    if (a<=k*(c-1)/n) {
      double CDF = 0;
      for (int i=0; i<=a; ++i) {
        CDF += getExactPMF(i);
      }
      return CDF;
    } else {
      HypergeometricDistribution reflection 
        = new HypergeometricDistribution
              ( (int)n, (int)(n+1-k), (int)c );
      double complementaryCDF = 0;
      for (int i=0; i<c-a; ++i) {
        complementaryCDF += reflection.getExactPMF(i);
      }
      return 1.0 - complementaryCDF; 
    }
  }
  public static double 
    getExactCDFHypergeometric(int m, int k, int c, int a) {
    return (new HypergeometricDistribution(m,k,c)).getExactCDF(a);
  }
  @Override
  public double getApproximateCDF(double a) {
    if (a<0) {
      return 0;
    } else if (c<=a) {
      return 1;
    }
    double CDF = 0;
    for (int i=0; i<=a; ++i) {
      CDF += getApproximatePMF(i);
    }
    if (1<CDF) {
      CDF=1;
    }
    return CDF;
  }
  public double getBinomialApproximationOfCDF(double a) {
    //Approximation using the binomial distribution 
    if (a<1) return 0;
    if (c<=a) return 1;
    double p = k/n;
    double t = p - a/c;
    double divergence
      = (p-t)*Math.log((p-t)/p)
      + (1-p+t)*Math.log((1-p+t)/(1-p));
    double prob = Math.exp( -c * divergence ) / 2.0;
    return (0<=t) ? prob : (1-prob);
  }
  public double getSampleSize() {
    return c;
  }
}
