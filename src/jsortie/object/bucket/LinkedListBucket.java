package jsortie.object.bucket;

public class LinkedListBucket<T>
  implements IterableBucket<T> {
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
  protected class LinkedListNode {
    public T                     value;
    public LinkedListNode        next;
    public LinkedListNode()    {            }
    public LinkedListNode(T v) { value = v; }
  }
  private LinkedListNode beforeFirst;
  private LinkedListNode last;
  public LinkedListBucket() {
    last = beforeFirst = new LinkedListNode();
  }
  @Override
  public void append(T v) {
    last  = last.next = new LinkedListNode(v);
  }
  @Override
  public BucketIterator<T> getBucketIterator() {
    class ListIterator 
      implements BucketIterator<T> {
      private LinkedListNode scan;
      public ListIterator() {
        scan = beforeFirst.next; 
      }
      @Override
      public boolean hasNext() {
        return scan != null; 
      }
      @Override
      public T next() {
        T v  = scan.value;
        scan = scan.next;
        return v;
      }		
    }
    return new ListIterator();
  }
  @Override
  public int emit(T[] dest, int destPos) {
    for ( LinkedListNode scan=beforeFirst.next; 
          scan != null; scan=scan.next) {
      dest[destPos] = scan.value;
      ++destPos;
    }
    return destPos;
  }
  @Override
  public IterableBucket<T> newBucket(int size) {
    return new LinkedListBucket<T>();
  }
  @Override
  public void clear() {
    last = beforeFirst;
    last.next = null;
  }
}
