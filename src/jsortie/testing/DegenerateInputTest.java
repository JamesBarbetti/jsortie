package jsortie.testing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jsortie.Java8ParallelSorter;
import jsortie.RangeSorter;
import jsortie.earlyexitdetector.NullEarlyExitDetector;
import jsortie.earlyexitdetector.RangeSortEarlyExitDetector;
import jsortie.earlyexitdetector.TwoWayInsertionEarlyExitDetector;
import jsortie.earlyexitdetector.WainwrightEarlyExitDetector;
import jsortie.exception.SortingFailureException;
import jsortie.heapsort.topdown.HeapsortStandard;
import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.helper.DumpRangeHelper;
import jsortie.helper.RangeSortHelper;
import jsortie.janitors.exchanging.BranchAvoidingAlternatingCombsort;
import jsortie.janitors.insertion.InsertionSort;
import jsortie.janitors.insertion.OrigamiInsertionSort;
import jsortie.janitors.insertion.PairInsertionSort;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.mergesort.hybrid.WinnowingSort;
import jsortie.mergesort.hybrid.WinnowingSort2;
import jsortie.mergesort.timsort.IntegerTimsortWrapper;
import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.PositionalSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.collector.external.ExternalPositionalCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.adapter.PartitionerToExpander;
import jsortie.quicksort.expander.branchavoiding.LeftSkippyExpander;
import jsortie.quicksort.expander.branchavoiding.LeftTunedExpander;
import jsortie.quicksort.expander.branchavoiding.RightSkippyExpander;
import jsortie.quicksort.expander.branchavoiding.RightTunedExpander;
import jsortie.quicksort.expander.unidirectional.LeftLomutoExpander;
import jsortie.quicksort.expander.unidirectional.RightLomutoExpander;
import jsortie.quicksort.governor.QuicksortGovernor;
import jsortie.quicksort.governor.adaptive.QuicksortAdaptive;
import jsortie.quicksort.governor.adaptive.QuicksortAdaptive2;
import jsortie.quicksort.governor.adaptive.QuicksortTwoPartitioner;
import jsortie.quicksort.governor.expansionist.ExpansionistQuicksort;
import jsortie.quicksort.governor.expansionist.NormativeSamplePartitioner;
import jsortie.quicksort.multiway.governor.AdaptiveMultiPivotQuicksort;
import jsortie.quicksort.multiway.governor.MultiPivotQuicksort;
import jsortie.quicksort.multiway.governor.MultiThreadedQuicksort;
import jsortie.quicksort.multiway.partitioner.FixedCountPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.adapter.SingleToMultiPartitioner;
import jsortie.quicksort.multiway.partitioner.centrifugal.CentrifugalPartitioner2;
import jsortie.quicksort.multiway.partitioner.centrifugal.CentrifugalPartitioner3;
import jsortie.quicksort.multiway.partitioner.centrifugal.CentrifugalPartitioner4;
import jsortie.quicksort.multiway.partitioner.centrifugal.CentrifugalPartitioner5;
import jsortie.quicksort.multiway.partitioner.centrifugal.CentrifugalPartitioner6;
import jsortie.quicksort.multiway.partitioner.centrifugal.CentrifugalPartitioner7;
import jsortie.quicksort.multiway.partitioner.centripetal.CentripetalPartitioner2;
import jsortie.quicksort.multiway.partitioner.decorator.BSortPartitioner;
import jsortie.quicksort.multiway.partitioner.decorator.BirdsOfAFeatherPartitioner;
import jsortie.quicksort.multiway.partitioner.decorator.FoldingPartitioner;
import jsortie.quicksort.multiway.partitioner.decorator.HSortDecorator;
import jsortie.quicksort.multiway.partitioner.decorator.QSortePartitioner;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner3;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner4;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner5;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner6;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner7;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner8;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyCentripetalPartitioner2;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner2;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner3;
import jsortie.quicksort.multiway.partitioner.singlepivot.AnimalFarmPartitioner;
import jsortie.quicksort.multiway.partitioner.singlepivot.BentleyMcIlroyPartitioner;
import jsortie.quicksort.multiway.partitioner.singlepivot.DutchNationalFlagPartitioner;
import jsortie.quicksort.multiway.partitioner.singlepivot.HoyosDutchPartitioner;
import jsortie.quicksort.multiway.partitioner.singlepivot.InertialPartitioner;
import jsortie.quicksort.multiway.partitioner.singlepivot.SkippyDutchPartitioner;
import jsortie.quicksort.multiway.partitioner.singlepivot.OptimisticBentleyMcIlroyPartitioner;
import jsortie.quicksort.multiway.partitioner.singlepivot.STLTernaryPartitioner;
import jsortie.quicksort.multiway.partitioner.singlepivot.SlowCookedDutchPartitioner;
import jsortie.quicksort.multiway.partitioner.singlepivot.YaroslavskiyDutchPartitioner;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner2;
import jsortie.quicksort.multiway.partitioner.twopivot.YaroslavskiyPartitioner2;
import jsortie.quicksort.multiway.protector.CheckedMultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.twopivot.Java8Partitioner;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.multiway.selector.adapter.SingleToMultiSelector;
import jsortie.quicksort.multiway.selector.clean.CleanMultiPivotPositionalSelector;
import jsortie.quicksort.multiway.selector.clean.CleanMultiPivotSelector;
import jsortie.quicksort.multiway.selector.clean.SamplingMultiPivotSelector;
import jsortie.quicksort.partitioner.PartitionerSet;
import jsortie.quicksort.partitioner.bidirectional.centrepivot.CentrePivotExchangePartitioner;
import jsortie.quicksort.partitioner.bidirectional.centrepivot.CentrePivotPartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.HoyosPartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.SingletonPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.JavaFriendlyPDQPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyCentripetalPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyMirrorPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.PDQPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.BalancedSkippyPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.FatalisticFloristPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.FlowerArrangementPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.TunedPartitioner;
import jsortie.quicksort.partitioner.insideout.CentripetalPartitioner;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.Algorithm489Partitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.ZeroDelta489Partitioner;
import jsortie.quicksort.partitioner.lomuto.LomutoMirrorPartitioner;
import jsortie.quicksort.partitioner.lomuto.LomutoPartitioner;
import jsortie.quicksort.partitioner.unidirectional.Defenestrator;
import jsortie.quicksort.partitioner.unidirectional.HolierThanThouPartitioner;
import jsortie.quicksort.partitioner.unidirectional.SmallMercyPartitioner;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.clean.CleanLeftHandedSelector;
import jsortie.quicksort.selector.clean.CleanPositionalSampleSelector;
import jsortie.quicksort.selector.clean.CleanRemedianSelector;
import jsortie.quicksort.selector.clean.CleanSinglePivotSelector;
import jsortie.quicksort.selector.clean.CleanTheoreticalSelector;
import jsortie.quicksort.selector.dirty.DirtyCompoundSelector;
import jsortie.quicksort.selector.dirty.DirtySingletonSelector;
import jsortie.quicksort.selector.dirty.DirtyTheoreticalSelector;
import jsortie.quicksort.selector.dirty.Java8Selector;
import jsortie.quicksort.selector.simple.FirstElementSelector;
import jsortie.quicksort.selector.simple.MiddleElementSelector;
import jsortie.quicksort.selector.simple.RandomElementSelector;
import jsortie.quicksort.sort.decorator.AnimalFarmSort;
import jsortie.quicksort.sort.decorator.MuddlingSort;
import jsortie.quicksort.sort.decorator.RandomizedSort;
import jsortie.quicksort.sort.decorator.ScatteredSort;
import jsortie.quicksort.sort.decorator.SlamTiltSort;
import jsortie.quicksort.sort.duplicates.DuplicateFriendlySort;
import jsortie.testing.DegenerateInput.DegenerateInputType;

public class DegenerateInputTest extends SortTest {
  protected int maxCount = 65536 * 16;
  protected int runCount = 25;
  protected DegenerateInput degen = new DegenerateInput();
  protected RandomInput    random = new RandomInput();
  protected SortTest     sortTest = new SortTest();
  protected PartitionerTest    tp = new PartitionerTest();
  protected MultiPivotTest    mpt = new MultiPivotTest();
  static    double thousand = 1000;
  static    double million  = thousand * thousand;
  static    double billion  = thousand * million;
  
  RangeSorter             isort        = new InsertionSort();
  RangeSorter             isort2Way    = new InsertionSort2Way();
  SinglePivotPartitioner  centripetal  = new CentripetalPartitioner();
  SinglePivotPartitioner  singleton    = new SingletonPartitioner();
  int                     threshold    = 64;
  RangeSorter             lastResort   = new TwoAtATimeHeapsort();
  MultiPivotPartitioner   bentley      = new BentleyMcIlroyPartitioner();
  MultiPivotPartitioner   oBentley     = new OptimisticBentleyMcIlroyPartitioner();
  MultiPivotPartitioner   stlTernary   = new STLTernaryPartitioner();

  MultiPivotPartitioner   dutch        = new DutchNationalFlagPartitioner();  
  WainwrightEarlyExitDetector earlyOut = new WainwrightEarlyExitDetector();

