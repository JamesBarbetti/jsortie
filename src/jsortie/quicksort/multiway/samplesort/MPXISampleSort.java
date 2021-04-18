package jsortie.quicksort.multiway.samplesort;

import jsortie.RangeSorter;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.indexselector.IndexSelector;
import jsortie.quicksort.multiway.expander.MultiPivotPartitionExpander;
import jsortie.quicksort.samplesizer.SampleSizer;

public class MPXISampleSort 
  implements RangeSorter {
  public interface SampleLocator {
    public int getSampleStart(int start, int stop, int c);
  }
  public class LeftSampleLocator 
    implements SampleLocator {
    @Override
    public int getSampleStart(int start, int stop, int c) {
      return start;
    }
  }
  int                         radix;
  SampleSizer                 sampleSizer;
  SampleCollector             sampleCollector;
  SampleLocator               sampleLocator;
  IndexSelector               indexSelector;
  MultiPivotPartitionExpander mpx;
  RangeSorter                 janitor;
  int                         janitorThreshold;
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    //int m = stop - start;
    int c = sampleSizer.getSampleSize(2, radix);
    int s = sampleLocator.getSampleStart(start,  stop,  c);
    sampleCollector.moveSampleToPosition
      (vArray, start, stop, s, s+c);
    int[] pivots 
      = indexSelector.selectIndices(s, s+c, radix);
    //Todo: finish this class
  }
}
