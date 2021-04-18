package jsortie.testing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import jsortie.ClaytonsSort;
import jsortie.RangeSorter;
import jsortie.earlyexitdetector.RangeSortEarlyExitDetector;
import jsortie.earlyexitdetector.TwoWayInsertionEarlyExitDetector;
import jsortie.earlyexitdetector.WainwrightEarlyExitDetector;
import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.helper.RangeSortHelper;
import jsortie.janitors.exchanging.BranchAvoidingAlternatingCombsort;
import jsortie.janitors.insertion.BinaryInsertionSort;
import jsortie.janitors.insertion.InsertionSort;
import jsortie.janitors.insertion.PairInsertionSort;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.mergesort.asymmetric.AsymmetricMergesort;
import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.PositionalSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.bidirectional.CentripetalExpander;
import jsortie.quicksort.expander.branchavoiding.BalancedSkippyExpander;
import jsortie.quicksort.external.BranchAvoidingExternalPartitioner;
import jsortie.quicksort.external.DefaultExternalPartitioner;
import jsortie.quicksort.external.ExternalSinglePivotPartitioner;
import jsortie.quicksort.governor.QuicksortGovernor;
import jsortie.quicksort.governor.adaptive.QuicksortAdaptive;
import jsortie.quicksort.governor.adaptive.QuicksortAdaptive2;
import jsortie.quicksort.indexselector.IndexSelector;
import jsortie.quicksort.indexselector.PositionalIndexSelector;
import jsortie.quicksort.inplace.FixedPartitionQuicksort;
import jsortie.quicksort.inplace.InPlaceQuicksort;
import jsortie.quicksort.inplace.SamplingInPlaceQuicksort;
import jsortie.quicksort.multiway.external.ExternalMultiPivotQuicksort;
import jsortie.quicksort.multiway.governor.MultiPivotQuicksort;
import jsortie.quicksort.multiway.partitioner.FixedCountPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.adapter.SingleToMultiPartitioner;
import jsortie.quicksort.multiway.partitioner.centripetal.CentripetalPartitioner2;
import jsortie.quicksort.multiway.partitioner.centripetal.CentripetalPartitioner3;
import jsortie.quicksort.multiway.partitioner.geometric.GeometricPartitioner2;
import jsortie.quicksort.multiway.partitioner.geometric.GeometricPartitioner3;
import jsortie.quicksort.multiway.partitioner.geometric.GeometricPartitioner4;
import jsortie.quicksort.multiway.partitioner.geometric.GeometricPartitioner5;
import jsortie.quicksort.multiway.partitioner.geometric.GeometricPartitioner6;
import jsortie.quicksort.multiway.partitioner.geometric.GeometricPartitioner7;
import jsortie.quicksort.multiway.partitioner.geometric.GeometricPartitionerAny;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner2;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner3;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner4;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner5;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner6;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner7;
import jsortie.quicksort.multiway.partitioner.permuting.CountAndPermutePartitioner;
import jsortie.quicksort.multiway.partitioner.permuting.TrackAndCopyPartitioner;
import jsortie.quicksort.multiway.partitioner.permuting.TrackAndPermutePartitioner;
import jsortie.quicksort.multiway.partitioner.permuting.TrackAndPermutePartitionerWithBellsOn;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.multiway.selector.clean.CleanMultiPivotPositionalSelector;
import jsortie.quicksort.multiway.selector.clean.SamplingMultiPivotSelector;
import jsortie.quicksort.multiway.selector.dirty.DirtyCompoundMultiPivotSelector;
import jsortie.quicksort.multiway.selector.dirty.DirtyMultiPivotPositionalSelector;
import jsortie.quicksort.partitioner.bidirectional.centrepivot.CentrePivotPartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.HoarePartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.HoyosPartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.RevisedHoarePartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.RevisedHoyosMirrorPartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.SingletonPartitioner;
import jsortie.quicksort.partitioner.PartitionerSet;
import jsortie.quicksort.partitioner.bizarre.PlacementPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.BalancedSkippyPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.FlowerArrangementPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyCentripetalPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyMirrorPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.FatalisticFloristPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.PDQPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.TunedPartitioner;
import jsortie.quicksort.partitioner.decorator.TelescopingPartitioner;
import jsortie.quicksort.partitioner.insideout.CentripetalPartitioner;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.interfaces.StandAlonePartitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.IntroSelect489Partitioner;
import jsortie.quicksort.partitioner.kthstatistic.floydrivest.FloydRivestPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.floydrivest.SimplifiedFloydRivestPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.heap.KislitsynPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.mom.MedianOfMediansPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.quickselect.QuickSelectPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.remedian.RemedianPartitioner;
import jsortie.quicksort.partitioner.lomuto.LomutoMirrorPartitioner;
import jsortie.quicksort.partitioner.lomuto.LomutoPartitioner;
import jsortie.quicksort.partitioner.lomuto.RevisedLomutoPartitioner;
import jsortie.quicksort.partitioner.standalone.StandaloneSingletonPartitioner;
import jsortie.quicksort.partitioner.standalone.VanEmdenPartitioner;
import jsortie.quicksort.partitioner.standalone.VanEmdenPartitionerRevised;
import jsortie.quicksort.partitioner.unidirectional.HolierThanThouMirrorPartitioner;
import jsortie.quicksort.partitioner.unidirectional.HolierThanThouPartitioner;
import jsortie.quicksort.partitioner.unidirectional.SmallMercyPartitioner;
import jsortie.quicksort.protector.CheckedRangeSorter;
import jsortie.quicksort.protector.CheckedSinglePivotPartitioner;
import jsortie.quicksort.protector.CheckedSinglePivotSelector;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.clean.CleanExternalSelector;
import jsortie.quicksort.selector.clean.CleanLeftHandedSelector;
import jsortie.quicksort.selector.clean.CleanPositionalSampleSelector;
import jsortie.quicksort.selector.clean.CleanSinglePivotSelector;
import jsortie.quicksort.selector.dirty.DirtySingletonSelector;
import jsortie.quicksort.selector.simple.MiddleElementSelector;
import jsortie.quicksort.selector.simple.RandomElementSelector;
import jsortie.quicksort.theoretical.Gain;