  public DegenerateInputTest() {
  }
  public static DegenerateInputTest newTest(int count, int runs) {
	DegenerateInputTest dit = new DegenerateInputTest();
    dit.maxCount = count;
    dit.runCount = runs;
    return dit;
  }
  @Test
  public void testSTLTernaryPartitioner() {
    int[] input = new int[] 
      { 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0
      , 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0
      , 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0, 0, 0, 1
      , 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1 };
    stlTernary.multiPartitionRange(input, 0, input.length, new int[] { 34 } );
  }
  @Test
  public void testSortsOnAnimalFarmData() {
    maxCount = 65536;
    runCount = 200;
    SinglePivotSelector       me        = new MiddleElementSelector();
    MultiPivotSelector        mme       = new SingleToMultiSelector(me);
    RangeSorter               heapsort  = new HeapsortStandard();
    RangeSortEarlyExitDetector detector = new TwoWayInsertionEarlyExitDetector();
    MultiPivotSelector        any2      = new SamplingMultiPivotSelector(2, false);
    MultiPivotPartitioner  yaroslavskiy = new YaroslavskiyPartitioner2();
    MultiPivotPartitioner[]   inertial  = new InertialPartitioner[4];
    for (int i=0; i<inertial.length; ++i) {
      inertial[i] = new InertialPartitioner(i);
    }
    SinglePivotPartitioner smallmercy        = new SmallMercyPartitioner();
    SinglePivotPartitioner lomutoAndMirror[] = new SinglePivotPartitioner[]
      { new LomutoPartitioner(), new LomutoMirrorPartitioner() };
    
    SinglePivotPartitioner kanga            = new SkippyPartitioner();
    SinglePivotPartitioner kangaMirror      = new SkippyMirrorPartitioner();
    SinglePivotPartitioner kangaBalanced    = new BalancedSkippyPartitioner();
    MultiPivotPartitioner  kanga2           = new SkippyPartitioner2();
    MultiPivotPartitioner  kangaDutch       = new SkippyDutchPartitioner();
    SinglePivotPartitioner kangaAndMirror[] = new SinglePivotPartitioner[]
       { kanga, kangaMirror };
    
    //Not designed for it
    SortList sorts = new SortList();
    sorts.add(new QuicksortGovernor       (me,   singleton,        isort, 64), "Singleton");
    sorts.add(new QuicksortAdaptive       (me,   singleton,        isort, 64, heapsort, detector), "Singleton-Adaptive");
    sorts.add(new QuicksortTwoPartitioner (me,   lomutoAndMirror,  isort, 64, heapsort, detector), "Lomuto + LomutoMirror");
    sorts.add(new MultiPivotQuicksort     (any2, yaroslavskiy,     isort, 64), "Yaroslavskiy");

    //Designed for it
    sorts.add(new QuicksortGovernor       (me,   smallmercy,       isort, 64), "SmallMercy");
    sorts.add(new MultiPivotQuicksort     (mme,  bentley,          isort, 64), "Bentley-McIlroy");
    sorts.add(new MultiPivotQuicksort     (mme,  oBentley,         isort, 64), "Optimistic Bentley-McIlroy");
    sorts.add(new MultiPivotQuicksort     (mme,  dutch,            isort, 64), "DutchNationalFlag");
    for (int i=0; i<inertial.length; ++i) {
      sorts.add(new MultiPivotQuicksort   (mme,  inertial[i],      isort, 64), "Intertial " + i);
    }
    
    //Not designed for it, branch-avoiding
    sorts.add(new QuicksortGovernor       (me,   kangaBalanced,    isort, 64), "BalancedSkippy");
    sorts.add(new QuicksortAdaptive       (me,   kanga,            isort, 64, heapsort, detector), "Skippy");
    sorts.add(new QuicksortTwoPartitioner (me,   kangaAndMirror,   isort, 64, heapsort, detector), "Skippy + SkippyMirror");
    sorts.add(new MultiPivotQuicksort     (any2, kanga2,           isort, 64), "Skippy2");
    
    //Designed for it, branch avoiding
    sorts.add(new MultiPivotQuicksort     (mme,  kangaDutch,       isort, 64), "SkippyDutch");
    sorts.add(new MultiPivotQuicksort     (mme,  stlTernary,       isort, 64), "STL Ternary");
    sorts.warmUp();
    System.out.println("Sorts run on " + maxCount + "-integer animal farm inputs, running times in milliseconds");
    sorts.writeSortHeader("input");
    runSortsOnDuplicatedData(sorts, "millisecond");
  }
  public void testSortsOnSortedData() {
    RangeSorter isort = new InsertionSort();
    SinglePivotSelector me = new MiddleElementSelector();
    SinglePivotPartitioner lomutoAndMirror[] = new SinglePivotPartitioner[] 
      { new LomutoPartitioner(), new LomutoMirrorPartitioner() };
    RangeSorter heapsort = new HeapsortStandard();
    RangeSortEarlyExitDetector detector = new TwoWayInsertionEarlyExitDetector();

    CleanLeftHandedSelector lefty = new CleanLeftHandedSelector(true);
    CentrePivotPartitioner hoyos = new CentrePivotPartitioner();
    SingletonPartitioner singleton = new SingletonPartitioner();
    SamplingMultiPivotSelector sampling = new SamplingMultiPivotSelector(2, false);

    SortList sorts = new SortList();
    sorts.add(new QuicksortGovernor(me, singleton, isort, 5));
    sorts.add(new QuicksortAdaptive(me, singleton, isort, 5, heapsort, detector));
    sorts.add(new QuicksortAdaptive(lefty, hoyos, isort, 5,  heapsort, detector));
    sorts.add(new QuicksortTwoPartitioner(me, lomutoAndMirror, isort, 5, heapsort, detector));
    sorts.add(new MultiPivotQuicksort(sampling, new YaroslavskiyPartitioner2(), isort, 5));
    runSortsOnSortedData(sorts, "second");
  }
  @Test
  public void testSortsOnScatteredData() {
    int oldMaxCount = maxCount;
    int oldRunCount = runCount;
    maxCount = 65536;
    runCount = 25;
    RangeSorter isort = new InsertionSort();
    RangeSorter lomutoSort = new QuicksortGovernor
      ( new DirtySingletonSelector(), new LomutoPartitioner(), isort, 32 );
    RangeSorter randomSelectorSort = new QuicksortGovernor
      ( new RandomElementSelector(), new LomutoPartitioner(), isort, 32 );
    RangeSorter naughtySort = new QuicksortGovernor
        ( new FirstElementSelector(), new LomutoPartitioner(), isort, 32 );

    SortList sorts = new SortList();
    sorts.add( lomutoSort, "DirtySingletonSelector + Lomuto" );
    sorts.add( randomSelectorSort, "RandomElementSelector + Lomuto" );
    sorts.add(new ScatteredSort  ( lomutoSort ),  "Scattered");
    sorts.add(new RandomizedSort ( lomutoSort ),  "Randomized");
    sorts.add(new MuddlingSort   ( lomutoSort ),  "Muddled");
    sorts.add(new MuddlingSort   ( naughtySort ), "Muddled (with FirstElementSelector!)");
    System.out.println("testSortsOnScatteredData, running times, in milliseconds, to sort " + maxCount + " integers");
    sorts.writeSortHeader("input");
    sorts.warmUp();
    runSortsOnSortedData(sorts, "millisecond");
    maxCount = oldMaxCount;
    runCount = oldRunCount;
  }

  public void testHoyosVersusHoyos2() {
    int oldMaxCount = maxCount;
    int oldRunCount = runCount;
    maxCount = 65536;
    runCount = 25;
    SinglePivotSelector me = new MiddleElementSelector();
    SortList sorts = new SortList();
    sorts.add(new QuicksortGovernor(me, singleton, isort, 5));
    sorts.add(new QuicksortGovernor(me, new CentrePivotExchangePartitioner(), isort, 5));
    sorts.add(new QuicksortGovernor(me, new HoyosPartitioner(), isort, 5));
    sorts.add(new QuicksortGovernor(me, new CentrePivotPartitioner(), isort, 5));
    runSortsOnSortedData(sorts, "second");
    maxCount = oldMaxCount;
    runCount = oldRunCount;
  }

	public void testDefenestratorAgainstHoyos2() {
		int oldMaxCount = maxCount;
		int oldRunCount = runCount;
		maxCount = 65536;
		runCount = 25;
		RangeSorter isort = new InsertionSort();
		SinglePivotSelector me = new MiddleElementSelector();
		SortList sorts = new SortList();
		sorts.add(new QuicksortGovernor(me, new CentrePivotPartitioner(), isort, 64));
		sorts.add(new QuicksortGovernor(me, new Defenestrator(), isort, 64));
		runSortsOnSortedData(sorts, "second");
		maxCount = oldMaxCount;
		runCount = oldRunCount;
	}

