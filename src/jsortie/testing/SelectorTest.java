package jsortie.testing;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import jsortie.RangeSorter;
import jsortie.earlyexitdetector.RangeSortEarlyExitDetector;
import jsortie.earlyexitdetector.WainwrightEarlyExitDetector;
import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.janitors.insertion.OrigamiInsertionSort;
import jsortie.janitors.insertion.ShellSort;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.PositionalSampleCollector;
import jsortie.quicksort.collector.RandomSampleCollector;
import jsortie.quicksort.collector.external.ExternalNullCollector;
import jsortie.quicksort.collector.external.ExternalPositionalCollector;
import jsortie.quicksort.collector.external.ExternalRandomCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.adapter.PartitionerToExpander;
import jsortie.quicksort.expander.branchavoiding.BalancedSkippyExpander;
import jsortie.quicksort.governor.QuicksortGovernor;
import jsortie.quicksort.governor.adaptive.QuicksortAdaptive;
import jsortie.quicksort.indexselector.PositionalIndexSelector;
import jsortie.quicksort.multiway.expander.kangaroo.SkippyExpander2;
import jsortie.quicksort.multiway.partitioner.centripetal.CentripetalPartitioner2;
import jsortie.quicksort.multiway.partitioner.centripetal.CentripetalPartitioner3;
import jsortie.quicksort.multiway.partitioner.decorator.BirdsOfAFeatherPartitioner;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner2;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner3;
import jsortie.quicksort.multiway.quickselect.MultiPivotQuickSelector;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.multiway.selector.clean.CleanMultiPivotPositionalSelector;
import jsortie.quicksort.partitioner.bidirectional.centrepivot.CentrePivotPartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.HoyosPartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.RevisedSingletonPartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.SingletonPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.BalancedSkippyPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.FlowerArrangementPartitioner;
import jsortie.quicksort.partitioner.decorator.TelescopingPartitioner;
import jsortie.quicksort.partitioner.insideout.CentripetalPartitioner;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.BrainDeadKthStatisticPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.Afterthought489Partitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.Algorithm489Partitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.Derivative489Partitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.ZeroDelta489Partitioner;
import jsortie.quicksort.partitioner.kthstatistic.floydrivest.SimplifiedFloydRivestPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.heap.KislitsynPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.floydrivest.FloydRivestPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.floydrivest.FridgePartitioner;
import jsortie.quicksort.partitioner.kthstatistic.mom.MedianOfMediansPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.quickselect.QuickSelectPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.remedian.RemedianPartitioner;
import jsortie.quicksort.partitioner.lomuto.LomutoPartitioner;
import jsortie.quicksort.partitioner.unidirectional.HolierThanThouPartitioner;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.clean.CleanExternalSelector;
import jsortie.quicksort.selector.clean.CleanLeftHandedSelector;
import jsortie.quicksort.selector.clean.CleanPositionalSampleSelector;
import jsortie.quicksort.selector.clean.CleanRemedianSelector;
import jsortie.quicksort.selector.clean.CleanRemedianSelectorBranchAvoiding;
import jsortie.quicksort.selector.clean.CleanRightHandedSelector;
import jsortie.quicksort.selector.clean.CleanSingletonSelector;
import jsortie.quicksort.selector.clean.CleanTheoreticalSelector;
import jsortie.quicksort.selector.clean.CleanTukeySelector;
import jsortie.quicksort.selector.clean.CleanVanEmdenSelector;
import jsortie.quicksort.selector.clean.RevisedCleanTukeySelector;
import jsortie.quicksort.selector.dirty.DirtyCombSelector;
import jsortie.quicksort.selector.dirty.DirtyCompoundSelector;
import jsortie.quicksort.selector.dirty.DirtyLeftHandedSelector;
import jsortie.quicksort.selector.dirty.DirtyRemedianSelector;
import jsortie.quicksort.selector.dirty.DirtyRemedianSelectorBranchAvoiding;
import jsortie.quicksort.selector.dirty.DirtyRemedianSelectorGeneral;
import jsortie.quicksort.selector.dirty.DirtyRightHandedSelector;
import jsortie.quicksort.selector.dirty.DirtySingletonSelector;
import jsortie.quicksort.selector.dirty.DirtyTheoreticalSelector;
import jsortie.quicksort.selector.dirty.DirtyTukeySelector;
import jsortie.quicksort.selector.dirty.DirtyVanEmdenSelector;
import jsortie.quicksort.selector.dirty.LessDirtyCompoundSelector;
import jsortie.quicksort.selector.dirty.DirtyVanEmdenSelectorRevised;
import jsortie.quicksort.selector.simple.MiddleElementSelector;
import jsortie.quicksort.selector.simple.RandomElementSelector;
import jsortie.quicksort.sort.monolithic.QuicksortSingleton;
import jsortie.quicksort.theoretical.Gain;

public class SelectorTest {
  int runCountSmall = 1000;
  int runCount = 100;
  int minCount = 100;
  int maxCount = 10000000;
  protected boolean runSlowCrap = false;
  protected RandomInput random = new RandomInput();
  protected SortTest  sortTest = new SortTest();
  
