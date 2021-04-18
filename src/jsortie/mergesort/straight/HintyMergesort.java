package jsortie.mergesort.straight;

import jsortie.StableRangeSorter;

public class HintyMergesort extends StraightMergesort
{
  public HintyMergesort(StableRangeSorter janitor, int threshold) {
    super(janitor, threshold);
  }	
  public static void straightMergeFromLeft 
  ( int source[], int aStart, int aStop 
  , int bStart, int bStop
  , int dest[], int dStart) {
	while (aStart<aStop && bStart<bStop) {
      int     vLeft        = source[aStart];
      int     vRight       = source[bStart];
      boolean isLeftWinner = vLeft <= vRight ;
      int     vMove        = isLeftWinner ? vLeft : vRight;
      int     aTick        = isLeftWinner ? 1 : 0;
      int     bTick        = isLeftWinner ? 0 : 1;
      dest[dStart++]       = vMove;
      aStart              += aTick;
      bStart              += bTick;	
    }
    while (aStart<aStop) dest[dStart++] = source[aStart++];
    while (bStart<bStop) dest[dStart++] = source[bStart++];
  }
}
