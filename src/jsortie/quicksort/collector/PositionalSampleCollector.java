package jsortie.quicksort.collector;

import jsortie.helper.RangeSortHelper;

public class PositionalSampleCollector
  implements SampleCollector {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public void moveSampleToPosition
    ( int[] vArray, int start, int stop
    , int sampleStart, int sampleStop) {
    int    count        = stop-start;
    int    sampleCount  = sampleStop-sampleStart;		
    double step         = (double)count 
                        / (double)(sampleCount+1);
    int sampleLeft
      = (int)Math.floor((sampleStart-start) / step);
    if (sampleCount<sampleLeft) {
      sampleLeft = sampleCount;
    } else {
      while ( sampleLeft<sampleCount && 
              start + (sampleLeft-.5)*step  
              < sampleStart + sampleLeft ) {
        ++sampleLeft;
      }
    }
    if (start<sampleStart) {
      double rDouble = start + (sampleLeft-.5) * step;
      int w = sampleStart+sampleLeft-1;
      if (stop<=w) w=stop-1;
      for (; sampleStart<=w; --w, rDouble-=step) {
        int r = (int)Math.floor(rDouble+.5);
        if (r<w) {
          RangeSortHelper.swapElements(vArray, r, w);
        } else {
          r += 0;
        }
      }
    }
    if (sampleStop<stop) {
      double rDouble = start + sampleLeft * step;
      for ( int w=sampleStart+sampleLeft; 
            w<sampleStop; ++w, rDouble+=step) {
        int r = (int)Math.floor(rDouble+.5);
        if (w<r) {
          RangeSortHelper.swapElements
            ( vArray, r, w );
        }
      }
    }
  }
}
