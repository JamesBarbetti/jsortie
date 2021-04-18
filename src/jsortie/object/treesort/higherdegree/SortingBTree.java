package jsortie.object.treesort.higherdegree;

import java.util.Comparator;

import jsortie.object.NullObjectRangeSorter;
import jsortie.object.ObjectRangeSortHelper;
import jsortie.object.janitor.ObjectBinaryInsertionSort;

public class SortingBTree<T> 
  extends FancierSortingATree<T> {
  ObjectBinaryInsertionSort<T> ibsort;
  public SortingBTree(Comparator<? super T> comparer) {	  
	super(comparer, new NullObjectRangeSorter<T>());
	countOfObjectsPerInteriorNode = 15; //might as well be odd.  
    countOfValuesPerLeaf = 31;  //must be odd.  Ideally 1 less than a power of 2
    this.root     = getNewLeafNode();
    this.scratch  = getNewLeafNode();
    this.ibsort   = new ObjectBinaryInsertionSort<T>();
  }
  public SortingBTree
    ( Comparator<? super T> comparer, int interiorValues
    , int leafValues) {	  
    super(comparer, new NullObjectRangeSorter<T>());
    interiorValues += ((interiorValues&1)==0) ? 1 : 0;
    leafValues     += ((leafValues&1)==0)     ? 1 : 0;
    countOfObjectsPerInteriorNode = interiorValues; //might as well be odd.  
    countOfValuesPerLeaf = leafValues;  //must be odd.  
    //(and is, ideally, 1 less than a power of 2).
    this.root     = getNewLeafNode();
    this.scratch  = getNewLeafNode();
    this.ibsort   = new ObjectBinaryInsertionSort<T>();
  }
  public ATreeLeafNode getNewLeafNode() {
    return new BTreeLeafNode();
  }
  protected class BTreeLeafNode 
    extends ATreeLeafNode {
    @Override
    public    void    accept(T item) {
      int pos = ibsort.findPostInsertionPoint
                ( comparator, values, 0, valueCount, item );
      for (int shift=valueCount; pos<shift; --shift) {
        values[shift] = values[shift-1];
      }
      values[pos] = item;
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
      return pos + valueCount;
    }
  }
  public void sort
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    if (start<stop) {
      SortingBTree<T> tree 
        = new SortingBTree<T> ( comparator
                       , countOfObjectsPerInteriorNode
                       , countOfValuesPerLeaf);
      for (int i=start; i<stop; ++i) {
        tree.root.accept(vArray[i]);
      }
      tree.emit( vArray, start);
    }	  
  }
}