  NullSampleCollector         nix   = new NullSampleCollector();

  SinglePivotSelector        middle = new MiddleElementSelector();
  CentripetalPartitioner     centri = new CentripetalPartitioner();
  KthStatisticPartitioner brainDead = new BrainDeadKthStatisticPartitioner();
  KthStatisticPartitioner     kissy = new KislitsynPartitioner();
  
  BalancedSkippyPartitioner roo   = new BalancedSkippyPartitioner();
  BalancedSkippyExpander    roox  = new BalancedSkippyExpander();
  PartitionExpander           roop  = new PartitionerToExpander(roo);  

  MedianOfMediansPartitioner  mom5  = new MedianOfMediansPartitioner(5, roox, roox );
  MedianOfMediansPartitioner  mom17 = new MedianOfMediansPartitioner(17, roox, roox );
  RemedianPartitioner         rem   = new RemedianPartitioner();
  QuickSelectPartitioner      qspc  
    = new QuickSelectPartitioner(middle, centri, centri, 5, kissy);
  QuickSelectPartitioner      qspba 
    = new QuickSelectPartitioner(middle, roo, roo, 5, kissy);
  Algorithm489Partitioner     a489  = new Algorithm489Partitioner(nix, roop, roop);
  Afterthought489Partitioner after489 = new Afterthought489Partitioner(nix, roox, roox);
  Algorithm489Partitioner     a489x = new Algorithm489Partitioner(nix, roox, roox);
  ZeroDelta489Partitioner     a489z = new ZeroDelta489Partitioner(nix, roox, roox);
  Derivative489Partitioner    a489d = new Derivative489Partitioner(nix, roox, roox);
  SimplifiedFloydRivestPartitioner sfrc   = new SimplifiedFloydRivestPartitioner();
  SimplifiedFloydRivestPartitioner sfrba  
    = new SimplifiedFloydRivestPartitioner(new SkippyExpander2());
  FloydRivestPartitioner      frc   = new FloydRivestPartitioner();
  FloydRivestPartitioner      frba  = new FloydRivestPartitioner(nix, roox, roox);
  FridgePartitioner          fridge = new FridgePartitioner();
  
  RangeSorter              heapsort = new TwoAtATimeHeapsort();
  RangeSorter                 isort = new OrigamiInsertionSort();
  RangeSortEarlyExitDetector  eed   = new WainwrightEarlyExitDetector();
  
  SquareRootSampleSizer       sqr   = new SquareRootSampleSizer();
  
  PositionalSampleCollector   pos   = new PositionalSampleCollector();
  RandomSampleCollector       rand  = new RandomSampleCollector();
  
