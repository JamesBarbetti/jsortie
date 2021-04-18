package jsortie.testing.object;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.heapsort.ObjectHeapSort;
import jsortie.object.janitor.ObjectBinaryInsertionSort;
import jsortie.object.quicksort.governor.ArrayObjectQuicksort;
import jsortie.object.quicksort.partitioner.CentripetalObjectPartitioner;
import jsortie.object.quicksort.partitioner.HolierThanThouObjectPartitioner;
import jsortie.object.quicksort.partitioner.HoyosObjectPartitioner;
import jsortie.object.quicksort.partitioner.LomutoObjectPartitioner;
import jsortie.object.quicksort.partitioner.SinglePivotObjectPartitioner;
import jsortie.object.quicksort.partitioner.SingletonObjectPartitioner;
import jsortie.object.quicksort.partitioner.VanEmdenObjectPartitioner;
import jsortie.object.quicksort.partitioner.VanEmdenObjectPartitionerRevised;
import jsortie.object.quicksort.selector.MiddleElementObjectSelector;
import jsortie.testing.RandomInput;
import junit.framework.TestCase;

public class ObjectPartitionerTest extends TestCase {
  protected int runCount = 25;	
  protected Comparator<MyInteger> comp = MyInteger.integerComparator;
  protected RandomInput         random = new RandomInput();
  public String f5(double x) {
    return "\t" + (Math.floor(x * 100000.0 + .5) / 100000.0);
  }  
  protected void timePartitionerRuns
    ( List<SinglePivotObjectPartitioner<MyInteger>> partitioners
    , int m, int pm, double elapsedNanoseconds[]) {
    int data[] = random.randomPermutation(m);
    for (int i=0; i<m; ++i) {
      if (data[i]==pm) {
        data[i]=data[m/2];
        data[m/2] = pm;
      }
    }
    for (int p=0; p<partitioners.size(); ++p) {
      MyInteger[] copy = new MyInteger[m];
      for (int i=0; i<m; ++i) {
        copy[i] = new MyInteger(data[i]);
      }
      SinglePivotObjectPartitioner<MyInteger> party = partitioners.get(p);
      long startTime = System.nanoTime();
      party.partitionRange(MyInteger.integerComparator, copy, 0, copy.length, m/2);
      long stopTime = System.nanoTime();
      elapsedNanoseconds[p] += (double)(stopTime-startTime);
    }
  }
  protected List<SinglePivotObjectPartitioner<MyInteger>> getPartitioners() {
    List<SinglePivotObjectPartitioner<MyInteger>> parties 
      = new ArrayList<SinglePivotObjectPartitioner<MyInteger>>();
    parties.add(new SingletonObjectPartitioner<MyInteger>());
    parties.add(new CentripetalObjectPartitioner<MyInteger>());
    parties.add(new HolierThanThouObjectPartitioner<MyInteger>());
    parties.add(new LomutoObjectPartitioner<MyInteger>());
    parties.add(new HoyosObjectPartitioner<MyInteger>());
    return parties;
  }
  protected String getTabPrefixedPartitionerNames() {
    return "\tSingleton\tCentripetal\tHolierThanThou\tLomuto\tHoyos";
  }
  protected void warmUpPartitioners
    ( List<SinglePivotObjectPartitioner<MyInteger>> parties
    , Comparator<MyInteger> comparator) {
    int count = 1000;
    for (int r=0; r<1000; ++r) {
      int       input[] = random.randomPermutation(count);
      MyInteger copy[]  = new MyInteger[count];     
      for (SinglePivotObjectPartitioner<MyInteger> p : parties) {
        for (int i=0; i<count; ++i) {
          copy[i] = new MyInteger(input[i]);
        }
        p.partitionRange(comparator, copy, 0, copy.length, copy.length/2);
      }
    }
  }
  
