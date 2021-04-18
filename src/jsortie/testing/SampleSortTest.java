package jsortie.testing;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import jsortie.RangeSorter;
import jsortie.earlyexitdetector.NullEarlyExitDetector;
import jsortie.earlyexitdetector.RangeSortEarlyExitDetector;
import jsortie.earlyexitdetector.TwoWayInsertionEarlyExitDetector;
import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.janitors.insertion.InstrumentedBinaryInsertionSort;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.PositionalSampleCollector;
import jsortie.quicksort.collector.RandomSampleCollector;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.branchavoiding.BalancedSkippyExpander;
import jsortie.quicksort.governor.QuicksortGovernor;
import jsortie.quicksort.governor.adaptive.QuicksortAdaptive;
import jsortie.quicksort.indexselector.IndexSelector;
import jsortie.quicksort.indexselector.MiddleIndexSelector;
import jsortie.quicksort.indexselector.PositionalIndexSelector;
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
import jsortie.quicksort.multiway.partitioner.centripetal.CentripetalPartitioner3;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner3;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner4;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner2;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner3;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner4;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner5;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner6;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner7;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner2;
import jsortie.quicksort.multiway.samplesort.MultiPivotInternalSampleSort;
import jsortie.quicksort.multiway.selector.clean.CleanMultiPivotPositionalSelector;
import jsortie.quicksort.partitioner.bidirectional.traditional.HoyosPartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.SingletonPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyPartitioner;
import jsortie.quicksort.partitioner.insideout.CentripetalPartitioner;
import jsortie.quicksort.partitioner.instrumented.InstrumentedSingletonPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.lomuto.LomutoMirrorPartitioner;
import jsortie.quicksort.samplesizer.ChenSampleSizer;
import jsortie.quicksort.samplesizer.FixedSampleSizer;
import jsortie.quicksort.samplesizer.OversamplingSampleSizer;
import jsortie.quicksort.samplesizer.SampleSortSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;
import jsortie.quicksort.samplesort.AdaptiveInternalSampleSort;
import jsortie.quicksort.samplesort.AsymmetricSampleSort;
import jsortie.quicksort.samplesort.ExternalSampleSort;
import jsortie.quicksort.samplesort.FancierExternalSampleSort;
import jsortie.quicksort.samplesort.InternalSampleSort;
import jsortie.quicksort.samplesort.LazySampleSort;
import jsortie.quicksort.samplesort.SymmetryPartitionSort;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.clean.CleanRemedianSelector;
import jsortie.quicksort.selector.dirty.DirtyTheoreticalSelector;
import jsortie.quicksort.selector.simple.FirstElementSelector;
import jsortie.quicksort.selector.simple.MiddleElementSelector;
import jsortie.quicksort.theoretical.Gain;

public class SampleSortTest {
	int maxCount = 10000000;
	int runCount = 25;
	protected RandomInput random = new RandomInput();
	protected SortTest sortTest = new SortTest();
	protected SinglePivotSelector me = new MiddleElementSelector();
	protected SinglePivotSelector thoeretical = new DirtyTheoreticalSelector(.5);
	protected SinglePivotSelector theoreticalA = new DirtyTheoreticalSelector(.125);
	protected SinglePivotPartitioner hoyos = new HoyosPartitioner();
	protected SinglePivotPartitioner party = hoyos;
	protected SinglePivotPartitioner kangaroo = new SkippyPartitioner();
	protected PartitionExpander      balkanga = new BalancedSkippyExpander();
	protected RangeSorter isort = new InsertionSort2Way();
	protected TwoAtATimeHeapsort heapsort = new TwoAtATimeHeapsort();
	protected RangeSortEarlyExitDetector eed = new NullEarlyExitDetector();
	protected int janitorThreshold = 64;
	protected boolean adaptive = false;
	protected SinglePivotSelector remedian = new CleanRemedianSelector(true);
	protected SquareRootSampleSizer sqr = new SquareRootSampleSizer(1);
	protected PositionalSampleCollector pos = new PositionalSampleCollector(); 
	protected SinglePivotSelector theoretical = new DirtyTheoreticalSelector(.5);

	public SampleSortTest() {
	}

	public static SampleSortTest newSampleSortTest(int maxCount, int runCount) {
		SampleSortTest x = new SampleSortTest();
		x.maxCount = maxCount;
		x.runCount = runCount;
		return x;
	}

  protected void addInternalSampleSort
    ( OversamplingSampleSizer sizer
    , SampleCollector collector
    , List<RangeSorter> sorts) {
    if (adaptive) {
      sorts.add ( new AdaptiveInternalSampleSort
                      ( sizer, collector, party, isort
                      , janitorThreshold, eed));
    } else {
      sorts.add ( new InternalSampleSort
                      ( sizer, collector, party
                      , isort, janitorThreshold));
    }
  }
  
