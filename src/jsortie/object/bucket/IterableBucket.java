package jsortie.object.bucket;

public interface IterableBucket<T> 
  extends SortingBucket<T> {
  public interface BucketIterator<T> {
    boolean hasNext();
    T next();
  }
  public BucketIterator<T> getBucketIterator();
  public IterableBucket<T> newBucket(int size);
}
