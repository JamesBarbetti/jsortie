package jsortie.quicksort.multiway.partitioner.kthstatistic;

import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.twopivot.YaroslavskiyPartitioner2;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.multiway.selector.clean.CleanMultiPivotPositionalSelector;

public class QuickSelectMultiPartitioner 
  implements KthStatisticMultiPartitioner {
  protected KthStatisticMultiPartitioner 
    lastResort = new MedianOfMediansMultiPartitioner();
  protected MultiPivotPartitioner 
    innerPartitioner  = new YaroslavskiyPartitioner2();
  protected MultiPivotSelector
    innerSelector = new CleanMultiPivotPositionalSelector(2);
  protected MultiPivotUtils 
    utils = new MultiPivotUtils();
  protected int threshold = 32;
  protected InsertionSort2Way isort
    = new InsertionSort2Way();	
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public class Implementation {
    public int    originalStart;
    public int    originalStop;
    public int[]  vArray;
    public int[]  targetIndices;
    public double comparisonsLeft; //this can only ever be a guess.
    public boolean partitionRangeExactly
      ( int start, int stop, int targetStart, int targetStop ) {
      while (threshold<stop-start) {    	
        //1. forget about any target indices that are out of range
        while ( targetStart<targetStop 
                && targetIndices[targetStart]<start) { 
          ++targetStart;
        }
        while ( targetStart<targetStop 
                && stop<=targetIndices[targetStop-1]) {
          --targetStop;
        }
        if (targetStop<=targetStart) { 
          break;
        }
        //
        //2. partition using inner selector 
        //   and inner partitioner
        int[] pivotIndices 
          = innerSelector.selectPivotIndices
            ( vArray, start, stop );
        int[] partitions 
          = innerPartitioner.multiPartitionRange
            ( vArray, start, stop, pivotIndices );
        int p = pivotIndices.length+1;
        comparisonsLeft -= 
          (stop-start)*Math.log(p)/Math.log(2.0);
        if (comparisonsLeft<0) {
          return false;
        }
        //
        //3. Figure out which partition is the largest
        int bigP = utils.indexOfLargestPartition
                   ( partitions );
        //
        //4. recurse for all of the partitions 
        //   other than the largest
        for (p=0;p<partitions.length;p+=2) {
          if (p!=bigP) {
            if (!partitionRangeExactly
                ( partitions[p], partitions[p+1]
                , targetStart, targetStop)) {
              return false;
            }
          }
        }
        //5. tail recurse for the largest 
        //   partition identified in step #3
        start = partitions [ bigP     ];
        stop  = partitions [ bigP + 1 ];
      }
      isort.sortRange(vArray,  start, stop);
      return true;
    }
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start, int stop
    , int[] targetIndices ) {
    int count = stop-start;
    if (count<2) return;
    Implementation imp = new Implementation();
    imp.originalStart  = start;
    imp.originalStop   = stop;
    imp.vArray         = vArray;
    imp.targetIndices  = targetIndices;
    int p = targetIndices.length+1;
    double maxRatio 
      = 10.0*Math.floor(Math.log(p)/Math.log(2));
    imp.comparisonsLeft = count*maxRatio; 
    if (!imp.partitionRangeExactly
          ( start, stop, 0, targetIndices.length ) ) {
      lastResort.partitionRangeExactly
        ( vArray, start, stop, targetIndices );
    }
  }  
}
