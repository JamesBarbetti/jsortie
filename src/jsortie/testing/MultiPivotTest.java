package jsortie.testing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import jsortie.RangeSorter;
import jsortie.earlyexitdetector.RangeSortEarlyExitDetector;
import jsortie.earlyexitdetector.TwoWayInsertionEarlyExitDetector;
import jsortie.earlyexitdetector.WainwrightEarlyExitDetector;
import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.helper.DumpRangeHelper;
import jsortie.janitors.exchanging.BranchAvoidingAlternatingCombsort;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.quicksort.governor.QuicksortGovernor;
import jsortie.quicksort.governor.adaptive.QuicksortAdaptive;
import jsortie.quicksort.governor.adaptive.QuicksortAdaptive2;
import jsortie.quicksort.multiway.governor.AdaptiveMultiPivotQuicksort;
import jsortie.quicksort.multiway.governor.MultiPivotQuicksort;
import jsortie.quicksort.multiway.partitioner.FixedCountPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.adapter.SingleToMultiPartitioner;
import jsortie.quicksort.multiway.partitioner.centrifugal.CentrifugalPartitioner2;
import jsortie.quicksort.multiway.partitioner.centrifugal.CentrifugalPartitioner3;
import jsortie.quicksort.multiway.partitioner.centrifugal.CentrifugalPartitioner4;
import jsortie.quicksort.multiway.partitioner.centrifugal.CentrifugalPartitioner5;
import jsortie.quicksort.multiway.partitioner.centrifugal.CentrifugalPartitioner6;
import jsortie.quicksort.multiway.partitioner.centripetal.CentripetalPartitioner2;
import jsortie.quicksort.multiway.partitioner.centripetal.CentripetalPartitioner4;
import jsortie.quicksort.multiway.partitioner.fourpivot.FourPivotMetamorphicPartitioner;
import jsortie.quicksort.multiway.partitioner.fourpivot.FourPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.geometric.GeometricPartitioner2;
import jsortie.quicksort.multiway.partitioner.geometric.GeometricPartitioner3;
import jsortie.quicksort.multiway.partitioner.geometric.GeometricPartitioner4;
import jsortie.quicksort.multiway.partitioner.geometric.GeometricPartitioner5;
import jsortie.quicksort.multiway.partitioner.geometric.GeometricPartitioner6;
import jsortie.quicksort.multiway.partitioner.geometric.GeometricPartitioner7;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner3;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner5;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner6;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner7;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyCentripetalPartitioner2;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner2;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner3;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner4;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner5;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner6;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner7;
import jsortie.quicksort.multiway.partitioner.kangaroo.KangarooPawPartitioner3;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner2;
import jsortie.quicksort.multiway.partitioner.morepivots.FivePivotPartitioner;
import jsortie.quicksort.multiway.partitioner.morepivots.SevenPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.pretend.FancierMultiplexingPartitioner;
import jsortie.quicksort.multiway.partitioner.pretend.MultiplexingPartitioner;
import jsortie.quicksort.multiway.partitioner.threepivot.KLQMPartitioner3;
import jsortie.quicksort.multiway.partitioner.twopivot.DoubleLomutoPartitioner2;
import jsortie.quicksort.multiway.partitioner.twopivot.YaroslavskiyPartitioner2;
import jsortie.quicksort.multiway.partitioner.twopivot.YarrowPartitioner2;
import jsortie.quicksort.multiway.protector.CheckedMultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.twopivot.YaroslavskiyMetamorphic;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.multiway.selector.clean.CleanFirstPSelector;
import jsortie.quicksort.multiway.selector.clean.CleanMultiPivotPositionalSelector;
import jsortie.quicksort.multiway.selector.clean.SamplingMultiPivotSelector;
import jsortie.quicksort.multiway.selector.dirty.DirtyCompoundMultiPivotSelector;
import jsortie.quicksort.multiway.selector.dirty.DirtyFirstPSelector;
import jsortie.quicksort.partitioner.bidirectional.centrepivot.CentrePivotPartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.HoyosPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.BalancedSkippyPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyCentripetalPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyMirrorPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.interfaces.StandAlonePartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.clean.CleanRightHandedSelector;
import jsortie.quicksort.selector.dirty.DirtyTheoreticalSelector;
import jsortie.quicksort.selector.simple.CleanPositionalSelector;
import jsortie.quicksort.selector.simple.MiddleElementSelector;
import jsortie.quicksort.selector.clean.CleanPositionalSampleSelector;

