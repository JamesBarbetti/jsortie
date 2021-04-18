package jsortie.object.treesort.higherdegree;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.ObjectRangeSortHelper;
import jsortie.object.janitor.ObjectInsertionSort;
import jsortie.object.mergesort.ArrayObjectMergesort;

public class SortingATree<T> {
  protected int countOfValuesPerLeaf          = 8191; 
  //must be odd.  Ideally 1 less than a power of 2
  
  protected int countOfObjectsPerInteriorNode = 255;
  //must be odd.  Ideally 1 less than a power of 2	
  
  public String toString() {
    return this.getClass().getSimpleName()
    + "(" + this.comparator.toString() 
    + "," + countOfObjectsPerInteriorNode
    + "," + countOfObjectsPerInteriorNode +")";
  }	
  protected class ATreeNode {
    public    ATreeInteriorNode  parent;
    public    int                valueCount;
    public    void               accept(T item)    {}
    public    int emit(T [] vArray, int pos) { return -1; }
  }
  protected class ATreeInteriorNode extends ATreeNode {
    public Object valuesAndChildren[] 
      = new Object[countOfObjectsPerInteriorNode];    
      //Must be odd.  Even numbered entries are pointers to children.
      //Odd-numbered entries are the pivot objects chosen from
      //the objects being sorted.  Putting both pivot values and children
      //in the one array saves about 12 bytes per instance of
      //LazyBTreeInteriorNode.
      //
      //Also see FancierATreeInteriorNode, which uses the same array declaration, but
      //"lays out" the array elements themselves *very* differently indeed.

    @SuppressWarnings("unchecked")
    public    void    accept(T item) {
      int lo = 1;
      int hi = valueCount*2+1;

      while ( lo < hi ) {
        int middle = lo + ((hi - lo ) / 4) * 2;
        if ( comparator.compare
             ( item, (T) valuesAndChildren[middle] )
             < 0 ) {
          hi = middle;
        } else {
          lo = middle + 2;
        }
      }
      ((ATreeNode) valuesAndChildren[hi-1]).accept(item) ;
    }

    @SuppressWarnings("unchecked")
    public ATreeInteriorNode split() {
      if (parent==null) {
        parent = new ATreeInteriorNode();
        parent.valuesAndChildren[0] = this;
      }
      ATreeInteriorNode rhs = new ATreeInteriorNode();
      int         leftCount = valueCount/2;

      ObjectRangeSortHelper.copyRange
        ( valuesAndChildren,  leftCount*2+2
        , valueCount*2+1, rhs.valuesAndChildren, 0);

      rhs.valueCount = valueCount - leftCount - 1;
      valueCount = leftCount;		
      parent.addChild 
        ( this, (T) valuesAndChildren[leftCount*2+1], rhs);		

      for (int i=0; i<=rhs.valueCount*2; i+=2) {
        ((ATreeNode)rhs.valuesAndChildren[i]).parent = rhs;
      }
      return rhs;
    }

    public boolean addChild
      ( ATreeNode oldChild, T value, ATreeNode newChild ) {
      if (valueCount*2+1 == valuesAndChildren.length) {
        ATreeInteriorNode rhs = split();
        if (rhs.addChild(oldChild, value, newChild)) {
          return true;
        }
      }
			
      int i=0;
      for (i=0; i<=valueCount*2; i+=2) {
        if ( valuesAndChildren[i] == oldChild ) {
          for (int j=valueCount*2; j>i; --j) {
            valuesAndChildren[j+2] = valuesAndChildren[j];
          }
          valuesAndChildren[i+1] = value;
          valuesAndChildren[i+2] = newChild;
          ++valueCount;
          newChild.parent = this;
          return true;
        }
      }
      return false;
    }	

    @SuppressWarnings("unchecked")
    public    int emit(T[] vArray, int pos) {
      for (int i=0; i<valueCount*2; i+=2) {
        pos = ((ATreeNode)valuesAndChildren[i]).emit(vArray, pos);
        vArray[pos++] = (T) valuesAndChildren[i+1];
      }
      pos = ((ATreeNode)valuesAndChildren[valueCount*2]).emit(vArray, pos);
      return pos;			
    }
  }
	
  protected class ATreeLeafNode extends ATreeNode {
    protected int sortedValueCount; //only used on leaf nodes
    @SuppressWarnings("unchecked")
    protected T   values[] 
      = (T[]) new Object[countOfValuesPerLeaf];

