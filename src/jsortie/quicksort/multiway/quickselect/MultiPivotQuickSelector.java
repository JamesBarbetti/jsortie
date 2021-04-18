package jsortie.quicksort.multiway.quickselect;

import jsortie.RangeSorter;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;
import jsortie.quicksort.selector.SinglePivotSelector;

public class MultiPivotQuickSelector 
	implements SinglePivotSelector, SinglePivotPartitioner, 
			MultiPivotSelector, MultiPivotPartitioner, KthStatisticPartitioner
{
  protected MultiPivotSelector    innerSelector;
  protected MultiPivotPartitioner innerPartitioner;
  protected int                   pivotCount;
  protected RangeSorter           smallRangeSorter;
	
  @Override public String toString() {
    return this.getClass().getSimpleName() + "("
           + innerSelector.toString() + ", "
           + pivotCount + ", "
           + innerPartitioner.toString() + ")";
  }
	
  public MultiPivotQuickSelector
    ( MultiPivotSelector selector        
    , MultiPivotPartitioner partitioner
    , int maxPivotCount) {
    innerSelector    = selector;
    innerPartitioner = partitioner;
    pivotCount       = maxPivotCount;
    smallRangeSorter = new InsertionSort2Way();
  }
	
  public int[] chooseTargetIndices(int start, int stop) {
    if (stop-start < pivotCount) pivotCount = stop-start;
    int indices[] = new int [ pivotCount ];
    double step = (double)(stop-start) 
                / (double)(pivotCount+1);
    for (int i=0; i<pivotCount; ++i) {
      indices[i] 
        = (int)Math.floor((double)(i+1) * step +.5);
    }
    return indices;
  }
	
  @Override
  public int[] selectPivotIndices
    ( int[] vArray, int start, int stop ) {
    int indices[]  = chooseTargetIndices(start, stop);
    multiPartitionRangeExactly
      ( vArray, start, stop, indices, 0, indices.length );
    return indices;
  }

  @Override
  public int selectPivotIndex(int[] vArray, int start, int stop) {
    int indices[] = new int[] { start + (stop-start)/2 };
    multiPartitionRangeExactly(vArray, start, stop, indices, 0, 1);
    return indices[0];
  }

  public void multiPartitionRangeExactly
    ( int[] vArray, int start, int stop 
    , int[] indices
    , int startPivotIndex, int stopPivotIndex) {
    while (pivotCount<stop-start) {
      int pivotsIn[]        
        = innerSelector.selectPivotIndices
          ( vArray, start, stop );
      int childPartitions[] 
        = innerPartitioner.multiPartitionRange
          ( vArray, start, stop, pivotsIn );
      int partitionCount    = childPartitions.length/2;
      int p; //pivot found
      int q=startPivotIndex; //pivot desired
			
      //For each partition that we got, figure out which desired indices are in it
      //(and, therefore, can't have been placed yet), and... 
      //recurse for that partition (*unless*... it's a large partition)
      int fatPartition[] = null; //tracks fat partition, if we found one
			
      for ( p=0; p<partitionCount && q<stopPivotIndex; ++p) {
        int partitionStart = childPartitions[p+p];
        int partitionStop  = childPartitions[p+p+1];
        while ( indices[q] < partitionStart ) {
          ++q;
          if (q==stopPivotIndex) { 
            return;
          }
        }
        int q0 = q;
        while ( indices[q] < partitionStop ) {
          ++q;
          if (q==stopPivotIndex) {
            break;
          }
        }
        if (q0<q) {
          if ( (partitionStop - partitionStart)*2 <= (stop-start)) {
            multiPartitionRangeExactly
            ( vArray, partitionStart
            , partitionStop, indices, q0, q);
          } else {
            //save this for later; we'll tail recurse instead
            fatPartition 
              = new int[] { partitionStart
                          , partitionStop, q0, q };
          }
        }
      }
      if (fatPartition==null) {
        return;
      }
      //tail recurse, using the details saved in fatPartition
      start = fatPartition[0];
      stop  = fatPartition[1];
      startPivotIndex = fatPartition[2];
      stopPivotIndex  = fatPartition[3];
    }
    smallRangeSorter.sortRange(vArray, start, stop);
  }
  @Override
  public int[] multiPartitionRange 
    ( int[] vArray, int start, int stop
    , int[] pivotIndices) {
    int indices[]  = pivotIndices;
    multiPartitionRangeExactly
      ( vArray, start, stop, indices
      , 0, indices.length);
    return indices;
  }
  @Override
  public int partitionRange
    ( int[] vArray, int start, int stop
    , int pivotIndex) {
    int indices[] = new int[] { pivotIndex };
    multiPartitionRangeExactly
      ( vArray, start, stop, indices, 0, 1 );
    return indices[0];
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start, int stop
   , int targetIndex) {
    int indices[] = new int[] { targetIndex };
    multiPartitionRangeExactly
      ( vArray, start, stop, indices
      , 0, indices.length);    
  }
}
