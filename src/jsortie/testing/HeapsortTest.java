package jsortie.testing;

import java.util.Arrays;

import org.junit.Test;

import jsortie.heapsort.BranchAvoidingHeapsort;
import jsortie.heapsort.HeapsortBase;
import jsortie.heapsort.bottomup.HeapsortBottomUp;
import jsortie.heapsort.instrumented.ComparisonCountingHeapsort;
import jsortie.heapsort.instrumented.ComparisonCountingHeapsortBase;
import jsortie.heapsort.instrumented.ComparisonCountingHeapsortRadix3;
import jsortie.heapsort.instrumented.ComparisonCountingHeapsortRadix4;
import jsortie.heapsort.instrumented.ComparisonCountingHeapsortTopDown;
import jsortie.heapsort.retro.HeapsortOriginal;
import jsortie.heapsort.retro.HeapsortRecursive;
import jsortie.heapsort.topdown.HeapsortRadix4;
import jsortie.heapsort.topdown.HeapsortStandard;
import jsortie.heapsort.topdown.ToplessHeapsort;
import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.heapsort.zoned.ZonedHeapsort;

public class HeapsortTest 
  extends JanitorTest {
  @Test
  public void testHeapSorts() {
    SortList sorts = new SortList();
    sorts.add(new HeapsortStandard());
    sorts.add(new HeapsortBottomUp());
    sorts.add(new ToplessHeapsort());
    sorts.add(new TwoAtATimeHeapsort());
    sorts.add(new HeapsortOriginal());
    sorts.add(new BranchAvoidingHeapsort());
    //sortTest.warmUpSorts(sorts);
    sortTest.testSpecificSorts(sorts, 10, 2000, 10000);
    DegenerateInputTest dit2 
      = DegenerateInputTest.newTest(10000, 100);
    dit2.runSortsOnSortedData(sorts, "millisecond");
  }
  @Test 
  public void testZonedHeapsort() {
    SortList sorts = new SortList();
    sorts.add(new HeapsortStandard());
    sorts.add(new TwoAtATimeHeapsort());
    sorts.add(new HeapsortRadix4());
    String header="n\th2\th2*\th4";
    for (int k : new int [] 
      { 4, 6, 14, 30, 62, 126, 254, 510 } ) {
      sorts.add(new ZonedHeapsort(k));
      header += "\t" + k;
    }
    System.out.println(header);
    sortTest.warmUpSorts(sorts);
    sortTest.testSpecificSorts(sorts, 10, 1292, 10000);
    sortTest.testSpecificSorts(sorts, 1292, 10488, 1600);
    sortTest.testSpecificSorts(sorts, 10488, 10000000, 25);
  }  
  @Test 
  public void testAsymmetry() {
    System.out.println("theta\tb\ta\ta2\tb\ta\ta2");
    for (int x=0; x<100; ++x) {
      double theta = x / 100.0;
      String line = "" + theta;
      
      //d=2
      int n = 1048575 + (int)Math.floor(1048576 * theta);
      ComparisonCountingHeapsort cch 
        = new ComparisonCountingHeapsort();
      line += heapsortLine(cch, n);
      
      
      //d=3
      n = 265720 + (int)Math.floor(531441 * theta);
      ComparisonCountingHeapsortRadix3 cch3 = 
        new ComparisonCountingHeapsortRadix3();
      line += heapsortLine(cch3, n);
      
      System.out.println(line);
    }
  }
  @Test
  public void testTreeSort3() {
    int runCount = 10;
    System.out.println("theta\t(c-2n.log2(n))/n");
    for (int x=0; x<100; ++x) {
      double theta = x / 100.0;
      int n = 1048575 + (int)Math.floor(1048576 * theta);
      ComparisonCountingHeapsortBase cch 
        = new ComparisonCountingHeapsortTopDown();
      for (int r=0; r<runCount; ++r) {
        int[] vData = random.randomPermutation(n);
        cch.sortRange(vData, 0, n);
      }
      double comparisons = cch.getComparisonCount() / runCount;
      double nby2log2n   = 2.0 * (double)n * Math.log(n) / Math.log(2.0);
      double diff = (comparisons - nby2log2n) / (double)n;
      
      System.out.println("" + theta + "\t" + 
        Math.floor(diff*1000000.0)/1000000.0);
    }
  }
  @Test
  public void testBottomUpHeapsort() {
    int runCount = 25;
    System.out.println("theta\t(c-n.log2(n))/n\tclimb(C)\tclimb(X)\tRaised(C)\tRaised(x)");
    for (int x=0; x<100; ++x) {
      double theta = x / 100.0;
      int n = 1048575 + (int)Math.floor(1048576 * theta);
      ComparisonCountingHeapsortBase cch 
        = new ComparisonCountingHeapsort();
      for (int r=0; r<runCount; ++r) {
        int[] vData = random.randomPermutation(n);
        cch.sortRange(vData, 0, n);
      }
      double comparisons = cch.getComparisonCount() / runCount;
      double nbylog2n    = (double)n * Math.log(n) / Math.log(2.0);
      double diff = (comparisons - nbylog2n) / (double)n;
      double divisor = (double)n * (double)runCount;
      double climbsC = cch.getConstructionLiftCount() / divisor;
      double climbsX = cch.getExtractionLiftCount()   / divisor;
      double liftedC = cch.getConstructionLiftedItemCount() / divisor;
      double liftedX = cch.getExtractionLiftedItemCount() / divisor;
      System.out.println("" + theta +  
        "\t" + Math.floor(diff*1000000.0)/1000000.0 + 
        "\t" + Math.floor(climbsC*1000000.0)/1000000.0 +
        "\t" + Math.floor(climbsX*1000000.0)/1000000.0 + 
        "\t" + Math.floor(liftedC*1000000.0)/1000000.0 +
        "\t" + Math.floor(liftedX*1000000.0)/1000000.0);
    }    
  }
  @Test
  public void testBottomUpHeapsortRadix3() {
    int runCount = 25;
    System.out.println("theta\t(c-2n.log3(n))/n\tclimb(C)\tclimb(X)");
    for (int x=0; x<100; ++x) {
      double theta = x / 100.0;
      int n = 797161 + (int)Math.floor(1594323 * theta);
      ComparisonCountingHeapsortBase cch 
        = new ComparisonCountingHeapsortRadix3();
      for (int r=0; r<runCount; ++r) {
        int[] vData = random.randomPermutation(n);
        cch.sortRange(vData, 0, n);
      }
      double comparisons = cch.getComparisonCount() / runCount;
      double nby2log3n   = 2.0 * (double)n * Math.log(n) / Math.log(3.0);
      double diff = (comparisons - nby2log3n) / (double)n;
      double climbsC = cch.getConstructionLiftCount() / (double)n / (double)runCount;
      double climbsX = cch.getExtractionLiftCount()   / (double)n / (double)runCount;
      System.out.println("" + theta +  
        "\t" + Math.floor(diff*1000000.0)/1000000.0 + 
        "\t" + Math.floor(climbsC*1000000.0)/1000000.0 +
        "\t" + Math.floor(climbsX*1000000.0)/1000000.0);
    }    
  }  
  @Test
  public void testBottomUpHeapsortRadix4() {
    int runCount = 25;
    System.out.println("theta\t(c-3.log4(n))/n\tclimb(C)\tclimb(X)");
    for (int x=0; x<100; ++x) {
      double theta = x / 100.0;
      int n = 349525 + (int)Math.floor(1048576 * theta);
      ComparisonCountingHeapsortBase cch 
        = new ComparisonCountingHeapsortRadix4();
      for (int r=0; r<runCount; ++r) {
        int[] vData = random.randomPermutation(n);
        cch.sortRange(vData, 0, n);
      }
      double comparisons = cch.getComparisonCount() / runCount;
      double nby2log3n   = 3.0 * (double)n * Math.log(n) / Math.log(4.0);
      double diff = (comparisons - nby2log3n) / (double)n;
      double climbsC = cch.getConstructionLiftCount() / (double)n / (double)runCount;
      double climbsX = cch.getExtractionLiftCount()   / (double)n / (double)runCount;
      System.out.println("" + theta +  
        "\t" + Math.floor(diff*1000000.0)/1000000.0 + 
        "\t" + Math.floor(climbsC*1000000.0)/1000000.0 +
        "\t" + Math.floor(climbsX*1000000.0)/1000000.0);
    }    
  }  
  protected String heapsortLine(ComparisonCountingHeapsortBase cch, int n) {
    int runCount = 10;
      for (int r=0; r<runCount; ++r) {
        int[] vData = random.randomPermutation(n);
        cch.sortRange(vData, 0, n);
      }
      int radix = cch.getRadix();
      double drops     = ( cch.getExtractionComparisonCount()
                         - cch.getExtractionLiftCount() ) / (double)runCount;
      double bottom    = (double)n * (radix-1) * Math.log(n) / Math.log(radix);
      double overhead  = (drops - bottom) / (double)n;
      double lifted    = cch.getExtractionLiftedItemCount() / (double)runCount / (double)n;
      double lifts     = cch.getExtractionLiftCount() / (double)runCount / (double)n;
          
      return  
          "\t" + Math.floor(overhead*1000000.0)/1000000.0 +
          "\t" + Math.floor(lifted*1000000.0)/1000000.0 +
          "\t" + Math.floor(lifts*1000000.0)/1000000.0;    
  }
  @Test
  public void testConstructionTime() {
    HeapsortBase[] heapsorts = new HeapsortBase[] {
     new HeapsortOriginal(),
     new HeapsortStandard(),
     new HeapsortBottomUp(),
     new ToplessHeapsort(),
     new TwoAtATimeHeapsort(),
     new HeapsortRecursive()
        };
    SortList sorts = new SortList();
    for (HeapsortBase h : heapsorts) {
      sorts.add(h);
    }
    (new SortTest()).warmUpSorts(sorts);    
    for (int n=1000;n<=100000000;n*=10) {
      String line = "" + n;
      int[] vData = random.randomPermutation(n);
      for (HeapsortBase h : heapsorts) {
        int[] vCopy = Arrays.copyOf(vData, n);
        long start = System.nanoTime();
        h.constructHeap(vCopy, 0, n);
        long stop = System.nanoTime();
        line += "\t" + (double)(stop-start)/1000000000.0;
      }
      System.out.println(line);
    }    
  }
  @Test
  public void testNToLift() {
    ComparisonCountingHeapsortBase[] sorts 
      = new ComparisonCountingHeapsortBase[]
        { new ComparisonCountingHeapsort()
        , new ComparisonCountingHeapsortRadix3()
        , new ComparisonCountingHeapsortRadix4()
        };
    double runCount = 500000;
    int    n = 10000;
    for (int r=0; r<runCount; ++r) {
      int[] vData = random.randomPermutation(n);
      for (ComparisonCountingHeapsortBase h : sorts) {
        int[] vCopy = Arrays.copyOf(vData, n);
        h.sortRange(vCopy, 0, n);
      }
    }
    for (int i=n-1; 0<=i; --i) {
      String line = "" + i;
      for (ComparisonCountingHeapsortBase h : sorts) {
        line += "\t" + h.getExtractionLiftCountVector()[i] / runCount;
      }
      System.out.println(line);
    }
  }
}
