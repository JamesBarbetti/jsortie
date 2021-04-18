package jsortie.quicksort.multiway.governor;

import jsortie.RangeSorter;
import jsortie.earlyexitdetector.RangeSortEarlyExitDetector;
import jsortie.earlyexitdetector.TwoWayInsertionEarlyExitDetector;
import jsortie.heapsort.topdown.TwoAtATimeHeapsort;
import jsortie.quicksort.multiway.partitioner.AdaptivePartitioner;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.partitioner.interfaces.StandAlonePartitioner;

public class AdaptiveMultiPivotQuicksort
  extends MultiPivotQuicksort {
  protected RangeSorter                lastResort;
  protected RangeSortEarlyExitDetector detector  ;
  protected MultiPivotUtils            utils     ;
  public void setDefaultHelpers() {
    lastResort = new TwoAtATimeHeapsort();
    detector   = new TwoWayInsertionEarlyExitDetector();
    utils      = new MultiPivotUtils();
  }
  public AdaptiveMultiPivotQuicksort 
    ( MultiPivotSelector selector
    , MultiPivotPartitioner partitioner
    , RangeSorter rangeSorter
    , int janitorThreshold) {
    super ( selector
          , new AdaptivePartitioner(partitioner)
          , rangeSorter, janitorThreshold);    
    setDefaultHelpers();
  }
  public AdaptiveMultiPivotQuicksort 
    ( MultiPivotSelector selector
    , MultiPivotPartitioner partitioner
    , RangeSorter janitor
    , int janitorThreshold
    , RangeSorter lastResortSort
    , RangeSortEarlyExitDetector detector) {
    super ( selector
          , new AdaptivePartitioner(partitioner)
          , janitor, janitorThreshold);
    setDefaultHelpers();
    this.lastResort = lastResortSort;
    this.detector   = detector;
  }
  public AdaptiveMultiPivotQuicksort 
    ( StandAlonePartitioner party
    , RangeSorter rangeSorter
    , int janitorThreshold) {
    super(party, rangeSorter, janitorThreshold);  
    setDefaultHelpers();
  }
  @Override
  public String toString() {
    return getClass().getSimpleName() + "( "
         + party.toString() + ", "
         + janitor.toString() + ", " 
         + janitorThreshold + ", "
         + lastResort.toString()+ " )";
  }
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    if (start+1<stop) {
      double maxDepth = Math.log(stop-start)
                      / Math.log(2) *3.0;
      adaptiveSortRange
        ( vArray, -1, start, stop, -1
        , (int) Math.floor(maxDepth) );
    }
  }
  protected void adaptiveSortChildRange 
    ( int[] vArray, int lastBoundary
    , int start, int stop
    , int nextBoundary, int maxDepth) {
    adaptiveSortRange ( vArray, lastBoundary
                      , start, stop
                      , nextBoundary, maxDepth);
  }
  protected void adaptiveSortRange 
    ( int[] vArray, int lastBoundary
    , int start, int stop
    , int nextBoundary, int maxDepth) {
    while (janitorThreshold < stop-start) {
      if ( maxDepth < 1 ) {
        lastResort.sortRange(vArray, start, stop);
        return;
      } else if ( detector.exitEarlyIfSorted
                  ( vArray, start, stop ) ) {
        return;
      }
      int[] partitions = party.multiPartitionRange
                         (vArray, start, stop);
      if (partitions.length==0) {
        return;
      }
      int i 
        = utils.indexOfLargestPartition
          ( partitions );
      --maxDepth;
      //Recursively sort all non-empty 
      //partitions, except the largest
      for (int j=0; j<partitions.length; j+=2) {
        if (i != j && partitions[j] < partitions[j+1]) {
          int leftBoundary  = lastBoundary;
          int rightBoundary = nextBoundary;
          if ( 0<j && partitions[j-1]<partitions[j]
                   && start<partitions[j]) {
            leftBoundary = partitions[j]-1;
          }
          if ( j+2<partitions.length 
               && partitions[j+1]<partitions[j+2] 
               && partitions[j+1]<stop) {
            rightBoundary = partitions[j+1];
          }
          if ( leftBoundary==-1 || rightBoundary==-1 
               || vArray[leftBoundary] 
                  < vArray[rightBoundary]) {
            adaptiveSortChildRange 
              ( vArray, leftBoundary
              , partitions[j], partitions[j+1]
              , rightBoundary, maxDepth);
          }
        }
      }
			
      //Tail-recurse to sort the largest
      //(if it's got two or more elements in it)
      if ( partitions[i+1] <= partitions[i] + 1 ) {
        return;
      }
      lastBoundary = start<partitions[i] 
                   ? (partitions[i]-1) : lastBoundary;
      nextBoundary = partitions[i+1]<stop 
                   ? (partitions[i+1]) : nextBoundary;
      start        = partitions[i];
      stop         = partitions[i+1];
      if ( lastBoundary!=-1 && nextBoundary!=-1 
           && vArray[lastBoundary]
              ==vArray[nextBoundary]) {
        return;
      }
    }		
    janitor.sortRange(vArray, start, stop);
  }
}
