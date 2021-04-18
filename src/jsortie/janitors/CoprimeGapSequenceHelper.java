package jsortie.janitors;

import java.util.ArrayList;
import java.util.Collection;

public class CoprimeGapSequenceHelper { 
  public static int gcd(int a, int b) {
    return (b==0) ? a : 
      (b<a ? gcd(b,a%b) : gcd(a,b%a));
  }
  public static boolean  coPrimeWithAll
    (int x, Collection<Integer> others) {
    for ( int y : others ) {
      if (1<gcd(x,y)) {
        return false;
      }
    }
	return true;
  }
	
  public static int[] coPrimeGapSequence
    (double ratio) {
    if (ratio<=1) {
      throw new IllegalArgumentException(
        "GapSequenceRatio, g, must be "
        + "greater than 1, but " 
        + ratio + " was requested"); 
    }
    ArrayList<Integer> gaps 
      = new ArrayList<Integer>();
    double gap=ratio; 
    for (;gap<Integer.MAX_VALUE; gap*=ratio ) {
      int x = (int)Math.floor(gap);
      while ( !coPrimeWithAll ( x, gaps ) 
              && x < Integer.MAX_VALUE ) {
        ++x;
      }
      gaps.add( x ) ;
    }
    int intGaps[] = new int[gaps.size()];
    for (int i=0; i<gaps.size(); ++i) {
      intGaps[i] = gaps.get(i);
    }
    return intGaps;
  }
}