  @Test
  public void testSelectors() {
    runSlowCrap = true;
    maxCount = 11000000;
    if (runSlowCrap) {
      benchmarkSelectors("Positional  dirty quick-select selectors", getPositionalMedianSelectors());
      benchmarkSelectors("Randomizing dirty quick-select selectors", getRandomMedianSelectors(qspba));
      benchmarkSelectors("Randomizing clean quick-select selectors", getRandomCleanMedianSelectors(qspba));
    }
    benchmarkSelectors("(Dirty) Partially sorting sample", getPositionalMedianSelectors2(true));
    benchmarkSelectors("(Clean) Partially sorting sample", getPositionalMedianSelectors2(false));
    benchmarkSelectors("\nInterval selectors",   getIntervalSelectors());
    benchmarkSelectors("\nRemedian selectors",   getRemedianSelectors());
    benchmarkSelectors("\nAsymmetric selectors", getAsymmetricSelectors());
    //benchmarkSelectors("Multi-pivot quick selectors", getQuickSelectors()); 
  }
  public NamedArrayList<SinglePivotSelector> 
    getPositionalMedianSelectors() {
    //Each of these fully sort the sample and take its median
    NamedArrayList<SinglePivotSelector> list 
      = new NamedArrayList<SinglePivotSelector>();
    list.add("Clean-X", new CleanExternalSelector
                 ( new PositionalIndexSelector(), after489));
    list.add("Dirty", new DirtyCompoundSelector
                 ( new SquareRootSampleSizer(0)
                 , new PositionalSampleCollector(), after489));
    list.add("Clean-X-Pos",   new CleanPositionalSampleSelector());
    list.add("LessDirty-Pos", new LessDirtyCompoundSelector
                 ( new PositionalIndexSelector()));
    return list;
  }
  public void testCombSelector() {
    SinglePivotSelector combOver 
      = new DirtyCombSelector();
    SortList sorts = new SortList();
    sorts.add 
      ( new QuicksortAdaptive 
            ( combOver, roo, isort, 64, heapsort, eed) );
    sortTest.warmUpSorts(sorts);
    sortTest.testSpecificSorts(sorts, 10, 1000000, 10);
  }
  public NamedArrayList<SinglePivotSelector> 
    getPositionalMedianSelectors2(boolean dirty) {
    //Each of these *partially*, rather than fully sort the sample;
    //the variety of partial sort is governed by the ... selector.	
    NamedArrayList<SinglePivotSelector> list 
      = new NamedArrayList<SinglePivotSelector>();
    for ( KthStatisticPartitioner ksp : new KthStatisticPartitioner[]
        { qspc, qspba, mom5, mom17, rem, a489, a489x
        , after489, a489z, a489d, sfrc, sfrba, frc, frba, fridge } ) {
      if (dirty) {
        list.add
          ( "DirtyCompound(Sqr,Pos," + ksp.toString() + ")"
          ,  new DirtyCompoundSelector(sqr, pos, ksp));
      } else {
        list.add
          ( "CleanExternal(Sqr,Pos," + ksp.toString() + ")"
          , new CleanExternalSelector(new PositionalIndexSelector(), ksp));
      }
    }
    return list;
  }
  public NamedArrayList<SinglePivotSelector> 
    getRandomMedianSelectors(KthStatisticPartitioner ksp) {
    NamedArrayList<SinglePivotSelector> list 
      = new NamedArrayList<SinglePivotSelector>();
    list.add ( "DirtyCompound",         new DirtyCompoundSelector ( sqr, nix,  ksp));
    list.add ( "DirtyCompound(Pos)",    new DirtyCompoundSelector ( sqr, pos,  ksp));
    list.add ( "DirtyCompound(Random)", new DirtyCompoundSelector ( sqr, rand, ksp));
    return list;
  }
  @Test
  public void testRandomizingMedianSelectors() {
    benchmarkSelectors
      ( "Randomizing dirty a489 selectors"
      , getRandomMedianSelectors(a489));
    benchmarkSelectors
      ( "Randomizing clean a489 selectors"
      , getRandomCleanMedianSelectors(a489));
  }
  @Test
  public void testRandomizingDirtyMedianSelectors() {
    benchmarkSelectors ( "Randomizing dirty a489 selectors"
                       , getRandomMedianSelectors(a489));
  }
  public NamedArrayList<SinglePivotSelector> getRandomCleanMedianSelectors
    ( KthStatisticPartitioner ksp ) {
    NamedArrayList<SinglePivotSelector> list 
      = new NamedArrayList<SinglePivotSelector>();
    list.add ( "CleanThoeretical" 
             , new CleanTheoreticalSelector 
                   ( .5, new ExternalNullCollector(), ksp));
    list.add ( "CleanTheoretical(Pos)"
             ,    new CleanTheoreticalSelector 
                      ( .5, new ExternalPositionalCollector(), ksp));
    list.add ( "CleanTheoretical(Random)"
             , new CleanTheoreticalSelector 
                   ( .5, new ExternalRandomCollector(), ksp));
    return list;
  }
  @Test
  public void testRandomizingCleanMedianSelectors() {
    benchmarkSelectors("Randomizing clean a489 selectors", getRandomCleanMedianSelectors(a489));
  }
  public NamedArrayList<SinglePivotSelector> getRemedianSelectors() {
    NamedArrayList<SinglePivotSelector> list 
      = new NamedArrayList<SinglePivotSelector>();
    list.add ( "CleanRemedian(Pos)",    new CleanRemedianSelector ( true     ) );
    list.add ( "CleanRemedian",         new CleanRemedianSelector ( false    ) );
    list.add ( "CleanRemedian(BA/Pos)", new CleanRemedianSelectorBranchAvoiding ( true ) );
    list.add ( "CleanRemedian(BA)",     new CleanRemedianSelectorBranchAvoiding ( false ) );
    list.add ( "CleanExternal(Pos)",    new CleanExternalSelector 
                                            ( new PositionalIndexSelector()
                                            , new DirtyRemedianSelector(false) ) );
    list.add ( "DirtyRemedian(Pos)",    new DirtyRemedianSelector ( true     ) );
    list.add ( "DirtyRemedian",         new DirtyRemedianSelector ( false    ) );
    list.add ( "DirtyRemedian(BA/Pos)", new DirtyRemedianSelectorBranchAvoiding ( true     ) );
    list.add ( "DirtyRemedian(BA)",     new DirtyRemedianSelectorBranchAvoiding ( false    ) );
    list.add ( "DirtyRemedianG(3,Pos)", new DirtyRemedianSelectorGeneral ( 3, true  ) );
    list.add ( "DirtyRemedianG(3)",     new DirtyRemedianSelectorGeneral ( 3, false ) );
    list.add ( "DirtyCompound(sqr,pos,Remedian)", new DirtyCompoundSelector 
                   ( new SquareRootSampleSizer()
                   , new PositionalSampleCollector()
                   , new DirtyRemedianSelector(false) ) );
    return list;
  }
  public NamedArrayList<SinglePivotSelector> getAsymmetricSelectors() {
    NamedArrayList<SinglePivotSelector> list 
      = new NamedArrayList<SinglePivotSelector>();
    list.add ( "LHS-C-Uniform",     new CleanLeftHandedSelector  ( true  ) );
    list.add ( "LHS-C-Non-uniform", new CleanLeftHandedSelector  ( false ) );
    list.add ( "RHS-C-Uniform",     new CleanRightHandedSelector ( true  ) );
    list.add ( "RHS-C-Non-uniform", new CleanRightHandedSelector ( false ) );
    list.add ( "LHS-D-Uniform",     new DirtyLeftHandedSelector  ( true  ) );
    list.add ( "LHS-D-Non-uniform", new DirtyLeftHandedSelector  ( false ) );
    list.add ( "RHS-D-Uniform",     new DirtyRightHandedSelector ( true  ) );
    list.add ( "RHS-D-Non-uniform", new DirtyRightHandedSelector ( false ) );
    return list;
  }
  public NamedArrayList<KthStatisticPartitioner> getQuickSelectors() {
    NamedArrayList<KthStatisticPartitioner> list 
      = new NamedArrayList<KthStatisticPartitioner>();
    list.add ( "QS", new QuickSelectPartitioner  () );
    list.add ( "QS2(Centripetal)"
             , new MultiPivotQuickSelector 
                   ( new CleanMultiPivotPositionalSelector(2)
                   , new CentripetalPartitioner2(), 2 ) );
    list.add ( "QS2(Skippy)"
             , new MultiPivotQuickSelector 
                   ( new CleanMultiPivotPositionalSelector(2)
                   , new BirdsOfAFeatherPartitioner
                         ( new SkippyPartitioner2() ), 3 ) );
    list.add ( "QS3(Centripetal)"
             , new MultiPivotQuickSelector 
                   ( new CleanMultiPivotPositionalSelector(3)
                   , new CentripetalPartitioner3(), 2 ) );
    list.add ( "QS3(Skippy)"
             , new MultiPivotQuickSelector 
                   ( new CleanMultiPivotPositionalSelector(3)
                   , new BirdsOfAFeatherPartitioner
                         ( new SkippyPartitioner3() ), 3 ) );
    return list;
  }
  NamedArrayList<SinglePivotSelector> getSelectors() {
    NamedArrayList<SinglePivotSelector> selectors 
      = new NamedArrayList<SinglePivotSelector>();
    selectors.add ( "MiddleElement",    new MiddleElementSelector    () );
    selectors.add ( "DirtySingleton",   new DirtySingletonSelector   () );
    selectors.add ( "DirtyTukey",       new DirtyTukeySelector       () );
    selectors.add ( "CleanRemedian",    new CleanRemedianSelector    (false) );
    selectors.add ( "DirtyTheoretical", new DirtyTheoreticalSelector (.5));
    selectors.add ( "CleanRHS",         new CleanRightHandedSelector (false) );
    selectors.add ( "CleanLHS",         new CleanLeftHandedSelector  (false) );
    selectors.add ( "CleanVanEmden",    new CleanVanEmdenSelector    () );
    return selectors;
  }
  NamedArrayList<SinglePivotSelector> getIntervalSelectors() {
    NamedArrayList<SinglePivotSelector> selectors 
      = new NamedArrayList<SinglePivotSelector>();
    selectors.add ( "DirtyVanEmden",   new DirtyVanEmdenSelector() );
    selectors.add ( "DirtyVanEmden-R", new DirtyVanEmdenSelectorRevised() );
    selectors.add ( "CleanVanEmden",   new CleanVanEmdenSelector() );
    return selectors;
  }
  