  protected void addSymmetryPartitionSort
    ( OversamplingSampleSizer sizer
    , SampleCollector collector
    , List<RangeSorter> sorts) {
    sorts.add ( new SymmetryPartitionSort
                    ( sizer, collector, party
                    , isort, janitorThreshold));
  }

  protected void setAdaptive(boolean isAdaptive) {
    eed = isAdaptive 
      ? (new TwoWayInsertionEarlyExitDetector()) 
      : (new NullEarlyExitDetector());
    adaptive = isAdaptive;
  }

	protected void setPartitioner(SinglePivotPartitioner partitioner) {
		party = partitioner;
	}

  @Test
  public void testSampleSorts() {
    //Penguin: Needs to be re-run; theoretical is now 
    //based on ZeroDelta489Partitioner rather than A489.
    System.out.println("\tTesting " + (adaptive ? "adaptive " : "") + "Sample Sorts with " + party.toString());
    SortList sorts = new SortList();
    sorts.add(new QuicksortAdaptive(me, party, isort, janitorThreshold, heapsort, eed));
    sorts.add(new QuicksortAdaptive(remedian, party, isort, janitorThreshold, heapsort, eed));
    sorts.add(new QuicksortAdaptive(theoretical, party, isort, janitorThreshold, heapsort, eed));
    OversamplingSampleSizer sqr = new SquareRootSampleSizer();
    OversamplingSampleSizer overSquare8 = new SquareRootSampleSizer(8);
    ChenSampleSizer chen = new ChenSampleSizer();
    
    SampleCollector nix  = new NullSampleCollector();
    SampleCollector pos  = new PositionalSampleCollector();
    SampleCollector rand = new RandomSampleCollector();
    
    addInternalSampleSort(sqr, pos, sorts);
    addInternalSampleSort(sqr, rand, sorts); // randomized sample sort
    addInternalSampleSort(sqr, nix,  sorts);     // sample
    addInternalSampleSort(overSquare8 , pos,  sorts); 
    addInternalSampleSort(chen, nix, sorts); //chen's collector with no shuffling
    addInternalSampleSort(chen, pos, sorts); //chen's collector, with chen's shuffling
    addInternalSampleSort(chen, rand, sorts); //chen's sort, randomized
    addInternalSampleSort(sqr, nix, sorts); //square root samplesort
    addInternalSampleSort(new SquareRootSampleSizer(0), nix, sorts); // square root samplesort
    addInternalSampleSort(sqr, rand, sorts); //square root samplesort, randomized
    sortTest.warmUpSorts(sorts);
    showEfficienciesOfSelectedSorts(sorts);
  }

  @Test
  public void testSampleSortPartitioners() {
    PartitionerTest pt = new PartitionerTest();
    SortList sorts = new SortList();
    
    for (SinglePivotPartitioner p : pt.getLotsOfPartitioners()) {
      party = p;
      addInternalSampleSort(new SquareRootSampleSizer(0), new PositionalSampleCollector(), sorts);
    }
    System.out.println("Average efficiency, sorting random 65536-integer permutations with InternalSampleSort");
    sortTest.warmUpSorts(sorts);
    String text = sorts.getSortHeader("efficiency") + "\n" + sortLine(65536, sorts);
    System.out.println(transpose(text));
    sorts.clear();
    for (SinglePivotPartitioner p : pt.getLotsOfPartitioners()) {
      party = p;
      addSymmetryPartitionSort
        ( new SquareRootSampleSizer(0)
        , new PositionalSampleCollector(), sorts);
    }
    System.out.println("\nAverage efficiency, sorting random 65536-integer permutations with InternalSampleSort");
    sortTest.warmUpSorts(sorts);
    text = sorts.getSortHeader("efficiency") + "\n" + sortLine(65536, sorts);
    System.out.println(transpose(text));
    sorts.clear();
    party = hoyos;
  }
  
	public String transpose(String text) {
		String[] linesIn = text.split("\n");
		String[] firstLine = linesIn[0].split("\t");
		StringBuilder[] linesOut = new StringBuilder[firstLine.length];
		for (int field = 0; field < firstLine.length; ++field) {
			linesOut[field] = new StringBuilder(firstLine[field]);
		}
		for (int line = 1; line < linesIn.length; ++line) {
			String[] fieldsIn = linesIn[line].split("\t");
			for (int field = 0; field < fieldsIn.length && field < linesOut.length; ++field) {
				linesOut[field].append("\t");
				linesOut[field].append(fieldsIn[field]);
			}
		}
		StringBuilder answer = new StringBuilder();
		for (int line = 0; line < linesOut.length; ++line) {
			answer.append((0 == line) ? "" : "\n");
			answer.append(linesOut[line]);
		}
		return answer.toString();
	}

	@Test
	public void testBranchAvoidingSampleSorts() {
		testSampleSorts();
		party = kangaroo;
		testSampleSorts();
		party = hoyos;
	}

