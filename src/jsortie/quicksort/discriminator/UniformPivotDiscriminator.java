package jsortie.quicksort.discriminator;

public class UniformPivotDiscriminator
  implements Discriminator {
  protected int firstIndex;
  protected int divisor;
  public UniformPivotDiscriminator
    ( int firstPivotIndex, int pivotStep ) {
    if (firstIndex<0) {
      throw new IllegalArgumentException
        ( "firstIndex (" + firstIndex + "):"
        + " may not be negative");
    } else if (divisor<=0) {
      throw new IllegalArgumentException
        ( "Illegal divisor (" + divisor + "):"
        + " must be positive");
    
    }
    firstIndex = firstPivotIndex; 
    divisor = pivotStep; 
  }
  @Override
  public void pushState() {
  }
  @Override
  public void popState() {
  }
  @Override
  public boolean ofInterest(int start, int stop) {
    if ( divisor < stop-start ) { 
      return true; //so big, it must be  
    } 
    if ( stop-start < 2 ) { 
      return false; //so small, it can't be 
    } 
    if ( stop  <= firstIndex ) { 
      return false; //out of range, it can't be 
    } 
    if ( start <= firstIndex ) { 
      return true;  
    } //start on or left of first index, it must be 
    //now we have 0<=firstIndex<=start<stop.  
    //So the dividends in the following
    //expression must both be positive.
    return ( (start-firstIndex) / divisor )
         < ( (stop-1-firstIndex) / divisor );
  }
  @Override
  public int getTargetIndex(int start, int stop) {
    int a = (start - firstIndex)/divisor;
    int b = (stop  - firstIndex)/divisor;
    int c = (a + (b-a)/2) * divisor + firstIndex;
    if ( c<start ) {
      return start;
    } else if ( c<stop) {
      return c;
    } else {
      return stop-1;
    }
  }
}
