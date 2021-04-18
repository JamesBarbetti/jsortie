package jsortie.object.bucket;

public class OldLinkedListBucket<T> implements IterableBucket<T>
{
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
	
  private class LinkedListNode {
    public T              value;
    public LinkedListNode next;
    public LinkedListNode(T v) {
      value = v;
    }
  }
	
  private LinkedListNode first;
  private LinkedListNode last;

  private class ListIterator implements BucketIterator<T> {
    private OldLinkedListBucket<T>.LinkedListNode scan;
    public ListIterator(OldLinkedListBucket<T> bucky) {
      scan = bucky.first;
    }
    @Override
    public boolean hasNext() {
      return scan!=null;
    }
    @Override
    public T next() {
      T v  = scan.value;
      scan = scan.next;
      return v;
    }		
  }
	
  @Override
  public void append(T v) {
    if (first==null) {
      first = last = new LinkedListNode(v);
    } else {
      last = last.next = new LinkedListNode(v);
    }
  }

  @Override
  public BucketIterator<T> getBucketIterator() {
    return new ListIterator(this);
  }

  @Override
  public int emit(T[] dest, int destPos) {
    for (LinkedListNode scan=first; scan!=null; scan=scan.next) {
      dest[destPos] = scan.value;
      ++destPos;
    }
    return destPos;
  }

  @Override
  public IterableBucket<T> newBucket(int size) {
    return new OldLinkedListBucket<T>();
  }
  
  @Override
  public void clear() {
    first = last = null;
  }
}