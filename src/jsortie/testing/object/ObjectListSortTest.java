package jsortie.testing.object;

import org.junit.Test;

import jsortie.object.quicksort.governor.ArrayObjectQuicksort;
import jsortie.object.quicksort.linkedlist.FancierLinkedListQuicksort;
import jsortie.object.quicksort.linkedlist.JavaLinkedListQuicksort;
import jsortie.object.quicksort.linkedlist.QuickerLinkedListQuicksort;
import jsortie.object.quicksort.linkedlist.SamplingLinkedListQuicksort;
import jsortie.object.quicksort.linkedlist.StableLinkedListQuicksort;

public class ObjectListSortTest
  extends ObjectSortTest {
  public ObjectSortList<MyInteger> getListQuicksorts() {
    ObjectSortList<MyInteger> sorts = new ObjectSortList<MyInteger>();
    sorts.add(new ArrayObjectQuicksort<MyInteger>());
    sorts.add(new JavaLinkedListQuicksort<MyInteger>());
    sorts.add(new QuickerLinkedListQuicksort<MyInteger>());
    sorts.add(new StableLinkedListQuicksort<MyInteger>());
    sorts.add(new SamplingLinkedListQuicksort<MyInteger>(15));
    sorts.add(new SamplingLinkedListQuicksort<MyInteger>(255));
    sorts.add(new SamplingLinkedListQuicksort<MyInteger>(4095));
    sorts.add(new FancierLinkedListQuicksort<MyInteger>(15));
    sorts.add(new FancierLinkedListQuicksort<MyInteger>(255));
    sorts.add(new FancierLinkedListQuicksort<MyInteger>(4095));
    return sorts;
  }	
  @Test
  public void testLinkedListSorts() {
    runCount = 10000; //was 10k
    maxCount = 1000000;
    ObjectSortList<MyInteger> sorts = this.getListQuicksorts();
    warmUpSorts(sorts, comp, 1000 );
    testSpecificSorts(sorts, false, false);
  }
  @Test
  public void testQLLQuicksortOnSortedInputs() {
    ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>();
    sorts.add(new ArrayObjectQuicksort<MyInteger>());
    sorts.add(new QuickerLinkedListQuicksort<MyInteger>());
    sorts.add(new StableLinkedListQuicksort<MyInteger>());
    sorts.add(new SamplingLinkedListQuicksort<MyInteger>(255));
    sorts.add(new FancierLinkedListQuicksort<MyInteger>(255));
    warmUpSorts(sorts, comp, 100);
    runCount = 100;
    this.testSpecificSortsOnSortedInput
      ( sorts, MyInteger.integerComparator
      , 10000, false, "millisecond");
    System.out.println("");
    this.testSpecificSortsOnDuplicatedInput
      ( false, "==", sorts, comp
      , 10000, false, "millisecond" );
  }
}
