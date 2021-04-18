package jsortie;

public class Java8ParallelSorter  implements RangeSorter {
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    java.util.Arrays.parallelSort( vArray, start, stop );
  }	
}
