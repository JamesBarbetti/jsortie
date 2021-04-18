package jsortie.quicksort.expander.branchavoiding;

import jsortie.quicksort.expander.PartitionExpander;

public class BalancedSkippyExpander 
  implements PartitionExpander {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int expandPartition
    ( int[] vArray, int start, int stopLeft
    , int hole, int startRight, int stop) {
    int vPivot         = vArray[hole];
    if (stopLeft-start<stop-startRight) {
      //we want items equal to the pivot to go to the left
      for (int scan=stopLeft-1; start<=scan; --scan) {
        int v = vArray[scan];
        vArray[hole] = v;
        hole        -= ( vPivot <= v) ? 1 : 0;
        vArray[scan] = vArray[hole];
      }
      for (int scan=startRight; scan<stop; ++scan) {
        int v        = vArray[scan];
        vArray[hole] = v;
        hole        += ( v <= vPivot) ? 1 : 0;
        vArray[scan] = vArray[hole];
      }
    } else {
      //we want items equal to the pivot to go the right
      for (int scan=startRight; scan<stop; ++scan) {
        int v        = vArray[scan];
        vArray[hole] = v;
        hole        += ( v <= vPivot) ? 1 : 0;
        vArray[scan] = vArray[hole];
      }
      for (int scan=stopLeft-1; start<=scan; --scan) {
        int v = vArray[scan];
        vArray[hole] = v;
        hole        -= ( vPivot <= v) ? 1 : 0;
        vArray[scan] = vArray[hole];
      }
    }
    vArray[hole] = vPivot;
    return hole;
  }
}