  public void benchmarkSelectors
    ( String description
    , NamedArrayList<SinglePivotSelector> list) {
    System.out.println(description 
      + " (overhead measured in nanoseconds" 
      + " per candidate)");
    for (int i=0; i< list.size(); ++i) {
      System.out.println
        ( list.getName(i) 
        + "=" + list.get(i).toString());
    }
    String header = (minCount<maxCount) ? "m" : "";
    for (int s = 0; s < list.size(); ++s) {
      String selName = list.getName(s);
      if (minCount<maxCount) {
        header += "\t" + selName;
      }
      SinglePivotSelector sel = list.get(s);
      for (int x=0; x<5000; ++x) {
        int warmUpInput[] 
          = random.randomPermutation(1000);
        sel.selectPivotIndex
          ( warmUpInput, 0, warmUpInput.length );
      }
    }
    if (minCount<maxCount) {
      System.out.println("\n" + header);
    }
    for ( int n = minCount; n <= maxCount
        ; n = n * 5 / 4) {
      double elapsed[] = new double[list.size()];
      int runCountHere 
        = (n < 10000) ? runCountSmall : runCount;
      for (int r = 0; r < runCountHere; ++r) {
        int input[] = random.randomPermutation(n);
        for (int s = 0; s < list.size(); ++s) {
          SinglePivotSelector sel = list.get(s);
          int copy[] = Arrays.copyOf(input, n); 
          long startTime = System.nanoTime();
          sel.selectPivotIndex(copy, 0, copy.length);
          long stopTime = System.nanoTime();
          elapsed[s] += (stopTime - startTime);
        }
      }
      String text;
      if (minCount==maxCount) {
        text = "Selector";
      } else {
        text = "" + n;
      }
      for (int s = 0; s < elapsed.length; ++s) {
        double nanosecPer 
          = elapsed[s] 
          / (double) runCountHere 
          / Math.sqrt(n);
        text += ((minCount==maxCount) 
                ? list.get(s).toString() : "") + "\t";
        text += (int)Math.floor(nanosecPer * 100.0 +.5) / 100.0;
        text += ((minCount==maxCount) ? "\n" : "");
      }
      System.out.println(text);
    }
    System.out.println("");
  }
  public void testQuickSelectAnalogue() {
    System.out.println
      ( "Predicted compares/n for quickSelect" 
      + " of 0.5n = " + (2.0 + 2 * Math.log(2)));
    double runs = runCount * 100;
    for (double t = 0; t <= 1.0; t += 1 / 64.0) {
      double c[] = new double[30];
      for (int r = 0; r < runs; ++r) {
        double lo = 0;
        double hi = 1;
        double p = .5;
        int k;
        for (k = 0; k < c.length && lo != hi; ++k) {
          c[k] += (hi - lo);
          p = Math.random() * (hi - lo) + lo;
          if (p < t) {
            lo = p;
          } else if (t < p) {
            hi = p;
          }
        }
      }
      double tc = 0;
      for (int i = c.length - 1; i >= 0; --i) {
        tc += c[i];
      }
      System.out.println
        ( "" + Math.floor(tc / (double) runs * 1000.0 + .5) 
               / 1000.0);
    }
  }
  public void testQuickSelectForReal() {
    int runCount = 1000;
    SinglePivotPartitioner p 
      = new CentrePivotPartitioner();
    int n = 10000;
    String text = "" + n;
    for (int t = 0; t <= n; t += n / 50) {
      int c = 0;
      for (int run = 0; run < runCount; ++run) {
        int data[] 
          = random.randomPermutation(n + 1);
        int lo = 0;
        int hi = n + 1;
        for (;;) {
          c += (hi - lo - 1);
          int r = p.partitionRange
                  ( data, lo, hi, t );
          if (r < t) {
            lo = r + 1;
          } else if (t < r) {
            hi = r;
          } else {
            break;
          }
        }
      }
      double x = (double) c / (double) runCount / (double) n;
      text += "\t" + Math.floor(x * 1000.0) / 1000.0;
    }
    System.out.println(text);
  }
  public void testEvilSelectors() {
    SortList sorts = new SortList ();
    RangeSorter j = new InsertionSort2Way();
    String line = "";
    for ( SinglePivotSelector s : new SinglePivotSelector []
          { new DirtySingletonSelector()
          , new CleanSingletonSelector() } ) {
      for ( SinglePivotPartitioner p : new SinglePivotPartitioner[]
            { new LomutoPartitioner()
            , new SingletonPartitioner()
            , new CentripetalPartitioner() } ) {
        sorts.add(new QuicksortGovernor(s, p, j, 10));
        line+= "\t" + s.toString() + "/" + p.toString(); 
      }
    }
    System.out.println(line);
    sortTest.warmUpSorts(sorts);
    DegenerateInputTest dit
      = DegenerateInputTest.newTest(100000, 25);
    dit.runSortsOnSortedData(sorts, "second");
    System.out.println("");
  }
  public void testEvilSelectors2() {
    SortList sorts = new SortList ();
    RangeSorter j = new InsertionSort2Way();
    String line = "";
    for ( SinglePivotSelector s : new SinglePivotSelector []
          { new DirtySingletonSelector()
          , new CleanSingletonSelector() } ) {
      for ( SinglePivotPartitioner p : new SinglePivotPartitioner[]
            { new HolierThanThouPartitioner()
            , new HoyosPartitioner()
            , new CentrePivotPartitioner() } ) {
        sorts.add(new QuicksortGovernor(s, p, j, 10));
        line+= "\t" + s.toString() + "/" + p.toString(); 
      }
    }
    System.out.println(line);
    sortTest.warmUpSorts(sorts);
    DegenerateInputTest dit
      = DegenerateInputTest.newTest(100000, 25);
    dit.runSortsOnSortedData(sorts, "second");
    System.out.println("");
  }
  @Test
  public void testEvilSelectors3() {
    SortList sorts = new SortList ();
    for (SinglePivotPartitioner party : new SinglePivotPartitioner[]
      { new SingletonPartitioner()
      , new RevisedSingletonPartitioner() 
      , new CentripetalPartitioner() }) {
      sorts.add
        ( new QuicksortGovernor 
              ( new DirtySingletonSelector()
              , party, new InsertionSort2Way(), 10 ) );
    }
    sorts.warmUp();
    int n=65536;
    System.out.println
      ( "Running times (milliseconds) sorting " 
      + n + " badly-shuffled inputs");
    String line = "Input";
    for (int i = sorts.size(); 0<i; --i) {
      line += "\tProper\tProper2\tExchanged";
    }
    System.out.println(line);
    DegenerateInputTest dit
      = DegenerateInputTest.newTest(n, 100);
    dit.runSortsOnPretendSortedData(sorts);
    System.out.println("");
  }
  public void testEvilSelectors4() {
    SinglePivotPartitioner singleton = new SingletonPartitioner();
    RangeSorter isort     = new InsertionSort2Way();
    RangeSorter governor  = new QuicksortSingleton( singleton, isort, 10);
    RangeSorter decorated 
      = new QuicksortGovernor 
            ( new DirtySingletonSelector()
            , new TelescopingPartitioner(singleton)
            , isort, 10);
    SortList sorts = new SortList ();
    sorts.add(governor);
    sorts.add(decorated);
    DegenerateInputTest dit
      = DegenerateInputTest.newTest(100000, 25);
    dit.runSortsOnSortedData(sorts, "second");
    System.out.println("");		
  }
  @Test
  public void testEvilSelectors5() {
    SortList sorts = new SortList ();
    RangeSorter j = new InsertionSort2Way();
    for ( SinglePivotSelector s : new SinglePivotSelector []
          { new CleanSingletonSelector()
          , new CleanTukeySelector()
          , new RevisedCleanTukeySelector() } ) {
      for ( SinglePivotPartitioner p : new SinglePivotPartitioner[]
            { new SingletonPartitioner()
            , new FlowerArrangementPartitioner()
            //, new HoyosPartitioner()
            //, new CentrePivotPartitioner() 
            } ) {
        sorts.add ( new QuicksortGovernor(s, p, j, 25)
                  , s.toString() + "/" + p.toString());
      }
    }
    sortTest.warmUpSorts(sorts);
    DegenerateInputTest dit = DegenerateInputTest.newTest(65536, 10);
    dit.runSortsOnSortedData(sorts, "second");
    System.out.println("");
    dit.runSortsOnDifferentSortsOfDegenerateData
     ( sorts, 65535, 25, 1.25);
  }
  
