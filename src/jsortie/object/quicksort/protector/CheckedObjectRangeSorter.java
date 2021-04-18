package jsortie.object.quicksort.protector;

import java.util.Arrays;
import java.util.Comparator;

import jsortie.exception.SortingFailureException;
import jsortie.helper.DumpRangeHelper;
import jsortie.object.ObjectRangeSorter;

public class CheckedObjectRangeSorter<T>
  implements ObjectRangeSorter<T> {
  protected ObjectRangeSorter<T> innerSorter;
  
  public CheckedObjectRangeSorter
    ( ObjectRangeSorter<T> innerSorter ) {
    this.innerSorter = innerSorter;
  }  
  @Override
  public String toString() {
    return "Checked(" 
      + innerSorter.toString() + ")";
  }  
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    if (start+1<stop) {
      @SuppressWarnings("unchecked")
      T[] vCopy = (T[]) new Object[stop-start];
      for (int i=start; i<stop; ++i) {
        vCopy[i-start] = vArray[i];
      }
      Arrays.sort(vCopy, comparator);
      innerSorter.sortRange
      ( comparator, vArray, start, stop );
      for (int i=start; i<stop; ++i) {
        if ( comparator.compare
             ( vArray[i] , vCopy[i-start]) != 0 ) {
          if ( vCopy.length < 100 ) {
            DumpRangeHelper.dumpRange
              ( "output", vArray, start, stop );
            DumpRangeHelper.dumpRange
              ( "expected", vCopy, 0, stop-start );
          }          
          throw new SortingFailureException(
            innerSorter.getClass().getSimpleName() 
            + " did not sort array subrange" 
            + " [" + start + ".." + (stop-1) + "]"
            + " correctly: in it's output element"
            + " [" + i + "] was " + vArray[i].toString()
            + " but should have been " + vCopy[i-start].toString());
        }
      }
    } else {
      innerSorter.sortRange
        ( comparator, vArray, start, stop );
    }
  }
}
