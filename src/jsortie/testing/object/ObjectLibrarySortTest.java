package jsortie.testing.object;

import org.junit.Test;

import jsortie.exception.SortingFailureException;
import jsortie.object.ObjectRangeSorter;
import jsortie.object.librarysort.ObjectLibrarySort;
import jsortie.object.librarysort.ObjectMiddenSort;
import jsortie.object.quicksort.governor.ArrayObjectQuicksort;
import jsortie.object.treesort.binary.balanced.RedBlackTreeSort;
import jsortie.testing.RandomInput;

public class ObjectLibrarySortTest 
  extends ObjectSortTest {
  @Test
  public void testObjectLibrarySort() {
    maxCount = 10*1000*1000;
    ObjectRangeSorter<MyInteger>   qsort
      = new ArrayObjectQuicksort<MyInteger>();
    ObjectRangeSorter<MyInteger>   rbtsort  
      = new RedBlackTreeSort<MyInteger>();
    ObjectRangeSorter<MyInteger>   libsort  
      = new ObjectLibrarySort<MyInteger>(1.5);
    //ObjectRangeSorter<MyInteger> midsort  
    //  = new ObjectMiddenSort<MyInteger>(1.5);
    //ObjectRangeSorter<MyInteger> libsort2 
    //  = new ObjectLibrarySort<MyInteger>(2);
    ObjectRangeSorter<MyInteger>   midsort2 
      = new ObjectMiddenSort<MyInteger>(2);
    ObjectSortList<MyInteger>      sorts
      = new ObjectSortList<MyInteger>(); 
    sorts.add(qsort);
    sorts.add(rbtsort);
    sorts.add(libsort);
    //sorts.add(midsort);
    //sorts.add(libsort2);
    sorts.add(midsort2);
    warmUpSorts(sorts, comp, 100);
    testSpecificSortsOnSortedInput
      ( sorts, MyInteger.integerComparator
      ,  65536*16, false, "millisecond");
    //testSpecificSorts(sorts, false, false);
  }
  @Test
  public void testOok() {
    RandomInput ri = new RandomInput();
    for (int run=0;run<5;++run) {
      int[] input = ri.randomPermutation(29);
      MyInteger[] input2 = new MyInteger[input.length];
      for (int i=0; i<input.length; ++i) {
        input2[i] = new MyInteger(input[i]);
      }
      ObjectRangeSorter<MyInteger> midsort 
        = new ObjectLibrarySort<MyInteger>(1.5);
      midsort.sortRange(comp, input2, 0, input2.length);
      for (int i=0; i<input2.length; ++i) {
        if (input2[i].intValue() != i) {
          throw new SortingFailureException
            ( "Input not sorted!" 
            + " Found " + input2[i].intValue() 
            + " at " + i);
        }
      }
    }
  }
}
