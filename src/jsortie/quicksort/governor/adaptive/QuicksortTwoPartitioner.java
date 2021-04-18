package jsortie.quicksort.governor.adaptive;

import jsortie.RangeSorter;
import jsortie.earlyexitdetector.RangeSortEarlyExitDetector;
import jsortie.earlyexitdetector.TwoWayInsertionEarlyExitDetector;
import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.janitors.exchanging.BranchAvoidingAlternatingCombsort;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.partitioner.lomuto.RevisedLomutoMirrorPartitioner;
import jsortie.quicksort.partitioner.lomuto.RevisedLomutoPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;
import jsortie.quicksort.selector.clean.CleanTheoreticalSelector;

public class QuicksortTwoPartitioner extends QuicksortAdaptive {
  protected SinglePivotPartitioner[] partitioners;
  static protected SinglePivotSelector defaultSelector
    = new CleanTheoreticalSelector(0.5); 
  static protected RangeSorter defaultJanitor
    = new BranchAvoidingAlternatingCombsort(1.4, 256);
  static protected int defaultJanitorThreshold = 512;
  static protected RangeSorter defaultLastResort
    = new TwoAtATimeHeapsort();
  static protected RangeSortEarlyExitDetector defaultEED 
    = new TwoWayInsertionEarlyExitDetector();
  public String toString() {
    String partitionerList = "";
    String sep = "";
    for ( SinglePivotPartitioner party : partitioners ) {
      partitionerList += sep + party.toString();
      sep = ", ";
    }
    return this.getClass().getSimpleName()
      + "(" + selector.toString() + ", {" + partitionerList + "}"
      + ", " + janitor.toString() + ", " + lastResort.toString() 
      + ", " + detector.toString() + ")";
  }
  @Override
  protected SinglePivotPartitioner choosePartitioner
    ( int start, int stop, int maxDepth) {
    return partitioners[ maxDepth % partitioners.length];
  }	
  public QuicksortTwoPartitioner
    ( SinglePivotSelector singlePivotSelector
    , SinglePivotPartitioner[] parties
    , RangeSorter smallRangeSort
    , int janitorThresholdToUse
    , RangeSorter lastResortSort
    , RangeSortEarlyExitDetector eed) {
    super ( singlePivotSelector, parties[0], smallRangeSort
        , janitorThresholdToUse
        , lastResortSort, eed);
    this.partitioners = parties;
  }	  
  public QuicksortTwoPartitioner(SinglePivotPartitioner[] partitionersToUse) {
    super ( defaultSelector, partitionersToUse[0]
    	  , defaultJanitor, defaultJanitorThreshold
    	  , defaultLastResort, defaultEED );
   this.partitioners = partitionersToUse;
  }
  public QuicksortTwoPartitioner() {
    super ( defaultSelector, new RevisedLomutoPartitioner()
          , defaultJanitor, defaultJanitorThreshold
    	  , defaultLastResort, defaultEED );
    this.partitioners = new SinglePivotPartitioner[] 
      { this.partitioner, new RevisedLomutoMirrorPartitioner() };
  }
}
