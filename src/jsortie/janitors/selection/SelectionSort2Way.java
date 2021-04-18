package jsortie.janitors.selection;

import jsortie.RangeSorter;
import jsortie.flags.QuadraticAverageCase;

public class SelectionSort2Way implements RangeSorter, QuadraticAverageCase {
  @Override
  public String toString() { return this.getClass().getSimpleName(); }

  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    int first=start;
    int last=stop-1;
    int lowest;
    int highest;
    for (;first<last;++first,--last) {
      int vLow ;
      int vHigh;
      if (vArray[first] <= vArray[last]) {    	  
        lowest  = first;
        highest = last;
      } else {
        lowest  = last;
        highest = first;
      }
      vLow  = vArray[lowest];
      vHigh = vArray[highest];
      for (int scan=first+1; scan<last; ++scan) {
        int vScan = vArray[scan];
        if ( vScan < vLow ) {
          lowest = scan;				  
          vLow   = vScan;
        } else if ( vHigh < vScan ) {
          highest = scan;
          vHigh   = vScan;
        }
      }
      vArray[lowest]  = vArray[first];
      vArray[first]   = vLow;
      if (highest==first) {
        highest=lowest;
      }
      vArray[highest] = vArray[last];
      vArray[last]    = vHigh;
    }
  }
}
