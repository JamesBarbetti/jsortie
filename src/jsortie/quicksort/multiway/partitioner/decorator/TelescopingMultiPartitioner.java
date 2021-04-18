package jsortie.quicksort.multiway.partitioner.decorator;

import jsortie.helper.RangeSortHelper;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;

public class TelescopingMultiPartitioner
  implements MultiPivotPartitioner {
  protected MultiPivotPartitioner inner;
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + inner.toString() + ")";
  }
  public TelescopingMultiPartitioner
    ( MultiPivotPartitioner basePartitioner ) {
    inner = basePartitioner;
  }
  @Override
  @Deprecated
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices) {
    int minIndex = pivotIndices[0];
    int maxIndex = minIndex;
    for (int i=1; i<pivotIndices.length; ++i) {
      if (pivotIndices[i]<minIndex) {
        minIndex = pivotIndices[i];
      } else if (maxIndex<pivotIndices[i]) {
        maxIndex = pivotIndices[i];
      }
    }
    int innerStart = start;
    int innerLast  = stop-1;
    int vMinPivot  = vArray[pivotIndices[0]];
    while ( innerStart < minIndex && 
            vArray[innerStart] <= vMinPivot) {
      ++innerStart;
    }
    int vMaxPivot 
    = vArray[pivotIndices[pivotIndices.length-1]];
    while ( maxIndex < innerLast && 
            vMaxPivot <= vArray[innerLast]) {
      --innerLast;
    }
    int innerStop = innerLast+1;
    int[] partitions 
      = inner.multiPartitionRange
        ( vArray, innerStart, innerStop
        , pivotIndices);
    int partitionCount = partitions.length;
    if ( partitions[0] == innerStart ) {
      if ( partitions[partitionCount-1]
           == innerStop ) {
        //extend the outer partitions
        partitions[0] = start;
        partitions[partitions.length-1] = stop;
        return partitions;
      } else {
        //need to add one partition on the right
        partitions[0] = start;
        int[] fluffy 
          = new int[partitions.length+2];
        RangeSortHelper.copyRange
          ( partitions, 0, partitionCount
          , fluffy, 0 );
        fluffy[partitionCount]   = innerStop; 
        fluffy[partitionCount+1] = stop;
        return fluffy;
      }
    } else if ( partitions[partitionCount-1]
                == innerStop) {
      //need to add one partition on the left
      partitions[partitionCount-1] = stop;
      int[] fluffy 
        = new int[partitions.length+2];
      fluffy[0] = start;
      fluffy[1] = innerStart;
      RangeSortHelper.copyRange
        ( partitions, 0, partitionCount
        , fluffy, 2 );
      return fluffy;
    } else {
      //need to add a partition on the left,
      //and another on the right
      int[] fluffy = new int[partitions.length+4];
      fluffy[0] = start;
      fluffy[1] = innerStart;
      RangeSortHelper.copyRange
        ( partitions, 0, partitionCount
        , fluffy, 2 );
      fluffy[partitionCount]   = innerStop; 
      fluffy[partitionCount+1] = stop;
      return fluffy;
    }
  }
}
