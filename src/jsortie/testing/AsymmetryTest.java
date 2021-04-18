package jsortie.testing;

import java.util.Arrays;

import org.junit.Test;

import jsortie.janitors.insertion.InsertionSort;
import jsortie.mergesort.asymmetric.BranchAvoidingMergesort;
import jsortie.mergesort.vanilla.StaircaseMergesort;
import jsortie.quicksort.external.BranchAvoidingExternalPartitioner;
import jsortie.quicksort.external.DefaultExternalPartitioner;
import jsortie.quicksort.external.ExternalQuicksort;
import jsortie.quicksort.external.ExternalSinglePivotPartitioner;
import jsortie.quicksort.partitioner.bidirectional.centrepivot.CentrePivotPartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.RevisedHoyosMirrorPartitioner;
import jsortie.quicksort.partitioner.bidirectional.traditional.SingletonPartitioner;
import jsortie.quicksort.partitioner.bizarre.LoveChildPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyMirrorPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyPartitioner;
import jsortie.quicksort.partitioner.insideout.CentripetalPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.lomuto.LomutoMirrorPartitioner;
import jsortie.quicksort.partitioner.lomuto.LomutoPartitioner;
import jsortie.quicksort.selector.simple.MiddleElementSelector;

public class AsymmetryTest
{
  PartitionerTest tp = new PartitionerTest();
  SortTest  sortTest = new SortTest();
  RandomInput random = new RandomInput();
  public void testFigureOutBestValueOfPForQuicksort() {
    runAsymmetryTests(tp.getStandardPartitioners(), 8192, 16, 1000);
  }
  public void testFigureOutBestValueOfPForQuicksortBig() {
    runAsymmetryTests(tp.getStandardPartitioners(), 65536*16, 2048, 25);
  }
  public void testFigureOutBestValueOfPForQuicksortReallyBig() {
    runAsymmetryTests(tp.getStandardPartitioners(), 65536*16*32, 65536, 4);
  }
  public void testLoveChildPartitioner() {
    NamedArrayList<SinglePivotPartitioner> partitioners 
      = new NamedArrayList<SinglePivotPartitioner> ();
    partitioners.add("Singleton",   new SingletonPartitioner());
    partitioners.add("Lomuto",      new LomutoPartitioner());
    partitioners.add("LomutoMirro", new LomutoMirrorPartitioner());
    partitioners.add("CentrePivot", new CentrePivotPartitioner());
    partitioners.add("R-Hoyos",     new RevisedHoyosMirrorPartitioner());
    partitioners.add("Centripetal", new CentripetalPartitioner());
    partitioners.add("Skippy",    new SkippyPartitioner());
    partitioners.add("LoveChild",   new LoveChildPartitioner());
    tp.warmUpPartitioners(partitioners.getArrayList());
    runAsymmetryTests(partitioners, 65536, 16*16, 100);
    //runAsymmetryTests(partitioners, 1048576*16, 1048576, 10);
  }	
  public void testKangarooPartitionerResponseToX() {
    NamedArrayList<SinglePivotPartitioner> partitioners 
      = new NamedArrayList<SinglePivotPartitioner> ();
    partitioners.add("CentrePivot", new CentrePivotPartitioner());
    partitioners.add("Skippy",    new SkippyPartitioner());
    tp.warmUpPartitioners(partitioners.getArrayList());
    runAsymmetryTests(partitioners, 65536, 16*16, 100);
  }
  @Test
  public void testMirrorPartitionerResponseToX() {
    NamedArrayList<SinglePivotPartitioner> partitioners
     = new NamedArrayList<SinglePivotPartitioner> ();
    partitioners.add("Skippy",       new SkippyPartitioner());
    partitioners.add("SkippyMirror", new SkippyMirrorPartitioner());
    partitioners.add("Lomuto",         new LomutoPartitioner());
    partitioners.add("LomutoMirror",   new LomutoMirrorPartitioner());
    tp.warmUpPartitioners(partitioners.getArrayList());
    runAsymmetryTests(partitioners, 65536, 16*16, 1000);
  }
  public void testExternalVersusMergesort() {
    InsertionSort       janitor = new InsertionSort();
    DefaultExternalPartitioner xp      = new DefaultExternalPartitioner();
    StaircaseMergesort  merger  = new StaircaseMergesort(janitor, 64);
    testSpecificExternalVersusMergesort(xp, merger);
  }
  @Test
  public void testExternalVersusMergesort2() {
    InsertionSort                janitor = new InsertionSort();
	BranchAvoidingMergesort           cm = new BranchAvoidingMergesort(janitor, 15);
	sortTest.warmUpSort (cm, 1000, 10000); 
	
    MiddleElementSelector             me = new MiddleElementSelector();
	BranchAvoidingExternalPartitioner cx = new BranchAvoidingExternalPartitioner();
    ExternalQuicksort                 qs = new ExternalQuicksort ( me, cx, janitor, 15 );
	sortTest.warmUpSort (qs, 1000, 10000);  

	testSpecificExternalVersusMergesort(cx, cm);
  }
  
