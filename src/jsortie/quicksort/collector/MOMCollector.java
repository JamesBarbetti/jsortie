package jsortie.quicksort.collector;

import jsortie.janitors.insertion.ShellSort;

public class MOMCollector
  implements SampleCollector { 
    protected ShellSort s = new ShellSort();
    protected double overSamplingFactor; 
    public MOMCollector(double oversample) {
      overSamplingFactor = oversample;
    }
    @Override 
    public String toString() {
      return this.getClass().getSimpleName() 
        + "(" + overSamplingFactor + ")";
    }
    @Override
    public void moveSampleToPosition
      ( int[] vArray, int start, int stop
      , int sampleStart, int sampleStop ) {
      int step = sampleStop - sampleStart;
      if (start<sampleStart || sampleStop<stop) {
        //Given sampleStart and sampleStop, we can 
        //*guess* what the target fractional index (kHat)
        //and target index (k) are likely to be,
        //supposing this method is being called from
        //inside a Kth statistic partitioner.
        int leftOfSample  = sampleStart - start;
        int rightOfSample = stop - sampleStop;
        int outsideSample = leftOfSample + rightOfSample;
        double kHat = (double) leftOfSample
                    / (double) outsideSample;
        double countExtra = step * overSamplingFactor;
        int oversampleStart 
          = sampleStart 
          - (int) Math.floor(kHat * countExtra);
        if (oversampleStart<start) {
          oversampleStart = start;
        }
        int oversampleStop 
          = sampleStop
          + (int) Math.floor((1.0-kHat) * countExtra);
        if (stop<oversampleStop) {
          oversampleStop = stop;
        }
        s.sortSlicesOfRange
          ( vArray, oversampleStart
          , oversampleStop, step);
      }
    }
}
