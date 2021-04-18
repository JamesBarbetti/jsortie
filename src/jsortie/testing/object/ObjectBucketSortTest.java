package jsortie.testing.object;


import org.junit.Test;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.bucket.DynamicArrayBucket;
import jsortie.object.bucket.FancierObjectBucketSort;
import jsortie.object.bucket.JavaLinkedListBucket;
import jsortie.object.bucket.LinkedListBucket;
import jsortie.object.bucket.ListOfArraysBucket;
import jsortie.object.bucket.ObjectBucketSort;
import jsortie.object.bucket.SortingBucket;
import jsortie.object.collector.ObjectExternalSampleCollector;
import jsortie.object.collector.ObjectPositionalCollector;
import jsortie.object.collector.ObjectRandomCollector;
import jsortie.object.quicksort.governor.ArrayObjectQuicksort;
import jsortie.quicksort.samplesizer.OversamplingSampleSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;

public class ObjectBucketSortTest
  extends ObjectSortTest {
  public static ObjectRangeSorter<MyInteger> getBucketSort() {
    OversamplingSampleSizer sqr 
      = new SquareRootSampleSizer();
    ObjectExternalSampleCollector<MyInteger>  pos 
      = new ObjectPositionalCollector<MyInteger>();
    ObjectRangeSorter<MyInteger> qsort 
      = new ArrayObjectQuicksort<MyInteger>();
    SortingBucket<MyInteger> arrayBucket 
      = new DynamicArrayBucket<MyInteger>(8);  
    return new FancierObjectBucketSort<MyInteger>(sqr, pos, arrayBucket,   qsort, 1024);
  }
  @Test
  public void testBucketSorts() {
    ObjectRangeSorter<MyInteger> qsort 
      = new ArrayObjectQuicksort<MyInteger>();
    OversamplingSampleSizer sqr 
      = new SquareRootSampleSizer();
    ObjectExternalSampleCollector<MyInteger> pos 
      = new ObjectPositionalCollector<MyInteger>();
    ObjectExternalSampleCollector<MyInteger>  ran 
      = new ObjectRandomCollector<MyInteger>();
    ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>(); 
    SortingBucket<MyInteger> arrayBucket 
      = new DynamicArrayBucket<MyInteger>(8);
    SortingBucket<MyInteger> labBucket 
      = new ListOfArraysBucket<MyInteger>(8);
    SortingBucket<MyInteger> listBucket 
      = new LinkedListBucket<MyInteger>();
    SortingBucket<MyInteger> javaBucket 
      = new JavaLinkedListBucket<MyInteger>();    
    sorts.add(qsort);
    sorts.add(new ObjectBucketSort<MyInteger>(sqr, ran, arrayBucket, qsort, 1024));
    sorts.add(new ObjectBucketSort<MyInteger>(sqr, pos, arrayBucket, qsort, 1024));
    sorts.add(new ObjectBucketSort<MyInteger>(sqr, pos, labBucket,   qsort, 1024));
    sorts.add(new ObjectBucketSort<MyInteger>(sqr, pos, listBucket,  qsort, 1024));
    sorts.add(new ObjectBucketSort<MyInteger>(sqr, pos, javaBucket,  qsort, 1024));
    warmUpSorts(sorts, comp, 100);
    testSpecificSortsOnSortedInput
      ( sorts, MyInteger.integerComparator, 100000, false, "millisecond" );
  }
  @Test
  public void testFancierBucketSort() {
    ObjectRangeSorter<MyInteger> qsort 
      = new ArrayObjectQuicksort<MyInteger>();
    OversamplingSampleSizer sqr 
      = new SquareRootSampleSizer();
    ObjectExternalSampleCollector<MyInteger> pos 
      = new ObjectPositionalCollector<MyInteger>();
    SortingBucket<MyInteger> arrayBucket 
      = new DynamicArrayBucket<MyInteger>(8);
    ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>(); 
    sorts.add(qsort);
    sorts.add(new ObjectBucketSort       <MyInteger>(sqr, pos, arrayBucket, qsort, 1024));
    sorts.add(new FancierObjectBucketSort<MyInteger>(sqr, pos, arrayBucket, qsort, 1024));
    warmUpSorts(sorts, comp, 100);
    testSpecificSortsOnSortedInput
      ( sorts, MyInteger.integerComparator, 100000, false, "millisecond" );
  }
  @Test
  public void testBucketSortsDifferentSizes() {
    OversamplingSampleSizer sqr 
      = new SquareRootSampleSizer();
    OversamplingSampleSizer sqr1
      = new SquareRootSampleSizer(1);
    OversamplingSampleSizer sqr3 
      = new SquareRootSampleSizer(3);
    ObjectExternalSampleCollector<MyInteger> pos  
      = new ObjectPositionalCollector<MyInteger>();
    ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>(); 
    SortingBucket<MyInteger> arrayBucket 
      = new DynamicArrayBucket<MyInteger>(8);	  
    ObjectRangeSorter<MyInteger> qsort 
      = new ArrayObjectQuicksort<MyInteger>();
    sorts.add(qsort);
    sorts.add(new ObjectBucketSort<MyInteger>(sqr, pos,  arrayBucket, qsort, 1024));
    sorts.add(new ObjectBucketSort<MyInteger>(sqr1, pos, arrayBucket, qsort, 1024));
    sorts.add(new ObjectBucketSort<MyInteger>(sqr3, pos, arrayBucket, qsort, 1024));
    warmUpSorts(sorts, comp, 100);
    testSpecificSorts(sorts, false, false);		
  }
  @Test
  public void testFancierBucketSortOnDifferentSizes() {
    OversamplingSampleSizer sqr3 = new SquareRootSampleSizer(3);
    ObjectExternalSampleCollector<MyInteger> pos3 
      = new ObjectPositionalCollector<MyInteger>(comp, 3, 511);
    ObjectSortList<MyInteger>  sorts = new ObjectSortList<MyInteger>(); 
    SortingBucket<MyInteger> arrayBucket = new DynamicArrayBucket<MyInteger>(8);
    ObjectRangeSorter<MyInteger> qsort = new ArrayObjectQuicksort<MyInteger>();
    sorts.add(qsort);
    sorts.add(new        ObjectBucketSort<MyInteger>(sqr3, pos3, arrayBucket, qsort, 1024));
    sorts.add(new FancierObjectBucketSort<MyInteger>(sqr3, pos3, arrayBucket, qsort, 1024));
    warmUpSorts(sorts, comp, 100);
    testSpecificSorts(sorts, false, false);			  
  }
}
