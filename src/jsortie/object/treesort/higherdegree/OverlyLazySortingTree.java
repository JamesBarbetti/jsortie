package jsortie.object.treesort.higherdegree;

import java.util.Comparator;

import jsortie.object.ObjectRangeSortHelper;
import jsortie.object.ObjectRangeSorter;
import jsortie.object.quicksort.selector.Algorithm489ObjectSelector;
import jsortie.object.quicksort.selector.KthStatisticObjectPartitioner;

public class OverlyLazySortingTree<T> 
  extends FancierSortingATree<T> {
  KthStatisticObjectPartitioner<T> selector;
  
  protected class OverlyLazyTreeLeafNode 
    extends ATreeLeafNode {
    @Override
    public    void    accept(T item) {
      values[valueCount] = item;
      ++valueCount;
      if (valueCount<values.length) {
        return;
      }
      split();
      if (root.parent != null) {
        root = root.parent;
      }
    }	
    @Override
    public ATreeLeafNode split() {
      getAdoptedIfOrphaned();		
      ATreeLeafNode rhs = getNewLeafNode();
      int leftCount  = valueCount / 2;
      int rightCount = valueCount - leftCount - 1;
      selector.partitionRangeExactly
        ( comparator, values, 0, valueCount, leftCount );
      ObjectRangeSortHelper.copyRange
        ( values, leftCount+1, valueCount, rhs.values, 0 );
      valueCount = leftCount;
      rhs.valueCount = rightCount;
      parent.addChild(this, values[leftCount], rhs);
      return rhs;		
    }
    @Override
    public int emit(T [] vArray, int pos) {
      ObjectRangeSortHelper.copyRange
        ( values, 0, valueCount, vArray, pos );
      sorter.sortRange
        ( comparator, vArray, pos, pos+valueCount );
      return pos + valueCount;
    }	
  }
  public OverlyLazySortingTree
    ( Comparator<? super T> comparator
    , ObjectRangeSorter<T> sorter) {
    super(comparator, sorter);
    this.selector 
      = new Algorithm489ObjectSelector<T>();
  }
  public OverlyLazySortingTree
    ( Comparator<? super T> comparator
    , KthStatisticObjectPartitioner<T> selectorToUse
    , ObjectRangeSorter<T> sorter) {
    super(comparator, sorter);
    this.selector = selectorToUse;
  }
  public ATreeLeafNode getNewLeafNode() {
    return new OverlyLazyTreeLeafNode();
  }
  public void sort(T[] vArray, int start, int stop) {
    if (start<stop) {
      OverlyLazySortingTree<T> tree 
        = new OverlyLazySortingTree<T>(comparator, sorter);
      for (int i=start; i<stop; ++i) {
        tree.root.accept(vArray[i]);
      }
      tree.emit( vArray, start);
    }
  }
}
