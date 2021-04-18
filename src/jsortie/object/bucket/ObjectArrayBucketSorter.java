package jsortie.object.bucket;

import java.util.Comparator;
import jsortie.object.ObjectRangeSorter;
import jsortie.object.bucket.IterableBucket.BucketIterator;

public class ObjectArrayBucketSorter<T> 
  implements ObjectRangeSorter<T> {
  ObjectRangeSorter<T> m_sorter;
  public ObjectArrayBucketSorter
    ( ObjectRangeSorter<T> sorterToUse) {
    m_sorter = sorterToUse;
  }
  protected long loadTimeInNanoseconds=0;
  protected long unloadTimeInNanoseconds=0;
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
        , T[] vContainingArray
    , int start, int stop) {
    long loadStart = System.nanoTime();
    DynamicArrayBucket<T> bucky
      = new DynamicArrayBucket<T>
        ( stop-start );
    for (int r=start; r<stop; ++r) {
      bucky.append(vContainingArray[r]);
    }
    loadTimeInNanoseconds
      += System.nanoTime() - loadStart;
    bucky.sortWith(m_sorter, comparator);
    long unloadStart = System.nanoTime();
    //either: ...
    BucketIterator<T> it =
        bucky.getBucketIterator();
    int w=start;
    while (it.hasNext()) {
      vContainingArray[w] = it.next();
      ++w;
    }
    //...or: bucky.emit(vContainingArray, start);
    unloadTimeInNanoseconds
      += System.nanoTime() - unloadStart;
  }
  public void zeroTimers() {
    loadTimeInNanoseconds = 0;
    unloadTimeInNanoseconds = 0;
  }
  public long getLoadingTimeInNanoSeconds() {
    return loadTimeInNanoseconds;
  }
  public long getUnloadingTimeInNanoSeconds() {
    return unloadTimeInNanoseconds;
  }
}