  @Test
  public void testPartitionersOnRandomInput() {
    runCount = 625;
    List<SinglePivotObjectPartitioner<MyInteger>> parties = getPartitioners();
    warmUpPartitioners(parties, MyInteger.integerComparator);
    for (int m=1000;m<=10000000;m*=100) {	  
      System.out.println("for m=" + m);
      System.out.println(getTabPrefixedPartitionerNames());
      for (int pm=m/100; pm<m; pm+=m/100) {
        double elapsedNanoseconds[] = new double[parties.size()]; 
        for (int r=0;r<runCount;++r) { 
          timePartitionerRuns(parties,  m, pm, elapsedNanoseconds);
        } 
        double bitsAcquired = (bitsNeeded(m) - bitsNeeded(pm) - bitsNeeded(m-pm-1));
        String line = "" + (double)pm/(double)m;
        for (int p=0; p<parties.size(); ++p) {
          line += f5 ((double)runCount * bitsAcquired/elapsedNanoseconds[p]);
        }
        System.out.println(line);;
      }
      System.out.println("");
      runCount/=25;
    }
  }
  @Test
  public void testPartitionersOnRandomInputShowTimePerElement() {
    runCount = 625;
    List<SinglePivotObjectPartitioner<MyInteger>> parties 
      = getPartitioners();
    warmUpPartitioners(parties, MyInteger.integerComparator);
    for (int m=1000;m<=10000000;m*=100) {	  
      System.out.println("for m=" + m);
      System.out.println(getTabPrefixedPartitionerNames());
      for (int pm=m/100; pm<m; pm+=m/100) {
        double elapsedNanoseconds[] = new double[parties.size()]; 
        for (int r=0;r<runCount;++r) { 
          timePartitionerRuns(parties,  m, pm, elapsedNanoseconds);
        } 
        String line = "" + (double)pm/(double)m;
        for (int p=0; p<parties.size(); ++p) {
         line += f5(elapsedNanoseconds[p] / (double)runCount / (double)m);
        }
        System.out.println(line);;
      }
      System.out.println("");
      runCount/=25;
    }
  }
  @Test
  public void testVanEmdenObjectPartitioners() {
    List<SinglePivotObjectPartitioner<MyInteger>> parties 
    = getVanEmdenPartitioners();
    runCount = 1000;
    int m = 1048576;
    double[] elapsedNanoseconds = new double[parties.size()];
    double[] totalComparisons   = new double[parties.size()];
    double[] totalGain          = new double[parties.size()];
    for (int r=0; r<runCount; ++r) {    	
      int data[] = random.randomPermutation(m);
      for (int p=0; p<parties.size(); ++p) {
        MyInteger[] copy = new MyInteger[m];
        for (int i=0; i<m; ++i) {
          copy[i] = new MyInteger(data[i]);
        }
        SinglePivotObjectPartitioner<MyInteger> party = parties.get(p);
        MyInteger.comparisons = 0;
        long startTime = System.nanoTime();
        int k = party.partitionRange 
                ( MyInteger.countingComparator, copy, 0, copy.length, m/2 );
        long stopTime = System.nanoTime();
        double stopComparisons = MyInteger.comparisons;
        elapsedNanoseconds[p] += (double)(stopTime-startTime);
        totalComparisons[p]   += (double)(stopComparisons);
        double bitsAqcquired = bitsNeeded(m) - bitsNeeded(k) - bitsNeeded(m-k-1);
        totalGain[p] += bitsAqcquired;
      }
    }   
    for (int p=0; p<parties.size(); ++p) {
      System.out.println( parties.get(p).toString() 
        + "\t" + r(totalGain[p]/elapsedNanoseconds[p],10000)
        + "\t" + r(totalGain[p]/(double)runCount/(double)m,10000)
        + "\t" + r((double)runCount*(double)m/totalGain[p],10000));
    }
  }
  @Test
  public void testVanEmdenSorts() {
    List<SinglePivotObjectPartitioner<MyInteger>> parties 
      = getVanEmdenPartitioners();
    runCount = 20000;
    for (int n = 1024; 0<runCount; n*=2, runCount/=2) {
      double[] elapsedNanoseconds = new double[parties.size()];
      double[] totalComparisons   = new double[parties.size()];
      MiddleElementObjectSelector<MyInteger> middle 
        = new MiddleElementObjectSelector<MyInteger>();
      ObjectBinaryInsertionSort<MyInteger> janitor
        = new ObjectBinaryInsertionSort<MyInteger>();
      ObjectHeapSort<MyInteger> heapsort 
        = new ObjectHeapSort<MyInteger>();
      for (int r=0; r<runCount; ++r) {    	
        int data[] = random.randomPermutation(n);
        for (int p=0; p<parties.size(); ++p) {
          MyInteger[] copy = new MyInteger[n];
          for (int i=0; i<n; ++i) {
            copy[i] = new MyInteger(data[i]);
          }
          SinglePivotObjectPartitioner<MyInteger> party = parties.get(p);
          ObjectRangeSorter<MyInteger> sorter 
            = new ArrayObjectQuicksort<MyInteger>
                  ( middle, party, janitor, 32, heapsort );
          MyInteger.comparisons = 0;
          long startTime = System.nanoTime();
          sorter.sortRange(MyInteger.countingComparator, copy, 0, n);
          long stopTime = System.nanoTime();
          double stopComparisons = MyInteger.comparisons;
          elapsedNanoseconds[p] += (double)(stopTime-startTime);
          totalComparisons[p]   += (double)(stopComparisons);
        }
      }   
      double totalGain = bitsNeeded(n) * runCount;
      //note that totalGain is runCount*n/Math.log(2) *less* 
      //than runCount*n.Math.log(n)/Math.log(2).  
      double rnlog2n = (double) n * Math.log(n)/Math.log(2) * runCount;
      System.out.println("for n=" + n + " (" + runCount + " executions)");
      for (int p=0; p<parties.size(); ++p) {
        System.out.println( parties.get(p).toString() 
          + "\t" + r(totalGain/elapsedNanoseconds[p],10000)
          + "\t" + r(totalGain/totalComparisons[p],10000)
          + "\t" + r(totalComparisons[p]/totalGain,10000)
          + "\t" + r(rnlog2n/totalComparisons[p],10000)
          + "\t" + r(totalComparisons[p]/rnlog2n,10000));
      }
      System.out.println("");
    }
  }
  private double r(double x, double p) {
    return Math.floor(x*p+.5)/p;
  }
  protected List<SinglePivotObjectPartitioner<MyInteger>> 
    getVanEmdenPartitioners() {
    List<SinglePivotObjectPartitioner<MyInteger>> parties 
      = new ArrayList<SinglePivotObjectPartitioner<MyInteger>>();
    parties.add(new SingletonObjectPartitioner<MyInteger>());
    parties.add(new VanEmdenObjectPartitioner<MyInteger>());
    parties.add(new VanEmdenObjectPartitionerRevised<MyInteger>());
    return parties;
  }
  protected double bitsNeeded(int n) {
    return ((double)n * (Math.log(n) - 1.0)) / Math.log(2);
  }
}
