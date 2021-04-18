package jsortie.testing.object;

import java.util.Comparator;

import org.junit.Test;

import jsortie.helper.DumpRangeHelper;
import jsortie.object.ObjectRangeSorter;
import jsortie.object.heapsort.ObjectHeapSort;
import jsortie.object.janitor.ObjectBinaryInsertionSort;
import jsortie.object.janitor.ObjectInsertionSort;
import jsortie.object.mergesort.ArrayObjectMergesort;
import jsortie.object.mergesort.ObjectSpliceSort;
import jsortie.object.mergesort.ObjectTimsortWrapper;
import jsortie.object.mergesort.WeakObjectMergesort;
import jsortie.object.mergesort.ZonedMergesort;
import jsortie.object.quicksort.governor.ArrayObjectQuicksort;

public class ObjectMergeSortTest extends ObjectSortTest {
  public static ObjectRangeSorter<MyInteger> getTimsort() {
    return new ObjectTimsortWrapper<MyInteger>();
  }
  public static ObjectRangeSorter<MyInteger> getMergesort
    ( ObjectRangeSorter<MyInteger> bisort, int threshold) {
    return new ArrayObjectMergesort<MyInteger>(bisort, threshold);
  }
  static ObjectRangeSorter<MyInteger> bisort = new ObjectBinaryInsertionSort<MyInteger>();
  static ArrayObjectMergesort<MyInteger> aom = new ArrayObjectMergesort<MyInteger>(bisort, 32);
  static ZonedMergesort<MyInteger> zom = new ZonedMergesort<MyInteger>(aom);
  static Comparator<MyInteger> comparator = MyInteger.integerComparator;

  public static ObjectSortList<MyInteger> getCollectionMergesorts() {
    ObjectSortList<MyInteger> sorts = new ObjectSortList<MyInteger>();
    sorts.add(new ObjectHeapSort<MyInteger>());
    sorts.add(new ObjectTimsortWrapper<MyInteger>());
    sorts.add(aom);
    sorts.add(zom);
    sorts.add(new ObjectSpliceSort<MyInteger>(bisort, 32));
    return sorts;
  }
  @Test
  public void testZoneSortSmall() {
    for (int n = 20; n<100; n+=5 ) {
      int       input[] = random.randomPermutation(n);
      MyInteger copy[]  = new MyInteger[n];	
      for (int i=0; i<n; ++i) {
        copy[i] = new MyInteger(input[i]);
      }
      ObjectRangeSorter<MyInteger> j
        = new ObjectInsertionSort<MyInteger>();
      ObjectRangeSorter<MyInteger> s 
        = new ZonedMergesort<MyInteger>
          ( new ArrayObjectMergesort<MyInteger>(j, 4));
      s.sortRange(comp, copy, 0, copy.length);
      for (int i=0; i<n; ++i) {
        input[i] = copy[i].intValue();
      }
      DumpRangeHelper.dumpRange("out", input, 0, input.length);
    }
  }
  @Test
  public void testZoneSort() {
    int n = 1048576;
    ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>();
    sorts.add(new ObjectTimsortWrapper<MyInteger>());
    sorts.add(aom);
    sorts.add(zom);
    ObjectSortTest x 
      = ObjectSortTest.newObjectSortTest(10, n, n);
    x.testSpecificSortsOnSortedInput
      ( sorts, MyInteger.integerComparator
      , n, false, "millisecond");
    System.out.println("");
    testSpecificSortsOnDuplicatedInput
      ( false, "==", sorts
      , comparator, n, false, "millisecond");
  }
  @Test
  public void testWeakMergesort() {
    WeakObjectMergesort<MyInteger> weako 
      = new WeakObjectMergesort<MyInteger>();
    ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>();
    //sorts.add(new ObjectTimsortWrapper<MyInteger>());
    sorts.add(new ArrayObjectQuicksort<MyInteger>());
    //sorts.add(aom);
    sorts.add(weako);
    warmUpSorts
      ( sorts, MyInteger.integerComparator
      , 1000);
    int n = 1048576;
    ObjectSortTest x 
      = ObjectSortTest.newObjectSortTest(10, n, n);
    x.testSpecificSortsOnSortedInput
      ( sorts, MyInteger.integerComparator
      , n, false, "millisecond");
  }
  
}
