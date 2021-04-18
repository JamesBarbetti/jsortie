package jsortie.quicksort.tracer;

import javax.management.RuntimeErrorException;

import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;

public class TracedKthStatisticPartitioner 
  extends Traced<KthStatisticPartitioner> 
  implements KthStatisticPartitioner {
  public TracedKthStatisticPartitioner(KthStatisticPartitioner innerObject) {
   super(innerObject);
  }

  @Override
  public void partitionRangeExactly(int[] vArray, int start, int stop, int targetIndex) {
    boolean threw = false;
	String range = " range [" + start + ".." + (stop-1) + "]\n  into"
			+ " partitions [" + start + ".." + (targetIndex-1) + "] " 
			+ " and [" + (targetIndex+1) + ".." + stop + "]";
	traceEntry( prefix("partitionRangeExactly") + range);
	try {
	  inner.partitionRangeExactly(vArray, start, stop, targetIndex);
	} catch (RuntimeErrorException e) {
      threw = true;
      traceException( prefix("partitionRangeExactly"), e);
    } finally { 	
	  traceExit( prefix("partitionRangeExactly") 
        + (threw ? "failed to partition" : "finished partitioning") 
        + range 
        + (threw ? "" : ( "; pivot was " + vArray[targetIndex])));
    }
  }
}
