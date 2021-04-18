package jsortie.quicksort.samplesizer;

public class FloydRivestSampleSizer
  implements SampleSizer {
  final     double defaultAlpha = 18.6624;
  protected double alpha;
  @Override
  public String toString() {
    return this.getClass().getSimpleName() 
           + "(" + alpha + ")";
  }
  public FloydRivestSampleSizer() {
    alpha = defaultAlpha;
  }
  public FloydRivestSampleSizer
    ( double alphaToUse ) {
    alpha = alphaToUse;
  }
  @Override
  public int getSampleSize
    ( int count, int radix ) {
    int sampleSize 
      = (int) Math.floor(count / alpha);
    if (sampleSize==0) {
      sampleSize = count/4;
    }
    if (sampleSize<2) {
      sampleSize = 2;
    }
    return sampleSize;
  }
}
