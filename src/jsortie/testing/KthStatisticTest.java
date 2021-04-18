package jsortie.testing;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import jsortie.exception.SortingFailureException;
import jsortie.helper.DumpRangeHelper;
import jsortie.helper.RangeSortHelper;
import jsortie.helper.SinglePivotPartitionHelper;
import jsortie.janitors.insertion.BinaryInsertionSort;
import jsortie.janitors.insertion.InsertionSort;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.PositionalSampleCollector;
import jsortie.quicksort.collector.RandomSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.collector.SampleOfMediansCollector;
import jsortie.quicksort.collector.external.ExternalPositionalCollector;
import jsortie.quicksort.collector.external.ExternalSampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.Repartitioner;
import jsortie.quicksort.expander.RotatingPartitionExpander;
import jsortie.quicksort.expander.adapter.PartitionerToExpander;
import jsortie.quicksort.expander.adapter.PretendExpander;
import jsortie.quicksort.expander.bidirectional.CentripetalExpander;
import jsortie.quicksort.expander.bidirectional.HoareExpander;
import jsortie.quicksort.expander.bidirectional.HoyosExpander;
import jsortie.quicksort.expander.bidirectional.ReverseSingletonExpander;
import jsortie.quicksort.expander.bidirectional.RevisedHoareExpander;
import jsortie.quicksort.expander.bidirectional.SingletonExpander;
import jsortie.quicksort.expander.branchavoiding.BalancedSkippyExpander;
import jsortie.quicksort.expander.branchavoiding.SkippyCentripetalExpander;
import jsortie.quicksort.expander.branchavoiding.SkippyExpander;
import jsortie.quicksort.expander.branchavoiding.LeftSkippyExpander;
import jsortie.quicksort.expander.branchavoiding.LeftTunedExpander;
import jsortie.quicksort.expander.branchavoiding.RightSkippyExpander;
import jsortie.quicksort.expander.branchavoiding.RightTunedExpander;
import jsortie.quicksort.expander.branchavoiding.TunedExpander;
import jsortie.quicksort.expander.unidirectional.HolierThanThouExpander;
import jsortie.quicksort.expander.unidirectional.LeftHTTExpander;
import jsortie.quicksort.expander.unidirectional.LeftLomutoExpander;
import jsortie.quicksort.expander.unidirectional.RightHTTExpander;
import jsortie.quicksort.expander.unidirectional.RightLomutoExpander;
import jsortie.quicksort.indexselector.RandomIndexSelector;
import jsortie.quicksort.indexselector.indexset.IndexSet;
import jsortie.quicksort.multiway.expander.DoubleLomutoExpander2;
import jsortie.quicksort.multiway.expander.MultiPivotPartitionExpander;
import jsortie.quicksort.multiway.expander.TunedExpander2;
import jsortie.quicksort.multiway.expander.centripetal.CentripetalExpander2;
import jsortie.quicksort.multiway.expander.holierthanthou.HolierThanThouExpander2;
import jsortie.quicksort.multiway.expander.kangaroo.SkippyCentripetalExpander2;
import jsortie.quicksort.multiway.expander.kangaroo.SkippyExpander2;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.adapter.SingleToMultiPartitioner;
import jsortie.quicksort.multiway.partitioner.centrifugal.CentrifugalPartitioner2;
import jsortie.quicksort.multiway.partitioner.centripetal.CentripetalPartitioner2;
import jsortie.quicksort.multiway.partitioner.decorator.BirdsOfAFeatherExpander;
import jsortie.quicksort.multiway.partitioner.decorator.BirdsOfAFeatherPartitioner;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner2;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyCentripetalPartitioner2;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner2;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner3;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner4;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner5;
import jsortie.quicksort.multiway.partitioner.kthstatistic.MedianOfMediansMultiPartitioner;
import jsortie.quicksort.multiway.partitioner.kthstatistic.MultiPivotQuickSelectPartitioner;
import jsortie.quicksort.multiway.partitioner.kthstatistic.STLKthStatisticPartitioner;
import jsortie.quicksort.multiway.partitioner.pretend.MultiplexingPartitioner;
import jsortie.quicksort.multiway.partitioner.singlepivot.BentleyMcIlroyPartitioner;
import jsortie.quicksort.multiway.partitioner.singlepivot.SkippyDutchPartitioner;
import jsortie.quicksort.multiway.partitioner.singlepivot.OptimisticBentleyMcIlroyPartitioner;
import jsortie.quicksort.multiway.partitioner.singlepivot.STLTernaryPartitioner;
import jsortie.quicksort.multiway.partitioner.twopivot.DoubleLomutoPartitioner2;
import jsortie.quicksort.multiway.partitioner.twopivot.TunedPartitioner2;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.multiway.selector.adapter.SingleToMultiSelector;
import jsortie.quicksort.multiway.selector.clean.CleanMultiPivotPositionalSelector;
import jsortie.quicksort.multiway.selector.dirty.DirtyMultiPivotPositionalSelector;
import jsortie.quicksort.partitioner.bidirectional.biased.LeftHoarePartitioner;
import jsortie.quicksort.partitioner.bidirectional.biased.LeftSingletonPartitioner;
import jsortie.quicksort.partitioner.bidirectional.biased.RightHoarePartitioner;
import jsortie.quicksort.partitioner.bidirectional.biased.RightSingletonPartitioner;
import jsortie.quicksort.partitioner.bidirectional.centrepivot.CentrePivotPartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.HoarePartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.HoyosPartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.RevisedHoarePartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.SingletonPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.BalancedSkippyPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.FlowerArrangementPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyCentripetalPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyMirrorPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.TunedMirrorPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.TunedPartitioner;
import jsortie.quicksort.partitioner.insideout.CentripetalPartitioner;
import jsortie.quicksort.partitioner.insideout.ReverseSingletonPartitioner;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.BrainDeadKthStatisticPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.PollyannaPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.Afterthought489Partitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.Algorithm489Partitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.Ternary489Partitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.ZeroDelta489Partitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.Afterthought489SampleSelector;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.sampleselector.RauhAndArceSampleSelector;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.Derivative489Partitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.Dutch489Partitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.ExpansiveQuickSelectPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.FairlyCompensated489Partitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.IntroSelect489Partitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.RauhAndArce489Partitioner;
import jsortie.quicksort.partitioner.kthstatistic.floydrivest.LazySimplifiedFloydRivestPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.floydrivest.SimplifiedFloydRivestPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.floydrivest.FloydRivestPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.floydrivest.FloydRivestPartitionerBase;
import jsortie.quicksort.partitioner.kthstatistic.floydrivest.FridgePartitioner;
import jsortie.quicksort.partitioner.kthstatistic.floydrivest.QuintaryFloydRivestPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.heap.DualHeapPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.heap.KislitsynPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.heap.TweakedKislitsynPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.interval.VanEmdenKthStatisticPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.mom.FancierMedianOfMediansPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.mom.HalfMeasurePartitioner;
import jsortie.quicksort.partitioner.kthstatistic.mom.MedianOfMediansPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.mom.ReallyFancyMedianOfMediansPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.mom.TernaryMOMPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.quickselect.DutchQuickSelectPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.quickselect.ExpansionistQuickSelectPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.quickselect.GNUSTLPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.quickselect.IntroSelectPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.quickselect.LessShitGNUSTLPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.quickselect.NoShitGNUSTLPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.quickselect.QuickSelectPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.quickselect.TernaryQuickSelectPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.remedian.RemedianQuickSelectPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.remedian.DutchRemedianPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.remedian.GeneralRemedianPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.remedian.RemedianPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.remedian.TernaryRemedianPartitioner;
import jsortie.quicksort.partitioner.lomuto.LomutoMirrorPartitioner;
import jsortie.quicksort.partitioner.lomuto.LomutoPartitioner;
import jsortie.quicksort.partitioner.unidirectional.HolierThanThouMirrorPartitioner;
import jsortie.quicksort.partitioner.unidirectional.HolierThanThouPartitioner;
import jsortie.quicksort.protector.CheckedKthStatisticPartitioner;
import jsortie.quicksort.samplesizer.SampleSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.clean.CleanPositionalSampleSelector;
import jsortie.quicksort.selector.clean.CleanSingletonSelector;
import jsortie.quicksort.selector.clean.CleanTheoreticalSelector;
import jsortie.quicksort.selector.clean.CleanTukeySelector;
import jsortie.quicksort.selector.dirty.DirtyTheoreticalSelector;
import jsortie.quicksort.selector.dirty.LessShitSingletonSelector;
import jsortie.quicksort.selector.dirty.NoShitSingletonSelector;
import jsortie.quicksort.selector.simple.FirstElementSelector;
import jsortie.quicksort.selector.simple.MiddleElementSelector;
import jsortie.quicksort.theoretical.Gain;
import statistical.BinomialDistribution;
import statistical.HypergeometricDistribution;
import statistical.HypergeometricRetribution;
import statistical.NormalDistribution;

