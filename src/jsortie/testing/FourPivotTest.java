package jsortie.testing;

import java.util.ArrayList;

import org.junit.Test;

import jsortie.RangeSorter;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.centrifugal.CentrifugalPartitioner4;
import jsortie.quicksort.multiway.partitioner.fourpivot.FourPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.geometric.GeometricPartitioner4;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner4;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner4;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.multiway.selector.clean.SamplingMultiPivotSelector;

public class FourPivotTest extends MultiPivotTest {
  public void testSkewedPivotsForFourPivotPartitioners() {
    ArrayList<MultiPivotPartitioner> partitioners = new ArrayList<MultiPivotPartitioner>();
    partitioners.add( new FourPivotPartitioner());
    partitioners.add( new HolierThanThouPartitioner4());
    partitioners.add( new CentrifugalPartitioner4());
    partitioners.add( new GeometricPartitioner4());
    trySpecificPivots( 4, partitioners
            , new double[] {0.2, 0.4, 0.6, 0.8}
            , new int[] { 8192, 1048576, 32*1024*1024} );		
    trySpecificPivots( 4, partitioners
            , new double[] {0.1, 0.2, 0.8, 0.9}
            , new int[] { 8192, 1048576, 32*1024*1024} );		
    trySpecificPivots( 4, partitioners
            , new double[] {0.0625, 0.125, 0.25, 0.5}
            , new int[] { 8192, 1048576, 32*1024*1024} );		
    trySpecificPivots( 4, partitioners
            , new double[] {0.0625, 0.125, 0.1875, 0.25}
            , new int[] { 8192, 1048576, 32*1024*1024} );		
  }
  @Test
  public void testFourPivotSorts() {
    RangeSorter        isort = new InsertionSort2Way();
    MultiPivotSelector any4  = new SamplingMultiPivotSelector(4, false);
    SortList           sorts = new SortList(any4, isort, 64);
    sorts.addMPQ(new FourPivotPartitioner()       );
    sorts.addMPQ(new HolierThanThouPartitioner4() );
    sorts.addMPQ(new CentrifugalPartitioner4()    );
    //sorts.addMPQ(new CentripetalPartitioner4()    ); //busted
    sorts.addMPQ(new GeometricPartitioner4()      );
    sorts.addMPQ(new SkippyPartitioner4()       );
    DegenerateInputTest dit = DegenerateInputTest.newTest(1048576,25);
    sorts.warmUp();
    sorts.writeSortHeader("Input");
    dit.runSortsOnSortedData     ( sorts, "second" );
    dit.runSortsOnDuplicatedData ( sorts, "second" );
  }  
  public void test4PivotPartitionersWithSampling() {
    ArrayList<MultiPivotSelector> selectors = new ArrayList<MultiPivotSelector>();
    for (int s=3; s<=5; s+=2) {
      selectors.add(new SamplingMultiPivotSelector(s*5+4, false));
    }
    MultiPivotSelector skewed = new SamplingMultiPivotSelector(19, new int[] { 1,3,15,17 }, false);
    selectors.add(skewed);

    ArrayList<MultiPivotPartitioner> partitioners = new ArrayList<MultiPivotPartitioner>();
    partitioners.add ( new FourPivotPartitioner());
    partitioners.add ( new HolierThanThouPartitioner4());
    partitioners.add ( new CentrifugalPartitioner4());

    warmUpPartitioners(partitioners, 4);
    sortTest.testSelectorsAndPartitioners( selectors, partitioners, new int[] { 8192, 8192, 1048576, 33554432 });
  }
}
