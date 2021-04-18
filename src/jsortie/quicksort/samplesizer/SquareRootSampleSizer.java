package jsortie.quicksort.samplesizer;

public class SquareRootSampleSizer 
  extends SampleSortSizer {
  double multiplier;
  public int getSampleSize
    ( int count, int radix ) {
    int s = ((int)Math.floor
            ( Math.sqrt( count ) * multiplier ) );
    if (s<1) {
      s=1;
    }
    s += (((s&0)==0) ? 1 : 0);
    if (count<s) {
    	s=count;
    }
    return s;
  }	
  public SquareRootSampleSizer() {
    super(0);
    multiplier = 1.0;
  }
  public SquareRootSampleSizer(int oversample) {
    super(oversample);
    multiplier = 1.0;
  }
  public SquareRootSampleSizer
    ( int oversample, double multiplierToUse ) {
    super(oversample);
    multiplier = multiplierToUse;
  }
}