public class KthStatisticTest 
  extends SortTest {
  protected int maxCount = 100000000; // 100 million(!)
  protected int minMedianCount = 1023;
  protected int maxMedianCount = 1048576;
  protected int medianCount = 65535;
  protected int runCount = 100;
  protected DegenerateInput degen = new DegenerateInput();
  protected RandomInput     random = new RandomInput();
  protected PartitionerTest tp = new PartitionerTest();

  protected SampleCollector             nix = new NullSampleCollector();
  protected SampleCollector             pos = new PositionalSampleCollector();

  protected SinglePivotSelector         middle = new MiddleElementSelector();
  
  protected SingletonPartitioner        single      = new SingletonPartitioner();
  protected LeftSingletonPartitioner    leftSingle  = new LeftSingletonPartitioner();
  protected RightSingletonPartitioner   rightSingle = new RightSingletonPartitioner();
  
  protected CentripetalExpander         centri = new CentripetalExpander();
  protected LeftLomutoExpander          llx    = new LeftLomutoExpander();
  protected RightLomutoExpander         rlx    = new RightLomutoExpander();
  
  protected BalancedSkippyExpander    bkx = new BalancedSkippyExpander();
  protected SkippyPartitioner         kp  = new SkippyPartitioner();
  protected SkippyMirrorPartitioner   kmp = new SkippyMirrorPartitioner();
  protected BalancedSkippyPartitioner bkp = new BalancedSkippyPartitioner();
  protected SinglePivotSelector         me  = new MiddleElementSelector();
  protected CleanPositionalSampleSelector cpss = new CleanPositionalSampleSelector();
  protected InsertionSort2Way         isort = new InsertionSort2Way();
  protected SkippyCentripetalExpander kcx = new SkippyCentripetalExpander();
  protected RotatingPartitionExpander  roxy = new RotatingPartitionExpander();
  protected SkippyExpander            kx  = new SkippyExpander();
  protected LeftSkippyExpander        lkx = new LeftSkippyExpander();
  protected RightSkippyExpander       rkx = new RightSkippyExpander();
  protected LeftTunedExpander           ltx = new LeftTunedExpander();
  protected RightTunedExpander          rtx = new RightTunedExpander();
  protected SkippyExpander2           kx2 = new SkippyExpander2();
  protected SinglePivotPartitionHelper checker 
    = new SinglePivotPartitionHelper();
  protected KthStatisticPartitioner brainDead 
    = new BrainDeadKthStatisticPartitioner();
  protected KthStatisticPartitioner kissy
    = new KislitsynPartitioner();
  protected SinglePivotPartitioner rev
    = new ReverseSingletonPartitioner();
  
  public NamedArrayList<KthStatisticPartitioner>
    getKthStatisticPartitioners
    ( SinglePivotPartitioner lp, SinglePivotPartitioner rp
    , PartitionExpander lx,      PartitionExpander rx
    , MultiPivotPartitioner mp,  MultiPivotPartitionExpander mpx
    , SampleCollector coll) {
    NamedArrayList<KthStatisticPartitioner> kstats 
      = new NamedArrayList<KthStatisticPartitioner>();
    if (lp != null && rp != null) {
      PretendExpander lxFake = new PretendExpander(lp);
      PretendExpander rxFake = new PretendExpander(rp);
      kstats.add("QS", new IntroSelectPartitioner(me, lp, rp, 5, kissy));
      //kstats.add("QS-R(O)", new ExpansiveQuickSelectPartitioner(nix, me, lxFake, rxFake));
      kstats.add("MOM-R",   new MedianOfMediansPartitioner(5, lxFake, rxFake));
      kstats.add("MOR-R-U", new RemedianPartitioner(lxFake, rxFake, 2, false));
      kstats.add("MOR-R-B", new RemedianPartitioner(lxFake, rxFake, 2, true));
      kstats.add("HM-R-B",  new HalfMeasurePartitioner(lxFake, rxFake, 2, false));
      kstats.add("HM-R-U",  new HalfMeasurePartitioner(lxFake, rxFake, 2, true));
      kstats.add("Intro489-R", new IntroSelect489Partitioner(coll, lxFake, rxFake));
    }
    if (lx != null && rx != null)
    {
      kstats.add("QS-XL",    new ExpansionistQuickSelectPartitioner(nix, me, lx, rx));
      kstats.add("QS-X",     new ExpansiveQuickSelectPartitioner(nix, me, lx, rx));
      kstats.add("MOM-X",    new MedianOfMediansPartitioner(5, lx, rx));
      kstats.add("MOR-X-U",  new RemedianPartitioner(lx, rx, 2, false));
      kstats.add("MOR-X-B",  new RemedianPartitioner(lx, rx, 2, true));
      kstats.add("HM-X-U",   new HalfMeasurePartitioner(lx, rx, 2, false));
      kstats.add("HM-X-B",   new HalfMeasurePartitioner(lx, rx, 2, true));
      kstats.add("A489-X",   new IntroSelect489Partitioner(coll, lx, rx));
      kstats.add("A489-RA",  new RauhAndArce489Partitioner(coll, lx, rx));
      kstats.add("A489-A",   new Afterthought489Partitioner(coll, lx, rx));
      kstats.add("A489-DP",  new Derivative489Partitioner(coll, lx, rx));
      kstats.add("FR-V",     new FloydRivestPartitioner(nix, lx,rx));
      kstats.add("FR-J",     new FridgePartitioner(nix, lx, rx));
    }
    if (mpx != null) {
      MultiPivotPartitionExpander bof
        = new BirdsOfAFeatherExpander(mpx);
      kstats.add("SFR", new SimplifiedFloydRivestPartitioner(coll, mpx));
      kstats.add("SFR(BOF)", new SimplifiedFloydRivestPartitioner(coll, bof));
    }
    return kstats;
  }

  @Test
  public void testFMOM() {
    int[] crap = random.randomPermutation(200);
    CentripetalExpander cx = new CentripetalExpander(); 
    KthStatisticPartitioner ksp = new FancierMedianOfMediansPartitioner(cx, cx);
    ksp.partitionRangeExactly(crap, 0, crap.length, crap.length / 2);

    int[] crap2 = random.randomPermutation(200);
    KthStatisticPartitioner ksp2 = new ReallyFancyMedianOfMediansPartitioner();
    ksp2.partitionRangeExactly(crap2, 0, crap.length, crap.length / 2);
  }

  @Test
  public void testFMOMPerformance() {
    NamedArrayList<KthStatisticPartitioner> kStats 
      = new NamedArrayList<KthStatisticPartitioner>();
    kStats.add("A489", new Algorithm489Partitioner(nix, bkx, bkx));
    kStats.add("QS", new QuickSelectPartitioner(middle, bkp, bkp, 5, kissy));
    kStats.add("MOM", new MedianOfMediansPartitioner(5, bkx, bkx));
    kStats.add("HM", new HalfMeasurePartitioner(bkx, bkx, 1, true));
    kStats.add("MOR-U-power2", new RemedianPartitioner(bkx, bkx, 2, false));
    kStats.add("MOR-B-power2", new RemedianPartitioner(bkx, bkx, 2, true));
    kStats.add("MOR-B-power3", new RemedianPartitioner(bkx, bkx, 3, true));
    kStats.add("MOR-B-power4", new RemedianPartitioner(bkx, bkx, 4, true));
    kStats.add("MOR-B-power5", new RemedianPartitioner(bkx, bkx, 5, true));
    runStandardKthStatTests(kStats, true, true, true, 0.5);
  }

  private void runStandardKthStatTests
    ( NamedArrayList<KthStatisticPartitioner> kStats
    , boolean random, boolean tilted
    , boolean animalFarm, double kHat ) {
    warmUp(kStats.getArrayList());
    dumpList(kStats);
    int oldRunCount = runCount;
    for ( medianCount = minMedianCount; medianCount<maxMedianCount
        ; medianCount=medianCount*32+31) {
      int k = (int) Math.floor( (medianCount +1 ) * kHat + .5);
      runCount = (medianCount < 1024) ? oldRunCount*100 : oldRunCount; 
      runCount = (medianCount < 32768) ? runCount : (oldRunCount/10);
      if (runCount<1) {
        runCount = 1;
      }
      if (random) {
        testSpecificKthStatisticPartitioners(kStats, 100);
      }
      testSpecificKthStatisticPartitionersOnDegenerateInputs
        ( kStats, medianCount, k, tilted, animalFarm );
    }
    runCount = oldRunCount;
  }

  private void dumpList(NamedArrayList<KthStatisticPartitioner> kStats) {
    for (int i=0; i<kStats.size(); ++i) {
      System.out.println(kStats.getName(i) + "=" + kStats.get(i));
    }
    System.out.println("");
  }

  public double myFloydComparisonGuess(int m) {
    double a = 18.6624;
    if (m < 100) {
      return m * 3 / 2;
    } else {
      double middle = 2 * Math.sqrt((a - 1) * Math.log(m) * (m + 1));
      if (m - 2 * a < middle)
        middle = m - 2 * a;
      return myFloydComparisonGuess((int) Math.floor(m / a)) + 1.5 * (a - 1) * m / a
          + myFloydComparisonGuess((int) Math.floor(middle));
    }
  }

  @Test
  public void testSection20_9_1_2() {
    double k = 3;
    double s = 0;
    for (int i = 0; i < 1000; ++i) {
      s += 1.0 / k;
      k = k * 2.0 - 1.0;
    }
    System.out.println("" + s);
    ;
  }

  @Test
  public void testSection20_10_5_3() {
    for (int m = 100; m <= 10000000; m = m * 5 / 4) {
      System.out.println("" + m + "\t" + myFloydComparisonGuess(m) / (double) m);
    }
  }

  @Test
  public void testFloydRivestVariants() {
    runCount = 100;
    double datapoints = 7;
    CentripetalExpander2 mpx = new CentripetalExpander2();
    SkippyExpander2    kx2 = new SkippyExpander2();
    SampleOfMediansCollector smc 
      = new SampleOfMediansCollector();
    ArrayList<FloydRivestPartitionerBase> kStats = new ArrayList<FloydRivestPartitionerBase>();
    kStats.add(new SimplifiedFloydRivestPartitioner(mpx));
    kStats.add(new SimplifiedFloydRivestPartitioner(kx2));
    kStats.add(new SimplifiedFloydRivestPartitioner(smc, mpx));
    kStats.add(new SimplifiedFloydRivestPartitioner(smc, kx2));
    kStats.add(new LazySimplifiedFloydRivestPartitioner(lkx, rkx));
    kStats.add(new FloydRivestPartitioner(nix, lkx, rkx));
    kStats.add(new FridgePartitioner(nix, lkx, rkx));
    kStats.add(new QuintaryFloydRivestPartitioner(lkx, rkx));
    // warmUp(kStats);
    for (int m = 100; m <= 10000000; m *= 10) {
      System.out.println("Comparisons/m, for m=" + m);
      for (double k = m / (datapoints + 1); k < m; k += m / (datapoints + 1)) {
        for (FloydRivestPartitionerBase p : kStats) {
          p.setComparisonCount(0);
        }
        for (int run = 0; run < runCount; ++run) {
          int[] crud = random.randomPermutation(m);
          for (int i = 0; i < kStats.size(); ++i) {
            int[] copy = Arrays.copyOf(crud, crud.length);
            kStats.get(i).partitionRangeExactly(copy, 0, copy.length, (int) Math.floor(k));
          }
        }
        String line = "" + (double) k / (double) m;
        for (int i = 0; i < kStats.size(); ++i) {
          double coefficientOfM = kStats.get(i).getComparisonCount() / (double) runCount / (double) m;
          line += "\t" + Math.floor(1000.0 * coefficientOfM + .5) / 1000.0;
        }
        System.out.println(line);
      }
      System.out.println("");
    }
  }
  public double hmw(int start, int stop, int targetIndex) {
    double c = 0;
    while (32 < stop - start) {
      int t = (stop - start) / 3;
      c += (stop - start);
      int sampleStart = start + t;
      int sampleStop = sampleStart + t + 1;
      int sampleTarget = sampleStart + (targetIndex - start) / 3;
      c += hmw(sampleStart, sampleStop, sampleTarget);
      int l = (sampleTarget - sampleStart) * 2 + 1;
      int r = (sampleStop - sampleTarget) * 2 - 1;
      if (l < r && start + l < targetIndex) {
        start += l;
      } else if (targetIndex < stop - r) {
        stop -= r;
      } else {
        break;
      }
    }
    return c + SortTest.bitsLeftInPartitions(new int[] { start, stop });
  }

  @Test
  public void t() {
    for (int m = 2; m < 1000000000; m *= 2) {
      double c = hmw(0, m, m / 2);
      System.out.println
        ( "" + m + "\t" + c + "\t" + (c / m) + "\t" 
        + Math.log(c) / Math.log(m));
    }
    System.out.println(2.0 - Math.log(2) / Math.log(3));
  }

  @Test
  public void testHalfMeasureJobbies() {
    medianCount = 65536;
    runCount = 100;
    NamedArrayList<KthStatisticPartitioner> kStats 
      = new NamedArrayList<KthStatisticPartitioner>();
    kStats.add("HM(roxy)", new HalfMeasurePartitioner(roxy, roxy, 1, true));
    kStats.add("HM(kcx)", new HalfMeasurePartitioner(kcx, kcx, 1, true));
    kStats.add("HM(kx)", new HalfMeasurePartitioner(lkx, rkx, 1, true));
    kStats.add("HM(bkx)", new HalfMeasurePartitioner(bkx, bkx, 1, true));
    warmUp(kStats.getArrayList());
    testSpecificKthStatisticPartitioners(kStats, 128);
  }

  @Test
  public void testCollectorSamplingEffect() {
    NamedArrayList<KthStatisticPartitioner> kStats 
      = new NamedArrayList<KthStatisticPartitioner>();
    for ( PartitionExpander px 
          : new PartitionExpander[] 
                { new CentripetalExpander(), new HolierThanThouExpander()
                , new TunedExpander(), new SkippyExpander()
                , new SkippyCentripetalExpander() }) {
      kStats.add("A489/Nil-" + px.toString(), new Algorithm489Partitioner(nix, px, px));
      kStats.add("A489/Pos-" + px.toString(), new Algorithm489Partitioner
                     ( new PositionalSampleCollector(), px, px ) );
    }
    warmUp(kStats.getArrayList());
    int k = (medianCount+1)/2;
    testSpecificKthStatisticPartitionersOnDegenerateInputs
      ( kStats, medianCount, k, true, true );
    testSpecificKthStatisticPartitioners(kStats, 128);
  }

  @Test
  public void testKthStatisticPartitioners() {
    //testKthStatisticSingletonPartitioners();
    //testKthStatisticLomutoPartitioners();
    //testKthStatisticCentripetalPartitioners();
    long start = System.nanoTime();
    testKthStatisticHolierThanThouPartitioners();
    System.out.println("\nHolierThanThou Elapsed Time " + (System.nanoTime()-start)/billion );
    start = System.nanoTime();
    testKthStatisticHoyosPartitioners();
    System.out.println("\nHoyos Elapsed Time " + (System.nanoTime()-start)/billion );
    start = System.nanoTime();
    testKthStatisticReverseSingletonPartitioners();
    System.out.println("\nReverseSingleton Elapsed Time " + (System.nanoTime()-start)/billion );
    //testKthStatisticTunedPartitioners();
    //testKthStatisticKangarooPartitioners();
    //testKthStatisticKCP();
    //testKthStatisticFlowerPartitioners();
  }
  @Test
  public void testKthStatisticBaselines() {
    CleanSingletonSelector     bo3   = new CleanSingletonSelector();
    LessShitSingletonSelector  lsbo3 = new LessShitSingletonSelector();
    NoShitSingletonSelector    nsbo3 = new NoShitSingletonSelector();
    SinglePivotPartitioner     p     = new HoarePartitioner();
    QuickSelectPartitioner     hoare = new QuickSelectPartitioner( me, p, p, 3, kissy);
    QuickSelectPartitioner     trad  = new QuickSelectPartitioner( bo3, single, single, 3, kissy);
    GNUSTLPartitioner          gnu   = new GNUSTLPartitioner();
    STLKthStatisticPartitioner ms    = new STLKthStatisticPartitioner();
    
    int oldMMC = maxMedianCount;
    maxMedianCount = 1024;
    runCount = 1000;
    NamedArrayList<KthStatisticPartitioner> kStats 
      = new NamedArrayList<KthStatisticPartitioner>();
    kStats.add("Algorithm65", hoare);
    kStats.add("Traditional", trad);
    kStats.add("GNU", gnu);
    kStats.add("MS",  ms);
    runStandardKthStatTests(kStats, true, true, true, 0.5);
    System.out.println();
    testKStatPartitioners(kStats, 10, 100000, 0.5, 0, true);
    System.out.println();
    kStats.clear();
    QuickSelectPartitioner trad2  
      = new QuickSelectPartitioner( lsbo3, single, single, 5, kissy);
    QuickSelectPartitioner trad3  
      = new QuickSelectPartitioner( nsbo3, single, single, 5, kissy);
    LessShitGNUSTLPartitioner gnu2 
      = new LessShitGNUSTLPartitioner();
    NoShitGNUSTLPartitioner gnu3 
      = new NoShitGNUSTLPartitioner();
    kStats.add("Traditional",           trad);
    kStats.add("Traditional(LessShit)", trad2);
    kStats.add("Traditional(NoShit)",   trad3);
    kStats.add("GNU",                   gnu);
    kStats.add("GNU(LessShit)",         gnu2);
    kStats.add("GNU(NoShit)",           gnu3);
    runStandardKthStatTests(kStats, false, true, false, 0.5);
    maxMedianCount = oldMMC;
  }
  @Test
  public void testKthStatisticHoarePartitioners() {
    SinglePivotPartitioner p  = new HoarePartitioner();
    SinglePivotPartitioner lp = new LeftHoarePartitioner();
    SinglePivotPartitioner rp = new RightHoarePartitioner();
    PartitionExpander      px = new HoareExpander();
    MultiPivotPartitioner  mp = new MultiplexingPartitioner(lp, rp);
    MultiPivotPartitionExpander mpx = null;
    NamedArrayList<KthStatisticPartitioner> kStats 
      = getKthStatisticPartitioners(p, p, px, px, mp, mpx, nix);
    runStandardKthStatTests(kStats, true, true, true, 0.5);
  }
  @Test
  public void testSTLKthStatisticPartitioner() {
    NamedArrayList<KthStatisticPartitioner> kStats 
      = new NamedArrayList<KthStatisticPartitioner>();
    QuickSelectPartitioner qk 
      = new QuickSelectPartitioner();
    BentleyMcIlroyPartitioner bentley 
      = new BentleyMcIlroyPartitioner();
    OptimisticBentleyMcIlroyPartitioner oBentley 
      = new OptimisticBentleyMcIlroyPartitioner();
    SkippyDutchPartitioner  kd 
      = new SkippyDutchPartitioner();
    
    kStats.add ("QS (1P)", new QuickSelectPartitioner
                               ( me, single,     single,      10, kissy) );
    kStats.add ("QS (2P)", new QuickSelectPartitioner
                               ( me, leftSingle, rightSingle, 10, kissy) );
    kStats.add( "STL",     new STLKthStatisticPartitioner() );
    kStats.add( "ME + QS", new STLKthStatisticPartitioner(me, qk) );
    kStats.add( "ME + BM + QS",  new STLKthStatisticPartitioner
                                     (me, bentley, qk) );
    kStats.add( "ME + OBM + QS", new STLKthStatisticPartitioner
                                     (me, oBentley, qk) );
    kStats.add( "ME + KD + QS",  new STLKthStatisticPartitioner
                                     (me, kd, qk) );
    runStandardKthStatTests(kStats, true, true, true, 0.5);
  }
  
  @Test
  public void testKthStatisticRevisedHoarePartitioners() {
    SinglePivotPartitioner p = new RevisedHoarePartitioner();
    PartitionExpander      x  = new RevisedHoareExpander();
    MultiPivotPartitioner  mp = new MultiplexingPartitioner(p, p);
    MultiPivotPartitionExpander mpx = null;
    NamedArrayList<KthStatisticPartitioner> kStats 
      = getKthStatisticPartitioners(p, p, x, x, mp, mpx, nix);
    runStandardKthStatTests(kStats, true, true, true, 0.5);
  }

  private void dumpList(ArrayList<KthStatisticPartitioner> kStats) {
    for (KthStatisticPartitioner ksp : kStats) {
      System.out.println(shortName(ksp.toString()));
    }
    System.out.println("");
  }

  @Test
  public void testKthStatisticSingletonPartitioners() {
    SinglePivotPartitioner p  = new SingletonPartitioner();
    SinglePivotPartitioner lp = new LeftSingletonPartitioner();
    SinglePivotPartitioner rp = new RightSingletonPartitioner();
    PartitionExpander      px = new SingletonExpander();
    MultiPivotPartitioner  mp = new MultiplexingPartitioner(lp, rp);
    MultiPivotPartitionExpander mpx = null;
    NamedArrayList<KthStatisticPartitioner> kStats 
      = getKthStatisticPartitioners(p, p, px, px, mp, mpx, nix);
    //runStandardKthStatTests(kStats, false, 0.5);
    runCount = 100;
    warmUp(kStats.getArrayList());
    testSpecificKthStatisticPartitionersOnDegenerateInputs
        ( kStats, 1048575, 1048575/2, true, true);
  }
  
  @Test
  public void testKthStatisticReverseSingletonPartitioners() {
    SinglePivotPartitioner p  = new ReverseSingletonPartitioner();
    PartitionExpander      px = new ReverseSingletonExpander();
    MultiPivotPartitioner  mp = new MultiplexingPartitioner(p,p);
    MultiPivotPartitionExpander mpx = null;
    NamedArrayList<KthStatisticPartitioner> kStats 
      = getKthStatisticPartitioners(p, p, px, px, mp, mpx, nix);
    runStandardKthStatTests(kStats, true, true, true, 0.5);
  }

  @Test
  public void testGingerRogersOnSingleton() {
    SingletonPartitioner   p  = new SingletonPartitioner();
    SinglePivotPartitioner lp = new LeftSingletonPartitioner();
    SinglePivotPartitioner rp = new RightSingletonPartitioner();
    PartitionExpander      px = new SingletonExpander();
    MultiPivotPartitioner  mp = new MultiplexingPartitioner(lp,rp);
    MultiPivotPartitionExpander mpx = null;
    NamedArrayList<KthStatisticPartitioner> kStats 
      = getKthStatisticPartitioners(p, p, px, px, mp, mpx, nix);
    runStandardKthStatTests(kStats, true, true, false, 0.1);
  }
  @Test
  public void testCPFRProblem() {
    CentripetalExpander2    mpx 
      = new CentripetalExpander2();
    KthStatisticPartitioner sfr 
      = new SimplifiedFloydRivestPartitioner(mpx);
    int[] vData = degen.permutationOfDupes(32767, 2);
    sfr.partitionRangeExactly(vData, 0, vData.length, vData.length/2);
  }
  @Test
  public void testKthStatisticCentripetalPartitioners() {
    SinglePivotPartitioner  p   = new CentripetalPartitioner();
    PartitionExpander       x   = new CentripetalExpander();
    CentripetalPartitioner2 mp  = new CentripetalPartitioner2();
    CentripetalExpander2    mpx = new CentripetalExpander2();
    NamedArrayList<KthStatisticPartitioner> kStats 
      = getKthStatisticPartitioners(p, p, x, x, mp, mpx, nix);
    runStandardKthStatTests(kStats, true, true, true, 0.5);
  }
  
  @Test
  public void testKthStatisticLomutoPartitioners() {
    SinglePivotPartitioner lp 
      = new LomutoMirrorPartitioner();
    SinglePivotPartitioner rp = new LomutoPartitioner();
    PartitionExpander      lx = new LeftLomutoExpander();
    PartitionExpander      rx = new RightLomutoExpander();
    MultiPivotPartitioner  mp 
      = new DoubleLomutoPartitioner2();
    MultiPivotPartitionExpander mpx 
      = new DoubleLomutoExpander2();
    NamedArrayList<KthStatisticPartitioner> kStats 
      = getKthStatisticPartitioners(lp, rp, lx, rx, mp, mpx, nix);
    runStandardKthStatTests(kStats, false, false, true, 0.5);
  }
  @Test
  public void testKthStatisticHolierThanThouPartitioners() {
    HolierThanThouMirrorPartitioner lp 
      = new HolierThanThouMirrorPartitioner();
    HolierThanThouPartitioner rp 
      = new HolierThanThouPartitioner();
    HolierThanThouExpander x 
      = new HolierThanThouExpander();
    HolierThanThouPartitioner2 mp 
      = new HolierThanThouPartitioner2();
    HolierThanThouExpander2 mpx 
      = new HolierThanThouExpander2();
    NamedArrayList<KthStatisticPartitioner> kStats 
      = getKthStatisticPartitioners(lp, rp, x, x, mp, mpx, nix);
    runStandardKthStatTests(kStats, true, true, true, 0.5);
  }
  @Test
  public void testKthStatisticTandemHTTPartitioners() {
    //I'm only interested in performance on animal farm inputs just now.
    runCount = 1;
    HolierThanThouMirrorPartitioner lp 
      = new HolierThanThouMirrorPartitioner();
    HolierThanThouPartitioner rp 
      = new HolierThanThouPartitioner();
    LeftHTTExpander lx 
      = new LeftHTTExpander();
    RightHTTExpander rx 
      = new RightHTTExpander();
    HolierThanThouPartitioner2 mp 
      = new HolierThanThouPartitioner2();
    HolierThanThouExpander2 mpx 
      = new HolierThanThouExpander2();
    NamedArrayList<KthStatisticPartitioner> kStats 
      = getKthStatisticPartitioners(lp, rp, lx, rx, mp, mpx, nix);
    runStandardKthStatTests(kStats, false, false, true, 0.5);
  }
  @Test
  public void testKthStatisticHoyosPartitioners() {
    SinglePivotPartitioner p  = new CentrePivotPartitioner();
    PartitionExpander      x  = new HoyosExpander();
    MultiPivotPartitioner  p2 = new CentrifugalPartitioner2();
    MultiPivotPartitionExpander x2 = null; //new CentrifugalExpander2();
    NamedArrayList<KthStatisticPartitioner> kStats 
      = getKthStatisticPartitioners(p, p, x, x, p2, x2, nix);
    runStandardKthStatTests(kStats, true, true, true, 0.5);
  }
  @Test
  public void testKthStatisticKangarooPartitioners() {
    int oldRunCount = runCount;
    runCount = 500;
    SkippyMirrorPartitioner lp = new SkippyMirrorPartitioner();
    SkippyPartitioner       rp = new SkippyPartitioner();
    LeftSkippyExpander      lx = new LeftSkippyExpander();
    RightSkippyExpander     rx = new RightSkippyExpander();
    SkippyPartitioner2     kp2 = new SkippyPartitioner2();
    SkippyExpander2        kx2 = new SkippyExpander2();
    NamedArrayList<KthStatisticPartitioner> kStats 
      = getKthStatisticPartitioners(lp, rp, lx, rx, kp2, kx2, nix);
    runStandardKthStatTests(kStats, true, true, true, 0.5);
    runCount = oldRunCount;
  }

  @Test
  public void testKKPartitionersOnBackwardsInputs() {
    SkippyMirrorPartitioner lp = new SkippyMirrorPartitioner();
    SkippyPartitioner       rp = new SkippyPartitioner();
    LeftSkippyExpander      lx = new LeftSkippyExpander();
    RightSkippyExpander     rx = new RightSkippyExpander();
    SkippyPartitioner2     kp2 = new SkippyPartitioner2();
    SkippyExpander2        kx2 = new SkippyExpander2();
    runKPartitionersOnBackwardsInputs(lp, rp, lx, rx, kp2, kx2);
  }

  public void runKPartitionersOnBackwardsInputs
    ( SinglePivotPartitioner lp, SinglePivotPartitioner rp
    , PartitionExpander lx, PartitionExpander rx
    , MultiPivotPartitioner p2
    , MultiPivotPartitionExpander x2 ) {
    NamedArrayList<KthStatisticPartitioner> kStats 
      = new NamedArrayList<KthStatisticPartitioner>();
    for ( SampleCollector sc : new SampleCollector[] 
          { new NullSampleCollector()
          , new PositionalSampleCollector()
          , new RandomSampleCollector() } ) {
      kStats.add("A489-" + sc.toString(), new IntroSelect489Partitioner(sc, lx, rx));
      kStats.add("SFR-"  + sc.toString(),  new SimplifiedFloydRivestPartitioner(sc, kx2));
      kStats.add("FR-"   + sc.toString(),   new FloydRivestPartitioner(sc, lx, rx));
      kStats.add("FRJ-"  + sc.toString(),  new FridgePartitioner(sc, lx, rx));
    }
    IntroSelect489Partitioner k1
      = new IntroSelect489Partitioner(nix, lx, rx);
    SimplifiedFloydRivestPartitioner k2
      = new SimplifiedFloydRivestPartitioner(kx2);
    FloydRivestPartitioner k3
      = new FloydRivestPartitioner(nix, lx, rx);
    FridgePartitioner k4
      = new FridgePartitioner(nix, lx, rx);
    k1.setFolding(true);
    k2.setFolding(true);
    k3.setFolding(true);
    k4.setFolding(true);
    kStats.add("A489", k1);
    kStats.add("SFR", k2);
    kStats.add("FR", k3);
    kStats.add("FRJ", k4);
    warmUp(kStats.getArrayList());
    dumpList(kStats.getArrayList());
    medianCount = 32767;
    runCount    = 100;
    int k = (medianCount+1)/2;
    testSpecificKthStatisticPartitionersOnDegenerateInputs
      ( kStats, medianCount, k, true, false);
  }
  @Test
  public void testKthStatisticBKP() {
    BalancedSkippyPartitioner p 
      = new BalancedSkippyPartitioner();
    BalancedSkippyExpander    x 
      = new BalancedSkippyExpander();
    /*
    BalancedKangarooPartitioner2 mp
      = new BalancedKangarooPartitioner2();
    BalancedKangarooExpander2 mx
      = new BalancedKangarooExpander2();
    */
    NamedArrayList<KthStatisticPartitioner> kStats 
      = getKthStatisticPartitioners(p, p, x, x, null, null, nix);
    runStandardKthStatTests(kStats, true, true, true, 0.5);
  }
  @Test
  public void testKthStatisticBKP2() {
    BalancedSkippyPartitioner p 
      = new BalancedSkippyPartitioner();
    BalancedSkippyExpander    x 
      = new BalancedSkippyExpander();
    NamedArrayList<KthStatisticPartitioner> kStats 
      = getKthStatisticPartitioners(p, p, x, x, null, null, pos);
    runStandardKthStatTests(kStats, true, true, true, 0.5);
  }
  @Test
  public void testKthStatisticTunedPartitioners() {
    TunedMirrorPartitioner lp = new TunedMirrorPartitioner();
    TunedPartitioner       rp = new TunedPartitioner();
    LeftTunedExpander      lx = new LeftTunedExpander();
    RightTunedExpander     rx = new RightTunedExpander();
    TunedPartitioner2      mp = new TunedPartitioner2();
    TunedExpander2         mx = new TunedExpander2();
    NamedArrayList<KthStatisticPartitioner> kStats 
      = getKthStatisticPartitioners(lp, rp, lx, rx, mp, mx, nix);
    runStandardKthStatTests(kStats, true, true, true, 0.5);
  }
  
  @Test
  public void testKthStatisticKCP() {
    SkippyCentripetalPartitioner  p   
      = new SkippyCentripetalPartitioner();
    SkippyCentripetalExpander     px  
      = new SkippyCentripetalExpander();
    SkippyCentripetalPartitioner2 mp  
      = new SkippyCentripetalPartitioner2();
    SkippyCentripetalExpander2    mpx 
      = new SkippyCentripetalExpander2();
    NamedArrayList<KthStatisticPartitioner> kStats 
      = getKthStatisticPartitioners(p, p, px, px, mp, mpx, nix);
    runStandardKthStatTests(kStats, true, true, true, 0.5);
  }
  
  @Test
  public void testKCPKPartitionersOnBackwardsInputs() {
    SkippyCentripetalPartitioner  p   
      = new SkippyCentripetalPartitioner();
    SkippyCentripetalExpander     x   
      = new SkippyCentripetalExpander();
    SkippyCentripetalPartitioner2 p2  
      = new SkippyCentripetalPartitioner2();
    SkippyCentripetalExpander2    x2  
      = new SkippyCentripetalExpander2();
    runKPartitionersOnBackwardsInputs(p, p, x, x, p2, x2);
  }
  @Test
  public void testKthStatisticFlowerPartitioners() {
    FlowerArrangementPartitioner p 
      = new FlowerArrangementPartitioner();
    PartitionExpander            px 
      = new PartitionerToExpander(p);
    MultiPivotPartitioner        mp 
      = new MultiplexingPartitioner(p, p);
    MultiPivotPartitionExpander  mpx = null;
    NamedArrayList<KthStatisticPartitioner> kStats 
      = getKthStatisticPartitioners(p, p, px, px, mp, mpx, nix);
    warmUp(kStats.getArrayList());
    runStandardKthStatTests(kStats, true, true, true, 0.5);
  }
  @Test
  public void testKthStatisticFlowerPartitioners2() {
    FlowerArrangementPartitioner p 
      = new FlowerArrangementPartitioner();
    PartitionExpander            px 
      = new PartitionerToExpander(p);
    NamedArrayList<KthStatisticPartitioner> kStats 
      = new NamedArrayList<KthStatisticPartitioner>();
    PositionalSampleCollector pos 
      = new PositionalSampleCollector(); 
    kStats.add("QS-XL",    new ExpansionistQuickSelectPartitioner(pos, me, px, px));
    kStats.add("QS-X",     new ExpansiveQuickSelectPartitioner(pos, me, px, px));
    kStats.add("A489-X",   new IntroSelect489Partitioner(pos, px, px));
    kStats.add("A489-RA",  new RauhAndArce489Partitioner(pos, px, px));
    kStats.add("A489-A",   new Afterthought489Partitioner(pos, px, px));
    kStats.add("A489-DP",  new Derivative489Partitioner(pos, px, px));
    kStats.add("FR-V",     new FloydRivestPartitioner(pos, px, px));
    kStats.add("FR-J",     new FridgePartitioner(pos, px, px));
    warmUp(kStats.getArrayList());
    runStandardKthStatTests(kStats, true, true, true, 0.5);
  }
  public void testTheoreticalSelectors() {
    testTheoreticalSelectors2(true);
  }
  public void testDirtyTheoreticalSelectors() {
    testTheoreticalSelectors2(false);
  }
  public void testTheoreticalSelectors2(boolean clean) {
    //Penguin: testTheoreticalSelectors3 
    //needs to use ZeroDelta489Partitioner
    //and corresponding expanders, instead
    int n = 1048576;
    ArrayList<SinglePivotPartitioner> simplePartitioners 
      = new ArrayList<SinglePivotPartitioner>();
    simplePartitioners.add(new LomutoPartitioner());
    simplePartitioners.add(new SingletonPartitioner());
    simplePartitioners.add(new HoyosPartitioner());
    simplePartitioners.add(new CentripetalPartitioner());
    simplePartitioners.add(new TunedPartitioner());
    simplePartitioners.add(new SkippyPartitioner());
    simplePartitioners.add(new SkippyCentripetalPartitioner());
    tp.warmUpPartitioners(simplePartitioners);
    String header = "x";
    for (SinglePivotPartitioner party : simplePartitioners) {
      header += "\t" + shortName(party.toString());
    }
    System.out.println(header);
    for (int xx = 1; xx < 50; ++xx) {
      double x = xx / 50.0;
      SampleSizer     sqr = new SquareRootSampleSizer(1); 
      SampleCollector nix = new NullSampleCollector();
      SampleCollector pos = new PositionalSampleCollector();
      ArrayList<SinglePivotSelector> fancySelectors 
        = new ArrayList<SinglePivotSelector>();
      for (SinglePivotPartitioner party : simplePartitioners) {
        PartitionExpander expo = new PartitionerToExpander(party);
        ExternalSampleCollector cleanColl 
          = new ExternalPositionalCollector();
        Algorithm489Partitioner a489      
          = new Algorithm489Partitioner(nix, expo, expo);
        fancySelectors.add(
          clean 
            ? new CleanTheoreticalSelector(x, cleanColl, a489) 
            : new DirtyTheoreticalSelector(x, sqr, pos, a489));
      }
      double[] times = new double[simplePartitioners.size()];
      double[] gains = new double[simplePartitioners.size()];
      for (int r = 0; r < 50; ++r) {
        int[] input = random.randomPermutation(n);
        for (int p = 0; p < simplePartitioners.size(); ++p) {
          int[] copy = Arrays.copyOf(input, input.length);
          SinglePivotSelector sector = fancySelectors.get(p);
          SinglePivotPartitioner party = simplePartitioners.get(p);
          long startTime = System.nanoTime();
          int split = sector.selectPivotIndex(copy, 0, copy.length);
          int k = party.partitionRange(copy, 0, copy.length, split);
          long stopTime = System.nanoTime();
          times[p] += (double) (stopTime - startTime);
          gains[p] += partitioningGain(copy.length, k);
        }
      }
      String s = "" + x;
      for (int p = 0; p < simplePartitioners.size(); ++p) {
        s += "\t" + Math.floor(gains[p] / times[p] * 1000.0) / 1000.0;
      }
      System.out.println(s);
    }
  }

  public void testSpecificKthStatisticPartitioners
    ( NamedArrayList<KthStatisticPartitioner> kStats
    , double dataPointCount) {
    int n = medianCount;
    double kStep = (n + 1) / dataPointCount;
    System.out.println
      ( "Efficiency (Gbps) for" 
      + " kthStatisticPartitioner to find kth of " + n
      + " (over " + runCount + " executions)");
    String header = "k";
    for (String name : kStats.getNames()) {
      header += "\t" + name;
    }
    System.out.println(header);
    for ( double kDouble = (kStep / 2) - 1
        ; kDouble < n; kDouble += kStep) {
      int k = (int) Math.floor(kDouble);
      double[] nanoTimes = new double[kStats.size()];
      for (int run = 0; run < runCount; ++run) {
        int[] crap = random.randomPermutation(n);
        int i = 0;
        for (KthStatisticPartitioner ksp : kStats) {
          nanoTimes[i] += 
            timeOneKthStatisticPartition(crap, ksp, k);
          ++i;
        }
      }
      double gainPerRun = partitioningGain(n, k);
      String s = "" + k;
      for (int i = 0; i < kStats.size(); ++i) {
        double efficiency = gainPerRun * runCount / nanoTimes[i];
        s += "\t" + Math.floor(efficiency * 100000.0 + .5) / 100000.0;
      }
      System.out.println(s);
    }
    System.out.println("\n");
  }

  // return the number of bits of information 
  // gained by exactly partitioning
  // an array range containing m items, so that 
  // every item left of the (k-1)th iss <= it, and 
  // every item right of the (k-1)th is >= it.
  protected double partitioningGain(int m, int k) {
    int y = m - k - 1;
    return 
      ( m * Math.log(m) - k * Math.log(k) - y * Math.log(y) ) 
      / Math.log(2);
  }

  // returns: time in nanoseconds
  protected double timeOneKthStatisticPartition
    ( int[] vInput, KthStatisticPartitioner ksp
    , int targetIndex) {
    int[] vCopy 
      = Arrays.copyOf(vInput, vInput.length);
    long startTime = System.nanoTime();
    ksp.partitionRangeExactly
      ( vCopy, 0, vCopy.length, targetIndex );
    long stopTime = System.nanoTime();
    /* Todo: Turn these checks on!
    if ( vCopy[targetIndex] != targetIndex ) {
      if (vInput.length<50) {
        DumpRangeHelper.dumpArray("Input", vInput);
        DumpRangeHelper.dumpRange
          ( "Output", vCopy, 0, targetIndex );
        DumpRangeHelper.dumpRange
          ( "Output", vCopy, targetIndex+1, vInput.length );
      }
      throw new 
        SortingFailureException
        ("\nRun with k=" + targetIndex 
        + " of " + ksp.toString()
        + " failed:\n copy[" + (targetIndex) + "]"
        + " was " + vCopy[targetIndex]); 
    }
    checker.checkPartition
      ( "\n" + ksp.toString() + " (Run with k=" + targetIndex 
      + " of " + ksp.toString() + ")"
      , vCopy, 0, targetIndex, vInput.length);
    */
    return (double) (stopTime - startTime);
    
  }

  @Test
  public void testKthStatisticPartitionersOnDegenerateInputs() {
    BalancedSkippyPartitioner  kp 
      = new BalancedSkippyPartitioner();
    BalancedSkippyExpander     kpx 
      = new BalancedSkippyExpander();
    SkippyPartitioner2         kp2 
      = new SkippyPartitioner2();
    SkippyExpander2            kpx2 
      = new SkippyExpander2();
    FlowerArrangementPartitioner flower 
      = new FlowerArrangementPartitioner();
    SkippyCentripetalExpander  kcpx 
      = new SkippyCentripetalExpander();

    SampleSizer sqr = new SquareRootSampleSizer();
    SampleCollector nix = new NullSampleCollector();
    
    NamedArrayList<KthStatisticPartitioner> kStats;
    PartitionExpander xFlower 
      = new PartitionerToExpander(flower);
    kStats 
      = getKthStatisticPartitioners
        ( kp, kp, kpx, kpx, kp2, kpx2, nix );
    kStats.add("A489(KCP)", new IntroSelect489Partitioner(nix, kcpx, kcpx));
    kStats.add("A489(Flower)", new IntroSelect489Partitioner(nix, xFlower, xFlower));
    kStats.add("VanEmden", new VanEmdenKthStatisticPartitioner());
    kStats.add("VanEmden(Sqrt)", new VanEmdenKthStatisticPartitioner(sqr, nix));
    kStats.add("DualHeap", new DualHeapPartitioner());

    runCount = 1000;
    testSpecificKthStatisticPartitionersOnDegenerateInputs
      (kStats, 65535, 32768, true, true);

    runCount = 100;
    testSpecificKthStatisticPartitionersOnDegenerateInputs
    (kStats, 1048575, 524288, true, true);
  }
  
  @Test
  public void testZeroDelta489Partitioner() {
    NamedArrayList<KthStatisticPartitioner> kStats 
      = new NamedArrayList<KthStatisticPartitioner>(); 
    kStats.add("QS", new QuickSelectPartitioner(middle, kp, kmp, 5, kissy));
    kStats.add("A489", new IntroSelect489Partitioner(nix, lkx, rkx));
    kStats.add("A489-RA", new RauhAndArce489Partitioner(nix, lkx, rkx));
    kStats.add("A489-ZD", new ZeroDelta489Partitioner(nix, lkx, rkx));
    kStats.add("A489-DP", new Derivative489Partitioner(nix, lkx, rkx));
    warmUp(kStats.getArrayList());
    medianCount = 511;
    runCount = 10000;
    testSpecificKthStatisticPartitioners(kStats, 128);
    System.out.println("");
    medianCount = 8191;
    runCount = 10000;
    testSpecificKthStatisticPartitioners(kStats, 256);
    System.out.println("");
    medianCount = 65535;
    runCount = 1000;
    testSpecificKthStatisticPartitioners(kStats, 256);
  }

  public void testSpecificKthStatisticPartitionersOnDegenerateInputs
    ( NamedArrayList<KthStatisticPartitioner> kStats
    , int n, int k
    , boolean ordered, boolean animalFarm ) {
    double divisor = 1000.0;
    String units   = "milliseconds";
    if (n<32000) {
      units = "microseconds";
      divisor = 1.0;
    }
    warmUp(kStats.getArrayList());
    System.out.println
        ( "Mean time (" + units + ") for kthStatisticPartitioner" 
        + " to find median of degenerate " + n
        + " integer inputs (over " + runCount + " executions)");
    String header = "out-of-order";
    for (String name : kStats.getNames()) {
      header += "\t" + name;
    }
    System.out.println(header);
    ArrayList<Integer> valuesOfD = new ArrayList<Integer>();
    for (int d = 1; d < n; d += (d >= 4) ? (d / 4) : 1) {
      valuesOfD.add(d);
    }
    if(ordered) {
      for (int rev = 1; rev >= 0; --rev) { //1 for reverse-ordered, 
                                           //0 for ordered
        for (int i = 0; i < valuesOfD.size(); ++i) {
          int d = valuesOfD.get((rev == 0) 
                ? valuesOfD.size() - 1 - i : i);
          double[] nanoTimes = new double[kStats.size()];
          for (int run = 0; run < runCount; ++run) {
            int[] crap 
              = degen.postRandomUpdatePermutation(n, d);
            if (rev == 1) {
              RangeSortHelper.reverseRange(crap, 0, n);
            }
            int x = 0;
            for (KthStatisticPartitioner ksp : kStats) {
              nanoTimes[x] += timeOneKthStatisticPartition
                              ( crap, ksp, n / 2 );
              ++x;
            }
          }
          String s 
            = padTo ( ( ( rev > 0 ? "desc" : "asc") + d )
                    , 5 + (int) Math.log10(n) );
          for (int j = 0; j < kStats.size(); ++j) {
            //avg time in milliseconds
            double avgTime 
              = nanoTimes[j] / (double) runCount;
            s += "\t" + Math.floor(avgTime / divisor / +.5) / 1000.0;
          }
          System.out.println(s);
        }
      }
      System.out.println("");
    }
    if (!animalFarm) {
      return;
    }
    for (int d = 1; d < n; d += (d >= 4) ? (d / 4) : 1) {
      double[] nanoTimes = new double[kStats.size()];
      for (int run = 0; run < runCount; ++run) {
        int[] crap = degen.permutationOfDupes(n, d);
        int i = 0;
        for (KthStatisticPartitioner ksp : kStats) {
          try {
            nanoTimes[i] += 
              timeOneKthStatisticPartition
              ( crap, ksp, n / 2 );
            ++i;
          } catch (Exception e) {
            System.out.println
              ( "Exception thrown by " 
              + ksp.toString() + " with d=" + d);
            throw e;
          }
        }
      }
      String s = padTo("equal" + d, 5 + (int) Math.log10(n));
      for (int i = 0; i < kStats.size(); ++i) {
        double avgTime = nanoTimes[i] / (double) runCount;
        s += "\t" + Math.floor(avgTime / divisor + .5) / 1000.0;
      }
      System.out.println(s);
    }
    System.out.println("");
  }
  @Test 
  public void testTernaryKthStatisticPartitioners () {
    ArrayList<KthStatisticPartitioner> kStats 
      = new ArrayList<KthStatisticPartitioner>();

    kStats.add(new Algorithm489Partitioner(nix, bkx, bkx));
    kStats.add(new Algorithm489Partitioner(nix, lkx, rkx));
    kStats.add(new Ternary489Partitioner(nix, lkx, rkx));
    kStats.add(new Dutch489Partitioner(nix, bkx, bkx));
    
    kStats.add(new QuickSelectPartitioner(middle, bkp, bkp, 5, kissy));
    kStats.add(new QuickSelectPartitioner(middle, kmp, kp, 5, kissy));
    kStats.add(new TernaryQuickSelectPartitioner(middle, bkp, bkp, 5, kissy));
    kStats.add(new DutchQuickSelectPartitioner(middle, bkp, bkp, 5, kissy));
    
    kStats.add(new RemedianPartitioner(bkx, bkx, 2, false));
    kStats.add(new RemedianPartitioner(lkx, rkx, 2, false));
    kStats.add(new TernaryRemedianPartitioner(bkx, bkx, 2, false));
    
    kStats.add(new RemedianPartitioner(bkx, bkx, 2, true));
    kStats.add(new RemedianPartitioner(lkx, rkx, 2, true));
    kStats.add(new TernaryRemedianPartitioner(bkx, bkx, 2, true));

    kStats.add(new MedianOfMediansPartitioner(5, bkx, bkx));
    kStats.add(new MedianOfMediansPartitioner(5, lkx, rkx));
    kStats.add(new TernaryMOMPartitioner(5, bkx, bkx));
    
    testAnimalFarmKthStatPartitioners(kStats, 65535, 10);
  }
  @Test 
  public void testQuintaryKthStatisticPartitioners() {
    ArrayList<KthStatisticPartitioner> kStats 
      = new ArrayList<KthStatisticPartitioner>();
    
    kStats.add(new MultiPivotQuickSelectPartitioner());
    kStats.add(
      new MultiPivotQuickSelectPartitioner(
        new BirdsOfAFeatherPartitioner(
          new SkippyPartitioner2())));

    kStats.add(new FridgePartitioner(nix, lkx, lkx));
    kStats.add(new FridgePartitioner(nix, bkx, bkx));
    kStats.add(new FridgePartitioner(nix, lkx, rkx));
    
    kStats.add(new QuintaryFloydRivestPartitioner(lkx, lkx));
    kStats.add(new QuintaryFloydRivestPartitioner(bkx, bkx));
    kStats.add(new QuintaryFloydRivestPartitioner(lkx, rkx));
    
    testAnimalFarmKthStatPartitioners(kStats, 66536, 100);
  }
  @Test 
  public void testMultiPivotKthStatisticPartitioners() {
    NamedArrayList<KthStatisticPartitioner> kStats 
      = new NamedArrayList<KthStatisticPartitioner>();
    kStats.add("MP-C-BK", new MultiPivotQuickSelectPartitioner(
        new CleanMultiPivotPositionalSelector(1),
          new SingleToMultiPartitioner(
            new BalancedSkippyPartitioner())));
    kStats.add("MP-C-BOF(BK)", new MultiPivotQuickSelectPartitioner(
        new CleanMultiPivotPositionalSelector(1),
          new BirdsOfAFeatherPartitioner(
            new SingleToMultiPartitioner(
              new BalancedSkippyPartitioner()))));
    kStats.add("MP-C-KP2",
        new MultiPivotQuickSelectPartitioner(
          new CleanMultiPivotPositionalSelector(2),
          new SkippyPartitioner2()));
    kStats.add("MP-C-BOF(KP2)",
      new MultiPivotQuickSelectPartitioner(
        new CleanMultiPivotPositionalSelector(2),
        new BirdsOfAFeatherPartitioner(
          new SkippyPartitioner2())));
    kStats.add("MP-C-KP3",
      new MultiPivotQuickSelectPartitioner(
          new CleanMultiPivotPositionalSelector(3),
          new SkippyPartitioner3()));
    kStats.add("MP-C-BOF(KP3)",
      new MultiPivotQuickSelectPartitioner(
        new CleanMultiPivotPositionalSelector(3),
        new BirdsOfAFeatherPartitioner(
          new SkippyPartitioner3())));
    kStats.add("MP-C-KP4",
      new MultiPivotQuickSelectPartitioner(
          new CleanMultiPivotPositionalSelector(4),
          new SkippyPartitioner4()));
    kStats.add("MP-C-BOF(KP4)",
      new MultiPivotQuickSelectPartitioner(
        new CleanMultiPivotPositionalSelector(4),
        new BirdsOfAFeatherPartitioner(
          new SkippyPartitioner4())));
    kStats.add("MP-C-KP5",
      new MultiPivotQuickSelectPartitioner(
          new CleanMultiPivotPositionalSelector(5),
          new SkippyPartitioner5()));
    kStats.add("MP-C-BOF(KP5",
      new MultiPivotQuickSelectPartitioner(
        new CleanMultiPivotPositionalSelector(5),
        new BirdsOfAFeatherPartitioner(
          new SkippyPartitioner5())));
    runCount=1000;
    testKStatPartitioners
      ( kStats, 10, 1000000, 0.5, 0, true);
  }
  
  public void testAnimalFarmKthStatPartitioners
    ( ArrayList<KthStatisticPartitioner> kStats, int n, int runCount ) {
    warmUp(kStats);
    System.out.println
      ( "Mean time (milliseconds) for kthStatisticPartitioner" 
      + " to find median of degenerate " + n
      + " integer inputs");
    String header = "n";
    for (KthStatisticPartitioner party : kStats) {
      header += "\t" + shortName(party.toString());
    }
    System.out.println(header);
    
    for (int d = 1; d < n; d += (d < 10) ? 1 : (d / 10) ) {
      double[] nanoTimes = new double[kStats.size()];
      for (int run = 0; run < runCount; ++run) {
        int[] crap = degen.permutationOfDupes(n, d);
        int i = 0;
        for (KthStatisticPartitioner ksp : kStats) {
          try {
            int[] copyOfCrap = Arrays.copyOf(crap, n);
            nanoTimes[i] 
              += timeOneKthStatisticPartition
                 ( copyOfCrap, ksp, n / 2 );
            ++i;
          } catch (Exception e) {
            System.out.println
              ( "Exception thrown by " + ksp.toString() 
              + " with d=" + d);
            throw e;
          }
        }
      }
      String s = padTo ( "equal" + d
                       , 5 + (int) Math.log10(n));
      for (int i = 0; i < kStats.size(); ++i) {
        double avgTime = nanoTimes[i] / (double) runCount;
        s += "\t" + Math.floor(avgTime / 1000.0 + .5) / 1000.0;
      }
      System.out.println(s);
    }
    System.out.println("");
  }
  private String padTo(String s, int d) {
    while (s.length() < d) {
      s += " ";
    }
    return s;
  }
  @Test
  public void testKthStatisticPartitionersOnTheEdge() {
    //255, 100000
    //4095, 10000.
    //65535, 1000
    int n=65535;
    runCount = 100;
    ArrayList<KthStatisticPartitioner> kStats
      = new ArrayList<KthStatisticPartitioner>();
    kStats.add(new Algorithm489Partitioner(nix, lkx, rkx));
    kStats.add(new SimplifiedFloydRivestPartitioner(kx2));
    kStats.add(new LazySimplifiedFloydRivestPartitioner(lkx, rkx));
    kStats.add(new FloydRivestPartitioner(nix, lkx, rkx));
    kStats.add(new FridgePartitioner(nix, lkx, rkx));
    kStats.add(new QuickSelectPartitioner(middle, single, single, 5, kissy));
    kStats.add(new QuickSelectPartitioner(middle, kp, kmp, 5, kissy));
    kStats.add(new RemedianPartitioner(lkx, rkx, 2, false));
    kStats.add(new RemedianPartitioner(lkx, rkx, 2, true));
    kStats.add(new RemedianQuickSelectPartitioner());
    kStats.add(new RemedianQuickSelectPartitioner(true, false, false));
    kStats.add(new MedianOfMediansPartitioner(5, lkx, rkx));
    warmUp(kStats);
    System.out.println("Mean time (milliseconds) for kthStatisticPartitioner" + " to find median of random " + n
        + " integer inputs");
    System.out.println("kHat\tA489\tF-R\tFRJ\tQS(s)\tQS(k)\tRemedian(U)\tRemedian(B)\tRemedian(C)\tMOM");
    for (double kHat100 = 0; kHat100<=100; ++kHat100 ) {
      int k = (int) Math.floor( kHat100 * 0.01 * n + 0.5) ;
      if (n<=k) k=n-1;
      double[] nanoTimes = new double[kStats.size()];
      for (int run = 0; run < runCount; ++run) {
        int[] crap = random.randomPermutation(n);
        int i = 0;
        for (KthStatisticPartitioner ksp : kStats) {
          try {
            int[] copyOfcrap = Arrays.copyOf(crap, n);
            nanoTimes[i] += 
              timeOneKthStatisticPartition
              ( copyOfcrap, ksp, k );
            ++i;
          } catch (Exception e) {
            System.out.println
            ( "Exception thrown by " + ksp.toString() 
            + " with kHat100=" + kHat100);
            throw e;
          }
        }
      }
      String s = "" + ((kHat100 * 0.01) + "00").substring(0,4);
      for (int i = 0; i < kStats.size(); ++i) {
        double avgTime = nanoTimes[i] / (double) runCount;
        s += "\t" + Math.floor(avgTime / 100.0 + .5) / 10000.0;
      }
      System.out.println(s);
    }
    System.out.println("");
    
  }
  
  
  private void warmUp(ArrayList<KthStatisticPartitioner> kStats) {
    for (int i = 0; i < 2500; ++i) {
      int[] junk = random.randomPermutation(1000);
      for (KthStatisticPartitioner ksp : kStats) {
        int[] crap = Arrays.copyOf(junk, junk.length);
        ksp.partitionRangeExactly(crap, 0, crap.length, crap.length / 2);
        crap = Arrays.copyOf(junk, junk.length);
        ksp.partitionRangeExactly(crap, 0, crap.length, crap.length / 4);
        crap = Arrays.copyOf(junk, junk.length);
        ksp.partitionRangeExactly(crap, 0, crap.length, crap.length * 3 / 4);
      }
    }
  }

  public void testTwoHeapKthStatisticPartitioner() {
    int n = 1000000;
    KthStatisticPartitioner h2kp = new DualHeapPartitioner();
    for (int d = n / 50; d <= n / 2; d += n / 50) {
      int[] crap = random.randomPermutation(n);
      h2kp.partitionRangeExactly(crap, 0, crap.length, d);
    }
  }

  public void testTwoHeapPerformance() {
    NamedArrayList<KthStatisticPartitioner> kStats 
      = new NamedArrayList<KthStatisticPartitioner>();
    kStats.add("MOM", new MedianOfMediansPartitioner(5, lkx, rkx));
    kStats.add("DualHeap", new DualHeapPartitioner());
    kStats.add("A489", new Algorithm489Partitioner(nix, lkx, rkx));
    warmUp(kStats.getArrayList());
    int k = (medianCount + 1)/2;
    testSpecificKthStatisticPartitionersOnDegenerateInputs
      ( kStats, medianCount, k, true, true );
    testSpecificKthStatisticPartitioners(kStats, 128);
  }

  @Test
  public void testMOM3Again() {
    SkippyCentripetalExpander kcx = new SkippyCentripetalExpander();
    NamedArrayList<KthStatisticPartitioner> kStats 
      = new NamedArrayList<KthStatisticPartitioner>();
    MedianOfMediansMultiPartitioner mom3 = new MedianOfMediansMultiPartitioner();
    kStats.add("MOM3", mom3);
    kStats.add("MOM5", new MedianOfMediansPartitioner(5, kcx, kcx));
    kStats.add("A489", new Algorithm489Partitioner(nix, kcx, kcx));
    warmUp(kStats.getArrayList());
    int k = (medianCount + 1)/2;
    testSpecificKthStatisticPartitionersOnDegenerateInputs
      (kStats, medianCount, k, true, true);
    testSpecificKthStatisticPartitioners(kStats, 128);
  }

  @Test
  public void testQuickSelectComparisonCounts() {
    testQuickSelectComparisonCounts(new int[] { 0, 1, 3, 7, 15 }, false);
  }

  public void testQuickSelectComparisonCounts(int[] oversamples, boolean countMoves) {
    int m = 10000;
    int r = 1000;
    int s = m / 100;
    System.out
        .println("Amortized " + (countMoves ? "moves" : "comparisons") + " for quickselect, given x=k/m and s=(c+1)/2");
    String header = "k";
    for (Integer oversample : oversamples) {
      header += "\ts=" + oversample;
    }
    System.out.println(header);
    SingletonPartitioner party = new SingletonPartitioner();
    for (int k = s; k < m; k += s) {
      String line = "" + (double) k / (double) m;
      for (Integer oversample : oversamples) {
        int c = 1 + 2 * oversample;
        SinglePivotSelector selector 
         = (0 < oversample) 
           ? new CleanPositionalSampleSelector(c) // note: update code below if you change this
           : new FirstElementSelector();
        double sampleComparisons = 0; // really:
                                      // c*(Math.log(c)-1.0)/Math.log(2);
        double sampleMoves = 0; // really: sampleComparisons/3;
        double comparisons = 0;
        double moves = 0;
        for (int i = 0; i < r; ++i) {
          int[] data = random.randomPermutation(m);
          int lhs = 0;
          int rhs = m;
          int p;
          do {
            p = party.partitionRange(data, lhs, rhs, selector.selectPivotIndex(data, lhs, rhs));
            // note: sampling costs estimated. Change if sampling selector
            // changed.
            comparisons += (rhs - lhs - 1) + sampleComparisons;
            moves += (2 * (p - lhs) * (rhs - p - 1)) / (rhs - lhs) + sampleMoves;
            if (k < p)
              rhs = p;
            else
              lhs = p + 1;
          } while (p != k);
        }
        double cCost = (double) comparisons / (double) m / (double) r;
        double mCost = (double) moves / (double) m / (double) r;
        line += "\t";
        line += Math.floor((countMoves ? mCost : cCost) * 1000.0) / 1000.0;
      }
      System.out.println(line);
    }
  }

  @Test
  public void testTwoPivotQuickSelectComparisonCounts() {
    testTwoPivotQuickSelectComparisonCounts(0);
  }

  public void testTwoPivotQuickSelectComparisonCounts(int oversample) {
    int m = 1000001;
    int r = 100;
    int s = m / 100;
    int runs = 0;
    int c = 2 + 3 * oversample;
    MultiPivotSelector selector = new DirtyMultiPivotPositionalSelector(c);
    double sampleComparisons = c * (Math.log(c) - 1.0) / Math.log(2);
    double sampleMoves = sampleComparisons / 3;
    double totalComparisons = 0;
    double totalMoves = 0;
    for (int k = 0; k < m; k += s) {
      int comparisons = 0;
      int moves = 0;
      for (int i = 0; i < r; ++i) {
        int[] data = random.randomPermutation(m);
        int lhs = 0;
        int rhs = m;
        do {
          int[] pivots = selector.selectPivotIndices(data, lhs, rhs);
          MultiPivotUtils.tryToMovePivotsAside(data, pivots, new int[] { lhs, rhs - 1 });
          int[] p = twoPivotPartitionRange(data, lhs, rhs);
          comparisons += p[2] + sampleComparisons;
          moves += p[3] + sampleMoves;
          if (k < p[0])
            rhs = p[0];
          else if (k < p[1]) {
            lhs = p[0] + 1;
            rhs = p[1];
          } else
            lhs = p[1] + 1;
        } while (lhs <= k && k < rhs && lhs < rhs);
        ++runs;
      }
      double cCost = (double) comparisons / (double) m / (double) r;
      double mCost = (double) moves / (double) m / (double) r;
      System.out.println("" + Math.floor((double) k / (double) m * 100.0 + .5) / 100.0 + "\t"
          + Math.floor(cCost * 1000.0 + .5) / 1000.0 + "\t" + Math.floor(mCost * 1000.0 + .5) / 1000.0);
      totalComparisons += comparisons;
      totalMoves += moves;
    }
    double cCost = (double) totalComparisons / (double) m / (double) runs;
    double mCost = (double) totalMoves / (double) m / (double) runs;
    System.out.println(
        "Av" + "\t" + Math.floor(cCost * 1000.0 + .5) / 1000.0 + "\t" + Math.floor(mCost * 1000.0 + .5) / 1000.0);
  }

  private int[] twoPivotPartitionRange(int[] vArray, int start, int stop) {
    // A yaroslavskiy partitioner, that counts moves and comparisons
    int vP = vArray[start];
    int vQ = vArray[stop - 1];
    int moves = 0;
    int comparisons = 1;
    if (vQ < vP) {
      int vX = vP;
      vP = vQ;
      vQ = vX;
      moves = 2;
    }
    int lhs = start + 1; // first element that might be >= vP
    int rhs = stop - 1; // last large item that is known to
                        // be > vQ (if any found)
                        // OR the location of vQ
                        // (if no large item yet found)
    int scan = lhs;
    if (scan < rhs) {
      do {
        int v = vArray[scan];
        ++comparisons;
        if (v < vP) {
          vArray[scan] = vArray[lhs];
          vArray[lhs] = v;
          moves += 2;
          ++lhs;
        } else {
          ++comparisons;
          if (vQ < v) {
            int vRight;
            do {
              --rhs;
              vRight = vArray[rhs];
              if (rhs <= scan) {
                break;
              }
              comparisons++;
            } while (vQ < vRight);
            ++comparisons;
            if (vRight < vP) {
              vArray[scan] = vArray[lhs];
              vArray[lhs] = vRight;
              moves += 3; // includes vArray[rhs]=v below
              ++lhs;
            } else {
              vArray[scan] = vRight;
              moves += 2; // includes vArray[rhs]=v below
            }
            vArray[rhs] = v;
          }
        }
        ++scan;
      } while (scan < rhs);
    }
    // place vP
    --lhs;
    vArray[start] = vArray[lhs];
    vArray[lhs] = vP;
    // place vQ
    vArray[stop - 1] = vArray[rhs];
    vArray[rhs] = vQ;
    moves += 4;
    return new int[] { lhs, rhs, comparisons, moves };
  }

  @Test
  public void testThreePivotQuickSelectComparisonCounts() {
    testThreePivotQuickSelectComparisonCounts(1);
  }

  public void testThreePivotQuickSelectComparisonCounts(int oversample) {
    int m = 1001;
    int r = 10000;
    int s = m / 100;
    int runs = 0;
    int c = 3 + 4 * oversample;
    MultiPivotSelector selector = new DirtyMultiPivotPositionalSelector(c);
    double sampleComparisons = c * (Math.log(c) - 1.0) / Math.log(2);
    double sampleMoves = sampleComparisons / 3;
    InsertionSort isort = new InsertionSort();
    double totalComparisons = 0;
    double totalMoves = 0;
    for (int k = 0; k < m; k += s) {
      int comparisons = 0;
      int moves = 0;
      for (int i = 0; i < r; ++i) {
        int[] data = random.randomPermutation(m);
        int lhs = 0;
        int rhs = m;
        do {
          if (5 < rhs - lhs) {
            int[] pivots = selector.selectPivotIndices(data, lhs, rhs);
            MultiPivotUtils.tryToMovePivotsAside(data, pivots, new int[] { lhs, lhs + 1, rhs - 1 });
            int[] p = threePivotPartitionRange(data, lhs, rhs);
            comparisons += p[3] + sampleComparisons;
            moves += p[4] + sampleMoves;
            if (k < p[0])
              rhs = p[0];
            else if (k < p[1]) {
              lhs = p[0] + 1;
              rhs = p[1];
            } else if (k < p[2]) {
              lhs = p[1] + 1;
              rhs = p[2];
            } else
              lhs = p[2] + 1;
          } else {
            int count = rhs - lhs;
            isort.sortRange(data, lhs, rhs);
            comparisons += count * (count - 1) / 4;
            moves += count * (count - 1) / 4;
            rhs = k;
          }
        } while (lhs <= k && k < rhs && lhs < rhs);
        ++runs;
      }
      double cCost = (double) comparisons / (double) m / (double) r;
      double mCost = (double) moves / (double) m / (double) r;
      System.out.println("" + Math.floor((double) k / (double) m * 100.0 + .5) / 100.0 + "\t"
          + Math.floor(cCost * 1000.0) / 1000.0 + "\t" + Math.floor(mCost * 1000.0) / 1000.0);
      totalComparisons += comparisons;
      totalMoves += moves;
    }
    double cCost = (double) totalComparisons / (double) m / (double) runs;
    double mCost = (double) totalMoves / (double) m / (double) runs;
    System.out.println("Av" + "\t" + Math.floor(cCost * 1000.0) / 1000.0 + "\t" + Math.floor(mCost * 1000.0) / 1000.0);
  }

  public int[] threePivotPartitionRange(int[] vArray, int start, int stop) {
    int[] p = new int[3];
    p[0] = vArray[start];
    p[1] = vArray[start + 1];
    p[2] = vArray[stop - 1];
    (new InsertionSort()).sortRange(p, 0, 3); // We'll pretend this takes 3
    int vP = vArray[start] = p[0];
    int vQ = vArray[start + 1] = p[1];
    int vR = vArray[stop - 1] = p[2];
    int a = start + 2; // the place the next value found < vP is to go
    int b = a; // the left-hand scanning pointer
    int c = stop - 2; // the right-hand scanning pointer
    int d = c; // the place the next value found > vR is to go
    int vTemp;
    int comparisons = 3 + (stop - start - 3) * 2; // 3 for sorting vP, vQ, vR
    int moves = 0;
    for (;;) {
      while (vArray[b] < vQ) { // like LomutoPartitioner on left
        if (vArray[b] < vP) {
          vTemp = vArray[a];
          vArray[a] = vArray[b];
          vArray[b] = vTemp;
          moves += 2;
          ++a;
        }
        ++b;
      }
      while (vQ < vArray[c]) { // LomutoMirrorPartitioner on right
        if (vR < vArray[c]) {
          vTemp = vArray[c];
          vArray[c] = vArray[d];
          vArray[d] = vTemp;
          moves += 2;
          --d;
        }
        --c;
      }
      if (c <= b) {
        break; // Exit the loop, if the b and c pointers have crossed
      }
      if (vR < vArray[b]) {
        if (vArray[c] < vP) {
          vTemp = vArray[a];
          vArray[a] = vArray[b];
          vArray[b] = vTemp;
          vTemp = vArray[a];
          vArray[a] = vArray[c];
          vArray[c] = vTemp;
          moves += 4;
          ++a;
        } else {
          vTemp = vArray[b];
          vArray[b] = vArray[c];
          vArray[c] = vTemp;
          moves += 2;
        }
        vTemp = vArray[c];
        vArray[c] = vArray[d];
        vArray[d] = vTemp;
        moves += 2;
        --d;
      } else if (vArray[c] < vP) {
        vTemp = vArray[a];
        vArray[a] = vArray[b];
        vArray[b] = vTemp;
        vTemp = vArray[a];
        vArray[a] = vArray[c];
        vArray[c] = vTemp;
        ++a;
        moves += 4;
      } else {
        vTemp = vArray[b];
        vArray[b] = vArray[c];
        vArray[c] = vTemp;
        moves += 2;
      }
      ++b;
      --c;
    }
    --a;
    --b;
    ++c;
    ++d;
    RangeSortHelper.swapElements(vArray, start + 1, a);
    RangeSortHelper.swapElements(vArray, a, b);
    --a;
    RangeSortHelper.swapElements(vArray, start, a);
    RangeSortHelper.swapElements(vArray, d, stop - 1);
    moves += 8;
    return new int[] { a, b, d, comparisons, moves };
  }
  @Test
  public void testDeviationOfMedian() {
    int runs = 2000;
    double zHat = .5; // expected value of zHat, E(d)
    System.out.println("Using " + runs + " samples for each value of c");
    System.out.println("c\tTrue(z-Hat)\tApprox(z-Hat)\tMonte(z-Hat)\tw\tEc\tCs(0.5)\tE(d)");
    for (int c = 1; c < 200; c += 2) {
      zHat = zHat * c / (c + 1);
      // d is the product of the odd numbers up to c, divided by
      // twice the product of the even numbers up to c+1.
      double sum = 0;
      double[] data = new double[c];
      for (int r = 0; r < runs; ++r) {
        for (int j = 0; j < c; ++j) {
          data[j] = Math.random();
        }
        Arrays.sort(data);
        double m = data[c / 2];
        sum += (m < .5) ? (.5 - m) : (m - .5);
      }
      double w = .5 / (.5 - zHat); // wasted comparisons
      double efficiency // partitioner efficiency/ using median-of-c pivot
          = (Gain.harmonic(c + 1) - Gain.harmonic((c + 1) / 2)) / Math.log(2);
      double tMedian = w + 1 / efficiency; // time to find median
      double zHatApprox = Math.sqrt(0.5 / Math.PI / (c + 1));
      double zHatActual = sum / (double) runs;
      System.out.println(
          "" + c + "\t" + zHat + "\t" + zHatApprox + "\t" + zHatActual + "\t" + w + "\t" + efficiency + "\t" + tMedian);
    }
  }
  @Test
  public void testAllDeviates() {
    int m = 1000;
    int runs = 1000000;
    System.out.println("Predicted; Using " + runs + " samples for each value of c, a");
    System.out.println(f5(nCr(4,2)));
    for (int c = 1; c <= 21; ++c) {
      String line = "" + c;
      for (int a=1; a<=c; ++a) {
        double zHat = (double)a*(c+1-a)/(c+1)/(c+1)*nCr(c+1,(c+1)/2) / Math.pow(2.0, c);
        line += f5(zHat*m);
      }
      System.out.println(line);
    }
    System.out.println("");
    System.out.println("Actual; Using " + runs + " samples for each value of c, a");    
    for (int c = 1; c <= 21; ++c) {
      double[] data = new double[c];
      double[] lowDeviations = new double[c];
      double[] highDeviations = new double[c];
      for (int r = 0; r < runs; ++r) {
        for (int a = 1; a <= c; ++a) {
          data[a-1] = 1 + Math.floor(Math.random() * m);
        }
        Arrays.sort(data);
        for (int a=1; a<=c; ++a) {
          double x = (double)(a)*(double)(m+1)/(double)(c+1);
          if (data[a-1]<x) {
            lowDeviations[a-1]  += (x-data[a-1]) / (double)runs;
          } else {
            highDeviations[a-1] += (data[a-1]-x) / (double)runs;
          }
        }
      }
      String line = "" + c;
      for (int a=1; a<=c; ++a) {
        line += f5(lowDeviations[a-1] + highDeviations[a-1]);
      }
      System.out.println(line);
    }
  }
  public double nCr(int n, int r) {
    double p = 1;
    if (r>n-r) r=n-r;
    for (int i=1; i<=r; ++i) {
      p *= (double)(n+1-i);
      p /= (double)i;
    }
    return p;
  }
  
  public double nHr(int n, int r) {
    double p =1;
    if (r>n-r) r=n-r;
    for (int i=n-r; i<=n; ++i) {
      p *= (double)i;
    }
    for (int i=1; i<=r; ++i) {
      p /= (double)i;
    }
    return p;
  }
  
  @Test
  public void testDeviationOfTertile() {
    int runs = 1000;
    int p = 2;
    double x = 1 / 3.0;
    for (int c = 2; c < 100; c += 3) {
      double sum = 0;
      double dev; // deviation: the true value of d
      double[] data = new double[c];
      for (int r = 0; r < runs; ++r) {
        for (int j = 0; j < c; ++j) {
          data[j] = Math.random();
        }
        Arrays.sort(data);
        double m = data[c / (p + 1)];
        double m2 = data[p * c / (p + 1)];
        sum += Math.abs(x - (m2 - m));
      }
      dev = sum / (double) runs;
      System.out.println("" + c + "\t" + dev);
    }
  }

  @Test
  public void testMOMAndApplePie() {
    runCount = 1000;
    NamedArrayList<KthStatisticPartitioner> kStats 
      = new NamedArrayList<KthStatisticPartitioner>();
    PartitionerTest partitionerTest = new PartitionerTest();
    for (SinglePivotPartitioner party : partitionerTest.getLotsOfPartitioners()) {
      PartitionExpander x = new PretendExpander(party);
      kStats.add("MOM-" + x .toString(), new MedianOfMediansPartitioner(5, x, x));
    }
    warmUp(kStats.getArrayList());
    testSpecificKthStatisticPartitioners(kStats, 128);
    // testSpecificKthStatisticPartitionersOnDegenerateInputs(kStats,
    // medianCount);
  }

  @Test
  public void testPollyanna() {
    KthStatisticPartitioner polly = new PollyannaPartitioner();
    int[] vArray = random.randomPermutation(10000);
    polly.partitionRangeExactly(vArray, 0, vArray.length, vArray.length / 2);
  }

  @Test
  public void testRetribution() {
    runCount = 10000;
    for (double m = 1; m <= 9; ++m) {
      for (double c = 1; c < m; ++c) {
        for (double a = 1; a <= c; ++a) {
          double sumOfSquares = 0;
          double sum = 0;
          for (int r = 0; r < runCount; ++r) {
            int[] vPopulation = random.randomPermutation((int) m);
            InsertionSort.sortSmallRange(vPopulation, 0, (int) c);
            int v = vPopulation[(int) a - 1];
            double x = a;
            for (int k = (int) c; k < (int) m; ++k) {
              x += (vPopulation[k] < v) ? 1 : 0;
            }
            sumOfSquares += x * x;
            sum += x;
          }
          double mean = sum / runCount;
          double variance = sumOfSquares / runCount - mean * mean;
          double v2 = a * (c + 1.0 - a) / (c + 1) / (c + 1) / (c + 2) * (m + 1) * (m - c);
          System.out.println("" + m + "\t" + c + "\t" + a + "\t" + mean + "\t" + variance + "\t" + v2);
        }
      }
    }
  }
  @Test
  public void testHypergeometricBounds() {
    runCount = 1000000;
    int m = 1000;
    int c = 31;
    int grain = 5;
    int rows = (m + grain - 1) / grain;
    int[][] counts = new int[c][rows];
    for (int r = 0; r < runCount; ++r) {
      int[] vSample = random.randomOrderedCombination(m, c);
      for (int a = 1; a <= c; ++a) {
        ++counts[a - 1][vSample[a - 1] / grain];
      }
    }
    for (int row = 0; row < rows; ++row) {
      String line = "" + row * grain;
      for (int a = 1; a <= c; ++a) {
        line += "\t" + (double) counts[a - 1][row] / (double) runCount;
      }
      System.out.println(line);
    }
  }

  @Test
  public void testExchangeCounts() {
    runCount = 10000;
    int m = 9;
    int k = 4;
    double sumX = 0;
    double sumOfSquaresOfX = 0;
    for (int r = 0; r < runCount; ++r) {
      int[] vPerm = random.randomPermutation(m);
      for (int i = 0; i < m; ++i) {
        if (vPerm[i] == k) {
          vPerm[i] = vPerm[0];
          vPerm[0] = k;
          break;
        }
      }
      int x = 0; // exchanges partitioning this permutation
      for (int i = 1; i <= k; ++i) {
        x += (vPerm[i] > k) ? 1 : 0;
      }
      sumX += x;
      sumOfSquaresOfX += x * x;
    }
    double meanX = sumX / runCount;
    double variance = sumOfSquaresOfX / (double) runCount - meanX * meanX;
    double stdDev = Math.sqrt(variance);
    System.out.println("mean=" + meanX + ", variance=" + variance + ", stddev=" + stdDev);
    System.out.println("SumOfSquares=" + sumOfSquaresOfX + ", mean*mean=" + (meanX * meanX));
    System.out.println("(k-1).(m-k)/m=" + (double) k * (double) (m - k) / (double) (m + 1));
  }
  public String f5(double x) {
    return "\t" + (Math.floor(x * 100000.0 + .5) / 100000.0);
  }
  public double bitsOfChaos(double n) {
    if (n<2) return 0;
    if (n==2) return 1.0;
    return n * (Math.log(n) - 1.0) / Math.log(2.0);
  }
  @Test
  public void testFR() {
    int[] vArray = new int[] { 5, 1, 4, 3, 7, 8, 6, 2, 0, 9 };
    (new FloydRivestPartitioner(nix, lkx, rkx))
    .partitionRangeExactly
     ( vArray 
     , 0, vArray.length, 6);
    DumpRangeHelper.dumpArray("out", vArray);
  }
  @Test
  public void testA489R() {
    int[] vInput = new int[]
      { 24, 17, 28, 29, 32, 13, 10, 11, 3, 26, 12, 14, 7, 21, 6, 9, 15, 1, 22, 30, 20, 27, 19, 8, 23, 18, 25, 5, 0
      , 2, 16, 31, 4 };
    Algorithm489Partitioner a489r 
      = new Algorithm489Partitioner
            ( nix, new PartitionerToExpander(kp)
            , new PartitionerToExpander(kmp));
    CheckedKthStatisticPartitioner ck 
      = new CheckedKthStatisticPartitioner(a489r);
    //a489r.setCheckOnExpanders();
    ck.partitionRangeExactly(vInput, 0, vInput.length, 17);
  }
  @Test 
  public void TestQSTukey() {
    int[] vInput = new int[] { 6, 0, 3, 2, 5, 4, 1, 7, 8, 9 };
    QuickSelectPartitioner qs
      = new QuickSelectPartitioner
            ( new CleanTukeySelector(), rev, rev, 5, kissy );
    qs.enablePartitionerChecking();
    CheckedKthStatisticPartitioner ck 
      = new CheckedKthStatisticPartitioner(qs);
    ck.partitionRangeExactly(vInput, 0, vInput.length, 6);
  }
  
  @Test
  public void testSmallMPerformance() {
    runCount = 10000;
    NamedArrayList<KthStatisticPartitioner> kStats 
      = new NamedArrayList<KthStatisticPartitioner>();
    kStats.add("MOM-R",   new MedianOfMediansPartitioner
                              ( 5, new Repartitioner(bkx), new Repartitioner(bkx) ) );
    kStats.add("MOM-X",   new MedianOfMediansPartitioner(5, bkx, bkx));
    kStats.add("MOR-X",   new RemedianPartitioner(lkx, rkx, 2, true));
    kStats.add("MOR-X-T", new TernaryRemedianPartitioner(lkx, rkx, 2, true));
    kStats.add("MOR-X-D", new DutchRemedianPartitioner(lkx, rkx, 2, true));
    kStats.add("MOR5-X",  new GeneralRemedianPartitioner(lkx, lkx, 5, 2, true));
    kStats.add("MOR25-X",  new GeneralRemedianPartitioner(lkx, lkx, 25, 2, true));
    kStats.add("QS-X",    new QuickSelectPartitioner
                              ( middle, bkp, bkp, 5, kissy ) );
    kStats.add("QS-3",    new TernaryQuickSelectPartitioner
                              ( middle, bkp, bkp, 5, kissy ) );
    kStats.add("QS-BO3",  new QuickSelectPartitioner
                              ( new CleanSingletonSelector(), bkp, bkp, 5, kissy ) );
    kStats.add("QS-T",    new QuickSelectPartitioner
                              ( new CleanTukeySelector(), bkp, bkp, 5, kissy ) );
    kStats.add("QS-T-RS", new QuickSelectPartitioner
                              ( new CleanTukeySelector(), rev, rev, 5, kissy ) );
    kStats.add("QS-STL",  new MultiPivotQuickSelectPartitioner
                              ( new SingleToMultiSelector(new CleanTukeySelector()) 
                              , new STLTernaryPartitioner()));
    kStats.add("HM",      new HalfMeasurePartitioner(bkx, bkx, 1, true));
    kStats.add("A489-R",  new Algorithm489Partitioner
                              ( nix, new PartitionerToExpander(kp)
                              , new PartitionerToExpander(kmp)));
    kStats.add("A489-X",  new Algorithm489Partitioner(nix, lkx, rkx));
    kStats.add("A489-RA", new Algorithm489Partitioner
                              ( new RauhAndArceSampleSelector(false), nix, lkx, rkx ) );
    kStats.add("A489-A",  new Algorithm489Partitioner
                              ( new Afterthought489SampleSelector(), nix, lkx, rkx ) );
    kStats.add("Zero",    new ZeroDelta489Partitioner(nix, lkx, rkx));
    kStats.add("DP",      new Derivative489Partitioner(nix, lkx, rkx));
    kStats.add("DP-P",    new Derivative489Partitioner(pos, lkx, rkx));
    kStats.add("SFR",     new SimplifiedFloydRivestPartitioner(kx2));
    kStats.add("LSFR",    new LazySimplifiedFloydRivestPartitioner(lkx, rkx));
    kStats.add("FR",      new FloydRivestPartitioner(nix, lkx, rkx));
    kStats.add("FRJ",     new FridgePartitioner(nix, lkx, rkx));
    kStats.add("FRJ-I5",  new FridgePartitioner(nix, lkx, rkx, true));
    testKStatPartitioners(kStats, 10, 1000000, 0.5, 0, true);
  }
  @Test
  public void testDerivativePartitioner() {
    Derivative489Partitioner dp 
      = new Derivative489Partitioner(nix, lkx, rkx);
    int[] vArray = random.randomPermutation(10000);
    dp.partitionRangeExactly(vArray, 0, vArray.length, vArray.length/2);
  }
  @Test
  public void testKislitsynPartitioner() {
    /*
    int m  = 50;
    int k1 = m/4;
    int k3 = m - k1;
    int k2 = k1 + (k3-k1)/2;
    int[] vArray = random.randomPermutation(m);    
    DumpRangeHelper.dumpArray("Input", vArray);
    System.out.println("k1=" + k1 + ", k2=" + k2 + ", k3=" + k3);
    kp.partitionRangeExactly(vArray, 0,  m, k1-1);
    DumpRangeHelper.dumpArray("K1", vArray);
    kp.partitionRangeExactly(vArray, k1, m, k3-1);
    DumpRangeHelper.dumpArray("K1,K3", vArray);
    kp.partitionRangeExactly(vArray, k1, k3-1, k2-1);
    DumpRangeHelper.dumpArray("K1,K2,K3", vArray);
    */
    int oldMaxMedianCount = maxMedianCount;
    maxMedianCount = 1024;
    NamedArrayList<KthStatisticPartitioner> kStats 
      = new NamedArrayList<KthStatisticPartitioner>();
    KislitsynPartitioner kissy
      = new KislitsynPartitioner();
    TweakedKislitsynPartitioner kissy2
      = new TweakedKislitsynPartitioner();
    DualHeapPartitioner dh
      = new DualHeapPartitioner();
    QuickSelectPartitioner qs 
      = new QuickSelectPartitioner(me, single, single, 5, kissy);
    QuickSelectPartitioner qsk 
      = new QuickSelectPartitioner(me, kmp, kp, 5, kissy);
    kStats.add("Kislitsyn",               kissy);
    kStats.add("Tweaked Kislitsyn",       kissy2);
    kStats.add("DualHeap",                dh);
    kStats.add("QuickSelect(Singleton)",  qs);
    kStats.add("QuickSelect(Skippy)",   qsk);
    runCount = 100;
    runStandardKthStatTests(kStats, true, true, true, 0.5);
    maxMedianCount = oldMaxMedianCount;
    MedianOfMediansPartitioner mom
      = new MedianOfMediansPartitioner();
    BrainDeadKthStatisticPartitioner bd
      = new BrainDeadKthStatisticPartitioner();
    Derivative489Partitioner dp
      = new Derivative489Partitioner(nix, lkx, rkx);
    kStats.add("MOM(Skippy)", mom);
    kStats.add("BrainDead",  bd);
    kStats.add("Derivative",  dp);
    System.out.println("");
    runCount = 10000;
    testKStatPartitioners(kStats, 10, 1000, 0.5, 0, true);
    
  }
  @Test
  public void testSmallMPerformanceTheSequel() {
    BalancedSkippyPartitioner bkp
      = new BalancedSkippyPartitioner();
    Afterthought489SampleSelector after 
      = new Afterthought489SampleSelector(); 
    NamedArrayList<KthStatisticPartitioner> kStats 
      = new NamedArrayList<KthStatisticPartitioner>();
    kStats.add("QS",      new QuickSelectPartitioner(middle, bkp, bkp, 5, kissy));
    kStats.add("A489",    new Algorithm489Partitioner(nix, lkx, rkx));
    kStats.add("A489-FC", new FairlyCompensated489Partitioner(nix, lkx, rkx));
    kStats.add("A489-A",  new Algorithm489Partitioner(after, nix, lkx, rkx));
    kStats.add("ZD489",   new ZeroDelta489Partitioner(nix, lkx, rkx));
    kStats.add("A489-DP", new Derivative489Partitioner(nix, lkx, rkx));
    double phi = 0.5 + Math.sqrt(5.0) / 2.0;
    double oneOnPhiSquared = 1 / phi / phi;
    runCount = 10000;
    testKStatPartitioners(kStats, 10, 1000000, 0.5,             0, true);
    testKStatPartitioners(kStats, 10, 1000000, oneOnPhiSquared, 0, false);
    testKStatPartitioners(kStats, 10, 1000000, 0.211,           0, false);
  }
  @Test
  public void testSmallMPerformancePart_33_And_A_Third() {
    BalancedSkippyPartitioner bkp
      = new BalancedSkippyPartitioner();
    NamedArrayList<KthStatisticPartitioner> kStats 
      = new NamedArrayList<KthStatisticPartitioner>();
    kStats.add("QS", new QuickSelectPartitioner(middle, bkp, bkp, 5, kissy));
    kStats.add("ZD489", new ZeroDelta489Partitioner(nix, lkx, rkx));
    kStats.add("DP", new Derivative489Partitioner(nix, lkx, rkx));
    double phi = 0.5 + Math.sqrt(5.0) / 2.0;
    double oneOnPhiSquared = 1 / phi / phi;
    runCount = 10000;
    testKStatPartitioners(kStats, 10, 65000, 0.5,             0, true);
    testKStatPartitioners(kStats, 10, 65000, oneOnPhiSquared, 0, false);
    testKStatPartitioners(kStats, 10, 65000, 0.211,           0, false);
  }
  @Test
  public void testOvercompensationCost() {
    NamedArrayList<KthStatisticPartitioner> kStats 
      = new NamedArrayList<KthStatisticPartitioner>();
    runCount = 100000;
    kStats.add("A489",    new Algorithm489Partitioner(nix, lkx, rkx));
    kStats.add("A489-FC", new FairlyCompensated489Partitioner(nix, lkx, rkx));
    testKStatPartitioners(kStats, 10, 100000, 0.5, -1, true);
  }
  @Test
  public void testProbeMORBug() {
    int[] vData = new int[]
    { 131, 115, 112, 6, 121, 68, 141, 9, 149, 127, 151, 155, 87, 17, 161, 174, 77, 21, 159, 167, 156, 94, 67, 132
    , 2, 30, 4, 66, 42, 129, 48, 144, 56, 116, 61, 52, 92, 41, 40, 102, 81, 126, 176, 171, 178, 18, 137, 95, 154, 1, 75, 19
    , 110, 59, 157, 28, 74, 119, 106, 165, 173, 55, 168, 143, 46, 54, 45, 63, 139, 5, 57, 64, 175, 136, 38, 104, 13, 88, 150
    , 107, 99, 25, 145, 113, 164, 73, 160, 83, 117, 146, 162, 135, 26, 37, 123, 3, 170, 114, 169, 70, 22, 11, 111, 124, 142
    , 109, 91, 101, 24, 82, 16, 147, 78, 163, 72, 43, 44, 12, 105, 47, 50, 53, 36, 71, 27, 34, 20, 29, 98, 134, 58, 120, 122
    , 90, 33, 69, 172, 100, 152, 51, 79, 130, 93, 0, 148, 138, 80, 8, 62, 133, 177, 97, 76, 65, 10, 86, 140, 158, 49, 125
    , 128, 89, 108, 103, 85, 166, 31, 153, 23, 14, 7, 39, 118, 35, 84, 96, 32, 60, 15 };
    RemedianPartitioner mor = new RemedianPartitioner();
    int m = vData.length;
    int k = 90;
    mor.partitionRangeExactly(vData, 0, m, k);
    DumpRangeHelper.dumpRange("Output", vData, 0, k);
    DumpRangeHelper.dumpRange("Middle", vData, k, k+1);
    DumpRangeHelper.dumpRange("Output", vData, k+1, m);
  }
  @Test
  public void testProbeDHBug() {
    int[] vData = { 0, 8, 9, 5, 4, 7, 6, 2, 3, 1 };
    DualHeapPartitioner dh 
      = new DualHeapPartitioner();
    int m = vData.length;
    int k = 6;
    dh.partitionRangeExactly(vData, 0, m, k);
    DumpRangeHelper.dumpRange("Output", vData, 0, k);
    DumpRangeHelper.dumpRange("Middle", vData, k, k+1);
    DumpRangeHelper.dumpRange("Output", vData, k+1, m);
  }
  
  public void testKStatPartitioners
    ( NamedArrayList<KthStatisticPartitioner> kStats
    , int lo, int hi, double kHat
    , int twitch, boolean bHeader) {
    warmUp(kStats.getArrayList());
    String header = "m";
    for (String name : kStats.getNames()) {
      header += "\t" + name;
    }
    if (bHeader) {
      System.out.println(header);
    } else {
      System.out.println("");
    }
    for (int m = lo; m < hi; m = m * 11 / 10) {
      int k = (int) Math.floor((m+1)*kHat-.5 + twitch);
      if (k<0) {
        k = 0;
      }
      if (m<=k) {
        k = m -1;
      }
      int runs = (m<20000) ? runCount : runCount/10;
      double[] times = new double[kStats.size()];
      for (int r=0; r<runs; ++r) {
        int[] vPerm = random.randomPermutation(m);
        for (int p = 0; p < kStats.size(); ++p) {
          int[] vCopy = Arrays.copyOf(vPerm, vPerm.length);
          KthStatisticPartitioner party = kStats.get(p);
          long startTime = System.nanoTime();
          party.partitionRangeExactly(vCopy, 0, m, k);
          long stopTime  = System.nanoTime();
          times[p] += (double) (stopTime - startTime);
          if ( vCopy[k] != k ) {
            if (m<200) {
              DumpRangeHelper.dumpArray("Input", vPerm);
              DumpRangeHelper.dumpRange("Output", vCopy, 0, k);
              DumpRangeHelper.dumpRange("Output", vCopy, k+1, m);
            }
            throw new 
              SortingFailureException
              ("\nRun " + r + " with m=" + k +", k=" + k 
              + " of " + kStats.getName(p) 
              + " failed:\n copy[" + (k) + "]"
              + " was " + vCopy[k]); 
          }
          checker.checkPartition
            ( "\n" + kStats.getName(p) + "(run " + r + " with k=" + k
            , vCopy, 0, k, m);
        }
      }
      double gain = ( bitsOfChaos(m)
                      - bitsOfChaos(k-1)
                      - bitsOfChaos(m-k) )
                  * (double)runs;
      String line = "" + m;
      for (int p = 0; p < kStats.size(); ++p) {
        line += f5(gain/times[p]);
      }
      System.out.println(line);
    }
  }
  @Test
  public void testJackpotRate() {
    for (int c=1;c<=99;c+=2) {
      tjr((c+1)/2, c, 50, 99, 100000);
    }
  }
  public void tjr(int a, int c, int k, int m, int runs) {
    double jackpots = 0;
    for (int r=0; r<runs; ++r) {
      int[] sample = random.randomOrderedCombination(m, c);
      if ( sample[a-1] == (k-1)) {
        ++jackpots;
      }
    }
    System.out.println("" + a + "\t" + c + "\t" + k + "\t" + m 
        + "\t" + Math.floor(jackpots*100000.0/runs)/100000.0 
        + "\t" + (double)(c-1)/(double)(m-1));
  }
  int costToFindKthOfM(int k, int m) {
    if (k+k<m+1) {
      return m + k - 1;
    } else {
      return m + m - k;
    }
  }
  double benefitOfFindingXLookingForKthOfM
    (int x, int k, int m) {
    double startCost = costToFindKthOfM(k,m);
    if ( x < k ) {
      return startCost - costToFindKthOfM(k-x, m-x);
    } else if ( k < x ) {
      return startCost - costToFindKthOfM(k, x-1);
    } else {
      return startCost;
    }
  }
  @Test
  public void testBenefitVersusLead() {
    int m = 1001;
    int c = 191;
    int k = 200;
    double benefits[] = new double[c];
    double xbar[]     = new double[c];
    int runs = 10000;
    for (int r=0; r<runs; ++r ) {
      int[] vSample = 
          random.randomOrderedCombination(m,c);
      for (int a=1; a<=c; ++a) {
        int x = vSample[a-1] + 1;
        benefits[a-1] += 
            benefitOfFindingXLookingForKthOfM(x,k,m);
        xbar[a-1] += x;
      }
    }
    for (int a=1; a<=c; ++a ) {
      int x = (int)Math.floor( xbar[a-1] / (double)runs + .5);
      double benefit = benefitOfFindingXLookingForKthOfM(x,k,m);
      System.out.println("" + a 
        + "\t" + benefits[a-1] / (double)runs
        + "\t" + benefit //if x==xbar
        + "\t" + x);
    }
  }
  @Test
  public void testFindDeltaCurve() {
    testFindDeltaCurve3(1001,193,10000);
  }
  @Test
  public void testFindDeltaCurve10K() {
    testFindDeltaCurve3(10001,1973,1000);
  }
  public void testFindDeltaCurve3(int m, int c, int runs) {
    double[][] benefits = new double[m+1][c];
    for (int r=0; r<runs; ++r ) {
      int[] vSample = 
          random.randomOrderedCombination(m,c);
      for (int a=1; a<=c; ++a) {
        int x = vSample[a-1] + 1;
        for (int k=1;k<=m;++k) {
          benefits[k][a-1] += 
            benefitOfFindingXLookingForKthOfM(x,k,m);
        }
      }
    }
    for (int k=1; k<=m; ++k) {
      double best = 0;
      int bestA = 0;
      for (int a=1; a<=c; ++a) {
        if (best < benefits[k][a-1] ) {
          best = benefits[k][a-1];
          bestA = a;
        }
      }
      double t = 1 + (double)(k-1)*(c-1)/(m-1);
      double delta = bestA - t;
      double cube  = 4 * t * (c+1-t) * (m-c) * (m + 1 - 2*k) 
             / 9.0 / (double)(m+1) / (double)(m+1);
      double sign = Math.signum(cube);
      double predicted = sign * Math.pow(Math.abs(cube), 1/3.0);
      double a489_delta = sign * Math.sqrt(Math.log(m)) 
                        * Math.sqrt((m+1)*(m-c)/(double)c)
                        * (double)(c+1)/(double)(m+1);
      int    a489_a = (int) Math.floor(t+a489_delta+.5);
      double a489_benefit = benefits[k][a489_a-1];
      System.out.println("" + k + "\t" + t + "\t" + delta + "\t" + predicted 
          + "\t" + a489_benefit/(double)runs/(double)m + "\t" + best/(double)runs/(double)m);
    }
  }
  @Test
  public void testFindCCurve() {
    int m    = 1001;
    int runs = 10000;
    for (int c=1; c<192; ++c)
    {
      String line = "" + c;
      String header = "c";
      for (int k : new int[] { 1,2,5,10,20,50,100,150,211,250,300,382,500 } )
      {
        double t = 1 + (double)(k-1)*(c-1)/(m-1);
        double cube  = t * (c+1-t) * (m-c) * (m + 1 - 2*k) 
             / 4.0 / (double)(m+1) / (double)(m+1);
        double sign  = Math.signum(cube);
        double delta = sign * Math.pow(Math.abs(cube), 1/3.0);
        int    a     = (int) Math.floor(t + delta + .5);
        if (c<a) a=c;
        double sampleCost = costToFindKthOfM(a, c);
        double b     = 0.0;
        for (int r=1; r<runs; ++r) 
        {
          int[] vSample = 
            random.randomOrderedCombination(m,c);
          int x = vSample[a-1] + 1;
          b += benefitOfFindingXLookingForKthOfM(x,k,m)
             - sampleCost;
        }
        b = b/(double)runs/(double)(sampleCost+m-c);
        header += "\tk=" + k;
        line   += "\t"   + Math.floor(b*10000.0+.5)/10000.0;
      }
      if (c==1) System.out.println(header);
      System.out.println(line);
    }
  }
  @Test
  public void testFindCAsFunctionOfK() {
    int m    = 1001;
    int runs = 10000;
    int maxC = 100;
    System.out.println("k\tc\tb");
    for (int k=1; k<=(m+1)/2; ++k )
    {
      double[] benefit = new double[maxC];
      for (int c=1; c<=maxC; ++c)
      {
        double t = 1 + (double)(k-1)*(c-1)/(m-1);
        double cube  = t * (c+1-t) * (m-c) * (m + 1 - 2*k) 
             / 4.0 / (double)(m+1) / (double)(m+1);
        double sign  = Math.signum(cube);
        double delta = sign * Math.pow(Math.abs(cube), 1/3.0);
        int    a     = (int) Math.floor(t + delta + .5);
        if (c<a) a=c;
        double sampleCost = costToFindKthOfM(a, c);
        double b     = 0.0;
        for (int r=1; r<runs; ++r) 
        {
          int[] vSample = 
            random.randomOrderedCombination(m,c);
          int x = vSample[a-1] + 1;
          b += benefitOfFindingXLookingForKthOfM(x,k,m)
             - sampleCost;
        }
        benefit[c-1] = b/(double)runs/(double)(sampleCost+m-c);
      }
      int c = 0;
      double b = 0;
      for (int i=1; i<=maxC; ++i) {
        if ( b < benefit[i-1] ) {
          b = benefit[i-1];
          c = i;
        }
      }
      System.out.println("" + k + "\t" + c + "\t" + b);
    }
  }
  @Test
  public void testBaseLineBForAlgorithm489() {
    int m    = 1001;
    int runs = 10000;
    int c = (int) Math.floor( Math.pow(m*m*Math.log(m), 1/3.0) + .5);
    if ((c&1)==0) {
      ++c;
    }
    System.out.println("k\tb-Their\tb-MyDelta");
    for (int k=1; k<=(m+1)/2; ++k )
    {
      double t = 1 + (double)(k-1)*(c-1)/(m-1);
      double cube  = t * (c+1-t) * (m-c) * (m + 1 - 2*k) 
           / 4.0 / (double)(m+1) / (double)(m+1);
      double sign  = Math.signum(cube);
      double myDelta = sign * Math.pow(Math.abs(cube), 1/3.0);
      int    myA     = (int) Math.floor(t + myDelta + .5);
      if (c<myA) myA=c;
      double sampleCost = costToFindKthOfM(myA, c);
      double bMine      = 0.0;
      double theirDelta = sign * Math.sqrt(c * Math.log(m));
      int    theirA     = (int) Math.floor(t + theirDelta + .5);
      if (c<theirA) theirA=c;
      double theirSampleCost = costToFindKthOfM(theirA, c);
      double bTheir = 0.0;
      for (int r=1; r<runs; ++r) 
      {
        int[] vSample = 
          random.randomOrderedCombination(m,c);
        int xMine = vSample[myA-1] + 1;
        bMine += benefitOfFindingXLookingForKthOfM(xMine,k,m);
        int theirX =vSample[theirA-1] + 1;
        bTheir += benefitOfFindingXLookingForKthOfM(theirX,k,m);
      }
      bMine = bMine/(double)runs/(double)(sampleCost+m-c);
      bTheir = bTheir/(double)runs/(double)(theirSampleCost+m-c);
      System.out.println("" + k + "\t" + bTheir + "\t" + bMine);
    }
  }
  @Test
  public void testExchangeCount() {
    int n=7;
    int runs=10000;
    for (int x=1; x<=n; ++x) {
      double exchangeCount=0;
      for (int r=1; r<runs; ++r) {
        int[] vA = random.randomPermutation(n);
        for (int i=0; i<n; ++i) {
          if (vA[i]==x-1) {
            vA[i]=vA[0];
            vA[0]=x-1;
          }
        }
        for (int i=x; i<n; ++i) {
          if (vA[i]<x) {
            ++exchangeCount;
          }
        }
      }
      System.out.println("" + x + "\t" + exchangeCount/(double)runs);
    }
  }
  @Test 
  public void testHoleInOne() {
    int c = 3;
    int a = 2;
    for (int n=3;n<101;n+=2) {
      int k = (n+1)/2;
      int runs = 10000;
      int hits = 0;
      for (int r=0;r<runs;++r) {
        int[] vSample = 
          random.randomOrderedCombination(n,c);
        if (vSample[a-1] == k-1) ++hits;
      }
      System.out.println( "" + n + "\t" + (double)hits / (double)runs);
    }
  }
  @Test
  public void testZ() {
    int c=19;
    int m=1023;
    double g=(m+1)/(double)(c+1);
    System.out.println("m=" + m + ", c=" + c + ", g=" + g + "\n");
    int runs=1000000;
    double[] lsum = new double[c];
    double[] lcount = new double[c];
    double[] rsum = new double[c];
    double[] rcount = new double[c];
    for (int r=0; r<runs; ++r) {
      int[] vSample 
        =  random.randomOrderedCombination(m,c);
      for (int a=1; a<=c; ++a) {
        double u = a * (m+1) / (double)(c+1);
        if (vSample[a-1] < u-1) {
          lsum[a-1] += vSample[a-1] + 1;
          ++lcount[a-1];
        } else if ( u-1 < vSample[a-1]) {
          rsum[a-1] += vSample[a-1] + 1;
          ++rcount[a-1];          
        }
      }
    }
    System.out.println("a\txL\tmu\txR\tPr(x<mu)\tPr(x>mu)");
    for (int a=1; a<=c; ++a) {
      double u = a * (m+1) / (double)(c+1);
      double leftX = lsum[a-1]/lcount[a-1];
      double rightX = rsum[a-1]/rcount[a-1];
      System.out.println("" + a + "\t" + leftX + "\t" + u + "\t" + rightX 
        + "\t" + lcount[a-1]/runs + "\t" + rcount[a-1]/runs );
    }
    System.out.println("");
    for (int a=1; a<=c; ++a) {
      double u = a * (m+1) / (double)(c+1);
      double leftX = lsum[a-1]/lcount[a-1];
      double leftZ = u - leftX;
      double predZ = Math.sqrt(2*a*(c+1-a)*(m+1)*(m-c)/Math.PI/(c+1)/(c+1)/(c+2));
      System.out.println("" + a + "\t" + leftZ + "\t" + predZ + "\t" + leftZ/predZ);
    }
  }
  @Test
  public void testHoleInOneCounts() {
    int m=101;
    int k=(m+1)/2;
    int runs=1000000;
    for (int c=1; c<m; c+=2) {
      int holesInOne = 0;  
      for (int r=0; r<runs; ++r) {
        int[] vSample 
          =  random.randomOrderedCombination(m,c);
        int x = vSample[ (c-1)/2 ] + 1;
        if (x==k) {
          ++holesInOne;
        }
      }
      double actual =((double)holesInOne/(double)runs);
      double predicted = (double)c/(double)m;
      if (1<c && c<m) {
        double t = (m+1<c+c) ? (m-c): (c-1);
        predicted /= Math.sqrt(Math.PI*t/2.0);
      }
      System.out.println(
        "" + c + t4(predicted) + t4(actual) );
    }
  }
  @Test
  public void testMonteCarloQuickselect() {
    int runCount = 0;
    for (int m=2047; m<2048; m=m+m+1) {
      for (int k=1; k<=m; ++k) {
        double c=0;
        for (int r=0; r<runCount; ++r) {
          int first=1;
          int last=m;
          do {
            int count = last + 1 -first;
            c += (count-1);
            int j = first + (int)Math.floor(count*Math.random());
            if (j<=k) first = j+1;
            if (k<=j) last  = j-1;
          } while (first<last);
        }
        double avg   = Math.floor(c/(runCount==0 ? 1 : runCount)*1000.0+.5)/1000.0;
        double left  = (k==1) ? 1 : (k-1);
        double right = (k==m) ? 1 : (m-k);
        double model = 2*m
          - 2.0*(k-1)*Math.log(left/(double)m)
          - 2.0*(m-k)*Math.log(right/(double)m)
          + 4*Gain.harmonic(m)
          - 6*Gain.harmonic(k) 
          - 6*Gain.harmonic(m+1-k);
        model = Math.floor(model*1000.0+.5)/1000.0;
        double trad = 2*m
          - 2.0*(k)*Math.log((double)left/(double)m)
          - 2.0*(m-k)*Math.log((double)right/(double)m);
        trad = Math.floor(trad*1000.0+.5)/1000.0;
        String line = "";
        if (0<runCount) {
          line += "" + m + "\t" + k + "\t" + avg +"\t"; 
        }
        line += model; 
        if (0<runCount) { 
          line+= "\t" + trad;
        }
        System.out.println(line);
      }
      System.out.println("");
    }
  }  
  private interface SingleIndexSelector {
    public int selectIndex(int start, int stop, Double c);
    public double getModelCount(int k, int n);
    public int getC();
    public int getA();
  }
  private class BOF3IndexSelector 
    implements SingleIndexSelector {
    @Override
    public int selectIndex(int start, int stop, Double c) {
      int count = stop -start;
      int j     = start + (int)Math.floor(count*Math.random());
      if (3<=count) {
        int j2;
        do {
          j2 = start + (int)Math.floor(count*Math.random());
        } while (j2==j);
        int j3;
        do {
          j3 = start + (int)Math.floor(count*Math.random());
        } while (j3==j2 || j3==j);
        c+=3;
        if (j<j2) {
          if (j2<j3) { 
            j=j2;
            --c; //Finding best of 3 took 2, not 3 comparisons
          } else if (j<j3) {
            j=j3;
          } else {
          }
        } else {
          if (j3<j2) {
            --c; //Finding best of 3 took 2, not 3 comparisons
            j=j2;
          } else if (j3<j) {
            j=j3;
          }
        }
      }
      return j;
    }
    @Override
    public double getModelCount(int k, int n) {
      return 2*n + 3*k*(n+1-k)/(double)n;
    }
    public int getC() {
      return 3;
    }
    public int getA() {
      return 2;
    }
  }
  private class AthOfCIndexSelector
    implements SingleIndexSelector {
    protected int c;
    protected int a;
    protected RandomIndexSelector r 
      = new RandomIndexSelector();
    protected SingleIndexSelector s;
    double    selectionComparisonCount;
    public AthOfCIndexSelector
      ( int sampleSize, int sampleRank
      , SingleIndexSelector alternate ) {
      c = sampleSize;
      a = sampleRank;
      s = alternate;
      int k = (c+1)/2;
      selectionComparisonCount  
        = 2*(c+3+(c+1)*Gain.harmonic(c)
            - (c+3-k)*Gain.harmonic(c+1-k)
            - (k+2)*Gain.harmonic(k));
      //Assumes QuickSelect.
    }
    @Override
    public int selectIndex
      ( int start, int stop, Double comparisonCount) {
      int count = stop - start;
      if (count < c) {
        return s.selectIndex(start,  stop, comparisonCount);
      }
      IndexSet set = r.getRandomSubset(start, stop, c);
      int[] arr = new int[c];
      set.emit(arr, 0);
      Arrays.sort(arr);
      comparisonCount += selectionComparisonCount;
      return arr[a-1];
    }
    @Override
    public double getModelCount(int k, int n) {
      return 2*n + 3*k*(n+1-k)/(double)n;
    }
    public int getC() {
      return c;
    }
    public int getA() {
      return a;
    }
  }
  public void testMonteCarloQuickselect
    ( SingleIndexSelector seltor, int m
    , int kFirst, int kLast, int kStep
    , boolean showHeader) {
    ArrayList<SingleIndexSelector> seltors
      = new ArrayList<SingleIndexSelector>();
    seltors.add(seltor);
    testMonteCarloQuickselect
      ( seltors, m, kFirst, kLast, kStep, showHeader, true, true);
  }
  public void testMonteCarloQuickselect
   ( ArrayList<SingleIndexSelector> seltors, int m
    , int kFirst, int kLast, int kStep
    , boolean showHeader, boolean showCAndA, boolean showModel) {
    int runCount = 10000;
    if (showHeader) {
      String header="m\tk";
      if (0<runCount) {
        if (showCAndA) {
          header += "\tc\ta";
        }
        header += "\tavg"; 
      }
      if (showModel) {
        header += "\tmodel";
        if (0<runCount) {
          header += "\tdiff"; 
        }
      }
      System.out.println(header);
    }
    for (int k=kFirst; k<=kLast; k+=kStep) {
      String line = "" + m + "\t" + k;
      for ( SingleIndexSelector seltor : seltors ) {
        Double c=new Double(0);
        for (int r=0; r<runCount; ++r) {
          int first=1;
          int last=m;
          do {
            c += last-first;
            int j = seltor.selectIndex(first, last+1, c);
            if (j<=k) first = j+1;
            if (k<=j) last  = j-1;
          } while (first<last);
        }
        double avg   = Math.floor
                       ( c/(runCount==0 ? 1 : runCount)
                         * 1000.0 +.5 )/1000.0;
        //double left  = (k==1) ? 1 : (k-1);
        //double right = (k==m) ? 1 : (m-k);
        if (0<runCount) {
          if (showCAndA) {
            line += "\t" + seltor.getC()
                 + "\t"  + seltor.getA();
          }
          line += "\t"  + avg;  
        }
        if (showModel) {
          double model = seltor.getModelCount(k,m);
          model = Math.floor(model*1000.0+.5)/1000.0;
          line += "\t" + model; 
          if (0<runCount) {
            line += "\t" + Math.floor((model-avg)*1000.0)/1000.0;
          }
        }
      }
      System.out.println(line);
    }
  }
  @Test
  public void testMonteCarloQuickselectBOF3() {
    testMonteCarloQuickselect
      ( new BOF3IndexSelector(),1001,1,1001,10, true );
  }
  @Test
  public void testMonteCarloQuickselectBOFCentral() {
    for (int c = 3; c < 32; c+=2) {
      testMonteCarloQuickselect
        ( new AthOfCIndexSelector(c, (c+1)/2, new BOF3IndexSelector())
        ,100001,50001,50001,1, c==3 );
    }
  }
  @Test
  public void testMonteCarloQuickselectC5() {
    runCount = 1000;
    ArrayList<SingleIndexSelector> seltors
      = new ArrayList<SingleIndexSelector>();
    for (int a = 1; a <= 5; ++a) {
      seltors.add ( new AthOfCIndexSelector(5, a, new BOF3IndexSelector()));
    }
    testMonteCarloQuickselect(
        seltors,10001,1,10001,100, true, false, false );
  }
  @Test
  public void testMonteCarloQuickselectBOF5() {
    testMonteCarloQuickselect
      ( new AthOfCIndexSelector(5, 3, new BOF3IndexSelector())
      , 1001,1,1001,10,true );
  }
  @Test
  public void testMonteCarloQuickselectBOF7() {
    testMonteCarloQuickselect
      ( new AthOfCIndexSelector(7, 4, new BOF3IndexSelector())
      , 1001,1,1001,10, true );
  }
  @Test
  public void testMonteCarloQuickselectBOF9() {
    testMonteCarloQuickselect
      ( new AthOfCIndexSelector(9, 5, new BOF3IndexSelector())
      , 1001,1,1001,10 , true);
  }
  @Test
  public void testMonteCarloQuickselectBOF11() {
    testMonteCarloQuickselect
      ( new AthOfCIndexSelector(11, 6, new BOF3IndexSelector())
      , 1001,1,1001,10 , true);
  }
  @Test
  public void testMonteCarloQuickselectBOF13() {
    testMonteCarloQuickselect
      ( new AthOfCIndexSelector(13, 7, new BOF3IndexSelector())
      , 1001,1,1001,10 , true );
  }
  @Test
  public void testMonteCarloQuickselectBOF15() {
    testMonteCarloQuickselect
      ( new AthOfCIndexSelector(15, 8, new BOF3IndexSelector())
      , 1001,1,1001,10, true);
  }
  @Test
  public void testMonteCarloQuickselectTukey() {
    //testMonteCarloQuickselect(new TukeyIndexSelector());
  }
  @Test
  public void testHypergeometricPMFandCDFApproximations() {
    int m    = 1999;
    int c    = 199;
    int runs = 1000000;
    for (int k : new int[] { (m+1)/8, (m+1)/4, (m+1)/2 } ) {
      int[] counts = new int[c+1];
      for (int r=0; r<runs; ++r) {
        int[] sample 
          = random.randomOrderedCombination(m, c); //0..(m-2)
        int a
          = BinaryInsertionSort.findPreInsertionPoint
            (sample, 0, c, k-1); 
            //k-1, because k is one-based, but
            //indices in sample[] are 0-based.
        ++counts[a]; 
      }
      HypergeometricDistribution H
        = new HypergeometricDistribution(m, k-1, c);
      double p               = (double)k / (double)m;
      BinomialDistribution B = new BinomialDistribution(c, p); //Approximation
      NormalDistribution   N = new NormalDistribution(H); //Approximation
      NormalDistribution  N2 
        = new NormalDistribution
              ( H.getMean()
              , (double)(c+1)*(double)(m-c)/4.0/(double)(m-2) );
     
      double runningCDF  = 0.0;
      double[] exactCDFs = new double[c+1];
      System.out.println("Hypergeometric PMF for m=" + m + ", k=" + k + ", and c=" + c);
      System.out.println("a\tTrue\tExperiment\tModel\tBinomial\tB-Approx\tNormal\tNormal-Bound");
      for (int a=0; a<=c; ++a) {
        double truePMF         = H.getExactPMF(a);
        double experimentalPMF = (double)counts[a]/(double)runs;
        double modelPMF        = H.getApproximatePMF(a);
        double binomialPMF     = B.getExactPMF(a);
        double binomialApprox  = B.getApproximatePMF(a);
        double normalPMF       = (N.getCDF(a+.5)-N.getCDF(a-.5));
        double boundNormalPMF  = (N2.getCDF(a+.5)-N2.getCDF(a-.5));
        System.out.println
          ( "" + a 
          + t6(truePMF)
          + t6(experimentalPMF)
          + t6(modelPMF)
          + t6(binomialPMF)
          + t6(binomialApprox)
          + t6(normalPMF)
          + t6(boundNormalPMF));
        runningCDF += truePMF;
        exactCDFs[a] = runningCDF; 
      }
      System.out.println();
      
      int total = 0;
      System.out.println("Hypergeometric CDF for m=" + m + ", k=" + k + ", and c=" + c);
      System.out.println("a\tTrue\tExperiment\tModel\tBinomial\tNormal\tNormal-Bound\tCheck");
      for (int a=0; a<=c; ++a) {
        total += counts[a];
        double trueCDF         = exactCDFs[a];
        double experimentalCDF = (double)total/(double)runs;
        double modelCDF        = H.getApproximateCDF(a); 
        double binomialCDF     = B.getApproximateCDF(a);
        double normalCDF       = N.getCDF(a);
        double boundNormalCDF  = N2.getCDF(a);
        double checkCDF        = H.getExactCDF(a); //should be == trueCDF
        System.out.println
          ( "" + a 
          + t6(trueCDF)
          + t6(experimentalCDF)
          + t6(modelCDF)
          + t6(binomialCDF)
          + t6(normalCDF)
          + t6(boundNormalCDF)
          + t6(checkCDF));
      }
      System.out.println();
    }
  }
  @Test
  public void testBetaBinomialCDFApproximations() {
    int m    = 1999;
    int runs = 1000000;
    int c    = 199;
    int step = 1;
    for (int a : new int[] { (c+1)/8, (c+1)/4, (c+1)/2 } ) {
      int[] counts = new int[m+1]; 
      for (int r=0; r<runs; ++r) {
        int[] sample = random.randomOrderedCombination(m, c); //0..(m-1)
        ++counts[sample[a-1]+1]; 
      }
      double mu    = (double)a * (double)(m+1) / (double)(c+1);
      double varFR = (double)(m+1) * (double)(m-c) / (double)4.0 / (double)(c+2);
      double p     = (double)a / (double)(c+1);
      HypergeometricRetribution X
        = new HypergeometricRetribution(m, c, a);
      NormalDistribution N 
        = new NormalDistribution(X);
      NormalDistribution N2  
        = new NormalDistribution(mu, varFR);
      BinomialDistribution B 
        = new BinomialDistribution(c, p);
      
      double sigma        = X.getStandardDeviation();
      int total           = 0;
      double logOfTruePMF = 0;
      double trueCDF      = 0;
      
      System.out.println("Beta binomial CDF for m=" + m + ", c=" + c + ", and a=" + a);
      System.out.println("x\tTrue\tExperiment\tNormal\tB-Normal\tBBQ\tProjection\tV-P");
      for (int x=1; x<=m; ++x) {
        total            += counts[x];
        if (x<a) {
          //Do nothing. PMF at a<a is zero.
          trueCDF = 0;
          
        } else if (x==a) {
          for (int i=1; i<=a; ++i) {
            //We want log(binomial_coefficent(c-1,a-1))
            //  - log(binomial_coefficient(m-1,x-1)
            logOfTruePMF += Math.log((double)(c+1-i) / (double)(i));
            logOfTruePMF -= Math.log((double)(m+1-i) / (double)(i));
          }        
          trueCDF += Math.exp(logOfTruePMF);
        } else if (x<=m-c+a) {
          logOfTruePMF 
            += Math.log((double)(m+1-c+a-x) / (double)(x-a))
             + Math.log((double)(x-1) / (double)(m+1-x));
          trueCDF += Math.exp(logOfTruePMF); 
        } else {
          trueCDF = 1.0;
        }
        double experimentalCDF = (double)total/(double)runs;
        double normalCDF  = N.getCDF(x);
          //CDF according to the Normal distribution with
          //same mean and variance
        double boundingNormalCDF = N2.getCDF(x);
          //CDF according to Normal distribution
          //with same mean, and variance of the (a=(c+1)/2) 
          //distribution: this is a little bit *tighter* than 
          //Floyd & Rivest's approximation (since they use a
          //an approximate quantile function that loosens it
          //still further).
        double bbqCDF 
          = B.getApproximateCDF
            ( a + (x-mu) / X.getStandardDeviation() 
                         * B.getStandardDeviation());
        HypergeometricDistribution A
          = new HypergeometricDistribution(m, (x<2) ? x : (x-1), c);
        double projectionCDF 
          = 1 - A.getExactCDF( a-1 );
        double vpCDF = -1; 
        double deviations = Math.abs(mu-x)/sigma; 
        double outBound   
          = (deviations==0) ? 1 : (4.0/9.0/deviations/deviations);
        if (outBound < 0.5 ) {
          vpCDF = (x<mu) ? outBound : (1 - outBound);
        }
        if ( step == 1 || (x % step) == 1) {
        System.out.println
          ( "" + x 
          + t6(trueCDF)
          + t6(experimentalCDF) 
          + t6(normalCDF)
          + t6(boundingNormalCDF)
          + t6(bbqCDF)
          + t6(projectionCDF) 
          + ((vpCDF<0) ? "" : t6(vpCDF)));
        }
      }
    }
  }
  @Test
  public void testBetaBinomialPMFApproximations() {
    int m    = 1001;     //999      51
    int runs = 100000; //1000000
    int c    = 5;       //49       9
    int a    = 2;       //25       5
    int step = 10;
    int[] counts = new int[m+1]; 
    for (int r=0; r<runs; ++r) {
      int[] sample = random.randomOrderedCombination(m, c); //0..(m-1)
      ++counts[sample[a-1]+1]; 
    }
    double mu    = (double)a * (double)(m+1) / (double)(c+1);
    double varFR = (double)(m+1) * (double)(m-c) / (double)4.0 / (double)(c+2); 
    NormalDistribution   N  = new NormalDistribution(mu, varFR);
    HypergeometricRetribution X 
      = new HypergeometricRetribution(m, c, a); 
    
    //double p              = (double)a / (double)(c+1);
    double logOfTruePMF     = 0;
    double truePMF          = 0;
    
    System.out.println("Beta binomial PMF for m=" + m + ", c=" + c + ", and a=" + 25);
    System.out.println("k\tTrue\tExperiment\tApprox");
    for (int x=1; x<=m; ++x) {
      if (x<a) {
        truePMF = 0;
      } else if (x==a) {
        for (int i=1; i<=a; ++i) {
          //We want log(binomial_coefficent(c-1,a-1))
          //  - log(binomial_coefficient(m-1,x-1)
          logOfTruePMF 
            += Math.log((double)(c+1-i))
            -  Math.log((double)(m+1-i));
        }
        truePMF = Math.exp(logOfTruePMF);
      } else if (x<=m-c+a) {
        logOfTruePMF 
          += Math.log((double)(m+1-c+a-x) / (double)(x-a))
           + Math.log((double)(x-1) / (double)(m+1-x));
        truePMF = Math.exp(logOfTruePMF); 
      } else {
        truePMF = 0;
      }
      double experimentalPMF = (double)counts[x]/(double)runs;
      double normalPMF       = N.getDensity(x);
      double approximatePMF  = X.getApproximatePMF(x);
        //CDF according to Normal distribution
        //with same mean, and variance of the (a=(c+1)/2) 
        //distribution.
      if ( step == 1 || (x % step) == 1) {
      System.out.println
        ( "" + x 
        + t6(truePMF)
        + t6(experimentalPMF) 
        + t6(approximatePMF)
        + t6(normalPMF));
      }
    }
  }
  @Test
  public void testCostFactors() {
    int m = 999; //was 99999
    int c = 99;    //was 999
    int runs = 100000; //was 100,000
    int step = 1;   //was 25
    for (int k: new int[] { (m+1)/4, 7*(m+1)/100, (m+1)/2-2 } ) {
      System.out.println
        ( "Cost factors, for m=" + m + ", c=" + c + ", k=" + k 
        + " (average over " + runs + " runs)");
      double leftOverCosts[]
        = new double[c+1]; //includes *notional* sampling cost
      for (int r=0; r<runs; ++r) {
        int[] sample = random.randomOrderedCombination(m, c);
        for (int a=1; a<=c; a+=step) {
          int x = sample[a-1]+1;
          //if (a==1) System.out.println(x);
          int leftOverCost = 0;
          if (x<k) {
            leftOverCost = notionalCost(m-x, k-x);
          } else if (k<x) {
            leftOverCost = notionalCost(x-1, k);
          }
          leftOverCosts[a]  
            += leftOverCost;
        }
      }
      int initialCost = notionalCost(m, k);
      System.out.println("k\tExcess\tNetSample\tHole\tOutside\tOffside\tSilly\tSystematic\tSystemic\tTotal\tDiff");
      for (int a=1; a<=c; a+=step) {
        HypergeometricDistribution A
          = new HypergeometricDistribution(m,k,c);
        HypergeometricRetribution X
          = new HypergeometricRetribution(m,c,a);
        NormalDistribution N
          = new NormalDistribution();
        double avgLeftOver   = leftOverCosts[a] / (double)runs;
        double avgBenefit    = initialCost - avgLeftOver;
        double sampleCost    = notionalCost(c, a);
        double netSampleCost = sampleCost - (c-1);
        double avgExcess     = (m-c) + sampleCost - avgBenefit;
        double holeInOne   
          = -( c<100 ? A.getExactPMF(a) : A.getApproximatePMF(a) ) 
            * (double)c / (double)m;
        if (!Double.isFinite(holeInOne)) {
          holeInOne = 0;
        }
        double outsideRisk 
          = ( m<1000 ? X.getExactCDF(k-1) : X.getApproximateCDF(k-1) )
            * (double)( m + 1 - k - k); 
          //note, this formula is valid *only* for k<=(m+1)/2
        double mu             = X.getMean();
        double gradientTilt   = k + k;
        double middle         = ( m + 1.0 ) / 2.0;
        double systematicMiss = gradient2Or1(k,      gradientTilt, middle, mu);
        if (mu<k) {
          systematicMiss = (k-mu) * 2.0 - 2;
        }
        double offsideCost    = gradient2Or1(middle, gradientTilt, m+1-k, mu);
        double sillyPivotCost = gradient2Or1(m+1-k,  gradientTilt, m,     mu);
        double stdDev         = X.getStandardDeviation();
        double multiplier     
          = 0.75 - N.getERF(Math.abs(k-mu)/stdDev)
                 + N.getERF(Math.abs(2.0*k-mu)/stdDev)*0.25;
        double systemicMissCost 
          = stdDev * multiplier * Math.sqrt(8/Math.PI);
        
        double estExcess 
          = netSampleCost + holeInOne + outsideRisk
          + systematicMiss + offsideCost + sillyPivotCost
          + systemicMissCost;
        System.out.println
          ( "" + a + t2(avgExcess)
          + "\t" + (int)netSampleCost  + t4(holeInOne) + t2(outsideRisk)
          + t2(offsideCost) + t2(sillyPivotCost)
          + t2(systematicMiss) + t2(systemicMissCost)
          + t2(estExcess)
          + t2(avgExcess - estExcess));
      }
      System.out.println();
    }
  }
  private double gradient2Or1(double start, double gradientTilt, double stop, double mu) {
    if (mu<start) {
      return 0;
    }
    if (stop<mu) {
      mu = stop;
    }
    if (gradientTilt<start) {
      gradientTilt = start;
    }
    if (mu<gradientTilt) {
      return 2.0 * (mu-start) - 1;
    }
    return 2.0 * (gradientTilt-start) + mu - gradientTilt - 1;
 }
  public int notionalCost(int m, int k) {
    int cost1 = m + k - 2;
    int cost2 = m + m - k - 1;
    return (cost1 < cost2) ? cost1 : cost2;
  }
  public String t6(double x) {
    return "\t" + Math.floor(x*1000000.0 + .5) / 1000000.0;
  }
  public String t4(double x) {
    return "\t" + (Math.floor(x*10000.0 + .5)/10000.0);
  }
  public String t2(double x) {
    return "\t" + Math.floor(x*100.0 + .5) / 100.0;
  }
  @Test
  public void testMendel() {
    int[][] data = new int[][]{
        new int[] {7324 , 5474 , 1850 },
        new int[] {8023,  6022,  2001},
        new int[] {929,   705,   224},
        new int[] {1181,  882,   299},
        new int[] {580,   428,   152},
        new int[] {858,   651,   207},
        new int[] {1064,  787,   277}
    };
    double tprob = 1.0;
    for (int[] row : data) {
      int n = row[0];
      int k = row[1];
      double p = 0.75;
      double sigma = n * p;
      BinomialDistribution b 
        = new BinomialDistribution(n, p);
      double prob;
      if ( sigma < k ) {
        prob = b.getExactCDF(k);
      } else {
        prob = 1.0 - b.getExactCDF(k);
      }
      System.out.println(n +"\t" + sigma + "\t" + k + "\t" + (k-sigma) + "\t" + prob);
      tprob *= prob;
    }
    System.out.println(tprob);
  }
}
