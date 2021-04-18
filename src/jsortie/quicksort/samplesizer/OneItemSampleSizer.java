package jsortie.quicksort.samplesizer;

public class OneItemSampleSizer 
  implements OversamplingSampleSizer {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int getSampleSize(int count, int radix) {
    return 1;
  }
  @Override
  public int getOverSamplingFactor(int count, int radix) {
    return 0;
  }
}
