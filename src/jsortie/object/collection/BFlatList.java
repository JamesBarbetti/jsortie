package jsortie.object.collection;

import java.util.Collection;
import java.util.Iterator;

import jsortie.exception.SortingFailureException;
import jsortie.object.bucket.IterableBucket;

public class BFlatList<T> 
  implements Collection<T>
           , IterableBucket<T> {
  int bufferSize = 64; // for now, hardcoded
  //The next two are calculated from bufferSize
  //(if bufferSize changes, they should change too).
  int bufferSizeLess1 = bufferSize - 1;
  int doubleBufSizeLess2
    = bufferSizeLess1 + bufferSizeLess1; 
  int threeQuartersBufSize = (bufferSize*3) >> 2; 
  public int totalElementCount;
  static final int POSITION_BOF = -1;
  static final int POSITION_INVALIDATED = -2;
  static final int POSITION_EOF = -3;
  protected class BufferNode {
    BufferNode prev;
    BufferNode next;
    Position   firstPosition; //first active iterator
               //home on thiis buffer node (if any)
    T[] buffer;
    int start;
    int stop; 
    @SuppressWarnings("unchecked")
    protected BufferNode() {
      prev = next = null;
      firstPosition = null;
      buffer = (T[]) new Object[bufferSize];
      start = stop = 0;
    }
    //If start==stop then the buffer is full
    protected boolean insertAt
      ( T e, int i, int count ) { 
      //returns true if a new node was created
      
      //Place e here
      adjustPositions(i, count, 1);
      if (i + i < count) {
        //Shift items left to make room
        int newStart = start - 1 ;
        if (newStart<0) {
          newStart  += bufferSize;
        }
        int shuffle = newStart;
        for (; 0 < i; --i) {
          int shufflePlus1 = shuffle+1;
          if (bufferSize==shufflePlus1) {
            shufflePlus1=0;
          }
          buffer[shuffle] 
            = buffer[shufflePlus1];
          shuffle = shufflePlus1;
        }
        buffer[shuffle] = e;
        start = newStart; 
      } else {
        //Shift items right to make room
        int shuffle = stop;
        for (; i<count; ++i) {
          int shuffleMinus1 = shuffle - 1;
          if (shuffleMinus1<0) {
            shuffleMinus1+=bufferSize;
          }
          buffer[shuffle] 
            = buffer[shuffleMinus1];
          shuffle = shuffleMinus1;
        }
        buffer[shuffle] = e;
        ++stop;
        if (bufferSize==stop) {
          stop=0;
        }
      }
      ++count;
      ++totalElementCount;
      
      //Balance with neighbours (by donating items)
      if ( this != firstNode 
           && prev.getBufferCount() < count ) {
        spillLeft(1);
        --count;
        return false;
      }
      if ( this != lastNode 
           && next.getBufferCount() < count ) {
        spillRight(1);
        --count;
        return false;
      }
      if (count!=bufferSize) {
        return false;
      }
      if ( next!=null 
           && next.getBufferCount() 
              >= bufferSizeLess1) {
        BufferNode afterNext = next;
        next = newNode();
        next.prev = this;
        next.next = afterNext;
        if (afterNext!=null) {
          afterNext.prev = next;
          spillRight(count/3);
          afterNext.spillLeft
            ( afterNext.getBufferCount()/3 );
        } else {
          lastNode = next;
          spillRight(count/2);
        }
        
      } else if ( prev != null 
                  && prev.getBufferCount() 
                     >= bufferSizeLess1) {
         BufferNode beforePrev = prev;
         prev = newNode();
         if (beforePrev!=null) {
           beforePrev.next = prev;
           beforePrev.spillRight
             ( beforePrev.getBufferCount()/3 );
           spillLeft(count/3);
         } else {
           firstNode = prev;
           spillLeft(count/2);
         }
         prev.prev = beforePrev;
         prev.next = this;
      } else if (prev==null && next==null){
        //this must be the only node. Donate the 
        //second half its items to a new node, 
        //added on the right.
        this.next = lastNode = newNode(); 
        lastNode.prev = this;
        spillRight(count/2);
      } else {
        return false;
      }
      return true;
    }
    protected void spillLeft(int howMany) {
      //Assumes 0<howMany
      int newStart = start + howMany;
      if (bufferSize <= newStart ) {
        newStart -= bufferSize;
      }
      int r=start;
      int w=prev.stop;
      T[] prevBuffer = prev.buffer;
      do {
        prevBuffer[w] = buffer[r];
        buffer[r] = null;
        ++r; 
        if (bufferSize==r) {
          r=0;
        }
        ++w;
        if (bufferSize==w) {
          w=0;
        }
      } while (r!=newStart);
      start = newStart;
      prev.stop = w;
      adjustPositions(0, howMany, -howMany);
    }
    protected void spillRight(int howMany) {
      int count = getBufferCount();
      int newStop = stop - howMany;
      if (newStop < 0) {
        newStop += bufferSize;
      }
      int r = stop;
      int w = next.start;
      T[] nextBuffer = next.buffer;
      do {
        --r;
        if (r<0) {
          r += bufferSize;
        }
        --w;
        if (w<0) {
          w += bufferSize;
        }
        nextBuffer[w] = buffer[r];
        buffer[r]     = null;
      } while (r!=newStop);
      stop = newStop;
      next.start = w;
      adjustPositions( count-howMany, count, howMany );
    }
    protected boolean removeAt
      ( int indexInBuffer ) {
      //Returns true if this BufferNode got 
      //unlinked as a result.
      int count = getBufferCount();
      if (indexInBuffer + indexInBuffer < count ) {
        //Shift items on the left, one place right
        int w = start+indexInBuffer;
        if (bufferSize<=w) {
          w -= bufferSize;
        }
        while (w!=start) {
          int r = w -1;
          if (r<0) {
            r += bufferSize;
          }
          buffer[w] = buffer[r];
          w = r;
        }
        buffer[start] = null;
        ++start;
        if (bufferSize==start) {
          start = 0;
        }
      } else {
        //Shift items on the right, one place left
        int w = start+indexInBuffer;
        if (bufferSize<=w) {
          w -= bufferSize;
        }
        for (;;) {
          int r = w + 1;
          if (bufferSize<=r) {
            r -= bufferSize;
          }
          if (r==stop) {
            break;
          }
          buffer[w] = buffer[r];
          w = r;
        }
        buffer[w] = null;
        stop = w;
      }
      //Balance with neighbours (by stealing items)
      //Note count is out of date, so we steal if we're
      //smaller than a neighbour by *2* or more.
      if ( this != firstNode 
           && count < prev.getBufferCount() ) {
        prev.spillRight(1);
        ++count;
      }
      if ( this != lastNode 
           && count < next.getBufferCount()  ) {
        next.spillLeft(1);
        ++count;
      }
      --totalElementCount;
      if ( bufferSize < count + count ) {
        return false;
      }
      --count;
      int leftCount
        = (this==firstNode) 
          ? doubleBufSizeLess2 
          : prev.getBufferCount();
      int rightCount
        = (this==lastNode) 
          ? doubleBufSizeLess2 
          : next.getBufferCount();
      int count3 
        = count + leftCount + rightCount ;
      if ( count3 <= doubleBufSizeLess2 ) {
        //Coalesce
        int newLeftCount = leftCount + count/2;
        if (bufferSize<newLeftCount) {
          newLeftCount = bufferSize;
        }
        int newRightCount = count3 - newLeftCount;
        if (leftCount<newLeftCount) {
          spillLeft(newLeftCount-leftCount);
        }
        if (rightCount<newRightCount) {
          spillRight(newRightCount-rightCount);
        }
        prev.next = next;
        next.prev = prev;
        return true;
      } else if ( count + rightCount 
                  < threeQuartersBufSize ) {
        spillRight(count);
        if (prev!=null) {
          prev.next = next;
        } else {
          firstNode = next;
        }
        next.prev = prev;
        return true;
      } else if ( leftCount + count 
                  < threeQuartersBufSize ) {
        spillLeft(count);
        if (next!=null) {
          next.prev = prev;
        } else {
          lastNode = prev;
        }
        prev.next = next;
        return true;
      }
      return false;
    }
    protected void adjustPositions
      (int begin, int end, int offset) {
      Position prevPos = null;
      Position pos=firstPosition; 
      int myCount = getBufferCount();
      while (pos!=null) { 
        Position nextPos = pos.nextPosition;
        if ( begin <= pos.indexInBuffer 
             && pos.indexInBuffer < end ) {
          pos.indexInBuffer += offset;
          BufferNode owner = this;
          if ( pos.indexInBuffer < 0 
               && this!=firstNode) {
            owner = prev;
            pos.indexInBuffer 
              += prev.getBufferCount();
          } else if ( myCount <= pos.indexInBuffer 
                      && this!=lastNode ) {
            owner = next;
            pos.indexInBuffer -= myCount;
          }
          if (owner!=this) {
            //hand pos to prev
            pos.node           = owner;
            pos.nextPosition   = owner.firstPosition;
            owner.firstPosition = pos;
            if (prevPos==null) {
              firstPosition = nextPos;
            } else {
              prevPos.nextPosition = nextPos;
            }
          } else { 
            prevPos = pos;
          }
        } else {
          prevPos = pos;
        }
        pos = nextPos;
      }
    }
    protected int getBufferCount() {
      int count = stop - start 
                + ((start < stop) ? 0 : bufferSize );
      if ( count == bufferSize ) {
        if ( this == lastNode 
             && totalElementCount == 0) {
          count = 0;
        }
      }
      return count;
    }
    protected void invalidatePositions() {
      for ( Position pos=firstPosition; pos!=null  
          ; pos = pos.nextPosition ) {
        pos.node = null;
        pos.indexInBuffer 
          = POSITION_INVALIDATED;
      }
    }
    protected void detachPosition(Position position) {
      if (position==firstPosition) {
        firstPosition = firstPosition.nextPosition;
      } else {
        Position before = firstPosition;
        while (before!=null) {
          if (before.nextPosition == position) {
            before.nextPosition
              = before.nextPosition.nextPosition;
          }
        }
      }
    }
    @SuppressWarnings("unchecked")
    protected <U> int emitToArray(U[] vArray, int w) {
      
      int r = start;
      do {
        vArray[w] = (U) buffer[r];
        ++w;
        ++r;
        if (r==bufferSize) {
          r=0;
        }
      } while (r!=stop);
      return w;
    }
  }
  BufferNode root      = newNode();
  BufferNode firstNode = root;
  BufferNode lastNode  = root;
  
  protected class Position 
    implements Iterator<T>, BucketIterator<T> {
    protected BufferNode node;
    protected Position nextPosition; //in same node
    protected int indexInBuffer; //relative to start, not zero
    protected Position
      ( BufferNode whatNode, int whatIndex ) {
      node = whatNode;
      indexInBuffer = whatIndex;
      nextPosition = node.firstPosition;
      node.firstPosition = this;
    }
    public void remove() {
      if (indexInBuffer<0) {
        
      }
      node.removeAt(indexInBuffer);
    }
    void insert(T e) {
      node.insertAt
        ( e, indexInBuffer, node.getBufferCount() );
    }
    public boolean hasNext() {
      if (node!=lastNode) {
        return (node!=null); 
      }
      int nodeCount = node.getBufferCount();
      boolean rc = (indexInBuffer + 1 < nodeCount);
      if (!rc) {
        node.detachPosition(this);
        this.indexInBuffer = POSITION_EOF;
      }
      return rc;
    }
    @Override
    public T next() {
      if (indexInBuffer<-1) {
        if (indexInBuffer==POSITION_EOF) {
          throw new SortingFailureException
            ( "next after EOF of BFlatTree" );
        } else {
          throw new SortingFailureException
            ( "next on invalidated BFlatTree.Position" );
        }
      }
      ++indexInBuffer;
      int nodeCount = node.getBufferCount();
      if (indexInBuffer<nodeCount) {
        int trueIndex = indexInBuffer + node.start;
        if (bufferSize<=trueIndex) {
          trueIndex-=bufferSize;
        }
        return node.buffer[trueIndex];
      } else if (node==lastNode) {
        throw new SortingFailureException("off end");
      } else {
        node.detachPosition(this);
        node = node.next;
        indexInBuffer = 0;
        nextPosition = node.firstPosition;
        node.firstPosition = this;
        return node.buffer[node.start];
      }
    }
    public boolean findNext(Object e) {
      //Ick! Linear search!
      if (e==null) {
        //For a null.
        while (hasNext()) {
          T v = next();
          if (v==null) {
            return true;
          }
        }
        return false;
      }
      //For an item, v, 
      //that compares equal to e.
      while (hasNext()) {
        T v = next();
        if (v!=null) {
          if (v.equals(e)) {
            return true;
          }
        }
      }
      return false;
    }
  }
  public BFlatList() {
    clear();
  }
  public BFlatList(int capacity) {
    clear();
  }
  public BFlatList(int capacity, int bufSize) {
    if (bufSize<4) {
      throw new IllegalArgumentException
        ( "Buffer size must be at least 4."
        + " Cannot be " + bufSize);
    }
    bufferSize = bufSize;
    bufferSizeLess1 = bufferSize - 1;
    doubleBufSizeLess2   
      = bufferSizeLess1 + bufferSizeLess1; 
    threeQuartersBufSize = (bufferSize*3) >> 2; 
    clear();
  }
  public String toString() {
    return this.getClass().getSimpleName(); 
  }
  public String toDebugString() {
    Object[] obs   = this.toArray();
    StringBuffer s = new StringBuffer();
    String sep = "";
    int i = 0;
    for (Object o: obs) {
      if ( o !=null ) {      
        s.append(sep + o.toString());
      } else {
        s.append("null@" + i);
      }
      sep = ", ";
    }
    return "{ " + s.toString() + " }";
  }  
  @Override
  public void append(T e) {
    int lastNodeCount 
      = lastNode.getBufferCount();
    lastNode.insertAt(e, lastNodeCount, lastNodeCount);
  }
  @Override
  public int emit(T[] vArray, int start) {
    return emitToArray(vArray, start);
  }
  @Override
  public BucketIterator<T> getBucketIterator() {
    return start();
  }
  @Override
  public IterableBucket<T> newBucket(int size) {
    return new BFlatList<T>();
  }
  @Override
  public boolean add(T e) {
    int lastBufCount 
      = lastNode.getBufferCount();
    lastNode.insertAt
      ( e, lastBufCount, lastBufCount );
    return true;
  }
  @Override
  public boolean addAll
    ( Collection<? extends T> c ) {
    for (T e : c) {
      int lastBufCount 
        = lastNode.getBufferCount();
      lastNode.insertAt
        ( e, lastBufCount, lastBufCount );
    }
    return true;
  }
  @Override
  public void clear() {
    BufferNode newRoot = newNode();
    BufferNode node = firstNode;
    do {
      node.invalidatePositions();
      node = node.next; 
    } while (node!=null);
    firstNode = lastNode = root = newRoot;
    totalElementCount = 0;
  }
  @Override
  public boolean contains(Object e) {
    Position p = start();
    return p.findNext(e);
  }
  @Override
  public boolean containsAll(Collection<?> c) {
    for (Object e : c ) {
      if (!contains(e)) {
        return false;
      }
    }
    return true;
  }
  @Override
  public boolean isEmpty() {
    return totalElementCount == 0;
  }
  @Override
  public Iterator<T> iterator() {
    //For outsiders.
    return start();
  }
  @Override
  public boolean remove(Object e) {
    Position p = start();
    boolean rc = p.findNext(e);
    if (rc) {
      p.remove();
    }
    return rc;
  }
  @Override
  public boolean removeAll(Collection<?> c) {
    boolean anyRemoved = false;
    Position p  = start();
    while (p.hasNext()) {
      boolean r = c.contains(p.next());
      if (r) {
        p.remove();
        anyRemoved = true;
      }
    }
    return anyRemoved;
  }
  @Override
  public boolean retainAll(Collection<?> c) {
    boolean anyRemoved = false;
    Position p = start();
    while (p.hasNext()) {
      boolean keep = c.contains(p.next());
      if (!keep) {
        p.remove();
        anyRemoved = true;
      } 
    }
    return anyRemoved;
  }
  @Override
  public int size() {
    return totalElementCount;
  }
  @Override
  public Object[] toArray() {
    Object[] vA 
      = new Object[totalElementCount];
    emitToArray(vA, 0);
    return vA;
  }
  @Override
  public <U> U[] toArray(U[] vA) {
    emitToArray(vA, 0);
    return vA;
  }
  public Iterator<T> iteratorByOrdinal(int ordinal) {
    if (ordinal<0 || totalElementCount<=ordinal) {
      throw new IllegalArgumentException
        ( "Cannot locate item with ordinal " + ordinal 
        + " from a list containing " + totalElementCount);
    }
    if (ordinal + ordinal < totalElementCount) {
      //Find it forward
      BufferNode node = firstNode;
      for (;;) {
        int nodeCount 
          = node.getBufferCount();
        if (ordinal<nodeCount) {
          return new Position(node, ordinal);
        }
        ordinal -= nodeCount;
      }
    } else {
      //Find it backward
      int backOrdinal = totalElementCount - ordinal;
      BufferNode node = lastNode;
      for (;;) {
        int nodeCount 
          = node.getBufferCount();
        if (backOrdinal<nodeCount) {
          return new Position
            ( node, nodeCount-1-backOrdinal);
        }
        backOrdinal -= nodeCount;
      }
    }
  }
  public boolean removeByOrdinal(int ordinal) {
    if (ordinal<0 || totalElementCount<=ordinal) {
      throw new IllegalArgumentException
        ( "Cannot remove item with ordinal " + ordinal 
        + " from a list containing " + totalElementCount);
    }
    Iterator<T> i = iteratorByOrdinal(ordinal);
    i.remove();
    return true;
  }
  protected BufferNode newNode() {
    return new BufferNode();
  }
  protected Position start() {
    //For insiders.  Outsiders use iterator().
    return new Position(firstNode, POSITION_BOF);
  }  
  protected <U> int emitToArray
    ( U[] vArray, int start ) {
    if ( totalElementCount == 0 ) {
      return start;
    }
    for ( BufferNode node=firstNode; node!=null 
        ; node = node.next ) {
      start = node.emitToArray(vArray, start);
    }
    return start;
  }
  protected int getNodeCount() {
    int count = 0;
    for ( BufferNode node=firstNode; node!=null
        ; node = node.next ) {
      ++count;
    }
    return count;
  }
}