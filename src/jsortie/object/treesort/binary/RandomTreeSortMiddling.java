package jsortie.object.treesort.binary;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;

public class RandomTreeSortMiddling<T>
  implements ObjectRangeSorter<T> {
  public String toString() {
    return this.getClass().getSimpleName().toString();
  }	
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop ) {
    if (start+1<stop) {
      RandomTreeNode<T> root 
        = arrayToTree ( comparator, vArray, start, stop );
      root.emit(vArray, start);
    }
  }
  public RandomTreeNode<T> arrayToTree
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop ) {
    if (stop<=start) {
      return null;
    }
    int middle = start + (stop-start) / 2;
    RandomTreeNode<T> root 
      = new RandomTreeNode<T>
            (vArray[middle], middle);
    addArrayRangeToTree
      ( comparator, vArray, start,    middle, root);
    addArrayRangeToTree
      ( comparator, vArray, middle+1, stop,   root);
    return root;
  }
  protected void addArrayRangeToTree
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , RandomTreeNode<T> root) {
    while (start<stop) {
      int middle = start + (stop-start)/2;
      RandomTreeNode<T> node 
        = new RandomTreeNode<T>
              (vArray[middle], middle);
      root.accept(comparator, node);
      if (start<middle) {
        addArrayRangeToTree
          ( comparator, vArray, start,    middle, root );
      }
      start = middle +1;
    }
  }
}
