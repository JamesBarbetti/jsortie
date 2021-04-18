package jsortie.object.quicksort.multiway.partitioner;

public interface FixedCountPivotObjectPartitioner<T> 
  extends MultiPivotObjectPartitioner<T> {
  public int getPivotCount();
}
