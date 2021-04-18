package jsortie.quicksort.multiway.tracer;

import javax.management.RuntimeErrorException;

import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.tracer.Traced;

public class TracedMultiPivotPartitioner 
  extends Traced<MultiPivotPartitioner>
  implements MultiPivotPartitioner {
  public TracedMultiPivotPartitioner
    ( MultiPivotPartitioner innerObject ) {
    super(innerObject);
  }

  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    boolean threw = false;
    int[] result  = null;
    String whatNow 
       = "range [" + start + ".." + (stop-1) + "]"
      + " using pivots ";
    if ( 0 < pivotIndices.length ) {
      for (int i=0; i<pivotIndices.length; ++i) {
        whatNow += (0<i) ? ", " : "";
        whatNow += "[" + pivotIndices[i] + "]=" 
            + vArray[pivotIndices[i]];
      }
  }
  traceEntry("partitioning " + whatNow);
  try {
      result = inner.multiPartitionRange(vArray, start, stop, pivotIndices);
  } catch (RuntimeErrorException e) {
      threw = true;
      traceException( inner.toString() + ".sortRange", e);
    } finally {
      if (threw) {
        whatNow +=" failed";  
      } else if (result==null) {
        whatNow +=" returned null";
      } else {
        whatNow = "partitioned " + whatNow + ": into ";
        if ( 0 < result.length ) {
          for (int i=0; i<result.length-1; i+=2) {
    	    whatNow += (0<i) ? ", " : "";
            whatNow +=" [" + result[i] + ".." + (result[i+1]-1) + "]";
            if (i+3 < result.length && result[i+1] < result[i+3] ) {
              whatNow += " pivot" + vArray[result[i+1]];
            }
          }
        }
      }
    }
    System.out.println(whatNow);
    return result;
  }  
}
