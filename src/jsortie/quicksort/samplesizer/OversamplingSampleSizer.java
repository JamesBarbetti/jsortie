package jsortie.quicksort.samplesizer;

public interface OversamplingSampleSizer
  extends SampleSizer {
  public int getOverSamplingFactor 
             ( int count, int radix);
}
