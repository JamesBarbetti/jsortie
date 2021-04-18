package jsortie.object.indexed;

public interface IndexPartitioner {
  int[] partitionIndexRange(IndexComparator comparator, int start, int stop, int[] iiPivots);
}
