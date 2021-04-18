package jsortie.object.collector;

public class ObjectChenCollector<T> 
  extends ObjectPositionalCollector<T> {
  double alpha = 16;
  public String toString() {
    return this.getClass().getSimpleName()
           + "(alpha=" + alpha + ")";
  }
  public ObjectChenCollector 
    ( double alpha ) {
    super(0);
    if (alpha<=1.0) {
      throw new IllegalArgumentException
        ( "ObjectChenCollector cannot have alpha"
        + " (" + alpha + ") less than or equal to 1.0");
    }
    this.alpha = alpha;
  }	
  public ObjectChenCollector
    ( double alpha, int oversample ) {
    super(oversample);
    this.alpha = alpha;
  }	
  public int getSampleSize(int count, int radix) {
    int size = (int)Math.floor( (double)count / alpha) ;
    size += 1 - (size&1);
    return size;
  }  
}
