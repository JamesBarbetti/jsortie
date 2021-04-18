package jsortie.janitors.selection;

import jsortie.RangeSorter;
import jsortie.flags.QuadraticAverageCase;
import jsortie.helper.RangeSortHelper;

public class SelectionSort2WayDirtier implements RangeSorter, QuadraticAverageCase  {
  @Override
  public String toString() { return this.getClass().getSimpleName(); }

  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    int first=start;
    int last=stop-1;
    for (;first<last;++first,--last) {
      RangeSortHelper.compareAndSwapIntoOrder(vArray, first, last);
      int vLow  = vArray[first];
      int vHigh = vArray[last];
      for (int scan=first+1; scan<last; ++scan) {
        int vScan = vArray[scan];
        if ( vScan < vLow ) {
          vArray[first] = vScan;
          vArray[scan]  = vLow;
          vLow         = vScan;
        } else if ( vHigh < vScan ) {
          vArray[last] = vScan;
          vArray[scan] = vHigh;
          vHigh       = vScan;
        }
      }
    }
  }
}
