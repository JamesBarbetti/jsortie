package jsortie.testing;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import jsortie.RangeSorter;
import jsortie.earlyexitdetector.RangeSortEarlyExitDetector;
import jsortie.earlyexitdetector.TwoWayInsertionEarlyExitDetector;
import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.janitors.exchanging.BranchAvoidingCombsort;
import jsortie.janitors.insertion.InsertionSort;
import jsortie.janitors.insertion.PairInsertionSort;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.mergesort.vanilla.MergesortBase;
import jsortie.quicksort.collector.NullSampleCollector;
import jsortie.quicksort.collector.external.ExternalPositionalCollector;
import jsortie.quicksort.collector.external.ExternalSampleCollector;
import jsortie.quicksort.expander.branchavoiding.LeftSkippyExpander;
import jsortie.quicksort.expander.branchavoiding.RightSkippyExpander;
import jsortie.quicksort.governor.adaptive.QuicksortAdaptive;
import jsortie.quicksort.multiway.partitioner.CompoundPartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.SingletonPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyPartitioner;
import jsortie.quicksort.partitioner.insideout.CentripetalPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.interfaces.StandAlonePartitioner;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.Algorithm489Partitioner;
import jsortie.quicksort.partitioner.lomuto.LomutoPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.clean.CleanLeftHandedSelector;
import jsortie.quicksort.selector.clean.CleanRemedianSelector;
import jsortie.quicksort.selector.clean.CleanTheoreticalSelector;
import jsortie.quicksort.selector.dirty.DirtySingletonSelector;
import jsortie.quicksort.selector.dirty.DirtyTheoreticalSelector;
import jsortie.quicksort.selector.dirty.DirtyTukeySelector;
import jsortie.quicksort.selector.simple.MiddleElementSelector;

public class JanitorThresholdTest {
  protected RandomInput random = new RandomInput();
  protected PartitionerTest tp = new PartitionerTest();
  
	public void figureOutThresholdForMerging()
	{
		int totalTime[] = new int[2];
		int firstSort = 0;
		int lastSort = totalTime.length-1;
		int latin = 0;
		InsertionSort isort = new InsertionSort();
		MergesortBase base = new MergesortBase(isort, 64);
		
		for (int n=10; n<1000; n=n*11/10)
		{
			for (int i=firstSort; i<=lastSort; ++i)
				totalTime[i]=0;
	
			for (int run=0; run<SortTest.runCount(n); ++run)
			{
				int input[] = random.randomPermutation(n);				
				int copy[] = new int[n];
				for (int s = 0; s <= lastSort-firstSort; ++ s)
				{
					int sort = ((s + latin) % (lastSort-firstSort+1)) + firstSort;
					for (int i=0; i<n; ++i)
					{
						copy[i] = input[i];
					}
					int work [] = new int[n];
					long start = System.nanoTime();
					switch (sort)
					{
						case 0:
							isort.sortRange(copy, 0, copy.length);
							break;
						case 1:
							int left = n / 2;
							InsertionSort.copyAndSortSmallRange(copy,  0, left, work, 0);
							InsertionSort.sortSmallRange(copy, left, n);
							base.mergeToLeft(work, 0, left, copy, left, n, 0);
							break;
					}
					totalTime[sort] +=  System.nanoTime() - start; 
				}
				--latin;
				if (latin<0) latin+=lastSort-firstSort+1;
			
		}
			String s = Integer.toString(n);
			for (int i=firstSort; i<=lastSort; ++i)
				s+= "\t" + ((double)totalTime[i]/SortTest.runCount(n))/SortTest.billion;				
			System.out.println ( s);
		}			
	}

