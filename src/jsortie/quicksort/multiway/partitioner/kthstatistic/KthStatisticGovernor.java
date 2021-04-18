package jsortie.quicksort.multiway.partitioner.kthstatistic;

import jsortie.quicksort.discriminator.Discriminator;
import jsortie.quicksort.discriminator.SpecificPivotDiscriminator;
import jsortie.quicksort.discriminator.UniformPivotDiscriminator;
import jsortie.quicksort.expander.branchavoiding.LeftSkippyExpander;
import jsortie.quicksort.expander.branchavoiding.RightSkippyExpander;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.remedian.RemedianPartitioner;

public class KthStatisticGovernor
  implements KthStatisticMultiPartitioner {
  protected MultiPivotSelector      selector;
  protected MultiPivotPartitioner   partitioner;
  protected MultiPivotUtils         utils;
  protected KthStatisticPartitioner lastResort;
  public KthStatisticGovernor
    ( MultiPivotSelector selectorToUse
    , MultiPivotPartitioner partitionerToUse) {
    selector    = selectorToUse;
    partitioner = partitionerToUse;
    utils       = new MultiPivotUtils();
    lastResort  
      = new RemedianPartitioner
            ( new LeftSkippyExpander()
            , new RightSkippyExpander()
            , 2, false);
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start, int stop
    , int[] targetIndices) {
    Discriminator d 
      = new SpecificPivotDiscriminator
            ( targetIndices );
    int maxDepth 
      = 2 * (int) Math.floor
        ( Math.log ( stop - start ) / Math.log(2));
    processRange 
      ( d, vArray, -1, start, stop
      , -1, maxDepth);
  }
  public void partitionRangeUniformly
    ( int[] vArray, int start, int stop
    , int firstPivotIndex, int pivotStep) {
    Discriminator d 
      = new UniformPivotDiscriminator
            ( firstPivotIndex, pivotStep );
    int maxDepth 
      = 2 * (int) Math.floor
        ( Math.log(stop - start) / Math.log(2) );
    processRange
      ( d, vArray, -1, start, stop
      , -1, maxDepth);
  }
  public void processRange
    ( Discriminator d, int[] vArray
    , int lastBoundary, int start, int stop
    , int nextBoundary, int maxDepth) {
    while (d.ofInterest(start, stop)) {
      int[] partitions;
      if (maxDepth<1) {
        int targetIndex 
          = d.getTargetIndex(start,stop);
        lastResort.partitionRangeExactly
          ( vArray, start, stop, targetIndex );
        partitions 
          = new int[] { start, targetIndex
                      , targetIndex+1, stop };
      } else {
        int[] indices 
          = selector.selectPivotIndices
            ( vArray, start, stop );
        partitions 
          = partitioner.multiPartitionRange
            ( vArray, start, stop, indices );
        if (partitions.length == 0) {
          return;
        }
      }
      int i = utils.indexOfLargestPartition
              ( partitions );
      --maxDepth;
      //Recursively process all non-empty partitions, 
      //except the largest
      for (int j = 0; j < partitions.length; j += 2) {
        if (i != j && partitions[j] < partitions[j + 1]) {
          int leftBoundary = lastBoundary;
          int rightBoundary = nextBoundary;
          if ( 0 < j 
               && partitions[j - 1] < partitions[j]
               && start < partitions[j]) {
            leftBoundary = partitions[j] - 1;
          }
          if (  j + 2 < partitions.length 
               && partitions[j + 1] < partitions[j + 2]
               && partitions[j + 1] < stop) {
            rightBoundary = partitions[j + 1];
          }
          if ( leftBoundary == -1 
               || rightBoundary == -1 
               || vArray[leftBoundary] 
                  < vArray[rightBoundary]) {
            //If the items in the range
            //don't all compare equal
            try {
              d.pushState();
              processRange ( d, vArray, leftBoundary
                           , partitions[j], partitions[j + 1]
                           , rightBoundary, maxDepth );
            } finally {
              d.popState();
            }
          } // worth sub-partitioning partition j
        } // considered partition j
      } // done the smaller partitions
      // Tail-recurse to sort the largest partition
      // (if it's got two or more elements in it)
      if (partitions[i + 1] <= partitions[i] + 1) {
        return;
      }
      lastBoundary = start < partitions[i] 
        ? (partitions[i] - 1) 
        : lastBoundary;
      nextBoundary = partitions[i + 1] < stop 
        ? (partitions[i + 1])
        : nextBoundary;
      start = partitions[i];
      stop = partitions[i + 1];
      if ( lastBoundary != -1 
           && nextBoundary != -1 
           && vArray[lastBoundary] 
              == vArray[nextBoundary]) {
        //if the Items in the range all compare equal
        return;
      }
    } // while stuff's interesting
  }
}
