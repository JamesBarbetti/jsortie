package jsortie.quicksort.multiway.partitioner.decorator;

import jsortie.helper.EgalitarianPartitionerHelper;
import jsortie.quicksort.multiway.expander.MultiPivotPartitionExpander;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.adapter.MultiPivotExpanderToPartitioner;

public class BirdsOfAFeatherExpander 
  extends BirdsOfAFeatherPartitioner 
  implements MultiPivotPartitionExpander {
  protected MultiPivotPartitionExpander expander;
  EgalitarianPartitionerHelper helper 
    = new EgalitarianPartitionerHelper(); 
  public String toString() {
    return this.getClass().getSimpleName()
           + "(" + expander.toString() + ")";
  }
  public BirdsOfAFeatherExpander 
    ( MultiPivotPartitionExpander innerExpander ) {
    super(new MultiPivotExpanderToPartitioner(innerExpander));
    expander = innerExpander;
  }
  public BirdsOfAFeatherExpander 
    ( MultiPivotPartitionExpander innerExpander
    , MultiPivotPartitioner innerPartitioner) {
	super(innerPartitioner);
    expander = innerExpander;
  }
  @Override
  public int[] expandPartitions 
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int startRight, int stop) {
    int[] partitions 
      = expander.expandPartitions 
        ( vArray, start, stopLeft
        , pivotIndices, startRight, stop);
    return shrinkPartitions(vArray, start, stop, partitions);
  }
}
