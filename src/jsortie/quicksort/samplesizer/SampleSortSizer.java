package jsortie.quicksort.samplesizer;

public class SampleSortSizer 
  implements OversamplingSampleSizer {
  protected int oversample;
  @Override
  public String toString() {
    return this.getClass().getSimpleName() ;
  }
  public SampleSortSizer
    ( int oversamplingFactor )  {
    this.oversample = oversamplingFactor;
  }
  @Override
  public int getSampleSize
    ( int count, int radix ) {
    int ceiling 
      = (int)Math.floor
             ( (double)count / Math.log(count)) 
               / (1+oversample) - 1;
    int sample=radix-1;
    while (sample+sample<ceiling) {
      sample=(sample+1)*radix-1;
    }
    sample = (sample+1) * (oversample+1) - 1;
    sample = (count<sample) ? count : sample; 
    //for when count<radix*(oversample+1)-1
    return sample;
  }
  @Override
  public int getOverSamplingFactor
    ( int count, int radix ) {
    return oversample;
  }
}