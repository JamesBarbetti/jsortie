package jsortie.object.treesort.higherdegree;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;

public class FancierSortingATree<T> 
  extends SortingATree<T> {
  protected class FancierInteriorNode 
    extends ATreeInteriorNode {
    @SuppressWarnings("unchecked")
    public    void    accept(T item) {
      int i = 0;
      while ( i < valueCount ) {
        i += i + ( comparator.compare
                   ( item, (T) valuesAndChildren[i] )
                   < 0 ? 1 : 2 );
      }
      ((ATreeNode)(valuesAndChildren[i])).accept(item);
    }
    public boolean addChild
      ( ATreeNode oldChild, T value, ATreeNode newChild ) {
      int countOfValuesAndChildren = valueCount*2+1;
      if ( countOfValuesAndChildren == valuesAndChildren.length) {
        ATreeInteriorNode rhs = split();
        if (rhs.addChild(oldChild, value, newChild)) {
          return true;
        }
        countOfValuesAndChildren = valueCount*2+1;
      }
      Object[] flat 
        = new Object[valueCount*2 + 3];
      if ( countOfValuesAndChildren 
           < flatten( 0, oldChild, value, newChild, flat, 0)) {
        ++valueCount;
        retree(flat, 0, 0);
        newChild.parent = this;
        return true;
      }
      return false;
    }
    @SuppressWarnings("unchecked")
    public ATreeInteriorNode split() {
      if (parent==null) {
        parent = new FancierInteriorNode();
        parent.valuesAndChildren[0] = this;
      }
      FancierInteriorNode rhs       = new FancierInteriorNode();
      int                 leftCount = valueCount/2;
      Object[]            flat      = new Object[valueCount*2 + 1];
      flatten( 0, rhs, null, null, flat, 0);
      rhs.valueCount = valueCount - leftCount - 1;
      valueCount = leftCount;
      int flatPos = retree( flat, 0, 0 );
      rhs.retree( flat, flatPos+1, 0);
      parent.addChild( this, (T) flat[flatPos], rhs);
      for (int i=flatPos+1; i<flat.length; i+=2) {
        ((ATreeNode)flat[i]).parent = rhs;
      }
      return rhs;
    }
    private int flatten
      ( int readPos, Object oldChild
      , Object newValue, Object newChild
      , Object[] dest, int destPos) {
      while (readPos<valueCount) {
        destPos = flatten ( readPos + readPos + 1, oldChild
                          , newValue, newChild, dest, destPos );
        dest[destPos++] = valuesAndChildren[readPos];
        readPos += readPos + 2;				
      }
      dest[destPos++] = valuesAndChildren[readPos];
      if ( valuesAndChildren[readPos] == oldChild ) {
        dest[destPos++] = newValue;
        dest[destPos++] = newChild;
      }
      return destPos;
    }
    private int retree
      ( Object flat[], int flatPos, int treePos ) { 
      //returns updated flatPos
      while (treePos<valueCount) {
        flatPos = retree( flat, flatPos, treePos + treePos + 1);
        valuesAndChildren[treePos] = flat[flatPos++];
        treePos += treePos + 2;
      }
      valuesAndChildren[treePos] = flat[flatPos++];
      return flatPos;
    }
    public int emit(T [] vArray, int pos) {
      return emit2(0, vArray, pos);
    }
    @SuppressWarnings("unchecked")
    private int emit2(int readPos, T dest[], int destPos) {
      while (readPos<valueCount) {
        destPos = emit2( readPos + readPos + 1, dest, destPos);
        dest[destPos] = (T) valuesAndChildren[readPos];
        ++destPos;
        readPos += readPos + 2;
      }
      return ((ATreeNode)(valuesAndChildren[readPos])).emit(dest, destPos);
    }
  }
  protected class FancierLeafNode 
    extends ATreeLeafNode {
    protected void getAdoptedIfOrphaned() {
      if (parent==null) {
        parent = new FancierInteriorNode();
        parent.valuesAndChildren[0] = this;
      }		
    }
  }
  public ATreeLeafNode getNewLeafNode() {
    return new FancierLeafNode();
  }
  public SortingATree<T> getNewTree
    ( Comparator<? super T> comparer
    , ObjectRangeSorter<T> sorter ) {
    return new FancierSortingATree<T>(comparer, sorter);
  }
  public FancierSortingATree
    ( Comparator<? super T> comparer
    , ObjectRangeSorter<T> sorter ) {
    super(comparer, sorter);
  }
}