  @Test
  public void testSymmetryPartitionSorts() {
    System.out.println("\tTesting SymmetryPartitionSort with " + party.toString());
    SortList sorts = new SortList();
    sorts.add(new QuicksortAdaptive(me, party, isort, janitorThreshold, heapsort, eed));
    sorts.add(new QuicksortAdaptive(remedian, party, isort, janitorThreshold, heapsort, eed));
    sorts.add(new QuicksortAdaptive(theoretical, party, isort, janitorThreshold, heapsort, eed));
    OversamplingSampleSizer samp = new SampleSortSizer(0);
    OversamplingSampleSizer sqr  = new SquareRootSampleSizer();
    OversamplingSampleSizer chen = new ChenSampleSizer(16);
    SampleCollector    nix  = new NullSampleCollector();
    SampleCollector    pos  = new PositionalSampleCollector();
    SampleCollector    rand = new RandomSampleCollector();
    
    addSymmetryPartitionSort( samp, nix,  sorts);
    addSymmetryPartitionSort( samp, rand, sorts); // randomized sample sort
    addSymmetryPartitionSort( samp, pos,  sorts);  // sample sort with oversampling
    addSymmetryPartitionSort( new SampleSortSizer(8),  nix, sorts); // sample
    addSymmetryPartitionSort( chen, nix,  sorts);  //chen's sample size,  no shuffling
    addSymmetryPartitionSort( chen, pos,  sorts);  //chen's sample, positional shuffling
    addSymmetryPartitionSort( chen, rand, sorts); //chen's sample, random shuffling 
    addSymmetryPartitionSort( sqr,  nix,  sorts); // square root
    addSymmetryPartitionSort( sqr,  rand, sorts); // square root
    addSymmetryPartitionSort( sqr,  pos,  sorts); // square root
    sortTest.warmUpSorts(sorts);
    showEfficienciesOfSelectedSorts(sorts);
  }

	@Test
	public void testBranchAvoidingSymmetryPartitionSorts() {
		testSymmetryPartitionSorts();
		party = kangaroo;
		testSymmetryPartitionSorts();
		party = hoyos;
	}

	@Test
	public void testSampleAndSymmetryPartitionSorts() {
		testBranchAvoidingSampleSorts();
		testBranchAvoidingSymmetryPartitionSorts();
	}

  @Test
  public void testSampleSortsOnDegenerateInputs() {
    //Penguin: test needs to be re-run, theoretical is now ZD489 not A489
    SortList sorts = new SortList();
    SinglePivotSelector theoretical = new DirtyTheoreticalSelector(.5);
    sorts.add(new QuicksortAdaptive(me, party, isort, janitorThreshold, isort, eed));
    sorts.add(new QuicksortAdaptive(theoretical, party, isort, janitorThreshold, isort, eed));
    sortTest.warmUpSorts(sorts);
    sortTest.warmUpSorts(sorts);
    OversamplingSampleSizer chen = new ChenSampleSizer(16);
    SampleCollector         nix  = new NullSampleCollector();
    SampleCollector         pos  = new PositionalSampleCollector();
    SampleCollector         rand = new RandomSampleCollector();
    if (!adaptive) {
      addInternalSampleSort(chen, nix, sorts); // chen's sort
    }
    addInternalSampleSort(chen, pos, sorts); // chen's sort
    addInternalSampleSort(chen, rand, sorts); 
    DegenerateInputTest dit = DegenerateInputTest.newTest(65536, 100);
    sortTest.dumpHeaderLine("input", sorts);
    dit.runSortsOnSortedData(sorts, "millisecond");
    dit.runSortsOnDuplicatedData(sorts, "millisecond");
  }

	@Test
	public void testSampleSortsOnDegenerateInputs2() {
		setPartitioner(kangaroo);
		testSampleSortsOnDegenerateInputs();
		setPartitioner(hoyos);
	}

	@Test
	public void testSampleSortsOnDegenerateInputs3() {
		setAdaptive(true);
		setPartitioner(kangaroo);
		testSampleSortsOnDegenerateInputs();
		setPartitioner(hoyos);
		testSampleSortsOnDegenerateInputs();
	}

  public void testExternalSampleSorts() {
    RangeSorter janitor = new TwoAtATimeHeapsort();
    OversamplingSampleSizer fixed = new FixedSampleSizer(255, 0);
    OversamplingSampleSizer sqr   = new SquareRootSampleSizer(0);
    System.out.println("Testing External Sample Sort");
    SortList sorts = new SortList();
    sorts.add(new ExternalSampleSort(sqr, pos, janitor, 1024));
    sorts.add(new ExternalSampleSort(fixed, pos, janitor, 4096));
    sorts.add(new FancierExternalSampleSort(fixed, pos, janitor, 4096));
    sortTest.warmUpSorts(sorts);
    showEfficienciesOfSelectedSorts(sorts);
  }

