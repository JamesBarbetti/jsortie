package jsortie.quicksort.tracer;

import javax.management.RuntimeErrorException;

import jsortie.quicksort.selector.SinglePivotSelector;

public class TracedSinglePivotSelector
  extends Traced<SinglePivotSelector>
  implements SinglePivotSelector {
  public TracedSinglePivotSelector(SinglePivotSelector innerObject) {
    super(innerObject);
  }
  @Override
  public int selectPivotIndex(int[] vArray, int start, int stop) {
	int pivotIndex = start;
	boolean threw = false;
	traceEntry( prefix("selectPivotIndex") + " selecting pivot" 
      + " in range array[" + start + ".." + (stop-1) + "]");
	try {
      pivotIndex = inner.selectPivotIndex(vArray, start, stop);
	} catch (RuntimeErrorException e) {
	  threw = true;
	  traceException( prefix("selectPivotIndex"), e);
    } finally {
	  traceExit( prefix("selectPivotIndex") + " pivot for [" + start + ".." + (stop-1) + "]"
        + (threw ? "failed" : (" was vArray[" + pivotIndex + "]=" + vArray[pivotIndex])));
	}
	return pivotIndex;
  }
}