  public void testJanitorsAndThresholdsForOnePartitioner( RangeSorter[] janitors
	      , SinglePivotSelector selector, SinglePivotPartitioner party) {
	    int         n         = 1048576; //20th power of 2.		
	    int         runCount  = 20;
	    int         data[]    = new int[n];
	    RangeSorter sorters[] = new RangeSorter[janitors.length];
		RangeSortEarlyExitDetector detector = new TwoWayInsertionEarlyExitDetector();

	    String header = "";
	    for (int j=0; j<janitors.length; ++j) {
	      header += "\t" + janitors[j].toString();
	    }
	    System.out.println(header);

	    for ( int t=5; t<1000; t=(t<10)?(t+1):(t*11/10) ) {
	      double nanoSecondsElapsed[] = new double[janitors.length];
	      for (int j=0; j<janitors.length; ++j) {
	        sorters[j] = new QuicksortAdaptive ( selector, party, janitors[j], t, new TwoAtATimeHeapsort(), detector );
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
	        double meanTimeInSeconds =
				Math.floor( (double)nanoSecondsElapsed[j] / 1000.0 / 1000.0 / (double)runCount + .5) / 1000.0;
		    line += "\t" + meanTimeInSeconds;
	      }
	      System.out.println(line);
	    }
	  }
			