  public void testExternalSampleSorts2() {
    RangeSorter lastResort = new TwoAtATimeHeapsort();
    System.out.println("Testing External Sample Sort");
    SortList sorts = new SortList();
    SampleCollector nix = new NullSampleCollector();
    for (int s = 3; s < 2048; s = s * 2 + 1) {
      OversamplingSampleSizer fSample = new FixedSampleSizer(s, 0);
      sorts.add(new FancierExternalSampleSort(fSample, nix, lastResort, 32768));
    }
    sortTest.warmUpSorts(sorts);
    showEfficienciesOfSelectedSorts(sorts);
  }

	public void testBleh() {
    OversamplingSampleSizer sqr= new SquareRootSampleSizer(0);
    SampleCollector pos = new PositionalSampleCollector();
    IndexSelector iSelector = new CleanMultiPivotPositionalSelector(2);
    FixedCountPivotPartitioner htt2 = new HolierThanThouPartitioner2();
    SinglePivotPartitioner party = new CentripetalPartitioner();
    RangeSorter s 
      = new MultiPivotInternalSampleSort
            ( sqr, pos, iSelector, htt2, 2, party, isort, 64 );
    int a[] = random.randomPermutation(1000);
    s.sortRange(a, 0, a.length);
  }
  public void testMultiPivotSampleSortsForPartitioners
    ( SinglePivotPartitioner p1
    , FixedCountPivotPartitioner[] partitioners) {
    SortList sorts 
      = new SortList();
    OversamplingSampleSizer  sqr 
      = new SquareRootSampleSizer(0);
    SampleCollector pos   
      = new PositionalSampleCollector();
    MultiPivotPartitioner mp1 
      = new SingleToMultiPartitioner(p1);
    sorts.add(new InternalSampleSort(sqr, pos, p1, isort, 64));
    sorts.add(new MultiPivotInternalSampleSort
      ( sqr, pos, new CleanMultiPivotPositionalSelector(1)
      , mp1, 1, p1, isort, 64));  
    for (int i=0; i<partitioners.length; ++i) {
      FixedCountPivotPartitioner mp = partitioners[i];
      int pivotCount = mp.getPivotCount();
      IndexSelector  msel 
        = new CleanMultiPivotPositionalSelector(pivotCount);
      sorts.add(new MultiPivotInternalSampleSort(
          sqr, pos, msel, mp, 1, p1, isort, 64));  
    }
    sortTest.warmUpSorts(sorts);
    showEfficienciesOfSelectedSorts(sorts);
  }
  @Test
  public void testMultiPivotSampleSorts() {
    // Tries out single-pivot sample sort, and 2- and 3- and 4-pivot sample
    // sorts with four different collectors.
    System.out.println("\tTesting multi-pivot Sample Sorts");
    testMultiPivotSampleSortsForPartitioners(
      new CentripetalPartitioner(), 
      new FixedCountPivotPartitioner[] 
        { new HolierThanThouPartitioner2()
        , new HolierThanThouPartitioner3()
        , new HolierThanThouPartitioner4()
      });
  }
  @Test
  public void testBranchAvoidingMultiPivotSampleSorts() {
    System.out.println("\tTesting branch-avoiding multi-pivot Sample Sorts");
    testMultiPivotSampleSortsForPartitioners(
      new SkippyPartitioner(), 
      new FixedCountPivotPartitioner[] 
        { new SkippyPartitioner2(), new SkippyPartitioner3()
        , new SkippyPartitioner4(), new SkippyPartitioner5()
        , new SkippyPartitioner6(), new SkippyPartitioner7()
      });
  }
  
