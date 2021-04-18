package jsortie.testing;

import java.util.ArrayList;

import org.junit.Test;

import jsortie.RangeSorter;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.quicksort.multiway.governor.MultiPivotQuicksort;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.centrifugal.CentrifugalPartitioner3;
import jsortie.quicksort.multiway.partitioner.centripetal.CentripetalPartitioner3;
import jsortie.quicksort.multiway.partitioner.geometric.GeometricPartitioner3;
import jsortie.quicksort.multiway.partitioner.geometric.GeometricPartitioner4;
import jsortie.quicksort.multiway.partitioner.geometric.GeometricPartitioner7;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThou3MetamorphicPartitioner;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner2;
import jsortie.quicksort.multiway.partitioner.holierthanthou.HolierThanThouPartitioner3;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner3;
import jsortie.quicksort.multiway.partitioner.kangaroo.KangarooPawPartitioner3;
import jsortie.quicksort.multiway.partitioner.threepivot.KLQMPartitioner3;
import jsortie.quicksort.multiway.partitioner.threepivot.OutsideInPartitioner3;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.multiway.selector.clean.SamplingMultiPivotSelector;
import jsortie.quicksort.partitioner.bidirectional.centrepivot.CentrePivotPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyMirrorPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyPartitioner;

public class ThreePivotTest
  extends MultiPivotTest {	
  @Test
  public void testThreePivotSorts() {
    RangeSorter isort = new InsertionSort2Way();
    MultiPivotSelector selector 
      = new SamplingMultiPivotSelector(3, false);
    SortList sorts 
      = new SortList(selector, isort, 64);
    sorts.addAdaptiveSPQ(new CentrePivotPartitioner());
    sorts.addMPQ ( new KLQMPartitioner3());
    sorts.addMPQ ( new OutsideInPartitioner3());
    sorts.addMPQ ( new HolierThanThouPartitioner3());
    sorts.addMPQ ( new GeometricPartitioner3());
    sorts.addMPQ ( new HolierThanThou3MetamorphicPartitioner());
    sorts.addMPQ ( new CentrifugalPartitioner3());
    sorts.addMPQ ( new CentripetalPartitioner3());
    sorts.add2PQ ( new SkippyPartitioner()
                 , new SkippyMirrorPartitioner());
    sorts.addMPQ ( new SkippyPartitioner3());
    sorts.addMPQ ( new KangarooPawPartitioner3());
    sortTest.warmUpSorts(sorts);
    sorts.writeSortHeader("input");
    int n = 1048576; //usually 1048576
    int r = 1048576 * 25 / n;
    DegenerateInputTest dit 
      = DegenerateInputTest.newTest(n, r);
    //dit.runSortsOnSortedData(sorts, "second");
    System.out.println("");
    dit.runSortsOnDuplicatedData(sorts, "second");
  }
  public void testHolierThanThou3() {
    SortList sorts        = new SortList();
    MultiPivotSelector     any2         = new SamplingMultiPivotSelector(2, false);
    MultiPivotSelector     any3         = new SamplingMultiPivotSelector(3, false);
    RangeSorter            isort2       = new InsertionSort2Way();

    sorts.add( new MultiPivotQuicksort(any2, new HolierThanThouPartitioner2(),  isort2, 64));
    sorts.add( new MultiPivotQuicksort(any3, new KLQMPartitioner3(),            isort2, 64));
    sorts.add( new MultiPivotQuicksort(any3, new HolierThanThouPartitioner3(),  isort2, 64));
    sorts.add( new MultiPivotQuicksort(any3, new HolierThanThou3MetamorphicPartitioner(),  isort2, 64));
    sorts.add( new MultiPivotQuicksort(any3, new CentripetalPartitioner3(),     isort2, 64));
    sorts.add( new MultiPivotQuicksort(any3, new CentrifugalPartitioner3(),     isort2, 64));
		
    sortTest.testSpecificSorts( sorts, 10, 10000, 400, 10000000, 25  );
  }
  public void testGeometricPartitioner3() {
    //Selectors
    MultiPivotSelector     any3         = new SamplingMultiPivotSelector(3,   false);
    MultiPivotSelector     any7         = new SamplingMultiPivotSelector(7,   false);
    MultiPivotSelector     any15        = new SamplingMultiPivotSelector(15,  false);
    MultiPivotSelector     any31        = new SamplingMultiPivotSelector(31,  false);
    MultiPivotSelector     any63        = new SamplingMultiPivotSelector(31,  false);
    MultiPivotSelector     any127       = new SamplingMultiPivotSelector(127, false);

    //Partitioners
    GeometricPartitioner3  pgeo3        = new GeometricPartitioner3();
    GeometricPartitioner4  pgeo4        = new GeometricPartitioner4();
    GeometricPartitioner7  pgeo7        = new GeometricPartitioner7();

    //Janitors
	RangeSorter            isort2       = new InsertionSort2Way();
	RangeSorter            gp3          = new MultiPivotQuicksort(any3, pgeo3, isort2, 64);

	SortList sorts        = new SortList();
	sorts.add( new MultiPivotQuicksort(any3,    pgeo3, isort2, 64));
	sorts.add( new MultiPivotQuicksort(any7,    pgeo3, isort2, 64));
	sorts.add( new MultiPivotQuicksort(any15,   pgeo3, isort2, 64));
	sorts.add( new MultiPivotQuicksort(any31,   pgeo3, isort2, 64));
	sorts.add( new MultiPivotQuicksort(any63,   pgeo3, isort2, 64));
	sorts.add( new MultiPivotQuicksort(any127,  pgeo3, gp3,    512));
	sorts.add( new MultiPivotQuicksort(any127,  pgeo4, gp3,    512));
	sorts.add( new MultiPivotQuicksort(any127,  pgeo7, gp3,    512));
	
	sortTest.testSpecificSorts( sorts, 10, 10000, 400, 1000000, 25  );		
  }
  public void testHolierThanThou3MOnce() {
    MultiPivotSelector    spp = new SamplingMultiPivotSelector(3, false);
    MultiPivotPartitioner mpp = new HolierThanThou3MetamorphicPartitioner();
    usePartitionerOnce(53, spp, mpp);		
  }
  @Test
  public void test3PivotPartitionersWithUniformSampling() {
    ArrayList<MultiPivotSelector> selectors = new ArrayList<MultiPivotSelector>();
    for (int s=3; s<=5; s+=2) {
      selectors.add(new SamplingMultiPivotSelector(s*3+2, false));
    }
    ArrayList<MultiPivotPartitioner> partitioners = new ArrayList<MultiPivotPartitioner>();
    partitioners.add( new KLQMPartitioner3());
    partitioners.add( new OutsideInPartitioner3());
    partitioners.add( new HolierThanThouPartitioner3());
    partitioners.add( new CentrifugalPartitioner3());
    partitioners.add( new CentripetalPartitioner3());
    partitioners.add( new GeometricPartitioner3());
    partitioners.add( new SkippyPartitioner3());
    partitioners.add( new KangarooPawPartitioner3());
    sortTest.testSelectorsAndPartitioners
      ( selectors, partitioners
      , new int[] { 8192, 8192, 1048576, 33554432 } );
  }
  public void testSkewedPivotsForThreePivotPartitioners() {
    ArrayList<MultiPivotPartitioner> partitioners = new ArrayList<MultiPivotPartitioner>();
    partitioners.add( new KLQMPartitioner3());
    partitioners.add( new OutsideInPartitioner3());
    partitioners.add( new HolierThanThouPartitioner3());
    partitioners.add( new CentrifugalPartitioner3());
    partitioners.add( new CentripetalPartitioner3());
    partitioners.add( new GeometricPartitioner3());
		
    trySkewedPivots( 3, 20, partitioners, new int[] { 8192, 1048576 } );		
  }
}
