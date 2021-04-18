package jsortie.testing;

import java.util.Arrays;
import java.util.List;

import jsortie.ClaytonsSort;
import jsortie.RangeSorter;
import jsortie.earlyexitdetector.NullEarlyExitDetector;
import jsortie.earlyexitdetector.RangeSortEarlyExitDetector;
import jsortie.earlyexitdetector.TwoWayInsertionEarlyExitDetector;
import jsortie.heapsort.bottomup.HeapsortBottomUp;
import jsortie.heapsort.topdown.HeapsortRadix3;
import jsortie.heapsort.topdown.HeapsortRadix4;
import jsortie.heapsort.topdown.HeapsortStandard;
import jsortie.heapsort.topdown.ToplessHeapsort;
import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.helper.RangeSortHelper;
import jsortie.janitors.StrategicJanitorSort;
import jsortie.janitors.TwoModeJanitor;
import jsortie.janitors.exchanging.AlternatingCombsort;
import jsortie.janitors.exchanging.BranchAvoidingCombsort;
import jsortie.janitors.exchanging.BubbleSort;
import jsortie.janitors.exchanging.CascadingCombSort;
import jsortie.janitors.exchanging.Combsort;
import jsortie.janitors.exchanging.BranchAvoidingAlternatingCombsort;
import jsortie.janitors.exchanging.BranchAvoidingBubbleSort;
import jsortie.janitors.exchanging.PrattSort;
import jsortie.janitors.insertion.BinaryInsertionSort;
import jsortie.janitors.insertion.BranchAvoidingBinaryInsertionSort;
import jsortie.janitors.insertion.FourFoldInsertionSort;
import jsortie.janitors.insertion.InsertionSort;
import jsortie.janitors.insertion.PairInsertionSort;
import jsortie.janitors.insertion.PairInsertionSortRevised;
import jsortie.janitors.insertion.PessimisticInsertionSort;
import jsortie.janitors.insertion.PleatedInsertionSort;
import jsortie.janitors.insertion.QuadrupletInsertionSort;
import jsortie.janitors.insertion.SentinelInsertionSort;
import jsortie.janitors.insertion.OrigamiInsertionSort;
import jsortie.janitors.insertion.ShellSort;
import jsortie.janitors.insertion.twoway.BranchAvoidingCocktailSort;
import jsortie.janitors.insertion.twoway.CocktailSort;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.janitors.insertion.twoway.InsertionSort2WayRevised;
import jsortie.janitors.insertion.twoway.ShellSort2Way;
import jsortie.janitors.insertion.twoway.StableInsertionSort2Way;
import jsortie.janitors.selection.BranchAvoidingSelectionSort;
import jsortie.janitors.selection.CanberraBubbleSort;
import jsortie.janitors.selection.SelectionSort;
import jsortie.janitors.selection.SelectionSort2Way;
import jsortie.mergesort.asymmetric.AsymmetricMergesort;
import jsortie.mergesort.splicing.SpliceMergesort;
import jsortie.mergesort.timsort.IntegerTimsortWrapper;
import jsortie.mergesort.vanilla.StaircaseMergesort;
import jsortie.quicksort.discriminator.Discriminator;
import jsortie.quicksort.discriminator.PartitionSizeDiscriminator;
import jsortie.quicksort.governor.QuicksortGovernor;
import jsortie.quicksort.governor.adaptive.DiscriminatingQuicksort;
import jsortie.quicksort.governor.adaptive.QuicksortAdaptive;
import jsortie.quicksort.governor.traditional.QuicksortClassic;
import jsortie.quicksort.multiway.governor.MultiPivotQuicksort;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.twopivot.YaroslavskiyPartitioner2;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.partitioner.bidirectional.centrepivot.CentrePivotPartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.HoarePartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.HoyosPartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.SingletonPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.TunedPartitioner;
import jsortie.quicksort.partitioner.insideout.CentripetalPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.lomuto.LomutoPartitioner;
import jsortie.quicksort.partitioner.unidirectional.HolierThanThouPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.clean.CleanLeftHandedSelector;
import jsortie.quicksort.selector.dirty.DirtyTheoreticalSelector;
import jsortie.quicksort.selector.dirty.Java8Selector;
import jsortie.quicksort.selector.simple.FirstElementSelector;
import jsortie.quicksort.selector.simple.MiddleElementSelector;
import jsortie.quicksort.sort.QuicksortStrategic;
import jsortie.quicksort.sort.monolithic.PureSkippySort;

import org.junit.Test;

public class JanitorTest {
  static int runCount = 10;
  double siftCompares = 0;
  DegenerateInputTest dit = DegenerateInputTest.newTest(500, 100);
  RandomInput      random = new RandomInput();
  SortTest       sortTest = new SortTest();
  final double BILLION = 1000.0*1000.0*1000.0;