  public void testSpecificExternalVersusMergesort 
    ( ExternalSinglePivotPartitioner xp
    , StaircaseMergesort  sm) {
    int n        = 65536;
    int step     = 256;
    int runCount = 20;
    System.out.println("Average efficiency (Gbps) for merging/partitioning " + n + " integers, as a function of x");
    System.out.println("xm\tx\t" + xp.toString() + "\t" + sm.toString() );
    for ( int x=step; x<n; x+=step) {
      double time[] = new double[2];
      for ( int run=0; run<runCount; ++run ) {
        int input[] = random.randomPermutation(n);
        // fake input for external partitioner
        int aux[]  = new int[n];
        int w = 0;
        for (int r=0; r<n; ++r) {
          if ( input[r] != x ) {
            aux[w++] = input[r];
          }
          if ( w == n / 2 ) {
            aux[w++] = x;
          }
        }
        int main[] = Arrays.copyOf(aux, n);
        // and feed the beast.
        time[0] -= System.nanoTime();
        xp.partitionMainRange(main, 0, n, n/2, aux, 0);
        time[0] += System.nanoTime();
        int copy2[] = Arrays.copyOf(input, aux.length);
        int copy3[] = Arrays.copyOf(input, x);
        sm.sortRange(copy2, x, n);
        sm.sortRange(copy3, 0, x);    	  
        time[1] -= System.nanoTime();
        sm.mergeToLeftForecasting(copy3, 0, x, copy2, x, n, 0);
        time[1] += System.nanoTime();
      }
      String s = Integer.toString(x) + "\t"
              + Math.floor((double)x/(double)n*1000.0 + .5) / 1000.0;
      double bitsInInput           = (double)n*Math.log(n)/Math.log(2.0);
      double bitsUnresolvedOnLeft  = (x>0) ? ((double)x*Math.log(x)/Math.log(2.0)) : 0;
      double bitsUnresolvedOnRight = (n-x-1>0) ? ((double)(n-x-1)*Math.log(n-x-1)/Math.log(2.0)) : 0;
      double bitsFiguredOut = bitsInInput - bitsUnresolvedOnLeft - bitsUnresolvedOnRight;
      //Gbps, because times are in nanoseconds
      s+= "\t" + Math.floor(1000.0*bitsFiguredOut*runCount/(double)time[0]+.5)/1000.0;
      s+= "\t" + Math.floor(1000.0*bitsFiguredOut*runCount/(double)time[1]+.5)/1000.0;
      System.out.println ( s);
    }
  }
  public void runAsymmetryTests
    ( NamedArrayList<SinglePivotPartitioner> partitioners
    , int n, int step, int runs) {
    tp.warmUpPartitioners(partitioners.getArrayList());
    String header = "n\tx";
    for ( SinglePivotPartitioner party : partitioners) {
      header += "\t" + party.toString();
    }
    System.out.println(header);
    int[] fails = new int[partitioners.size()];
    for ( int x = 0; x<n; x+=step) {    	
      double time[] = new double[partitioners.size()];
      for (int run=0; run<runs; ++run) {
        int input[] = random.randomPermutation(n);				
        int copy[]  = new int[n];
        for (int pp=0; pp<partitioners.size(); ++pp) {
          int w = 0;
          for (int r=0; r<n; ++r) {
            if (input[r]!=x)
              copy[w++] = input[r];
            if (w== n/2)
              copy[w++] = x;
          }
          int par = ( pp + run ) % partitioners.size();
          SinglePivotPartitioner party = partitioners.get(par);
          long t1 = System.nanoTime();
          int check = party.partitionRange(copy, 0, n, n/2);
          if (check!=x) {
            ++fails[par];
          }
          long t2 = System.nanoTime();
          time[par] += t2 - t1;
        }
      }
      String s = Integer.toString(x) + "\t"
               + Math.floor((double)x/(double)n*1000.0 + .5) / 1000.0;
      double bitsInInput = (double)n*Math.log(n)/Math.log(2.0);
      double bitsUnresolvedOnLeft = (x>0) ? ((double)x*Math.log(x)/Math.log(2.0)) : 0;
      double bitsUnresolvedOnRight = (n-x-1>0) ? ((double)(n-x-1)*Math.log(n-x-1)/Math.log(2.0)) : 0;
      double bitsFiguredOut = bitsInInput - bitsUnresolvedOnLeft - bitsUnresolvedOnRight;
      for (int i=0; i<partitioners.size(); ++i) {
        s+= "\t" + Math.floor(1000.0*bitsFiguredOut*runs/(double)time[i]+.5)/1000.0; //Efficiency (Gbps), to 3 decimal places
      }
      System.out.println ( s);
    }
    for (int par=0; par<partitioners.size(); ++par) {
      if ( 0 < fails[par] )  {    	  
        System.out.println( partitioners.get(par).toString() + " failed " + fails[par] + "times.");
      }
    }
  }
}
