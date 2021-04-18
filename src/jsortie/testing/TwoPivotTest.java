package jsortie.testing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jsortie.RangeSorter;
import jsortie.earlyexitdetector.WainwrightEarlyExitDetector;
import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.helper.RotationHelper;
import jsortie.janitors.exchanging.BranchAvoidingAlternatingCombsort;
import jsortie.janitors.insertion.InsertionSort;
import jsortie.janitors.insertion.PairInsertionSort;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.multiway.governor.AdaptiveMultiPivotQuicksort;
import jsortie.quicksort.multiway.governor.MultiPivotQuicksort;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.centrifugal.CentrifugalPartitioner2;
import jsortie.quicksort.multiway.partitioner.centripetal.CentripetalPartitioner2;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner2;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyCrossoverPartitioner2;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyCentripetalPartitioner2;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner2;
import jsortie.quicksort.multiway.partitioner.threepivot.KLQMPartitioner3;
import jsortie.quicksort.multiway.partitioner.twopivot.ComparisonCountingYaroslavskiyPartitioner2;
import jsortie.quicksort.multiway.partitioner.twopivot.DoubleLomutoPartitioner2;
import jsortie.quicksort.multiway.partitioner.twopivot.Java8Partitioner;
import jsortie.quicksort.multiway.partitioner.twopivot.YaroslavskiyMoveEqualOutPartitioner2;
import jsortie.quicksort.multiway.partitioner.twopivot.YaroslavskiyPartitioner2;
import jsortie.quicksort.multiway.partitioner.twopivot.YarrowPartitioner2;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.multiway.selector.clean.CleanMultiPivotPositionalSelector;
import jsortie.quicksort.multiway.selector.clean.SamplingMultiPivotSelector;
import jsortie.quicksort.multiway.selector.dirty.DirtyCompoundMultiPivotSelector;
import jsortie.quicksort.samplesizer.FixedSampleSizer;
import jsortie.quicksort.selector.dirty.Java8Selector;
import jsortie.quicksort.sort.reference.QuicksortJava8;
import jsortie.quicksort.theoretical.Gain;

