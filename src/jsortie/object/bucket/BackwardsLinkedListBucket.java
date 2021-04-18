package jsortie.object.bucket;

public class BackwardsLinkedListBucket<T>
  implements SortingBucket<T> {
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
  protected class LinkedListNode {
    public T                     value;
    public LinkedListNode        prev;
    public LinkedListNode
      ( T v, LinkedListNode previous ) { 
      value = v;
      prev = previous;
    }
  }
  private LinkedListNode head;
  int     size;
  public BackwardsLinkedListBucket() {
    head = null;
    size = 0;
  }
  @Override
  public void append(T v) {
    head = new LinkedListNode(v, head);
    ++size;
  }
  @Override
  public int emit(T[] dest, int destPos) {
    int w = destPos + size - 1;
    for ( LinkedListNode scan=head; 
          scan != null; scan=scan.prev) {
      dest[w] = scan.value;
      --w;
    }
    return destPos;
  }
  @Override
  public IterableBucket<T> newBucket(int size) {
    return new LinkedListBucket<T>();
  }
  @Override
  public void clear() {
    head = null;
  }
}