    @Override
    public    void    accept(T item) {
      values[valueCount] = item;
      valueCount++;
      if (valueCount<values.length) {
        return;
      }
      split();
      if (root.parent != null) {
        root = root.parent;
      }
    }
		
    protected void getAdoptedIfOrphaned() {
      if (parent==null) {
        parent = new ATreeInteriorNode();
        parent.valuesAndChildren[0] = this;
      }
    }

    public ATreeLeafNode split() {
      getAdoptedIfOrphaned();
      extendSortedPortion();

      //   Old          New
      //   ===          ===
      //   S U    
      //   S            Sorted(U) --after copy and sort range  (1) 
      //   S Sorted(U)            --after merge to right       (2)
      //   S            Sorted(U) --after copy range from left (3)
      //

      ATreeLeafNode rhs = getNewLeafNode();
      if (sortedValueCount < valueCount) {
        int unsorted = valueCount - sortedValueCount;
        sorter.sortRange
          ( comparator, values, sortedValueCount, valueCount );
        ObjectRangeSortHelper.copyRange
          ( values, sortedValueCount, valueCount, rhs.values, 0 );
        merger.mergeToRight
          ( comparator, rhs.values, 0, unsorted
          , values, 0, sortedValueCount, values, 0);
      }
      moveRightHalfTo(rhs);
      return rhs;
    }

    public void moveRightHalfTo(ATreeLeafNode rhs) {
      int leftCount  = valueCount / 2;
      int rightCount = valueCount - leftCount - 1;
      ObjectRangeSortHelper.copyRange
        ( values, leftCount+1, valueCount, rhs.values, 0 );
      valueCount = leftCount;
      sortedValueCount = leftCount;
      rhs.valueCount = rightCount;
      rhs.sortedValueCount = rightCount;
      parent.addChild(this, values[leftCount], rhs);
    }
		
    public    int emit(T [] vArray, int pos) {
      extendSortedPortion();
      if (sortedValueCount == valueCount) {
        ObjectRangeSortHelper.copyRange
          ( values, 0, valueCount, vArray, pos );
      } else if (sortedValueCount < valueCount/4) {
        ObjectRangeSortHelper.copyRange 
          ( values, 0, valueCount, vArray, pos );		
        sorter.sortRange
          ( comparator, vArray, pos, pos+valueCount );
      } else {
        sorter.sortRange 
          ( comparator, values
          , sortedValueCount, valueCount);
        merger.mergeToLeft 
          ( comparator, values, sortedValueCount
          , valueCount, values, 0, sortedValueCount
          , vArray, pos);
      }
      return pos + valueCount;
    }
    
    public void extendSortedPortion() {
      if (sortedValueCount==0) {
        ++sortedValueCount;
      }
      while (sortedValueCount < valueCount &&
        comparator.compare ( values[sortedValueCount-1]
                           , values[sortedValueCount]) <=0 ) {
        ++sortedValueCount;
      }
    }
  }
  protected Comparator<? super T>   comparator;
  protected ArrayObjectMergesort<T> merger;
  protected ObjectRangeSorter<T>    sorter;
  protected ATreeNode               root;
  protected ATreeLeafNode           scratch; 
  public ATreeLeafNode getNewLeafNode() {
    return new ATreeLeafNode();
  }
  public SortingATree
    ( Comparator<? super T> comparer
    , ObjectRangeSorter<T> sorter) {
    this.comparator = comparer;
    this.sorter   = sorter;
    this.merger   = new ArrayObjectMergesort<T>
                        ( new ObjectInsertionSort<T>(), 8 );
    this.root     = getNewLeafNode();
    this.scratch  = getNewLeafNode();
  }
  protected int emit(T[] vArray, int start) {
    return root.emit( vArray, start );
  }
  public SortingATree<T> getNewTree
    ( Comparator<? super T> comparer
    , ObjectRangeSorter<T> sorter) {
    return new SortingATree<T>(comparer, sorter);
  }
  public void sort(T[] vArray, int start, int stop) {
    if (start<stop) {
      SortingATree<T> tree = getNewTree(comparator, sorter);
      tree.countOfObjectsPerInteriorNode 
        = this.countOfObjectsPerInteriorNode;
      tree.countOfValuesPerLeaf 
        = this.countOfValuesPerLeaf;
      for (int i=start; i<stop; ++i) {
        tree.root.accept(vArray[i]);
      }
      tree.emit( vArray, start);
    }
  }
  public Comparator<? super T> getComparator() {
    return this.comparator;
  }	
}