public class PartitionerTest {
  int maxCount = 1000000;
  int runCount = 1000;
  protected RandomInput     random   = new RandomInput();
  protected SortTest        sortTest = new SortTest();
  public double bitsOfChaos(double n) {
    if (n<2) return 0;
    if (n==2) return 1.0;
    return n * (Math.log(n) - 1.0) / Math.log(2.0);
  }
  public void testBitsOfChaos() {
    System.out.println(1000*Math.log(1000)/Math.log(2.0));
    System.out.println(bitsOfChaos(1000));
    System.out.println(bitsOfChaos(500));
    System.out.println(bitsOfChaos(1000) - bitsOfChaos(500) - bitsOfChaos(500));
  }
  @Test	
  public void testPartitionersHeadToHead() {
    testPartitionersHeadToHead(8192,       1000, true );
    testPartitionersHeadToHead(8192,       1000, false);
    testPartitionersHeadToHead(1048576,    100,   false);
    testPartitionersHeadToHead(32*1048576, 25,   false);
  }
  public <T> String ToTabbedColumns
    (String sFirstColumn, Collection<T> list) {
    String s = sFirstColumn;
    for (T t : list) {
      s += "\t" + t.toString();
    }
    return s;
  }
  @Test
  public void testR() {
    System.out.println((11.0 + 2.0 * Math.sqrt(30))/5.0);
    System.out.println(Math.sqrt((11.0 + 2.0 * Math.sqrt(30))/5.0));
  }
  public void testPartitionersOnly() {
    NamedArrayList<SinglePivotPartitioner> partitioners
      = getReallyGoodPartitioners(); 
    warmUpPartitioners(partitioners.getArrayList());
    for ( SinglePivotSelector selector : new SinglePivotSelector[] 
            { new CleanPositionalSampleSelector(5)
            , new CleanPositionalSampleSelector(1,17)  } ) {
      System.out.println("Testing with selector " + selector.toString());
      System.out.println(ToTabbedColumns("n", partitioners.getNameList()));
      for ( int n=20; n<2000; n=n*11/10 ) {
        runCount = (n<2000) ? 10000: 25; 
        double gain = 0;
        double nanosElapsed[] = new double[partitioners.size()];
        int data[] = new int[n];			
        for ( int run=0; run < runCount; ++run) {
          int split=0;
          int input[]    = random.randomPermutation(n);
          int pivotIndex = selector.selectPivotIndex(input, 0, n);
          int p=0;
          for ( SinglePivotPartitioner partitioner : partitioners) {
            for (int i=0; i<n; ++i) {
              data[i] = input[i];
            }
            nanosElapsed[p] -= System.nanoTime();
            split = partitioner.partitionRange
                    (data, 0, data.length, pivotIndex);
            nanosElapsed[p] += System.nanoTime();
            ++p;
          }
          double bitsLeft  = (0<split) 
                           ? Math.log(split) * (double)split / Math.log(2)
                           : 0;
          double bitsRight = (split<data.length-1) 
                           ? Math.log(data.length-split-1) * (double)(data.length-split-1) / Math.log(2)
                           : 0;
          gain += Math.log(n) * (double)n / Math.log(2) - bitsLeft - bitsRight;
        }				
        String line = "" + n;
        for (int p = 0 ; p < nanosElapsed.length; ++p) {
          double eff = gain / nanosElapsed[p];
          line += "\t" + Math.floor(eff * 1000.0 + .5) / 1000.0;
        }
        System.out.println(line);
      }
    }
  }
  @Test
  public void testPartitionersInSort() {
    testSpecificPartitionersInSort ( getOrdinaryPartitioners() );
    testSpecificPartitionersInSort ( getBranchAvoidingPartitioners() );
  }
  public void testSpecificPartitionersInSort
    ( NamedArrayList<SinglePivotPartitioner> partitioners ) {
    SinglePivotSelector selector = new MiddleElementSelector();
    RangeSorter janitor = new ClaytonsSort();
    SortList sorters = new SortList();
    int n = 1000000;
    System.out.println ( "Average time in milliseconds,"
                       + " spent partitioning (but not fully sorting" 
                       + n + " elements"); 
    String header = "n";
    for ( SinglePivotPartitioner partitioner : partitioners) {
      RangeSorter sorter = new QuicksortGovernor(selector, partitioner, janitor, 200);
      sortTest.warmUpSort(sorter, 1000);
      sorters.add(sorter);
      header += "\t" + partitioner.toString();
    }
    System.out.println(header);
    testSorters(sorters, n);
  }
  @Test
  public void testRevisedHoarePartitioner() {
	RevisedHoarePartitioner       rhp  = new RevisedHoarePartitioner();
    //TracedSinglePivotPartitioner  tp   = new TracedSinglePivotPartitioner(rhp);
    CheckedSinglePivotPartitioner cspp = new CheckedSinglePivotPartitioner(rhp);
    int n = 50;
    for (int r = 0; r<n; ++r) {
      int[] crap = random.randomPermutation(n);
      cspp.partitionRange(crap, 0, crap.length, r);
    }
  }
  @Test
  public void testRevisedHoarePartitioner2() {
   HoarePartitioner        hp    = new HoarePartitioner();
    RevisedHoarePartitioner rhp   = new RevisedHoarePartitioner();
    HoyosPartitioner        hoyos = new HoyosPartitioner();
    SkippyPartitioner     kp    = new SkippyPartitioner();
    NamedArrayList<SinglePivotPartitioner> partitioners 
      = new NamedArrayList<SinglePivotPartitioner> ();
    partitioners.add("Hoare", hp);
    partitioners.add("RevisedHoare", rhp);
    partitioners.add("Hoyos", hoyos);
    partitioners.add("Skippy", kp);
    testSpecificPartitionersInSort(partitioners);
  }
  public void testSorters(SortList sorters, int n) {		
    int data[] = new int[n];			
    double nanosElapsed[] = new double[sorters.size()];
    runCount = 100;
    for ( int run=0; run < runCount; ++run) {
      int input[] = random.randomPermutation(n);
      int s=0;				
      for ( RangeSorter sorter: sorters) {
        for (int i=0; i<n; ++i) {
          data[i] = input[i];
        }
        nanosElapsed[s] -= System.nanoTime();
        sorter.sortRange(data, 0, data.length);
        nanosElapsed[s] += System.nanoTime();
        ++s;
      }
    }
    //Here's the catch; we don't actually know what the *gain* was.
    //So guessing at efficiency is difficult.  But we can output how long
    //the sorts took
    String line = "" + n;
    for (int p = 0 ; p < nanosElapsed.length; ++p) {
      double milliseconds = nanosElapsed[p]/(double)runCount/1000.0/1000.0;
      line += "\t" + Math.floor(milliseconds * 1000.0 + .5) / 1000.0;
    }
    System.out.println(line);
  }
  public void testPartitionersHeadToHead(int m, int runs, boolean silent) {
    //Penguin: test may need to be re-run, now that theoretical selectors
    //have been rebased on ZD489 and paired kangaroo partitioners. 
    NamedArrayList<SinglePivotSelector>    selectors    = (new SelectorTest()).getSelectors();	
    NamedArrayList<SinglePivotPartitioner> partitioners = getLotsOfPartitioners();
    ExternalSinglePivotPartitioner[] externals 
      = new ExternalSinglePivotPartitioner[] 
        { new DefaultExternalPartitioner(), new BranchAvoidingExternalPartitioner() } ;
    int maxLen=0;
    for (SinglePivotPartitioner p : partitioners ) {
      int len = p.toString().length();
      if ( maxLen < len ) {
        maxLen = len;
      }
    }
    for (ExternalSinglePivotPartitioner x : externals ) {
      int len = x.toString().length();
      if ( maxLen < len ) {
        maxLen = len;
      }
    }
    testSelectorsAndPartitioners(selectors, partitioners, maxLen, m, runs, silent);
    for ( ExternalSinglePivotPartitioner ep : externals ) {
      int sCount = selectors.size();
      double timing[] = new double[sCount];
      double gain[]   = new double[sCount];
      for (int r=0; r<runs; ++r) {
        int input[] = random.randomPermutation(m);
        for (int s=0; s<sCount; ++s) {
          SinglePivotSelector selector = selectors.get(s);
          int copy[] = Arrays.copyOf(input, input.length);
          int aux[]   = Arrays.copyOf(input, input.length);
          int pivotIndex = selector.selectPivotIndex(aux, 0, aux.length);
          long startTime = System.nanoTime();
          pivotIndex = ep.partitionAuxiliaryRange(copy, 0, aux, 0, aux.length, pivotIndex);
          long stopTime = System.nanoTime();
          timing[s] += ((double)stopTime - (double)startTime ); //nanoseconds
          gain[s]   += bitsOfChaos(m) - bitsOfChaos(pivotIndex) - bitsOfChaos(m-pivotIndex-1);
        }
      }
      String line = padRight(ep.toString(),maxLen);
      for (int s=0; s<sCount; ++s) {
        double gbps = gain[s] / timing[s];
        gbps = Math.floor( gbps* 1000.0) / 1000.0;
        line += "\t" + gbps; 
      }
      if (!silent) {
        System.out.println(line);
      }
    }
    if (!silent) {
      System.out.println("");   	
    }
  }
  public void testSelectorsAndPartitioners
    ( NamedArrayList<SinglePivotSelector> selectors
    , NamedArrayList<SinglePivotPartitioner> partitioners
    , int padNameLength
    , int m, int runs, boolean silent) {		
    int pCount = partitioners.size();
    int sCount = selectors.size();		
    double timing[][] = new double[sCount][pCount];
    double gain[][] = new double[sCount][pCount];
    for (int r=0; r<runs; ++r) {
      int input[] = random.randomPermutation(m);
      for (int s=0; s<sCount; ++s) {
        SinglePivotSelector selector = selectors.get(s);
        for (int p=0; p<pCount; ++p) {
          SinglePivotPartitioner partitioner = partitioners.get(p);
          int copy[] = Arrays.copyOf(input, input.length);
          long startTime = System.nanoTime();
          int pivotIndex = selector.selectPivotIndex(copy, 0, copy.length);
          pivotIndex = partitioner.partitionRange(copy, 0, copy.length, pivotIndex);
          long stopTime = System.nanoTime();
          timing[s][p] += ((double)stopTime - (double)startTime ); //nanoseconds
          gain[s][p]   += bitsOfChaos(m) - bitsOfChaos(pivotIndex) - bitsOfChaos(m-pivotIndex-1);		
        }
      }
    }
    if (!silent) {
      System.out.println("Testing partitioner/selector combinations for m=" + m);
      String header = padRight("", padNameLength);
      for (int s=0;s<sCount; ++s) {
        header += "\t" + selectors.get(s).toString();
      }
      System.out.println(header);
      for (int p=0; p<pCount; ++p) {
        String line = padRight(partitioners.get(p).toString(),padNameLength);
        for (int s=0; s<sCount; ++s) {
          double gbps = gain[s][p] / timing[s][p];
          gbps = Math.floor( gbps* 1000.0) / 1000.0;
          line += "\t" + gbps; 
        }
        System.out.println(line);;
      }
    }
  }
  private String padRight(String string, int padNameLength) {
    while (string.length() < padNameLength) {
      string +=" ";
    }
    return string;
  }
  public NamedArrayList<SinglePivotPartitioner> getStandardPartitioners() {
    NamedArrayList<SinglePivotPartitioner> partitioners 
      = new NamedArrayList<SinglePivotPartitioner>();
    //left-to-right
    partitioners.add("Lomuto",         new LomutoPartitioner());
    partitioners.add("HolierThanThou", new HolierThanThouPartitioner());
    partitioners.add("Tuned",          new TunedPartitioner());
    partitioners.add("Skippy",         new SkippyPartitioner());
    //outside-in
    partitioners.add("Singleton",      new SingletonPartitioner());
    partitioners.add("Flower",         new FlowerArrangementPartitioner());
    partitioners.add("Fatal-Flower",   new FatalisticFloristPartitioner());
    partitioners.add("PDQ",            new PDQPartitioner());
    //inside-out
    partitioners.add("Centripetal",    new CentripetalPartitioner());
    partitioners.add("Skippy-C",       new SkippyCentripetalPartitioner());
    return partitioners;
  }
  public NamedArrayList<SinglePivotPartitioner> getOrdinaryPartitioners() {
    NamedArrayList<SinglePivotPartitioner> partitioners 
      = new NamedArrayList<SinglePivotPartitioner>();
    partitioners.add("Lomuto",         new LomutoPartitioner());
    partitioners.add("Singleton",      new SingletonPartitioner());
    partitioners.add("Hoyos",          new HoyosPartitioner());
    partitioners.add("HolierThanThou", new HolierThanThouPartitioner());
    partitioners.add("Centripetal",    new CentripetalPartitioner());
    partitioners.add("PDQ",            new PDQPartitioner());
    partitioners.add("Tuned",          new TunedPartitioner());
    return partitioners;		
  }
  public NamedArrayList<SinglePivotPartitioner> getBoringPartitioners() {
    NamedArrayList<SinglePivotPartitioner> partitioners 
      = new NamedArrayList<SinglePivotPartitioner>();
    partitioners.add("Hoare",         new HoarePartitioner());              //like LomutoPartitioner
    partitioners.add("R-Hoare",       new RevisedHoarePartitioner());       //like LomutoPartitioner
    partitioners.add("T-Singleton",   new TelescopingPartitioner());   //like SingletonPartitioner
    partitioners.add("R-Lomuto",      new RevisedLomutoPartitioner());	   //like SingletonPartitioner	
    partitioners.add("CentrePivot",   new CentrePivotPartitioner());       //like HoyosPartitioner
    partitioners.add("R-HoyosMirror", new RevisedHoyosMirrorPartitioner()); //like HoyosPartitioner
    return partitioners;		
  }
  public NamedArrayList<SinglePivotPartitioner> getBranchAvoidingPartitioners() {
    NamedArrayList<SinglePivotPartitioner> partitioners 
      = new NamedArrayList<SinglePivotPartitioner>();
    partitioners.add("Tuned",           new TunedPartitioner());
    partitioners.add("PDQ",             new PDQPartitioner());
    partitioners.add("Skippy",          new SkippyPartitioner());
    partitioners.add("Balanced Skippy", new BalancedSkippyPartitioner());
    partitioners.add("Skippy-C",        new SkippyCentripetalPartitioner());
    partitioners.add("Flower",          new FlowerArrangementPartitioner());
    partitioners.add("Fatal-Flower",    new FatalisticFloristPartitioner());
    return partitioners;
  }
  public NamedArrayList<SinglePivotPartitioner> getReallyGoodPartitioners() {
    NamedArrayList<SinglePivotPartitioner> partitioners
      = new NamedArrayList<SinglePivotPartitioner>();
    partitioners.add("Skippy",       new SkippyPartitioner());
    partitioners.add("Skippy-C",     new SkippyCentripetalPartitioner());
    partitioners.add("Flower",       new FlowerArrangementPartitioner());
    partitioners.add("Fatal-Flower", new FatalisticFloristPartitioner());
    return partitioners;
  }
  public NamedArrayList<SinglePivotPartitioner> getGoodPartitioners()  {
    NamedArrayList<SinglePivotPartitioner> partitioners 
      = new NamedArrayList<SinglePivotPartitioner>();
    partitioners.add("Singleton",   new SingletonPartitioner());
    partitioners.add("Hoyos",       new HoyosPartitioner());
    partitioners.add("Centripetal", new CentripetalPartitioner());
    partitioners.add("Tuned",       new TunedPartitioner());
    partitioners.add("Skippy",      new SkippyPartitioner());
    partitioners.add("Skippy-C",    new SkippyCentripetalPartitioner());		
    return partitioners;		
  }
  public NamedArrayList<SinglePivotPartitioner> getLotsOfPartitioners() {
    NamedArrayList<SinglePivotPartitioner> partitioners 
      = new NamedArrayList<SinglePivotPartitioner>();
    partitioners.add("Hoare",          new HoarePartitioner()); //like LomutoPartitioner
    partitioners.add("Singleton",      new SingletonPartitioner());
    partitioners.add("Lomuto",         new LomutoPartitioner());
    partitioners.add("R-Hoare",        new RevisedHoarePartitioner());  //like LomutoPartitioner
    partitioners.add("R-Lomuto",       new RevisedLomutoPartitioner()); //like SingletonPartitioner	
    partitioners.add("LomutoMirror",   new LomutoMirrorPartitioner());       
    partitioners.add("Hoyos",          new HoyosPartitioner());
    partitioners.add("CentrePivot",    new CentrePivotPartitioner());
    partitioners.add("R-HoyosMirror",  new RevisedHoyosMirrorPartitioner()); //like HoyosPartitioner
    partitioners.add("HolierThanThou", new HolierThanThouPartitioner());
    partitioners.add("SmallMercy",     new SmallMercyPartitioner());
    partitioners.add("HTT-Mirror",     new HolierThanThouMirrorPartitioner());
    partitioners.add("Centripetal",    new CentripetalPartitioner());
    partitioners.add("PDQ",            new PDQPartitioner());
    partitioners.add("Tuned",          new TunedPartitioner());
    partitioners.add("Skippy",         new SkippyPartitioner());
    partitioners.add("SkippyMirror",   new SkippyMirrorPartitioner());
    partitioners.add("Skippy-C",       new SkippyCentripetalPartitioner());
    partitioners.add("Flower",         new FlowerArrangementPartitioner());
    partitioners.add("Fatal-Flower",   new FatalisticFloristPartitioner());
    return partitioners;		
  }
  public void runSpecificPartitionerOnce
      ( FixedCountPivotPartitioner party
      , boolean naughty ) {
    runSpecificPartitionerOnce
      ( party, party.getPivotCount(), naughty );
  }
  public void runSpecificPartitionerOnce
    ( SinglePivotPartitioner party
    , int pivotCount, boolean naughty) {
    runSpecificPartitionerOnce
      ( new SingleToMultiPartitioner(party)
      , pivotCount, naughty);
  }
  private void runSpecificPartitionerOnce
    ( StandAlonePartitioner party
    , int pivotCount, boolean naughty ) {
    int n = 50;
    int input[] = random.randomPermutation(n);
    int partitions[] = party.multiPartitionRange(input, 0, n);
    checkPartitions(input, partitions, pivotCount, naughty);
  }
  private void checkPartitions
    ( int[] input, int[] partitions
    , int pivotCount, boolean naughty) {
    int pivots[] = new int[pivotCount];
    if (naughty) {
      String pivotsUsedLine     = "output pivots...";
      String pivotLocationsLine = "output locations";
      for (int p=0; p<pivotCount && p*2+1<partitions.length;++p) {
        if (partitions[p*2+1]<input.length) {
          pivots[p] = input[partitions[p*2+1]];
          pivotsUsedLine     += "\t" + pivots[p];
        } else {
          pivotsUsedLine     += "\tError";			
        }
        pivotLocationsLine += "\t" + partitions[p*2+1];
      }
      System.out.println(pivotsUsedLine);
      System.out.println(pivotLocationsLine);
    }
    InsertionSort is = new InsertionSort();
    for (int i=0; i<partitions.length; i+=2) {
      int start = partitions[i];
      int stop  = partitions[i+1];
      String line = "[" + start + ".." + (stop-1) + "] =";
      for (int j=start; j<stop; ++j) {
        line += " " + input[j];
      }
      System.out.println(line);
      if (start+1<stop) {
        line = "          ";
        is.sortRange( input, start, stop);
        for (int j=start; j<stop; ++j) {
          line += " " + input[j];
        }
        System.out.println(line);
      }
      if ( stop < input.length) {
        System.out.println 
          ( "[" + stop + "] was " + input[stop]
          + " versus pivot " + pivots[i/2]);
      }
    }
    boolean correct = true;
    for ( int scan=1
        ; scan<input.length && correct
        ; ++scan) {
      if (input[scan]!=scan) {
        correct=false;
      }
    }
    if (!correct) {
      System.out.println("partitioner failed");
    }
  }
  private void runSpecificPartitionerOnce
    ( MultiPivotPartitioner party
    , int pivotCount, boolean naughty ) {
    int n = 50;
    int input[] = random.randomPermutation(n);
    MultiPivotSelector sel 
      = new CleanMultiPivotPositionalSelector(pivotCount);
    int pivotIndices[] 
      = sel.selectPivotIndices(input, 0, n);
    int pivots[] = new int[pivotIndices.length];
    String pivotsUsedLine     = "input pivots...";
    String pivotLocationsLine = "input locations";
    for (int p=0; p<pivotIndices.length; ++p) {
      pivots[p] = input[pivotIndices[p]];
      pivotsUsedLine += "\t" + pivots[p];
      pivotLocationsLine += "\t" + pivotIndices[p];			
    }
    System.out.println(pivotsUsedLine);
    System.out.println(pivotLocationsLine);
    int partitions[] = party.multiPartitionRange(input, 0, n, pivotIndices);
    checkPartitions(input, partitions, pivotCount, naughty);
  }
  public void runSpecificPartitionerOnce(SinglePivotPartitioner party) {
    runSpecificPartitionerOnce(new SingleToMultiPartitioner(party), true);
  }
  public void testPartitionerInAnger() {
    QuicksortGovernor x = new QuicksortGovernor
       ( new CheckedSinglePivotSelector ( new RandomElementSelector() )
       , new CheckedSinglePivotPartitioner( new SingletonPartitioner() )
       , new CheckedRangeSorter( new InsertionSort() ), 9);
    int n = 50;
    int input[] = random.randomPermutation(n);
    x.sortRange(input, 0, input.length);
  }
  public void testCentripetal3Once() {
    runSpecificPartitionerOnce( new CentripetalPartitioner3(), false );
  }
  public void testCentripetal2Once() {
    runSpecificPartitionerOnce( new CentripetalPartitioner2(), false );
  }
  public void testKangarooOnce() {
    runSpecificPartitionerOnce( new SkippyPartitioner());   
  }
  public void testSingletonOnce() {
    runSpecificPartitionerOnce( new SingletonPartitioner());   
  }
  @Test
  public void testQuickSelectVariantsOnRandomInput() {
    ArrayList<SinglePivotPartitioner> partitioners
      = new ArrayList<SinglePivotPartitioner>();
    SortList sorters = new SortList();
    RangeSorter            hsort   = new TwoAtATimeHeapsort();
    partitioners.add (new SingletonPartitioner());
    partitioners.add (new LomutoPartitioner());
    partitioners.add (new HoyosPartitioner());
    partitioners.add (new HolierThanThouPartitioner());
    partitioners.add (new CentripetalPartitioner());
    partitioners.add (new TunedPartitioner());
    partitioners.add (new SkippyCentripetalPartitioner());
    partitioners.add (new SkippyPartitioner());    
    for (SinglePivotPartitioner p: partitioners) { 
      CleanSinglePivotSelector sps = new MiddleElementSelector();
      sorters.add ( new QuicksortGovernor(sps, p, hsort, 16));			
    }
    System.out.println("");
    sortTest.dumpHeaderLine("n", partitioners);
    sortTest.warmUpSorts(sorters);
    sortTest.testSpecificSorts(sorters, 94, 12000, 1600, 10000000, 100);
  }
  public void testQuickSelectVariantsOnDegenerateInput() {
    DegenerateInputTest dit = DegenerateInputTest.newTest(10000, 25);
    ArrayList<SinglePivotPartitioner> partitioners
      = new ArrayList<SinglePivotPartitioner>();
    SortList             sorters = new SortList();
    RangeSorter          isort   = new InsertionSort2Way();
    IndexSelector        sqrt    = new PositionalIndexSelector();
    SinglePivotSelector  middle  = new MiddleElementSelector();
    KislitsynPartitioner kissy   = new KislitsynPartitioner();
    System.out.println("");		
    partitioners.add (new CentripetalPartitioner());
    partitioners.add (new SingletonPartitioner());
    partitioners.add (new LomutoPartitioner());
    partitioners.add (new HoyosPartitioner());
    String line = "";
    for (SinglePivotPartitioner p: partitioners) { 
      line+= "\t" + p.toString();
      KthStatisticPartitioner  ksp 
        = new QuickSelectPartitioner(middle, p, p, 5, kissy);
      CleanSinglePivotSelector sps 
        = new CleanExternalSelector(sqrt, ksp);
      sorters.add ( new QuicksortGovernor(sps, p, isort, 64));			
    }
    System.out.println(line);		
    sortTest.warmUpSorts(sorters);
    dit.runSortsOnSortedData(sorters, "millisecond");
    dit.runSortsOnDuplicatedData(sorters, "millisecond");					
  }		
  public void testQuickSelectVariantsOnDegenerateInput2() {
    DegenerateInputTest dit = DegenerateInputTest.newTest(10000, 25);
    SortList   sorters = new SortList();
    RangeSorter          isort   = new InsertionSort2Way();
    RangeSorter          hsort   = new TwoAtATimeHeapsort();
    IndexSelector        sqrt    = new PositionalIndexSelector();
    SinglePivotSelector  middle  = new MiddleElementSelector();
    KislitsynPartitioner kissy   = new KislitsynPartitioner(); 
    System.out.println("");		
    ArrayList<SinglePivotPartitioner> partitioners
      = new ArrayList<SinglePivotPartitioner>();
    partitioners.add ( new CentripetalPartitioner() );
    partitioners.add ( new SingletonPartitioner()   );
    partitioners.add ( new LomutoPartitioner()      );
    partitioners.add ( new HoyosPartitioner()       );
    String line = "";
    for (SinglePivotPartitioner p: partitioners) { 
      line+= "\t" + p.toString(); 
      KthStatisticPartitioner  ksp 
        = new QuickSelectPartitioner(middle, p, p, 5, kissy);
      CleanSinglePivotSelector sps 
        = new CleanExternalSelector(sqrt, ksp);
      sorters.add ( new QuicksortAdaptive2(sps, p, isort, 64, hsort ));			
    }
    System.out.println(line);
    sortTest.warmUpSorts(sorters);
    dit.runSortsOnSortedData(sorters, "microsecond");
    dit.runSortsOnDuplicatedData(sorters, "microsecond");
  }
  public void testPartitionersOnDegenerateInput() {
    DegenerateInputTest dit = DegenerateInputTest.newTest(10000, 25);
    SortList   sorters = new SortList();
    ArrayList<SinglePivotPartitioner> partitioners = new ArrayList<SinglePivotPartitioner>();
    partitioners.add ( new CentripetalPartitioner() );
    partitioners.add ( new SingletonPartitioner()   );
    partitioners.add ( new LomutoPartitioner()      );
    partitioners.add ( new HoyosPartitioner()       );
    sorters.clear();
    CleanSinglePivotSelector middle  = new MiddleElementSelector();
    RangeSorter              isort   = new InsertionSort2Way();
    String line = "";
    for (SinglePivotPartitioner p: partitioners) { 
      line+= "\t" + p.toString(); 
      sorters.add ( new QuicksortGovernor(middle, p, isort, 64));			
    }
    System.out.println(line);		
    sortTest.warmUpSorts(sorters);
    dit.runSortsOnSortedData(sorters, "millisecond");
    dit.runSortsOnDuplicatedData(sorters, "millisecond");		
  }		
  public void testVanEmdenOnce() {
    runSpecificPartitionerOnce( new VanEmdenPartitionerRevised(), 1, true);
  }
  public void testVanEmdenPivotDistributions() {
    ArrayList<StandAlonePartitioner> partitioners = new ArrayList<StandAlonePartitioner>();
    SampleSizer sqr = new SquareRootSampleSizer();
    SampleCollector pos = new PositionalSampleCollector();
    
    partitioners.add( new VanEmdenPartitioner());
    partitioners.add( new VanEmdenPartitionerRevised());
    partitioners.add( new VanEmdenPartitionerRevised(sqr, pos));
    ArrayList<int[]> byParty = new ArrayList<int[]>();
    int n = 1001;
    int grain = 10;
    int runs = 1000000;
    String header = "rank";
    for (StandAlonePartitioner party : partitioners) {
      header += "\t" + party.toString();
      int[] counts = new int[n/grain];
      for (int run=0; run<runs; ++run) {
        int[] input = random.randomPermutation(n);     
        int pivotIndex = party.multiPartitionRange(input, 0, n)[1];
        ++counts[pivotIndex/grain];
      }
      byParty.add(counts);
    }
    System.out.println(header);
    for (int i=0; i<n/grain; ++i) {
      String line = "" + i*grain;
      for (int[] counts : byParty) {
        line += "\t" + counts[i];
      }
      System.out.println(line);
    }    
  }
  public void testVanEmdenSort() {
    DegenerateInputTest dit = DegenerateInputTest.newTest(65536, 5);
    SortList   sorters = new SortList();
    RangeSorter              isort   = new InsertionSort2Way();
    sorters.add( new MultiPivotQuicksort(new StandaloneSingletonPartitioner(), isort, 64) );
    sorters.add( new MultiPivotQuicksort(new VanEmdenPartitioner(),            isort, 64) );
    sorters.add( new MultiPivotQuicksort(new VanEmdenPartitionerRevised(),     isort, 64) );
    sortTest.warmUpSorts(sorters);
    dit.runSortsOnSortedData(sorters, "microsecond");
    dit.runSortsOnDuplicatedData(sorters, "microsecond");		
  }
  public void testGeometricPartitionerAnyOnce() {
	  runSpecificPartitionerOnce( (SinglePivotPartitioner) ( new GeometricPartitionerAny() ), 13, true);
  }
  public MultiPivotSelector any(int c) {
    return new DirtyMultiPivotPositionalSelector(c);
  }
  @Test
  public void testGeometricPartitionerAny() {
    SortList    sorters = new SortList();
    RangeSorter isort   = new InsertionSort2Way();
    RangeSorter q2 = new MultiPivotQuicksort(any(3),   new GeometricPartitioner2(), isort, 64);
    RangeSorter q3 = new MultiPivotQuicksort(any(7),   new GeometricPartitioner3(), q2,    64);
    RangeSorter q4 = new MultiPivotQuicksort(any(15),  new GeometricPartitioner4(), q3,   256);
    RangeSorter q5 = new MultiPivotQuicksort(any(31),  new GeometricPartitioner5(), q4,  1024);
    RangeSorter q6 = new MultiPivotQuicksort(any(63),  new GeometricPartitioner6(), q5,  4096);
    RangeSorter q7 = new MultiPivotQuicksort(any(127), new GeometricPartitioner7(), q6, 16384);
    RangeSorter qAny = new MultiPivotQuicksort(new GeometricPartitionerAny(), isort, 64);	
    sorters.add( q2   );
    sorters.add( q3   );
    sorters.add( q4   );
    sorters.add( q5   );
    sorters.add( q6   );
    sorters.add( q7   );
    sorters.add( qAny );
    sortTest.warmUpSorts(sorters);
    sortTest.testSpecificSorts(sorters, 10, 10000, 400, 1000000, 25);
  }
  @Test
  public void testInPlaceQuicksort() {
    CleanSinglePivotSelector   skewed    = new CleanLeftHandedSelector(true);
    SinglePivotSelector        middle    = new MiddleElementSelector();
    SinglePivotSelector        bestOf3   = new DirtySingletonSelector();
    SinglePivotPartitioner     centipede = new CentripetalPartitioner();
    RangeSorter                isort     = new InsertionSort2Way();
    RangeSorter                hsort     = new TwoAtATimeHeapsort();
    RangeSortEarlyExitDetector detector  = new TwoWayInsertionEarlyExitDetector();
    SortList                   sorters   = new SortList();
    SinglePivotPartitioner     kanga     = new SkippyPartitioner();
    SinglePivotPartitioner kangaMirror   = new SkippyMirrorPartitioner();
    SinglePivotPartitioner balancedKanga = new BalancedSkippyPartitioner();
    SampleSizer                sqr       = new SquareRootSampleSizer();
    SampleCollector            nix       = new NullSampleCollector();
    sorters.add ( new QuicksortAdaptive(middle,  centipede, isort, 64, hsort, detector));
    sorters.add ( new QuicksortAdaptive(skewed,  centipede, isort, 64, hsort, detector));
    sorters.add ( new QuicksortAdaptive(middle,  kanga,     isort, 64, hsort, detector));
    sorters.add ( new InPlaceQuicksort (null,    centipede, centipede, 64, isort));
    sorters.add ( new InPlaceQuicksort (bestOf3, kanga, kangaMirror,   64, isort));
    sorters.add ( new SamplingInPlaceQuicksort ( sqr, nix, kanga, kangaMirror, 64, isort));
    sorters.add ( new SamplingInPlaceQuicksort ( sqr, nix, balancedKanga, balancedKanga, 64, isort));
    sorters.warmUp();
    DegenerateInputTest dit = DegenerateInputTest.newTest(65536, 400);
    sorters.writeSortHeader("input");
    dit.runSortsOnSortedData(sorters, "millisecond");
    System.out.println("");
    dit.runSortsOnDuplicatedData(sorters, "millisecond");			
  }
  @Test
  public void testInPlaceQuicksortAgain() {
    SortList                   sorters   = new SortList();
    RangeSorter                isort     = new InsertionSort2Way();
    RangeSorter                hsort     = new TwoAtATimeHeapsort();
    SinglePivotSelector        middle    = new MiddleElementSelector();
    CentripetalExpander        centri    = new CentripetalExpander();
    CentripetalPartitioner     centipede = new CentripetalPartitioner();
    SinglePivotPartitioner balancedKanga = new BalancedSkippyPartitioner();
    PartitionExpander                bkx = new BalancedSkippyExpander();
    RangeSortEarlyExitDetector detector  = new WainwrightEarlyExitDetector();
    CentripetalExpander              cx  = new CentripetalExpander();
    KthStatisticPartitioner         remC = new RemedianPartitioner(cx, cx, 2, false);
    KthStatisticPartitioner         momC = new MedianOfMediansPartitioner(5, centri, centri);
    NullSampleCollector              nix = new NullSampleCollector(); 
    KthStatisticPartitioner        a489C = new IntroSelect489Partitioner(nix, cx, cx);
    KthStatisticPartitioner         remA = new RemedianPartitioner(bkx, bkx, 2, false);
    KthStatisticPartitioner         momA = new MedianOfMediansPartitioner(5, bkx, bkx);
    KthStatisticPartitioner        a489A = new IntroSelect489Partitioner(nix, bkx, bkx);
    sorters.add ( new QuicksortAdaptive(middle, centipede, isort, 64, hsort, detector), "Reference");
    sorters.add ( new InPlaceQuicksort (middle, centipede, centipede, 64, isort), "QuickSelect (C)");
    sorters.add ( new InPlaceQuicksort (a489C, 64, isort), "Algorithm 489 (C)");
    sorters.add ( new InPlaceQuicksort (remC,  64, isort), "Median-of-Remedians (C)");
    sorters.add ( new InPlaceQuicksort (momC,  64, isort), "Median-Of-Medians (C)");
    sorters.add ( new QuicksortAdaptive(middle, balancedKanga, isort, 64, hsort, detector), "Reference (BA)");
    sorters.add ( new InPlaceQuicksort (middle, balancedKanga, balancedKanga, 64, isort), "QuickSelect (BA)");
    sorters.add ( new InPlaceQuicksort (a489A, 64, isort), "Algorithm 489 (BA)");
    sorters.add ( new InPlaceQuicksort (remA,  64, isort), "Median-of-Remedians (BA)");
    sorters.add ( new InPlaceQuicksort (momA,  64, isort), "Median-Of-Medians (BA)");
    sorters.warmUp();
    DegenerateInputTest dit = DegenerateInputTest.newTest(65536, 100);
    sorters.writeSortHeader("input");
    dit.runSortsOnSortedData(sorters, "millisecond");
    System.out.println("");
    dit.runSortsOnDuplicatedData(sorters, "millisecond");			
  }
  @Test
  public void testFixedPartitionQuicksort() {
    SortList                   sorters   = new SortList();
    sorters.add(new FixedPartitionQuicksort(new QuickSelectPartitioner()));
    sorters.add(new FixedPartitionQuicksort(.5, new QuickSelectPartitioner()));
    sorters.add(new FixedPartitionQuicksort(new RemedianPartitioner()));
    sorters.add(new FixedPartitionQuicksort(.5, new RemedianPartitioner()));
    sorters.add(new FixedPartitionQuicksort());   //with default ratio 1/phi
    sorters.add(new FixedPartitionQuicksort(.5)); //with the usual ratio 0.5
    sorters.add(new FixedPartitionQuicksort(new SimplifiedFloydRivestPartitioner()));
    sorters.add(new FixedPartitionQuicksort(.5, new SimplifiedFloydRivestPartitioner()));
    sorters.add(new FixedPartitionQuicksort(new FloydRivestPartitioner()));
    sorters.add(new FixedPartitionQuicksort(.5, new FloydRivestPartitioner()));
    sorters.warmUp();
    DegenerateInputTest dit = DegenerateInputTest.newTest(65536, 100);
    sorters.writeSortHeader("input");
    dit.runSortsOnSortedData(sorters, "millisecond");
    System.out.println("");
    dit.runSortsOnDuplicatedData(sorters, "millisecond");			
  }
  public void warmUpPartitioners(ArrayList<SinglePivotPartitioner> partitioners) {
    int n=1000;
    for (int r=0;r<1000;++r) {
      int[] junk = random.randomPermutation(n);
      for (SinglePivotPartitioner partitioner : partitioners) {
        int[] copy     = Arrays.copyOf( junk, n);
        int pivotIndex = (int) Math.floor(Math.random()*(double)n);
        partitioner.partitionRange (copy, 0, copy.length, pivotIndex);
      }
    }
  }	
  public void warmUpPartitioner(SinglePivotPartitioner partitioner) {
    int n=1000;
    for (int r=0;r<1000;++r) {
      int[] junk = random.randomPermutation(n);
      int pivotIndex = (int) Math.floor(Math.random()*(double)n);
      partitioner.partitionRange (junk, 0, junk.length, pivotIndex);
    }
  }
  @Test
  public void TestFP() {
    int n = 10001;
    int[] junk = random.randomPermutation(n);
    int i;
    for (i=0;i<n && junk[i]!=n/2;++i); 
    (new FlowerArrangementPartitioner()).partitionRange(junk, 0, n, i);
  }
  @Test
  public void testPermutingPartitioners() {
    SortList sorts = new SortList();
    for ( int p : new int[] { 15, 63, 255, 1023, 4095, 16383, 65535 } ) 
    {
      int                             x = p*16;
      DirtyCompoundMultiPivotSelector s = new DirtyCompoundMultiPivotSelector(p);
      PairInsertionSort               i = new PairInsertionSort();
      AsymmetricMergesort             m = new AsymmetricMergesort(i, 32); //stable
      RangeSorter                     c = new BranchAvoidingAlternatingCombsort(1.4, x/2);
      //Todo: use a properer quicksort, if x>sqrt(n), coz this is cheating!
      //      using BranchAvoidingAlternatingCombsort for x that large means
      //      most comparisons aren't pivot comparisons and this isn't a real quicksort
      sorts.add(new MultiPivotQuicksort(s, new CountAndPermutePartitioner(), c, x), "CP" + p);
      sorts.add(new MultiPivotQuicksort(s, new TrackAndPermutePartitioner(), c, x), "TP" + p);
      sorts.add(new MultiPivotQuicksort(s, new TrackAndPermutePartitionerWithBellsOn(), c, x), "TPB" + p);
      sorts.add(new ExternalMultiPivotQuicksort(s, new TrackAndCopyPartitioner(), m, x, m), "TC" + p);
    }
    sorts.warmUp();
    sorts.writeSortHeader("n");
    sortTest.testSpecificSorts(sorts, 10, 10000, 400, 10000000, 25);
    System.out.println("");
    sorts.writeSortHeader("n");
    sortTest.testSpecificSorts(sorts, 1017019, 20000000, 5);
  }
  @Test
  public void testPlacementPartitioner() {
    CleanSinglePivotSelector middle  = new MiddleElementSelector();
    RangeSorter              isort   = new InsertionSort2Way();
    SortList sorts = new SortList(middle, isort, 64);
	  sorts.addSPQ ( new SingletonPartitioner());
    sorts.addSPQ ( new PlacementPartitioner());
    sorts.warmUp();
    sorts.writeSortHeader("n");
    sortTest.testSpecificSorts(sorts, 10, 10000, 400, 10000000, 25);    
  }
  public void warmUpPartitioner(StandAlonePartitioner p) {
    int n=1000;
    for (int r=0;r<1000;++r) {
      int[] junk = random.randomPermutation(n);
      p.multiPartitionRange (junk, 0, junk.length);
    }
  }
  @Test
  public void testCForLargerP()
  {
    int n        = 1023;
    int maxP     = 15;
    int maxC     = 100;
    int runCount = 10;
    for (int c=1;c<maxC;++c) {
      double[] gain = new double [maxP];
      double[] cost = new double [maxP];
      double[] v    = new double [maxC];
      for (int r=0;r<runCount;++r) {
        for (int i=0;i<c;++i) {
          v[i] = Math.random();
        }
        Arrays.sort(v);
        for (int p=1;p<=maxP;++p) {
          gain[p-1] += guessGainPerItemFromPivots(v, 0, c, p, 0, 1 )*n;
          cost[p-1] += guessComparesPerItemFromPivots(v, 0, c, p, 0, 1)*n;
        }
      }
      String line = "" + c;
      for (int p=1;p<=maxP;++p) {
        double e = (gain[p-1]/cost[p-1]);
        line += "\t" + Math.floor(e*1000.0)/1000.0;
      }
      System.out.println(line);
    }
  }
  private double guessGainPerItemFromPivots
    ( double[] v, int iLo, int iHi
    , int p, double xLo, double xHi ) {
    double gain=0;
    while (1<p) {
      int i = iLo + (iHi - iLo)*(p/2)/(p+1);
        gain += guessGainPerItemFromPivots
                (v, i+1, iHi, p-p/2, v[i], xHi);
      iHi = i;
      xHi = v[i];
      p  -= p/2;
    }
    if (xLo<xHi) {
      gain += (xLo-xHi) * Math.log(xHi-xLo)/Math.log(2.0);
    }
    return gain;
  }
  private double guessComparesPerItemFromPivots
  ( double[] v, int iLo, int iHi
  , int p, double xLo, double xHi ) {
  double compares = (xHi-xLo);
  if (1<p) {
    int i = iLo + (iHi - iLo)*(p/2)/(p+1);
    compares += guessComparesPerItemFromPivots
           (v, iLo, i,   p/2,   xLo,  v[i])
         + guessComparesPerItemFromPivots
           (v, i+1, iHi, p-p/2, v[i], xHi);
    }
    return compares;
  }
  @Test
  public void testBinarySearchComparisonCounts() {
    int pStop = 1025;
    double[] cs = new double[pStop];
    //double[] ca = new double[pStop];
    //int[] split = new int[pStop];
    for (int p=1; p<pStop; ++p) {
      int m = p/2;
      double x = (double)(m+1) / (double)(p+1);
      cs[p] =  1 + x * cs[m] + (1-x) * cs[p-m-1];
      /*
      ca[p] = cs[p];
      split[p]=m;
      for (--m; m>0; --m) {
        x = (double)(m+1) / (double)(p+1);
        double t = 1 + x * cs[m] + (1-x) * cs[p-m-1];
        if (t < ca[p]) {
          ca[p] = t;
          split[p] = m;
        }
      }
      */
      double gain = Math.log(p+1)/Math.log(2);
      double overhead = cs[p]-gain;
      double log2PPlus1 = Math.log(p+1)/Math.log(2);
      double floorOfLog = Math.floor(log2PPlus1);
      double formula = floorOfLog 
        + 2.0*((p+1.0) - Math.pow(2.0, floorOfLog))/(p+1.0);
      System.out.println("" + p + "\t" + r(gain) + "\t"  + r(cs[p])
        + "\t" + r(formula)
        + "\t" + r(overhead) + "\t" + r(gain/(gain+overhead))
        /* + "\t" + r(ca[p]) + "\t" + r(ca[p]-g) + "\t" + split[p] */);
    }
  }
  @Test
  public void testComparisonEfficienciesForBinarySearchPartitioners() {
    int pStop = 127;
    for (int p=1; p<pStop; ++p) {
      String line = "" + p;
      double log2PPlus1 = Math.log(p+1)/Math.log(2);
      double floorOfLog = Math.floor(log2PPlus1);
      double cost = floorOfLog 
        + 2.0*((p+1.0) - Math.pow(2.0, floorOfLog))/(p+1.0);
      for (int s=0; s<=31; s=s+s+1) {
        double c = (p+1)*(s+1)-1;
        double gain = (Gain.harmonic(c+1)-Gain.harmonic(s+1))/Math.log(2);
        double efficiency = gain/cost;
        line += "\t" + r(efficiency);
      }
      System.out.println(line);
    }
  }
  @Test
  public void testItemMovementForCentrifugalPartitioners() {
    int n     = 10000;
    int r     = 10000;
    int pStop = 9;
    int sStop = 32;
    System.out.println("Predicted partitioning move costs for centripetal partitioners");
    String header = "p";
    for (int s=0; s<sStop; s=s+s+1) {
      header += "\ts=" + s;
    }
    System.out.println(header);
    for (int p=1; p<pStop; ++p) {
      String line = "" + p;
      boolean even = ((p&1)==0);
      for (int s=0; s<sStop; s=s+s+1) {
        double c = p*s + p + s;
        double predictedMoves = (p+3)/4.0 - 1.0/(double)(p+1) - 1.0/(c+2.0)/(double)(p+1);
        if (even) {
          predictedMoves 
            = p*(p+4)/(double)4.0/(double)(p+1)
            - 1/(double)(p+1)/(double)(p+1)/(c+2.0);
        } 
        line += "\t" + r(predictedMoves);
      }
      System.out.println(line);
    }
    System.out.println("\nActual partitioning move costs for centripetal partitioners"
      + " with n=" + n + ", and " + r + " executions for each (p,s) combination");
    System.out.println(header);
    for (int p=1; p<pStop; ++p) {
      String line = "" + p;
      for (int s=0; s<sStop; s=s+s+1) {
        double moveCount = 0;        
        for (int run=0; run<r; ++run) {
          moveCount += countCentrifugalMoves(p,s,n);
        }
        double amortizedMoveCount = moveCount/r/n;
        line += "\t" + r(amortizedMoveCount);
      }
      System.out.println(line);
    }
  }
  private double countCentrifugalMoves(int p, int s, int n) {
    int[] vData  = random.randomPermutation(n);
    int   c  = p * s + p + s;
    (new TwoAtATimeHeapsort()).sortRange(vData,0,c);
    for (int w=0; w<p; ++w) {
      int r = (w+1)*(s+1)-1;
      RangeSortHelper.swapElements(vData, w, r);
    }
    int    lhs   = p+1;
    int    rhs   = n-1;
    int    m     = p/2; //index of middle pivot
    int    v;           //item being considered
    int    d;           //destination pivot
    double count = 0; //item move count
    do {
      do {
        v = vData[lhs];
        d = BinaryInsertionSort.findPostInsertionPoint(vData, 0, p, v);
        ++lhs;
        if (d<m) {
          count += m-d+1;
        }
      } while (d<=m && lhs<=rhs);
      count += d-m;
      do {
        v = vData[rhs];
        d = BinaryInsertionSort.findPostInsertionPoint(vData, 0, p, v);
        --rhs;
        if (m+1<d) {
          count += d-m;
        }
      } while (m<d && lhs<=rhs);
      count += m-d+1;
    }
    while (lhs<=rhs);
    return count;
  }
  protected String r(double x) {
    return "" + Math.floor(x*100000.0+.5)/100000.0;
  }
	@Test
  public void testSection_20_2_1_1() {
    /*
    for (double m  = 3; m<=10000; m+=(m<100)?1.0:100.0) {
      double s  = 0;
      double s2 = 0;
      for (double k=1; k<=m; ++k) {
        for (double j=k+1; j<m; ++j) {
          s += k / j;
        }
        if (k<m) {
          s2 += k * (Math.log(m-1) - Math.log(k));
        }
      }
      System.out.println(m + "\t" + (m-3.0)/4.0 + "\t" + s/m 
        + "\t" + s2/m + "\t" + (s2-s)/m);
    }
    */
    for (double m  = 2; m<=10000; m+=(m<100)?1.0:100.0) {
      double s  = 0;
      double s2 = 0;
      for (double k=1; k<=m; ++k) {
        for (double j=k+1; j<m; ++j) {
          s += k / j;
        }
        if (k<m) {
          s2 += k * (Math.log(m-1) - Math.log(k));
        }
      }
      s2 += 2*Math.log(m);
      System.out.println(m + "\t" + 2*s2/(m+1)/m/Math.log(2) + "\t" + (2*s2/m-m/2) + "\t" + (s2-s));
    }
    System.out.println("" + (1.0/2.0/Math.log(2.0)));
  }
  public <T extends MultiPivotPartitioner> 
    void warmUpMultiPartitioners(ArrayList<T> partitioners) {
    int n=1000;
    for (int r=0;r<1000;++r) {
      int[] junk = random.randomPermutation(n);
      for (MultiPivotPartitioner partitioner : partitioners) {
        int[] copy = Arrays.copyOf( junk, n);
        int[] pivots;        
        if ( partitioner instanceof FixedCountPivotPartitioner) {
          FixedCountPivotPartitioner party2 = (FixedCountPivotPartitioner)partitioner;
          MultiPivotSelector s =new SamplingMultiPivotSelector(party2.getPivotCount(), false);
          pivots = s.selectPivotIndices(copy, 0, copy.length);
        } else {
          pivots = new int[] { (int) Math.floor(Math.random()*(double)n) };  
        }
        partitioner.multiPartitionRange (copy, 0, copy.length, pivots);
      }
    }
  }
  @Test
  public void testKangarooPartitionerEfficiences() {
    ArrayList<FixedCountPivotPartitioner> partitioners = 
      new ArrayList<FixedCountPivotPartitioner>();
    partitioners.add(new SingleToMultiPartitioner
                         ( new SkippyPartitioner() ) );
    partitioners.add(new SkippyPartitioner2());
    partitioners.add(new SkippyPartitioner3());
    partitioners.add(new SkippyPartitioner4());
    partitioners.add(new SkippyPartitioner5());
    partitioners.add(new SkippyPartitioner6());
    partitioners.add(new SkippyPartitioner7());
    warmUpMultiPartitioners(partitioners);
    int n=1048576;
    runCount = 100;
    double[] times = new double[partitioners.size()];
    double[] gains = new double[partitioners.size()];
    for (int r=0;r<runCount;++r) {
      int[] junk = random.randomPermutation(n);
      int p =0;
      for (FixedCountPivotPartitioner partitioner : partitioners) {
        int[] copy = Arrays.copyOf(junk, n);
        MultiPivotSelector s 
          = new SamplingMultiPivotSelector
                ( partitioner.getPivotCount(), false );
        int[] pivots = s.selectPivotIndices(copy, 0, copy.length);
        times[p] -= System.nanoTime();
        int[] boundaries = partitioner.multiPartitionRange(copy, 0, n, pivots);
        times[p] += System.nanoTime();
        gains[p] += bitsOfChaos(n);
        for (int i=0; i<boundaries.length-1; i+=2) {
          gains[p] -= bitsOfChaos(boundaries[i+1]-boundaries[i]);
        }
        ++p;
      }
    }
    for (int p=0; p<partitioners.size(); ++p) {
      System.out.println(partitioners.get(p).toString() + f5(gains[p]/times[p]));
    }
  }
  public String f5(double x) {
    return "\t" + (Math.floor(x * 100000.0 + .5) / 100000.0);
  }
  public void warmUpPartitionerSets  
    ( ArrayList<PartitionerSet> partitionerSets) {
    ArrayList<SinglePivotPartitioner> parties 
      = new ArrayList<SinglePivotPartitioner>();
    ArrayList<PartitionExpander> pandas 
      = new ArrayList<PartitionExpander>();
    for ( PartitionerSet s : partitionerSets) {
      parties.add(s.getLeftPartitioner());
      parties.add(s.getCentrePartitioner());
      parties.add(s.getRightPartitioner());
      pandas.add(s.getLeftExpander());
      pandas.add(s.getCentreExpander());
      pandas.add(s.getRightExpander());
    }
    warmUpPartitioners(parties);
    warmUpExpanders(pandas);
  }  
  public void warmUpExpanders
    ( ArrayList<PartitionExpander> pandas ) {
    int n=1000;
    SinglePivotPartitioner p 
      = new SingletonPartitioner();
    for (int r=0;r<1000;++r) {
      int[] junk = random.randomPermutation(n);
      for (PartitionExpander panda : pandas) {
        int[] vCopy     = Arrays.copyOf( junk, n);
        int pivotIndex = (int) Math.floor(Math.random()*(double)n);
        int hole       = p.partitionRange 
                         ( vCopy, vCopy.length/4
                         , vCopy.length*3/4, pivotIndex);
        panda.expandPartition
        ( vCopy, 0, vCopy.length/4, hole
        , vCopy.length*3/4, vCopy.length);
      }
    }
    
  }

}
