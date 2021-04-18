package jsortie.testing.object;

import org.junit.Test;

import jsortie.object.JavaArraySort;
import jsortie.object.ObjectRangeSorter;
import jsortie.object.heapsort.ObjectHeapSort;
import jsortie.object.quicksort.governor.ArrayObjectQuicksort;
import jsortie.object.quicksort.multiway.governor.ExternalMultiPivotObjectQuicksort;
import jsortie.object.quicksort.multiway.governor.MultiPivotObjectQuicksort;
import jsortie.object.quicksort.multiway.partitioner.MultiPivotObjectPartitioner;
import jsortie.object.quicksort.multiway.partitioner.permuting.CountAndPermuteObjectPartitioner;
import jsortie.object.quicksort.multiway.partitioner.permuting.TrackAndPermuteObjectPartitioner;
import jsortie.object.quicksort.multiway.partitioner.permuting.TrackAndPermuteObjectPartitionerWithBellsOn;
import jsortie.object.quicksort.multiway.partitioner.permuting.TrackAndStablyPermuteObjectPartitioner;
import jsortie.object.quicksort.multiway.selector.PositionalPivotObjectSelector;

public class ObjectPermutationSortTest
  extends ObjectSortTest {
  public static ObjectRangeSorter<MyInteger> getCountAndPermuteSort
    ( int pivots, int oversample, int cutoff) {
    ObjectRangeSorter<MyInteger> heapSort = new ObjectHeapSort<MyInteger>();
    return new MultiPivotObjectQuicksort<MyInteger>
     ( new PositionalPivotObjectSelector<MyInteger>(pivots, oversample)
     , new CountAndPermuteObjectPartitioner<MyInteger>()
     , heapSort, cutoff, heapSort);
  }
  public static 
    ObjectRangeSorter<MyInteger> getCountAndPermuteSort() {
    return getCountAndPermuteSort(127, 3, 1024);
  }
  public static 
    ObjectRangeSorter<MyInteger> getTrackAndPermuteSort
    ( int pivots, int oversample
    , int cutoff, int variant) {
    ObjectRangeSorter<MyInteger> heapSort 
      = new ObjectHeapSort<MyInteger>();
    MultiPivotObjectPartitioner<MyInteger> party;
    switch (variant) {
      case 0:  party = new TrackAndPermuteObjectPartitioner<MyInteger>();
               break;
      case 1:  party = new TrackAndPermuteObjectPartitionerWithBellsOn<MyInteger>(); 
               break;
      case 2:  party = new TrackAndStablyPermuteObjectPartitioner<MyInteger>();
               break;
      default: party = null;
               break;
    }
    return new MultiPivotObjectQuicksort<MyInteger>
      ( new PositionalPivotObjectSelector<MyInteger>(pivots, oversample)
      , party, heapSort, cutoff, heapSort);
  }
  public static ObjectRangeSorter<MyInteger> 
    getTrackAndPermuteSort(int variant) {
    return getTrackAndPermuteSort(127, 3, 1024, variant);
  }
  public static 
    ObjectRangeSorter<MyInteger> getTrackAndCopySort
    ( int pivots, int oversample, int cutoff) {
    return new ExternalMultiPivotObjectQuicksort<MyInteger>
               (pivots, oversample, cutoff);
  }
  @Test
  public void testPermutingPartitionerSortParameters() {	
    int count     = 65536;
    int threshold = 1024;
    ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>();
    sorts.add(getTrackAndPermuteSort(0));
    sorts.add(getTrackAndPermuteSort(1));
    sorts.add(getTrackAndPermuteSort(2));
    sorts.add(getCountAndPermuteSort());
    warmUpSorts(sorts, comp, 100);
    for (int i=1; i<=3; ++i) {
      String sName = "Track-and-permute";
      if (i==2) sName = "Fancy " + sName;
      if (i==3) sName = "Count-and-permute";
      System.out.println(sName + " sort of " + count 
        + " distinct items in random order," 
        + " with threshold of " + threshold
        + " each row is a different value of p" 
        + " (number of pivots), and each column" 
        + " is an oversampling rate, 0, 1, 3, 7, ...");
      for (int p=1;p<threshold;p=p+p+1) { //1, 3, 7, ... 1023
        sorts.clear();
        for (int s=0;(s+1)*(p+1)<=threshold;s=s+s+1) { //0, 1, 3, 7, ...
          if (i<3) sorts.add(getTrackAndPermuteSort(p, s, threshold, i));
          else sorts.add(getCountAndPermuteSort(p, s, threshold));
        }
        try {
          System.out.println("" + p + "\t" 
            + sortLine
              ( sorts, MyInteger.integerComparator
              , count, 25, false, false));
        } catch (Throwable e){
          System.out.println(sName + "threw " 
            + e.getClass().getSimpleName() 
            + " " + e.getMessage() + " with p=" + p );
          throw e;
        }
      }
      System.out.println("");
    }
  }
  @Test
  public void testPermutingPartitionerQuicksorts() {
    ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>();
    sorts.add(new ArrayObjectQuicksort<MyInteger>());
    sorts.add(getCountAndPermuteSort( 127, 3, 1024));
    sorts.add(getTrackAndPermuteSort( 127, 3, 1024, 0));
    sorts.add(getTrackAndPermuteSort( 127, 3, 1024, 1));
    sorts.add(getTrackAndPermuteSort( 127, 3, 1024, 2));
    sorts.add(getTrackAndCopySort   ( 127, 3, 1024));
    warmUpSorts(sorts, comp, 100);
    minCount = 1024;
    testSpecificSorts(sorts, false, false);
  }
  @Test
  public void testPermutingQuicksortsOnSortedInput() {
    ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>();
    sorts.add(new JavaArraySort<MyInteger>(),           "Java Array Sort (Timsort)");
    sorts.add(new ArrayObjectQuicksort<MyInteger>(),    "ArrayObjectQuicksort");
    sorts.add(getCountAndPermuteSort( 127, 3, 1024),    "Count And Permute");
    sorts.add(getTrackAndPermuteSort( 127, 3, 1024, 0), "Track And Permute");
    sorts.add(getTrackAndPermuteSort( 127, 3, 1024, 1), "Track And Permute(Bells)"); 
    sorts.add(getTrackAndPermuteSort( 127, 3, 1024, 2), "Track And Stably Permute"); //TASP
    sorts.add(getTrackAndCopySort   ( 127, 3, 1024),    "Track And Copy");    //TAC
    warmUpSorts(sorts, comp, 100);
    System.out.println
      ( "Object permutation quicksort running times"
      + " on sorted inputs");
    testSpecificSortsOnSortedInput
      ( sorts, MyInteger.integerComparator
      , 100000, false, "millisecond");
    System.out.println("");
    testSpecificSortsOnDuplicatedInput
      ( false, "==", sorts
      , MyInteger.integerComparator
      , 100000, false, "millisecond");
  }
}
