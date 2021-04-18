package jsortie.object.collection;

import java.util.Collection;
import java.util.Iterator;

import jsortie.object.bucket.IterableBucket;

public class BufferRing<T> 
  implements Collection<T>, IterableBucket<T> {
  int        bufferSize = 64;
  protected class RingBuffer {
    protected RingBuffer next;
    protected RingBuffer prev;
    protected T[]        data;
    @SuppressWarnings("unchecked")
    protected RingBuffer(int size) {
      data = (T[]) new Object[size];
    }
    protected void take
      (T[] src, int start, int stop ) {
      for (int i=start; i<stop; ++i) {
        data[i] = src[i];
        src[i]  = null;
      }
    }
  }
  protected class BufferPosition 
    implements Iterator<T>, BucketIterator<T> {
    protected RingBuffer current;
    protected int        index;
    protected BufferPosition
      ( RingBuffer buffer, int indexInIt) {
      current = buffer;
      index   = indexInIt;
    }
    protected boolean equal
      ( BufferPosition other ) {
      //Note: this is usually in-lined,
      //for performance reasons.
      return current == other.current 
          && index == other.index;
    }
    public void delete() {
      //You could make delete faster,
      //when there are a lot of buffers,
      //by "virtually shrinking" the 
      //current buffer! (You maintain
      //start and stop members on each buffer).
      //But that'd be... tricky. And you'd
      //want to coalesce small buffers, too.
      //Icky!  delete() is the one operation 
      //a Linked List is good at!  It's a pity
      //to... lose to it, even for one operation.
      
      //We can't tell whether we're nearer to
      //head or to tail.  But... we'll assume 
      //that we are closer to head...
      //It is assumed that: 0<count,
      //and the current position is between
      //head and tail.
      T[] data = current.data;
      int i =index;
      while (current != head.current) {
        for (; 0<i; --i) {
          data[i] = data[i-1];
        }
        current = current.prev;
        T[] oldData = data;
        data = current.data;
        i = current.data.length - 1;
        oldData[0] = data[i];
      }
      int headStart = head.index;
      for (; headStart<i; --i) {
        data[i] = data[i-1];
      }
      ++head.index;
      if (head.index == data.length) {
        head.current = head.current.next;
        head.index = 0;
      }
      --count;
    }
    @Override
    public boolean hasNext() {
      return current != tail.current
          || index != tail.index;
    }
    @Override
    public T next() {
      //Note: Does NOT check that 
      //hasNext()==true.  Wrap it, if you
      //want a throw when that happens!
      T[] data = current.data;
      T retVal = data[index];
      ++index;
      if (index==data.length) { //or: buffer.stop
        current = current.next;
        index   = 0; //or: buffer.start
      }
      return retVal;
    }
  }
  BufferPosition head; //first written item
  BufferPosition tail; //after last written item
  int        count = 0;
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public BufferRing() {
    clear();
  }
  public BufferRing(int size) {
    RingBuffer only = new RingBuffer(size);
    only.next = only.prev = only;
    head = new BufferPosition(only, 0 );
    tail = new BufferPosition(only, 0 );
    count = 0;
  }
  protected boolean isFull() {
    //Note: this is usually in-lined,
    //for performance reasons.
    return 
      ! ( count==0 
          || head.current != tail.current 
          || head.index   != tail.index); 
  }
  @Override
  public boolean add(T e) {
    //add==addTail
    if ( /*isFull()*/ 
         ! ( count==0 
             || head.current != tail.current 
             || head.index   != tail.index) ) {
      grow(bufferSize);
    }
    T[] buff = tail.current.data; 
    buff[tail.index] = e;
    ++tail.index;
    if ( tail.index == buff.length ) {
      tail.current = tail.current.next;
      tail.index   = 0;
    }
    ++count;
    return true;
  }
  public void addFront(T e) {
    T[] buff;
    if ( /*isFull()*/ 
         ! ( count==0 
             || head.current!=tail.current
             || head.index!=tail.index) ) {
      grow(bufferSize);
    }
    if (head.index==0) {
      head.current = head.current.prev;
      buff = head.current.data;
      head.index = buff.length; 
    } else {
      buff = head.current.data;
    }
    --head.index;
    buff[head.index] = e;
    ++count;
  }
  protected void grow(int amount) {
    //Note: it is assumed head and tail
    //have matching current and index.
    RingBuffer old = head.current;
    T[] source     = old.data;
    int sourceLen = source.length; 
        //or: old.stop
    int size = (bufferSize < sourceLen)
             ? sourceLen : bufferSize;
    RingBuffer extra = new RingBuffer(size);
    //Copy start and stop from old to extra
    if ( head.index < sourceLen - head.index ) {
      extra.take( source, 0, head.index); 
      RingBuffer oldPrev = old.prev;
      oldPrev.next  = extra;
      extra.prev = oldPrev;
      extra.next = old;
      tail.current = extra;
      //note: any iterator that had current==old,
      //and an index between 0 and head.index,
      //...would be invalidated.
      //I deliberately don't check for that, or 
      //handle it.  Extend this class if you
      //want to handle that!
    } else {
      extra.take( source, head.index, sourceLen);
      RingBuffer oldNext = old.next;
      old.next = extra;
      extra.prev = old;
      extra.next = oldNext;
      head.current = extra;
      //note: any iterator that had current==old,
      //and an index between head.index and
      //old.data.length... would be invalidated.
      //I deliberately don't check for that, or 
      //handle it.  extend this class if you 
      //want to handle that!
    }
  }
  @Override
  public boolean addAll
    ( Collection<? extends T> c ) {
    for (T x : c) {
      add(x);
    }
    return false;
  }
  @Override
  public void clear() {
    RingBuffer only = new RingBuffer(bufferSize);
    only.next = only.prev = only;
    head = new BufferPosition(only, 0 );
    tail = new BufferPosition(only, 0 );
    count = 0;
  }
  @Override
  public boolean contains(Object o) {
    BufferPosition pos
      = new BufferPosition(head.current, head.index);
    return find(o, pos);
  }
  @Override
  public boolean containsAll
    ( Collection<?> c ) {
    //Note: this is a silly method to call,
    //because BufferRing sucks at it... bad.
    int targetCount = c.size();
    if (targetCount==0) {
      return true;
    }
    if (count < targetCount ) {
      //Note: this would be woefully inefficient, 
      //when c.size() is a lot smaller than 
      //this.size().  You don't want to call
      //contains, c.size() times!  Nasty!
      for (Object o : c) {
        if (!contains(o)) {
          return false;
        }
      }    
      return true;
    } else {
      //So... c.size() is less than this.size().
      Object[] targets = c.toArray();
      //Strip out any nulls.
      int w = 0;
      for (int r=0; r<targetCount; ++r) {
        if ( targets[r]!=null ) {
          targets[w]=targets[r];
          ++w;
        }
      }
      boolean lookingForNull = (w<targetCount);
      RingBuffer buffer = head.current;
      T[]        data   = buffer.data; 
      int        index  = head.index;
      int        i;
      boolean    foundNull = false;
      for (i=0; i<count; ++i) {
        T vHere = data[index];
        if (vHere==null) {
          foundNull = true;
          if (targetCount==0) {
            return true;
          }
        } else {
          for ( int check = 0
              ; check < targetCount
              ; ++ check ) {
            Object vThere = targets[check];
            if (vThere.equals(vHere)) {
              if ( targetCount == 1
                   && ( !lookingForNull 
                        || foundNull) )
              {
                return true;
              }
              --targetCount;
              targets[check] 
                = targets[targetCount];
              --i;
            }
          }
        }
        ++index;
        if (index==data.length) {
          buffer = buffer.next;
          data   = buffer.data;
          index  = 0;
        }
      }
      return false;
    }
  }
  @Override
  public boolean isEmpty() {
    return count==0;
  }
  @Override
  public Iterator<T> iterator() {
    return new BufferPosition
               ( head.current, head.index );
  }
  @Override
  public boolean remove(Object o) {
    BufferPosition pos
      = new BufferPosition
            ( head.current, head.index );
    boolean rc = find(o, pos);
    if (rc) {
      pos.delete();
    }
    return rc;
  }
  private boolean find
    ( Object o, BufferPosition pos ) {
    // TODO Auto-generated method stub
    if (count==0) {
      return false;
    }
    RingBuffer buffer = head.current;
    T[]        data   = buffer.data; 
    int        index  = head.index;
    int        i;
    if (o==null) {
      for (i=0; i<count; ++i) {
        if (data[index]==null) {
          break;
        }
        ++index;
        if (index==data.length) {
          buffer = buffer.next;
          data   = buffer.data;
          index  = 0;
        }
      }
    } else {
      for (i=0; i<count; ++i) {
        T v = data[index];
        if ( v != null )
          if ( v.equals(o) ) {
          break;
        }
        ++index;
        if (index==data.length) {
          buffer = buffer.next;
          data   = buffer.data;
          index  = 0;
        }
      }
    }
    pos.current = buffer;
    pos.index   = index;
    return (i<count);
  }
  @Override
  public boolean removeAll(Collection<?> c) {
    int countRemoved = 0;
    BufferPosition pos 
      = new BufferPosition
            ( head.current, head.index );
    while (pos.hasNext()) {
      if ( c.contains
           ( pos.current.data[pos.index] ) ) {
        pos.delete();
        ++countRemoved;
      }
    }
    return ( 0 < countRemoved );
  }
  @Override
  public boolean retainAll(Collection<?> c) {
    int countRemoved = 0;
    BufferPosition pos 
      = new BufferPosition
            ( head.current, head.index );
    while (pos.hasNext()) {
      if ( !c.contains
            ( pos.current.data[pos.index] ) ) {
        pos.delete();
        ++countRemoved;
      }
    }
    return ( 0 < countRemoved );
  }
  @Override
  public int size() {
    return count;
  }
  @Override
  public Object[] toArray() {
    Object[] vArray = new Object[count];
    softTypedEmit(vArray, 0);
    return vArray;
  }
  @Override
  public <U> U[] toArray(U[] vArray) {
    softTypedEmit(vArray, 0);
    return vArray;
  }
  @SuppressWarnings("unchecked")
  public <U> int softTypedEmit
    ( U[] vArray, int start ) {
    RingBuffer buffer = head.current;
    T[]        data   = buffer.data; 
    int        index  = head.index;
    int        i;
    for (i=0; i<count; ++i) {
      vArray[start] = (U) data[index];
      ++start;
      ++index;
      if (index==data.length) {
        buffer = buffer.next;
        data   = buffer.data;
        index  = 0; 
      }
    }
    return start;
  }
  @Override
  public void append(T addMe) {
    // TODO Auto-generated method stub
  }
  @Override
  public int emit(T[] vArray, int start) {
    return softTypedEmit(vArray, start);
  }
  @Override
  public BucketIterator<T> getBucketIterator() {
    return new BufferPosition
               (head.current, head.index); 
  }
  @Override
  public IterableBucket<T> newBucket(int size) {
    return new BufferRing<T>(size);
  }  
}
