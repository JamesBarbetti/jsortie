package jsortie.testing;

import java.util.ArrayList;

import org.junit.Test;

import jsortie.RangeSorter;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.quicksort.multiway.governor.MultiPivotQuicksort;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.centrifugal.CentrifugalPartitioner2;
import jsortie.quicksort.multiway.partitioner.centripetal.CentripetalPartitioner2;
import jsortie.quicksort.multiway.partitioner.geometric.GeometricPartitioner2;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThou2Metamorphic;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner2;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyCentripetalPartitioner2;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner2;
import jsortie.quicksort.multiway.partitioner.twopivot.DoubleLomutoPartitioner2;
import jsortie.quicksort.multiway.partitioner.twopivot.YaroslavskiyPartitioner2;
import jsortie.quicksort.multiway.partitioner.twopivot.YarrowPartitioner2;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.multiway.selector.clean.SamplingMultiPivotSelector;
import jsortie.quicksort.theoretical.Gain;

public class DualPivotTest extends MultiPivotTest {
  public void AnalyzeDPQ
    ( int sample, int left, int right, boolean yaroslavskiy) {
    double a[] = new double[sample];
    double tSwaps = 0;
    double compares = 0;
    double tCompares = 0;
    double pTotal = 0;
    double qTotal = 0;
    double tGain = 0;
    double gain;
    double pSquareTotal = 0;
    double matrix[] = new double[9];
    int run;
    for (run=0; run<10000000; ++run) {
      for (int k=0; k<sample; ++k) {
        a[k] = Math.random();
      }
      java.util.Arrays.sort(a);
      double p = a[left];
      double q = 1 - a[right];
      double m = 1 - p - q;
      matrix[0] += p*p;
      matrix[1] += p*m;
      matrix[2] += p*q;
      matrix[3] += m*p;
      matrix[4] += m*m;
      matrix[5] += m*q;
      matrix[6] += q*p;
      matrix[7] += q*m;
      matrix[8] += q*q;				
      pTotal += p;
      qTotal += q;
      pSquareTotal += p*p;
      if (yaroslavskiy) {
        compares = 2 - q*q - p*p - m*p;
        tSwaps  += p + q - q*q;
      } else {			
        compares = 1 + 1 - ((p<q) ? q : p);
        tSwaps  += p + q;
      }
      gain       = Gain.gainForInterval(q) + Gain.gainForInterval(1-p-q) + Gain.gainForInterval(q);
      //System.out.println("" + gain + "\t" + compares + "\t" + p + "\t" + q);
      tCompares += compares;
      tGain     += gain;			
    }
    double efficiency = tGain/tCompares;
    double moveRatio  = tSwaps/tCompares;
    System.out.println("" + sample + "\t" + left + "\t" + right + "\t" + (yaroslavskiy?"y":"n") 
					+ "\t" + tSwaps/run + "\t" + tCompares/run + "\t" + efficiency
					+ "\t" + moveRatio/efficiency/Math.log(2) + "\t" + 1/efficiency/Math.log(2) + "\t" 
					+ Math.floor(pTotal/run*100) + "\t" + Math.floor(qTotal/run*100) + "\t" + Math.sqrt(pSquareTotal/run - pTotal*pTotal/run/run));
    System.out.println("" + matrix[0]/run + "\t" + matrix[1]/run +"\t" + matrix[2]/run );
    System.out.println("" + matrix[3]/run + "\t" + matrix[4]/run +"\t" + matrix[5]/run );
    System.out.println("" + matrix[6]/run + "\t" + matrix[7]/run +"\t" + matrix[8]/run );
  }
  public void testMultiPivotSelectorCombinations() {
    System.out.println("sample\tleft\tright\tyaro\tswaps           \tcompares" 
      + "         \tgain                \tTswaps/nlnn        \tTcompares/nlnn");
    AnalyzeDPQ(2,0,1,true); //2-pivot as in Java
    AnalyzeDPQ(5,1,3,true); //2-pivot as per Yaroslavskiy
    AnalyzeDPQ(2,0,1,false);
    AnalyzeDPQ(5,1,3,false);
    AnalyzeDPQ(3,1,2,false);
    AnalyzeDPQ(7,3,5,false);
    AnalyzeDPQ(15,7,11,false);
  }
  @Test
  public void testYaroslavskiy() {
    //Statistics.samplingHistogram();
    YaroslavskiyPartitioner2 y = new YaroslavskiyPartitioner2();
    for (int r = 0; r<100; ++r) {
      int[] vArray = random.randomPermutation(1000000);
      if (vArray[vArray.length-1]<vArray[0]) {
        int v = vArray[0];
        vArray[0] =vArray[vArray.length-1];
        vArray[vArray.length-1]=v;
      }
      y.multiPartitionRange(vArray, 0, vArray.length, new int[] { 0, vArray.length-1 } );
    }				
  }
  @Test
  public void test2PivotPartitionersWithUniformSampling() {
    ArrayList<MultiPivotSelector> selectors = new ArrayList<MultiPivotSelector>();
    for (int s=3; s<=5; s+=2) {
      selectors.add(new SamplingMultiPivotSelector(s*3+2, false));
    }
    ArrayList<MultiPivotPartitioner> partitioners = new ArrayList<MultiPivotPartitioner>();
    partitioners.add ( new HolierThanThouPartitioner2());
    partitioners.add ( new CentrifugalPartitioner2());
    partitioners.add ( new DoubleLomutoPartitioner2());
    partitioners.add ( new YaroslavskiyPartitioner2());
    partitioners.add ( new CentripetalPartitioner2());
    partitioners.add ( new SkippyPartitioner2());
    partitioners.add ( new SkippyCentripetalPartitioner2());
    partitioners.add ( new YarrowPartitioner2());
    sortTest.testSelectorsAndPartitioners
      ( selectors, partitioners
      , new int[] { 8192, 8192, 1048576, 33554432 } );
  }
  public void testHolierThanThou2() {
    SortList sorts        = new SortList();
    MultiPivotSelector     any2         = new SamplingMultiPivotSelector(2, false);
    MultiPivotSelector     take2and4of7 = new SamplingMultiPivotSelector(7, new int[]{1,3}, false);
    RangeSorter            isort2       = new InsertionSort2Way();
    sorts.add( new MultiPivotQuicksort(any2,         new YaroslavskiyPartitioner2(),    isort2, 64));
    sorts.add( new MultiPivotQuicksort(take2and4of7, new YaroslavskiyPartitioner2(),    isort2, 64));
    sorts.add( new MultiPivotQuicksort(any2,         new HolierThanThouPartitioner2(), isort2, 64));
    sorts.add( new MultiPivotQuicksort(take2and4of7, new HolierThanThouPartitioner2(), isort2, 64));
    sorts.add( new MultiPivotQuicksort(any2,         new HolierThanThou2Metamorphic(), isort2, 64));
    sorts.add( new MultiPivotQuicksort(take2and4of7, new HolierThanThou2Metamorphic(), isort2, 64));		
    sortTest.testSpecificSorts( sorts, 10, 10000, 100, 10000000, 25  );
  }
  public void testHolierThanThou2AOnce() {
    MultiPivotSelector    spp = new SamplingMultiPivotSelector(2, false);
    MultiPivotPartitioner mpp = new CentrifugalPartitioner2();
    usePartitionerOnce(37, spp, mpp);		
  }
  @Test
  public void testSkewedPivotsForTwoPivotPartitioners() {
    ArrayList<MultiPivotPartitioner> partitioners = new ArrayList<MultiPivotPartitioner>();
    /*
    partitioners.add ( new HolierThanThouPartitioner2());
    partitioners.add ( new CentrifugalPartitioner2());
    partitioners.add ( new DoubleLomutoPartitioner2());
    partitioners.add ( new YaroslavskiyPartitioner2());
    */
    partitioners.add( new CentripetalPartitioner2());
    partitioners.add( new GeometricPartitioner2());
    trySkewedPivots( 2, 20, partitioners, new int[] { 8192, 1048576 } );
  }
}
