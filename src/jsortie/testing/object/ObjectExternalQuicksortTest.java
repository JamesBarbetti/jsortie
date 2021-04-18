package jsortie.testing.object;


import org.junit.Test;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.janitor.ObjectBinaryInsertionSort;
import jsortie.object.quicksort.external.ExternalObjectQuicksort;
import jsortie.object.quicksort.governor.ArrayObjectQuicksort;
import jsortie.object.quicksort.selector.MiddleElementObjectSelector;
import jsortie.object.quicksort.selector.SinglePivotObjectSelector;

public class ObjectExternalQuicksortTest 
  extends ObjectSortTest {
  @Test
  public void testExternalObjectQuicksortsOnSortedInputs() {
    ObjectRangeSorter<MyInteger>         qsort  = new ArrayObjectQuicksort<MyInteger>();
    SinglePivotObjectSelector<MyInteger> middle = new MiddleElementObjectSelector<MyInteger>();
    ObjectRangeSorter<MyInteger>         bisort = new ObjectBinaryInsertionSort<MyInteger>();
    ObjectRangeSorter<MyInteger>         xoqs   = new ExternalObjectQuicksort<MyInteger>(middle, bisort, 32);
    ObjectSortList<MyInteger>            sorts  = new ObjectSortList<MyInteger>(); 
    sorts.add(qsort);
    sorts.add(xoqs);
    warmUpSorts(sorts, comp, 100);
    testSpecificSortsOnSortedInput(sorts, MyInteger.integerComparator, 100000, false, "millisecond");	  
  }
}