  public void testAgainstTimsortRound1() {
    maxCount = 65536;
    runCount = 100;
    SinglePivotSelector    selector    = new CleanRemedianSelector(false);
    SinglePivotPartitioner partitioner = new CentripetalPartitioner();

    SortList sorts = new SortList();
    sorts.add(new IntegerTimsortWrapper());
    sorts.add(new QuicksortGovernor(selector,partitioner,isort2Way,threshold));
    sorts.add(new QuicksortAdaptive(selector, partitioner, isort2Way
				 , threshold, lastResort, new TwoWayInsertionEarlyExitDetector() ));
	for ( SinglePivotPartitioner tuned : new SinglePivotPartitioner []
				{ new TunedPartitioner(), new SkippyCentripetalPartitioner()} ) {
      sorts.add(new QuicksortAdaptive(selector, tuned, isort2Way
				 , threshold, lastResort, new TwoWayInsertionEarlyExitDetector()));
    }
    sorts.warmUp();
    runSortsOnSortedData     ( sorts, "second");
    runSortsOnDuplicatedData ( sorts, "second");
  }
  @Test
  public void testAnimalFarmSort() {
    maxCount = 65536;
    runCount = 100;
    SinglePivotSelector     selector    = new CleanRemedianSelector(true);
    SkippyPartitioner         kp      = new SkippyPartitioner();
    SkippyMirrorPartitioner   kmp     = new SkippyMirrorPartitioner();
    BalancedSkippyPartitioner bkp     = new BalancedSkippyPartitioner();
    RangeSorter s1 = new QuicksortAdaptive
      ( selector, centripetal, isort2Way, threshold, lastResort, earlyOut );
    RangeSorter s2 = new QuicksortAdaptive
      ( selector, bkp, isort2Way, threshold, lastResort, earlyOut );
    SortList sorts = new SortList(selector, isort2Way, threshold);
    sorts.setEarlyExitDetector( earlyOut );
    sorts.addMPQ ( bentley );
    sorts.add2PQ ( kp, kmp );
    sorts.add(s1, "Centripetal");
    sorts.add(s2, "Skippy");
    sorts.add(new AnimalFarmSort(s1), "AnimalFarm(Centripetal)");
    sorts.add(new AnimalFarmSort(s2), "AnimalFarm(Skippy)");
    sorts.writeSortHeader("input");
    sortTest.warmUpSorts(sorts);
    runSortsOnSortedData    (sorts, "millisecond");
    System.out.println("");
    runSortsOnDuplicatedData(sorts, "millisecond");
  }
  @Test
  public void testSlamTiltSort() {
    testSlamTiltSort(65536, 100);
    //testSlamTiltSort(1048576, 10);
  }
  public void testSlamTiltSort(int n, int r) {
    maxCount = n;
    runCount = r;
    RangeSorter 
      origami  = new OrigamiInsertionSort();
    SinglePivotSelector 
      selector = new CleanRemedianSelector(true);
    SinglePivotPartitioner
       singlet = new SingletonPartitioner();
    RangeSorter s1 = new QuicksortAdaptive
      ( selector, singlet, origami
      , threshold, lastResort, earlyOut );
    RangeSorter s2 = new QuicksortTwoPartitioner
      ( new MiddleElementSelector()
      , new SinglePivotPartitioner[] { singlet, singlet }
      , new InsertionSort2Way(), threshold, lastResort
      , new NullEarlyExitDetector());
    RangeSorter s3 = new QuicksortAdaptive
      ( selector, new BalancedSkippyPartitioner()
      , origami, threshold, lastResort, earlyOut );
    SkippyPartitioner kp
      = new SkippyPartitioner();
    SkippyMirrorPartitioner kmp
      = new SkippyMirrorPartitioner();
    RangeSorter bothKangas
      = new QuicksortTwoPartitioner
            ( new CleanRemedianSelector(true)
            , new SinglePivotPartitioner[] { kp, kmp }
            , origami, threshold, lastResort
            , new WainwrightEarlyExitDetector());
    SortList sorts = new SortList(selector, origami, threshold);
    sorts.add( new IntegerTimsortWrapper(), "Timsort");
    sorts.addMPQ ( bentley );
    sorts.addMPQ ( oBentley );
    sorts.add2PQ ( new SkippyPartitioner()
                 , new SkippyMirrorPartitioner());
    sorts.add(s1, "SingletonPartitioner");
    sorts.add(s3, "BalancedKangarooPartitioner");
    sorts.add(new SlamTiltSort( new WinnowingSort(s1)
                              , bothKangas), "SlamTiltSort(w1)");
    sorts.add(new SlamTiltSort( new WinnowingSort2(s2)
                              , bothKangas), "SlamTiltSort(w2)");
    sorts.writeSortHeader("input");
    sortTest.warmUpSorts(sorts);
    runSortsOnSortedData    (sorts, "millisecond");
    System.out.println("");
    runSortsOnDuplicatedData(sorts, "millisecond");
  }
  @Test
  public void testAgainstTimsortRound2() {
    //Penguin: rerun
    maxCount = 65536;
    runCount = 1000;
    SortList sorts = new SortList();
    SampleCollector nix = new NullSampleCollector();
    KthStatisticPartitioner  kspCentre = new ZeroDelta489Partitioner();
    KthStatisticPartitioner  kspEdge   
      = new ZeroDelta489Partitioner
          ( nix, new LeftLomutoExpander()
          , new RightLomutoExpander());
    ExternalPositionalCollector pos    = new ExternalPositionalCollector();
    CleanSinglePivotSelector clean10   = new CleanTheoreticalSelector(.1, pos, kspEdge);
    CleanSinglePivotSelector clean50   = new CleanTheoreticalSelector(.5, pos, kspCentre);
    
    sorts.add(new IntegerTimsortWrapper());
    SinglePivotPartitioner[]   parties    = new SinglePivotPartitioner[] 
      { new LomutoPartitioner(), new CentrePivotPartitioner()
      , new TunedPartitioner(),  new SkippyPartitioner()
      , new SkippyCentripetalPartitioner()};
    CleanSinglePivotSelector[] selectors  = new CleanSinglePivotSelector[] 
      {  clean10, clean10, clean50, clean50, clean50 };
    for (int i=0; i<parties.length; ++i) {
      QuicksortAdaptive2 qa2 = new QuicksortAdaptive2
        (selectors[i], parties[i], isort2Way, threshold, lastResort);
      sorts.add(qa2);
    }
    sortTest.warmUpSorts(sorts);
    runSortsOnSortedData(sorts, "second");
    runSortsOnDuplicatedData(sorts, "second");
  }
  @Test
  public void testAgainstTimsortRound3() {
    maxCount = 65536;
    SortList sorts = new SortList();
    MultiPivotSelector any3 = new SamplingMultiPivotSelector(3, false);
    MultiPivotPartitioner htt3 = new HolierThanThouPartitioner3();
    RangeSorter isort = new InsertionSort2Way();
    sorts.add(new IntegerTimsortWrapper());
    sorts.add(new MultiPivotQuicksort(any3, new BSortPartitioner(htt3), isort, 64));
    sorts.add(new MultiPivotQuicksort(any3, new QSortePartitioner(htt3), isort, 64));
    sorts.add(new MultiPivotQuicksort(any3, new HSortDecorator(htt3), isort, 64)); //Broken
    sorts.add(new MultiPivotQuicksort(any3, new FoldingPartitioner(htt3), isort, 64));
    runSortsOnSortedData(sorts, "second");
    runSortsOnDuplicatedData(sorts, "second");
  }
  @Test
  public void testAgainstTimsortRound4() {
    MultiPivotSelector    any2  = new SamplingMultiPivotSelector(2, false);
    MultiPivotPartitioner c2    = new CentrifugalPartitioner2();
    RangeSorter           isort = new InsertionSort2Way();
    RangeSorter           qs2   = new AdaptiveMultiPivotQuicksort(any2, c2, isort, 64);
    MultiPivotSelector    s1    = new SingleToMultiSelector(new CleanLeftHandedSelector(false));
    MultiPivotPartitioner p1    = new SingleToMultiPartitioner(new CentrePivotPartitioner());
    RangeSorter qs1 = new AdaptiveMultiPivotQuicksort(s1, p1, isort, 64);
    SortList sorts = new SortList();
    sorts.add(new WinnowingSort(qs1));
    sorts.add(new WinnowingSort(qs2));
    sorts.add(new IntegerTimsortWrapper());
    sorts.add(qs1);
    sorts.add(qs2);
    runSortsOnSortedData(sorts, "second");
    runSortsOnDuplicatedData(sorts, "second");
  }
  @Test
  public void testAgainstTimsortRound5() {
    //Penguin: rerun
    maxCount = 1048576;
    runCount = 25;
    SampleCollector nix   = new NullSampleCollector();
    RangeSorter     isort = new InsertionSort2Way();
    SortList        sorts = new SortList();
    sorts.add(new IntegerTimsortWrapper());
    SkippyPartitioner            kp  = new SkippyPartitioner();
    SkippyCentripetalPartitioner kcp = new SkippyCentripetalPartitioner();
    
    for (SinglePivotPartitioner p : new SinglePivotPartitioner[] {kp, kcp } ) {    
      MultiPivotSelector          s1   = new SingleToMultiSelector(new DirtyTheoreticalSelector(.5));
      MultiPivotPartitioner       p1   = new SingleToMultiPartitioner(p);
      RangeSorter                 qs1  = new AdaptiveMultiPivotQuicksort(s1, p1, isort, 64);
      PartitionExpander           pa   = new LeftLomutoExpander();
      PartitionExpander           mpa  = new RightLomutoExpander();
      ZeroDelta489Partitioner     z489 = new ZeroDelta489Partitioner(nix, pa, mpa);
      SampleSizer                 sqr  = new SquareRootSampleSizer();
      SampleCollector             pos  = new PositionalSampleCollector();
      MultiPivotSelector          s1a  = new SingleToMultiSelector(new DirtyTheoreticalSelector(.125, sqr, pos, z489));
      MultiPivotPartitioner       p1a  = new SingleToMultiPartitioner(new LomutoPartitioner());
      RangeSorter                 qs1a = new AdaptiveMultiPivotQuicksort(s1a, p1a, isort, 64);
      sorts.add(new WinnowingSort2(qs1));
      sorts.add(new WinnowingSort2(qs1a));
    }
    MultiPivotPartitioner   htt2 = new HolierThanThouPartitioner2();
    MultiPivotPartitioner   cp2  = new CentrifugalPartitioner2();
    for (MultiPivotPartitioner c2a : new MultiPivotPartitioner[] { htt2, cp2 } ) {
      MultiPivotSelector      any2 = new SamplingMultiPivotSelector(2, false);
      RangeSorter             qs2a = new AdaptiveMultiPivotQuicksort(any2, c2a, isort, 64);
      MultiPivotSelector sampling2 = new SamplingMultiPivotSelector(20, 2, false);
      MultiPivotPartitioner     c2 = new SkippyPartitioner2();
      RangeSorter              qs2 = new AdaptiveMultiPivotQuicksort(sampling2, c2, isort, 64);
      sorts.add(new WinnowingSort2(qs2));
      sorts.add(new WinnowingSort2(qs2a));
    }
    sortTest.warmUpSorts(sorts);
    runSortsOnSortedData(sorts, "second");
    runSortsOnDuplicatedData(sorts, "second");
    System.out.println("");
    runSortsOnOtherDegenerateData(sorts,2.0,true);
  }
  public class SelectorPicker {
    public SinglePivotSelector chooseSelector(SinglePivotPartitioner p) {
      return new CleanPositionalSampleSelector(5);
    }
  }
  public class JanitorPicker {
    public RangeSorter chooseJanitor(SinglePivotPartitioner p) {
      return new InsertionSort2Way();
    }
    public int getJanitorThreshold(SinglePivotPartitioner p) {
      return 64;
    }
  }
  public void testSinglePivotPartitioners
    ( NamedArrayList<SinglePivotPartitioner> partitioners
    , boolean degenerate) {
    testSinglePivotPartitioners
      ( partitioners, degenerate
      , new SelectorPicker(), new JanitorPicker()
      , (maxCount<1048576) ? "millisecond" : "second");
  }
  public void testSinglePivotPartitioners
    ( NamedArrayList<SinglePivotPartitioner> partitioners
    , boolean degenerate, SelectorPicker picker
    , JanitorPicker janitorPicker
    , String unit) {
    System.out.println("(times measured in " + unit + "s)");
    TwoAtATimeHeapsort         heap             = new TwoAtATimeHeapsort();
    SortList                   sorts            = new SortList();
    RangeSortEarlyExitDetector detector         = new TwoWayInsertionEarlyExitDetector();
    SinglePivotSelector        selector         = new MiddleElementSelector();
    int                        janitorThreshold = 64;
    RangeSorter                janitor          = new InsertionSort2Way();
    String header = "Input";
    for (SinglePivotPartitioner par : partitioners) {
      selector              = picker.chooseSelector(par);
      janitor               = janitorPicker.chooseJanitor(par);
      janitorThreshold      = janitorPicker.getJanitorThreshold(par);
      QuicksortAdaptive qa2 = new QuicksortAdaptive
                                ( selector, par, janitor
                                , janitorThreshold, heap, detector);
      sorts.add(qa2);
      header += "\t" + par.toString();
    }
    if (degenerate) {	
      System.out.println("Adaptive quicksorts " 
        + "(using " + selector.toString() + ", as the selector"
        + ", " + janitor.toString() + " as the janitor, and"
        + "a janitor threshold of " + janitorThreshold + ")" 
        + ", for inputs of " + maxCount + " integers");
      System.out.println(header);
      runSortsOnSortedData(sorts, unit);
      runSortsOnDuplicatedData(sorts, unit);
    } else {
      System.out.println(header);
      sortTest.testRandomInputs(sorts, 20, 100 * SortTest.million, false);
    }
  }
  private void testPartitionerSets
    ( ArrayList<PartitionerSet> partitionerSets
    , JanitorPicker janitorPicker, String unit) {
    System.out.println("(times measured in " + unit + "s)");
    SampleCollector    nix   = new NullSampleCollector();
    TwoAtATimeHeapsort heap  = new TwoAtATimeHeapsort();
    SortList           sorts = new SortList();
    RangeSortEarlyExitDetector detector         = new TwoWayInsertionEarlyExitDetector();
    SinglePivotSelector        selector         = new MiddleElementSelector();
    int                        janitorThreshold = 64;
    RangeSorter                janitor          = new InsertionSort2Way();
    String header = "Input";
    
    for (PartitionerSet set : partitionerSets) {
      ZeroDelta489Partitioner z489
        = new ZeroDelta489Partitioner
              (nix, set.getLeftExpander(), set.getRightExpander());
      SampleSizer sqr = new SquareRootSampleSizer();
      SampleCollector pos = new PositionalSampleCollector();
      SinglePivotPartitioner party 
        = set.getCentrePartitioner();
      selector              = new DirtyTheoreticalSelector
                                  (0.5, sqr, pos, z489  );
      janitor               = janitorPicker.chooseJanitor(party);
      janitorThreshold      = janitorPicker.getJanitorThreshold(party);
      QuicksortAdaptive qa2 = new QuicksortAdaptive
                                ( selector, party, janitor
                                , janitorThreshold, heap, detector);
      sorts.add(qa2);
      header += "\t" + shortName(set.toString());
    }
    System.out.println("Adaptive quicksorts " 
      + "(using, e.g. " + selector.toString() + ", as the selector"
      + ", " + janitor.toString() + " as the janitor, and"
      + "a janitor threshold of " + janitorThreshold + ")" 
      + ", for inputs of " + maxCount + " integers");
    System.out.println(header);
    runSortsOnSortedData(sorts, unit);
    runSortsOnDuplicatedData(sorts, unit);
  }
  
  
  public void testMultiplePivotPartitioners
    ( ArrayList<FixedCountPivotPartitioner> partitioners
    , boolean degenerate) {
    RangeSorter            iSort = new InsertionSort2Way();
    SortList sorts = new SortList();
    String header = "Input";
    for (FixedCountPivotPartitioner par : partitioners) {
      int pivots = par.getPivotCount();
      CleanMultiPivotSelector sel 
        = new CleanMultiPivotPositionalSelector( (pivots+1)*3 - 1);
      AdaptiveMultiPivotQuicksort qa2 
        = new AdaptiveMultiPivotQuicksort(sel, par, iSort, 64);
      sorts.add(qa2);
      header += "\t" + par.toString();
    }
    if (degenerate) {
      System.out.println("Adaptive multi-pivot quicksorts " 
        + "(using HalfAssedHeapsort as the janitor, and a janitor threshold of 64)"
        + ", for inputs of " + maxCount + " integers");
      System.out.println(header);
      runSortsOnSortedData(sorts, "second");
      runSortsOnDuplicatedData(sorts, "second");
    } else {
      System.out.println(header);
      sortTest.testRandomInputs(sorts, 20, 100 * SortTest.million, false);
    }
  }	
  @Test
  public void testSinglePivotPartitioners() {
    testSinglePivotPartitioners
    ( (new PartitionerTest()).getGoodPartitioners(), true);
  }
  @Test
  public void testConventionalSinglePivotPartitioners() {
    maxCount=1048576;
    runCount=1;
    NamedArrayList<SinglePivotPartitioner> partitioners
      = new NamedArrayList<SinglePivotPartitioner>();
    partitioners.add("Lomuto",         new LomutoPartitioner());
    partitioners.add("HolierThanThou", new HolierThanThouPartitioner());
    partitioners.add("Singleton",      new SingletonPartitioner());
    partitioners.add("Hoyos",          new HoyosPartitioner());
    partitioners.add("Centripetal",    new CentripetalPartitioner());
    testSinglePivotPartitioners(partitioners, true);
  }
  @Test
  public void testBranchAvoidingSinglePivotPartitioners() {
    runCount=1;
    NamedArrayList<SinglePivotPartitioner> partitioners 
      = new NamedArrayList<SinglePivotPartitioner>();
    partitioners.add("Tuned",    new TunedPartitioner());
    partitioners.add("Skippy",   new SkippyPartitioner());
    partitioners.add("PDQ",      new PDQPartitioner());
    partitioners.add("Flower",   new FlowerArrangementPartitioner());
    partitioners.add("Fatal-Flower", new FatalisticFloristPartitioner());
    partitioners.add("Skippy-C",  new SkippyCentripetalPartitioner());
    tp.warmUpPartitioners(partitioners.getArrayList());
    testSinglePivotPartitioners(partitioners, true);
  }
  @Test
  public void testBranchAvoidingSinglePivotPartitionersAgain() {
    //Penguin: new test, but using theoretical selectors based on
    //expanders rather than partitioners.
    SampleCollector nix = new NullSampleCollector();
    NamedArrayList<SinglePivotPartitioner> partitioners
      = new NamedArrayList<SinglePivotPartitioner>();
    partitioners.add("Tuned",        new TunedPartitioner());
    partitioners.add("Skippy",       new SkippyPartitioner());
    partitioners.add("PDQ",          new PDQPartitioner());
    partitioners.add("Flower",       new FlowerArrangementPartitioner());
    partitioners.add("Fatal-Flower", new FatalisticFloristPartitioner());
    partitioners.add("Skippy-C",      new SkippyCentripetalPartitioner());
    tp.warmUpPartitioners(partitioners.getArrayList());
    testSinglePivotPartitioners
      ( partitioners, true, new SelectorPicker() {
      public SinglePivotSelector chooseSelector
        ( SinglePivotPartitioner par ) {
        PartitionExpander expo 
          = new PartitionerToExpander( par);
        ZeroDelta489Partitioner z489 
          = new ZeroDelta489Partitioner( nix, expo, expo);
        SquareRootSampleSizer sqr 
          = new SquareRootSampleSizer(1);
        SampleCollector pos 
          = new PositionalSampleCollector();
        return new DirtyTheoreticalSelector(.5, sqr, pos, z489 );
      }
    } , new JanitorPicker()
      , (maxCount<1048576) ? "millisecond" : "second");
  }
  @Test  
  public void testSinglePivotExpanders() {
    ArrayList<PartitionerSet> partitionerSets 
      = new ArrayList<PartitionerSet>();
    partitionerSets.add(PartitionerSet.SINGLETON); 
    partitionerSets.add(PartitionerSet.LOMUTO);
    partitionerSets.add(PartitionerSet.TUNED);
    partitionerSets.add(PartitionerSet.SKIPPY);
    partitionerSets.add(PartitionerSet.SKIPPY_CENTRIPETAL);
    partitionerSets.add(PartitionerSet.FLOWER);
    tp.warmUpPartitionerSets(partitionerSets);
    testPartitionerSets(partitionerSets, new JanitorPicker()
      , (maxCount<1048576) ? "millisecond" : "second");
  }
  public void testBranchAvoidingPartitionersWithCombsort() {
    //Penguin: new test, but using theoretical selectors based on
    //expanders rather than partitioners.
    SampleCollector nix = new NullSampleCollector();
    NamedArrayList<SinglePivotPartitioner> partitioners 
      = new NamedArrayList<SinglePivotPartitioner>();
    partitioners.add("Skippy", new SkippyPartitioner());	    
    partitioners.add("Skippy-C", new SkippyCentripetalPartitioner());
    tp.warmUpPartitioners(partitioners.getArrayList());
    testSinglePivotPartitioners
      ( partitioners, true, new SelectorPicker() {
      public SinglePivotSelector chooseSelector
        ( SinglePivotPartitioner par ) {
        PartitionExpander expo 
          = new PartitionerToExpander( par);
        ZeroDelta489Partitioner z489 
          = new ZeroDelta489Partitioner( nix, expo, expo);
        SampleSizer sqr 
          = new SquareRootSampleSizer(1);
        SampleCollector pos 
          = new PositionalSampleCollector();
        return new DirtyTheoreticalSelector(.5, sqr, pos, z489);
      }
    }, new JanitorPicker() {
      public RangeSorter chooseJanitor
        ( SinglePivotPartitioner par ) {
        return new BranchAvoidingAlternatingCombsort();
      }
      public int getJanitorThreshold
        ( SinglePivotPartitioner par ) {
        return 512;
      }
    }, (maxCount<1048576) ? "millisecond" : "second");	  
  }
  public void testCombsortAllIn() {
   SortList sorts = new SortList();
   sorts.add(new BranchAvoidingAlternatingCombsort());
   sorts.add(new IntegerTimsortWrapper());
   runSortsOnSortedData(sorts, "second");	  
  }
  public void testKangarooPartitionersOnDegenerateInputs() {
    NamedArrayList<SinglePivotPartitioner> partitioners
      = new NamedArrayList<SinglePivotPartitioner>();
    partitioners.add("Hoyos",       new HoyosPartitioner());
    partitioners.add("Skippy",      new SkippyPartitioner());
    partitioners.add("Centripetal", new CentripetalPartitioner());
    partitioners.add("Skippy-C",    new SkippyCentripetalPartitioner());
    testSinglePivotPartitioners(partitioners, true);
  }
  @Test
  public void testBranchAvoidingPartitionersOnDegenerateInputs() {
    maxCount = 65536;
    runCount = 10;
    NamedArrayList<SinglePivotPartitioner> singles
      = new NamedArrayList<SinglePivotPartitioner>();
    singles.add("Centripetal",   new CentripetalPartitioner()); //reference partitioner
    singles.add("PDQ",           new PDQPartitioner());
    singles.add("PDQ(JF)(64)",   new JavaFriendlyPDQPartitioner(64));
    singles.add("PDQ(JF)(1024)", new JavaFriendlyPDQPartitioner(1024));
    singles.add("Flower",        new FlowerArrangementPartitioner());
    singles.add("Lomuto",        new LomutoPartitioner());
    singles.add("Tuned",         new TunedPartitioner());
    singles.add("BalancedKangaroo", new BalancedSkippyPartitioner());
    tp.warmUpPartitioners(singles.getArrayList());
    testSinglePivotPartitioners(singles, true);
  }
  public void testKangarooPartitionersOnDegenerateInputsPart2() {
    maxCount = 65536;
    runCount = 100;
    NamedArrayList<SinglePivotPartitioner> singles
      = new NamedArrayList<SinglePivotPartitioner>();
    singles.add("Skippy-C",    new SkippyCentripetalPartitioner());
    singles.add("Skippy",      new SkippyPartitioner());    
    singles.add("Tuned",       new TunedPartitioner());		
    singles.add("PDQ",         new PDQPartitioner());
    singles.add("PQD(JF)(64)", new JavaFriendlyPDQPartitioner(64));		
    tp.warmUpPartitioners(singles.getArrayList());
    testSinglePivotPartitioners(singles, true);		
    ArrayList<FixedCountPivotPartitioner> partitioners
      = new ArrayList<FixedCountPivotPartitioner>();
	partitioners.add(new SkippyPartitioner2());
    partitioners.add(new SkippyPartitioner3());
    mpt.warmUpPartitioners(partitioners);
    testMultiplePivotPartitioners(partitioners, true);
  }
  public void testBranchAvoidingPartitionersOnLargeInputs() {
    NamedArrayList<SinglePivotPartitioner> partitioners
      = new NamedArrayList<SinglePivotPartitioner>();
    partitioners.add("Centripetal", new CentripetalPartitioner());
    partitioners.add("Tuned",       new TunedPartitioner());		
    partitioners.add("PDQ",         new PDQPartitioner());
    partitioners.add("PDQ(JF)(64)", new JavaFriendlyPDQPartitioner(64));
    tp.warmUpPartitioners(partitioners.getArrayList());
    testSinglePivotPartitioners(partitioners, false);
  }
  public void testKangarooPartitionersOnLargeInputs() {
    NamedArrayList<SinglePivotPartitioner> partitioners
      = new NamedArrayList<SinglePivotPartitioner>();
    partitioners.add("Hoyos",    new HoyosPartitioner());
    partitioners.add("Skippy",   new SkippyPartitioner());
    partitioners.add("Centripetal", new CentripetalPartitioner());
    partitioners.add("Skippy-C", new SkippyCentripetalPartitioner());
    partitioners.add("Tuned",    new TunedPartitioner());		
    partitioners.add("PDQ",      new PDQPartitioner());
    tp.warmUpPartitioners(partitioners.getArrayList());
    testSinglePivotPartitioners(partitioners, false);
  }
  public SinglePivotSelector middleSelector
    ( PartitionExpander lx, PartitionExpander rx) {
    SampleSizer sizer 
      = new SquareRootSampleSizer(0, 1/ Math.log(2.0));
    SampleCollector nix = new NullSampleCollector();
    Algorithm489Partitioner a489 
      = new Algorithm489Partitioner(nix, lx, rx);
    return new DirtyCompoundSelector
               (sizer, nix , a489);
  }
  public void testKangarooPartitionersOnLargeInputs2() {
    SinglePivotSelector    lefty      = new CleanLeftHandedSelector(false);
    SinglePivotPartitioner hoyos      = new HoyosPartitioner();
    SinglePivotPartitioner centipede  = new CentripetalPartitioner();
    SinglePivotPartitioner tuned      = new TunedPartitioner();
    SinglePivotPartitioner roo        = new SkippyPartitioner(); 
    RangeSorter            heapsort   = new TwoAtATimeHeapsort();
    RangeSorter            isort      = new InsertionSort2Way();
    LeftTunedExpander      ltx        = new LeftTunedExpander();
    RightTunedExpander     rtx        = new RightTunedExpander();
    LeftSkippyExpander   lkx        = new LeftSkippyExpander();
    RightSkippyExpander  rkx        = new RightSkippyExpander();
    
    SinglePivotSelector    middleTune = middleSelector(ltx, rtx);
    SinglePivotSelector    middleRoo  = middleSelector(lkx, rkx);
    RangeSortEarlyExitDetector detector = new TwoWayInsertionEarlyExitDetector();
    SortList sorts = new SortList();
    sorts.add(new QuicksortAdaptive( lefty,      hoyos,     isort, 150, heapsort, detector ));
    sorts.add(new QuicksortAdaptive( lefty,      centipede, isort, 150, heapsort, detector ));
    sorts.add(new QuicksortAdaptive( middleTune, tuned,     isort, 64,  heapsort, detector ));
    sorts.add(new QuicksortAdaptive( middleRoo,  roo,       isort, 64,  heapsort, detector ));
    sortTest.warmUpSorts(sorts);
    System.out.println("n\tHoyos\tCentripetal\tTuned\tSkippy");
    sortTest.testRandomInputs(sorts, 20, 100 * SortTest.million, false);
  }
  public void testAFP() {
    int[] a = random.randomPermutation(1000);
    AnimalFarmPartitioner afp = new AnimalFarmPartitioner();
    afp.multiPartitionRange(a,  0, 1000, new int[] { 500 } );
  }
  @Test
  public void testDHP() {
    int[] vInput = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19 };
    int n = vInput.length;
    HoyosDutchPartitioner dhp = new HoyosDutchPartitioner(); 
    int[] vCorrect = Arrays.copyOf(vInput, n);
    Arrays.sort(vCorrect);
    DumpRangeHelper.dumpArray("correct", vCorrect);
    CheckedMultiPivotPartitioner cdhp = new CheckedMultiPivotPartitioner(dhp);
    for (int pivotIndex=0; pivotIndex<n; ++pivotIndex) {
      int[] vOutput = Arrays.copyOf(vInput, n);
      cdhp.multiPartitionRange(vOutput,0, n, new int[] { pivotIndex } );
    }
  }
  
  @Test
  public void testThreePartitionSorts() {
    maxCount                              = 65535; //1M 
    runCount                              = 25;
    SinglePivotPartitioner     hoyos      = new SingletonPartitioner();
    FixedCountPivotPartitioner multiHoyos = new SingleToMultiPartitioner(hoyos);
    SortList                   sorts      = new SortList();    
    MultiPivotPartitioner[]    inertial   = new InertialPartitioner[4];
    for (int i=0; i<inertial.length; ++i) {
      inertial[i] = new InertialPartitioner(i);
    }    
    MultiPivotSelector any5 = new SamplingMultiPivotSelector(5, false);
    sorts.setSelector(any5);
    
    //Built, from the bottom up, as 3-way partitioner
    sorts.addMPQ ( bentley );
    sorts.addMPQ ( oBentley );
    sorts.addMPQ ( new DutchNationalFlagPartitioner() );
    for (int i=0; i<inertial.length; ++i) {
      sorts.addMPQ(inertial[i]);
    }
    sorts.addMPQ ( new YaroslavskiyDutchPartitioner() );
    sorts.addMPQ ( new HoyosDutchPartitioner() ); //Busted. Fix it!
    sorts.addMPQ ( new SkippyDutchPartitioner() );
    sorts.addMPQ ( new SlowCookedDutchPartitioner() );
    
    //2-way partitioners, converted into 3-way partitioners
    sorts.addMPQ ( new BirdsOfAFeatherPartitioner(multiHoyos) );
    sorts.addMPQ ( new BSortPartitioner(hoyos)  );
    sorts.addMPQ ( new QSortePartitioner(hoyos) );
    
    //pairs of 1-way partitioners, used in tandem
    sorts.add2PQ ( new LomutoPartitioner()
                 , new LomutoMirrorPartitioner() );
    sorts.add2PQ ( new SkippyPartitioner()
                 , new SkippyMirrorPartitioner() );
    
    //Baseline
    sorts.addAdaptiveSPQ(hoyos); 
    sorts.addAdaptiveSPQ(new SkippyCentripetalPartitioner());
        
    //2-pivot partitioner
    sorts.addMPQ(new SkippyPartitioner2());

    sorts.addMPQ ( new SkippyDutchPartitioner()      );
    sorts.addMPQ ( new SkippyPartitioner2());
    
    sorts.writeSortList();
    sorts.writeSortHeader    ( "Input" );
    sortTest.warmUpSorts     ( sorts );
    runSortsOnSortedData     ( sorts  , "millisecond" );
    System.out.println("");
    runSortsOnDuplicatedData ( sorts  , "millisecond" );
  }
  public void writeSortHeader ( String nameOfFirstColumn
                              , List<RangeSorter> sorts) {
    String header = nameOfFirstColumn;
    for ( RangeSorter s : sorts) {
      header += "\t" + s.toString();
    }
    System.out.println(header);	  
  }
  @Test
  public void testTwoPartitionerSorts() {
    maxCount = 65536;
    runCount = 25;
    SortList sorts = new SortList();
    SinglePivotPartitioner[] bothLomutos   = new SinglePivotPartitioner[] 
    		{ new LomutoPartitioner(), new LomutoMirrorPartitioner() };
    SinglePivotPartitioner[] bothKangas    = new SinglePivotPartitioner[] 
    		{ new SkippyPartitioner(), new SkippyMirrorPartitioner() };
    SinglePivotPartitioner[] inOut         = new SinglePivotPartitioner[] 
    		{ new SingletonPartitioner(), new CentripetalPartitioner() };
    SinglePivotPartitioner[] inOutNoBranch = new SinglePivotPartitioner[] 
    		{ new FlowerArrangementPartitioner(), new SkippyCentripetalPartitioner() };
    for (SinglePivotPartitioner party: new SinglePivotPartitioner[] 
    		{ new SkippyPartitioner(), new SkippyMirrorPartitioner()
    		, new FlowerArrangementPartitioner(), new SkippyCentripetalPartitioner() } ) {
      sorts.add(new QuicksortAdaptive ( new CleanTheoreticalSelector(0.5)
    		                          , party, new BranchAvoidingAlternatingCombsort()
    		                          , 512, new TwoAtATimeHeapsort()
    		                          , new TwoWayInsertionEarlyExitDetector() ));
    }
    sorts.add(new QuicksortTwoPartitioner(bothLomutos));
    sorts.add(new QuicksortTwoPartitioner(bothKangas));
    sorts.add(new QuicksortTwoPartitioner(inOut));
    sorts.add(new QuicksortTwoPartitioner(inOutNoBranch));
    writeSortHeader("Input", sorts);
    sortTest.warmUpSorts(sorts);
    sortTest.testSpecificSorts(sorts, 10, 10000, 100, 10000000, 25);
    System.out.println();
    writeSortHeader("Input", sorts);
    runSortsOnSortedData     ( sorts  , "millisecond" );
    System.out.println("");
    runSortsOnDuplicatedData ( sorts  , "millisecond" );
  }
  @Test 
  public void testExpansionistQuicksort() {
    maxCount = 65536;
    runCount = 25;
    SortList sorts = new SortList();
    SinglePivotPartitioner[] bothKangas    = new SinglePivotPartitioner[] 
        { new SkippyPartitioner(), new SkippyMirrorPartitioner() };
    sorts.add(new QuicksortTwoPartitioner(bothKangas), "Q2P");
    sorts.add(new ExpansionistQuicksort(), "XQ");
    ExpansionistQuicksort normativeQuicksort
      = new ExpansionistQuicksort();
    normativeQuicksort.setSamplePartitioner
      ( new NormativeSamplePartitioner() );
    sorts.add(normativeQuicksort);
    sortTest.warmUpSorts(sorts);
    writeSortHeader("Input", sorts);
    sortTest.testSpecificSorts(sorts, 10, 10000, 1000, 1000000, 100);
    System.out.println();
    writeSortHeader("Input", sorts);
    runSortsOnSortedData     ( sorts  , "millisecond" );
    System.out.println("");
    runSortsOnDuplicatedData ( sorts  , "millisecond" );
  }  
  public void testDuplicateFriendlySort() {
    SortList sorts = new SortList();
    sortTest.warmUpSorts(sorts);
    RangeSorter mpq = new MultiPivotQuicksort 
                      ( new Java8Selector(), new Java8Partitioner()
                      , new PairInsertionSort(), 47);
    sorts.add(mpq);    
    sorts.add(new DuplicateFriendlySort(mpq));
    String line = "";
    for (RangeSorter s : sorts) {
      line += "\t" + s.toString();
    }
    System.out.println(line);
    runSortsOnDuplicatedData(sorts, "second");
    System.out.println("");
  }
  @Test
  public void testMultithreadedQuicksort() {
    SortList sorts = new SortList();
    MultiPivotSelector     mpps  
      = new SamplingMultiPivotSelector(8, new int[] { 2, 4 }, false);
    RangeSorter isort2 = new InsertionSort2Way();
    YaroslavskiyPartitioner2 yaro2 = new YaroslavskiyPartitioner2();
    sorts.add(new AdaptiveMultiPivotQuicksort(mpps, yaro2, isort2, 27));
    for (int mttThreshold = 16384; mttThreshold > 128; mttThreshold /= 2) {
      sorts.add(new MultiThreadedQuicksort(mpps, yaro2, isort2, 27, mttThreshold));
    }    
    runSortsOnSortedData(sorts, "second");
    runSortsOnDuplicatedData(sorts, "second");
  }
  @Test
  public void testJavaMultithreadedQuicksort() {
    SortList sorts = new SortList();
    MultiPivotSelector     mpps  
      = new SamplingMultiPivotSelector(8, new int[] { 2, 5 }, false);
    RangeSorter              psort = new PairInsertionSort();
    MultiPivotPartitioner    yaro2 = new YaroslavskiyPartitioner2();
    MultiPivotPartitioner    kanga = new SingleToMultiPartitioner(new SkippyPartitioner());
    RangeSorter              comb  = new BranchAvoidingAlternatingCombsort();
    
    sorts.add(new AdaptiveMultiPivotQuicksort(mpps, yaro2, psort, 27));
    sorts.add(new Java8ParallelSorter());   
    sorts.add(new MultiThreadedQuicksort(mpps, yaro2, psort, 27,  256));
    sorts.add(new MultiThreadedQuicksort(mpps, kanga, comb,  256, 256)); 
    sorts.warmUp();
    runSortsOnSortedData     ( sorts, "millisecond");
    runSortsOnDuplicatedData ( sorts, "millisecond");
  }
  @Test
  public void testHolierThanThouPartitioners() {
    SortList sorts = new SortList();
    RangeSorter janitor = new InsertionSort2Way(); 
    for (int i=1;i<9;++i) {
      MultiPivotSelector    sel = getRawSelector(i);
      MultiPivotPartitioner hpp = getHpp(i);
      int                   threshold  = 64*i;
      janitor = sorts.addAdaptiveMPQ(sel, hpp, threshold, janitor);
    }
    sortTest.warmUpSorts(sorts);
    sortTest.testSpecificSorts(sorts, 100, 10000, 25, 10000000, 5);
    runSortsOnSortedData(sorts, "second");
    runSortsOnDuplicatedData(sorts, "second");
  }
  private MultiPivotPartitioner getHpp(int i) {
    switch (i) {
      case 1: break; //see below 
      case 2: return new HolierThanThouPartitioner2();
      case 3: return new HolierThanThouPartitioner3();
      case 4: return new HolierThanThouPartitioner4();
      case 5: return new HolierThanThouPartitioner5();
      case 6: return new HolierThanThouPartitioner6();
      case 7: return new HolierThanThouPartitioner7();
      case 8: return new HolierThanThouPartitioner8();
    }
    return new SingleToMultiPartitioner(new HolierThanThouPartitioner());
  }
  private MultiPivotSelector getRawSelector(int i) {
    return new CleanMultiPivotPositionalSelector(i);
  }
  @Test
  public void testCentrifugalPartitioners() {
    SortList sorts = new SortList();
    sorts.addAdaptiveMPQ(new CentrePivotPartitioner());
    sorts.addAdaptiveMPQ(new CentrifugalPartitioner2());
    sorts.addAdaptiveMPQ(new CentrifugalPartitioner3());
    sorts.addAdaptiveMPQ(new CentrifugalPartitioner4());
	  sorts.addAdaptiveMPQ(new CentrifugalPartitioner5());
    sorts.addAdaptiveMPQ(new CentrifugalPartitioner6());
    sorts.addAdaptiveMPQ(new CentrifugalPartitioner7());
    sortTest.warmUpSorts(sorts);
    sortTest.testSpecificSorts(sorts, 100, 10000, 400, 10000000, 25);
    runSortsOnSortedData(sorts, "second");
    runSortsOnDuplicatedData(sorts, "second");
  }
  @Test
  public void testCentripetalPartitioners() {
    SortList sorts = new SortList();
    sorts.addAdaptiveMPQ(new CentripetalPartitioner());
    sorts.addAdaptiveMPQ(new CentripetalPartitioner2());
    sorts.addAdaptiveMPQ(new SkippyCentripetalPartitioner());
    sorts.addAdaptiveMPQ(new SkippyCentripetalPartitioner2());
    sortTest.warmUpSorts(sorts);
//  sortTest.testSpecificSorts(sorts, 100, 10000, 400, 10000000, 25);
    runSortsOnSortedData(sorts, "second");
    runSortsOnDuplicatedData(sorts, "second");
  }
  @Test
  public void testCentripetalPartitionersUniform() {
    SortList sorts = new SortList();
    sorts.setSelector(new SamplingMultiPivotSelector(23, false));
    sorts.addAdaptiveMPQ(new CentripetalPartitioner());
    sorts.addAdaptiveMPQ(new CentripetalPartitioner2());
    sorts.addAdaptiveMPQ(new SkippyCentripetalPartitioner());
    sorts.addAdaptiveMPQ(new SkippyCentripetalPartitioner2());
    sortTest.warmUpSorts(sorts);
//  sortTest.testSpecificSorts(sorts, 100, 10000, 400, 10000000, 25);
    runSortsOnSortedData(sorts, "second");
    runSortsOnDuplicatedData(sorts, "second");
  }
  public void runSortsOnOtherDegenerateData(List<RangeSorter> sorts, double growthrate, boolean showHeader) {
    final double million = 1000000;
    final double billion = 1000000000;
    if (showHeader) {
      String header = "Type";
      for (RangeSorter sort : sorts) {
        header += "\t" + sort.toString().toLowerCase();
      }
      System.out.println(header);
    }
    DegenerateInputType type = DegenerateInput.firstType();
    for (;;) {
      switch (type) {
        case DUPLICATED:
        case IN_ORDER:
        case REVERSE_ORDER:
        case RANDOM:
          break;

        default:
          for (int d = 2; d < maxCount; d += (d<1/growthrate) ? 1 : d*(growthrate-1) ) {
            double timing[] = new double[sorts.size()];
            for (int run = 0; run < runCount; ++run) {
              int sortNum = 0;
              int input[] = degen.degeneratePermutation(type, maxCount, d);
              for (RangeSorter sort : sorts) {
                int copy[] = Arrays.copyOf(input, input.length);
                long startTime = System.nanoTime();
                sort.sortRange(copy, 0, copy.length);
                long stopTime = System.nanoTime();
                timing[sortNum] += (stopTime - startTime) / billion / runCount;
                ++sortNum;
              }
            }
            String line = type.name().toLowerCase() + d;
            for (int sortNum = 0; sortNum < sorts.size(); ++sortNum) {
              line += "\t" + (double) Math.floor(timing[sortNum] * million) / million;
            }
            System.out.println(line);
          }
          break;
      }
      if (type == DegenerateInput.lastType()) {
        break;
      }
      type = type.getNext();
    }
  }
  public void runOneSortOnDifferentSortsOfDegenerateData(RangeSorter sort, double growthrate) {
	DegenerateInputType[] types = new DegenerateInputType[] 
      { DegenerateInputType.DUPLICATED
      , DegenerateInputType.APPENDED,        DegenerateInputType.EXCHANGED
      , DegenerateInputType.UPDATED,         DegenerateInputType.TIMESTAMPED
      , DegenerateInputType.BOTTOM_SHUFFLED, DegenerateInputType.SAWTOOTH
      };
	String header="d";
    for (int t=0; t < types.length; ++t) {
	  header += "\t" + types[t].name().toLowerCase();
    }
    System.out.println(header);
    sortTest.warmUpSort(sort, maxCount, 2);
    for (int d = 2; d < maxCount; d += (int) Math.ceil ( d*(growthrate-1) ) ) {
      double[] timing = new double[types.length];
      for (int run = 0; run < runCount; ++run) {
        for (int t=0; t < types.length; ++t) {
          int input[] = degen.degeneratePermutation(types[t], maxCount, d);
          long startTime = System.nanoTime();
          sort.sortRange(input, 0, input.length);
          long stopTime = System.nanoTime();
          timing[t] += (stopTime - startTime);
        }
      }
      String line = "" + d;
      for (int t=0; t < types.length; ++t) {
        line += "\t" + (double) Math.floor(timing[t] / runCount / 1000.0 ) / 1000.0;
      }
      System.out.println(line);
    }	
  }
  public void runSortsOnDifferentSortsOfDegenerateData
    ( SortList sorts
    , int count, int runsToDo
    , double growthRate) {
    DegenerateInputType[] types 
    = new DegenerateInputType[] 
      { DegenerateInputType.DUPLICATED
      , DegenerateInputType.APPENDED,        DegenerateInputType.EXCHANGED
      , DegenerateInputType.UPDATED,         DegenerateInputType.TIMESTAMPED
      , DegenerateInputType.BOTTOM_SHUFFLED, DegenerateInputType.SAWTOOTH
      };
    sortTest.warmUpSorts(sorts);    
    for (int t=0; t < types.length; ++t) {
      System.out.println(types[t].name().toLowerCase());
      sorts.writeSortHeader("d");
      for (int d = 2; d < count; 
           d += (int) Math.ceil ( d*(growthRate-1) ) ) {
        double[] timing = new double[sorts.size()];
        for (int run = 0; run < runsToDo; ++run) {
          int input[] = degen.degeneratePermutation(types[t], count, d);
          for (int s=0; s < sorts.size(); ++s) {
            RangeSorter sort = sorts.get(s);
            int[] copy = Arrays.copyOf(input, count);
            long startTime = System.nanoTime();
            sort.sortRange(copy, 0, copy.length);
            long stopTime = System.nanoTime();
            timing[s] += (stopTime - startTime);
          }
        }
        String line = "" + d;
        for (int s=0; s < sorts.size(); ++s) {
          line += "\t" + (double) Math.floor(timing[s] / runsToDo / 1000.0 ) / 1000.0;
        }
        System.out.println(line);
      }
      System.out.println();
    } 
  }
  
  
  public void testSingletonOnDegenerateData() {
	maxCount = 65536;
	runCount = 25;
    runOneSortOnDifferentSortsOfDegenerateData 
      ( new QuicksortGovernor ( new MiddleElementSelector()
                              , new SingletonPartitioner()
    		                  , new InsertionSort2Way(), 64), 1.1);
  }

  public void runSortsOnDuplicatedData
    ( SortList sorts, String unit ) {
    int latin = 0;
    double divisor = billion;
    if (unit.equals("millisecond")) divisor = million;
    if (unit.equals("microsecond")) divisor = thousand;
    for (int d = 1; d < maxCount; d = ((d < 10) ? (d + 1) : (d * 11 / 10))) {
      double totalTime[] = new double[sorts.size()];
      for (int run = 0; run < runCount; ++run) {
        int input[]   = degen.permutationOfDupes(maxCount, d);
        int correct[] = Arrays.copyOf(input, maxCount);
        Arrays.sort(correct);
        int copy[] = new int[maxCount];
        for (int s = 0; s < sorts.size(); ++s) {
          int sort = (s + latin) % sorts.size();
          RangeSorter sorter = sorts.get(sort);
          for (int i = 0; i < maxCount; ++i) {
            copy[i] = input[i];
          }
          long startTime = System.nanoTime();
          sorter.sortRange(copy, 0, copy.length);
          long stopTime = System.nanoTime();
          totalTime[sort] += stopTime - startTime;
          for (int i=0; i<maxCount; ++i) {
            if ( copy[i]!=correct[i]) {
              if ( maxCount <= 100 ) {
                DumpRangeHelper.dumpArray("input  ", input);
                DumpRangeHelper.dumpArray("output ", copy);
                DumpRangeHelper.dumpArray("correct", correct);
              }
              throw new SortingFailureException("Sort " + (sort+1)
                + " (" + sorts.getSortName(sort) + ") failed on run " + (run+1)
                + " (of " + runCount + ") on " + 
                + d + "-distinct=value input of " + maxCount + " integers\n"
                + "Item [" + i + "] of output should have been " + correct[i]
                + " but was " + copy[i]);
            }
          }
        }
        --latin;
        if (latin < 0) {
          latin += sorts.size();
        }
      }
      String s = "Dupes" + Integer.toString(d);
      for (int i = 0; i < sorts.size(); ++i) {
        s += "\t" + Math.floor( (double) totalTime[i] 
                                / runCount / divisor * 1000000.0 + .5)
             / 1000000.0;
      }
      System.out.println(s);
    }
  }
  public void runSortsOnSortedData
    ( List<RangeSorter> sorts, String unit ) {
    int latin = 0;
    int d = 1;
    ArrayList<Integer> x = new ArrayList<Integer>();
    double divisor1 = thousand;
    double divisor2 = million;
    if (unit.equals("second"))      divisor2 = million;
    if (unit.equals("millisecond")) divisor2 = thousand;
    if (unit.equals("microsecond")) {
      divisor1= 1;
      divisor2 = thousand; 
    }
    BranchAvoidingAlternatingCombsort baac = new BranchAvoidingAlternatingCombsort();
    for (int rev = 1; rev >= 0; --rev) {
      while (1 <= d && d <= maxCount) {
        double totalTime[] = new double[sorts.size()];
        for (int run = 0; run < runCount; ++run) {
          int[] input     = degen.postRandomUpdatePermutation(maxCount, d);
          int[] correct   = Arrays.copyOf(input, maxCount);
          int[] output    = new int[maxCount];
          baac.sortRange(correct, 0, correct.length);
          for (int s = 0; s < sorts.size(); ++s) {
            int sort = (s + latin) % sorts.size();
            RangeSorter sorter = sorts.get(sort);
            for (int i = 0; i < maxCount; ++i) {
              output[i] = rev != 0 ? input[maxCount - 1 - i] : input[i];
            }
            long startTime = System.nanoTime();
            sorter.sortRange(output, 0, output.length);
            long stopTime = System.nanoTime();
            totalTime[sort] += stopTime - startTime;
            for (int i=0; i<maxCount; ++i) {
              if (output[i]!=correct[i]) {
                if (maxCount <= 100 ) {
                  DumpRangeHelper.dumpArray("input", input);
                  DumpRangeHelper.dumpArray("output", output);
                }
                throw new SortingFailureException("Sort " + sort
                  + " (" + sorter.toString() + ") failed on run " + (run+1)
                  + " (of " + runCount + ") on " + (rev==1 ? "Rev" : "Fwd")
                  + d + " input of " + maxCount + " integers\n"
                  + "Item [" + i + "] of output should have been " + correct[i]
                  + " but was " + output[i]);  
              }
            }
          }
          --latin;
          if (latin < 0) {
            latin += sorts.size();
          }
        }        
        String s = ((rev != 0)
                 ? "Backward" 
                 : "Forward");
        s += Integer.toString(d);
        for (int i = 0; i < sorts.size(); ++i) {
           s += "\t" + Math.floor(((double) totalTime[i] / runCount) 
                       / divisor1) / divisor2;
        } 
        System.out.println(s);
        if (rev != 0) {
          x.add(d);
          if (d == maxCount) {
            break;
          }
          d = ((d < 10) ? (d + 1) : (d * 11 / 10));
          if (d > maxCount) {
            d = maxCount;
          }
        } else {
          if (x.isEmpty()) {
            break;
          }
          d = x.get(x.size() - 1);
          x.remove(x.size() - 1);
        }
      }
    }
  }
  public void runSortsOnPretendSortedData(List<RangeSorter> sorts) {
    //
    // If there are s sorts, the first column describes the input
    // the next s columns are "post-random-update" the next s columns
    // are "post-random-exchange", and the last s columns are
    // "post-random-pairwise-exchange".
    //
    int latin = 0;
    int d = 1;
    ArrayList<Integer> x = new ArrayList<Integer>();
    for (int rev = 1; rev >= 0; --rev) {
      while (1 <= d && d <= maxCount) {
        double totalTime[] = new double[3 * sorts.size()];
        for (int run = 0; run < runCount; ++run) {
          int input[] = degen.postRandomUpdatePermutation(maxCount, d);
          int copy[] = new int[maxCount];
          for (int s = 0; s < 3 * sorts.size(); ++s) {
            int sort = (s + latin) % sorts.size();
            RangeSorter sorter = sorts.get(sort);
            for (int i = 0; i < maxCount; ++i) {
              copy[i] = rev != 0 ? input[maxCount - 1 - i] : input[i];
            }
            long startTime = System.nanoTime();
            sorter.sortRange(copy, 0, copy.length);
            long stopTime = System.nanoTime();
            sort += (s / sorts.size()) * sorts.size();
            totalTime[sort] += stopTime - startTime;
            if (s == sorts.size()) {
              input = degen.postRandomExchangePermutation(maxCount, d);
            } else if (s == sorts.size() * 2) {
              input = degen.postRandomPairSwapPermutation(maxCount, d);
            }
          }
          --latin;
          if (latin < 0) {
            latin += sorts.size();
          }
        }
        String s = ((rev != 0) ? "Backward" : "Forward") + Integer.toString(d);
        for (int i = 0; i < 3 * sorts.size(); ++i) {
          s += "\t" + Math.floor(((double) totalTime[i] / runCount) / million * 100.0) / 100.0;
        }
        System.out.println(s);
        if (rev != 0) {
          x.add(d);
          if (d == maxCount) {
            break;
          }
          d = ((d < 10) ? (d + 1) : (d * 11 / 10));
          if (d > maxCount) {
            d = maxCount;
          }
        } else {
          if (x.isEmpty()) {
            break;
          }
          d = x.get(x.size() - 1);
          x.remove(x.size() - 1);
        }
      }
    }
  }
  public void runSortsOnTwoSortedListsTapedTogether
    ( List<RangeSorter> sorts ) {
    int latin = 0;
    double billion = 1000.0 * 1000.0 * 1000.0;
    double totalTime[] = new double[sorts.size()];
    for (int run = 0; run < 5; ++run) {
      int input[] = new int[maxCount];
      for (int i = 0; i < maxCount; ++i) {
        input[i] = i % ((maxCount+1)/2);
      }
      int copy[]  = new int[maxCount];
      for (int s = 0; s <= sorts.size(); ++s) {
        int sort = (s + latin) % sorts.size();
        for (int i = 0; i < maxCount; ++i) {
          copy[i] = input[i];
        }
        RangeSorter sorter = sorts.get(sort);
        long startTime = System.nanoTime();
        sorter.sortRange(copy, 0, copy.length);
        long stopTime = System.nanoTime();
        totalTime[sort] += stopTime - startTime;
      }
    }
    String text = "2-Taped";
    for (int i = 0; i < sorts.size(); ++i) {
      text += "\t" + Math.floor(((double) totalTime[i] / runCount) / billion * 100000.0) / 100000.0;
    }
    System.out.println(text);
  }
  protected double countInversions
    ( int[] vX, int start, int stop ) {
    //Assumes: vArray contains a permutation
    double inversionsCounted = 0;
    int[] vCopy= new int[stop-start];
    for (int i=start; i<stop; ++i) {
      vCopy[i-start] = vX[i];
    }
    stop -= start;
    start = 0;
    while (stop>start) {
      int v = vCopy[start];
      for (int scan=start+1; scan<stop; ++scan) {
        if (vCopy[scan]<v) {
          vCopy[scan-1] = vCopy[scan];
          ++inversionsCounted;
        } else {
          vCopy[scan-1] = v;
          v = vCopy[scan];
        }
      }
      --stop;
      vCopy[stop] = v;
    }
    return inversionsCounted;
  }
  public double countTAPE
    ( int[] vArray, int start, int stop ) {
    double TAPE = 0;
    for (int i=start; i<stop; ++i) {
      TAPE += (vArray[i] < i) ? (i-vArray[i]) : (vArray[i]-i);
    }
    return TAPE;
  }
  
  @Test
  public void testInversionsVersusTAPE() {
    int n = 1000;
    int runCount = 10000;
    double totalTape = 0;
    double totalInv  = 0;
    for (int r=0; r<runCount; ++r) {
      int bleh[] = random.randomPermutation(n);
      double tape = countTAPE(bleh, 0, bleh.length);
      double inv  = countInversions(bleh, 0, bleh.length);
      /*
      System.out.println("" + tape
            + "\t" + inv + "\t" + tape/inv);
      */
      totalTape += tape;
      totalInv += inv;
    }
    System.out.println("" + totalTape/runCount 
      + "\t" + totalInv/runCount 
      + "\t" + totalTape/totalInv );
  }
  public void testBleh() {
    int n=200;
    for (int i=0;i<10;++i) {
      int bleh[] = random.randomPermutation(n);
      double divisor = n * ( n - 1 );
      System.out.println("" + countInversions(bleh, 0, bleh.length)/divisor);
    }
    System.out.println("");
    for (int d=1;d<n;d+=(d<10)?1:(d/10)) {
      int bleh[] = degen.postRandomUpdatePermutation(n, d);
      System.out.println("" + d + "\t" + countInversions(bleh, 0, bleh.length));
    }
    System.out.println("");
    for (int d=1;d<n;d+=(d<10)?1:(d/10)) {
      int bleh[] = degen.postRandomUpdatePermutation(n, d);
      RangeSortHelper.reverseRange(bleh, 0, bleh.length);
      System.out.println("" + d + "\t" + countInversions(bleh, 0, bleh.length));
    }
  }    
}
