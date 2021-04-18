package jsortie.janitors.insertion;

public class FourFoldInsertionSort
  extends OrigamiInsertionSort {
  protected int step=4;
  public static void foldRecursively
    ( int[] vArray, int start, int stop ) {
    int lhs = start;
    int rhs = stop-1;
    while (lhs<rhs) {
      int vLeft  = vArray[lhs];
      int vRight = vArray[rhs];
      boolean swap = (vRight<vLeft);
      vArray[lhs] = swap ? vRight : vLeft;
      vArray[rhs] = swap ? vLeft  : vRight;
      --rhs;
      ++lhs;
    }
    if (start<lhs && lhs<stop) {
      foldRecursively(vArray, start,lhs);
    }
    if (start<rhs && rhs<stop-1) {
      foldRecursively(vArray, lhs, stop);
    }
  }
  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    if (start + 1 < stop) {
      setUpSentinelsUnstable(vArray, start, stop);
      QuadrupletInsertionSort.sortSentinelledSubRange(vArray, start+1, stop);
    }
  }
}