  public void testMultiPivotSampleSorts2() {
    //Tries out single-pivot sample sort, and 2- and 3- and 4-pivot sample
    //sorts with four different collectors.
    //System.out.println("Testing multi-pivot Sample Sorts");
    SortList sorts = new SortList();
    OversamplingSampleSizer sqr = new SquareRootSampleSizer(0);
    SampleCollector pos = new PositionalSampleCollector(); 
    MiddleIndexSelector mie = new MiddleIndexSelector();
    SinglePivotPartitioner cp = new CentripetalPartitioner();
    MultiPivotPartitioner cpm = new SingleToMultiPartitioner(cp);
    IndexSelector i2 = new CleanMultiPivotPositionalSelector(2);
    MultiPivotPartitioner cp2 = new CentripetalPartitioner2();
    IndexSelector i3 = new CleanMultiPivotPositionalSelector(3);
    MultiPivotPartitioner cp3 = new CentripetalPartitioner3();
    sorts.add(new InternalSampleSort(sqr, pos, cp, isort, 64));
    sorts.add(new MultiPivotInternalSampleSort(sqr, pos, mie, cpm, 1, cp, isort, 64));
    sorts.add(new MultiPivotInternalSampleSort(sqr, pos, i2,  cp2, 2, cp, isort, 64));
    sorts.add(new MultiPivotInternalSampleSort(sqr, pos, i3,  cp3, 3, cp, isort, 64));
    sortTest.warmUpSorts(sorts);
    showEfficienciesOfSelectedSorts(sorts);
	}
	public void testMultiPivotSampleSorts3() {
    // Tries out single-pivot sample sort, and 2- and 3- and 4-pivot sample
    // sorts with four different collectors.
    System.out.println("Testing multi-pivot Sample Sorts");
    SortList sorts = new SortList();
    OversamplingSampleSizer sqr7 = new SquareRootSampleSizer(7);
    SampleCollector pos = new PositionalSampleCollector(); 
    IndexSelector mis = new MiddleIndexSelector();
    IndexSelector mis2 = new CleanMultiPivotPositionalSelector(2);
    IndexSelector mis3 = new CleanMultiPivotPositionalSelector(3);
    SinglePivotPartitioner cp = new CentripetalPartitioner();
    MultiPivotPartitioner cpm = new SingleToMultiPartitioner
                                    ( new CentripetalPartitioner() );
    MultiPivotPartitioner cp2 = new CentripetalPartitioner2();
    MultiPivotPartitioner cp3 = new CentripetalPartitioner3();
    sorts.add(new InternalSampleSort(sqr7, pos, cp, isort, 64));
    sorts.add(new MultiPivotInternalSampleSort(sqr7, pos, mis,  cpm, 1, cp, isort, 64));
    sorts.add(new MultiPivotInternalSampleSort(sqr7, pos, mis2, cp2, 2, cp, isort, 64));
    sorts.add(new MultiPivotInternalSampleSort(sqr7, pos, mis3, cp3, 3, cp, isort, 64));
    sortTest.warmUpSorts(sorts);
    showEfficienciesOfSelectedSorts(sorts);
	}
  public void testMultiPivotSampleSortsOnDegenerateInputs
    ( SinglePivotPartitioner p1
    , FixedCountPivotPartitioner[] partitioners) {
    SortList sorts 
      = new SortList();
    OversamplingSampleSizer sqr 
      = new SquareRootSampleSizer(0);
    SampleCollector pos 
      = new PositionalSampleCollector(); 
    MultiPivotPartitioner mp1 
      = new SingleToMultiPartitioner(p1);
    sorts.add(new InternalSampleSort(sqr, pos, p1, isort, 64));
    sorts.add(new MultiPivotInternalSampleSort
      ( sqr, pos
      , new CleanMultiPivotPositionalSelector(1), mp1, 1, p1, isort, 64));  
    for (int i=0; i<partitioners.length; ++i) {
      FixedCountPivotPartitioner mp = partitioners[i];
      int pivotCount = mp.getPivotCount();
      IndexSelector msel 
        = new CleanMultiPivotPositionalSelector(pivotCount);
      sorts.add(new MultiPivotInternalSampleSort(
          sqr, pos, msel, mp, pivotCount, p1, isort, 64));  
    }
    sortTest.warmUpSorts(sorts);
    DegenerateInputTest dit = new DegenerateInputTest ();
    dit.runCount  = 100;
    dit.maxCount = 65536;
    dit.runSortsOnSortedData(sorts, "second");
  }
  @Test
  public void testMPSentrySampleSortsOnDegenInputs() {
    testMultiPivotSampleSortsOnDegenerateInputs(
      new SingletonPartitioner(), 
      new FixedCountPivotPartitioner[] 
        { new CentrifugalPartitioner2(), new CentrifugalPartitioner3()
        , new CentrifugalPartitioner4(), new CentrifugalPartitioner5()
        , new CentrifugalPartitioner6(), new CentrifugalPartitioner7()
      });
  }
  @Test
  public void testMPKangaSampleSortsOnDegenInputs() {
    testMultiPivotSampleSortsOnDegenerateInputs(
      new SkippyPartitioner(), 
      new FixedCountPivotPartitioner[] 
        { new SkippyPartitioner2(), new SkippyPartitioner3()
        , new SkippyPartitioner4(), new SkippyPartitioner5()
        , new SkippyPartitioner6(), new SkippyPartitioner7()
      });
  }
  public void showEfficienciesOfSelectedSorts(SortList sorts) {
    sorts.writeSortHeader("n");
    for (int n = 10; n < maxCount; n = n * 11 / 10) {
      String line = sortLine(n, sorts);
      System.out.println(line);
    }
  }
  public String sortLine(int n, List<RangeSorter> sorts) {
		String line = "" + n;
		double elapsed[] = new double[sorts.size()];
		for (int r = 0; r < runCount; ++r) {
			int a[] = random.randomPermutation(n);
			for (int s = 0; s < sorts.size(); ++s) {
				RangeSorter sort = sorts.get(s);
				int b[] = Arrays.copyOf(a, n);
				long start = System.nanoTime();
				sort.sortRange(b, 0, b.length);
				elapsed[s] += System.nanoTime() - start;
			}
		}
		double bitsAcquired = ((double) n * Math.log(n) / Math.log(2) - (double) n / Math.log(2)) * runCount;
		for (int s = 0; s < sorts.size(); ++s) {
			line += "\t" + Math.floor(1000.0 * bitsAcquired / elapsed[s]) / 1000.0;
		}
		return line;
	}

