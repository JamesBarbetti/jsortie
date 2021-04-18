package jsortie.object.indexed;

public interface IndexPivotSelector {
  int[] selectPivotsForIndexRange(IndexComparator comparator, int start, int stop);
}
