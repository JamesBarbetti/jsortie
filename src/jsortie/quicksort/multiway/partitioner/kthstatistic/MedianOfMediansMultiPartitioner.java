package jsortie.quicksort.multiway.partitioner.kthstatistic;

import jsortie.RangeSorter;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.quicksort.multiway.expander.MultiPivotPartitionExpander;
import jsortie.quicksort.multiway.expander.kangaroo.SkippyExpander1A;
import jsortie.quicksort.multiway.expander.kangaroo.SkippyExpander2;
import jsortie.quicksort.multiway.expander.kangaroo.SkippyExpander3;
import jsortie.quicksort.multiway.partitioner.decorator.BirdsOfAFeatherPartitioner;
import jsortie.quicksort.multiway.partitioner.kangaroo.SkippyPartitioner3;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.selector.dirty.DirtySelectorHelper;

public class MedianOfMediansMultiPartitioner 
  implements KthStatisticPartitioner
           , KthStatisticMultiPartitioner {
  protected DirtySelectorHelper 
    helper = new DirtySelectorHelper();
  protected MultiPivotPartitionExpander[]  
    xpanda = new MultiPivotPartitionExpander[] {
    new SkippyExpander1A()
    , new SkippyExpander2()
    , new SkippyExpander3()
  };
  protected int threshold = 25;
  protected RangeSorter 
    janitor = new InsertionSort2Way();
  protected MultiPivotUtils
    utils = new MultiPivotUtils();
  protected SkippyPartitioner3
    kp3 = new SkippyPartitioner3();
  protected BirdsOfAFeatherPartitioner
    feather = new BirdsOfAFeatherPartitioner(kp3);
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start, int stop
    , int targetIndex ) {
    partitionRangeExactly ( vArray, start, stop
                          , new int[] { targetIndex }, 0, 1);
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start, int stop, int[] indices ) {
    partitionRangeExactly ( vArray, start, stop
                          , indices, 0, indices.length);
  }
  
  public void partitionRangeExactly 
    ( int[] vArray,   int start,  int stop
    , int[] indices, int tStart, int tStop ) {
    while (this.threshold<stop-start) {
      //Any indices outside this range don't matter.
      while (indices[tStart]<start) {
        ++tStart;
        if (tStart==tStop) {
          return;
        }
      }
      while (stop<=indices[tStop-1]) {
        --tStop;
        if (tStart==tStop) {
          return;
        }
      }
      //Partition, aiming for approximately the indices 
      //indicated by indices[tStart..(tStop-1)].
      int[] partitions 
        = partitionRangeApproximately
          ( vArray, start, stop, indices, tStart, tStop );
      /*
      String s ="Partitioned [" + start + ".." + (stop-1) + "] into "
       + (partitions.length/2) + " partitions, after " 
       + comparisons + " comparisons: [";
      for (int i = 0; i<partitions.length; ++i ) {
        s += (( 0<i ) ? "," : "");
        s += partitions[i];
      }
      System.out.println(s +  "]");
      */
      
      //We'll continue with the largest partition
      int largest 
        = utils.indexOfLargestPartition(partitions);
      start = partitions[largest];
      stop  = partitions[largest+1];

      //System.out.println("Largest partition [" 
      //  + start + ".." + (stop-1) + "]");
      //Recurse for smaller partitions
      for (int i=0; i<partitions.length; i+=2) {
        if (i!=largest) {
          partitionRangeExactly 
            ( vArray, partitions[i], partitions[i+1]
            , indices, tStart, tStop );
        }
      }
    }
    if (start<=indices[tStart] && indices[tStop-1]<stop) {
      //wstComparisons += (stop-start)*(stop-start-1)/4;
      //avgComparisons += (stop-start)*(stop-start-1)/8;
      janitor.sortRange(vArray, start, stop);
      //System.out.println("janitor on [" + start 
      //+ ".." + (stop-1) + "] after " + comparisons);
    }
  }
  public int[] partitionRangeApproximately 
    ( int[] vArray,   int start,  int stop
    , int[] indices, int tStart, int tStop ) {
    //assumes: 25 < stop-start
    int step       = (stop-start) / 5;
    int twoStep    = step + step;
    int innerStart = start + twoStep 
                     + ( (stop-start) % step) / 2;
    int innerStop  = innerStart + step;
    for (int m = innerStart; m<innerStop; ++m) {
      helper.branchAvoidingFindMedianOf5 
        ( vArray, m - twoStep, m-step, m
        , m+step, m+twoStep );
    } 
    //wstComparisons += 6*step;
    //avgComparisons += 5.6*step;
    int[] innerIndices = new int [ 3 ];
    int target = innerStart + step / 4;
    for (int i=0; i<3; ++i, target += step/4) {
      innerIndices[i] = target; 
    }
    partitionRangeExactly
      ( vArray, innerStart, innerStop
      , innerIndices, 0, 3 );
    int[] partitions = xpanda[2].expandPartitions
      ( vArray, start, innerStart
      , innerIndices, innerStop, stop);
    /*
    System.out.println("Expanded 4-way partition of [" 
      + innerStart + ".." + (innerStop-1) + "]"
      + " to cover [" + start + ".." + (stop-1) + "]"
      + " after " + comparisons + " comparisons");
    */
    //wstComparisons += 12*step;
    //avgComparisons += 12*step;
    return partitions; 
    //feather.shrinkPartitions(array, partitions);
  }
}
