package jsortie.heapsort;

import jsortie.heapsort.topdown.ToplessHeapsort;
import jsortie.helper.DumpRangeHelper;

public class BranchAvoidingHeapsort
  extends ToplessHeapsort {
  @Override
  public void extractFromHeap(int[] vArray, int start, int stop) {
    int fudge = start - 2;
    int j = stop;
    int i = stop - 1;
    int v = vArray[i];
    int oldStop = stop;
    do
    {
      boolean out = (stop<=j);
      vArray[i] = vArray[out ? i : j];
      vArray[i] = out ? v : vArray[i];
      System.out.print("[" + i + "]=" + vArray[i] + ",v=" + v + " ");
      DumpRangeHelper.dumpRange(" ", vArray, start, oldStop);
      stop     -= out ? 1 : 0;
      i         = out ? stop : j;
      v         = out ? vArray[stop] : v;
      j         = i + i - fudge;
      j         = out ? start : j;
      if (j+1<stop) {
        int k = j + 1;
        j = (vArray[k] > vArray[j]) ? k : j; 
      }
    } while (stop>start);
  }
}
