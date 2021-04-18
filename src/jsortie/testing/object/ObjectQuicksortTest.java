package jsortie.testing.object;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.heapsort.ObjectHeapSort;
import jsortie.object.janitor.ObjectBinaryInsertionSort;
import jsortie.object.janitor.ObjectInsertionSort;
import jsortie.object.quicksort.governor.ArrayObjectQuicksort;
import jsortie.object.quicksort.multiway.governor.MultiPivotObjectQuicksort;
import jsortie.object.quicksort.multiway.partitioner.KLQMObjectPartitioner3;
import jsortie.object.quicksort.multiway.partitioner.YaroslavskiyObjectPartitioner2;
import jsortie.object.quicksort.multiway.partitioner.centrifugal.CentrifugalObjectPartitioner2;
import jsortie.object.quicksort.multiway.partitioner.centrifugal.CentrifugalObjectPartitioner3;
import jsortie.object.quicksort.multiway.partitioner.dutch.BentleyMcIlroyObjectPartitioner;
import jsortie.object.quicksort.multiway.partitioner.dutch.FaithfulBentleyMcIlroyObjectPartitioner;
import jsortie.object.quicksort.multiway.partitioner.holierthanthou.HolierThanThouObjectPartitioner2;
import jsortie.object.quicksort.multiway.partitioner.holierthanthou.HolierThanThouObjectPartitioner3;
import jsortie.object.quicksort.multiway.selector.PositionalPivotObjectSelector;
import jsortie.object.quicksort.multiway.selector.RemedianPivotObjectSelector;
import jsortie.object.quicksort.partitioner.CentripetalObjectPartitioner;
import jsortie.object.quicksort.partitioner.SingletonObjectPartitioner;
import jsortie.object.quicksort.selector.Algorithm489ObjectSelector;
import jsortie.object.quicksort.selector.SinglePivotObjectSelector;

public class ObjectQuicksortTest 
  extends ObjectSortTest {
  public static ObjectRangeSorter<MyInteger> getBentleyMcIlroySort(boolean bFaithful) {
    return new MultiPivotObjectQuicksort<MyInteger>
      ( new RemedianPivotObjectSelector<MyInteger>()
      , bFaithful 
        ? new FaithfulBentleyMcIlroyObjectPartitioner<MyInteger>() 
        : new BentleyMcIlroyObjectPartitioner<MyInteger>()
      , new ObjectInsertionSort<MyInteger>(), 32
      , new ObjectHeapSort<MyInteger>());
  }
  public static ObjectRangeSorter<MyInteger> getCentripetalSort() {
    return new ArrayObjectQuicksort<MyInteger>
      ( new RemedianPivotObjectSelector<MyInteger>()
      , new CentripetalObjectPartitioner<MyInteger>()
      , new ObjectInsertionSort<MyInteger>(), 32
      , new ObjectHeapSort<MyInteger>());
  }
  public static ObjectRangeSorter<MyInteger> getSingletonSort 
    ( SinglePivotObjectSelector<MyInteger> sel) {
    return new ArrayObjectQuicksort<MyInteger>
               ( sel, new SingletonObjectPartitioner<MyInteger>()
               , new ObjectInsertionSort<MyInteger>(), 32
               , new ObjectHeapSort<MyInteger>());
  }  
  public static ObjectRangeSorter<MyInteger> getYaroSort() {
    return new MultiPivotObjectQuicksort<MyInteger>
      ( new PositionalPivotObjectSelector<MyInteger>(2, 3)
      , new YaroslavskiyObjectPartitioner2<MyInteger>()
      , new ObjectBinaryInsertionSort<MyInteger>(), 32
      , new ObjectHeapSort<MyInteger>());
  }
  public static ObjectRangeSorter<MyInteger> getHTT2() {
    return new MultiPivotObjectQuicksort<MyInteger>
      ( new PositionalPivotObjectSelector<MyInteger>(2, 3)
      , new HolierThanThouObjectPartitioner2<MyInteger>()
      , new ObjectBinaryInsertionSort<MyInteger>(), 32
      , new ObjectHeapSort<MyInteger>());
  }
  public static ObjectRangeSorter<MyInteger> getHTT3() {
    return new MultiPivotObjectQuicksort<MyInteger>
      ( new PositionalPivotObjectSelector<MyInteger>(3, 3)
      , new HolierThanThouObjectPartitioner3<MyInteger>()
      , new ObjectBinaryInsertionSort<MyInteger>(), 32
      , new ObjectHeapSort<MyInteger>());
  }
  public static ObjectRangeSorter<MyInteger> getQuicksort() {
    return new ArrayObjectQuicksort<MyInteger>();
  }
  public static ObjectRangeSorter<MyInteger> getA489Quicksort
    ( ObjectRangeSorter<MyInteger> janitor, int threshold
    , ObjectRangeSorter<MyInteger> lastResort) {
    return new ArrayObjectQuicksort<MyInteger>
               ( new Algorithm489ObjectSelector<MyInteger>(10)
               , new SingletonObjectPartitioner<MyInteger>()
               , janitor, threshold, lastResort);
  }
  public static ObjectRangeSorter<MyInteger> getCentrifugal2() {
    return new MultiPivotObjectQuicksort<MyInteger>
      ( new PositionalPivotObjectSelector<MyInteger>(2, 3)
      , new CentrifugalObjectPartitioner2<MyInteger>()
      , new ObjectBinaryInsertionSort<MyInteger>(), 32
      , new ObjectHeapSort<MyInteger>());
  }
  public static ObjectRangeSorter<MyInteger> getCentrifugal3() {
    return new MultiPivotObjectQuicksort<MyInteger>
      ( new PositionalPivotObjectSelector<MyInteger>(3, 3)
      , new CentrifugalObjectPartitioner3<MyInteger>()
      , new ObjectBinaryInsertionSort<MyInteger>(), 32
      , new ObjectHeapSort<MyInteger>());
  }
  public static ObjectRangeSorter<MyInteger> getKLQM3() {
    return new MultiPivotObjectQuicksort<MyInteger>
      ( new PositionalPivotObjectSelector<MyInteger>(3, 3)
      , new KLQMObjectPartitioner3<MyInteger>()
      , new ObjectBinaryInsertionSort<MyInteger>(), 32
      , new ObjectHeapSort<MyInteger>());
  }  
}
