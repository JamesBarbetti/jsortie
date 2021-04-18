package jsortie.object.indexed;

public class IndexMiddleElementSelector 
  implements IndexPivotSelector {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int[] selectPivotsForIndexRange(IndexComparator comparator, int start, int stop) {
    return new int[] { start + (stop-start)/2 };
  }
}