public class MultiPivotTest {
  protected RandomInput random = new RandomInput();
  protected PartitionerTest pt = new PartitionerTest();
  protected SortTest  sortTest = new SortTest();
  @Test
  public void testGeometricPartitioners() {
    //Partitioners
  	SinglePivotPartitioner kanga = new BalancedSkippyPartitioner();
    StandAlonePartitioner partitioners[] 
      = new     StandAlonePartitioner[]
        { new GeometricPartitioner2()
        , new GeometricPartitioner3()
        , new GeometricPartitioner4()
        , new GeometricPartitioner5()
        , new GeometricPartitioner6()
        , new GeometricPartitioner7()
        };
    //Selectors & Early-exit detectors
    SinglePivotSelector         middle = new MiddleElementSelector();
    WainwrightEarlyExitDetector eed    = new WainwrightEarlyExitDetector();
    //Janitor
    RangeSorter lastResort = new TwoAtATimeHeapsort();
    RangeSorter isort2     = new InsertionSort2Way();
    RangeSorter janitor    = new QuicksortAdaptive(middle, kanga, isort2, 64, lastResort, eed);
    //Sorts
    SortList sorts   = new SortList();
    int      thresh  = 64; 
    for (int p=0; p<partitioners.length; ++p) {
      RangeSorter sort = new AdaptiveMultiPivotQuicksort
                             ( partitioners[p], janitor, thresh );
      sorts.add(sort, partitioners[p].toString());
      janitor = sort;
      thresh += thresh;
    }
    //Tests
    DegenerateInputTest dt = DegenerateInputTest.newTest(1048576, 10);
    sorts.warmUp();
    sorts.writeSortHeader       ( "input" );
    dt.runSortsOnSortedData     ( sorts, "second" );
    System.out.println("");
    dt.runSortsOnDuplicatedData ( sorts, "second" );
  }
  public void testQuicksortScheme(int pivots, int sample) {
    double[] a        = new double[sample];
    double   swaps    = 0;
    double   compares = 0;
    double   gain     = 0;
    int      run;
    int step  = sample/pivots;
    int start = (sample-pivots)/pivots/2;
    for (run = 0; run< 1000000; ++run) {
      for (int k=0; k<sample; ++k) {
        a[k] = Math.random();
      }
      java.util.Arrays.sort(a);
			
      int i;
      for (i=start;i<sample/2;i+=step) {
        swaps    += a[i];
        compares += a[i+step];
      }
      if (i==sample/2 && (pivots&1)!=0) {
        swaps    += a[i]*(1-a[i]);
        compares += 1;
        i        += step;
      }
      for (;i<sample;i+=step) {
        swaps    += 1-a[i];
        compares += (1 - a[i-step]);
      }
      for (i=start; i<=sample+step-1; i+=step) {
        double s = (i<step)    ? 0 : a[i-step];
        double e = (sample<=i) ? 1 : a[i];
        if (s<e) {
          gain -= (e-s)*Math.log(e-s)/Math.log(2);
        }
      }
    }
    System.out.println("" + pivots +"\t" + sample  + swaps/run
      + "\t" + compares/run + "\t" + gain/run
      + "\t" + swaps/gain + "\t" + compares/gain 
      + "\t" + (swaps+compares)/gain ); 
  }
  public void testMultiPivotSorts() {
    MultiPivotSelector any2         = new SamplingMultiPivotSelector(2, false);
    MultiPivotSelector outside2of8  = new SamplingMultiPivotSelector(8, new int[]{0,7}, false);
    MultiPivotSelector take4and6of7 = new SamplingMultiPivotSelector(7, new int[]{3,5}, false);
    MultiPivotSelector take2and4of5 = new SamplingMultiPivotSelector(5, new int[]{1,3}, false);
    MultiPivotSelector take2and4of7 = new SamplingMultiPivotSelector(7, new int[]{1,3}, false);
    MultiPivotSelector any3         = new SamplingMultiPivotSelector(3, false);
    MultiPivotSelector take369of11  = new SamplingMultiPivotSelector(11, new int[]{2,5,8}, false);
    MultiPivotSelector take147of7   = new SamplingMultiPivotSelector(7, new int[]{0,3,6}, false);
    MultiPivotSelector any4         = new SamplingMultiPivotSelector(4, false);
    MultiPivotSelector take1267of7  = new SamplingMultiPivotSelector(7, new int[]{0,1,5,6}, false);
    MultiPivotSelector any6         = new SamplingMultiPivotSelector(6, false);
    MultiPivotSelector k12          = new SamplingMultiPivotSelector(24, new int[]{0,1,3,8,10,11}, false);
    MultiPivotSelector any7         = new SamplingMultiPivotSelector(7, false);
    class SPQ extends QuicksortGovernor {
      public SPQ( SinglePivotSelector s, SinglePivotPartitioner p) {
        super(s,p, new TwoAtATimeHeapsort(), 64);
      }
    }
    class MPQ extends MultiPivotQuicksort {
      public MPQ( MultiPivotSelector s, MultiPivotPartitioner p) {
        super(s, p, new TwoAtATimeHeapsort(), 64);
      }
    }
    SortList sorts = new SortList();
    sorts.add( new SPQ (new CleanRightHandedSelector(true), new HoyosPartitioner()));
    sorts.add( new MPQ ( any2,         new DoubleLomutoPartitioner2() ));
    sorts.add( new MPQ ( take4and6of7, new DoubleLomutoPartitioner2() ));
    sorts.add( new MPQ ( outside2of8,  new DoubleLomutoPartitioner2() ));
    sorts.add( new MPQ ( any2,         new YaroslavskiyPartitioner2() ));
    sorts.add( new MPQ ( take2and4of5, new YaroslavskiyPartitioner2() ));
    sorts.add( new MPQ ( take2and4of7, new YaroslavskiyPartitioner2() ));
    sorts.add( new MPQ ( outside2of8,  new YaroslavskiyPartitioner2() ));
    sorts.add( new MPQ ( any2,         new YaroslavskiyMetamorphic() ));
    sorts.add( new MPQ ( any3,         new KLQMPartitioner3() ));
    sorts.add( new MPQ ( take369of11,  new KLQMPartitioner3() ));
    sorts.add( new MPQ ( take147of7,   new KLQMPartitioner3() ));
    sorts.add( new MPQ ( any4,         new FourPivotPartitioner() ));
    sorts.add( new MPQ ( take1267of7,  new FourPivotPartitioner() ));
    sorts.add( new MPQ ( any6,         new HolierThanThouPartitioner6() ));
    sorts.add( new MPQ ( k12,          new HolierThanThouPartitioner6() ));
    sorts.add( new MPQ (any7,          new SevenPivotPartitioner() ) );
    /*
     * sorts.add( new MPQ ( any8,         new EightPivotPartitioner() ));
     * sorts.add( new MPQ ( k24,          new EightPivotPartitioner() ));
     */
    sortTest.testSpecificSorts( sorts, 10, 10000000, 1  );
  }
  public void testHolierThanThou7() {
    SortList sorts        = new SortList();
    MultiPivotSelector     any2         = new SamplingMultiPivotSelector(2, false);
    MultiPivotSelector     any3         = new SamplingMultiPivotSelector(3, false);
    MultiPivotSelector     any7         = new SamplingMultiPivotSelector(7, false);
    RangeSorter            isort2       = new InsertionSort2Way();
    HolierThanThouPartitioner2 htt2     = new HolierThanThouPartitioner2();
    HolierThanThouPartitioner3 htt3     = new HolierThanThouPartitioner3();
    HolierThanThouPartitioner7 htt7     = new HolierThanThouPartitioner7();
    YaroslavskiyMetamorphic myaro       = new YaroslavskiyMetamorphic();
    sorts.add( new MultiPivotQuicksort(any2,   myaro, isort2, 64));
    sorts.add( new MultiPivotQuicksort(any2,   htt2,  isort2, 64));
    sorts.add( new MultiPivotQuicksort(any3,   htt3,  isort2, 64));
    sorts.add( new MultiPivotQuicksort(any7,   htt7,  isort2, 64));
    sortTest.testSpecificSorts( sorts, 10, 10000, 400, 10000000, 25  );		
  }
  public void test5PivotPartitionersWithSampling() {
    ArrayList<MultiPivotSelector> selectors = new ArrayList<MultiPivotSelector>();
    for (int s=3; s<=5; s+=2) {
      selectors.add(new SamplingMultiPivotSelector(s*6+5, false));
    }
    MultiPivotSelector skewed = new SamplingMultiPivotSelector(19, new int[] { 1,3,13,15,17 }, false);
    selectors.add(skewed);		
    ArrayList<MultiPivotPartitioner> partitioners = new ArrayList<MultiPivotPartitioner>();
    partitioners.add ( new FivePivotPartitioner());
    partitioners.add ( new HolierThanThouPartitioner5());
    partitioners.add ( new CentrifugalPartitioner5());		
    sortTest.testSelectorsAndPartitioners( selectors, partitioners, new int[] { 8192, 8192, 1048576, 33554432 });
  }
  public void test246and8PivotPartitioners() {
    System.out.println("p\tCost");
    for (int p=2; p<10; p+=2) { //number of pivots
      double cTotal = 0;
      double mTotal = 0;
      int r;
      for (r=0; r<100000; ++r) {
        double v[] = new double[p];
        for (int i=0; i<p; ++i)
          v[i] = Math.random();
        double c=1;
        for (int a=1; a<p/2-1; ++a) c+=v[a];
        for (int b=p/2-1; b<p; ++b) c+=(1-v[b]);
        double c2=1;
        for (int i=0; i<p; ++i)
          v[i] = 1-v[p-1-i];
        for (int a=1; a<p/2-1; ++a) c2+=v[a];
        for (int b=p/2-1; b<p; ++b) c2+=(1-v[b]);
        cTotal += c;
        mTotal += ( c< c2 ) ? c : c2;
      }
      System.out.println("" + p + "\t" + cTotal/(double)r + "\t" + mTotal/(double)r ) ;
    }
  }
  @Test
  public void testFivePivotPartitioner() {
    MultiPivotSelector    spp = new SamplingMultiPivotSelector(11, 5, false);
    MultiPivotPartitioner mpp = new FivePivotPartitioner();
    usePartitionerOnce(37, spp, mpp);
  }
  public void testHolierThanThou7Once() {
    MultiPivotSelector    spp = new SamplingMultiPivotSelector(7, false);
    MultiPivotPartitioner mpp = new HolierThanThouPartitioner7();
    usePartitionerOnce(25, spp, mpp);		
  }
  public void testGeometricPartitioner7Once() {
    MultiPivotSelector    spp = new SamplingMultiPivotSelector(127, new int[]{0,1,3,7,15,31,63}, false);
    MultiPivotPartitioner mpp = new GeometricPartitioner7();
    usePartitionerOnce(200, spp, mpp);				
  }
  public void testCP4() {
   MultiPivotSelector spp = new SamplingMultiPivotSelector(4, false);
   CentripetalPartitioner4 cp4 = new    CentripetalPartitioner4();
   MultiPivotPartitioner mpp = new CheckedMultiPivotPartitioner(cp4);
   usePartitionerOnce(100, spp, mpp);
  }
  public void usePartitionerOnce(int n, MultiPivotSelector spp, MultiPivotPartitioner mpp) {
    Random r = new Random();
    r.setSeed(0);
    int a[] = random.randomPermutation(n, r);
    DumpRangeHelper.dumpRange("Input ", a, 0, n);		
    int p[] = spp.selectPivotIndices(a, 0, n);
    for (int i=0; i<p.length; ++i) {
      System.out.println("Pivot " + (i+1) + " is a[" + p[i] + "] == " + a[p[i]]);
    }
    System.out.println("");
    int par[] = mpp.multiPartitionRange(a, 0, n, p);
    for (int i=0; i<par.length; i+=2) {
      if (0<i && par[i-1]<par[i]) {
        if (par[i-1]==par[i]-1) {
          System.out.println("Pivot " + (i/2+1) + " is a[" + (par[i]-1) + "] == " + a[(par[i]-1)]);
        } else {
          DumpRangeHelper.dumpRange("Intermediates ", a, par[i-1], par[i]);
        }
      }
      DumpRangeHelper.dumpRange("Partition " + ((i/2)+1) + " ", a, par[i], par[i+1]);
      InsertionSort2Way.sortSmallRange(a, par[i], par[i+1]);
      DumpRangeHelper.dumpRange("(Sorted)  " + ((i/2)+1) + " ", a, par[i], par[i+1]);
    }
    DumpRangeHelper.dumpRange("Output ", a, 0, n);
    for (int i=0; i<a.length; ++i) {
      if (a[i]!=i) {
        System.out.println("fail at " + i);
        break;
      }
    }
  }
  @Test
  public void testDebugKPP3() {
    KangarooPawPartitioner3 kpp3 = new KangarooPawPartitioner3();
    usePartitionerOnce(51, new DirtyFirstPSelector(3), kpp3);
  }
  public void testQA2() {
    //for a bug-hunt, 12th July, 2017.
    SortList sorts = new SortList();
    RangeSorter isort = new InsertionSort2Way();	
    sorts.add( new QuicksortAdaptive2(new CleanPositionalSelector(1,4), new HoyosPartitioner(), isort, 5, new TwoAtATimeHeapsort()) );
    sortTest.testRandomInputs(sorts,12, 13, false);
  }
  protected class AMPQ extends AdaptiveMultiPivotQuicksort
  {
    public AMPQ( MultiPivotSelector selector, MultiPivotPartitioner partitioner) {
      super( selector, partitioner, new InsertionSort2Way(), 15 );
    }
  }
  public void testFucker() {
    RangeSorter isort = new InsertionSort2Way();
    RangeSorter hsort = new TwoAtATimeHeapsort();
    SortList sorts = new SortList();
    sorts.add( new QuicksortAdaptive2(new CleanPositionalSelector(1,4), new CentrePivotPartitioner(), isort, 15, hsort) );
    sorts.add( new AMPQ( new SamplingMultiPivotSelector(2,     false), new YaroslavskiyMetamorphic() ) );
    sorts.add( new AMPQ( new SamplingMultiPivotSelector(7,  3, false), new KLQMPartitioner3() ) );
    sorts.add( new AMPQ( new SamplingMultiPivotSelector(4,     false), new FourPivotMetamorphicPartitioner()) );
    sorts.add( new AMPQ( new SamplingMultiPivotSelector(11, 5, false), new FivePivotPartitioner()) );
    sorts.add( new AMPQ( new SamplingMultiPivotSelector(13, 6, false), new HolierThanThouPartitioner6()) );
    sortTest.testSpecificSortsShowPowerbands( sorts, 20, 10000000, 5 );		
  }	
  public void trySpecificPivots(int pivots
    , ArrayList<MultiPivotPartitioner> partitioners
    , double[] x, int[] valuesOfN) {
    long overallStart = System.nanoTime();
    String title = "Partitioning efficiency with skewed pivots, with x [";
    for (int p=0; p<x.length; ++p) title += ((0<p) ? "," : "") + x[p];
    System.out.println(title);
    MultiPivotSelector mps = new CleanFirstPSelector(pivots);
    int partyCount = partitioners.size();
    warmUpPartitioners(partitioners, pivots);
    String[]    header = new String[] { "n", "Partitioner" };
    String[] partyLine = new String[partyCount];
    for (int p=0; p<partyCount; ++p) {
      partyLine[p] = partitioners.get(p).toString();
    }
    for (Integer n: valuesOfN ) {
      double bitsToStartWith = SortTest.bitsLeftInPartitions( new int[] { 0, n } );
      header[0] += "\t" + n.intValue() + "\t\t";
      header[1] += "\tEfficiency (in Gbps)";
      int runCount = 5;
        if (n <= 1048576) runCount = 25;
        if (n <= 65536)   runCount = 100;
        double nanosElapsed[] = new double[partyCount];
        double gain[] = new double[partyCount];
        for (int r=0; r<runCount; ++r) {
        int testData [] = random.randomPermutation(n);
        for (int p=0; p<partyCount; ++p) {
          MultiPivotPartitioner party = partitioners.get(p);
          for (int i=0; i<pivots; ++i) {
            testData[i] = (int) Math.floor( x[i]*(double)n );	
          }				
          int[] copy         = Arrays.copyOf( testData, n);
          int[] pivotIndices = mps.selectPivotIndices(copy, 0, copy.length);
          long  start        = System.nanoTime();
          int[] partitions   = party.multiPartitionRange (copy, 0, copy.length, pivotIndices);
          long  stop         = System.nanoTime();
          nanosElapsed[p]   += (stop-start);	
          gain[p]           += bitsToStartWith - SortTest.bitsLeftInPartitions(partitions);
        } //partitioner
      } //run
      for (int p=0; p<partyCount; ++p) {
        double efficiency = gain[p] / nanosElapsed[p];
        partyLine[p] += "\t" + Math.floor(1000.0 * efficiency)/1000.0;
      }
    }
    for (String text: header) {
      System.out.println(text);
    }
    for (int p=0; p<partyCount; ++p) {
      System.out.println(partyLine[p]);
    }
      
    long overallStop = System.nanoTime();
    System.out.println("Total elapsed time for skewed pivot trial runs: "
      + intervalToTimeString(overallStart, overallStop));
    System.out.println("");
  }
  public void trySkewedPivots(int pivots, int sampleSize
    , ArrayList<MultiPivotPartitioner> partitioners
    , int[] valuesOfN) {
    long overallStart = System.nanoTime();
    System.out.println("Partitioning efficiency with skewed pivots, with x 1/" + sampleSize
      + " through " + (sampleSize-1) + "/" + sampleSize);
    MultiPivotSelector mps = new CleanFirstPSelector(pivots);
    int         partyCount = partitioners.size();
    String[]        header = new String[] { "n", "Partitioner" };
    String[]      bestLine = new String[partyCount];
    String[]     worstLine = new String[partyCount];
    for (int p=0; p<partyCount; ++p) {
      bestLine[p] = worstLine[p] = partitioners.get(p).toString();
    }   
    warmUpPartitioners(partitioners, pivots); 
    for (Integer n: valuesOfN ) {
      header[0] += "\t" + n.intValue() + "\t\t";
      header[1] += "\tPivots\tGain\tEfficiency";
      double[] bestEfficiency  = new double[partyCount];
      double[] worstEfficiency = new double[partyCount];
      String[] bestPivots      = new String[partyCount];
      String[] worstPivots     = new String[partyCount];
      int runCount = 1;
      if (n <= 1048576) runCount = 25;
      if (n <= 65536)   runCount = 100;
      int positions[] = firstPivotCombination(pivots, sampleSize); 
      do {
        double gain = 0; //in bits of information
        String pivotPositionString = "[";
        int lastRank =0;
        double fraction;
        for (int i=0; i<pivots; ++i) {
          pivotPositionString += (0<i) ? "," : "";
          pivotPositionString += (positions[i]<10 && 10<sampleSize) ? " " : "";
          pivotPositionString += positions[i];
          int rank = positions[i]*n / sampleSize;
          fraction = ((double)rank-(double)lastRank)/(double)n;
          gain    -= fraction * Math.log( fraction ) / Math.log(2.0); //bits
          lastRank = rank;
        }
        fraction = ((double)n-(double)lastRank)/(double)n;
        gain     -= fraction * Math.log( fraction ) / Math.log(2.0); //bits
        pivotPositionString += "]\t" + Math.floor(gain*1000.0)/1000.0;
        double nanosElapsed[] = new double[partyCount];
        for (int r=0; r<runCount; ++r) {
          int testData [] = random.randomPermutation(n);
          for (int p=0; p<partyCount; ++p) {
            MultiPivotPartitioner party = partitioners.get(p);
            for (int i=0; i<pivots; ++i) {
              testData[i] = (int) Math.floor( ((double)positions[i]*(double)n) / (double)sampleSize);
            }
            int[] copy         = Arrays.copyOf( testData, n);
            int[] pivotIndices = mps.selectPivotIndices(copy, 0, copy.length);
            long  start        = System.nanoTime();
            party.multiPartitionRange (copy, 0, copy.length, pivotIndices);
            long  stop         = System.nanoTime();
            nanosElapsed[p]   += (stop-start);
          }
        }
        for (int p=0; p<partyCount; ++p) {
          double efficiency = gain * (double)n * (double)runCount / nanosElapsed[p];
          if ( bestEfficiency[p] < efficiency ) {
            bestEfficiency[p] = efficiency;
            bestPivots[p]     = pivotPositionString + "\t" 
          		  + Math.floor(efficiency * 1000.0) / 1000.0;
          } 
          if ( efficiency < worstEfficiency[p] || worstEfficiency[p]==0) {
            worstEfficiency[p] = efficiency;
            worstPivots[p]     = pivotPositionString + "\t" 
            		  + Math.floor(efficiency * 1000.0) / 1000.0;
          }
        }
      } while (getNextPivotCombination(positions, sampleSize));
      for (int p=0; p<partyCount; ++p) {
        bestLine[p]  += "\t" + bestPivots[p];
        worstLine[p] += "\t" + worstPivots[p];
      }
    } 
    System.out.println("Best pivot positions...");
    for (String text: header) {
      System.out.println(text);
    }
    for (int p=0; p<partyCount; ++p) {
      System.out.println(bestLine[p]);
    }
    System.out.println("Worst pivot positions...");
    for (String text: header) {
      System.out.println(text);
    }
    for (int p=0; p<partyCount; ++p) {
      System.out.println(worstLine[p]);
    }
    long overallStop = System.nanoTime();
    System.out.println
      ( "Total elapsed time for skewed pivot trial runs: "
      + intervalToTimeString(overallStart, overallStop));
  }
  void warmUpPartitioners
    ( List<MultiPivotPartitioner> partitioners
    , int pivots ) {
    int n=10000;
    MultiPivotSelector mps 
      = new CleanFirstPSelector(pivots);
    for (int r=0;r<50;++r) {
      int[] junk = random.randomPermutation(n);
      for (MultiPivotPartitioner partitioner : partitioners) {
        int[] copy 
          = Arrays.copyOf( junk, n);
        int[] pivotIndices 
          = mps.selectPivotIndices(copy, 0, copy.length);
        partitioner.multiPartitionRange 
          ( copy, 0, copy.length, pivotIndices );
      }
    }
  }
  public void warmUpPartitioners
    ( Collection<FixedCountPivotPartitioner> 
      multiPivotPartitioners ) {
    int n=1000;
    for (int r=0;r<5000;++r) {
      int[] junk = random.randomPermutation(n);
      for ( FixedCountPivotPartitioner partitioner 
            : multiPivotPartitioners) {
        MultiPivotSelector mps 
          = new CleanFirstPSelector
                ( partitioner.getPivotCount() );
        int[] copy         = Arrays.copyOf( junk, n);
        int[] pivotIndices 
          = mps.selectPivotIndices(copy, 0, copy.length);
        partitioner.multiPartitionRange 
          (copy, 0, copy.length, pivotIndices);
      }
    }
  }
  void warmUpPartitioner
    ( MultiPivotPartitioner partitioner, int pivots ) {
    int n=10000;
    MultiPivotSelector mps 
      = new CleanFirstPSelector(pivots);
    for (int r=0;r<50;++r) {
      int[] junk = random.randomPermutation(n);
      int[] pivotIndices = mps.selectPivotIndices(junk, 0, junk.length);
      partitioner.multiPartitionRange (junk, 0, junk.length, pivotIndices);
    }
  }
  private String intervalToTimeString(long overallStart, long overallStop) {
    return "" + Math.floor((double) (overallStop - overallStart) / 1000.0 / 1000.0 ) / 1000.0
    + " seconds";
  }
  private int[] firstPivotCombination(int pivots, int sampleSize) {
    int [] positions = new int [pivots];
    for (int i=0; i<pivots; ++i) {
      positions[i] = i+1;
    }
    return positions;
  }
  private boolean getNextPivotCombination(int[] positions, int sampleSize) {
    int i;
    for (i=positions.length-1; i>=0; --i) {
      if (positions[i] + positions.length - i < sampleSize) {
        positions[i]++;
        for (++i; i<positions.length; ++i) {
          positions[i]=positions[i-1]+1;
        }
        return true;
      }
    }
    return false;
  }
  public SinglePivotSelector getCleanSel(int n) {
    return new CleanPositionalSampleSelector(n);
  }
  public MultiPivotSelector getCleanMultiSel(int n) {
    return new CleanMultiPivotPositionalSelector(n);
  }
  @Test
  public void testKangarooMultiPivotPartitioners() {
    //Partitioners
    SinglePivotPartitioner     kp       = new SkippyPartitioner();
    SinglePivotPartitioner     kcp      = new SkippyCentripetalPartitioner();
    FixedCountPivotPartitioner  kp2      = new SkippyPartitioner2();
    FixedCountPivotPartitioner  kcp2     = new SkippyCentripetalPartitioner2();
    FixedCountPivotPartitioner  kp3      = new SkippyPartitioner3();
    FixedCountPivotPartitioner  kp4      = new SkippyPartitioner4();
    FixedCountPivotPartitioner  kp5      = new SkippyPartitioner5();
    FixedCountPivotPartitioner  kp6      = new SkippyPartitioner6();
    FixedCountPivotPartitioner  kp7      = new SkippyPartitioner7();
    RangeSorter                hsort    = new TwoAtATimeHeapsort();
    RangeSortEarlyExitDetector detector = new TwoWayInsertionEarlyExitDetector();
    RangeSorter                combover = new BranchAvoidingAlternatingCombsort(1.4, 128); //As strategic janitor
    //Sorts using dirty multi-selectors
    RangeSorter k1  = new QuicksortAdaptive ( getDirtySel(), kp,  combover, 256, hsort, detector );
    RangeSorter k1c = new QuicksortAdaptive ( getDirtySel(), kcp, combover, 256, hsort, detector );
    RangeSorter k2  = new AdaptiveMultiPivotQuicksort ( getDirtyMultiSel(2), kp2,  combover, 256);
    RangeSorter k2c = new AdaptiveMultiPivotQuicksort ( getDirtyMultiSel(2), kcp2, combover, 256);
    RangeSorter k3  = new AdaptiveMultiPivotQuicksort ( getDirtyMultiSel(3), kp3,  combover, 256);
    RangeSorter k4  = new AdaptiveMultiPivotQuicksort ( getDirtyMultiSel(4), kp4,  combover, 256);
    RangeSorter k5  = new AdaptiveMultiPivotQuicksort ( getDirtyMultiSel(5), kp5,  combover, 256);
    RangeSorter k6  = new AdaptiveMultiPivotQuicksort ( getDirtyMultiSel(6), kp6,  combover, 256);
    RangeSorter k7  = new AdaptiveMultiPivotQuicksort ( getDirtyMultiSel(7), kp7,  combover, 256);
    SortList skippies = new SortList ();
    skippies.add(k1);
    skippies.add(k1c);
    skippies.add(k2);
    skippies.add(k2c);
    skippies.add(k3);
    skippies.add(k4);
    skippies.add(k5);
    skippies.add(k6);
    skippies.add(k7);
    skippies.warmUp(); 
    System.out.println("Running times for sorts using kangaroo partitioners");
    DegenerateInputTest dit = DegenerateInputTest.newTest(65536, 400);
    skippies.writeSortHeader ( "Input" );
    dit.runSortsOnSortedData      ( skippies, "millisecond" );
    System.out.println            ( "" );
    dit.runSortsOnDuplicatedData  ( skippies, "millisecond" );
  }
  @Test
  public void testKangarooFakeMultiPivotPartitioners() {
    SinglePivotPartitioner left
      = new SkippyMirrorPartitioner();
    SinglePivotPartitioner right
      = new SkippyPartitioner();
    MultiPivotPartitioner multi 
      = new MultiplexingPartitioner(left, right);
    MultiPivotPartitioner fancy 
      = new FancierMultiplexingPartitioner(left, right);
    RangeSorter combover 
      = new BranchAvoidingAlternatingCombsort(1.4, 128); //As tactical janitor
    SortList skippies 
      = new SortList ();
    for (int i=1; i<8; ++i) {
      skippies.add ( new MultiPivotQuicksort ( getDirtyMultiSel(i), multi,  combover, 256), "M" + i);
      skippies.add ( new MultiPivotQuicksort ( getDirtyMultiSel(i), fancy,  combover, 256), "F" + i);
    }
    skippies.warmUp(); 
    System.out.println("Running times for sorts using multiplexed kangaroo partitioners");
    DegenerateInputTest dit = DegenerateInputTest.newTest(65536, 400);
    skippies.writeSortHeader ( "Input" );
    dit.runSortsOnSortedData      ( skippies, "millisecond" );
    System.out.println            ( "" );
    dit.runSortsOnDuplicatedData  ( skippies, "millisecond" );
  }
  
