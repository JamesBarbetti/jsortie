package jsortie.quicksort.multiway.expander.kangaroo;

import jsortie.quicksort.multiway.expander.MultiPivotPartitionExpander;
import jsortie.quicksort.multiway.expander.pretend.MultiplexingPartitionExpander;
import jsortie.quicksort.partitioner.branchavoiding.SkippyMirrorPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.SkippyPartitioner;

public class QuintarySkippyExpander2 
  extends    MultiplexingPartitionExpander
  implements MultiPivotPartitionExpander {
  public QuintarySkippyExpander2() {
    super ( new SkippyMirrorPartitioner(), new SkippyPartitioner() );
  }
  @Override
  public int[] expandPartitions
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int startRight, int stop) {
    if (2<pivotIndices.length) {
      int[] oldIndices = pivotIndices;
      int third = oldIndices.length/3;
      pivotIndices = new int[] 
        { oldIndices[third]
        , oldIndices[oldIndices.length-1-third] };
    } 
    return super.ternaryExpandPartitions
           ( vArray, start, stopLeft
           , pivotIndices, startRight, stop);
  }
}
