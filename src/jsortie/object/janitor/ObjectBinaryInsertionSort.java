package jsortie.object.janitor;

import java.util.Comparator;

import jsortie.flags.QuadraticAverageCase;
import jsortie.object.ObjectRangeSorter;

public class ObjectBinaryInsertionSort<T>
  implements ObjectRangeSorter<T>
           , QuadraticAverageCase {
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public int findPostInsertionPoint
    ( Comparator<? super T> c, T[] vArray
    , int start, int stop, T v ) {
    while ( start < stop ) {
      int middle = start + (stop - start ) / 2;
      if ( c.compare(v, vArray[middle]) < 0 ) {
        stop = middle;
      } else {
        start = middle + 1;
      }
    }
    return stop;
  }
  public int findPreInsertionPoint
    ( Comparator<? super T> c, T [] vArray
    , int start, int stop, T v ) {
    while ( start < stop ) {
      int middle = start + (stop - start ) / 2;
      if ( c.compare(v, vArray[middle]) <= 0 ) {
        stop = middle;
      } else { 
        start = middle + 1;
      }
    }
    return stop;
  }
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop ) {
    for (int k=start+1; k<stop; ++k) {
      T v = vArray[k];
      //The following is basically an in-lined
      //version of findPostInsertionPoint.
      //In-lining it shouldn't be any faster,
      //but in practice, seems to be almost
      //twice as fast, when sorting 
      //integer-keyed objects. Damfino why.
      int h = 0;
      int i = k;
      while ( h < i ) {
        int middle = h + (i - h ) / 2;
        if ( comparator.compare
             ( v, vArray[middle] ) < 0 ) {
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
}
