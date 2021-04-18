package jsortie.quicksort.discriminator;

public class PartitionSizeDiscriminator 
  implements Discriminator {
  int threshold;
  public PartitionSizeDiscriminator
    (int janitorThreshold) {
    threshold = janitorThreshold;
  }
  @Override
  public void pushState() {
  }
  @Override
  public void popState() {
  }
  @Override
  public boolean ofInterest
    ( int start, int stop ) {
    return threshold < stop - start ;
  }
  @Override
  public int getTargetIndex
    ( int indexStart, int indexStop ) {
    int count = indexStop - indexStart;
    return indexStart + (count >> 1);
  }
}
