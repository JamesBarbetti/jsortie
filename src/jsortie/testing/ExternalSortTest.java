package jsortie.testing;

import org.junit.Test;

import jsortie.RangeSorter;
import jsortie.StableRangeSorter;
import jsortie.janitors.insertion.PairInsertionSort;
import jsortie.mergesort.asymmetric.AsymmetricMergesort;
import jsortie.mergesort.asymmetric.BranchAvoidingMergesort;
import jsortie.mergesort.timsort.IntegerTimsortWrapper;
import jsortie.quicksort.external.AdaptiveExternalQuicksort;
import jsortie.quicksort.external.BranchAvoidingExternalPartitioner;
import jsortie.quicksort.external.DefaultExternalPartitioner;
import jsortie.quicksort.external.ExternalQuicksort;
import jsortie.quicksort.external.ExternalSinglePivotPartitioner;
import jsortie.quicksort.governor.QuicksortGovernor;
import jsortie.quicksort.partitioner.branchavoiding.BalancedSkippyPartitioner;
import jsortie.quicksort.partitioner.insideout.CentripetalPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.lomuto.LomutoPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.clean.CleanLeftHandedSelector;
import jsortie.quicksort.selector.clean.CleanRemedianSelector;
import jsortie.quicksort.selector.clean.CleanRightHandedSelector;
import jsortie.quicksort.selector.clean.CleanSinglePivotSelector;
import jsortie.quicksort.selector.clean.CleanTheoreticalSelector;
import jsortie.quicksort.selector.simple.MiddleElementSelector;

public class ExternalSortTest extends SinglePivotTest {
  @Test
  public void testExternalQuicksort() {
    SinglePivotSelector    middle = new MiddleElementSelector();
    SinglePivotSelector    remmy  = new CleanRemedianSelector(true);
    SinglePivotSelector    lefty  = new CleanLeftHandedSelector(false);
    SinglePivotSelector    righto = new CleanRightHandedSelector(false);
    SinglePivotPartitioner kanga  = new BalancedSkippyPartitioner();
    ExternalSinglePivotPartitioner baxp 
      = new BranchAvoidingExternalPartitioner();
    RangeSorter pisort = new PairInsertionSort();
    int thresh = 64;
    
    SortList sorts = new SortList();
    
    /*
    sorts.add(new IntegerTimsortWrapper());
    System.out.println("Testing Timsort (sorting efficiency in Gbps) on random input");
    sortTest.warmUpSorts(sorts);
    sorts.writeSortHeader("n");
    sortTest.testSpecificSorts(sorts, 500, 100, 100000, 1000000, 20 );
     */
    
    sorts.clear();
    sorts.add(new QuicksortGovernor(middle, new CentripetalPartitioner(), pisort, thresh), "Middle/Centripetal");
    sorts.add(new QuicksortGovernor(lefty,  new LomutoPartitioner(),      pisort, thresh), "Middle/Lomuto");
    sorts.add(new ExternalQuicksort(middle, pisort, thresh), "Middle/XP");
    sorts.add(new ExternalQuicksort(lefty,  pisort, thresh), "LeftHanded/XP");
    sorts.add(new ExternalQuicksort(righto, pisort, thresh), "RightHanded/XP");
    System.out.println("Testing Quicksort (1st, 2nd) versus QuicksortExternal (3rd, 4th, 5th) (sorting efficiency in Gbps) on random input");
    sortTest.warmUpSorts(sorts);
    sorts.writeSortHeader("n");
    sortTest.testSpecificSorts(sorts, 500, 10000, 10000, 1000000, 100 );
    
    sorts.clear();
    sorts.add(new QuicksortGovernor(middle, kanga, pisort, thresh), "Middle/Skippy");
    sorts.add(new QuicksortGovernor(remmy,  kanga, pisort, thresh), "Remedian/Skippy");
    sorts.add(new ExternalQuicksort(middle, baxp , pisort, thresh), "Middle/BAXP");
    sorts.add(new ExternalQuicksort(remmy,  baxp , pisort, thresh), "Remedian/BAXP");
    System.out.println("Testing Quicksort (1st, 2nd) versus QuicksortExternal (3rd) (sorting efficiency in Gbps) on random input");
    sortTest.warmUpSorts(sorts);
    sorts.writeSortHeader("n");
    sortTest.testSpecificSorts(sorts, 500, 10000, 100000, 1000000, 100 );
  }	
  @Test
  public void testAdaptiveExternalQuicksort() {
    SinglePivotSelector lefty 
      = new CleanLeftHandedSelector(false);
    SinglePivotSelector remmy 
      = new CleanRemedianSelector(true);
    ExternalSinglePivotPartitioner dxp  
      = new DefaultExternalPartitioner();
    ExternalSinglePivotPartitioner baxp 
      = new BranchAvoidingExternalPartitioner();
    RangeSorter         pisort = new PairInsertionSort();
    int                 thresh = 64;
    SortList            sorts  = new SortList();
    sorts.add ( new IntegerTimsortWrapper(), "Timsort");
    sorts.add ( new ExternalQuicksort
                  ( lefty,  pisort, thresh )
              , "XQ (CleanLeftHandedSelector)");
    sorts.add ( new ExternalQuicksort
                    ( remmy,  baxp , pisort, thresh )
              , "Remedian/BAXP");
    sorts.add ( new AdaptiveExternalQuicksort
                    ( lefty,  dxp, pisort, thresh )
              , "AXQ (CleanLeftHandedSelector)");
    System.out.println
      ( "Testing AdaptiveExternalQuicksort" 
      + " (running times in milliseconds)" 
      + " on 65536-integer inputs");
    sortTest.warmUpSorts(sorts);
    sorts.writeSortHeader("Input");
    DegenerateInputTest dit 
      = DegenerateInputTest.newTest(65536, 100);
    dit.runSortsOnSortedData     ( sorts, "millisecond");
    dit.runSortsOnDuplicatedData ( sorts, "millisecond");
  }
  @Test
  public void testBranchAvoidingExternalQuicksort() {
    SortList                       sorts    = new SortList();
    StableRangeSorter              pisort   = new PairInsertionSort();
    CleanSinglePivotSelector       lefty    = new CleanLeftHandedSelector(false);
    CleanSinglePivotSelector       middling = new CleanTheoreticalSelector(.5);
    ExternalSinglePivotPartitioner baxp     = new BranchAvoidingExternalPartitioner();
    System.out.println("Testing Mergesorts versus QuicksortExternal (sorting efficiency in Gbps) on random input\n");
    sorts.add(new IntegerTimsortWrapper());
    sorts.add(new AsymmetricMergesort     (pisort, 64));
    sorts.add(new BranchAvoidingMergesort (pisort, 64));
    sorts.add(new ExternalQuicksort(lefty,          pisort, 64));
    sorts.add(new ExternalQuicksort(middling, baxp, pisort, 64));
    sortTest.warmUpSorts(sorts, 1024, 3000);
    
    System.out.println("Running times in milliseconds to sort 65,536 integer inputs");
    sortTest.dumpHeaderLine("Input", sorts);
    DegenerateInputTest dit = DegenerateInputTest.newTest(65536, 20);
    dit.runSortsOnSortedData(sorts, "millisecond");
    dit.runSortsOnDuplicatedData(sorts, "millisecond");
    
    sortTest.dumpHeaderLine("n", sorts);
    sortTest.testSpecificSorts(sorts, 10, 10000, 400, 10000000, 25);
  }
}
