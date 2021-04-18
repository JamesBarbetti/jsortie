package jsortie.testing.object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.Test;

import jsortie.exception.SortingFailureException;
import jsortie.helper.DumpRangeHelper;
import jsortie.object.JavaArraySort;
import jsortie.object.ObjectRangeSorter;
import jsortie.object.bucket.ObjectArrayBucketSorter;
import jsortie.object.collector.ObjectPositionalCollector;
import jsortie.object.collector.ObjectSampleCollector;
import jsortie.object.heapsort.ObjectHeapSort;
import jsortie.object.janitor.ObjectBinaryInsertionSort;
import jsortie.object.janitor.ObjectInsertionSort;
import jsortie.object.quicksort.governor.ArrayObjectQuicksort;
import jsortie.object.quicksort.governor.InternalObjectSampleSort;
import jsortie.object.quicksort.governor.ObjectSymmetryPartitionSort;
import jsortie.object.quicksort.multiway.governor.MultiPivotObjectQuicksort;
import jsortie.object.quicksort.multiway.partitioner.MultiPivotObjectPartitioner;
import jsortie.object.quicksort.multiway.partitioner.dutch.STLTernaryObjectPartitioner;
import jsortie.object.quicksort.multiway.selector.MultiPivotObjectSelector;
import jsortie.object.quicksort.multiway.selector.RemedianPivotObjectSelector;
import jsortie.object.quicksort.multiway.selector.SingleToMultiObjectSelector;
import jsortie.object.quicksort.partitioner.SinglePivotObjectPartitioner;
import jsortie.object.quicksort.partitioner.SingletonObjectPartitioner;
import jsortie.object.quicksort.selector.MiddleElementObjectSelector;
import jsortie.object.quicksort.selector.NoShitDirtyTukeyObjectSelector;
import jsortie.object.quicksort.selector.SinglePivotObjectSelector;
import jsortie.object.quicksort.selector.DirtySingletonObjectSelector;
import jsortie.object.quicksort.selector.DirtyTukeyObjectSelector;
import jsortie.object.sort.decorator.NullFriendlySort;
import jsortie.quicksort.samplesizer.OversamplingSampleSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;
import jsortie.testing.DegenerateInput;
import jsortie.testing.RandomInput;
import junit.framework.TestCase;

