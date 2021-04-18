package jsortie.testing.object;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import jsortie.helper.DumpRangeHelper;
import jsortie.object.ObjectRangeSorter;
import jsortie.object.heapsort.ObjectHeapSort;
import jsortie.object.heapsort.ObjectHeapSortClassical;
import jsortie.object.janitor.ObjectAlternatingCombsort;
import jsortie.object.janitor.ObjectBinaryInsertionSort;
import jsortie.object.janitor.ObjectInsertionSort;
import jsortie.object.janitor.ObjectOrigamiInsertionSort;
import jsortie.object.janitor.ObjectShellSort;
import jsortie.object.janitor.PairedObjectShellSort;
import jsortie.object.mergesort.ArrayObjectMergesort;
import jsortie.object.mergesort.LinkedListMergesort;
import jsortie.object.mergesort.ObjectTimsortWrapper;
import jsortie.object.quicksort.governor.ArrayObjectQuicksort;
import jsortie.object.quicksort.linkedlist.QuickerLinkedListQuicksort;
import jsortie.object.quicksort.linkedlist.SamplingLinkedListQuicksort;
import jsortie.object.quicksort.multiway.selector.RemedianPivotObjectSelector;
import jsortie.object.quicksort.partitioner.CompoundObjectPartitioner;
import jsortie.object.quicksort.partitioner.SinglePivotObjectPartitioner;
import jsortie.object.quicksort.partitioner.SingletonObjectPartitioner;
import jsortie.object.quicksort.partitioner.StandaloneObjectPartitioner;
import jsortie.object.quicksort.selector.SinglePivotObjectSelector;
import jsortie.object.treesort.binary.RandomTreeSort;
import jsortie.object.treesort.binary.RandomTreeSortBreadthFirst;
import jsortie.object.treesort.binary.balanced.RedBlackTreeSort;
import jsortie.testing.RandomInput;
import junit.framework.TestCase;

