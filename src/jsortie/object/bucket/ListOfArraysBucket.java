package jsortie.object.bucket;

import java.util.Iterator;
import java.util.LinkedList;

public class ListOfArraysBucket<T>
  implements IterableBucket<T> {
  private T[]             lastArray;
  private int             lastArrayCount;
  private int             lastArrayLength;
  private LinkedList<T[]> previousArrays;
  private int countOfItemsInPreviousArrays;
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
  @SuppressWarnings("unchecked")
  public ListOfArraysBucket(int size) {
    lastArrayLength = size;
    lastArray
      = (T[]) new Object[lastArrayLength];
    lastArrayCount  = 0;
  }
  public int nextSize(int lastSize) {
    return lastSize+lastSize;
  }
  @SuppressWarnings("unchecked")
  @Override
  public void append(T addMe)  {
    if ( lastArrayCount == lastArrayLength) {
      if (previousArrays==null) {
        previousArrays 
          = new LinkedList<T[]>();
      }
      previousArrays.add ( lastArray );
      countOfItemsInPreviousArrays += 
        lastArrayCount;
      lastArrayLength = nextSize(lastArray.length);
      lastArray       = (T[]) new Object
                                  [lastArrayLength];
      lastArrayCount  = 0;
    }
    lastArray[lastArrayCount] = addMe;
    ++lastArrayCount;
  }
  private class LABIterator 
    implements BucketIterator<T> {
    private Iterator<T[]> previousArrayIterator;
    private T[]           currentArray;
    private int           currentIndex;
    private int           currentArrayCount;
    public LABIterator() {
        this.currentIndex = 0;
      if (previousArrays!=null) {
        this.previousArrayIterator
          = previousArrays.iterator();
        if (previousArrayIterator.hasNext()) {
          this.currentArray
            = (T[]) previousArrayIterator.next();
          this.currentArrayCount = currentArray.length;
          return;
        }
      }
      this.previousArrayIterator = null;
      this.currentArray          = lastArray;
      this.currentArrayCount     = lastArrayCount;
    }
    @Override
    public boolean hasNext() {
      return (currentArray != null);
    }
    @Override
    public T next() {
      T v = currentArray[currentIndex];
      ++currentIndex;
      if (currentIndex < currentArrayCount ) {
        return v;
      }
      if ( previousArrayIterator != null 
           && previousArrayIterator.hasNext()) {
        currentArray
          = (T[]) previousArrayIterator.next();
        currentArrayCount 
          = currentArray.length;
      } else if (currentArray!=lastArray) {
        currentArray      = lastArray;			
        currentArrayCount = lastArrayCount;
      } else {
        currentArray      = null;
      }
      currentIndex = 0;
      return v;
    }
  }
  @Override
  public BucketIterator<T> getBucketIterator() {
    return new LABIterator();
  }
  @Override
  public int emit(T[] dest, int destPos) {
    if (previousArrays!=null) {
      for ( T[] prevArray: previousArrays ) {
        for (int i=0; i<prevArray.length; 
             ++i, ++destPos) {
          dest[destPos] = prevArray[i];
        }
      }
    }
    for (int i=0; i<lastArrayCount;
         ++i, ++destPos) {
      if ( dest.length<=destPos 
           || lastArray.length<=i) {
        return destPos;
      }
      dest[destPos] = lastArray[i];
    }
    return destPos;
  }
  @Override
  public IterableBucket<T> 
    newBucket(int size) {
    return new ListOfArraysBucket<T>(size);
  }
  public int size() {
    return lastArrayCount 
      + countOfItemsInPreviousArrays;
  }
  @Override
  public void clear() {
    countOfItemsInPreviousArrays = 0;
    lastArrayCount = 0;
    previousArrays.clear();
  }
}
