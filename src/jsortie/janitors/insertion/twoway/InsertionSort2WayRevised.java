package jsortie.janitors.insertion.twoway;

public class InsertionSort2WayRevised
  extends InsertionSort2Way {
  public static void sortSmallRange
    ( int[] vArray, int start, int stop ) {
    if (stop<start+2) return;
    int count = (stop-start);
    int lhs = start + (count) / 2 - 1;
    int rhs = lhs + 1 + (count&1);
    if (start<=lhs) do {
      int     vLeft  = vArray[lhs];
      int     vRight = vArray[rhs];
      boolean swap   = vRight < vLeft;
      int     vLow   = swap ? vRight : vLeft;
      int     vHigh  = swap ? vLeft  : vRight;
      int     scan   = lhs + 1;
      for (; vArray[scan]<vLow; ++scan) {
        vArray[scan-1] = vArray[scan];
      }
      vArray[scan-1] = vLow;
      --lhs;
      scan = rhs-1;
      for (; vHigh<vArray[scan]; --scan) {
        vArray[scan+1] = vArray[scan];
      }
      vArray[scan+1] = vHigh;
      ++rhs;
    } while (start<=lhs);
  }
}
