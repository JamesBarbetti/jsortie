package jsortie.quicksort.theoretical;

import java.util.Arrays;

import org.junit.Test;

public class Gain 
{
  public static double gainForInterval(double i) {
    return -i*Math.log(i)/Math.log(2);
  }
  public static double harmonic(double x) {
    double result = 0;
    for (double i = 1; i <= x; i = i + 1) {
      result += 1.0 / i;
    }
    return result;
  }
  public static double harmonic(int x) {
    double result = 0;
    for (int i = 1; i <= x; ++i) {
      result += (1.0 / i);
    }
    return result;
  }
	public static void guessGain()
	{
		//
		//note: output is "natural gain", which is "binary gain" divided by ln2
		//
		System.out.println("p\tg.ln2\tE(g).ln2");
		double h = 0;            //initially: harmonic(1) - 1
		for (int p=1; p<20; ++p) //number of pivots
		{
			h = h + 1.0 / (double)( p + 1); //add 1/(p+1) to h, so it is harmonic(p)-1
			double g = 0;                   //total gain, over all the runs
			int r;                          //in the loop, run number.  after it, run count
			for (r=0; r<1000000; ++r) 
			{
				double v[] = new double[p]; 
				for (int i=0; i<p; ++i)
					v[i] = Math.random();
				Arrays.sort(v);
				g -= v[0] * Math.log(v[0]);  //log base(e), not log base 2
			}
			System.out.println("" + p + "\t" + g/(double)r*(double)(p+1) + "\t" + h ) ;
		}
	}
  public int gcd(int a, int b) {
   return (b==0) ? a : (b<a ? gcd(b,a%b) : gcd(a,b%a));
  }
  @Test
  public void testHarmonicsAsFractions() {
    int numerator=1;
    int denominator=1;
    for (int n=2; n<=15; ++n) {
      numerator   = numerator*n + denominator;
      denominator = n*denominator;
      int g = gcd(numerator, denominator);
      if (1<g) {
        numerator /= g;
        denominator /= g;
      }
      System.out.println( "" + n + "\t" + numerator + "/" + denominator 
      		+ "\t" + (double)numerator/(double)denominator);
    }
  }
}
