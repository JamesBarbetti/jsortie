package jsortie.object.treesort.higherdegree;

import java.util.Comparator;
import jsortie.object.ObjectRangeSorter;

public class BTreeSort<T> 
  implements ObjectRangeSorter<T> {	
  protected SortingBTree<T> btreeFactory;
  public String toString() {
    return this.getClass().getSimpleName() + "("
    		+  btreeFactory.toString() +")";
  }
  public BTreeSort
    ( Comparator<? super T> comparator ) {
    this.btreeFactory = new SortingBTree<T>(comparator);
  }
  public BTreeSort
    ( Comparator<? super T> comparator, int interior, int leaf ) {
    this.btreeFactory = new SortingBTree<T>(comparator, interior, leaf);
  }
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    btreeFactory.sort ( btreeFactory.getComparator()
                      , vArray, start, stop);
  }
}
