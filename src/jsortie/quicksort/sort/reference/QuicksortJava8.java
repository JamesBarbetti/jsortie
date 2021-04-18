package jsortie.quicksort.sort.reference;

import jsortie.RangeSorter;

public class QuicksortJava8
  implements RangeSorter {
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    if (start==0 && stop == vArray.length) {
      java.util.Arrays.sort( vArray );
    } else {
      int[] copy = java.util.Arrays.copyOfRange
                   ( vArray, start, stop-1 );
      java.util.Arrays.sort( copy );
      for (int i=0; i<copy.length; ++i) {
        vArray[i + start] = copy[i];
      }
    }
  }	
}
