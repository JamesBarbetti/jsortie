package jsortie.object.indexed;


public class IndexBinaryInsertionSort
  implements IndexSorter {
  @Override
  public void sortIndexRange
    ( IndexComparator comparator, int start, int stop) {
    int[] iArray = comparator.getIndexArray();
    for (int k=start+1; k<stop; ++k) {
      int pivotIndex = iArray[k];
      int i = findPostInsertionPoint
              (comparator, start, k, pivotIndex);
      for (int j=k; i<j; --j) {
        iArray[j] = iArray[j - 1];
      }
      iArray[i] = pivotIndex;
    }
  }
  public int findPostInsertionPoint
    ( IndexComparator comparator, int start, int stop, int pivotIndex ) {
    while ( start < stop ) {
      int middle = start + (stop - start ) / 2;
      if ( comparator.comparePivotToIndex(pivotIndex , middle) < 0 ) {
        stop = middle;
      } else {
        start = middle + 1;
      }
    }
    return stop;
  }
  public int findPreInsertionPoint
    (IndexComparator comparator, int start, int stop, int pivotIndex) {
    while ( start < stop ) {
      int middle = start + (stop - start) / 2;
      if ( comparator.comparePivotToIndex(pivotIndex , middle) <= 0 ) {
        stop = middle;
      } else {
        start = middle + 1;
      }
    }
    return stop;
  }
}
