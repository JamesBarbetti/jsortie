package jsortie.quicksort.selector.clean;

public class CleanRemedianSelectorBranchAvoiding 
  extends CleanRemedianSelector {
  public CleanRemedianSelectorBranchAvoiding
    ( boolean uniform ) {
    super(uniform);
  }
	@Override
  public int medianOf3Candidates
    ( int [] vArray, int a, int b, int c ) {
    int vA = vArray[a];
    int vB = vArray[b];
    int vC = vArray[c];
    int iLower  = (vA <= vB ) ? a : b;
    int iHigher = a - iLower + b;
    int iLowest = (vArray[iLower] <= vC) ? iLower : c;
    int iOther  = a - iHigher + b - iLowest + c;
    return (vArray[iOther] <= vArray[iHigher]) ? iOther : iHigher;
    //iLower:  a, or b, whichever points to smaller
    //iHigher: a, or b, whichever wasn't iLow 
    //        (so, whichever points to higher of those two)
    //iLowest: a, b, or c, whichever points to smallest
    //iOther:  a, b, or c, whichever isn't iHigher or iLowest 
    //         (so: not the smallest, and not iHigher)
    //So Either: iOther or iHigher is the median item
  }
}
