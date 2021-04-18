package jsortie.object.bucket;

import java.util.ArrayDeque;
import java.util.Iterator;

public class ArrayDequeBucket<T> 
  implements IterableBucket<T> {
  ArrayDeque<T> deque;
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public ArrayDequeBucket() {
    deque = new ArrayDeque<T>();
  }
  public ArrayDequeBucket(int expectedSize) {
    deque = new ArrayDeque<T>(expectedSize);
  }
  @Override
  public void append(T addMe) {
    deque.add(addMe);
  }
  @Override
  public int emit(T[] vArray, int start) {
    Iterator<T> it = deque.iterator();
    while (it.hasNext()) {
      vArray[start] = it.next();
      ++start;
    }
    return start;
  }
  @Override
  public void clear() {
    deque.clear();
  }
  private class ArrayDequeIterator 
    implements BucketIterator<T> {
    Iterator<T> it;
    public ArrayDequeIterator() {
      it = deque.iterator();
    }
    @Override
    public boolean hasNext() {
      return it.hasNext();
    }
    @Override
    public T next() {
      return it.next();
    }    
  }
  @Override
  public BucketIterator<T> getBucketIterator() {
    return new ArrayDequeIterator();
  }
  @Override
  public IterableBucket<T> newBucket(int size) {
    return new ArrayDequeBucket<T>(size);
  }
}
