package jsortie.quicksort.samplesizer;

public class ChenSampleSizer 
  extends SampleSortSizer {
  double alpha;
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(alpha=" + alpha + ")";
  }
  public ChenSampleSizer() {
    super(0);
    alpha = 16.0;
  }  
  public ChenSampleSizer
    ( double alphaToUse ) {
    super(0);
    if (alphaToUse<1.0) {
      throw new IllegalArgumentException
        ( this.getClass().getSimpleName() 
        + " cannot have alpha"
        + " (" + alphaToUse + ") "
        + " less than 1.0");
    }
    this.alpha = alphaToUse;
  }	
  public ChenSampleSizer
    ( double alphaToUse, int oversample ) {
    super(oversample);
    if (alphaToUse<1.0) {
      throw new IllegalArgumentException
        ( this.getClass().getSimpleName() 
        + " cannot have alpha "
        + " (" + alphaToUse + ")" 
        + " less than 1.0");
    }
    this.alpha = alphaToUse;
  }	
  public int getSampleSize(int count, int radix) {
    int size = (int)Math.floor( (double)count / alpha) ;
    if ( size<1 ) {
      size = 1;
    }
    return size;
  }
}