  @Test
  public void testBlehsky() {
	double tp = 0;
    for (int n=3; n<=1023; n=n*2+1) {
      double count = 0;
      double tries = 0;
      for (int run=0; run<1000000; ++run) {
    	int[] crap = random.randomPermutation(n*2);
    	fakeHeap(crap, 0, n);
    	fakeHeap(crap, n, 2*n);
    	for (int i=n+(n/2); i<2*n; ++i) {
          ++tries;
          count += ( crap[0] < crap[i] ) ? 1 : 0;
    	}
      }
      double p = (double)count / (double)tries;
      tp += p;
      System.out.println("" + n + "\t" + p + "\t" + tp );
    }
  }
  protected void fakeHeap(int[] vArray, int start, int stop) {
    //heap construction phase
	int fudge = start - 1;
    for (int h=start+(stop-start+1)/2;h>=start;--h) { 
      int i = h;
      int v = vArray[i];
      int j = i - fudge + i;	
      while (j<stop) {
        if (j+1<stop) {
          j += (vArray[j] < vArray[j+1]) ? 1 : 0;
        }
        if (vArray[j]<=v) {
          break;
        }
        vArray[i]=vArray[j];
        i = j;
        j = i - fudge + i;
      }
      vArray[i]=v;			
    }
  }
  @Test
  public void testBleh2() {
    int   n    = 10000000;
  	int[] crap = random.randomPermutation(n);
	fakeHeap(crap, 0, n);
	siftCompares = 0;
	extractFromHeap(crap, 0, n);
	System.out.println( (double)siftCompares / (double )n);
  }
  public void extractFromHeap
    ( int[] vArray, int start, int stop ) {
    int fudge= start - 1;
    //heap extraction phase
    int firstChild=start+2;
    for (--stop;stop>=firstChild;stop--) {
      int v = vArray[stop];		
      int i = stop;	    
      int j = start;

      //extract, assuming v will go into a bottom-level node
      do {
        if (j+1<stop) {
          j += (vArray[j] < vArray[j+1]) ? 1 : 0;
        }
        vArray[i] = vArray[j];
        i = j;
        j = i - fudge + i;	
      } while (j<stop);

      //search back up the path toward the 
      //top of the heap, to place v
      int h = (i-fudge)/2 + fudge;
      while (start<=h && vArray[h]<v) {
        ++siftCompares;
        vArray[i] = vArray[h];
        i = h;
        h = (i-fudge)/2+fudge;
      }	
      vArray[i] = v;
    }    
    InsertionSort.sortSmallRange(vArray, start, firstChild);
  }
  public void tryInsertionSortThresholds() {		
    SinglePivotSelector selector = new FirstElementSelector();
    SinglePivotPartitioner partitioner = new SingletonPartitioner();
    RangeSorter janitor = new InsertionSort();
    List<RangeSorter> sorts = new SortList();

    sorts.clear();
    sorts.add(new QuicksortGovernor(selector, partitioner, janitor, 9));
    sorts.add(new QuicksortStrategic(selector, partitioner, janitor, 9));
    System.out.println("Tactical versus Strategic sorting, for a Classic (1973); Quicksort");
    sortTest.testSpecificSorts(sorts, 1000000, 1000001, 25);
		
    sorts.clear();
    for (int thresh=10; thresh<=200; thresh+=10) {
      sorts.add(new QuicksortGovernor( selector, partitioner, janitor, thresh));
    }
    System.out.println("Insertion sort thresholds for a Classic (1973); Quicksort (multiples of 10)");
    sortTest.testSpecificSorts(sorts, 1000000, 1000001, 25);
  }
  public void insertion150VersusHeapsort1000() {
    SinglePivotSelector selector = new FirstElementSelector();
    SinglePivotPartitioner partitioner = new SingletonPartitioner();
    List<RangeSorter> sorts = new SortList();

    sorts.clear();
    sorts.add(new QuicksortGovernor(selector, partitioner, new InsertionSort(), 9));
    sorts.add(new QuicksortGovernor(selector, partitioner, new InsertionSort(), 150));
    sorts.add(new QuicksortGovernor(selector, partitioner, new HeapsortStandard(), 1000));
    System.out.println("Insertion sort with thresholds 9 and 150, versus Heapsort with threshold 1000, for a Classic (1973) Quicksort");
    sortTest.testSpecificSorts(sorts, 1000000, 1000001, 25);
  }
  public void tryJanitorsHeadToHead(int step) {
    int n = 10000 * step;
    int [] vArray = new int[n];
    for (int i=0; i<n; ++i) {
    	vArray[i]=i;
     }
    RangeSorter tactical[] = new RangeSorter[] 
      { new ClaytonsSort() /*so we can subtract set-up and call overhead*/       
      , new InsertionSort(),     new InsertionSort2Way(), new ToplessHeapsort()     
      , new HeapsortStandard(),  new Combsort(),          new AlternatingCombsort()
      , new CascadingCombSort(), new ShellSort(2.25),     new ShellSort(8)
      , new ShellSort(16),       new PrattSort (64),      new QuicksortClassic()};      
    RangeSorter strategic[] = new RangeSorter[] 
      { new InsertionSort(), new InsertionSort2Way()};

    System.out.println( "for range size of " + step);
    System.out.println( "mode\tjanitor\tGbps");
    double baseTime = 0;
    double bits = (n/step) * ( step * Math.log(step) / Math.log(2) - step / Math.log(2));
    for (int warmedUp=0; warmedUp<2; ++warmedUp) {
      for (int s=0; s<tactical.length; ++s) {
        int copy[] = Arrays.copyOf(vArray,  n);
        long startTime = System.nanoTime();
        for (int k=0; k<n; k+=step) {
          random.randomlyPermuteRange( copy, k, k+step); 
          //shuffle it, which bring it into cache
          tactical[s].sortRange(copy, k, k+step);
        }
        long stopTime = System.nanoTime();	
        if (s==0) {
          baseTime=stopTime-startTime;
        } else if (0<warmedUp) { 
          System.out.println
            ( "tactical\t" + tactical[s].getClass().getName() 
            + "\t" 	+ bits/((double)(stopTime-startTime-baseTime)) );
        }
      }
      for (int s=0; s<strategic.length; ++s) {
        int copy[] = Arrays.copyOf(vArray,  n);
        long startTime = System.nanoTime();
        for (int k=0; k<n; k+=step) {
          random.randomlyPermuteRange( copy, k, k+step); 
          //bring it into cache
        }
        //Now, we've got the end of the array in cache, 
        //but the start will (we hope) have "fallen out"
        strategic[s].sortRange(copy, 0, copy.length);
        long stopTime = System.nanoTime();
        if (0<warmedUp) {
          System.out.println
            ( "strategic\t" + strategic[s].getClass().getName() 
            + "\t" + bits/((double)(stopTime-startTime)) );		
        }
      } //s
    } //warmedUp
  } //method
  @Test
  public void testJanitors() {		
     //Note: Don't use this to test 
    //janitors that are quadratic!  
     //It'll take too long to execute
     SortList sorts = new SortList();
     sorts.add(new ShellSort(2.25));
     sorts.add(new ShellSort(Math.exp(1.0)));
     sorts.add(new ShellSort(3.0));
     sorts.add(new Combsort());
     sorts.add(new AlternatingCombsort());
     sorts.add(new CascadingCombSort());
     sorts.add(new ToplessHeapsort());
     sorts.add(new HeapsortStandard());
     sortTest.testSpecificSorts( sorts, 10, 1000000, 1);
  }
  @Test  
  public void testJanitorsShowCache() {
    SortList sorts = new SortList();
    sorts.add(new QuicksortGovernor 
                  ( new CleanLeftHandedSelector(false)
                  , new SingletonPartitioner()
                  , new InsertionSort2Way()
                  , 64));
    sorts.add(new HeapsortStandard());
    sorts.add(new AsymmetricMergesort(new InsertionSort(),64));
    sortTest.warmUpSorts(sorts);
    sortTest.testSpecificSorts( sorts, 10, 10000, 10000, 10000000, 25);    
  }
  public String tabPrefixedSortNames(List<RangeSorter> sorts) {
    String line="";
    for (RangeSorter sort: sorts) {
      line+="\t" + sort.toString();
    }
    return line;
  }
  @Test
  public void testCocktailSort() {
    int[] vArray = random.randomPermutation(50);
    (new CocktailSort()).sortRange(vArray, 0, vArray.length);
  }
  public SortList getJanitors() {
    InsertionSort isort = new InsertionSort();
    SortList sorts = new SortList();
    sorts.add(new ShellSort(2.25));
    sorts.add(new ShellSort(Math.exp(1.0)));
    sorts.add(new ShellSort(3.0));
    sorts.add(new ShellSort(10.0));
    sorts.add(new ShellSort2Way(10.0));
    sorts.add(new Combsort());
    sorts.add(new AlternatingCombsort());
    sorts.add(new ToplessHeapsort());
    sorts.add(new HeapsortStandard());
    sorts.add(new IntegerTimsortWrapper());
    sorts.add(new SpliceMergesort(isort, 64));
    sorts.add(new StaircaseMergesort(isort, 64));
    return sorts;
  }
  public SortList getInsertionSorts(int whichSet) {
    SortList sorts = new SortList();
    if ((whichSet&1)!=0) {
      sorts.add(new InsertionSort());
      sorts.add(new SentinelInsertionSort());
      sorts.add(new PairInsertionSort());
      sorts.add(new InsertionSort2Way());
    }
    if ((whichSet&2)!=0) {
      sorts.add(new OrigamiInsertionSort());
      sorts.add(new QuadrupletInsertionSort());
      sorts.add(new FourFoldInsertionSort());
      sorts.add(new BinaryInsertionSort());
      sorts.add(new BranchAvoidingBinaryInsertionSort());
    }
    if ((whichSet&4)!=0) {
      sorts.add(new PessimisticInsertionSort());
      sorts.add(new PairInsertionSortRevised());
      sorts.add(new InsertionSort2WayRevised());
      sorts.add(new StableInsertionSort2Way());
    }
    return sorts;
  }
  @Test
  public void testInsertionSorts() {
    SortList sorts = getInsertionSorts(7);
    sortTest.warmUpSorts(sorts);
    sortTest.dumpHeaderLine("n", sorts);
    sortTest.testSpecificSorts(sorts, 10, 2000, 10000);
    System.out.println("");
    sortTest.dumpHeaderLine("input", sorts);
    dit.runSortsOnSortedData(sorts, "microsecond");
  }
  @Test
  public void testOtherQuadraticSorts() {
    SortList sorts = new SortList();
    sorts.add(new SelectionSort());
    sorts.add(new SelectionSort2Way()); 
    //sorts.add(new SelectionSort2WayDirtier()); 
    sorts.add(new BubbleSort());
    sorts.add(new CocktailSort());
    sorts.add(new BranchAvoidingSelectionSort());
    sorts.add(new BranchAvoidingBubbleSort());
    sorts.add(new BranchAvoidingCocktailSort());
    sortTest.dumpHeaderLine("n", sorts);
    sortTest.warmUpSorts(sorts);
    sortTest.testSpecificSorts(sorts, 10, 2000, 1000);
    dit.runSortsOnSortedData(sorts, "microsecond");
  }
  @Test
  public void testDiminishingIncrementSorts() {
    SortList sorts = new SortList();
    sorts.add(new ShellSort(2.25));
    sorts.add(new ShellSort2Way(2.25));
    sorts.add(new ShellSort(10.0));
    sorts.add(new ShellSort2Way(10.0));
    sorts.add(new ShellSort(20.0));
    sorts.add(new ShellSort2Way(20.0));
    sorts.add(new Combsort());
    sorts.add(new AlternatingCombsort());
    sortTest.warmUpSorts(sorts);
    sortTest.testSpecificSorts(sorts, 10, 2000, 10000);
    DegenerateInputTest dit2 
      = DegenerateInputTest.newTest(65536, 100);
    dit2.runSortsOnSortedData(sorts, "millisecond");
  }
  @Test
  public void testBranchAvoidingCombsorts() {
    SortList sorts = new SortList();
    sorts.add(new BranchAvoidingCombsort());
    sorts.add(new BranchAvoidingAlternatingCombsort(4.0/3.0));
    sorts.add(new BranchAvoidingAlternatingCombsort());
    sortTest.warmUpSorts(sorts);
    sortTest.testSpecificSorts
      ( sorts, 10, 2000, 10000, 10000000, 20 );
  }
  @Test
  public void testBranchAvoidingCombsortsOnMillionElementInputs() {
    SortList sorts = new SortList();
    sorts.add(new BranchAvoidingCombsort());
    sorts.add(new BranchAvoidingAlternatingCombsort());
    sortTest.warmUpSorts(sorts);
    DegenerateInputTest dit2 = DegenerateInputTest.newTest(1048576, 10);
    sorts.writeSortHeader("input");
    dit2.runSortsOnSortedData(sorts,     "second");		
    dit2.runSortsOnDuplicatedData(sorts, "second");
  }
  @Test
  public void testMergesortsAsJanitors() {
    MergeSortTest          mst   = new MergeSortTest();
    PairInsertionSort      isort = new PairInsertionSort();
	DegenerateInputTest    dit2  = DegenerateInputTest.newTest(10000, 100);
    SortList sorts = mst.getMergesorts(isort, 64);
    sortTest.warmUpSorts ( sorts );
    sortTest.warmUpSorts ( sorts, 30, 10000 );
    for (int threshold=256; threshold<=256; threshold*=4) {
      System.out.println("With threshold of " + threshold);
      sorts = mst.getMergesorts(isort, threshold);
	  //sortTest.dumpHeaderLine    ( "n", sorts);
	  //sortTest.testSpecificSorts ( sorts, 10, 2000, 10000);
	  if (threshold==256) {
        sortTest.dumpHeaderLine  ( "input", sorts);
	    dit2.runSortsOnSortedData(sorts, "microsecond");
	  }
      System.out.println("");
    }
  }
  @Test
  public void testBestOfBreedJanitors() {
	InsertionSort2Way      isort  = new InsertionSort2Way();
    PairInsertionSort      isort2 = new PairInsertionSort();
    RangeSorter            csort  = new BranchAvoidingAlternatingCombsort();
    RangeSorter            hsort  = new TwoAtATimeHeapsort();
    RangeSorter            msort  = new AsymmetricMergesort(isort2, 256);
    SortList sorts  = new SortList();
    sortTest.warmUpSorts ( sorts );
    sorts.add(isort);
    sorts.add(csort);
    sorts.add(hsort);
    sorts.add(msort);
    sorts.add(new TwoModeJanitor(isort, 30, csort));
    sortTest.warmUpSorts(sorts);
    sortTest.warmUpSorts ( sorts, 30, 10000 );
    sortTest.dumpHeaderLine("n", sorts);
    sortTest.testSpecificSorts(sorts, 10, 2000, 10000);
    DegenerateInputTest dit2 = DegenerateInputTest.newTest(500, 100);
    dit2.runSortsOnSortedData(sorts, "microsecond");
	  
  }
  