  public void testSymmetryPartitionSortsAgain() {
    System.out.println("Testing Symmetry Partition Sort with different values of alpha");
    SortList sorts = new SortList();
    SampleCollector pos   = new PositionalSampleCollector();
    for (int alpha = 2; alpha < 8; ++alpha) {
      sorts.add ( new InternalSampleSort
                      ( new ChenSampleSizer(alpha)
                      , pos, hoyos, isort, 5)); 
    }
    sorts.add ( new InternalSampleSort
                    ( new ChenSampleSizer(2, 1)
                    , pos, hoyos, isort, 5)); 
    showEfficienciesOfSelectedSorts(sorts);
  }

  @Test
  public void testSampleSortTiming() {
    SortList sorts = new SortList();
    System.out.println("Testing sample Sorts (sorting efficiency in Gbps)");
    PositionalSampleCollector pos = new PositionalSampleCollector(); 
    sorts.add(new InternalSampleSort
                  ( new SquareRootSampleSizer()
                  , new PositionalSampleCollector()
                  , hoyos, isort, 64)); 
    sorts.add(new InternalSampleSort
                  ( new SquareRootSampleSizer(1)
                  , pos, hoyos, isort, 64)); 
    sorts.add(new InternalSampleSort
                  ( new SquareRootSampleSizer(8) 
                  , pos , hoyos, isort, 64)); 
    sorts.add(new InternalSampleSort
                  ( new ChenSampleSizer(16)
                  , pos, hoyos, isort, 64));
    sorts.add(new InternalSampleSort
                  ( new SquareRootSampleSizer(0)
                  , pos, hoyos, isort, 64)); 
    sorts.add(new QuicksortGovernor(new FirstElementSelector(), hoyos, isort, 64));
    sortTest.warmUpSorts(sorts);
    sortTest.testSpecificSorts(sorts, 500, 10000, 400, 10000000, 25);
  }

  public void testAsymmetricSampleSortTiming() {
    SquareRootSampleSizer sqr = new SquareRootSampleSizer();
    NullSampleCollector   nix = new NullSampleCollector(); 
    SortList sorts = new SortList();
		sorts.add(new InternalSampleSort(sqr, nix, hoyos, isort, 64)); // sample
		                                                                                 // sort
		sorts.add(new AsymmetricSampleSort(sqr, nix, hoyos, isort, 64)); // sample
		                                                                                   // sort
		sorts.add(new QuicksortGovernor(new FirstElementSelector(), hoyos, isort, 64));
		sortTest.warmUpSorts(sorts);
		sortTest.testSpecificSorts(sorts, 500, 10000, 400, 10000000, 25);
	}

  public void testDualPivotSampleSort() {
    SortList sorts = new SortList();
    OversamplingSampleSizer sqr = new SquareRootSampleSizer(0);
    SampleCollector pos = new PositionalSampleCollector();
    CentripetalPartitioner cp = new CentripetalPartitioner();
    FixedCountPivotPartitioner htt2 = new HolierThanThouPartitioner2();
    FixedCountPivotPartitioner htt3 = new HolierThanThouPartitioner3();
    FixedCountPivotPartitioner htt4 = new HolierThanThouPartitioner4();
    IndexSelector is2 = new CleanMultiPivotPositionalSelector(2);
    IndexSelector is3 = new CleanMultiPivotPositionalSelector(3);
    IndexSelector is4 = new CleanMultiPivotPositionalSelector(4);
    sorts.add(new MultiPivotInternalSampleSort(sqr, pos, is2, htt2, 2, cp, isort, 64));
    sorts.add(new MultiPivotInternalSampleSort(sqr, pos, is3, htt3, 3, cp, isort, 64));
    sorts.add(new MultiPivotInternalSampleSort(sqr, pos, is4, htt4, 4, cp, isort, 64));
    sortTest.warmUpSorts(sorts);
    sortTest.testSpecificSorts(sorts, 500, 10000, 400, 10000000, 25);
  }

  @Test
  public void testLazySampleSortTiming() {
    int thresh = 64;
    SortList sorts = new SortList();
    SinglePivotPartitioner kanga = new SkippyPartitioner();
    OversamplingSampleSizer sqr = new SquareRootSampleSizer(1);
    SampleCollector pos = new PositionalSampleCollector();
    sorts.add(new InternalSampleSort(sqr, pos, kanga, isort, thresh), "InternalSampleSort");
    sorts.add(new SymmetryPartitionSort(sqr, pos, kanga, isort, thresh), "SymmetryPartitionSort");
    sorts.add(new LazySampleSort(sqr, pos, kanga, isort, thresh), "LazySampleSort");
    sortTest.warmUpSorts(sorts);
    DegenerateInputTest dit = DegenerateInputTest.newTest(65536, 1);
    System.out.println("Testing Interal Sample Sort variants with SquareRootCollector and Kangaroo partitioners"
        + " (running times in milliseconds to sort " + dit.maxCount + " integer inputs)");
    sorts.writeSortHeader("Input");
    dit.runSortsOnSortedData(sorts, "millisecond");
    dit.runSortsOnDuplicatedData(sorts, "millisecond");
  }

