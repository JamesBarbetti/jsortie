package jsortie.testing.object;

import java.util.Comparator;

import org.junit.Test;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.collector.ObjectBalancedChenCollector;
import jsortie.object.collector.ObjectChenCollector;
import jsortie.object.collector.ObjectPositionalCollector;
import jsortie.object.collector.ObjectSampleCollector;
import jsortie.object.heapsort.ObjectHeapSort;
import jsortie.object.janitor.ObjectBinaryInsertionSort;
import jsortie.object.mergesort.ArrayObjectMergesort;
import jsortie.object.quicksort.governor.InternalObjectSampleSort;
import jsortie.object.quicksort.governor.ObjectSymmetryPartitionSort;
import jsortie.object.quicksort.partitioner.SinglePivotObjectPartitioner;
import jsortie.object.quicksort.partitioner.SingletonObjectPartitioner;
import jsortie.object.quicksort.selector.DirtyObjectTheoreticalSelector;
import jsortie.quicksort.samplesizer.ChenSampleSizer;
import jsortie.quicksort.samplesizer.OversamplingSampleSizer;
import jsortie.quicksort.samplesizer.SquareRootSampleSizer;

public class ObjectSampleSortTest 
  extends ObjectSortTest {
  ObjectBinaryInsertionSort<MyInteger> bisort 
    = new ObjectBinaryInsertionSort<MyInteger>();
  SinglePivotObjectPartitioner<MyInteger> party
    = new SingletonObjectPartitioner<MyInteger>();
  public static ObjectRangeSorter<MyInteger> getInternalSampleSort() {
    return new InternalObjectSampleSort<MyInteger>
      ( new SquareRootSampleSizer()
      , new ObjectPositionalCollector<MyInteger>() 
      , new SingletonObjectPartitioner<MyInteger>()
      , new ObjectHeapSort<MyInteger>(), 128, true);
  }
  public ObjectSortList<MyInteger> getSampleSorts(int oversample, int topAlpha) {
    ObjectSortList<MyInteger> sorts 
      = new ObjectSortList<MyInteger>();
    sorts.addSPQ(party, "Vanilla");
    sorts.setSelector(new DirtyObjectTheoreticalSelector<MyInteger>( 0.5));
    sorts.addSPQ(party, "Thoeretical");
    sorts.add ( new ArrayObjectMergesort<MyInteger>(), "Mergesort");
    ObjectSampleCollector<MyInteger> collector; 
    
    /* The sqrt(n) and n/log(n) selectors kind of, well, suck
    collector  = new ObjectPositionalCollector<MyInteger>();
    sorts.add ( new InternalObjectSampleSort<MyInteger>
                    ( collector, party, bisort, 32, false));
    sorts.add ( new ObjectSymmetryPartitionSort<MyInteger>
                    ( collector, party, bisort, 32, false));
     */
    for (int alpha=topAlpha;alpha>1;alpha>>=1) {
      OversamplingSampleSizer chenSizer = new ChenSampleSizer(alpha,oversample);
      collector = new ObjectChenCollector<MyInteger>(alpha,oversample);
      sorts.add ( new InternalObjectSampleSort<MyInteger>
                      ( chenSizer, collector, party, bisort, 32, false), "M/F" + alpha );
    }
    
    for (int alpha=topAlpha;alpha>1;alpha>>=1) {
      OversamplingSampleSizer chenSizer = new ChenSampleSizer(alpha,oversample);
      collector = new ObjectChenCollector<MyInteger>(alpha,oversample);
      sorts.add ( new ObjectSymmetryPartitionSort<MyInteger>
                      ( chenSizer, collector, party, bisort, 32, false), "Chen" + alpha);
    }
    return sorts;
  }
  
  @Test
  public void testInternalObjectSampleSortParameters() {
    ObjectSortList<MyInteger> sorts = getSampleSorts(1, 16);
    warmUpSorts(sorts, MyInteger.integerComparator, 100);
    warmUpSorts(sorts, MyInteger.countingComparator, 100);
    for (int n=16384; n<=1048576; n*=4) {
      int runs = (n<=65536) ? 25 : 5;
      System.out.println("Object (Internal) Sample Sort timings on randomly permuted " + n + "-item inputs");
      sorts.writeSortHeader("efficiency(Gbps)");
      System.out.println( "efficiency(Gbps)" + sortLine(sorts, MyInteger.integerComparator, n, runs, false, false) );
      System.out.println( "efficiency(Gbps)" + sortLine(sorts, MyInteger.countingComparator, n, runs, false, false) );
      System.out.println("Note: second row, using a comparator that counts how many comparisons");
      System.out.println("");
      System.out.println("Object (Internal) Sample Sort comparison count overheads on randomly permuted " + n + "-item inputs");
      sorts.writeSortHeader("overhead");
      System.out.println( "overhead" + sortLine(sorts, MyInteger.countingComparator, n, runs, true, true) );
      System.out.println( "Note: Best possible average is 1.0, best possible average overhead 0.0");
      System.out.println("");
    }
  }
   @Test
  public void testVaryingAlphaAndOversample() {
    int n = 100000;
    int runs = 100;
    System.out.println("overhead comparisons (divided by n), for n=" + n);
    for (int overSample = 0; overSample < 32; overSample+=overSample+1) {
      ObjectSortList<MyInteger> sorts = getSampleSorts(overSample, 128);
      if (overSample==0) {
        warmUpSorts(sorts, MyInteger.countingComparator, 100);
        warmUpSorts(sorts, MyInteger.integerComparator, 100);
        sorts.writeSortHeader("s");
      }
      System.out.println( "" + overSample + sortLine(sorts, MyInteger.countingComparator, n, runs, true, true) );
    }
    System.out.println("");    
    System.out.println("efficiency(Gbps), for n=" + n);
    for (int overSample = 0; overSample < 32; overSample+=overSample+1) {
      ObjectSortList<MyInteger> sorts = getSampleSorts(overSample, 128);
      if (overSample==0) {
        sorts.writeSortHeader("s");
      }
      System.out.println( "" + overSample + sortLine(sorts, MyInteger.integerComparator, n, runs, false, false) );
    }
    System.out.println("");
  }

  
  public static ObjectRangeSorter<MyInteger> 
    getInternalSampleSort ( Comparator<MyInteger> c, double alpha, int oversample    		
                          , ObjectRangeSorter<MyInteger> bisort) {
    ObjectBalancedChenCollector<MyInteger>
      chenCollector = new ObjectBalancedChenCollector<MyInteger>(alpha, oversample);    
    return new InternalObjectSampleSort<MyInteger>
                ( new ChenSampleSizer(), chenCollector
                , new SingletonObjectPartitioner<MyInteger>()
                , bisort, 32, false);
  }
  @Test
  public void testInternalObjectSampleSorts() {
    ObjectSortList<MyInteger> sorts = getSampleSorts(1,16);
    warmUpSorts(sorts, comp, 100);
    testSpecificSorts(sorts, false, false);		
  }
}
