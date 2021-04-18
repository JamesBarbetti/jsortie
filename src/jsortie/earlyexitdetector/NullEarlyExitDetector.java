package jsortie.earlyexitdetector;

public class NullEarlyExitDetector 
  implements RangeSortEarlyExitDetector {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public boolean exitEarlyIfSorted
    ( int[] vArray, int start, int stop ) {
    return false;
  }
}