public class ObjectSortTest 
  extends TestCase {
  int    minCount        = 10;
  int    maxCount        = 50000000;
  int    runCount        = 20;
  double countGrowthRate = 1.1;
  double rounding        = 10000;
  protected DegenerateInput  degen  = new DegenerateInput();
  protected RandomInput      random = new RandomInput();
  public Comparator<MyInteger> comp = MyInteger.integerComparator;
  ObjectRangeSorter<MyInteger> bisort 
  = new ObjectBinaryInsertionSort<MyInteger>();
  public ObjectSortTest() { }
  public static ObjectSortTest newObjectSortTest
    ( int runCount, int minCount, int maxCount ) {
    ObjectSortTest x = new ObjectSortTest();
    x.minCount = minCount;
    x.maxCount = maxCount;
    x.runCount = runCount;
    return x;
  }
  @Test
  public void testObjectQuicksortBaseline() {
    ObjectSortList<MyInteger> sorts = getBaselineSorts();
    warmUpSorts(sorts, comp, 100);
    testSpecificSortsOnSortedInput
      ( sorts, MyInteger.integerComparator
      , 1000000, false, "millisecond");
  }
  @Test
  public void testObjectMultiPivotArrayQuicksorts() {
    ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>();
    sorts.add(ObjectQuicksortTest.getYaroSort());
    sorts.add(ObjectQuicksortTest.getHTT2());
    sorts.add(ObjectQuicksortTest.getCentrifugal2());
    sorts.add(ObjectQuicksortTest.getKLQM3());
    sorts.add(ObjectQuicksortTest.getHTT3());
    sorts.add(ObjectQuicksortTest.getCentrifugal3());
    warmUpSorts(sorts, comp, 100);
    runCount=25;
    testSpecificSortsOnSortedInput
      ( sorts, MyInteger.integerComparator
      , 1048576, false, "millisecond");
  }
  @Test
  public void testSinglePivotSampleSorts() {
    ObjectSortList<MyInteger> sorts 
      = getSinglePivotSampleSorts();
    warmUpSorts(sorts, comp, 100);
    testSpecificSortsOnSortedInput
      ( sorts, MyInteger.integerComparator
      , 100000, false, "millisecond");
  }
  public ObjectSortList<MyInteger> 
    getBaselineQuicksorts() {
    SinglePivotObjectSelector<MyInteger> middle 
      = new MiddleElementObjectSelector<MyInteger>();
    SinglePivotObjectSelector<MyInteger> singleton 
      = new DirtySingletonObjectSelector<MyInteger>();
    SinglePivotObjectSelector<MyInteger> tukey
      = new DirtyTukeyObjectSelector<MyInteger>();
    MultiPivotObjectSelector<MyInteger> tukeyMulti
      = new SingleToMultiObjectSelector<MyInteger>(tukey);
    
    SinglePivotObjectPartitioner<MyInteger> party 
      = new SingletonObjectPartitioner<MyInteger>();
    MultiPivotObjectPartitioner<MyInteger> stlParty 
      = new STLTernaryObjectPartitioner<MyInteger>();
    
    ObjectRangeSorter<MyInteger> bisort
      = new ObjectBinaryInsertionSort<MyInteger>();
    ObjectRangeSorter<MyInteger> isort
      = new ObjectInsertionSort<MyInteger>();
    ObjectRangeSorter<MyInteger> heapSort
      = new ObjectHeapSort<MyInteger>();
    
    ArrayObjectQuicksort<MyInteger> quickSortClassic
      = new ArrayObjectQuicksort<MyInteger>
            ( middle, party, bisort, 32, heapSort );
    ArrayObjectQuicksort<MyInteger> quickSortBestOf3
      = new ArrayObjectQuicksort<MyInteger>
            ( singleton, party, bisort, 32, heapSort );
    ArrayObjectQuicksort<MyInteger> quickSortTukey
      = new ArrayObjectQuicksort<MyInteger>
            ( tukey, party, bisort, 32, heapSort );
    MultiPivotObjectQuicksort<MyInteger> quickSortSTL
      = new MultiPivotObjectQuicksort<MyInteger>
            ( tukeyMulti, stlParty, bisort, 32, isort );
    
    ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>();
    sorts.add( new JavaArraySort<MyInteger>()
             , "Java Array Sort (Timsort)");
    sorts.add( quickSortClassic, "Classic Quicksort");
    sorts.add( quickSortBestOf3, "Best-Of-3 Quicksort");
    sorts.add( quickSortTukey,   "Ninther Quicksort");
    sorts.add( new ArrayObjectQuicksort<MyInteger>()
             , "Remedian Selector Quicksort");
    sorts.add( quickSortSTL, "STL's Quicksort");
    return sorts;
  }
  @Test
  public void testBaselineSortsOnRandomInputs() {
    ObjectSortList<MyInteger> sorts 
      = getBaselineQuicksorts();
    warmUpSorts(sorts, comp, 100);
    System.out.println("Efficiency (Gbps), sorting" 
      + " random permutations of integer-keyed objects");
    this.testSpecificSortsOnRandomInput
      ( sorts, 100, MyInteger.integerComparator
      , 10, 1000000, false);
    System.out.println("");
    System.out.println("Comparison efficiency, sorting" 
      + " random permutations of integer-keyed objects");
    this.testSpecificSortsOnRandomInput
      ( sorts, 100, MyInteger.countingComparator
      , 10, 1000000, true);
  }
  @Test
  public void testTukeyVariantsOnDegenerateInputs() {
    SinglePivotObjectSelector<MyInteger> tukey
      = new DirtyTukeyObjectSelector<MyInteger>();
    SinglePivotObjectSelector<MyInteger> noShitTukey
      = new NoShitDirtyTukeyObjectSelector<MyInteger>();
    SinglePivotObjectPartitioner<MyInteger> party 
      = new SingletonObjectPartitioner<MyInteger>();
    ObjectRangeSorter<MyInteger> heapSort
      = new ObjectHeapSort<MyInteger>();
    ArrayObjectQuicksort<MyInteger> quickSortTukey
      = new ArrayObjectQuicksort<MyInteger>
            ( tukey, party, heapSort, 32, heapSort );
    ArrayObjectQuicksort<MyInteger> quickSortNoShit
      = new ArrayObjectQuicksort<MyInteger>
            ( noShitTukey, party, heapSort, 32, heapSort );
    ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>();
    runCount = 1000;
    sorts.add(quickSortTukey, "Tukey");
    sorts.add(quickSortNoShit, "NoShitTukey");
    warmUpSorts(sorts, comp, 100);
    System.out.println("Comparison counts, sorting" 
      + " degenerate inputs of 10,000 integer-keyed objects");
    this.testSpecificSortsOnSortedInput
      ( sorts, MyInteger.countingComparator
      , 10000, true, "millisecond");
  }
  @Test
  public void testBaselineSortsOnDegenerateInputs() {
    ObjectSortList<MyInteger> sorts = getBaselineQuicksorts();
    warmUpSorts(sorts, comp, 100);
    runCount = 100;
    System.out.println("Running times (microseconds), sorting" 
      + " degenerate inputs of 10,000 integer-keyed objects");
    this.testSpecificSortsOnSortedInput
      ( sorts, MyInteger.integerComparator, 10000, false, "microsecond");
    System.out.println("");
    System.out.println("Comparison counts, sorting" 
      + " degenerate inputs of 10,000 integer-keyed objects");
    this.testSpecificSortsOnSortedInput
      ( sorts, MyInteger.countingComparator, 10000, true, "microsecond");
    System.out.println("");
    this.testSpecificSortsOnDuplicatedInput
      ( false, "==", sorts
      , MyInteger.countingComparator, 10000, true, "microsecond");
  }
  public double bitsOfChaos(double n) {
    if (n<2) return 0;
    if (n==2) return 1.0;
    return n * (Math.log(n) - 1.0) / Math.log(2.0);
  }
  private void testSpecificSortsOnRandomInput
    ( ObjectSortList<MyInteger> sorts, int runCount
    , Comparator<MyInteger> comparator, int minCount
    , int maxCount, boolean countComparisons) {
    System.out.println(sorts.getSortHeader("n"));
    for (int n=minCount; n<maxCount; n=(n<4)?(n+1):n*5/4) { 
      double elapsedPerSort[]     = new double[sorts.size()];
      double comparisonsPerSort[] = new double[sorts.size()];
      for (int r=0; r<runCount; ++r) {
        int[] input = random.randomPermutation(n);
        runSortsOn 
          ( sorts, input, comparator, r, 0
          , elapsedPerSort, comparisonsPerSort);
      }
      sortLine ( "" + n
                 , sorts, runCount, countComparisons
                 , true, n
                 , elapsedPerSort, comparisonsPerSort
                 , 1000.0 );
      }
    }
  @Test
  public void testObjectSortsOnAMillion() {
    ObjectSortList<MyInteger> sorts = getBaselineSorts();
    warmUpSorts(sorts, comp, 100);
    int oldRunCount = runCount;
    runCount = 5;
    testSpecificSortsOnSortedInput
      ( sorts, MyInteger.integerComparator
      , 1000000, false, "millisecond");
    runCount = oldRunCount;
  }
  @Test
  public void testObjectSortsOnTenMillion() {
    ObjectSortList<MyInteger> sorts 
      = getBaselineSorts();
    warmUpSorts(sorts, comp, 100);
    int    oldRunCount   = runCount;
    double oldGrowthRate = countGrowthRate;
    countGrowthRate = 1.4;
    runCount = 5;
    testSpecificSortsOnSortedInput
      ( sorts, MyInteger.integerComparator
      , 10000000, false, "millisecond");
    runCount = oldRunCount;
    countGrowthRate = oldGrowthRate;
  }
  public ObjectSortList<MyInteger> getBaselineSorts() {
    ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>();
    sorts.add
      ( ObjectQuicksortTest.getSingletonSort
        ( new MiddleElementObjectSelector<MyInteger>() ) );
    sorts.add
     ( ObjectQuicksortTest.getSingletonSort
       ( new RemedianPivotObjectSelector<MyInteger>() ) );
    sorts.add(ObjectQuicksortTest.getCentripetalSort());
    sorts.add(new MultiPivotObjectQuicksort<MyInteger>());
    sorts.add(ObjectQuicksortTest.getYaroSort());
    sorts.add(ObjectQuicksortTest.getHTT3());
    sorts.add(ObjectPermutationSortTest.getCountAndPermuteSort());
    sorts.add(ObjectPermutationSortTest.getTrackAndPermuteSort(0));
    sorts.add(ObjectPermutationSortTest.getTrackAndPermuteSort(1));
    sorts.add(ObjectPermutationSortTest.getTrackAndPermuteSort(2));
    sorts.add(ObjectQuicksortTest.getCentrifugal2());
    sorts.add(ObjectQuicksortTest.getCentrifugal3());
    sorts.add(ObjectQuicksortTest.getKLQM3());
    return sorts;	  
  }
  public ObjectSortList<MyInteger> 
    getSinglePivotSampleSorts() {
    ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>();
    OversamplingSampleSizer sqr 
      = new SquareRootSampleSizer();
    ObjectSampleCollector<MyInteger> collector 
      = new ObjectPositionalCollector<MyInteger>();
    SinglePivotObjectPartitioner<MyInteger> party
      = new SingletonObjectPartitioner<MyInteger>();
    ObjectBinaryInsertionSort<MyInteger> bisort 
      = new ObjectBinaryInsertionSort<MyInteger>();
    sorts.add ( new InternalObjectSampleSort<MyInteger>
                    ( sqr, collector, party, bisort, 32, false));
    sorts.add ( new ObjectSymmetryPartitionSort<MyInteger>
                    ( sqr, collector, party, bisort, 32, false));
    return sorts;
  }
  
  @Test
  public void testObjectSortTiming() {
    ObjectSortList<MyInteger> sorts 
      = getCollectionQuicksorts(comp);
    System.out.println("Testing object sorts");
    testSpecificSorts(sorts, false, false);
  }
  @Test
  public void testObjectSortComparisonCounts() {
    ObjectSortList<MyInteger> sorts 
      = getCollectionQuicksorts(MyInteger.countingComparator);
    System.out.println("Object sort comparison counts");
    testSpecificSorts(sorts, true, false);
  }
  public ObjectSortList<MyInteger> 
    getCollectionQuicksorts(Comparator<MyInteger> c) {
  	ObjectSortList<MyInteger> sorts
      = new ObjectSortList<MyInteger>();
    sorts.add(ObjectQuicksortTest.getQuicksort());
    sorts.add(ObjectQuicksortTest.getBentleyMcIlroySort(false));
    sorts.add(ObjectQuicksortTest.getBentleyMcIlroySort(true));
    sorts.add(ObjectTreeSortTest.getFancyQuacksort());
    sorts.add(ObjectTreeSortTest.getRedBlackTreeSort());
    sorts.add(ObjectBucketSortTest.getBucketSort());
    return sorts;
  }
  public void testObjectSortComparisonCountsOnSortedInput() {
    ObjectSortList<MyInteger> sorts 
      = getCollectionQuicksorts
        ( MyInteger.countingComparator );
    System.out.println
      ( "Object sort comparison counts on sorted inputs" );
    testSpecificSortsOnSortedInput
      ( sorts, MyInteger.integerComparator
      , 100000, true, "millisecond");
  }	
  @Test
  public void testComparisonOverheadsOfBestSorts() {
    ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>();
    System.out.println("Overhead for binary insertion sort");
    System.out.println("Object sort comparison counts on random inputs");
    sorts.add(new ObjectHeapSort<MyInteger>());
    sorts.add(ObjectMergeSortTest.getMergesort(bisort, 32));
    sorts.add ( ObjectQuicksortTest.getA489Quicksort(bisort, 32, bisort));
    for (double alpha: new double[] { 4 } ) {
      for ( int oversample : new int[] 
            { 0, 1 /*0.39*/, 3  /*0.188*/, 7 /* 0.092*/ } ) {
        sorts.add ( ObjectSampleSortTest.getInternalSampleSort
                    ( MyInteger.countingComparator
                    , alpha, oversample, bisort));
      }
    }
    runCount=100;
    this.testSpecificSorts(sorts, true, true);
  }
  public void testSpecificSortsOnRandomInput
  ( ObjectSortList<MyInteger> sorts, int runCount
  , Comparator<MyInteger> comparator
  , int n, boolean countComparisons, String unit) {
    double divisor = chooseDivisor(unit);
    double elapsedPerSort[]     = new double[sorts.size()];
    double comparisonsPerSort[] = new double[sorts.size()];
    for (int run=0; run<runCount; ++run) {
      int[] input = (new RandomInput()).randomPermutation(n);
      runSortsOn ( sorts, input, comparator, 9, 0
                 , elapsedPerSort, comparisonsPerSort);
    }
    sortLine ( "" + n, sorts, runCount, countComparisons, false, n
        , elapsedPerSort, comparisonsPerSort, divisor );
  }
  protected double chooseDivisor(String unit) {
    double divisor = 1000.0 * 1000.0;
    if (unit.equals("second")) divisor = 1000.0 * 1000.0 * 1000.0;
    if (unit.equals("millisecond")) divisor = 1000.0 * 1000.0;
    if (unit.equals("microsecond")) divisor = 1000.0;
    return divisor;
  }
  public void sortLine
    ( String col1, ObjectSortList<MyInteger> sorts, int runCount
    , boolean countComparisons, boolean showEffiency, int n
    , double[] elapsedPerSort, double[] comparisonsPerSort
    , double divisor) {
  	String line = col1;
    for (int s=0; s<sorts.size(); ++s) {
      if (showEffiency) {
        if (countComparisons) {
          line += "\t" 
            + Math.floor( bitsOfChaos(n)
                          * (double)runCount 
                          / (double)comparisonsPerSort[s]
                          * 10000.0 + .5) / 100.0 ;
        } else {
          line += "\t" + ( Math.floor ( runCount * bitsOfChaos(n) 
                                    / elapsedPerSort[s] 
                                    * 1000.0 + .5 ) 
                         / 1000.0 );
        }
      } else if (countComparisons) {
        line += "\t" 
          + Math.floor( (double)comparisonsPerSort[s]
                        / (double)runCount * 100 + .5) / 100.0 ;
      } else {
        line += "\t" + Math.floor(elapsedPerSort[s]/(double)runCount 
                / divisor  * rounding + .5) / rounding;
      }
    }
    System.out.println(line);
  }
  public void runSortsOn
    ( ObjectSortList<MyInteger> sorts, int[] input
    , Comparator<MyInteger> comparator, int r
    , int desc, double[] elapsedPerSort
    , double[] comparisonsPerSort) {
    int[] copy2 = Arrays.copyOf(input, input.length);
    MyInteger[] copy  = new MyInteger[input.length];
    Arrays.sort(copy2);
    for (int s = 0; s < sorts.size(); ++s) {
      for (int c=0; c<input.length; ++c) {
        copy[c] = new MyInteger((desc>0) 
                ? input[input.length-1-c] : input[c]);
      }
      int s2 = (s + r) % sorts.size();
      ObjectRangeSorter<MyInteger> sort = sorts.get(s2);
      long startTime = System.nanoTime();
      MyInteger.comparisons = 0;
      sort.sortRange(comparator, copy, 0, copy.length);
      long stopTime = System.nanoTime();
      for (int c=0; c<input.length; ++c) {
        if (copy[c].intValue() != copy2[c]) {
          if (input.length<100) {
            DumpRangeHelper.dumpRange
              ( "INPUT  ", input, 0, input.length );
            DumpRangeHelper.dumpRange
              ( "OUTPUT ", copy,  0, input.length );
          }
          throw new SortingFailureException
            ( "Sort " + s2 + " (" + sort.toString() + ")"
            + " failed: element [" + c + "]"
            + " was " + copy[c].intValue()
            + ", but should have been " + copy2[c]);
        }
      }
      elapsedPerSort[s2]     += stopTime - startTime;
      comparisonsPerSort[s2] += MyInteger.comparisons;
    }
  }
  public void testSpecificSortsOnSortedInput
    ( ObjectSortList<MyInteger> sorts
    , Comparator<MyInteger> comparator
    , int n, boolean countComparisons, String unit) {
    sorts.writeSortHeader("n");
    double divisor = chooseDivisor(unit);
    ArrayList<Integer> dCounts 
      = new ArrayList<Integer>();
    int d=0;
    for (;;) {
      dCounts.add(d);
      if (d==n) {
        break;
      }
      int d2=(int)Math.floor(d*countGrowthRate);
      d = (d2==d) ? (d2+1) : d2;
      if (d>n) {
        d=n;
      }
    }
    for (int desc=1; desc>=0; --desc) { 
      //desc: 1==mostly descending, 0==mostly ascending
      for (int i=0; i<dCounts.size(); ++i) {
        double elapsedPerSort[]     = new double[sorts.size()];
        double comparisonsPerSort[] = new double[sorts.size()];
        int i2 = (desc==0) ? (dCounts.size()-1-i) : i;
        d      = dCounts.get( i2 );
        for (int r=0; r<runCount; ++r) {
          int[] input = degen.postRandomUpdatePermutation(n, d);
          runSortsOn ( sorts, input, comparator, r, desc
                     , elapsedPerSort, comparisonsPerSort);
        }
        sortLine ( "" + ((desc>0) ? "Backward" : "Forward" ) + d
                 , sorts, runCount, countComparisons, false, n
                 , elapsedPerSort, comparisonsPerSort
                 , divisor );
      }
    }
  }
  @Test
  public void testObjectSortsOnDuplicatedInput() {
    ObjectSortList<MyInteger> sorts = new ObjectSortList<MyInteger>();
    sorts.add ( ObjectQuicksortTest.getSingletonSort
                ( new RemedianPivotObjectSelector<MyInteger>()));
    sorts.add ( ObjectMergeSortTest.getTimsort());
    sorts.add ( ObjectQuicksortTest.getCentripetalSort());
    sorts.add ( ObjectQuicksortTest.getBentleyMcIlroySort(false));
    sorts.add ( ObjectQuicksortTest.getBentleyMcIlroySort(true));
    sorts.add ( ObjectQuicksortTest.getYaroSort());
    sorts.add ( ObjectPermutationSortTest.getTrackAndPermuteSort(0));
    sorts.add ( ObjectPermutationSortTest.getTrackAndPermuteSort(1));
    sorts.add ( ObjectPermutationSortTest.getTrackAndPermuteSort(2));
    sorts.add ( ObjectBucketSortTest.getBucketSort());
    sorts.add ( ObjectSampleSortTest.getInternalSampleSort());
    runCount = 1;
    int n = 1048576;
    System.out.println
      ( "Object sort comparison counts" 
      + " on heavily-duplicated " + n + "-item inputs");
    testSpecificSortsOnDuplicatedInput
      ( true, "==", sorts, MyInteger.countingComparator
      , n, true, "second");
    System.out.println("");
    System.out.println
      ( "Object sort timings" 
      + " on heavily-duplicated " + n + "-item inputs");
    testSpecificSortsOnDuplicatedInput
      ( true, "==", sorts
      , MyInteger.integerComparator, n, false, "second");
  }
  public void testSpecificSortsOnDuplicatedInput
    ( boolean bShowHeader, String dPrefix
    , ObjectSortList<MyInteger> sorts
    , Comparator<MyInteger> comparator
    , int n, boolean countComparisons
    , String unitToUse) {
    if (bShowHeader) {
      String header1 = "d";
      for (int s = 0; s < sorts.size(); ++s) {
        header1 += "\t" + sorts.get(s).toString() 
          + (countComparisons ? "\t" : "");
      }
      System.out.println(header1);
    }
    double divisor = chooseDivisor(unitToUse);
    MyInteger copy[] = new MyInteger[n];
    for (int d=1; d<=n; d=(d*5)/4 + ((d<4) ? 1 : 0) ) {
      double elapsedPerSort[]
        = new double[sorts.size()];
      long   comparisonsPerSort[] 
        = new long[sorts.size()];
      String line = dPrefix + d;
      for (int r=0; r<runCount; ++r) {
        int input[] = degen.permutationOfDupes(n, d);
        for (int s = 0; s < sorts.size(); ++s) {
          for (int c=0; c<input.length; ++c) {
            copy[c] = new MyInteger(input[c]);
          }
          int  s2 = (s + r) % sorts.size();
          ObjectRangeSorter<MyInteger> sort 
            = sorts.get(s2);
          long startTime = System.nanoTime();
          MyInteger.comparisons             = 0;
          sort.sortRange
            ( comparator, copy, 0, copy.length );
          long stopTime = System.nanoTime();
          elapsedPerSort[s2] += stopTime - startTime;
          comparisonsPerSort[s2] += MyInteger.comparisons;
        }
      }
      for (int s=0; s<sorts.size(); ++s) {
        if (countComparisons) {
          line += "\t" 
            + Math.floor( (double)comparisonsPerSort[s]
                          / (double)runCount * 100 + .5 ) / 100.0 ;
        } else {
          line += "\t" 
            + Math.floor( elapsedPerSort[s]
                        / (double)runCount 
                        / divisor  * rounding + .5 ) 
            / rounding;
        }
      }
      System.out.println(line);
    }
  }
  protected void addStandardObjectQuicksortTo
    ( ObjectSortList<MyInteger> sorts ) {
    sorts.add(ObjectQuicksortTest.getQuicksort());	  
  }
  protected void warmUpSorts
    ( ObjectSortList<MyInteger> sorts
    , Comparator<MyInteger> comparator, int runs) {
    int count = 10000;
    for (int r=0; r<runs; ++r) {
      int       input[] = random.randomPermutation(count);
      MyInteger copy[]  = new MyInteger[count];			
      for (ObjectRangeSorter<MyInteger> s : sorts) {
        for (int i=0; i<count; ++i) {
          copy[i] = new MyInteger(input[i]);
        }
        s.sortRange(comparator, copy, 0, copy.length);
      }
    }
  }
  public void testSpecificSorts
    (ObjectSortList<MyInteger> sorts
    , boolean countComparisons, boolean overhead) {
    String header1= "n";
    for (int s = 0; s < sorts.size(); ++s) {
      header1 += "\t" + sorts.get(s).toString();
    }
    System.out.println(header1);
    int runs = runCount;
    for (int n = minCount ; n<maxCount; ) {
      if (n>1000) runs=runCount/100;
      if (n>100000) runs=runCount/100;
      if (runs==0) runs=1;
      System.out.println( "" + n + 
          sortLine ( sorts, MyInteger.integerComparator, n
                   , runs, countComparisons, overhead) );
      int nextN = (int)Math.floor(n*countGrowthRate);
      n = (nextN==n) ? (n+1) : nextN;
    }		
  }
  public double infoTheoreticBound(int n) {
    //exact: not the approximation 
    //(Math.log(n)-1.0) / Math.log(2.0)
    double boundTimesLog2 = 0;
    for (int i=2; i<=n; ++i) {
      boundTimesLog2 += Math.log(i);
    }
    return boundTimesLog2 / Math.log(2.0);
  }
  public String sortLine
    ( ObjectSortList<MyInteger> sorts
    , Comparator<MyInteger> comparator, int n
    , int runs, boolean countComparisons
    , boolean overhead) {
    double  measure[] = new double [ sorts.size() ];
    String    line    = "";
    for (int r=0; r<runs; ++r) {
      int       input[] = random.randomPermutation(n);
      MyInteger copy[]  = new MyInteger[n];
      for (int s = 0; s < sorts.size(); ++s) {
        for (int i=0; i<n; ++i) {
          copy[i] = new MyInteger(input[i]);
        }
        MyInteger.comparisons = 0;
        long startTime = System.nanoTime();
        sorts.get(s).sortRange(comparator, copy, 0, copy.length);
        long stopTime = System.nanoTime();
        if (countComparisons) {
          measure[s] += MyInteger.comparisons;
        } else {
          measure[s] += (stopTime-startTime);
        }
        for (int check=1;check<n;++check) {
          if (0 < comparator.compare(copy[check-1], copy[check])) {
            if (n<100) {
              String dump = "Output = ";
              for (int i=0; i<n; ++i) {
                dump += (i==0) ? "[ " : ", ";
                dump += copy[i].toString();
              }
              dump += "]";
              System.out.println(dump);
            }
            String problem = "Sort " + s 
                + " (" + sorts.getSortName(s) + ")"
                + " failed, on run " + (r+1) + " of " + runs
                + " to correctly sort " + n + " items;"
                + " items [" + (check-1) + "]=" + copy[check-1].toString()
                + " and [" + check + "]=" + copy[check] 
                + " are out of order";
            throw new SortingFailureException(problem);
          }
        }
      }
    }
    for (int s=0; s<sorts.size(); ++s) {
      double minimum 
        = n*(Math.log(n)-1.0)/Math.log(2); 
          //theoretical minimum average # comparisons
      if (countComparisons) {
        if (overhead) {
          double actual 
            = measure[s] / (double)runs;
            //actual comparisons per run
          line += "\t" 
            + Math.floor( (actual - minimum) 
                          / (double)n * rounding + .5) 
            / rounding;
        } else {
          line += "\t" 
            + Math.floor ( measure[s] / minimum 
                           / (double)runs * rounding + .5 )
            / rounding;
        }
      } else {
        line += "\t" 
          + Math.floor ( minimum / measure[s] 
                         * (double)runs * rounding + .5) 
          / rounding;
      }
    }
    return line;
  }
  @Test
  public void testNullSafeSorts() {
    maxCount = 65536;
    runCount = 20;
    int n = maxCount;
    ObjectRangeSorter<MyInteger> 
      aoq = ObjectQuicksortTest.getQuicksort();
    ObjectRangeSorter<MyInteger> 
      friendly = new NullFriendlySort<MyInteger>(aoq);
    ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>();
    sorts.add( aoq );
    int nUnsafeComparator = sorts.size();
    sorts.add( friendly );
    this.warmUpSorts(sorts, comp, 100);
    for (int d=1; d<n; d+=(d>10) ? (d/10) : 1) {
      double[] timings = new double[sorts.size()];
      for (int r=0; r<runCount; ++r) {
        int input[]  = random.randomPermutation(maxCount);
        int nulled[] = random.randomUnorderedCombination(maxCount, d);
        for (int s=0; s<sorts.size(); ++s) {
          MyInteger[] copy = new MyInteger[n];
          for (int i=0; i<n; ++i) {
            copy[i] = new MyInteger( input[i] );
          }
          for (int z=0; z<d; ++z) {
            copy[nulled[z]]= null;
          }
          ObjectRangeSorter<MyInteger> sort = sorts.get(s);
          timings[s] -= System.nanoTime();
          sort.sortRange ( (s<nUnsafeComparator) 
                           ? MyInteger.nullSafeComparator 
                           : MyInteger.integerComparator
                         , copy, 0, copy.length);
          timings[s] += System.nanoTime();
        }
      }
      String line = "" + d;  
      for (int s=0; s<sorts.size(); ++s) {
        double millis 
          = Math.floor ( timings[s] 
                         / (double)runCount 
                         / 1000.0 ) / 1000.0;
        line += "\t" + millis;
      }
      System.out.println(line);
    }
  }
  @Test
  public void testX() {
    //Todo: move this; it's calculating comparison and moves cost
    System.out.println("" + Math.log(2)*2);
    for (int k=2, p=4; k<13; ++k, p*=2) {
      double c = 2.0 * k * Math.log(2) / (harmonic(p)-1);
      double m = 2.0 / (harmonic(p)-1) * Math.log(2);
      System.out.println("" + c + "\t" + m );
    }
  }
  public double harmonic(int x) {
    double result=0;
    for (double i=1; i<x; i=i+1) {
      result += 1.0 / i;
    }
    return result;
  }
  @Test
  public void testEncapsulatedQuicksort() {
    ObjectRangeSorter<MyInteger> naked 
      = ObjectQuicksortTest.getQuicksort();
    ObjectArrayBucketSorter<MyInteger> encapsulated 
      = new ObjectArrayBucketSorter<MyInteger>(naked);
    ObjectSortList<MyInteger> sorts
      = new ObjectSortList<MyInteger>();
    sorts.add( naked );
    sorts.add( encapsulated );
    warmUpSorts(sorts, comp, 100);
    int n = 1000000;
    long[] timings = new long[sorts.size()];
    encapsulated.zeroTimers();
    int runCount = 25;
    for (int r=0; r<runCount; ++r) {
      int input[]  = random.randomPermutation(n);
      for (int s=0; s<sorts.size(); ++s) {
        MyInteger[] copy = new MyInteger[n];
        for (int i=0; i<n; ++i) {
          copy[i] = new MyInteger( input[i] );
        }
        ObjectRangeSorter<MyInteger> sort 
          = sorts.get(s);
        timings[s] -= System.nanoTime();
        sort.sortRange( comp, copy, 0, copy.length);
        timings[s] += System.nanoTime();
      }
    }
    System.out.println
      ( "All timings in nano-seconds"
      + " (sorting " + n + " integers)");
    System.out.println
      ( "Naked:\t" + timings[0]/runCount );
    System.out.println
      ( "Encapsulated:\t" + timings[1]/runCount );
    System.out.println
      ( "Loading:\t" 
      + encapsulated.getLoadingTimeInNanoSeconds()/runCount);
    System.out.println
      ( "Unloading:\t" 
      + encapsulated.getUnloadingTimeInNanoSeconds()/runCount);
  }
}
