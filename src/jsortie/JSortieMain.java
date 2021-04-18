package jsortie;

import jsortie.quicksort.theoretical.Gain;
import jsortie.quicksort.theoretical.PerfectPartitioner;
import jsortie.testing.AsymmetryTest;
import jsortie.testing.DegenerateInputTest;
import jsortie.testing.DualPivotTest;
import jsortie.testing.FourPivotTest;
import jsortie.testing.HistoricalIntegerQuicksorts;
import jsortie.testing.JanitorThresholdTest;
import jsortie.testing.LibrarySortTest;
import jsortie.testing.JanitorTest;
import jsortie.testing.MultiPivotTest;
import jsortie.testing.PartitionerTest;
import jsortie.testing.SampleSortTest;
import jsortie.testing.SinglePivotTest;
import jsortie.testing.Statistics;
import jsortie.testing.ThreePivotTest;
import jsortie.testing.TwoPivotTest;
import jsortie.testing.object.ObjectMergeSortTest;
import jsortie.testing.object.ObjectTreeSortTest;

public class JSortieMain {
  public static void main(String[] args) {
    int thousand = 1000;
    int million = thousand*thousand;
    boolean skip = false;
						
    if (true) {
      if (true) (new PartitionerTest()).testPartitionersHeadToHead();
      if (skip) (new TwoPivotTest()).testTwoPivotSorts();
      if (skip) (new TwoPivotTest()).testTwoPivotSortsWithUniformSampling();
      if (skip) (new ThreePivotTest()).testThreePivotSorts();
      if (skip) (new FourPivotTest()).testFourPivotSorts();
      if (skip) (new ThreePivotTest()).testGeometricPartitioner3();
      if (skip) (new DualPivotTest()).testHolierThanThou2AOnce();
      if (skip) (new DualPivotTest()).testHolierThanThou2();
      if (skip) (new ThreePivotTest()).testHolierThanThou3();
      if (skip) (new MultiPivotTest()).testHolierThanThou7();
      if (skip) (new MultiPivotTest()).testHolierThanThou7Once();
      if (skip) (new DualPivotTest()).testSkewedPivotsForTwoPivotPartitioners();
      if (skip) (new ThreePivotTest()).testSkewedPivotsForThreePivotPartitioners();
      if (skip) (new LibrarySortTest()).testLibrarySortSmall();
      if (skip) (new MultiPivotTest()).testFivePivotPartitioner();
      if (skip) (new MultiPivotTest()).testFucker();
      if (skip) (new DualPivotTest()).test2PivotPartitionersWithUniformSampling();
      if (skip) (new FourPivotTest()).test4PivotPartitionersWithSampling();
      if (skip) (new MultiPivotTest()).test5PivotPartitionersWithSampling();
      if (skip) (new AsymmetryTest()).testLoveChildPartitioner();
      if (skip) (new ObjectMergeSortTest()).testZoneSort();
      if (skip) (new ObjectTreeSortTest()).testTreeSorts();
      if (skip) (new DegenerateInputTest()).testAgainstTimsortRound1();
      if (skip) (new DegenerateInputTest()).testAgainstTimsortRound2();
      if (skip) (new DegenerateInputTest()).testAgainstTimsortRound3();
      if (skip) (new DegenerateInputTest()).testAgainstTimsortRound4();
      if (skip) (new DegenerateInputTest()).testAgainstTimsortRound5();
      if (skip) (new TwoPivotTest()).testYaroslavskiySelectors();
      if (skip) (new TwoPivotTest()).testYaroslavskiyJava8();			
      if (skip) (new DegenerateInputTest()).testMultithreadedQuicksort();			
      if (skip) (new DegenerateInputTest()).testHolierThanThouPartitioners();
      if (skip) (new DegenerateInputTest()).testCentrifugalPartitioners();
      if (skip) (new SampleSortTest()).testSampleSortAnalogue();
      if (skip) (SampleSortTest.newSampleSortTest(million,25)).testSampleSorts();
      if (skip) (SampleSortTest.newSampleSortTest(million,25)).testMultiPivotSampleSorts();
      if (skip) (new JanitorTest()).tryJanitorsHeadToHead(75);
      if (skip) (new JanitorTest()).tryJanitorsHeadToHead(250);
      if (skip) (new JanitorTest()).tryJanitorsHeadToHead(500);
      if (skip) (new JanitorTest()).tryJanitorsHeadToHead(1000);
      if (skip) (new JanitorTest()).tryInsertionSortThresholds();
      if (skip) (new JanitorTest()).insertion150VersusHeapsort1000();			
      if (skip) (new JanitorTest()).testHeapsortVariants();
      if (skip) (new HistoricalIntegerQuicksorts()).testHistoricalIntegerSorts();
      if (skip) (new JanitorTest()).testJanitors();
      if (skip) (new MultiPivotTest()).testMultiPivotSorts();
      if (skip) (new DualPivotTest()).testMultiPivotSelectorCombinations();
      if (skip) (new SinglePivotTest()).testSinglePivotSelectors();
      if (skip) (new JanitorTest()).testStrategicJanitor2();
      if (skip) Gain.guessGain();
      if (skip) (new MultiPivotTest()).test246and8PivotPartitioners();
      if (skip) PerfectPartitioner.guessCost();
      if (skip) PerfectPartitioner.guessCostMoreDetail();
      if (skip) Statistics.samplingHistogram();
      if (skip) (new JanitorThresholdTest()).figureOutThresholdForMerging();
      if (skip) (new AsymmetryTest()).testFigureOutBestValueOfPForQuicksort();
      if (skip) (new AsymmetryTest()).testFigureOutBestValueOfPForQuicksortBig();
      if (skip) (new AsymmetryTest()).testFigureOutBestValueOfPForQuicksortReallyBig();
      //if (true)  SortTest.testRandomInputs(32,32,10,million);
    }
  }
}
