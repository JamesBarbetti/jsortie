package jsortie.janitors.insertion.twoway;

import jsortie.RangeSorter;
import jsortie.flags.QuadraticAverageCase;

public class UnstableCocktailShaker 
  implements RangeSorter, CocktailShaker, QuadraticAverageCase {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    while ( start+1 < stop ) {
      stop = shuffleMaximumToRight(vArray, start, stop);
      if (start+1 < stop) {
        start = shuffleMinimumToLeft(vArray, start, stop);
      }
    }
  }  
  @Override
  public int shuffleMinimumToLeft(int[] vArray, int start, int stop) {
    int vMin     = vArray[stop-1];
    int newStart = stop-1;
    for (int scan=stop-2; start<=scan; --scan) {
      int v = vArray[scan];
      if (v < vMin) {
        vArray[scan+1] = vMin;
        vMin          = v;				
      } else {
        vArray[scan+1] = v;
        if ( vMin < v) {
          newStart      = scan+1;
        }
      }
    }
    vArray[start] = vMin;
    return newStart;
  }
  @Override
  public int shuffleMaximumToRight(int[] vArray, int start, int stop) {
    int vMax       = vArray[start];
    int newStop    = start;
    for (int scan=start+1; scan<stop; ++scan) {
      int v = vArray[scan];
      if (vMax < v ) {
        vArray[scan-1] = vMax;
        vMax          = v;
      } else {
        vArray[scan-1] = v;
        if ( v < vMax )
        {
          newStop     = scan;
        }
      }
    }
    vArray[stop-1] = vMax;
    return newStop;
  }
}
