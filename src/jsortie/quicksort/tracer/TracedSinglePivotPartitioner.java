package jsortie.quicksort.tracer;

import javax.management.RuntimeErrorException;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class TracedSinglePivotPartitioner 
  extends Traced<SinglePivotPartitioner>
  implements SinglePivotPartitioner {
  public TracedSinglePivotPartitioner(SinglePivotPartitioner innerObject) {
    super(innerObject);
  }
  @Override
  public int partitionRange(int[] vArray, int start, int stop, int pivotIndex) {
	boolean threw = false;
	int     split = start;
	traceEntry( prefix("partitionRange") 
      + " partitioning vArray[" + start + ".." + (stop-1) + "] "
      + " with vArray[" + pivotIndex + "]=" + vArray[pivotIndex]);
	try {
      split = inner.partitionRange(vArray, start, stop, pivotIndex);
	} catch (RuntimeErrorException e) {
	  threw = true;
	  traceException( prefix("partitionRange"), e);
    } finally {
	  traceExit( prefix("partitionRange") + " partition of [" + start + ".." + (stop-1) + "]"
        + ( threw ? " failed" : (" returned " + split)));
	}
	return split;
  }
}