  @Test
  public void testSymmetryPartitionSortTiming() {
    int thresh = 64;
    SortList sorts = new SortList();
    SinglePivotPartitioner kanga = new SkippyPartitioner();
    SinglePivotPartitioner centripetal = new CentripetalPartitioner();
    SinglePivotPartitioner singleton = new SingletonPartitioner();
    SinglePivotPartitioner lomuto = new LomutoMirrorPartitioner();
    
    OversamplingSampleSizer chen = new ChenSampleSizer();
    OversamplingSampleSizer sqr  = new SquareRootSampleSizer(1);
    
    SampleCollector pos  = new PositionalSampleCollector();
    IndexSelector oneOf8 = new PositionalIndexSelector(new int[] { 1 }, 8);
    SymmetryPartitionSort chenSort 
      = new SymmetryPartitionSort(chen, pos, singleton, isort, thresh);

    sorts.add(chenSort); // chen's sort
		sorts.add(new SymmetryPartitionSort(chen, pos, centripetal, isort, thresh));
		sorts.add(new SymmetryPartitionSort(chen, pos, lomuto, isort, thresh));
		sorts.add(new SymmetryPartitionSort(chen, pos, oneOf8, centripetal, isort, thresh)); // asymmetric
		sorts.add(new SymmetryPartitionSort(chen, pos, oneOf8, lomuto, isort, thresh)); // asymmetric
		sorts.add(new SymmetryPartitionSort(chen, pos, kanga, isort, thresh)); // chen's
		                                                                    // sort
		sorts.add(new SymmetryPartitionSort(sqr, pos, kanga, isort, thresh));
		sortTest.warmUpSorts(sorts);
		DegenerateInputTest dit = DegenerateInputTest.newTest(65536, 25);
		System.out.println("Testing Symmetry Partition Sort variants" + " (running times in milliseconds to sort "
		    + dit.maxCount + " integer inputs)");
		sorts.writeSortHeader("Input");
		dit.runSortsOnSortedData(sorts, "millisecond");
		dit.runSortsOnDuplicatedData(sorts, "millisecond");
		System.out.println("Testing Symmetry Partition Sort with low alpha" + ", and s>0 (sorting efficiency in Gbps)");
	}

  @Test
  public void testHoyosSymmetryPartitionSorts() {
    int thresh = 64;
    SortList           sorts  = new SortList();
    OversamplingSampleSizer chen16 = new ChenSampleSizer(16);
    OversamplingSampleSizer sqr    = new SquareRootSampleSizer();
    IndexSelector      oneOf8 = new PositionalIndexSelector(new int[] { 1 }, 8);
    SampleCollector    nix    = new NullSampleCollector();
    SampleCollector    pos    = new PositionalSampleCollector();
    SampleCollector    rand   = new RandomSampleCollector();

		sorts.add(new QuicksortAdaptive(me, hoyos, isort, janitorThreshold, heapsort, eed), "Quicksort");
		sorts.add(new QuicksortAdaptive(theoretical, hoyos, isort, janitorThreshold, heapsort, eed),
		    "Quicksort/Theoretical(.5)");
		sorts.add(new QuicksortAdaptive(theoreticalA, hoyos, isort, janitorThreshold, heapsort, eed),
		    "Quicksort/Theoretical(.125)");
		sorts.add(new SymmetryPartitionSort(chen16, nix,  hoyos, isort, thresh), "SPS (NullChenCollector, alpha=16)");
		sorts.add(new SymmetryPartitionSort(chen16, pos,  hoyos, isort, thresh), "SPS (ChenCollector, alpha=16)");
		sorts.add(new SymmetryPartitionSort(chen16, rand, hoyos, isort, thresh),
		    "SPS (RandomizedChenCollector, alpha=16)");
		sorts.add(new SymmetryPartitionSort(sqr, pos, hoyos, isort, thresh), "SPS (Sqrt Collector)");
		sorts.add(new SymmetryPartitionSort(sqr, pos, oneOf8, hoyos, isort, thresh), "SPS (Sqrt Collector, Asymmetric)");
		sortTest.warmUpSorts(sorts);
		DegenerateInputTest dit = DegenerateInputTest.newTest(65536, 100);
		System.out.println("Testing Symmetry Partition Sort variants" + " (running times in milliseconds to sort "
		    + dit.maxCount + " integer inputs)");
		sorts.writeSortHeader("Input");
		dit.runSortsOnSortedData(sorts, "millisecond");
		dit.runSortsOnDuplicatedData(sorts, "millisecond");
	}

	public static double log2nOrZero(double n) {
		return (n == 0) ? 0 : Math.log(n) / Math.log(2);
	}

