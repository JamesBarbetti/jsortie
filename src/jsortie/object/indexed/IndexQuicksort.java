package jsortie.object.indexed;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class IndexQuicksort<T> 
  implements ObjectRangeSorter<T> {
  MultiPivotUtils mpu = new MultiPivotUtils();
  protected IndexPivotSelector selector
    = new IndexRemedianSelector();
  protected IndexPartitioner   partitioner
    = new IndexSingletonPartitioner();
  protected int janitorThreshold = 32;
  protected IndexSorter janitor 
    = new IndexBinaryInsertionSort();
  @Override
  public String toString() {
    return this.getClass().getSimpleName() 
      + "( " + selector.toString() 
      + ", " + partitioner.toString()
      + ", " + janitorThreshold
      + ", " + janitor.toString() 
      + ")";
  }
  protected class IndexQuicksortImplementation {
    T[]                   vArray;
    int[]                 iIndices;
    Comparator<? super T> comparator;
    IndexComparator       indexComparator;
    public IndexQuicksortImplementation
      ( Comparator<? super T> comparatorToUse
      , T[] vArrayToSort, int count ) {
      vArray     = vArrayToSort;
      comparator = comparatorToUse;
    }
    int[]  sortIndexRange(int start, int stop) {
      iIndices = new int[stop-start];
      for (int i=start; i<stop; ++i) {
        iIndices[i-start]=i;
      }
      indexComparator 
        = new IndexArrayComparator<T>
              ( comparator, vArray, iIndices );
      sortIndexSubRange(start, stop);
      return iIndices;
    }
    public void sortIndexSubRange
      ( int start, int stop ) {
      while (janitorThreshold<stop-start) {
        int[] iiPivots 
          = selector.selectPivotsForIndexRange
            ( indexComparator, start, stop );
        int[] iiBoundaries 
          = partitioner.partitionIndexRange
            ( indexComparator, start, stop, iiPivots );
        int bigPartition 
          = mpu.indexOfLargestPartition(iiBoundaries);
        for (int r=0; r<iiBoundaries.length; r+=2) {
          if (iiBoundaries.length==0) {
            return;
          } else if (r!=bigPartition) {
            sortIndexSubRange ( iiBoundaries[r]
                              , iiBoundaries[r+1]); 
          }
        }
        start = iiBoundaries[bigPartition];
        stop  = iiBoundaries[bigPartition+1];
      }
      if (start+1<stop) {
        janitor.sortIndexRange
          ( indexComparator, start, stop );
      }
    }
    @SuppressWarnings("unchecked")
		public void finishSort 
      ( T[] vArray, int start, int stop
      , int[] iSortedIndices) {
      int count = stop-start;
      Object[] vCopy = new Object[count];
      for (int i=0; i<count; ++i) {
        vCopy[i] = vArray[iSortedIndices[i]];
      }
      for (int i=0; i<count; ++i) {
        vArray[i+start] = (T) vCopy[i];
      }
    }
  }
  public IndexQuicksortImplementation 
    getImplementation
    ( Comparator<? super T> comparator
    , T[] vArray, int count ) {
    return new IndexQuicksortImplementation 
               ( comparator, vArray, count );
  }
  public IndexQuicksort() {
  }
  public IndexQuicksort
    ( IndexPivotSelector selectorToUse
    , IndexPartitioner partitionerToUse
    , int janitorThresholdToUse
    , IndexSorter  janitorToUse) {
    if (selectorToUse!=null) {
      selector = selectorToUse;
    }
    if (partitionerToUse!=null) {
      partitioner = partitionerToUse;
    }
    if (1<janitorThresholdToUse) {
      janitorThreshold = janitorThresholdToUse;
    }
    if (janitorToUse!=null) {
      janitor= janitorToUse;
    }
  }
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop ) {
    int count = stop-start;
    if (1<count) {
      IndexQuicksortImplementation imp 
        = getImplementation(comparator, vArray, count);
      int iSortedIndices[] 
        = imp.sortIndexRange(start, stop);
      imp.finishSort
        ( vArray, start, stop, iSortedIndices );
    }
  }
}
