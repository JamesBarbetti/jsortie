package jsortie.quicksort.tracer;

import javax.management.RuntimeErrorException;

import jsortie.RangeSorter;

public class TracedRangeSorter extends Traced<RangeSorter> implements RangeSorter {
  public TracedRangeSorter(RangeSorter innerObject) {
    super(innerObject);
  }

  @Override
  public void sortRange(int[] vArray, int start, int stop) {
	boolean threw = false;
	try {
      traceEntry( prefix("sortRange") + " sorting range [" + start + ".." + (stop-1) + "]" );
      inner.sortRange(vArray,  start,  stop);
	} catch (RuntimeErrorException e) {
	  threw = true;
	  traceException( prefix("sortRange"), e);
    } finally {
	  traceExit ( prefix("sortRange")
                + (threw ? "failed to sort" : "sorted") 
                + " range [" + start + ".." + (stop-1) + "]" );
	}
  }
}
