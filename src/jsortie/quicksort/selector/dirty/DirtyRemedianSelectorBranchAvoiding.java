package jsortie.quicksort.selector.dirty;

public class DirtyRemedianSelectorBranchAvoiding 
  extends DirtyRemedianSelector {
  public DirtyRemedianSelectorBranchAvoiding
    ( boolean uniform ) {
    super(uniform);
  }
	@Override
  protected int medianOf3Candidates
    ( int[] vArray, int a, int b, int c ) {
    //Note: There are two assignments in this method
    //      that risk overflow (that's not a problem 
    //      with int but for most types, e.g. double
    //      you would have to rewrite this method so
    //      that overflow can't occur).
    int vA         = vArray[a];
    int vB         = vArray[b];
    int vC         = vArray[c];
    int vLowAB     = (vA <= vB)      ? vA     : vB;
    int vHighAB    = vA - vLowAB + vB; //May overflow
    int vLowABC    = (vLowAB  <= vC) ? vLowAB : vC;
    int vHighABC   = (vHighAB <= vC) ? vC     : vHighAB;
    int vMiddleABC = vA - vLowABC + vB - vHighABC + vC; //May overflow
    vArray[a] = vLowABC;
    vArray[b] = vMiddleABC;
    vArray[c] = vHighABC;
    return b;
  }
}
