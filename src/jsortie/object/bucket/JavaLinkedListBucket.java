package jsortie.object.bucket;

import java.util.Iterator;
import java.util.LinkedList;

public class JavaLinkedListBucket<T> 
  implements IterableBucket<T> {
  LinkedList<T> list = new LinkedList<T>();
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
  public void append(T addMe)  {
    list.add(addMe);
  }
  public class LinkedListIterator 
    implements BucketIterator<T> {
    Iterator<T> trueIterator;
    public LinkedListIterator(JavaLinkedListBucket<T> bucky) {
      trueIterator = bucky.list.iterator();
    }
    @Override
    public boolean hasNext() {
      return trueIterator.hasNext();
    }
    @Override
    public T next() {
      return trueIterator.next();
    }
  }
  @Override
  public BucketIterator<T> getBucketIterator() {
    return new LinkedListIterator(this);
  }
  @Override
  public int emit(T[] dest, int destPos) {
    Iterator<T> iterator = list.iterator();
    while (iterator.hasNext()) {
      dest[destPos] = iterator.next();
      ++destPos;
    }
    return destPos;
  }
  @Override
  public IterableBucket<T> newBucket(int size) {
    return new JavaLinkedListBucket<T>();
  }
  public int size() {
    return list.size();
  }
  @Override public void clear() {
    list.clear();
  }
}
