package jsortie.quicksort.indexselector;

import java.util.Arrays;

import jsortie.quicksort.indexselector.indexset.HashIndexSet;
import jsortie.quicksort.indexselector.indexset.IndexSet;

public class RandomIndexSelector 
  implements IndexSelector {
  public RandomIndexSelector() {}
  @Override
  public String toString() { 
    return this.getClass().getSimpleName();
  }	
  public int[] selectIndices
    ( int start, int stop, int sampleSize ) {
    int   count   = stop - start;
    int[] indices;
    if (count<sampleSize+sampleSize) {
      if (count<sampleSize) {
        throw new IllegalArgumentException(toString() 
          + " cannot select " + sampleSize 
          + " distinct indices" 
          + " from the range " + start 
          + " to " + stop + " inclusive");
      }
      IndexSet hashy 
        = getRandomSubset(start, stop, count-sampleSize);
      indices = new int[sampleSize];
      hashy.emitInRangeAndNotInSet
        ( start, stop, indices, 0 );
    } else {
      IndexSet hashy
        = getRandomSubset(start, stop, sampleSize);
      indices = new int[sampleSize];
      hashy.emit(indices, 0);
      Arrays.sort(indices);
    }
    return indices;
  }
  public IndexSet getRandomSubset
    ( int start, int stop, int sampleSize ) {
    HashIndexSet subset
      = new HashIndexSet(start, stop, sampleSize);
    int count = stop-start;
    for (int i=sampleSize; 0<i; --i) {
      int r;
      do {
        double q = Math.random() * count;
        r = start + (int) Math.floor( q);
      } while (!subset.merge(r));
    }
    return subset;
  }
}