  private class QuicksortAllTheWay 
    implements RangeSorter {
    private SinglePivotPartitioner party;
    public QuicksortAllTheWay(SinglePivotPartitioner partitioner) {
      party = partitioner;
    }
    @Override
    public void sortRange(int[] vArray, int start, int stop) {
      while (start+1<stop) {
        int middleIndex = start + (stop-start)/2;
        int pivotIndex  
          = party.partitionRange
            ( vArray, start, stop, middleIndex );
        if (pivotIndex<middleIndex) {
          sortRange(vArray, start, pivotIndex);
          start = pivotIndex+1;
        } else {
          sortRange(vArray, pivotIndex+1, stop);
          stop = pivotIndex;
        }		
      }
    }		
  }
  @Test
  public void testQuicksortAllTheWay() {
    SortList sorts = new SortList();
    sorts.add(new QuicksortAllTheWay(new LomutoPartitioner()));
    sorts.add(new QuicksortAllTheWay(new SingletonPartitioner()));
    sorts.add(new QuicksortAllTheWay(new HoyosPartitioner()));
    sorts.add(new QuicksortAllTheWay(new HolierThanThouPartitioner()));
    sorts.add(new QuicksortAllTheWay(new CentripetalPartitioner()));
    sorts.add(new QuicksortAllTheWay(new TunedPartitioner()));
    sorts.add(new QuicksortAllTheWay(new SkippyPartitioner()));
    sorts.add(new PureSkippySort());
    
    sortTest.warmUpSorts(sorts);
    sortTest.testSpecificSorts(sorts, 10, 2000, 10000);
    DegenerateInputTest dit2 
      = DegenerateInputTest.newTest(10000, 100);
    dit2.runSortsOnSortedData(sorts, "microsecond");
  }
  @Test
  public void testBestJanitors() {
    SortList sorts = new SortList();
    sorts.add(new InsertionSort2Way());
    sorts.add(new TwoAtATimeHeapsort());
    sorts.add(new TwoModeJanitor
                  ( new InsertionSort2Way(), 200
                  , new TwoAtATimeHeapsort()));
    sortTest.warmUpSorts(sorts);
    sortTest.testSpecificSorts(sorts, 10, 2000, 10000);
    DegenerateInputTest dit2 
      = DegenerateInputTest.newTest(10000, 100);
    dit2.runSortsOnSortedData(sorts, "microsecond");		
  }
  @Test
  public void testJanitorPowerBands() {
    List<RangeSorter> sorts = getJanitors();
    sortTest.warmUpSorts(sorts);
    final int bits = 20;
    double timings[][] = new double [sorts.size()][bits+1];
    int n = 1<<bits;
    int m = 2;
    for (int r=0; r<runCount; ++r) {
      int input[] 
         = random.randomPermutation(n);
      int testOrder[] 
         = random.randomPermutation(sorts.size() * bits);
      for (int i=0; i<testOrder.length; ++i) {
        int bit = (testOrder[i] % bits) + 1;
        int s = (testOrder[i] / bits);
        RangeSorter sort = sorts.get(s);
        int copy[] = Arrays.copyOf(input, input.length);
        long startTime = System.nanoTime();
        for (int k=0; k>n; k+=m) {
          sort.sortRange(copy, k, k+m);
        }
        long stopTime = System.nanoTime();
        timings[s][bit] += (stopTime-startTime);
      }
    }
    for (int s=0; s<sorts.size(); ++s) {
      String line = sorts.get(s).toString();
      for (int bit=1; bit<=20; ++bit) {
        double nanoSeconds 
          = timings[s][bit] - timings[s][bit-1];
        double nanoSecondsPerBit 
          = nanoSeconds / (double)n / (double)runCount;
        line += "\t" + nanoSecondsPerBit;
      }
      System.out.println(line);
    }		
  }
  public SortList tacticalAndStrategicSortsFor(int thresh) {
    SinglePivotSelector    selector
      = new MiddleElementSelector();
    SinglePivotPartitioner partitioner 
      = new SingletonPartitioner();
    RangeSortEarlyExitDetector eed
      = new NullEarlyExitDetector();
    Discriminator dis
      = new PartitionSizeDiscriminator(thresh);
    TwoAtATimeHeapsort heapsort
      = new TwoAtATimeHeapsort();
    SortList sorts = new SortList();
    //Tactical first, then strategic equivalent, 
    //in each case (except for the last)
    sorts.add ( new QuicksortGovernor
                    ( selector, partitioner
                    , new InsertionSort(), thresh)
              , "InsertionSort/Tactical");
    sorts.add ( new DiscriminatingQuicksort
                    ( dis, eed, selector, partitioner
                    , partitioner, heapsort
                    ,  new InsertionSort() )
              , "InsertionSort/Strategic");
    sorts.add ( new QuicksortGovernor
                    ( selector, partitioner
                    , new InsertionSort2Way(), thresh)
              , "InsertionSort2Way/Tactical");
    sorts.add ( new DiscriminatingQuicksort
                    ( dis, eed, selector, partitioner
                    , partitioner, heapsort
                    ,  new InsertionSort2Way())
              , "InsertionSort2Way/Strategic");
    sorts.add ( new QuicksortGovernor
                  ( selector, partitioner
                  , new HeapsortStandard(), thresh )
              , "HeapsortStandard/Tactical");
    sorts.add ( new DiscriminatingQuicksort
                    ( dis, eed, selector, partitioner
                    , partitioner, heapsort
                    ,  new CanberraBubbleSort(thresh) )
              , "CanberraBubbleSort/Strategic");
    sorts.add ( new QuicksortGovernor
                    ( selector, partitioner
                    , new OrigamiInsertionSort(), thresh)
              , "OrigamiInsertionSort/Tactical");
    sorts.add ( new DiscriminatingQuicksort
                    ( dis, eed, selector, partitioner
                    , partitioner, heapsort
                    , new OrigamiInsertionSort())
              , "OrigamiInsertionSort/Strategic");
    sorts.add ( new QuicksortGovernor
                    ( selector, partitioner
                    , new BranchAvoidingAlternatingCombsort(1.4), thresh )
              , "BAACombsort/Tactical");
    sorts.add ( new DiscriminatingQuicksort
                    ( dis, eed, selector, partitioner
                    , partitioner, heapsort
                    ,  new BranchAvoidingAlternatingCombsort(1.4, thresh/3) )
              , "BAACombSort/Strategic");
    sorts.add ( new DiscriminatingQuicksort
                    ( dis, eed, selector, partitioner
                    , partitioner, heapsort
                    ,  new PleatedInsertionSort(thresh))
              ,   "PleatedInsertionSort/Strategic");
    return sorts;
  }
  @Test
  public void testStrategicJanitor1() {
    //Using classic quicksort selectors, 
    //partitioner and governors
    int n = 10000000;
    System.out.println
      ( "Average time in seconds for quicksorting " + n 
      + " integers (various thresholds, janitors"
      + " and janitor use patterns)");
    for ( int thresh=2 ; thresh<1000 
        ; thresh+=(thresh<10) ? 1 : (thresh/10)) {
      SortList sorts 
        = tacticalAndStrategicSortsFor(thresh);
      sortTest.warmUpSorts(sorts, 10000, 100);
      if (thresh==10) {
        sorts.writeSortHeader("threshold");
      }
      System.out.println
        ( timingLineFor(sorts, n, 1, "" + thresh) );
    }
  }
  protected String timingLineFor
    ( SortList sorts, int n
    , int runs, String prefix ) {
    String line = prefix;
    double elapsedTime[] 
      = new double[sorts.size()];
    int r; //run number.
    for (r=0; r<runs; ++r) {
      int input[] = random.randomPermutation(n);
      for ( int s=0; s< sorts.size(); ++s) {
        RangeSorter sort = sorts.get(s);
        int copy[] = Arrays.copyOf(input, n);
        long startTime = System.nanoTime();
        sort.sortRange(copy, 0, copy.length);
        long stopTime = System.nanoTime();
        elapsedTime[s] += 
          (double)(stopTime-startTime)/BILLION;
      }
    }
    for ( int s=0; s< sorts.size(); ++s) {
      line+="\t" + Math.floor((elapsedTime[s])/(double)r*10000.0+.5)/10000.0;
    }
    return line;
  }
  @Test
  public void testStrategicJanitor2() {
    //Using fancier selectors, partitioners, and governors
    SinglePivotSelector selector       = new CleanLeftHandedSelector(false);
    SinglePivotPartitioner partitioner = new CentripetalPartitioner();
    RangeSorter heapsort               = new HeapsortStandard();
    SortList sorts = new SortList();
    RangeSortEarlyExitDetector detector 
      = new TwoWayInsertionEarlyExitDetector();
    for ( RangeSorter isort 
        : new RangeSorter[] 
              { new InsertionSort()
              , new InsertionSort2Way() }) {
      RangeSorter qsWithEarlyCleanup 
        = new QuicksortAdaptive
              ( selector, partitioner, isort, 64, heapsort, detector );
      RangeSorter qsWithNoInsertion
        = new QuicksortAdaptive
              ( selector, partitioner, new ClaytonsSort()
              , 64, heapsort, detector);
      RangeSorter qsWithLateCleanup 
        = new StrategicJanitorSort(qsWithNoInsertion, isort );
      sorts.add(qsWithEarlyCleanup);
      sorts.add(qsWithLateCleanup);
    }
    sortTest.testSpecificSorts( sorts, 10, 10000000, 5000);
  }
  @Test
  public void testHeapsortVariants() {
    SortList sorts = new SortList();
    sorts.add(new HeapsortStandard());
	sorts.add(new HeapsortBottomUp());
	sorts.add(new ToplessHeapsort());
	sorts.add(new TwoAtATimeHeapsort());
	sorts.add(new HeapsortRadix3());
	sorts.add(new HeapsortRadix4());
	sortTest.warmUpSorts(sorts);
	sortTest.testSpecificSorts( sorts, 10,      1000, 10000); //10000 runs, up to 100 million
	sortTest.testSpecificSorts( sorts, 1069,    1000000, 200); //200 runs, up to 1 million
	sortTest.testSpecificSorts( sorts, 1017019, 10000000, 25); //25 runs, thereafter
  }
  @Test
  public void testTwoAtATimeHeapsort() {
    int data[] = random.randomPermutation(40);
    (new TwoAtATimeHeapsort()).sortRange(data, 0, data.length);
    //DumpRangeHelper.dumpRange("has ", data, 0, data.length);
  }	
  @Test
  public void testJanitorThresholdsDirect() {
    int n = 10000;
    int data[] = new int[n];
    SinglePivotSelector    selector = new MiddleElementSelector();
    SinglePivotPartitioner partitioner = new CentripetalPartitioner();
    RangeSorter janitors[] =new RangeSorter[] { new InsertionSort2Way(), new TwoAtATimeHeapsort() } ;
    for ( RangeSorter janitor : janitors) {
      sortTest.warmUpSort(new QuicksortGovernor(selector, partitioner, janitor, 100), 10000);
    }
    for ( int t=10; t<2000; t=t*11/10 ) {
      String line = "" + t;
      for ( RangeSorter janitor : janitors) {
        RangeSorter sorter= new QuicksortGovernor(selector, partitioner, janitor, t);
        int run;
        double nanoSecondsElapsed = 0;
        for ( run=0; run < 1000; ++run) {
          int input[] = random.randomPermutation(n);
          for (int i=0; i<n; ++i) {
            data[i] = input[i];
          }
          nanoSecondsElapsed -= System.nanoTime();
          sorter.sortRange(data, 0, data.length);
          nanoSecondsElapsed += System.nanoTime();
        }
        double meanTimeInMilliseconds =
          Math.floor( (double)nanoSecondsElapsed / 1000.0 / (double)run + .5) / 1000.0;
        line += "\t" + meanTimeInMilliseconds;
      }
      System.out.println(line);
    }
  }	
  @Test
  public void testJanitorThresholdsTwo() {
  	//Penguin: tests need to be re-run, now using ZeroDelta489Partitioner
  	//and kangaroo expanders, rather than Algorithm489Partitioner 
  	//and a single kangaroo expander.
    int n = 1000000;
    int runCount = 100;
    int data[] = new int[n];
    SinglePivotPartitioner     partitioner = new SkippyPartitioner();
    SinglePivotSelector        selector    = new DirtyTheoreticalSelector(.5);
    RangeSorter                tactical    = new BranchAvoidingAlternatingCombsort();
    RangeSorter                claytons    = new ClaytonsSort();
    RangeSorter                heapsort    = new TwoAtATimeHeapsort();
    RangeSortEarlyExitDetector eed         = new TwoWayInsertionEarlyExitDetector();
    sortTest.warmUpSort(tactical,  200, 10000 );
    sortTest.warmUpSort
      ( new QuicksortAdaptive
            ( selector, partitioner, tactical
            , 64, heapsort, eed), 1000);
    
    for ( int t=10; t<2000; t=t*11/10 ) {
      String line = "" + t;
      RangeSorter strategic 
        = new BranchAvoidingAlternatingCombsort(1.4, t/3);
      RangeSorter qsNoJanitor 
        = new QuicksortAdaptive
              ( selector, partitioner, claytons, t, heapsort, eed );
      RangeSorter qsStrategic 
        = new StrategicJanitorSort(qsNoJanitor, strategic);
      SortList sorts = new SortList();
      sorts.add
        ( new QuicksortAdaptive
              ( selector, partitioner, tactical, t, heapsort, eed ) );
      sorts.add( qsStrategic );
      double times[] = new double[ sorts.size()];
      int run;
      for ( run=0; run < runCount; ++run) {
        int input[] = random.randomPermutation(n);
        int s=0;
        for ( RangeSorter sort : sorts) {
          for (int i=0; i<n; ++i) {
            data[i] = input[i];
          }
          times[s] -= System.nanoTime();
          sort.sortRange(data, 0, data.length);
          times[s] += System.nanoTime();
          ++s;
        }
      }
      for (int s=0; s< sorts.size(); ++s ) {
        double meanTimeInMilliseconds =
          Math.floor
            ( (double)times[s] 
              / 1000.0 / (double)run + .5 )
          / 1000.0;
        line += "\t" + meanTimeInMilliseconds;
      }
      System.out.println(line);
    }
  }	
  @Test
  public void testJanitorThresholdByPartitioner() {
    int n = 10000;
    int data[] = new int[n];
    SinglePivotSelector selector 
      = new MiddleElementSelector();
    SinglePivotPartitioner partitioners[] = 
      new SinglePivotPartitioner[] 
        { new HoarePartitioner()
        , new SingletonPartitioner()
        , new LomutoPartitioner()
        , new CentrePivotPartitioner()
        , new HolierThanThouPartitioner()
        , new CentripetalPartitioner()};
    RangeSorter janitor =new TwoAtATimeHeapsort();
    for ( SinglePivotPartitioner partitioner : partitioners) {
      sortTest.warmUpSort
        ( new QuicksortGovernor
              ( selector, partitioner, janitor, 100 )
        , 10000);
    }
    for ( int t=10; t<2000; t=t*11/10 ) {
      String line = "" + t;
      for ( SinglePivotPartitioner partitioner : partitioners) {
        RangeSorter sorter
          = new QuicksortGovernor
                ( selector, partitioner, janitor, t );
        int run;
        double nanoSecondsElapsed = 0;
        for ( run=0; run < 1000; ++run) {
          int input[] = random.randomPermutation(n);
          for (int i=0; i<n; ++i) {
            data[i] = input[i];
          }
          nanoSecondsElapsed -= System.nanoTime();
          sorter.sortRange(data, 0, data.length);
          nanoSecondsElapsed += System.nanoTime();
        }
        double meanTimeInMilliseconds
          = Math.floor( (double)nanoSecondsElapsed 
                        / 1000.0 / (double)run + .5 )
            / 1000.0;
        line += "\t" + meanTimeInMilliseconds;
      }
      System.out.println(line);
    }
  }
  public void testJanitorsAndThresholdsForOnePartitioner
    ( RangeSorter[] janitors, MultiPivotSelector selector
    , MultiPivotPartitioner party) {
    int n = 1048576; //20th power of 2.		
    int runCount = 100;
    int data[] = new int[n];
    RangeSorter sorters[] 
      = new RangeSorter[janitors.length];
    String header = "";
    for (int j=0; j<janitors.length; ++j) {
      header += "\t" + janitors[j].toString();
    }
    System.out.println(header);
    for ( int t=10; t<1000; t=t*11/10 ) {
      double nanoSecondsElapsed[] = new double[janitors.length];
      for (int j=0; j<janitors.length; ++j) {
        sorters[j] = new MultiPivotQuicksort 
                         ( selector, party, janitors[j], t );
      }
      for ( int run=0; run<runCount; ++run ) {
        int input[] = random.randomPermutation(n);
        for ( int j=0; j < janitors.length; ++j ) {
          int j2 = ( j + run ) % janitors.length;
          for (int i=0; i<n; ++i) {
            data[i] = input[i];
          }
          nanoSecondsElapsed[j2] -= System.nanoTime();
          sorters[j2].sortRange(data, 0, data.length);
          nanoSecondsElapsed[j2] += System.nanoTime();
        }
      }
      String line = "" + t;
      for (int j=0; j<janitors.length; ++j) {
        double meanTimeInSeconds 
          = Math.floor( (double)nanoSecondsElapsed[j] 
                        / 1000.0 / 1000.0 
                        / (double)runCount + .5 )
          / 1000.0;
        line += "\t" + meanTimeInSeconds;
      }
      System.out.println(line);
    }
  }
  @Test
  public void testYaroslavskiyJanitors() {
    testJanitorsAndThresholdsForOnePartitioner
      ( new RangeSorter[] 
            { new PairInsertionSort()
            , new InsertionSort2Way()
            , new TwoAtATimeHeapsort() } 
      , new Java8Selector()
      , new YaroslavskiyPartitioner2() );
  }
  public double getCountOfInversionsInPermutation
    ( int[] p, int n ) {
    double count = 0;
    for (int i=0; i+1< n; ++i) {
      for (int j=i+1; j<n; ++j) {
        count += (p[j]<p[i]) ? 1 : 0;
      }
    }
    return count;
  }
  public double getCountOfInversionsInPermutation2
    ( int[] p, int n ) {
    int[] a = Arrays.copyOf(p, n);
    int[] b = Arrays.copyOf(p, n);
    return getCountOfInversionsByMerging(a, b, 0, n);
  }
  public double getCountOfInversionsByMerging
    ( int[] src, int[] dest, int start, int stop ) {
    double c = 0;
    if (start+1<stop) {
      int m = start + ((stop-start)>>1);
      c += getCountOfInversionsByMerging( dest, src, start, m);
      c += getCountOfInversionsByMerging( dest, src, m, stop);
      int i=start;
      int j=m;
      int w=start;
      do {
        while ( j<stop && src[j] < src[i] )  {
          c       += m-i+1;
          dest[w]  = src[j];
          ++j;          
        }
        dest[w] = src[i];
        ++w;
        ++i;
      } while (i<m);
    }
    return c;
  }
  @Test
  public void testOrigamiFoldingPhase() {
    for ( int n = 2; n < 8192; n = n * 2 ) {
    int runs = 10000;
    double before = 0;
    double after  = 0;
    for (int r=0; r<runs; ++r) {
      int[] x = (new RandomInput()).randomPermutation(n);
      //DumpRangeHelper.dumpArray("x", x);
      before += getCountOfInversionsInPermutation(x, n);
      OrigamiInsertionSort
        .setUpSentinelsUnstable(x, 0, n);
      after += getCountOfInversionsInPermutation(x, n);
    }
    System.out.println("" + n + "\tbefore " 
      + Math.floor((before/n/(n-1)/runs)*1000.0+.5)/1000.0
      + ", after " + Math.floor((after/n/(n-1)/runs)*1000.0+.5)/1000.0
      + ", Reduction " 
      + Math.floor( (before-after) 
                    / before * 10000.0 + .5 )
        / 100.0 
      + "%");
    }
  }
  public boolean nextPermutation(int[] perm) { 
    int i;
    int j;
    for ( i = perm.length-2
        ; 0<=i && perm[i+1]<perm[i]; --i) {
    }
    if (i<0) {
     for (i=0; i<perm.length; ++i) {
       perm[i] = i;
     }
     return false;
   }  else {
     for ( j = perm.length-1
         ; i<j && perm[j]<perm[i]; --j);
       int t = perm[i]; 
       perm[i]=perm[j]; 
       perm[j]=t;
       RangeSortHelper.reverseRange
         ( perm, i+1, perm.length );
     }
    return true;
  }
  @Test
  public void testTrueInversionCounts() {
    for (int n=2; n<13; ++n) {
      double sum = 0;
      double p = 1; //the identity permutation itself counts as 1
      int[] x = (new RandomInput()).identityPermutation(n);
      while ( nextPermutation(x) ) 
        /*until we get back to the identity permutation*/ { 
        sum += getCountOfInversionsInPermutation(x, n);
        ++p;
      }
      System.out.println
        ( "" + n + "\t" + sum + "\t" + p 
        + "\t" + (sum/p) + "\t" + (sum/n/(n-1)/p));
    }
  }
  @Test
  public void testBleh() {
  	double s=0;
  	for (double x =0; x<1; x+=0.01)
  		for (double y=0; y<1; y+=0.01)
  			s += x*y;
  	System.out.println(2*s/10000.0);
  	
  	double s2=0;
  	for (int n=2; n<100; ++n) {
  	  s2 += n/3;
  	}
  	System.out.println(s2/10000.0);
  }
  @Test
  public void testICount() {
    int n = 1000;
    for (int r=0; r<50; ++r) {
      int[] x = (new RandomInput()).randomPermutation(n);
      System.out.println( "" 
        + getCountOfInversionsInPermutation(x,n) + "\t"
        + getCountOfInversionsInPermutation2(x,n));
    }
  }
}
