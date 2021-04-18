package jsortie.quicksort.collector.external;

public class ExternalNullCollector
  implements ExternalSampleCollector {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public Object collectSampleInExternalArray 
    ( int[] vSourceArray, int start, int stop
    , int[] vSampleArray
    , int sampleStart, int sampleStop) {
    int m = stop - start;
    int c = sampleStop - sampleStart;
    int r = start + ( m - c ) / 2; 
    for ( int w = sampleStart ; w < sampleStop
        ; ++w, ++r) {
      vSampleArray[w] = vSourceArray[ r ];
    }
    return null;
  }
  @Override
  public int indexOfSampleItemInSourceArray
    ( Object state, int vItem
    , int sampleStart, int sampleStop
    , int[] vArray, int start, int stop) {
    //Note: it is assumed that vItem is found
    //in vArray[sampleStart..sampleStop-1].
    int c = sampleStop - sampleStart;
    int l = sampleStart + ( c >> 1 );
    int r = l + 1 - (c&1);
    do {
      if ( vArray[l] == vItem ) {
        return l;
      } else if ( vArray[r] == vItem ) {
        return r;
      }
      --l;
      ++r;
    } while (sampleStart<=l);
    return sampleStart + ( c >> 1 );
  }
}
