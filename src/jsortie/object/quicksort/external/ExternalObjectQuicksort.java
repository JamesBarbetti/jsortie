package jsortie.object.quicksort.external;

import java.util.Comparator;

import jsortie.object.ObjectRangeSortHelper;
import jsortie.object.ObjectRangeSorter;
import jsortie.object.quicksort.selector.SinglePivotObjectSelector;

public class ExternalObjectQuicksort<T> 
  implements ObjectRangeSorter<T> {
  ObjectRangeSorter<T> janitor;
  int threshold;
  SinglePivotObjectSelector<T> selector;
  ExternalObjectPartitioner<T> party;
  public ExternalObjectQuicksort
    ( SinglePivotObjectSelector<T> pivotSelector
    , ObjectRangeSorter<T> janitorRangeSorter
    , int janitorThreshold) {
    selector = pivotSelector;
    party = new ExternalObjectPartitioner<T>();
    janitor = janitorRangeSorter;
    threshold = janitorThreshold;
  }

  public ExternalObjectQuicksort
    ( SinglePivotObjectSelector<T> pivotSelector
    , ExternalObjectPartitioner<T> partitionerToUse
    , ObjectRangeSorter<T> janitorRangeSorter
    , int janitorThreshold) {
    selector = pivotSelector;
    party = partitionerToUse;
    janitor = janitorRangeSorter;
    threshold = janitorThreshold;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() 
        + "(" + selector.toString() 
        + "," + party.toString() 
        + "," + janitor.toString() 
        + "," + threshold + ")";
  }

  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    int count = stop - start;
    @SuppressWarnings("unchecked")
    T[] vAux = (T[]) new Object[count];
    sortRangeUsing 
      ( comparator, vArray, start, stop
      , vAux, 0, true );
  }

  public void sortRangeUsing
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , T[] vAuxArray, int auxStart
    , boolean inMainArray) {
    while (threshold < stop - start) {
      int leftCount;
      if (inMainArray) {
        int pivotIndex 
          = selector.selectPivotIndex
            ( comparator, vArray, start, stop );
        leftCount 
          = party.partitionMainRange
            ( comparator, vArray, start, stop, pivotIndex
            , vAuxArray, auxStart);
      } else {
        int auxStop = auxStart + (stop - start);
        int pivotIndexInAuxArray 
          = selector.selectPivotIndex
            ( comparator, vAuxArray, auxStart, auxStop );
        leftCount 
          = party.partitionAuxiliaryRange
            ( comparator, vArray, start
            , vAuxArray, auxStart, auxStop,
            pivotIndexInAuxArray);
      }
      if (leftCount + leftCount < stop - start) {
        // recurse for the (smaller) left child partition;
        sortRangeUsing
          ( comparator, vArray, start, start + leftCount
          , vAuxArray, auxStart + stop - start - leftCount
          , true);
        // and tail for the larger (right) partition
        start += leftCount + 1;
        inMainArray = false;
      } else {
        // recurse for the (smaller) right child partition
        sortRangeUsing
          ( comparator, vArray, start + leftCount + 1, stop
          , vAuxArray, auxStart, false );
        // and tail for the larger (left) partition
        stop = start + leftCount;
        inMainArray = true;
      }
    }
    if (!inMainArray) {
      int auxStop = auxStart + (stop - start);
      ObjectRangeSortHelper.copyRange
        ( vAuxArray, auxStart, auxStop
        , vArray, start );
    }
    janitor.sortRange(comparator, vArray, start, stop);
  }
}
