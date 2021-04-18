package jsortie.quicksort.multiway.tracer;

import javax.management.RuntimeErrorException;

import jsortie.quicksort.multiway.expander.MultiPivotPartitionExpander;
import jsortie.quicksort.tracer.Traced;

public class TracedMultiPivotPartitionExpander 
  extends Traced<MultiPivotPartitionExpander>
  implements MultiPivotPartitionExpander {
  public TracedMultiPivotPartitionExpander
    ( MultiPivotPartitionExpander innerObject ) {
    super(innerObject);
  }

  @Override 
  public int[] expandPartitions 
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int startRight, int stop) {
    boolean threw = false;
    int[] partitions = null;
    String s = "Expanding from" 
             + " [" + (stopLeft) + ".." + (startRight-1) + "]"
             + ", which had pivots at [";
    for (int i=0; i<pivotIndices.length; ++i) {
      s += (0<i) ? "," : "";
      s += pivotIndices[i];
    }
    s += "], to cover [" + start + ".." + (stop-1) + "]";
    traceEntry( prefix("expandPartitions") + s);
    try {
      partitions = inner.expandPartitions 
                   ( vArray, start, stopLeft
                   , pivotIndices, startRight, stop);
	} catch (RuntimeErrorException e) {
      threw = true;
      traceException( prefix("expandPartitions"), e);
    } finally {
      traceExit( prefix("expandPartitions") + s
               , threw, partitions);
    }
    return partitions;
  }
}