	public void testSampleSortAnalogue() {
		int levels = 24;
		int pivots = (1 << levels) - 1;
		double ranks[] = new double[pivots + 2];
		ranks[0] = 0;
		for (int p = 1; p <= pivots; ++p) {
			ranks[p] = Math.random();
		}
		ranks[1] = 1;
		Arrays.sort(ranks);
		int depth = 0;
		double totalGain = 0;
		for (int s = (1 << levels); s > 1; s = s / 2, ++depth) {
			double gain = 0;
			for (int p = s / 2; p <= pivots; p += s) {
				double a = ranks[p - s / 2];
				double b = ranks[p];
				double c = ranks[p + s / 2];
				gain += (c - a) * log2nOrZero(c - a) - (b - a) * log2nOrZero(b - a) - (c - b) * log2nOrZero(c - b);
			}
			System.out.println("" + depth + "\t" + gain + "\t");
			totalGain += gain;
		}
		System.out.println("total\t" + totalGain + "\t");
	}

	@Test
	public void testPredictedOverheadForSampleSortPartition() {
		for (int c = 1, p = 1; c < 1048576; c += c + 1, ++p) {
			double gain = (Gain.harmonic(c + 1) - 1) / Math.log(2.0);
			System.out.println("" + c + "\t" + gain + "\t" + ((double) p - gain));
		}
		double y = (1 - 0.57721) / Math.log(2);
		System.out.println("" + y);
	}

	public void testOversamplingComparisonDelta() {
		for (double alpha = 2; alpha < 128; alpha *= 2) {
			String line = "";
			for (double s = 2; s < 128; s *= 2) {
				double loss = (Gain.harmonic(s + 2) - 1 - (1.0 - 1 / alpha) * Math.log(s + 1)) / 2;
				line = line + ((s == 2) ? "" : "\t") + loss;
			}
			System.out.println(line);
		}
	}

	@Test
	public void testPredictedOverheadAsFunctionOfS() {
		for (int s = 0; s < 1000; ++s) {
			double mascheroni = .577215664901532;
			double overhead = (mascheroni + Math.log(s + 1) - Gain.harmonic(s + 1)) / Math.log(2);
			System.out.println("" + s + "\t" + overhead);
		}
	}
  public String f5(double x) {
    return "\t" + (Math.floor(x * 100000.0 + .5) / 100000.0);
  }
  @Test
  public void testActualOverheads() {
    InstrumentedSingletonPartitioner singly
      = new InstrumentedSingletonPartitioner();
    InstrumentedBinaryInsertionSort bin
      = new InstrumentedBinaryInsertionSort();
    SortList ss = new SortList();
    SampleCollector nix = new NullSampleCollector();
    ss.add(new InternalSampleSort(new ChenSampleSizer(1024.0), nix, singly, bin, 2048));
    ss.add(new InternalSampleSort(new ChenSampleSizer(256.0),  nix, singly, bin, 512));
    ss.add(new InternalSampleSort(new ChenSampleSizer(64.0),   nix, singly, bin, 128));
    ss.add(new InternalSampleSort(new ChenSampleSizer(16.0),   nix, singly, bin, 32));
//    ss.add(new InternalSampleSort(new ChenSampleSizeSelector(4.0),  nix, singly, bin,  8));
//    ss.add(new InternalSampleSort(new ChenSampleSizeSelector(2.0),  nix, singly, bin,  8));
    int s= ss.size();
    for (int n=100;n<1000000;n=n*5/4) {
      int runCount = 10;
      double[] partitionerMoves     = new double[s];
      double[] partitionerOverheads = new double[s];
      double[] janitorMoves         = new double[s];
      double[] janitorOverheads     = new double[s];
      for (int r=0; r<runCount; ++r) {
        int[] crud = random.randomPermutation(n);
        for (int i=0; i<ss.size(); ++i ) {
          int[] copy = new int[n];
          for (int k=0; k<n; ++k) {
            copy[k] = crud[k];
          }
          singly.zeroCounts();
          bin.zeroCounts();
          ss.get(i).sortRange(copy, 0, copy.length);
          partitionerMoves[i]     += singly.getMoveCount();
          partitionerOverheads[i] += singly.getOverheadComparisonCount();
          janitorMoves[i]         += bin.getMoveCount();
          janitorOverheads[i]     += bin.getOverheadComparisonCount();
        }
      }
      String line = "" + n;
      for (int i=0; i<ss.size(); ++i) {
        //double moveCost  = partitionerMoves[i]     / (double)runCount / (double)n / Math.log(n) * Math.log(2.0);
        double overhead1 = partitionerOverheads[i] / (double)runCount / (double)n;
        //double overhead2 = janitorOverheads[i]     / (double)runCount / (double)n;
        line += f5(overhead1); //f5(moveCost) + f5(overhead1) + f5(overhead2);
      }
      System.out.println(line);
    }
  }

	@Test
  public void testGamma() {
    for (double x=1; x<10000; ++x ) {
      System.out.println ( "" + (Gain.harmonic(x)-Math.log(x)) + "\t" + (Math.log(1.7810724*x)-Gain.harmonic(x)));
    }
  }
}
