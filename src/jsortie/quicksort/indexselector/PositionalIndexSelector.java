package jsortie.quicksort.indexselector;

import jsortie.exception.SortingFailureException;

public class PositionalIndexSelector 
  implements IndexSelector {
  protected int[] numerators;
  protected int   denominator;
  public PositionalIndexSelector() {
  }
  public PositionalIndexSelector
    ( int pivotCount ) {
    this.denominator = pivotCount+1;
  }
  public PositionalIndexSelector
    ( int[] numerators, int denominator ) {
    if (numerators==null) {
      throw new NullPointerException
        ( this.getClass().getSimpleName() 
        + " was passed null numerators array");
    }
    this.denominator = denominator;
  }
  @Override
  public String toString() {
    String s = this.getClass().getSimpleName();
    s += "(";
    for (int i=0; i<numerators.length; ++i) {
      s += ((i>0) ? "," : "") + numerators[i];
    }
    s+= "/" + denominator +")";
    return s;
  }	
  @Override
  public int[] selectIndices
    ( int start, int stop, int count ) {
    if (count!=numerators.length) {
      throw new IllegalArgumentException
        ( "selectIndices() method on a " 
        + numerators.length + "-index " 
        + this.getClass().getSimpleName()
        + " was passed a count of " + count);
    }
    int itemCount  = stop-start;
    int [] indices = new int[numerators.length];
    for (int i=0; i<numerators.length; ++i) {
      indices[i] = start 
        + (itemCount)*numerators[i]/denominator;
      if (0<i && indices[i]<=indices[i-1]) {
        indices[i]=indices[i-1]+1;
      }
    }	
    if (stop <= indices[numerators.length-1] ) {
      //ran out of room!
      //Need to move stuff left to make room
      int i = numerators.length-1;
      indices[i] = stop-1;
      for (--i;i>=0;--i) {
        if (indices[i+1] <= indices[i]) {
          indices[i] = indices[i+1]-1;
        } else {
          break;
        }
      }
      if (indices[0]<start) {
        throw new SortingFailureException
          ( "Failed to select " + numerators.length 
          + " pivot indices in the range "
          + start + " through " + stop);
      }
    }		
    return indices;
  }	
  public int getIndexCount() {
    return numerators.length;
  }
}
