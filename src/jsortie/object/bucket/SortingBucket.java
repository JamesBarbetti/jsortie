package jsortie.object.bucket;

public interface SortingBucket<T> {
  public void             append(T addMe);
  public int              emit(T[] vArray, int start);
  public SortingBucket<T> newBucket(int size);
  public void             clear();
}
