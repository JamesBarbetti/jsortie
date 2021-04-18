package jsortie.testing;

import org.junit.Test;

import jsortie.RangeSorter;
import jsortie.StableRangeSorter;
import jsortie.earlyexitdetector.RangeSortEarlyExitDetector;
import jsortie.earlyexitdetector.TwoWayInsertionEarlyExitDetector;
import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.janitors.TwoModeJanitor;
import jsortie.janitors.exchanging.AlternatingCombsort;
import jsortie.janitors.exchanging.BranchAvoidingAlternatingCombsort;
import jsortie.janitors.exchanging.Combsort;
import jsortie.janitors.insertion.InsertionSort;
import jsortie.janitors.insertion.ShellSort;
import jsortie.mergesort.asymmetric.AsymmetricMergesort;
import jsortie.quicksort.governor.adaptive.QuicksortAdaptive;
import jsortie.quicksort.partitioner.bidirectional.traditional.SingletonPartitioner;
import jsortie.quicksort.partitioner.insideout.CentripetalPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.lomuto.LomutoPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.clean.CleanLeftHandedSelector;
import jsortie.quicksort.selector.simple.MiddleElementSelector;
import jsortie.quicksort.theoretical.Gain;

public class SinglePivotTest {
  protected SortTest sortTest = new SortTest();  
  @Test
  //3,5,7,9,11th of 14  : but 5/14 isn't 1/3
  public void testJava8SelectorFix() {
    for (int count=5; count<300; ++count) {
    	int s1 = (count>>3) + (count>>6) + 1;
    	int s2 = ((count << 3) + count + 55) >> 6;
    	System.out.println(count + "\t" + (count/2) + "\t" + s1 + "\t" + s2
    		+ "\t" + ((count/2)-2*s1) + "\t" + ((count/2)+2*s1)
    		+ "\t" + ((count/2)-2*s2) + "\t" + ((count/2)+2*s2));
    	//System.out.println((s1/(double)count) + "\t" + (s2/(double)count));
    }
  }
  
  
  public void testAnalyzeSQ(int sample, int pick, boolean lomuto) {
    double a[] = new double[sample];
    double tSwaps = 0;
    double compares = 0;
    double tCompares = 0;
    double pTotal = 0;
    double tGain = 0;
    double gain;

    int run;
    for (run=0; run<1000; ++run) {
      for (int k=0; k<sample; ++k) {
        a[k] = Math.random();
      }
      java.util.Arrays.sort(a);

      double p = a[pick];
      pTotal += p;
        
      compares   = 1;
      gain       = Gain.gainForInterval(p) + Gain.gainForInterval(1-p);
      tCompares += compares;
      tSwaps    += lomuto ? p : 2*p*(1-p);
      tGain     += gain;			
    }
    double efficiency = tGain/tCompares;
    double moveRatio  = tSwaps/tCompares;
		
    System.out.println("" + sample + "\t" + pick + "\t" + (lomuto?"y":"n") 
      + "\t" + tSwaps/run + "\t" + tCompares/run + "\t" + efficiency
      + "\t" + moveRatio/efficiency/Math.log(2) + "\t" + 1/efficiency/Math.log(2) + "\t" 
      + Math.floor(pTotal/run*10000)/100 );		
  }
  void findFraction(double r1, double r2) {
    for (int x=1; x<100; ++x) {
	  for (int y=1; y<100; ++y) {
        double t = (double)x / (double)y;
        if ( r1 < t && t < r2) {
          System.out.println("" + x + "/" + y + "=" + t );
        }
      }
    }
  }
  public void testSinglePivotSelectors() {
    findFraction(0.7016, 0.7019);
    System.out.println("sample\tpick\tlomu\tswaps           \tcompares         \tgain                \tTswaps/nlnn        \tTcompares/nlnn");
    testAnalyzeSQ(1,0,false);
    testAnalyzeSQ(3,1,false);
    testAnalyzeSQ(5,2,false);
    testAnalyzeSQ(25,12,false);
    testAnalyzeSQ(125,62,false);
    testAnalyzeSQ(625,312,false);
    testAnalyzeSQ(3025,1512,false);
    testAnalyzeSQ(1,0,true);
    testAnalyzeSQ(3,1,true);
    System.out.println("");
  }	
  public void testTenPercentSelector() {
    SortList sorts  = new SortList ();	
    SinglePivotSelector lhs       = new CleanLeftHandedSelector(true);
    SinglePivotSelector middle    = new MiddleElementSelector();
    StableRangeSorter   isort     = new InsertionSort();
    RangeSorter         combsort  = new BranchAvoidingAlternatingCombsort();
    RangeSorter         heapy     = new TwoAtATimeHeapsort();
    RangeSorter         janitor   = new TwoModeJanitor(isort, 30, combsort);
    SinglePivotPartitioner singly = new SingletonPartitioner();
    SinglePivotPartitioner centri = new CentripetalPartitioner();
    SinglePivotPartitioner lomuto = new LomutoPartitioner();
    RangeSortEarlyExitDetector detector = new TwoWayInsertionEarlyExitDetector();
    sorts.clear();
    sorts.add( new QuicksortAdaptive(lhs, singly, janitor, 400, heapy, detector));
    sorts.add( new QuicksortAdaptive(lhs, centri, janitor, 400, heapy, detector));
    sorts.add( new QuicksortAdaptive(lhs, lomuto, janitor, 400, heapy, detector));
    tryEm(sorts);
    
    sorts.clear();
    sorts.add( new QuicksortAdaptive(middle, singly, janitor, 400, heapy, detector));
    sorts.add( new QuicksortAdaptive(middle, centri, janitor, 400, heapy, detector));
    sorts.add( new QuicksortAdaptive(middle, lomuto, janitor, 400, heapy, detector));
    tryEm(sorts);
    
    sorts.clear();
    sorts.add( new AlternatingCombsort());
    sorts.add( new Combsort());
    sorts.add( new ShellSort(2.25));
    tryEm(sorts);

    sorts.clear();
    sorts.add( new QuicksortAdaptive(middle, centri, janitor, 400, heapy, detector));
    sorts.add( heapy );
    sorts.add( new AsymmetricMergesort(isort, 64) );
    tryEm(sorts);		
  }

  private void tryEm(SortList sorts) {		
    String line = "";
    for (RangeSorter s: sorts) { 
      line+= "\t" + s.toString(); 
    }
    DegenerateInputTest dit = DegenerateInputTest.newTest(1000000, 25);
    System.out.println(line);
    sortTest.warmUpSorts(sorts);
    dit.runSortsOnSortedData(sorts, "second");
    dit.runSortsOnDuplicatedData(sorts, "second");
    System.out.println("");
  }
}
