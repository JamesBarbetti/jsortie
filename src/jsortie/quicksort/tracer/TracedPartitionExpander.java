package jsortie.quicksort.tracer;

import javax.management.RuntimeErrorException;

import jsortie.quicksort.expander.PartitionExpander;

public class TracedPartitionExpander 
  extends Traced<PartitionExpander> 
  implements PartitionExpander {
  public TracedPartitionExpander
    ( PartitionExpander innerObject ) {
    super(innerObject);
  }
  @Override
  public int expandPartition
    ( int[] vArray, int start, int stopLeft
    , int hole, int startRight, int stop) {
    boolean threw = false;
    String s = "expanding partition " 
               + PartitionDescription
                 ( vArray, start, stopLeft, hole
                 , startRight, stop );
    traceEntry(prefix("expandPartition") + s);
    try {       
      hole = inner.expandPartition
             ( vArray, start, stopLeft, hole
             , startRight, stop);
    } catch (RuntimeErrorException e) {
      threw = true;
      traceException ( inner.toString() 
                     + ".expandPartition", e);
    } finally { 
      traceExit ( prefix("expandPartition") + s 
                + (threw ? " failed" : (" left the pivot at " + hole)));
    }
    return hole;
  }
  private String PartitionDescription
    ( int[] vArray, int start, int stopLeft
    , int hole, int startRight, int stop) {
    boolean canSeeHoleCard 
      = (vArray!=null && 0<=hole && hole<vArray.length);
    return "[" + (stopLeft) + ".." + (startRight-1) + "]," 
      + " with pivot [" + hole + "]" 
      + ( canSeeHoleCard ? ("= " + vArray[hole] ) : "") 
      + "to cover [" + (start) + ".." + (stop) + "]";
  }
}
