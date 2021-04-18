package jsortie.testing;

import org.junit.Test;

import jsortie.StableRangeSorter;
import jsortie.janitors.insertion.PairInsertionSort;
import jsortie.mergesort.asymmetric.AsymmetricMergesort;
import jsortie.mergesort.asymmetric.BranchAvoidingMergesort;
import jsortie.mergesort.straight.AlternatingStraightMergesort;
import jsortie.mergesort.straight.BranchAvoidingStraightMergesort;
import jsortie.mergesort.straight.SheepishMergesort;
import jsortie.mergesort.straight.StraightMergesort;
import jsortie.mergesort.ternary.TernaryMergesort;
import jsortie.mergesort.ternary.TernaryMergesort2;
import jsortie.mergesort.timsort.IntegerTimsortWrapper;
import jsortie.mergesort.vanilla.MergesortBase;
import jsortie.mergesort.vanilla.StaircaseMergesort;
import jsortie.mergesort.vanilla.Urgesort;

public class MergeSortTest 
  extends SortTest {
  @Test
  public void testMergesorts() {
    PairInsertionSort isort = new PairInsertionSort();
    SortList          sorts = getMergesorts(isort, 64);
    //sortTest.warmUpSorts(sorts);
    testSpecificSorts(sorts, 10, 1000000, 10);		
  }	
  public SortList getMergesorts(StableRangeSorter janitor, int janitorThreshold) {
    SortList sorts = new SortList();
    sorts.add(new IntegerTimsortWrapper());
    sorts.add(new TernaryMergesort(janitor, janitorThreshold));
    sorts.add(new TernaryMergesort2(janitor, janitorThreshold));
    sorts.add(new StraightMergesort(janitor, janitorThreshold));
    sorts.add(new AlternatingStraightMergesort(janitor, janitorThreshold));
    sorts.add(new MergesortBase(janitor, janitorThreshold));
    sorts.add(new StaircaseMergesort(janitor, janitorThreshold));
    sorts.add(new AsymmetricMergesort(janitor, janitorThreshold));
    sorts.add(new BranchAvoidingMergesort(janitor, janitorThreshold));
    sorts.add(new BranchAvoidingStraightMergesort(janitor, janitorThreshold));
    sorts.add(new SheepishMergesort(janitor, janitorThreshold));
    //sorts.add(new SpliceMergesort(janitor, janitorThreshold));
    return sorts;
  }	
  @Test
  public void testAlternatingMergesorts() {
    SortList          sorts = new SortList();
    PairInsertionSort isort = new PairInsertionSort();
    sorts.add(new StraightMergesort(isort, 64));
    sorts.add(new AlternatingStraightMergesort(isort, 64));
    warmUpSorts(sorts);
    testSpecificSorts(sorts, 10, 1000000, 100);		
  }
  @Test
  public void testUrgesort() {
    SortList          sorts = new SortList();
    PairInsertionSort isort = new PairInsertionSort();
    sorts.add(new StraightMergesort(isort, 64));
    sorts.add(new IntegerTimsortWrapper());
    sorts.add(new Urgesort(isort, 64));
    warmUpSorts(sorts);
    DegenerateInputTest dit = DegenerateInputTest.newTest(65536,10);
    dit.runSortsOnSortedData(sorts, "millisecond");
    //testSpecificSorts(sorts, 10, 1000000, 100);
  }
  @Test
  public void testUrgesortAgin() {
    for (int n=1024;n<50000000;n+=n) {
      PairInsertionSort isort = new PairInsertionSort();
      new Urgesort(isort, 1).sortRange(random.randomPermutation(n), 0, n);
      double bits = SortTest.bitsLeftInPartitions(new int[]{0,n});  
      System.out.println((int)Math.floor(bits) + "\n");
    }
  }
  @Test
  public void testMergeEfficiency() {
    for (int m=1; m<=1048576; m+=m) {
      double e = 2-1.0/m;
      double g =0;
      for (int i=1; i<=m; ++i) {
        g += Math.log((double)(i+m)/(double)i) / Math.log(2.0);
      }
      System.out.println("" + m + "\t" + g + "\t" + (2*m-e) + "\t" + g/(2*m-e) + "\t" + (2*m-e-g) 
      		+ "\t" + (Math.log(m)/Math.log(2.0)/2.0-1.17425184114));
    }
  }
  @Test 
  public void testMergeComparisons() {
    System.out.println("m\tc\tg\to\to/m/2\teff");
    double cc=0;
    double gg=0;
    int n=1;
    for (int m=1; m<=1048576*256; m+=m) {
      double p = 2; //probability the hth last and all after it will be from same side
      double s = 0; //saved comparisons
      for (int h=0; h<m; ++h) {
        p *= (double)(m-h)/(double)(2*m-h);
        s += p;
      }
      double g = 0; //gain in bits
      for (int i=1; i<=m; ++i) {
        g += Math.log((double)(i+m)/(double)i) / Math.log(2.0);
      }
      double c = 2*m-s; //comparisons
      cc = cc*2 + c;
      gg = gg*2 + g;
      double o = cc-gg; //overhead
      System.out.println("" + m + "\t" + c + "\t" + g + "\t" + o + "\t" + (o/m/2) + "\t" + g/c );
      n = m * 2;
    }
    double w = n * (Math.log(n)/Math.log(2) - 1); //the m.(log2(m)-1) worst case for mergesort 
    
    System.out.println( "n=" + n + ", w=" + w + ", a=" + cc + ", g=" + gg);
    System.out.println( "(w-a)/n = " + (w-cc)/n); //yep, 0.2645
  }
}
