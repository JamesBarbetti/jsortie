package statistical;

public abstract class DiscreteDistribution 
  implements Distribution {
  protected static double L
    ( double first, double last ) {
    //Returns: the approximate sum of the logarithms 
    //of the (whole) numbers start through stop-1 
    double x;
    if (first==1) {
      return logOfNFactorial(last);
    } else if (first==last) {
      x = Math.log(last);
    } else {
      x = last * (Math.log(last) - 1) 
        + Math.log(last*2*Math.PI) / 2.0
        - (first-1) * (Math.log(first-1) - 1.0) 
        - Math.log((first-1)*2*Math.PI) / 2.0;
    }
    return x;
  }
  protected static double logOfNChooseR
    ( double n, double r ) {
    if (r==0) {
      return 1;
    } else if (r==1) {
      return Math.log(n);
    } 
    return logOfNFactorial(n) 
     - logOfNFactorial(r)
     - logOfNFactorial(n-r);
  }
  protected static double logOfNFactorial
    ( double r ) {
     return
       r * (Math.log(r) - 1) 
         + Math.log(r*2*Math.PI) / 2.0;
  }
  protected static double exactLogOfNChooseR
    ( double n, double r ) {
    double logOfNChooseR = 0;
    for (int i=1; i<=r; ++i) {
      logOfNChooseR
        += Math.log(n+1-i) - Math.log(i);
    }
    return logOfNChooseR;
  }
  
  public abstract double getExactPMF(int rank);
  public abstract double getApproximatePMF(double rank);
  public abstract double getExactCDF(int rank);
  public abstract double getApproximateCDF(double rank);
}
