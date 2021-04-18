package jsortie.object.collector;


public class ObjectBalancedChenCollector<T> 
  extends ObjectChenCollector<T> {
  public ObjectBalancedChenCollector(double alpha) {
    super(alpha);
  }
  public ObjectBalancedChenCollector
    (double alpha, int oversample) {
    super(alpha, oversample);
  }	
  public int getSampleSize(int count, int radix) {
    int maxSize = (int)Math.floor( (double)count / alpha) ;
    int size = (radix * (this.overSamplingFactor + 1)) - 1;
    if ( maxSize < size ) return 1;
    int nextSize = ( size + 1 ) * radix - 1;
    while (nextSize<maxSize) {
      size = nextSize;
      nextSize = ( size + 1 ) * radix - 1;
    }
    return size;
  }
}
