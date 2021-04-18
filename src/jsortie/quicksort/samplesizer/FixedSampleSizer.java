package jsortie.quicksort.samplesizer;

public class FixedSampleSizer 
  extends SampleSortSizer {
  int desiredPivots;
  int desiredSampleSize;
  public FixedSampleSizer
    ( int pivotCount, int oversample ) {
    super(oversample);
    desiredPivots = pivotCount;
    desiredSampleSize 
      = (pivotCount+1)*(oversample+1) - 1;
  }
  @Override
  public int getSampleSize(int count, int radix) {
    if (desiredSampleSize < count) {
      return desiredSampleSize;
    }
    int sPlus1 = (count+1) / desiredPivots;
    return (desiredPivots + 1) * sPlus1 - 1; 
  }	
  @Override
  public int getOverSamplingFactor
    ( int count, int radix ) {
    if (desiredSampleSize < count) {
      return oversample;
    }
    return (count+1) / desiredPivots - 1;
  }
}