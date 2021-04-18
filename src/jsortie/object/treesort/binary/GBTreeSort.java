package jsortie.object.treesort.binary;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;

public class GBTreeSort <T> 
  implements ObjectRangeSorter<T> {
  public String toString() {
    return this.getClass().getSimpleName(); 
  }
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    GBTreeBucket<T> bucket 
      = new GBTreeBucket<T>(comparator);
    for (int scan=start;scan<stop;++scan) {
      bucket.append(vArray[scan]);
    }
    bucket.emit(vArray, start);
  }
}