package jsortie.object.bucket;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;

public class DynamicArrayBucket<T>
  implements IterableBucket<T> {
  protected T[] vArray;
  protected int count=0;
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
  @SuppressWarnings("unchecked")
  public DynamicArrayBucket(int size) {
    vArray = (T[]) new Object[size];
  }
  @Override
  public void append(T addMe) {
    if (count==vArray.length) {
      @SuppressWarnings("unchecked")
      T[] newArray = (T[]) new Object[count*2];
      for (int i=0; i<count; ++i) {
        newArray[i] = vArray[i];
      }
      vArray = newArray;
    }
    vArray[count] = addMe;
    ++count;
  }
  public void sortWith
    ( ObjectRangeSorter<T> sorter
    , Comparator<? super T> comparator) {
    sorter.sortRange(comparator, vArray, 0, count );
  }  
  public class DABIterator 
    implements BucketIterator<T> {
    private Object[] vArray;
    private int      index=0;
    private int      count;
    protected DABIterator
      ( Object[] vOnArray, int ofCount ) {
      vArray = vOnArray;
      count = ofCount;
    }
    @Override
    public boolean hasNext() {
      return (index<count);
    }
    @Override
    public T next() {
      @SuppressWarnings("unchecked")
      T v = (T)(vArray[index]);
      ++index;
      return v;
    }
  }
  @Override
  public BucketIterator<T> getBucketIterator() {
    return new DABIterator(vArray, count);
  }
  @Override
  public int emit
    ( T[] dest, int destStart ) {
    for ( int i=0; i<count; 
          ++i, ++destStart) {
      dest[destStart] = vArray[i]; 
    }
    return destStart;
  }
  @Override
  public IterableBucket<T> 
    newBucket(int size) {
    return new DynamicArrayBucket<T>(size);
  }
  public int size() {
    return count;
  }
  @Override public void clear() {
    count = 0;
  }
}