public class ObjectJanitorTest extends TestCase {
  protected int runCount = 1000;
  protected RandomInput random = new RandomInput();
  public <T> ObjectSortList<T> janitorsToTry() {
    ObjectSortList<T> janitors = new ObjectSortList<T>();
    janitors.add ( new ObjectBinaryInsertionSort<T>());
    janitors.add ( new ObjectInsertionSort<T>());
    janitors.add ( new ObjectHeapSortClassical<T>());
    janitors.add ( new ObjectHeapSort<T>());
    return janitors;
  }	
  public void testObjectJanitors() {
    List<ObjectRangeSorter<MyInteger>> integerJanitors  = janitorsToTry();
    testSmallSorts(integerJanitors, MyInteger.integerComparator);
  }
  public void testObjectJanitorsOnOrderedInput() {
    ObjectSortList<MyInteger> integerJanitors = janitorsToTry();
    ObjectSortTest x = ObjectSortTest.newObjectSortTest(1000, 100, 100);
    warmUpJanitorsWith(integerJanitors, MyInteger.integerComparator);
    x.testSpecificSortsOnSortedInput(integerJanitors, MyInteger.integerComparator, 100, false, "millisecond");
  }
  @Test
  public void testOtherPossibleJanitorsOnOrderedInput() {
    ObjectRangeSorter<MyInteger> bsort = new ObjectBinaryInsertionSort<MyInteger> ();
    ObjectSortList<MyInteger> sorts = new ObjectSortList<MyInteger>();
    sorts.add ( new ObjectHeapSort<MyInteger>());
    sorts.add ( new ObjectOrigamiInsertionSort<MyInteger>());
    sorts.add ( new ObjectShellSort<MyInteger>());
    sorts.add ( new ObjectAlternatingCombsort<MyInteger>());
    sorts.add ( new SamplingLinkedListQuicksort<MyInteger>(31));
    sorts.add ( new RandomTreeSort<MyInteger>());
    sorts.add ( new RedBlackTreeSort<MyInteger>());
    sorts.add ( new ObjectTimsortWrapper<MyInteger>());
    sorts.add ( new ArrayObjectMergesort<MyInteger>(bsort, 8));
    ObjectSortTest x = ObjectSortTest.newObjectSortTest(1000, 100, 100);
    warmUpJanitorsWith
      ( sorts, MyInteger.integerComparator);
    System.out.println("Running times");
    x.testSpecificSortsOnSortedInput
    ( sorts, MyInteger.integerComparator
      , 100, false, "microsecond");
    runCount = 1000;
    System.out.println("\nComparison counts");
    warmUpJanitorsWith(sorts,MyInteger.countingComparator);
    x.testSpecificSortsOnSortedInput
      ( sorts, MyInteger.countingComparator
      , 100, true, "microsecond");
  }
  public void testEffectOfOrderingOnHeapsort() {
    Comparator<MyInteger> comparator = MyInteger.integerComparator;
    ObjectSortList<MyInteger> janitors = new ObjectSortList<MyInteger>();
    janitors.add ( new ObjectHeapSort<MyInteger>());
    janitors.add ( new ObjectHeapSortClassical<MyInteger>());
    warmUpJanitorsWith(janitors, comparator);
    ObjectSortTest x = ObjectSortTest.newObjectSortTest(100, 1000, 1000);
    x.testSpecificSortsOnSortedInput(janitors, MyInteger.integerComparator, 1000, false, "microsecond");		
  }
  @Test
  public void testJanitorThresholds() {
    Comparator<MyInteger> comparator = MyInteger.integerComparator;
    ObjectRangeSorter<MyInteger>            heapsort  = new ObjectHeapSort<MyInteger>();
    ObjectRangeSorter<MyInteger>            isort     = new ObjectBinaryInsertionSort<MyInteger>();
    ObjectRangeSorter<MyInteger>            oisort    = new ObjectOrigamiInsertionSort<MyInteger>();
    ObjectRangeSorter<MyInteger>            rtree     = new RandomTreeSortBreadthFirst<MyInteger>();
    ObjectRangeSorter<MyInteger>            tim       = new ObjectTimsortWrapper<MyInteger>();
    SinglePivotObjectSelector<MyInteger>    sel       = new RemedianPivotObjectSelector<MyInteger>();
    SinglePivotObjectPartitioner<MyInteger> party     = new SingletonObjectPartitioner<MyInteger>();
    ObjectSortTest                          x         = ObjectSortTest.newObjectSortTest(100, 10000, 10001);
    ObjectSortList<MyInteger> janitors = new ObjectSortList<MyInteger>();
    janitors.add(heapsort);
    janitors.add(isort);
    janitors.add(oisort);
    janitors.add(tim);
    janitors.add(rtree);
    //janitors.add(new ArrayObjectQuicksort<MyInteger>(sel, party, heapsort, 10, heapsort));
    //janitors.add(new ArrayObjectQuicksort<MyInteger>(sel, party, isort,    10, heapsort));
    int n = 10000;
    int r = 1000; //number of executions per data point
    System.out.println
      ( "Efficiency sorting random " + n + "-item inputs" 
      + " of integer keyed-objects," 
      + " as a function of janitor threshold");
    warmUpJanitorsWith(janitors, comparator);
    System.out.println(janitors.getSortHeader("threshold"));    
    for (int j=10; j<10000; j=j*11/10) {
      ObjectSortList<MyInteger> sorts = new ObjectSortList<MyInteger>();
      sorts.add(new ArrayObjectQuicksort<MyInteger>(sel, party, heapsort,  j, heapsort ));
      sorts.add(new ArrayObjectQuicksort<MyInteger>(sel, party, rtree,     j, heapsort));
      sorts.add(new ArrayObjectQuicksort<MyInteger>(sel, party, tim,       j, heapsort));
      if (j<1000) {
        sorts.add(new ArrayObjectQuicksort<MyInteger>(sel, party, isort,   j, heapsort ));
        sorts.add(new ArrayObjectQuicksort<MyInteger>(sel, party, oisort,  j, heapsort ));
      }
      System.out.println
        ( "" + j + x.sortLine ( sorts, comparator
                              , n, r, false, false));
    }
  }
  @Test
  public void testObjectMarginalCostCurves() {
    ObjectSortList<MyInteger> sortList = 
      new ObjectSortList<MyInteger>();
    sortList.add(new ObjectHeapSort<MyInteger>());
    sortList.add(new RandomTreeSortBreadthFirst<MyInteger>());
    sortList.add(new ObjectTimsortWrapper<MyInteger>());
    sortList.add(new ObjectOrigamiInsertionSort<MyInteger>());
    sortList.add(new ObjectInsertionSort<MyInteger>());
    sortList.add(new ObjectBinaryInsertionSort<MyInteger>());
    SinglePivotObjectSelector<MyInteger> selector 
      = new RemedianPivotObjectSelector<MyInteger>();
    SinglePivotObjectPartitioner<MyInteger> partitioner 
      = new SingletonObjectPartitioner<MyInteger>();
    graphObjectMarginalCostCurves( 
      new CompoundObjectPartitioner<MyInteger>
          ( selector, partitioner)
      , sortList, MyInteger.integerComparator
      , 3, 250, 1, 100000 );
  }
  @Test
  public void testLinkedListMergesort() {
    LinkedListMergesort<MyInteger> m = new LinkedListMergesort<MyInteger>();
    for (int n=1; n<10; ++n) {
      int[] input = random.randomPermutation(n);
      MyInteger[] copy = new MyInteger[n];
      for (int i=0; i<input.length; ++i) {
        copy[i] = new MyInteger(input[i]);
      }
      m.sortRange(MyInteger.integerComparator, copy, 0, copy.length);
      for (int i=0; i<input.length; ++i) {
        input[i] = copy[i].intValue();
      }
      DumpRangeHelper.dumpArray("" + n + " = ", input);
    }
  }
  @Test
  public void testObjectMarginalCostCurves2() {
    ObjectSortList<MyInteger> sortList = 
      new ObjectSortList<MyInteger>();
    sortList.add(new ObjectHeapSort<MyInteger>());
    sortList.add(new RandomTreeSortBreadthFirst<MyInteger>());
    sortList.add(new ObjectTimsortWrapper<MyInteger>());
    sortList.add(new QuickerLinkedListQuicksort<MyInteger>());
    sortList.add(new LinkedListMergesort<MyInteger>());
    SinglePivotObjectSelector<MyInteger> selector 
      = new RemedianPivotObjectSelector<MyInteger>();
    SinglePivotObjectPartitioner<MyInteger> partitioner 
      = new SingletonObjectPartitioner<MyInteger>();
    graphObjectMarginalCostCurves( 
      new CompoundObjectPartitioner<MyInteger>
          ( selector, partitioner)
      , sortList, MyInteger.integerComparator
      , 10, 2501, 10, 40000 );
  }
  
