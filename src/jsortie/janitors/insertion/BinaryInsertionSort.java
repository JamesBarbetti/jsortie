package jsortie.janitors.insertion;

import jsortie.StableRangeSorter;
import jsortie.flags.QuadraticAverageCase;

public class BinaryInsertionSort
implements StableRangeSorter, QuadraticAverageCase {
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
  public static int findPostInsertionPoint
    (int[] vArray, int start, int stop, int v) {
    //Finds a place to insert a value that came 
    //from a place to the right of the sorted 
    //sub-range (that is, the index of the 
    //first item that is greater, or stop, 
    //if none is).
    while ( start < stop ) {
      int middle = start + (stop - start ) / 2;
      if ( v < vArray[middle]) {
        stop = middle;
      } else {
        start = middle + 1;
      }
    }
    return stop;
  }
  public static int findPreInsertionPoint
    (int[] vArray, int start, int stop, int v) {
    //Finds a place to insert a value that comes
    //from a place to the left of the sorted
    //sub-range (that is, the index of the 
    //first item that is greater than,
    //*or* equal to, v, or stop, if none is.
    while ( start < stop ) {
      int middle = start + (stop - start) / 2;
      if ( v<= vArray[middle]) {
        stop = middle;
      } else {
        start = middle + 1;
      }
    }
    return stop;
  }
  @Override
  public void sortRange
    (int[] vArray, int start, int stop) {
    for (int k=start+1; k<stop; ++k) {
      int v= vArray[k];
      //The following is basically an in-lined
      //version of findPostInsertionPoint.
      //In-lining it shouldn't be any faster...
      //But in practice, it is!
      //(I only found this out benchmarking
      // objects with ObjectBinaryInsertionSort)
      int h = 0;
      int i = k;
      while ( h < i ) {
        int middle = h + (i - h ) / 2;
        if ( v < vArray[middle] ) {
          i = middle;
        } else {
          h = middle + 1;
        }
      }
      for (int j=k; i<j; --j) {
        vArray[j] = vArray[j - 1];
      }
      vArray[i] = v;
    }
  }
  public static void sortSmallRange
    ( int[] vArray, int start, int stop ) {
    for (int k=start+1; k<stop; ++k) {
      int v= vArray[k];
      int i = findPostInsertionPoint
              (vArray, start, k, v);
      for (int j=k; i<j; --j) {
        vArray[j] = vArray[j - 1];
      }
      vArray[i] = v;
    }
  }
}
