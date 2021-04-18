package jsortie.quicksort.multiway.partitioner.kthstatistic;

import jsortie.janitors.insertion.BinaryInsertionSort;
import jsortie.quicksort.collector.SampleCollector;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.kthstatistic.algorithm489.IntroSelect489Partitioner;

public class Algorithm489MultiPartitioner
  extends IntroSelect489Partitioner
  implements KthStatisticMultiPartitioner {
  public Algorithm489MultiPartitioner() {
    super();
  }
  public Algorithm489MultiPartitioner 
    ( SampleCollector collectorToUse
    , PartitionExpander leftpx
    , PartitionExpander rightpx) {
    super ( collectorToUse
          , leftpx, rightpx);
  }
  @Override
  public void partitionRangeExactly 
    ( int[] vArray, int start, int stop
    , int[] targetIndices) {
    Implementation p = getImplementation();
    p.vArray         = vArray;
    p.maxComparisons = (int) Math.floor
      ( 10.0*(stop-start)/Math.log(2)
        * Math.log(targetIndices.length+1));
    partitionWithSomeIndices
      ( vArray, start, stop, p, targetIndices
      , 0, targetIndices.length);
  }
  protected void partitionWithSomeIndices
    ( int[] vArray
    , int arrayStart, int arrayStop
    , Implementation p, int[] targetIndices
    , int targetStart, int targetStop) {
    while (targetStart<targetStop) {
      int arrayMiddle  
        = arrayStart + (arrayStop-arrayStart)/2;
      int targetMiddle 
        = BinaryInsertionSort.findPreInsertionPoint
          ( targetIndices, targetStart, targetStop
          , arrayMiddle);
      if (targetMiddle==targetStop) {
        --targetMiddle;
      } else if (targetStart<targetMiddle 
        && arrayMiddle-targetIndices[targetMiddle-1] 
           < targetIndices[targetMiddle] - arrayMiddle) {
        --targetMiddle;
      }
      int targetInArray = targetIndices[targetMiddle];
      p.start = arrayStart;
      p.stop  = arrayStop;
      PartitionExpander xpanda 
        = (targetInArray - p.start < p.stop - targetInArray)
        ? leftExpander : rightExpander;
      if (!p.isComparisonLimitExceeded()) {
        p.partitionExactly(targetInArray, xpanda);
      }
      if (p.isComparisonLimitExceeded()) {
        lastResortPartitioner.partitionRangeExactly
          ( vArray, arrayStart, arrayStop, targetInArray );
      }
      partitionWithSomeIndices
        ( vArray, arrayStart, targetInArray, p
        , targetIndices, targetStart, targetMiddle);
      targetStart = targetMiddle  + 1;
      arrayStart  = targetInArray + 1;
    }
  }
}
