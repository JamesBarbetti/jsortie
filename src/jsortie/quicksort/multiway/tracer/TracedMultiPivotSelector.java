package jsortie.quicksort.multiway.tracer;

import javax.management.RuntimeErrorException;

import jsortie.quicksort.multiway.selector.MultiPivotSelector;
import jsortie.quicksort.tracer.Traced;

public class TracedMultiPivotSelector 
  extends    Traced<MultiPivotSelector>
  implements MultiPivotSelector {
  public TracedMultiPivotSelector(MultiPivotSelector innerObject) {
   super(innerObject);
  }
  @Override
  public int[] selectPivotIndices(int[] vArray, int start, int stop) {
    boolean threw = false;
    int[] pivots = null;
    String whatNow = " pivots from the range [" + start + ".." + (stop-1) + "]";
    traceEntry( prefix("selectPivotIndices") +  " selecting" + whatNow);
    try {
      pivots = selectPivotIndices(vArray, start, stop);
    } catch (RuntimeErrorException e){
      traceException( prefix("selectPivotIndices"), e);
    } finally {
      if (threw) {
        whatNow += " failed";
      } else {
        whatNow += ": returned ";
        if (pivots==null) {    	
          System.out.println(whatNow + "null");
        } else {
          for (int i=0; i<pivots.length; ++i) {
            whatNow += (0<i) ? ", " : "";
            whatNow += "[" + pivots[i] + "]=" + vArray[pivots[i]];
          }
        }
      }
      traceExit( prefix("selectPivotIndices") + whatNow);
    }
    return pivots;
  }
}