  @Test
  public void testFancierMultiPivotSelectors() {
    ArrayList<FixedCountPivotPartitioner> dpa = new ArrayList<FixedCountPivotPartitioner>();
    dpa.add(new SingleToMultiPartitioner(new SkippyPartitioner()));
    dpa.add(new SingleToMultiPartitioner(new SkippyCentripetalPartitioner()));
    dpa.add(new SkippyPartitioner2());
    dpa.add(new SkippyCentripetalPartitioner2());
    dpa.add(new SkippyPartitioner3());
    dpa.add(new KangarooPawPartitioner3());
    dpa.add(new SkippyPartitioner4());
    dpa.add(new SkippyPartitioner5());
    dpa.add(new SkippyPartitioner6());
    dpa.add(new SkippyPartitioner7());
    SortList sorts = new SortList();
    RangeSorter combover = new BranchAvoidingAlternatingCombsort(1.4, 128); //As strategic janitor
    for (FixedCountPivotPartitioner dp : dpa ) {
      int pivotCount = dp.getPivotCount();
      DirtyCompoundMultiPivotSelector dcmps 
        = new DirtyCompoundMultiPivotSelector(pivotCount);
      sorts.add(new AdaptiveMultiPivotQuicksort( dcmps, dp, combover, 256));
    }
    sortTest.warmUpSorts( sorts); 
    System.out.println ( "Efficiencies of sorts" 
      + " using DirtyCompoundMultiPivotSelector"
      + " and Kangaroo multi-pivot partitioners" );
    sortTest.testSpecificSorts(sorts, 10, 10000000, 5);
    
    dpa.clear();
    sorts.clear();
    dpa.add(new SingleToMultiPartitioner(new HoyosPartitioner()));
    dpa.add(new CentrifugalPartitioner2());
    dpa.add(new CentrifugalPartitioner3());
    dpa.add(new CentrifugalPartitioner4());
    dpa.add(new CentrifugalPartitioner5());
    dpa.add(new CentrifugalPartitioner6());
    for (FixedCountPivotPartitioner dp : dpa ) {
        int pivotCount = dp.getPivotCount();
        DirtyCompoundMultiPivotSelector dcmps = new DirtyCompoundMultiPivotSelector(pivotCount);
        sorts.add(new AdaptiveMultiPivotQuicksort( dcmps, dp, combover, 256));
      }
    sortTest.warmUpSorts( sorts); 
    System.out.println ( "Efficiencies of sorts" 
      + " using DirtyCompoundMultiPivotSelector" 
      + " and Centrifugal multi-pivot partitioners" );
    sortTest.testSpecificSorts(sorts, 10, 10000000, 5);    
  }
  private MultiPivotSelector getDirtyMultiSel(int p) {
    return new DirtyCompoundMultiPivotSelector(p);
  }
  private SinglePivotSelector getDirtySel() {
    return new DirtyTheoreticalSelector(.5);
  }
  public String f5(double x) {
    return "\t" + (Math.floor(x * 100000.0 + .5) / 100000.0);
  }
  public ArrayList<FixedCountPivotPartitioner> 
    getTwoPivotPartitioners() {
    ArrayList<FixedCountPivotPartitioner> parties = new ArrayList<FixedCountPivotPartitioner>();
    parties.add(new HolierThanThouPartitioner2());
    parties.add(new CentrifugalPartitioner2());
    parties.add(new DoubleLomutoPartitioner2());
    parties.add(new YaroslavskiyPartitioner2());
    parties.add(new CentripetalPartitioner2());
    parties.add(new SkippyPartitioner2());
    parties.add(new SkippyCentripetalPartitioner2());
    parties.add(new YarrowPartitioner2());
    (new PartitionerTest()).warmUpMultiPartitioners(parties);
    return parties;
   
  }
  @Test
  public void testTheScanningTheory() {
    ArrayList<FixedCountPivotPartitioner> parties 
      = getTwoPivotPartitioners();
    int    m    = 33554432;
    int    runs = 100;
    String line = "\t";
    for (int p=0; p<parties.size(); ++p) {
      FixedCountPivotPartitioner party 
        = parties.get(p);
      line += party.toString() + "\t\t";
    }
    System.out.println(line);
    for (int r=0; r<runs; ++r) {
      line = "" +(r+1);
      int[]  vData = random.randomPermutation(m);
      for (int p=0; p<parties.size(); ++p) {
        FixedCountPivotPartitioner party 
          = parties.get(p);
        MultiPivotSelector sel 
          = new CleanFirstPSelector(party.getPivotCount());
        int[]  pivots = sel.selectPivotIndices(vData, 0, m);
        int[]  vCopy  = Arrays.copyOf(vData, m);
        long   startTime = System.nanoTime();
        int[]  partitions = party.multiPartitionRange(vCopy, 0, m, pivots);
        long   stopTime  = System.nanoTime();
        double elapsed   = (stopTime-startTime) 
               / (double)1000.0 / (double)1000.0 / (double)1000.0;
        double scans = getScanCount(party, partitions) / (double)m;
        line += f5(Math.floor(scans*1000.0)/1000.0) + f5(elapsed);
      }
      System.out.println(line);
    }
  }
  public double getScanCount
    ( FixedCountPivotPartitioner p
    , int[] partitions ) {
    int m = partitions[partitions.length-1];
    if (p instanceof SkippyPartitioner2)
      return m + partitions[1] + partitions[3];
    else if (p instanceof SkippyCentripetalPartitioner2)
      return 2 * m + partitions[3] - partitions[1];
    else if (p instanceof YarrowPartitioner2)
      return m + partitions[1];
    else if (p instanceof HolierThanThouPartitioner2)
      return m + partitions[1] + partitions[3];
    else if (p instanceof CentrifugalPartitioner2)
      return m + partitions[1];
    else if (p instanceof YaroslavskiyPartitioner2)
      return m + partitions[1];
    else if (p instanceof DoubleLomutoPartitioner2)
      return 2 * m + partitions[1] - partitions[3];
    else if (p instanceof CentripetalPartitioner2)
      return 2 * m + partitions[3] - partitions[1];
    return 1.0;
  }
  @Test
  public void testTheMispredictionTheory() {
    ArrayList<FixedCountPivotPartitioner> parties 
      = getTwoPivotPartitioners();
    int    m    = 33554432;
    int    runs = 100;
    String line = "\t";
    for (int p=0; p<parties.size(); ++p) {
      FixedCountPivotPartitioner party 
        = parties.get(p);
      line += party.toString() + "\t\t";
    }
    System.out.println(line);
    for (int r=0; r<runs; ++r) {
      line = "" +(r+1);
      int[]  vData = random.randomPermutation(m);
      for (int p=0; p<parties.size(); ++p) {
        FixedCountPivotPartitioner party 
          = parties.get(p);
        MultiPivotSelector sel 
          = new CleanFirstPSelector(party.getPivotCount());
        int[]  pivots     = sel.selectPivotIndices(vData, 0, m);
        int[]  vCopy      = Arrays.copyOf(vData, m);
        long   startTime  = System.nanoTime();
        int[]  partitions = party.multiPartitionRange(vCopy, 0, m, pivots);
        long   stopTime   = System.nanoTime();
        double elapsed    = (stopTime-startTime) 
               / (double)1000.0 / (double)1000.0 / (double)1000.0;
        double scans 
          = getMispredictionCount(party, partitions)
          / (double)m;
        line += f5(Math.floor(scans*1000.0)/1000.0) + f5(elapsed);
      }
      System.out.println(line);
    }
  }
  public double getMispredictionCount
    ( FixedCountPivotPartitioner p
    , int[] partitions) {
    double m  = partitions[partitions.length-1];
    double w1 = partitions[1]/m;
    double w2 = (partitions[3]-partitions[1])/m;
    double w3 = (partitions[5]-partitions[3])/m;
    double blue = miss(w1) + (1-w1)*miss(w3/(w2+w3));
    double green = miss(w3) + (1-w3)*miss(w1/(w1+w2));
    
    if (p instanceof SkippyPartitioner2)
      return 1.0;
    else if (p instanceof YarrowPartitioner2)
      return 1.0;
    else if (p instanceof SkippyCentripetalPartitioner2)
      return 1.0;
    else if (p instanceof HolierThanThouPartitioner2)
      return green*m;
    else if (p instanceof CentrifugalPartitioner2)
      return ( (w1+w2)*blue + w3*green )*m;
    else if (p instanceof YaroslavskiyPartitioner2)
      return ( (w1+w2)*blue + w3*green )*m;
    else if (p instanceof DoubleLomutoPartitioner2)
      return blue*m;
    else if (p instanceof CentripetalPartitioner2)
      return (blue+green)*m/2.0;
    return 1.0;
  }
  public double miss(double x) {
    return (x<0.5) ? (x) : (1-x);
  }
  @Test
  public void testArrayWritesVersusRunningTime() {
    ArrayList<FixedCountPivotPartitioner> parties 
      = getTwoPivotPartitioners();
    int    m    = 33554432;
    int    runs = 100;
    String line = "\t";
    for (int p=0; p<parties.size(); ++p) {
      FixedCountPivotPartitioner party 
        = parties.get(p);
      line += party.toString() + "\t\t";
    }
    System.out.println(line);
    for (int r=0; r<runs; ++r) {
      line = "" +(r+1);
      int[]  vData = random.randomPermutation(m);
      for (int p=0; p<parties.size(); ++p) {
        FixedCountPivotPartitioner party 
          = parties.get(p);
        MultiPivotSelector sel 
          = new CleanFirstPSelector(party.getPivotCount());
        int[]  pivots     = sel.selectPivotIndices(vData, 0, m);
        int[]  vCopy      = Arrays.copyOf(vData, m);
        long   startTime  = System.nanoTime();
        int[]  partitions = party.multiPartitionRange(vCopy, 0, m, pivots);
        long   stopTime   = System.nanoTime();
        double elapsed    = (stopTime-startTime) 
               / (double)1000.0 / (double)1000.0 / (double)1000.0;
        double scans 
          = getArrayWriteCount(party, partitions)
          / (double)m;
        line += f5(Math.floor(scans*1000.0)/1000.0) + f5(elapsed);
      }
      System.out.println(line);
    }
  }
  public double getArrayWriteCount
    ( FixedCountPivotPartitioner p
    , int[] partitions) {
    double m  = partitions[partitions.length-1];
    double w1 = partitions[1]/m;
    double w2 = (partitions[3]-partitions[1])/m;
    double w3 = (partitions[5]-partitions[3])/m;
    //double blue = miss(w1) + (1-w1)*miss(w3/(w2+w3));
    //double green = miss(w3) + (1-w3)*miss(w1/(w1+w2));
    
    if (p instanceof SkippyPartitioner2)
      return 2.0*m;
    else if (p instanceof YarrowPartitioner2)
      return 3.0*m;
    else if (p instanceof SkippyCentripetalPartitioner2)
      return 2.0*m;
    else if (p instanceof HolierThanThouPartitioner2)
      return (w1*3 + w2*2)*m;
    else if (p instanceof CentrifugalPartitioner2)
      return (1.0 - w2 - w3*w3)*2.0*m;
    else if (p instanceof YaroslavskiyPartitioner2)
      return (1.0 - w2 - w3*w3)*2.0*m; 
      //Items in middle partition aren't exchanged;
      //Items in right partition found scanning from right,
      //aren't exchanged.  All others are.
    else if (p instanceof DoubleLomutoPartitioner2)
      return 2.0*(w1+w3)*m;
    else if (p instanceof CentripetalPartitioner2)
      return ((w3+w1)*1.5 + w2*2) * m;
      //This formula's certainly wrong; I haven't thought it
      //through.
    return 1.0;
  }
}

 
