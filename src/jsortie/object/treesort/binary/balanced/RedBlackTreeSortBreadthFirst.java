package jsortie.object.treesort.binary.balanced;

import java.util.Comparator;

public class RedBlackTreeSortBreadthFirst<T> 
 extends RedBlackTreeSort<T> {
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    int count = stop - start;
    RedBlackBucket<T> bucket 
      = new RedBlackBucket<T>(comparator);
    if (1<count) {
      int middle = count/2;
      bucket.append(vArray[start+middle]);
      double step = count/2.0;
      for (; 2.0 <= step; step *= 0.5) {
        for ( double sweep = start + step*.5
            ; sweep < stop; sweep += step) {
          int i = (int)Math.floor(sweep);
          bucket.append ( vArray[i] );
        }
      }
      if (1.0<step) {
        int i = start-1;
        for ( double sweep = start+step
            ; sweep <= stop; sweep += step) {
          int j = (int)Math.floor(sweep);
          for (++i; i<j; ++i) {
            bucket.append ( vArray[i] );
          }
        }
      }
      bucket.emit(vArray, start);
    }
  } 
}
