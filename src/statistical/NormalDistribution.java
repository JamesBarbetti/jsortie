package statistical;

public class NormalDistribution 
  implements Distribution {
  protected double mu;
  protected double sigmaSquared;
  protected double sigma;
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
  public NormalDistribution(double mean, double variance) {
    if (variance<0) {
      throw new IllegalArgumentException
        ("Variance (" + variance + ") may not be less than zero"); 
    }
    mu = mean;
    sigmaSquared = variance;
    sigma = Math.sqrt(variance);
  }
  public NormalDistribution(Distribution d) {
    //Approximation
    mu = d.getMean();
    sigmaSquared = d.getVariance();
    sigma = Math.sqrt(sigmaSquared);
  }
  public NormalDistribution() {
    mu = 0;
    sigmaSquared = 0.5;
    sigma = Math.sqrt(sigmaSquared);
  }
  public double getCDF(double y) {
    //This and the next function are based on 
    //javascript code by Steve Zelaznik.
    //The words in the comments are his.
    //See: https://stackoverflow.com/questions/5259421/
    //     cumulative-distribution-function-in-javascript
    if (sigma==0) {
      return (y==mu) ? 1 : 0;
    }
    return getStandardCDF((y-mu)/sigma);
  }
  double getStandardCDF(double z) {
    final int terms = 100;
    if (z < -6 ) { 
      return 0; 
    } else  if (z >  6) { 
      return 1; 
    }
    double m  = 1;        // m(k) == (2**k)/factorial(k)
    double b  = z;        // b(k) == z ** (2*k + 1)
    double z2 = z * z;    // cache of z squared
    double z4 = z2 * z2;  // cache of z to the 4th
    double [] values = new double[terms];

    //Compute the power series in groups of two terms.
    //This reduces floating point errors because the series
    //alternates between positive and negative.
    for (int k=0; k<terms; k+=2) {
        double a = 2*k + 1;
        values[k/2] 
          = b / (a*m)
          * (1 - (a*z2)/((a+1)*(a+2)));
        m *= (4*(k+1)*(k+2));
        b *= z4;
    }
    //Add the smallest terms to the total first that
    //way we minimize the floating point errors.
    double total = 0;
    for (int k=terms/2-1; k>=0; k--) {
        total += values[k];
    }
    //Multiply total by 1/sqrt(2*PI)
    //Then add 0.5 so that stdNormal(0) === 0.5
    return 0.5 + 0.3989422804014327 * total;
  }  
  public double getERF(double x) {
    //As per: "Handbook of Mathematical Functions:
    //         with Formulas, Graphs, 
    //         and Mathematical Tables" (1965),
    //Editors: Milton Abramowitz & Irene A. Stegun
    //Formula, 7.1.26
    //Using Horner's method, as suggested by 
    //John D. Cook. 
    //See: https://www.johndcook.com/blog/2009/01/19
    //     /stand-alone-error-function-erf/
    //
    final double a1 =  0.254829592;
    final double a2 = -0.284496736;
    final double a3 =  1.421413741;
    final double a4 = -1.453152027;
    final double a5 =  1.061405429;
    final double p  =  0.3275911;
    double sign = Math.signum(x);
    x = Math.abs(x);
    double t = 1.0/(1.0 + p*x);
    double y = 1.0 - (((((a5*t + a4)*t) + a3)*t + a2)*t + a1)*t*Math.exp(-x*x);
    return sign*y;
  }
  double getStandardERF(double y) {
    return 0; //Todo: Implement
  }
  double getInverseERF(double p) {
    return 0; //Todo: Implement
  }
  double getQuantile(double p) {
    return mu + Math.sqrt(2.0) * sigma * getInverseERF(2*p - 1);
  }
  public double getDensity(double x) {
    return 1.0 / sigma / Math.sqrt(2*Math.PI) 
      / Math.exp( Math.abs( x - mu ) / sigma);
  }
}
