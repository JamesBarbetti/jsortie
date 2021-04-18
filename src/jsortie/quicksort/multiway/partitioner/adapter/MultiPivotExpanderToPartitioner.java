package jsortie.quicksort.multiway.partitioner.adapter;

import jsortie.quicksort.multiway.expander.MultiPivotPartitionExpander;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class MultiPivotExpanderToPartitioner 
  implements MultiPivotPartitioner {
  protected MultiPivotPartitionExpander expander;
  public MultiPivotExpanderToPartitioner
    (MultiPivotPartitionExpander innerExpander) {
    expander = innerExpander;
  }
  public String toString() {
    return this.getClass().getSimpleName() 
           + "(" + expander.toString() + ")";
  }
		
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices) {
    int pivotCount = pivotIndices.length;
    int[] middleIndices = new int[pivotCount];
    int middleStart = start + (stop - start - pivotCount) / 2;
    for (int i=0; i<middleIndices.length; ++i) {
      i = middleStart + i;
    }
    MultiPivotUtils.movePivotsAside
    ( vArray, pivotIndices, middleIndices );
    int boundaryCount = pivotCount + pivotCount + 2;
    int[] emptyPartitions = new int[boundaryCount];
    for (int i=0; i<pivotCount; ++i) {
      emptyPartitions[i+i] = (i==0) ? start : pivotIndices[i];
      emptyPartitions[i+1] = pivotIndices[i];
    }
    int middleStop = middleStart + pivotCount;
    emptyPartitions[boundaryCount-2] = middleStop;
    emptyPartitions[boundaryCount-1] = stop;
    return expander.expandPartitions 
           ( vArray, start, middleStart
           , middleIndices, middleStop, stop);
  }
}