  public void graphObjectMarginalCostCurves
    ( StandaloneObjectPartitioner<MyInteger> party
    , ObjectSortList<MyInteger> janitors
    , Comparator<MyInteger> comparator
    , int nLow, int nHigh, int nStep, int runCount ) {
    warmUpJanitorsWith(janitors, comparator);
    System.out.println
      ( janitors.getSortHeader
        ( "threshold\t" + party.toString()));
    for (int n=nLow; n<nHigh; n+=nStep) {
      double   pTime = 0; //nanoseconds
      double[] jTime = new double[janitors.size()]; //net janitor times
      for (int r=0; r<runCount; ++r) {
        int[] input = random.randomPermutation(n);
        MyInteger[] copy;
        
        for (int j=0; j<janitors.size(); ++j) {
          copy = new MyInteger[n];
          for (int i=0; i<input.length; ++i) {
            copy[i] = new MyInteger(input[i]);
          }
          ObjectRangeSorter<MyInteger> jan = janitors.get(j);
          jTime[j] -= System.nanoTime(); 
          jan.sortRange(comparator, copy, 0, n);
          jTime[j] += System.nanoTime();
        }
        copy = new MyInteger[n];
        for (int i=0; i<input.length; ++i) {
          copy[i] = new MyInteger(input[i]);
        }
        pTime -= System.nanoTime();
        int[] boundaries = party.partitionRange ( comparator, copy, 0, n );
        pTime += System.nanoTime();
        for (int j=0; j<janitors.size(); ++j) {
          copy = new MyInteger[n];
          for (int i=0; i<input.length; ++i) {
            copy[i] = new MyInteger(input[i]);
          }
          boundaries = party.partitionRange ( comparator, copy, 0, n );
          ObjectRangeSorter<MyInteger> jan = janitors.get(j);
          jTime[j] += System.nanoTime();
          for (int s=0; s<boundaries.length; s+=2) {
            jan.sortRange ( comparator, copy
                          , boundaries[s], boundaries[s+1]);
          }
          jTime[j] -= System.nanoTime();
        }
      }
      String line = "" + n + "\t" + Math.floor((pTime/n/runCount*1000.0+.5))/1000.0;
      for (int j=0; j<janitors.size(); ++j) {
        line += "\t" + Math.floor((jTime[j]/n/runCount*1000.0+.5))/1000.0;
      }
      System.out.println(line);
    }
  }
  public void warmUpJanitorsWith
    ( List<ObjectRangeSorter<MyInteger>> integerJanitors
    , Comparator<MyInteger> comparator) {
    for (int k=0; k<40000; ++k) {
      int n= 100;
      int a[] = random.randomPermutation(n);		
      MyInteger[] a2  = new MyInteger[n];
      for (int i=0; i<n; ++i ) {
        a2[i] = new MyInteger( a[i]);
      }
      integerJanitors.get( k % integerJanitors.size()).sortRange(comparator, a2, 0, a2.length);
    }
  }
  public void testSmallSorts
    ( List<ObjectRangeSorter<MyInteger>> integerJanitors
    , Comparator<MyInteger> comparator) {
    warmUpJanitorsWith(integerJanitors, comparator);
    for (int n=10; n<520; n=n*11/10) {
      double times[] = new double [ integerJanitors.size() ]; //nano-seconds
      int a[] = random.randomPermutation(n);		
      MyInteger a2[] = new MyInteger[n];
      for (int i=0; i<n; ++i ) {
        a2[i] = new MyInteger(a[i]);
      }
      for (int r=0; r<runCount; ++r) {
       for (int s=0; s<integerJanitors.size(); ++s) {
          int s2 = (s + r) % integerJanitors.size();
          ObjectRangeSorter<MyInteger> j = integerJanitors.get(s2);
          MyInteger a3[] = new MyInteger[n];
          for (int i=0; i<n; ++i) {
            a3[i] = a2[i];
          }
          long startTime = System.nanoTime();
          j.sortRange(comparator, a3, 0, a3.length);
          long stopTime  = System.nanoTime();
          times[s2] += (stopTime - startTime);
        }
      }
      String line = "" + n;
      for (int s=0; s<integerJanitors.size(); ++s) {
        double measure = n*Math.log(n)/Math.log(2) * runCount / times[s];
        line += "\t" + (Math.floor(measure*1000.0 + .5) / 1000.0);
      }
      System.out.println(line);
    }
  }
  @Test
  public void testPairedShellSort() {
    runCount = 5;
    for ( double g = 2.00; g<=4.005; g+=0.1 ) {
      ObjectShellSort<MyInteger> shell = new ObjectShellSort<MyInteger>(g); 
      PairedObjectShellSort<MyInteger> paired = new PairedObjectShellSort<MyInteger>(g);
      List<ObjectRangeSorter<MyInteger>> sorts = new ArrayList<ObjectRangeSorter<MyInteger>>();
      sorts.add(shell);
      sorts.add(paired);
      warmUpJanitorsWith(sorts, MyInteger.countingComparator);
      for (int n=10000000; n<=10000000; n=n*5/4) {
        double[] counts = new double [sorts.size()];
        for (int r=0; r<runCount; ++r) {
          int a[] = random.randomPermutation(n);
          MyInteger a2[] = new MyInteger[n];
          for (int i=0; i<n; ++i ) {
            a2[i] = new MyInteger(a[i]);
          }
          for (int s=0; s<sorts.size(); ++s) {
            int s2 = (s + r) % sorts.size();
            ObjectRangeSorter<MyInteger> j = sorts.get(s2);
            MyInteger a3[] = new MyInteger[n];
            for (int i=0; i<n; ++i) {
              a3[i] = a2[i];
            }
            double startCount = MyInteger.getComparisonCount();
            j.sortRange(MyInteger.countingComparator, a3, 0, a3.length);
            double stopCount = MyInteger.getComparisonCount();
            counts[s2] += (stopCount - startCount);
          }
        }         
        String line = "" + Math.floor(g*1000.0)/1000.0 + "\t" + n;
        for (int s=0; s<sorts.size(); ++s) {
          double measure = counts[s] / (n*(Math.log(n)-1)/Math.log(2)) / (double) runCount;
          line += "\t" + (Math.floor(measure*1000.0 + .5) / 1000.0);
        }
        System.out.println(line);
      }
    }
  }
}
