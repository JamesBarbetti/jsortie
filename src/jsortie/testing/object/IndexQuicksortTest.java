package jsortie.testing.object;

import org.junit.Test;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.heapsort.ObjectHeapSort;
import jsortie.object.indexed.HalfIndexQuicksort;
import jsortie.object.indexed.IndexCentripetalPartitioner;
import jsortie.object.indexed.IndexPartitioner;
import jsortie.object.indexed.IndexQuicksort;
import jsortie.object.indexed.IndexSingletonPartitioner;
import jsortie.object.janitor.ObjectBinaryInsertionSort;
import jsortie.object.quicksort.governor.ArrayObjectQuicksort;
import jsortie.object.quicksort.multiway.selector.RemedianPivotObjectSelector;
import jsortie.object.quicksort.partitioner.CentripetalObjectPartitioner;
import jsortie.object.quicksort.partitioner.SinglePivotObjectPartitioner;
import jsortie.object.quicksort.partitioner.SingletonObjectPartitioner;
import jsortie.object.quicksort.selector.SinglePivotObjectSelector;

public class IndexQuicksortTest 
  extends ObjectSortTest {
  @Test
  public void testIndexQuicksort() {
    IndexPartitioner indexSingleton   
      = new IndexSingletonPartitioner();
    IndexPartitioner indexCentripetal 
      = new IndexCentripetalPartitioner();
    SinglePivotObjectSelector<MyInteger> rem 
      = new RemedianPivotObjectSelector<MyInteger>();
    SinglePivotObjectPartitioner<MyInteger> singleton
      = new SingletonObjectPartitioner<MyInteger>();
    SinglePivotObjectPartitioner<MyInteger> sentry 
      = new CentripetalObjectPartitioner<MyInteger>();
    ObjectRangeSorter<MyInteger> bin 
      = new ObjectBinaryInsertionSort<MyInteger>();
    ObjectRangeSorter<MyInteger> heap 
      = new ObjectHeapSort<MyInteger>();
    ObjectSortList<MyInteger> sorts 
    = new ObjectSortList<MyInteger>(); 
    sorts.add ( new ArrayObjectQuicksort<MyInteger>
                    ( rem, singleton, bin , 32, heap )
              , "ArrayObjectQuicksort-S");
    sorts.add ( new ArrayObjectQuicksort<MyInteger>
                   ( rem, sentry, bin, 32, heap )
              , "ArrayObjectQuicksort-C");
    sorts.add (new IndexQuicksort<MyInteger>(null,     indexSingleton,   32,   null), "IndexQuicksort-S"); 
    sorts.add (new IndexQuicksort<MyInteger>(null,     indexCentripetal, 32,   null), "IndexQuicksort-C"); 
    sorts.add (new HalfIndexQuicksort<MyInteger>(null, indexSingleton,   4096, null), "HalfIndexQuicksort-S"); 
    sorts.add (new HalfIndexQuicksort<MyInteger>(null, indexCentripetal, 4096, null), "HalfIndexQuicksort-C"); 
    //Equivalent (in every way!) but using index-sorting versions 
    //of the selector, partitioner, and janitor
    warmUpSorts(sorts, comp, 100);
    int oldRunCount = runCount;
    runCount = 800;
    sorts.writeSortHeader("n");
    for (int n=1000; n<=10000000; n*=10) {
      runCount /= 2;
      testSpecificSortsOnRandomInput(sorts, 1, MyInteger.integerComparator, n, false, "millisecond");
    }
    testSpecificSortsOnSortedInput
      ( sorts, MyInteger.integerComparator, 1000000, false, "millisecond" );
    runCount = oldRunCount;
  }

}
