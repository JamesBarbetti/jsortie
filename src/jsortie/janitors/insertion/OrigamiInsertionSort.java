package jsortie.janitors.insertion;

import jsortie.RangeSorter;
import jsortie.flags.QuadraticAverageCase;

public class OrigamiInsertionSort 
  implements RangeSorter, QuadraticAverageCase
{
  @Override
  public String toString() { 
    return this.getClass().getSimpleName(); 
  }
  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    if (start+1<stop) {
      setUpSentinelsUnstable(vArray, start, stop);
      ++start;
      --stop;
      for (int sweep = start + 1; sweep<stop; ++sweep) {
        int v = vArray[sweep];
        int scan;
        for ( scan=sweep-1 ; v<vArray[scan]; --scan) {
          vArray[scan+1] = vArray[scan];
        }
        vArray[scan+1] = v;
      }
    }
  }
  public static void setUpSentinelsUnstable
    ( int[] vArray, int start, int stop ) {
    int last = stop-1;
    int lhs = start;
    int rhs = last;
    do {
      int vLeft  = vArray[lhs];
      int vRight = vArray[rhs];
      boolean swap = (vRight<vLeft);
      vArray[lhs] = swap ? vRight : vLeft;
      vArray[rhs] = swap ? vLeft  : vRight;
      --rhs;
      ++lhs;
      if (rhs<=lhs) {
        lhs=start;
      }
    } while (start<rhs);
    lhs = start + (stop-start)/2;
    rhs = stop -1;
    do {
      int vLeft  = vArray[lhs];
      int vRight = vArray[rhs];
      boolean swap = (vRight<vLeft);
      vArray[lhs] = swap ? vRight : vLeft;
      vArray[rhs] = swap ? vLeft  : vRight;
      --rhs;
      ++lhs;
      if (rhs<=lhs) {
        rhs=last;
      }
    } while (lhs<last);
  }
}