  public void testKangarooJanitorThresholds() {
	ArrayList<SinglePivotPartitioner> partitioners = new ArrayList<SinglePivotPartitioner>();
    tp.warmUpPartitioners(partitioners);
    testJanitorsAndThresholdsForOnePartitioner
	  ( new RangeSorter[] { new PairInsertionSort(), new InsertionSort2Way()
				            , new TwoAtATimeHeapsort() } 
	  , new CleanRemedianSelector(true)
	  , new SkippyPartitioner() );
  }	
  public void graphMarginalEfficiencyCurves 
    ( StandAlonePartitioner p, RangeSorter[] janitors
    , int nLow, int nHigh, int nStep, int runCount ) {
  	SortTest st = new SortTest();
    tp.warmUpPartitioner(p);
    for (RangeSorter jan : janitors) st.warmUpSort(jan, 200, 5000);
    for (int n=nLow; n<nHigh; n+=nStep) {
      double   pTime = 0; //nanoseconds
      double[] jTime = new double[janitors.length];     //net janitor times
      for (int r=0; r<runCount; ++r) {
        int[] input = random.randomPermutation(n);
        int[] copy;
        
        for (int j=0; j<janitors.length; ++j) {
          copy = Arrays.copyOf(input, n);
          jTime[j] -= System.nanoTime(); 
          janitors[j].sortRange(copy, 0, n);
          jTime[j] += System.nanoTime();
        }
        
        pTime -= System.nanoTime();
        int[] boundaries = p.multiPartitionRange ( input, 0, n );
        pTime += System.nanoTime();
        for (int j=0; j<janitors.length; ++j) {
          copy = Arrays.copyOf(input, n);
          jTime[j] += System.nanoTime();
          for (int s=0; s<boundaries.length; s+=2) {
            janitors[j].sortRange(copy, boundaries[s], boundaries[s+1]);
          }
          jTime[j] -= System.nanoTime();
        }
      }
      String line = "" + n + "\t" + Math.floor((pTime/n/runCount*1000.0))/1000.0;
      for (int j=0; j<janitors.length; ++j) {
        line += "\t" + Math.floor((jTime[j]/n/runCount*1000.0))/1000.0;
      }
      System.out.println(line);
    }
  }
  public void graphMarginalEfficiencyCurves 
    ( StandAlonePartitioner[] partitioners
    , int nLow, int nHigh, double growthRatio, int nStep
    , int runCount, boolean includeHeader ) {
    //Note: Have to output Gbps, because different StandalonePartitioner
    //      might gain different amounts of information.
    String header = "n";
    for (StandAlonePartitioner p: partitioners) {
      tp.warmUpPartitioner(p);
      header += "\t" + p.toString();
    }
    if (includeHeader) {
      System.out.println(header);
    }
    for (int n=nLow; n<nHigh; n=(int)Math.floor(n*growthRatio) + nStep) {
      double[] pTime = new double[partitioners.length]; //nanoseconds
      double[] pBits = new double[partitioners.length]; //bits
      for (int r=0; r<runCount; ++r) {
        int[] input = random.randomPermutation(n);
        double bitsBefore = SortTest.bitsLeftInPartitions( new int[] { 0, n });
        for (int p=0; p<partitioners.length; ++p) {
          pBits[p] += bitsBefore;
          int[] copy = Arrays.copyOf(input, n);
          StandAlonePartitioner party = partitioners[p];
          pTime[p] -= System.nanoTime();
          int[] boundaries = party.multiPartitionRange ( copy, 0, n );
          pTime[p] += System.nanoTime();
          pBits[p] -= SortTest.bitsLeftInPartitions(boundaries);
        }
      }
      String line = "" + n;
      for (int p=0; p<partitioners.length; ++p) {
        line += "\t" + Math.floor((pBits[p]/pTime[p]*1000.0))/1000.0;
      }
      System.out.println(line);
    }
  }
  protected double bitsRequired(int n) {
    if (n<2) return 0;
    return (Math.log(n)-1) * (double)n / Math.log(2);
  }
  @Test
  public void testMarginalEfficiencyCurves() {
    graphMarginalEfficiencyCurves( 
      new CompoundPartitioner
          ( new MiddleElementSelector()
          , new CentripetalPartitioner())
      , new RangeSorter[] 
        { new InsertionSort2Way()
        , new TwoAtATimeHeapsort()
        , new BranchAvoidingCombsort()
        }, 10, 250, 1, 100000 );
  }
  @Test
  public void testMarginalEfficiencyCurves2() {
    ExternalSampleCollector pos 
      = new ExternalPositionalCollector();
    Algorithm489Partitioner a489 
      = new Algorithm489Partitioner
            ( new NullSampleCollector()
            , new LeftSkippyExpander()
            , new RightSkippyExpander());
    graphMarginalEfficiencyCurves( 
      new CompoundPartitioner
          ( new CleanTheoreticalSelector(.5, pos, a489)
          , new SkippyPartitioner())
      , new RangeSorter[] 
        { new TwoAtATimeHeapsort()
        , new BranchAvoidingCombsort()
        }, 20, 2500, 20, 100000 );
  }
  @Test
  public void testMarginalEfficiencyCurves2A() {
    graphMarginalEfficiencyCurves( 
      new CompoundPartitioner
          ( new CleanTheoreticalSelector(.5)
          , new SkippyPartitioner())
      , new RangeSorter[] 
        { new TwoAtATimeHeapsort()
        , new BranchAvoidingCombsort()
        }, 20, 2500, 20, 100000 );
  }
  @Test
  public void testMarginalEfficiencyCurves3() {
    testCurvesForPartitioners
    ( getReferencePartitioners
      (new SingletonPartitioner(), true), 3);
    testCurvesForPartitioners
    ( getReferencePartitioners
      (new LomutoPartitioner(), true), 3);
  }
  @Test
  public void testMarginalEfficiencyCurves4() {
    testCurvesForPartitioners
      ( getReferencePartitioners
        (new SkippyPartitioner(), false), 10);
  }
  public void testCurvesForPartitioners(StandAlonePartitioner[] partitioners,int k) {
    graphMarginalEfficiencyCurves
    ( partitioners, 100, 20000, 1.1, 0, k*10000 , true ); //1000000, 1.1, 0, 1000 );
    graphMarginalEfficiencyCurves
    (  partitioners, 20132, 20000000, 1.1, 0, k*100 , false ); //1000000, 1.1, 0, 1000 );
  }
  public StandAlonePartitioner[] getReferencePartitioners(SinglePivotPartitioner basePartitioner, boolean asym) {
    SinglePivotSelector fourthSelector;
    SinglePivotSelector fifthSelector;
    SinglePivotSelector sixthSelector;
    if (asym) {
      fourthSelector = new CleanLeftHandedSelector(true);
      fifthSelector  = new CleanTheoreticalSelector(.1);
      sixthSelector  = new DirtyTheoreticalSelector(.1);
    } else {
      fourthSelector = new CleanRemedianSelector(true);
      fifthSelector  = new CleanTheoreticalSelector(.5);
      sixthSelector  = new DirtyTheoreticalSelector(.5);
    }
    return new StandAlonePartitioner[] 
        { new CompoundPartitioner
              ( new MiddleElementSelector()
              , basePartitioner)
        , new CompoundPartitioner
              ( new DirtySingletonSelector()
              , basePartitioner)
        , new CompoundPartitioner
              ( new DirtyTukeySelector()
              , basePartitioner)
        , new CompoundPartitioner
              ( fourthSelector
              , basePartitioner)
        , new CompoundPartitioner
              ( fifthSelector
              , basePartitioner)
        , new CompoundPartitioner
              ( sixthSelector
              , basePartitioner)
        };
  }
}
