package jsortie.janitors.insertion.twoway;

import jsortie.StableRangeSorter;
import jsortie.flags.QuadraticAverageCase;

public class CocktailSort 
	implements StableRangeSorter, CocktailShaker, QuadraticAverageCase {
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
      if (vArray[scan] <= vMin) {
        vArray[scan+1] = vMin;
        vMin          = vArray[scan];				
      } else {
        vArray[scan+1] = vArray[scan];
        newStart      = scan+1;
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
      if (vMax <= vArray[scan]) {
        vArray[scan-1] = vMax;
        vMax          = vArray[scan];					
      } else {
        vArray[scan-1] = vArray[scan];
        newStop       = scan;
      }
    }
    vArray[stop-1] = vMax;
    return newStop;
  }
}
