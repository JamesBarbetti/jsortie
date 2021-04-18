package jsortie.testing;

import org.junit.Test;

import jsortie.heapsort.topdown.HeapsortStandard;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.librarysort.LibrarySort;
import jsortie.quicksort.governor.adaptive.QuicksortAdaptive2;
import jsortie.quicksort.partitioner.bidirectional.centrepivot.CentrePivotPartitioner;
import jsortie.quicksort.selector.simple.MiddleElementSelector;

public class LibrarySortTest  {
  public SortTest sortTest = new SortTest();
  @Test
  public void testLibrarySortSmall() {
    SortList sorts = getLibrarySorts();
    sortTest.warmUpSorts(sorts);
    sortTest.testSpecificSorts( sorts, 10, 10000, 25, 10000000, 1);
    sortTest.testOnDuplicates(sorts, 10);
  }	
  @Test
  public void testLibrarySortSorted() {
    SortList sorts = getLibrarySorts();
    sortTest.warmUpSorts(sorts);
    sortTest.testOnOrderedInputs(sorts, 65536, "millisecond");
  }	
  public SortList getLibrarySorts() {
    SortList sorts = new SortList();
    sorts.add(new QuicksortAdaptive2( new MiddleElementSelector(), new CentrePivotPartitioner(), new InsertionSort2Way(), 5, new HeapsortStandard()));
    sorts.add(new LibrarySort(1.1));
    sorts.add(new LibrarySort(1.5));
    sorts.add(new LibrarySort(1.9));
    sorts.add(new LibrarySort(2));
    sorts.add(new LibrarySort(4));
    return sorts;
  }
}
