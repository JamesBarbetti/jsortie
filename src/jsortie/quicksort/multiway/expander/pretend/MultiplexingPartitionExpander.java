package jsortie.quicksort.multiway.expander.pretend;

import jsortie.quicksort.multiway.expander.MultiPivotPartitionExpander;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class MultiplexingPartitionExpander 
  implements MultiPivotPartitionExpander {
  SinglePivotPartitioner left;
  SinglePivotPartitioner right;
  public MultiplexingPartitionExpander
    ( SinglePivotPartitioner leftPartitioner
    , SinglePivotPartitioner rightPartitioner ) {
    left  = leftPartitioner;
    right = rightPartitioner;
  }
  @Override
  public int[] expandPartitions
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int startRight, int stop) {
    if ( ! MultiPivotUtils.arePivotsDistinct
           ( vArray, pivotIndices ) ) {
      return ternaryExpandPartitions
             ( vArray, start, stopLeft
             , pivotIndices, startRight, stop);
    }
    return null;
  }
  public int[] ternaryExpandPartitions
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int startRight, int stop) {
    return null;
  }
}