  public void testShowTheEvil() {
    RangeSorter evilSort = new QuicksortGovernor
        ( new DirtySingletonSelector(), new SingletonPartitioner()
        , new InsertionSort2Way(), 3);
    for (int n=4; n<10000000; n=n*5/4) {
      int crud[] = DegenerateInput.identityPermutation(n);
      crud[ n/3 ] = n+1;
      double start = System.nanoTime();
      evilSort.sortRange(crud, 0, crud.length);
      double elapsed = (System.nanoTime() - start) / 1000.0 / 1000.0 / 1000.0; //seconds
      System.out.println("" + n + "\t" + elapsed);
    }
  }
  public void testShowProblemsWithPositionalSelectors() {
    SinglePivotPartitioner singleton = new SingletonPartitioner();
    RangeSorter isort     = new InsertionSort2Way();
    RangeSorter decorated 
      = new QuicksortGovernor 
            ( new DirtySingletonSelector()
            , new TelescopingPartitioner(singleton)
            , isort, 10);
    SortList sorts = new SortList ();
    sorts.add ( new QuicksortGovernor 
                    ( new MiddleElementSelector()
                    , singleton, isort, 10));
    sorts.add(decorated);
    sorts.add( new QuicksortGovernor 
                   ( new RandomElementSelector()
                   , singleton, isort, 10));
    sortTest.warmUpSorts(sorts);
    DegenerateInputTest dit
      = DegenerateInputTest.newTest(100000, 25);
    dit.runSortsOnTwoSortedListsTapedTogether(sorts);
    System.out.println("");		
  }
  public void testSampleSizeComparisonCostTradeoff() {
    int    m = 1000;
    double k = 1.5;
    int[] before = new int[] {0, m };
    int runs = 10000;
    CleanRemedianSelector crs = new CleanRemedianSelector(false);
    int increment = 1;
    for (int c=1; c<2*Math.sqrt(m); c+=increment) {
      double cost    = 0, remedianCost    = 0;
      double benefit = 0, remedianBenefit = 0;
      double movesPerElement = 0;
      for (int r=0; r<runs; ++r) {
        int[] candidates = new int[c];
        for (int i=0; i<c; ++i) {
          candidates[i] = (int) Math.floor( Math.random() * m);
        }
        int remedian     = candidates[crs.selectPivotIndex(candidates, 0, candidates.length)];
        int[] after      = new int[] { 0, remedian, remedian+1, m };
        remedianCost    += m + c/3;
        remedianBenefit += SortTest.bitsLeftInPartitions(before)
                         - SortTest.bitsLeftInPartitions(after);
        
        Arrays.sort(candidates);        
        after    = new int[] { 0, candidates[c/2]
        		                 , candidates[c/2]+1, m };
        cost    += m + (k- 1) * c;        
        benefit += SortTest.bitsLeftInPartitions(before)
        		 - SortTest.bitsLeftInPartitions(after);
        double p = (double)candidates[c/2] / m;
        double q = (double)(m-candidates[c/2]-1) / m;
        movesPerElement += 2 * p * q / runs;
      }
      double efficiency = benefit/cost;
      String line = "" + c 
        + "\t" + Math.floor(efficiency*1000000.0)/1000000.0
        + "\t" + Math.floor(movesPerElement*1000000.0)/1000000.0;
      efficiency = remedianBenefit/remedianCost;
      line += "\t" + Math.floor(efficiency*1000000.0)/1000000.0;
      System.out.println(line);
    }
  }
  @Test
  public void testPickIt() {
    int c = 101;
    int[] hits = new int[c];
    int   runs = 1000000;
    double   target = (double)(c/2) / (double)c;
    double[] sample = new double[c+2];
    sample[0] = 1;
    sample[c] = 101;
    for (int r=0; r<runs; ++r) {
      for (int i=1; i<=c; ++i) {
        sample[i]=Math.random();
      }
      Arrays.sort(sample);
      for (int a=0; a<c; ++a) {
        hits[a] += ( sample[a]<target && target<=sample[a+1] ) ? 1 : 0;
      }
    }
    for (int a=0; a<c; ++a ) {
      System.out.println( a + ".." + ((double)hits[a] / (double)runs));
    }
  }
  public String f3(double d)
  {
    String s = "" + (int)Math.floor(d*1000+0.5);
    while (s.length()<4) {
      s = "0" + s;
    }
    return s.substring(0, s.length()-3) + "." + s.substring(s.length()-3);
  }
  public void testAthOfCFromM(int a, int c, int m) {
    double t         = (double)(a) * (double)(m+1) / (double)(c+1) - 1 ;
    double variance = 0;
    double mean     = 0;
    double driftLow = 0;
    double driftHi  = 0;
    double runs = 100000000 / m;
    for (int run=0;run<runs;++run) {   
      double[] sample = new double[c];
      for (int i=0;i<c;++i) {
        sample[i] = Math.random();
      }
      Arrays.sort(sample);
      double pivot = sample[a-1];
      double k     = a - 1;
      for (int i=c;i<m;++i) {
        k += ( Math.random() < pivot ) ? 1 : 0;
      }
      variance += (k-t)*(k-t) / runs;
      mean     += k /runs;
      driftLow += (k<t) ? (t-k)*2.0/runs : 0;
      driftHi  += (t<k) ? (k-t)*2.0/runs : 0;
    }
    double predictedError 
      = Math.sqrt( a*(c-a)*(m-c)
                   / (double)c / (double)c 
                   / (double)(c-1) / (double)(m-1))
      * (double)m;
    double nCr = 1.0;
    for (int i=1; i<=a; ++i) {
      nCr *= (double)( c + 1 - i );
      nCr /= (double)i;
    }
    //Now nCr is: binomial_coefficient(c-1,a-1)
    double powerOf2     = Math.exp(Math.log(2.0) * (c+1) );
    double prediction2  = (double)m * nCr / powerOf2; //(double)(m-c) * nCr / powerOf2 ; 
    System.out.println("" + a + "\t" + c + "\t" + "\t" + m 
      + "\t" + f3(mean) + "\t" + f3(Math.sqrt(variance))
      + "\t" + f3(predictedError) + "\t" + f3(driftLow) 
      + "\t" + f3(driftHi) + "\t" + f3(prediction2));
  }
  @Test
  public void testSection20_9_3_3() {
    //These numbers show the naive formula does okay, for a close to (c+1)/2.
    //But for a closer to 1 or to c, not so much.  It's quite a bit worse.
    testAthOfCFromM(1,1,101);
    testAthOfCFromM(2,3,101);
    testAthOfCFromM(4,7,101);
    //testAthOfCFromM(5,7,100);
    testAthOfCFromM(8,15,101);
    testAthOfCFromM(16,31,1001);
    testAthOfCFromM(32,63,1001);
    testAthOfCFromM(64,127,1001);
    testAthOfCFromM(128,255,1001);
    System.out.println("");
    testAthOfCFromM(1,7,101);
    testAthOfCFromM(2,7,101);
    testAthOfCFromM(3,7,101);
    System.out.println("");
    testAthOfCFromM(1,15,101);
    testAthOfCFromM(2,15,101);
    testAthOfCFromM(3,15,101);
    testAthOfCFromM(4,15,101);
    testAthOfCFromM(5,15,101);
    testAthOfCFromM(6,15,101);
    testAthOfCFromM(7,15,101);
  }
  @Test
  public void testSection5_5_5() {
    for (double m=1; m<=100000; m+=m+1) {
      double exact=0;
      double guess=0;
      for (double k=1; k<m; k+=1) {
        exact += k * (Math.log(m-1) - Math.log(k));
        for (double j=k+1; j<=m; j+=1) {
          guess += k / j;
        }
      }
      System.out.println
        ( "" + m + "\t" + guess + "\t" + exact 
        + "\t" + (exact-guess)/m);
    }
    for (int c=3; c<1024; c=c*2+1) {
      int c2 = (int)Math.floor(c * 3.3166);
      if ((c2 & 1) ==0) --c2;
      double e1 = (Gain.harmonic(c+1) - Gain.harmonic((c+1)/2)) / Math.log(2);
      double e2 = (Gain.harmonic(c2+1) - Gain.harmonic((c2+1)/2)) / Math.log(2);
      System.out.println
        ( c + "\t" + e1 + "\t" + e2 
        + "\t" + e2/e1 + "\t" + (1.0 + 0.5/(double)c));
    }
    for (int n=100; n<=10000000; n*=10) {
      double netG = 0;
      int c;
      for (c=1; c<Math.sqrt(n); c+=2) {
        double g = n*(Gain.harmonic(c+1) - Gain.harmonic((c+1)/2)) / Math.log(2); 
        double p = c*(Math.log(n)-1)/Math.log(2);
        if ( g- p < netG ) break; else netG=g-p;
      }
      System.out.println(n + "\t" + (c-2) 
        + "\t" + Math.sqrt(0.534*n/Math.log(n)));
    }
  }
  public void warmUpSelectors(ArrayList<MultiPivotSelector> selectors) {
    for (int s = 0; s < selectors.size(); ++s) {
      MultiPivotSelector sel = selectors.get(s);
      for (int x=0; x<5000; ++x) {
        int warmUpInput[] = random.randomPermutation(1000);
        sel.selectPivotIndices(warmUpInput, 0, warmUpInput.length);
      }
    }
  }
  public int rankOfRemedian
    (int n, int b) {
    ShellSort s = new ShellSort();
    int data[]  = random.randomPermutation(n);
    int start   = 0;
    int stop    = n;
    do {
      int step = (stop-start+b-1)/b;
      //System.out.println("[" + start + ".." + (stop-1) + "] step " + step);
      s.sortSlicesOfRange(data, start, stop, step );
      int middle = start + (stop-start)/2;
      start = middle - step/2;
      stop  = middle + step/2 + 1;
    } while (start+1<stop);
    return data[start];
  }
  public void estimateRemedianSelectorGain
    ( int b, int r ) {
    int c = 1;
    for (int i=0; i<r; ++i) c*=b;
    double[] count = new double[c];
    for (int i=0; i<runCount; ++i) {
      ++count[rankOfRemedian(c, b)];
    }
    double totalGain = 0.0;
    for (int i=1; i<=c; ++i) {
      if (0<count[i-1]) {
        double frac           = (double) count[i-1] / runCount;
        double gainIfAllUsed  = Gain.harmonic(c+1)/Math.log(2.0);
        double gainSacrificed = (i * Gain.harmonic(i) + (c+1-i) * Gain.harmonic(c+1-i))
                              / (double)(c+1)/Math.log(2.0);
        double gain          = gainIfAllUsed - gainSacrificed;
        System.out.println("" + i
          + "\t" + Math.floor(frac*100000.0+.5)/100000.0 
          + "\t" + Math.floor(gain*100000.0+.5)/100000.0
          + "\t" + Math.floor(frac*gain*100000.0+.5)/100000.0);
        totalGain += frac*gain;
      }
    }
    System.out.println
      ( "Total gain " + Math.floor(totalGain*100000.0)/100000.0 );
  }
  @Test
  public void testBustedTukeySelectorGain() {
    runCount = 1000000;
    for (int c = 7; c<9; ++c) {
      double[] count = new double[c];
      for (int r=0; r<runCount; ++r) {
        ++count[rankOfRemedian(7, 3)];
      }
      double totalGain = 0.0;
      for (int i=1; i<=c; ++i) {
        if (0<count[i-1]) {
          double frac           = (double) count[i-1] / runCount;
          double gainIfAllUsed  = Gain.harmonic(c+1)/Math.log(2.0);
          double gainSacrificed = (i * Gain.harmonic(i) + (c+1-i) * Gain.harmonic(c+1-i))
                                / (double)(c+1)/Math.log(2.0);
          double gain          = gainIfAllUsed - gainSacrificed;
          System.out.println("" + i 
            + "\t" + Math.floor(frac*100000.0+.5)/100000.0 
            + "\t" + Math.floor(gain*100000.0+.5)/100000.0
            + "\t" + Math.floor(frac*gain*100000.0+.5)/100000.0);
          totalGain += frac*gain;
        }
      }
      System.out.println
        ( "Total gain " + Math.floor(totalGain*100000.0)/100000.0);
    }
  }
  @Test
  public void testTukeyGain() {
    runCount = 10000000;
    estimateRemedianSelectorGain(3,0); //0.72134
    estimateRemedianSelectorGain(3,1); //0.84157 median-of-3
    estimateRemedianSelectorGain(3,2); //0.91908 tukey's ninther
    estimateRemedianSelectorGain(3,3); //0.96139 pseudomedian of 27
    estimateRemedianSelectorGain(3,4); //0.98226 psuedomedian of 81
  }
  @Test
  public void testBase5RemedianGain() {
    runCount = 10000000;
    estimateRemedianSelectorGain(5,1); //0.88966 median-of-5
    estimateRemedianSelectorGain(5,2); //0.96443 pseudomedian of 25
    estimateRemedianSelectorGain(5,3); //0.98947 pseudomedian of 125
  }
  @Test
  public void testBase7RemedianGain() {
    runCount = 10000000;
    estimateRemedianSelectorGain(7,1); //0.91542 median-of-7
    estimateRemedianSelectorGain(7,2); //0.98036 pseudomedian of 49
  }
}
