package jsortie.earlyexitdetector;

public class NullStableEarlyExitDetector
  implements StableRangeSortEarlyExitDetector {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public boolean exitEarlyIfSortedStable
    ( int[] vArray, int start, int stop ) {
    return false;
  }
}
