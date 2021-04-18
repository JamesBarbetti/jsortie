package jsortie.janitors.insertion;

import jsortie.StableRangeSorter;
import jsortie.flags.QuadraticAverageCase;

public class InstrumentedBinaryInsertionSort 
  implements StableRangeSorter
           , QuadraticAverageCase {
  double comparisonCount         = 0.0;
  double moveCount               = 0.0;
  double overheadComparisonCount = 0.0;
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
  public double getComparisonCount() {
    return comparisonCount;
  }
  public double getMoveCount() {
    return moveCount;
  }
  public double getOverheadComparisonCount() {
    return overheadComparisonCount;
  }
  public int findPostInsertionPoint
    ( int[] vArray, int start, int stop, int v ) {
    //Finds a place to insert a value that came 
    //from a place to the right of the sorted 
    //sub-range (that is, the index of the 
    //first item that is greater, or stop, 
    //if none is).
    double oldCount = comparisonCount;
    while ( start < stop ) {
      int middle = start + (stop - start ) / 2;
      ++comparisonCount;
      if ( v < vArray[middle]) {
        stop = middle;
      } else {
        start = middle + 1;
      }
    }
    overheadComparisonCount += 
      (comparisonCount - oldCount)
      - Math.log(stop+1-start) / Math.log(2.0);
    return stop;
  }
  public int findPreInsertionPoint
    ( int[] vArray, int start, int stop, int v ) {
    //Finds a place to insert a value that comes
    //from a place to the left of the sorted
    //sub-range (that is, the index of the 
    //first item that is greater than,
    //*or* equal to, v, or stop, if none is.
    double oldCount = comparisonCount;
    while ( start < stop ) {
      int middle = start + (stop - start) / 2;
      ++comparisonCount;
      if ( v<= vArray[middle]) {
        stop = middle;
      } else {
        start = middle + 1;
      }
    }
    overheadComparisonCount +=
      (comparisonCount - oldCount) - Math.log(stop+1-start) / Math.log(2.0);
    return stop;
  }
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    for (int k=start+1; k<stop; ++k) {
      int v= vArray[k];
      int i = findPostInsertionPoint
              (vArray, start, k, v);
      moveCount += k - i + 1;
      for (int j=k; i<j; --j) {
        vArray[j] = vArray[j - 1];
      }
      vArray[i] = v;
    }
  }
  public void zeroCounts() {
    comparisonCount = 0;
    moveCount = 0;
    overheadComparisonCount = 0;
  }
}
