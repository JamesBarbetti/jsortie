package jsortie.janitors.insertion;

import jsortie.StableRangeSorter;
import jsortie.flags.QuadraticAverageCase;

public class BranchAvoidingBinaryInsertionSort
  implements StableRangeSorter, QuadraticAverageCase {
  public int findPostInsertionPoint
    (int[] vArray, int start, int stop, int v) {
    //Finds a place to insert a value that came 
    //from a place to the right of the sorted 
    //sub-range (that is, the index of the 
    //first item that is greater, or stop, 
    //if none is).
    while ( start < stop ) {
      int middle = start + (stop - start ) / 2;
      boolean less = ( v < vArray[middle]);
      stop  = less ? middle : stop;
      start = less ? start  : (middle+1);
    }
    return stop;
  }
  public int findPreInsertionPoint
    (int[] vArray, int start, int stop, int v) {
    //Finds a place to insert a value that comes
    //from a place to the left of the sorted
    //sub-range (that is, the index of the 
    //first item that is greater than,
    //*or* equal to, v, or stop, if none is.
    while ( start < stop ) {
      int middle = start + (stop - start) / 2;
      boolean lessOrEqual = ( v<= vArray[middle]);
      stop  = lessOrEqual ? middle : stop;
      start = lessOrEqual ? start  : (middle+1);
    }
    return stop;
  }
  @Override
  public void sortRange
    (int[] vArray, int start, int stop) {
    for (int k=start+1; k<stop; ++k) {
      int v= vArray[k];
      int g = start;
      int i = k;
      while ( g < i ) {
        int h = g + (i- g) / 2;
        boolean lessOrEqual = ( v<= vArray[h]);
        g = lessOrEqual ? g : (h+1);
        i = lessOrEqual ? h : i;
      }
      for (int j=k; i<j; --j) {
        vArray[j] = vArray[j - 1];
      }
      vArray[i] = v;
    }
  }
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }

}