public class TwoPivotTest extends MultiPivotTest {
	RangeSorter isort2 = new InsertionSort2Way();	
  public void addSort 
    ( MultiPivotSelector seller
    , MultiPivotPartitioner party
    , List<RangeSorter> sorts ) {
    sorts.add( new AdaptiveMultiPivotQuicksort
                   ( seller, party, isort2, 64) );
  }
  @Test
  public void testTwoPivotSorts() {
    MultiPivotSelector any2  = new SamplingMultiPivotSelector(2, false);
    SortList           sorts = new SortList(any2, new InsertionSort2Way(), 64);
    sorts.addMPQ( new YaroslavskiyPartitioner2());
    sorts.addMPQ( new DoubleLomutoPartitioner2()        );
    sorts.addMPQ( new HolierThanThouPartitioner2()      );
    sorts.addMPQ( new CentrifugalPartitioner2()         );
    sorts.addMPQ( new CentripetalPartitioner2()         );
    sorts.addMPQ( new SkippyPartitioner2()            );
    sorts.addMPQ( new SkippyCentripetalPartitioner2() );
    sorts.addMPQ( new SkippyCrossoverPartitioner2()   );
    sorts.addMPQ( new YarrowPartitioner2()              );

    int n = 65536;
    int r = 1048576 * 8 / n;
    DegenerateInputTest dit = DegenerateInputTest.newTest(n, r);
    sortTest.warmUpSorts(sorts);
    sorts.writeSortHeader("input");
    dit.runSortsOnSortedData     ( sorts, "second" );
    System.out.println("");
    dit.runSortsOnDuplicatedData ( sorts, "second" );
  }
  @Test  
  public void testTwoPivotSortsWithUniformSampling() {
    ArrayList<SamplingMultiPivotSelector> selectors
      = new ArrayList<SamplingMultiPivotSelector>();
    selectors.add(new SamplingMultiPivotSelector
                      ( 5,  new int[] { 1, 3 }, false ) );
    selectors.add(new SamplingMultiPivotSelector
                      ( 11, new int[] { 3, 7 }, false ) );

    for (SamplingMultiPivotSelector s : selectors) {
      System.out.println("With " + s.toString());      
      SortList sorts = new SortList(s, new InsertionSort2Way(), 64);
      sorts.addAdaptiveMPQ(new YaroslavskiyPartitioner2());
      sorts.addAdaptiveMPQ(new DoubleLomutoPartitioner2());
      sorts.addAdaptiveMPQ(new HolierThanThouPartitioner2());
      sorts.addAdaptiveMPQ(new CentrifugalPartitioner2());
      sorts.addAdaptiveMPQ(new CentripetalPartitioner2());
      sorts.addAdaptiveMPQ(new SkippyPartitioner2());
      sorts.addAdaptiveMPQ(new SkippyCentripetalPartitioner2());
      sorts.addAdaptiveMPQ(new SkippyCrossoverPartitioner2());
      sorts.addAdaptiveMPQ(new YarrowPartitioner2());
      
      DegenerateInputTest dit = new DegenerateInputTest();
      sortTest.warmUpSorts(sorts);
      sorts.writeSortHeader("input");
      dit.runSortsOnSortedData(sorts, "second");
      System.out.println("");
      dit.runSortsOnDuplicatedData(sorts, "second");
      System.out.println("");
    }
  }
  @Test
  public void testKangarooTwoPivotSortsWithBigSamples() {
    int    runCount = 100;
    int    n        = 1048576;
    double bitsInInput = SortTest.bitsLeftInPartitions
                         ( new int[] { 0, n } );
    ArrayList<MultiPivotPartitioner> kangas 
      = new ArrayList<MultiPivotPartitioner>();
    kangas.add(new SkippyPartitioner2());
    kangas.add(new SkippyCentripetalPartitioner2());
    warmUpPartitioners(kangas, 2);
    System.out.println("Efficiency in Gbps");
    System.out.println("c\tKP2\tKCP2\tPredicted Gain\tActual Gain");
    for (int c = 2; c<2000; c+=c+1) {
      DirtyCompoundMultiPivotSelector dcmps 
        = new DirtyCompoundMultiPivotSelector
              ( 2, new FixedSampleSizer(c, 0), new NullSampleCollector() );
      double[] elapsed = new double [ kangas.size() ]; //nanoseconds
      double[] gain    = new double [ kangas.size() ]; //bits
      for (int run=0; run<runCount; ++run) {
        int[] input  = random.randomPermutation(n);
        int[] pivots = dcmps.selectPivotIndices(input, 0, n);
        for (int p=0; p<kangas.size(); ++p) {
          MultiPivotPartitioner party = kangas.get(p);
          int[] copy = Arrays.copyOf(input, input.length);
          elapsed[p] -= System.nanoTime();
          int[] boundaries = party.multiPartitionRange
                             ( copy, 0, copy.length, pivots );
          elapsed[p] += System.nanoTime();
          double bitsInOutput 
            = SortTest.bitsLeftInPartitions(boundaries);
          gain[p]    +=  bitsInInput - bitsInOutput; 
        }
      }
      String line = "" + c;
      for (int p=0; p<kangas.size(); ++p) {
        line += "\t" + Math.floor(gain[p] / elapsed[p] * 1000.0 + .5)/1000.0;
      }
      double partyGain 
        = (Gain.harmonic(c+1) - Gain.harmonic( (c+1) / 3.0))
        / Math.log(2.0);
      line += "\t" + Math.floor(partyGain * 1000.0 + .5)/1000.0;
      line += "\t" + Math.floor(gain[0] / n / runCount * 1000.0)/1000.0;
      System.out.println(line);
    }
  }
  public void testYaroslavskiySelectors() {
    ArrayList<SamplingMultiPivotSelector> 
      selectors = new ArrayList<SamplingMultiPivotSelector>();
    RangeSorter isort2 = new InsertionSort2Way();
    selectors.add(new SamplingMultiPivotSelector(2,  false));
    selectors.add(new SamplingMultiPivotSelector(5,  new int[] { 1, 3 },  false));
    selectors.add(new SamplingMultiPivotSelector(8,  new int[] { 2, 4 },  false));
    selectors.add(new SamplingMultiPivotSelector(11, new int[] { 3, 7 },  false));
    selectors.add(new SamplingMultiPivotSelector(15, new int[] { 7, 10 }, false));
    selectors.add(new SamplingMultiPivotSelector(15, new int[] { 3, 7 },  false));
    selectors.add(new SamplingMultiPivotSelector(11, new int[] { 4, 7 },  false));
    MultiPivotPartitioner yaro = new YaroslavskiyPartitioner2();
    SortList sorts = new SortList();
    for (MultiPivotSelector s : selectors) {
      sorts.add(new AdaptiveMultiPivotQuicksort(s, yaro, isort2, 64));
    }		
    DegenerateInputTest dit = new DegenerateInputTest();
    dit.runSortsOnSortedData(sorts, "second");
    dit.runSortsOnDuplicatedData(sorts, "second");
  }
  public void testYaroslavskiyJava8() {
    int thresh = 47;
    SortList sorts = new SortList();
    MultiPivotSelector selector = new Java8Selector();
    RangeSorter psort = new PairInsertionSort();
    MultiPivotSelector any3 = new CleanMultiPivotPositionalSelector(3);
    sorts.add(new QuicksortJava8());
    sorts.add(new MultiPivotQuicksort
                  ( selector, new Java8Partitioner(), psort, thresh ) );
    sorts.add(new MultiPivotQuicksort
                  (any3, new KLQMPartitioner3(), psort, thresh) );
    String line = "";
    for (RangeSorter s : sorts) {
      line += "\t" + s.toString();
    }
    sortTest.warmUpSorts(sorts);
    System.out.println(line);
    sortTest.testSpecificSorts(sorts, 10, 10000, 400, 1000000, 25);
    System.out.println("");
    System.out.println(line);
    DegenerateInputTest dit = new DegenerateInputTest();
    dit.runSortsOnSortedData(sorts, "second");
    dit.runSortsOnDuplicatedData(sorts, "second");
    System.out.println("");
  }
  @Test
  public void blowUpJava8()
  {
    int                   n             = 50000000;
    RotationHelper        rot           = new RotationHelper();
    MultiPivotSelector    j8selector    = new Java8Selector();
    MultiPivotPartitioner j8partitioner = new Java8Partitioner();
    RangeSorter           j8janitor     = new PairInsertionSort();
    int[]                 vEvilArray    = new int[n];
    int[]                 vEvilCopy;
    int                   i              = 0;
    int                   j;
    for (j = n/64; rot.gcd(j,n)!=1; ++j);
    for (int h=0; h<n; ++h) {
      vEvilArray[i] = h;
      i = (i + j ) % n;
    }
    vEvilCopy = Arrays.copyOf(vEvilArray, vEvilArray.length);
    (new QuicksortJava8()).sortRange(vEvilCopy,0, vEvilCopy.length);
    vEvilCopy = Arrays.copyOf(vEvilArray, vEvilArray.length);
    (new MultiPivotQuicksort(j8selector, j8partitioner, j8janitor, 47))
    	.sortRange(vEvilCopy,0, vEvilCopy.length);
  }
  @Test
  public void testJaroslavskiyRetune() {
    MultiPivotSelector items3and6Of8 = new SamplingMultiPivotSelector(8, new int[] { 2, 5 }, false);
    RangeSorter        isort         = new InsertionSort2Way();
    RangeSorter        comb          = new BranchAvoidingAlternatingCombsort(1.4, 32);
    RangeSorter        heapsort      = new TwoAtATimeHeapsort();
    SortList sorts = new SortList();
    sorts.add(new QuicksortJava8());
    sorts.add(new MultiPivotQuicksort ( new Java8Selector()
                                      , new Java8Partitioner()
                                      , new PairInsertionSort(), 47));
    sorts.add(new AdaptiveMultiPivotQuicksort ( new Java8Selector()
                                              , new YaroslavskiyMoveEqualOutPartitioner2()
                                              , new PairInsertionSort(), 47));
    sorts.add ( new AdaptiveMultiPivotQuicksort
                    ( items3and6Of8 , new YaroslavskiyPartitioner2()
                    , isort, 100, heapsort, new WainwrightEarlyExitDetector()));
    sorts.add ( new AdaptiveMultiPivotQuicksort
        ( items3and6Of8, new YaroslavskiyPartitioner2(), comb, 100
        , heapsort, new WainwrightEarlyExitDetector()));
    String line = "input\tTrue-Java8\tFake-Java8\tMoveEqualOut\tIn-Governor\tCombsorted";
    sorts.warmUp();
    System.out.println(line);
    DegenerateInputTest dit = DegenerateInputTest.newTest(1048576,25);
    dit.runSortsOnSortedData    (sorts, "millisecond");
    System.out.println("");
    dit.runSortsOnDuplicatedData(sorts, "millisecond");
    System.out.println("");
  }
  public double bitsOfChaos(double n) {
    if (n<2) return 0;
    if (n==2) return 1.0;
    return n * (Math.log(n) - 1.0) / Math.log(2.0);
  }
  @Test
  public void testWForYaroslavskiy() {
    int n = 100000;
    for (int r=100;r<1000;r+=100) {
    double t = bitsOfChaos(n);
    double meanOfSquares = 0;
    double mean = 0;
    double meanGain = 0;
    double meanSpend = 0;
    ComparisonCountingYaroslavskiyPartitioner2 y 
      = new ComparisonCountingYaroslavskiyPartitioner2();
    int[] pivots = new int[] { 0, 1 };
    for (int i=0; i<r; ++i) {
      int[] vInput = random.randomPermutation(n);
      InsertionSort.sortSmallRange(vInput, 0, 2);
      y.setComparisonCount(0);
      int[] iBoundaries = y.multiPartitionRange(vInput, 0, n, pivots);
      double gain = t - bitsOfChaos(iBoundaries[1])
          - bitsOfChaos(iBoundaries[3]-iBoundaries[2]) 
          - bitsOfChaos(iBoundaries[5]-iBoundaries[4]);
      double spend = y.getComparisonCount();
      meanGain  += gain / (double)n / (double)r;
      meanSpend += spend / (double)n / (double)r;
      double waste = spend - gain;
      meanOfSquares += waste*waste / (double)n / (double)n / (double)r;
      mean += waste / (double)n /(double)r;
    }
    System.out.println("For " + r + " runs on " + n + "-item partitions...");
    System.out.println("mean(w)\t" + mean /*+ "\tmean*12/19\t" + mean/19.0*12.0*/);
    System.out.println("mean(w^2) " + meanOfSquares + "\tminus mean(w)^2 \t" + mean*mean 
        + "\tequals\t" + (meanOfSquares-mean*mean));
    double Ec = 10.0 / 19.0 / Math.log(2);
    System.out.println("Divided by (1-Tr)*(Ec^2) " 
        + (meanOfSquares-mean*mean)/Ec/Ec/0.5 
        + " Where Ec is " + Ec);
    System.out.println("Measured Ec was " + meanGain/meanSpend);
    System.out.println("");
    }
  }
  @Test
  public void testYaroslavskiyVariance() {
    int n = 100000;
    int r = 1000;
    ComparisonCountingYaroslavskiyPartitioner2 y 
      = new ComparisonCountingYaroslavskiyPartitioner2();
    SamplingMultiPivotSelector any2 = new SamplingMultiPivotSelector(2, false);
    MultiPivotQuicksort q 
      = new MultiPivotQuicksort(any2, y, new InsertionSort(), 3 );
    double sum = 0;
    double sumOfSquares = 0;
    for (int i=0; i<r; ++i) {
      int[] vInput = random.randomPermutation(n);
      y.setComparisonCount(0);
      q.sortRange(vInput, 0, vInput.length);
      double c = y.getComparisonCount();
      sum += c;
      sumOfSquares += c*c;
    }
    double mean     = sum / (double)r;
    double variance = sumOfSquares/(double)r - mean*mean;
    System.out.println(variance/n/n);
  }
}
