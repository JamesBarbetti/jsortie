package jsortie.object.treesort.binary.balanced;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import jsortie.exception.SortingFailureException;
import jsortie.object.bucket.IterableBucket;

public class MonkeyPuzzleContainer<T> 
  extends MonkeyPuzzleBucket<T>
  implements IterableBucket<T>, Collection<T> {
  public MonkeyPuzzleContainer
    ( Comparator<? super T> comparator ) {
    super(comparator);
  }
  protected class DeletableMonkeyPuzzleNode
    extends MonkeyPuzzleNode {
    MonkeyPuzzleIterator firstIterator;
    public void walkTheGreenMile() {
      MonkeyPuzzleIterator i
        = firstIterator;
      if (i==null) {
        return;
      }
      MonkeyPuzzleNode succ 
        = successorInTree ( this );
      do {
        MonkeyPuzzleIterator o = i;
        i = i.nextIterator;
        o.attach ( succ ); 
      } while (i!=null);
    }
  }
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public boolean add(T v) {
    super.append(v);
    return true;
  }
  @Override public boolean addAll
    ( Collection<? extends T> c ) {
    boolean anyAdded = false;
    for (T v : c) {
      anyAdded |= add(v);
    }
    return anyAdded;
  }
  @Override public void clear() {
    root = nowhere;
  }
  @Override public boolean contains(Object v) {
    @SuppressWarnings("unchecked")
    boolean contained 
      = (locateHighest((T)v) != null);
    return contained;
  }
  @Override public boolean containsAll
    ( Collection<?> c ) {
    for (Object v: c) {
      if (!contains(v)) return false;
    }
    return true;
  }
  public boolean merge(T v) {
    //Equivalent to: contains(v) ? false : add(v);
    //...but a bit quicker (and a lot longer)
    MonkeyPuzzleNode node;
    if (root==nowhere) {
      //Tree was empty... Add it, return true
      node = newNode(v);
      makeRoot(node);
    } else {
      //Search for where to put it (it'll be... the 
      //rightChild of the last item equal to it, if
      //there are any such items).  Don't adjust weights
      //on the way down (not yet!).
      MonkeyPuzzleNode scan=root;
      MonkeyPuzzleNode parent;
      boolean goLeft;
      do {
        goLeft   = (comparator.compare(v,scan.v)<0);
        parent   = scan;
        scan     = goLeft ? scan.leftChild : scan.rightChild;
      } while (scan!=nowhere);
      if (goLeft) {
        node = newNode(v);
        parent.leftChild = node;
      } else {
        if (comparator.compare(v,parent.v)==0) {
          //already there. Return false.
          return false;
        }
        node = newNode(v);
        parent.rightChild = node;
      }
      node.parent = parent;       //Set upward link,
      adjustWeights(parent,1);    //(at last) adjust the weights 
      fixTreeAfterInsert(parent); //...and re-balance as needed
    }
    return true;
  }
  @Override public boolean isEmpty() {
    return root == nowhere;
  }
  protected class MonkeyPuzzleIterator 
    implements Iterator<T>, BucketIterator<T> {
      public MonkeyPuzzleIterator prevIterator;
      public MonkeyPuzzleIterator nextIterator;
      protected MonkeyPuzzleNode nextNode;
      public MonkeyPuzzleIterator () {
        nextNode 
          = (root==nowhere) 
            ? nowhere 
            : firstInSubTree(root);
      }
      public void attach(MonkeyPuzzleNode succ) {
        if ( nextNode instanceof 
             MonkeyPuzzleContainer.DeletableMonkeyPuzzleNode) {
          @SuppressWarnings("unchecked")
          DeletableMonkeyPuzzleNode old 
            = (MonkeyPuzzleContainer<T>.DeletableMonkeyPuzzleNode) 
              nextNode; 
          //Unlink this iterator from the list from the node
          //it is... leaving.
          if (prevIterator==null) {
            old.firstIterator = nextIterator;
            if (nextIterator!=null) {
              nextIterator.prevIterator = null;
            }
          } else {
            prevIterator.nextIterator 
              = this.nextIterator;
            if (nextIterator!=null) {
              nextIterator.prevIterator 
                = this.prevIterator;
            }
          }
        }
        if ( succ instanceof 
             MonkeyPuzzleContainer.DeletableMonkeyPuzzleNode) {
          //Link this iterator into the list for the successor node
          @SuppressWarnings("unchecked")
          DeletableMonkeyPuzzleNode rep 
            = (MonkeyPuzzleContainer<T>.DeletableMonkeyPuzzleNode) 
              succ;
          nextIterator = rep.firstIterator;
          if ( nextIterator != null ) {
            nextIterator.prevIterator = this;
          }
          rep.firstIterator = this;          
        } else {
          prevIterator = null;
          nextIterator = null;
        }
        nextNode = succ;
      }
      @Override public boolean hasNext() {
        return nextNode != nowhere;
      }
      @Override public T next() {
        T v = nextNode.v;
        attach(successorInTree(nextNode));
        return v;
      }
      @Override public void remove() {
        if (nextNode != nowhere ) {
          MonkeyPuzzleNode deadNode 
            = predecessorInTree(nextNode);
          if ( deadNode != nowhere ) {
            removeNode(deadNode, true);
          }
        }
      }
    }
  @Override
  public Iterator<T> iterator() {
    return new MonkeyPuzzleIterator();
  }
  protected MonkeyPuzzleNode successorInTree
    ( MonkeyPuzzleNode node ) {
    if ( node == nowhere ) {
      throw new SortingFailureException
        ("Attempt to access after end-of-tree");
    }
    if ( node.rightChild != nowhere ) {
      return firstInSubTree(node.rightChild);
    }
    for (;;) {
      System.out.println("Up " + node.toString());
      MonkeyPuzzleNode child = node;
      node = node.parent;
      if (node == nowhere) {
        return nowhere;
      }
      if ( node.leftChild == child ) {
        System.out.println("Found" + node.toString());
        return node;
      }
    }
  }
  protected MonkeyPuzzleNode predecessorInTree
    ( MonkeyPuzzleNode node ) {
    if ( node == nowhere ) {
      if ( root != nowhere ) {
        return lastInSubTree(root);
      }
      return nowhere;
    }
    if ( node.leftChild != nowhere ) {
      return lastInSubTree(node.leftChild);
    }
    for (;;) {
      MonkeyPuzzleNode child = node;
      node = node.parent;
      if (node == nowhere) {
        return nowhere;
      }
      if ( node.rightChild == child ) {
        return node;
      }
    }
  }
  @Override 
  public boolean remove(Object v) {
    @SuppressWarnings("unchecked")
    MonkeyPuzzleNode node 
      = locateHighest((T)v);
    if (node==null) {
      return false;
    }
    removeNode(node, true);
    return true;
  }
  @Override
  public boolean removeAll(Collection<?> that) {
    //Note: This is bugwards compatible with 
    //AbstractSet<T>'s implementation.  
    //It ought to just be: removeAllIn().
    //(If we honoured the Collection<T> 
    //convention, as it is written).
    return that.size() < this.size()  
      ? removeEachOf(that) 
      : removeAllIn(that);
  }
  public boolean removeEachOf(Collection<?> that) {
    boolean removedAny = false;
    for (Object v: that) {
      removedAny |= remove(v);
    }
    return removedAny;
  }
  public boolean removeAllIn(Collection<?> that) {
    boolean modified = false;
    Iterator<T> i = iterator(); 
    while ( i.hasNext() ) {
      if (that.contains(i.next())) {
        i.remove();
        modified = true; 
      }
    }
    return modified;
  }
  @Override 
  public boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException(
      this.getClass().getSimpleName()
      + " does not support retainAll");
  }
  @Override
  public Object[] toArray() {
    Object[] a = new Object[size()];
    emitToObjectArray(root, a, 0);
    return a;
  }
  private int emitToObjectArray
    ( MonkeyPuzzleNode treetop, Object[] a, int i ) {
    while (treetop!=nowhere) {
      i = emitToObjectArray
      ( treetop.leftChild, a, i );
      a[i] = treetop.v;
      ++i;
      treetop = treetop.rightChild;
    }
    return i;
  }
  @SuppressWarnings("unchecked")
  @Override public <T2> T2[] toArray(T2[] a) {
    if ( a.length < size() ) {
      a = (T2[])new Object[size()];
    }
    int i = emitToTypedArray(root, a, 0);
    if (i<a.length) {
      a[i] = null; 
      //Ick.  But Collection<T> says to do this!
      //But, when the convention is badly designed,
      //You gots to fall in with the bad design
      //(If you wants to honour the convention).
    }
    return a;
  }
  @SuppressWarnings("unchecked")
  private <T2> int emitToTypedArray
    ( MonkeyPuzzleNode treetop, T2[] a, int i ) {
    while (treetop!=nowhere) {
      i = emitToObjectArray
          ( treetop.leftChild, a, i );
      a[i] = (T2) (treetop.v);
      ++i;
      treetop = treetop.rightChild;
    }
    return i;
  }
  private MonkeyPuzzleNode locateHighest(T v) {
    MonkeyPuzzleNode scan  = root;
    while (scan!=nowhere) {
      int diff = comparator.compare(v, scan.v);
      if (diff < 0 ) {
        scan = scan.leftChild;
      } else if (0 < diff) {
        scan = scan.rightChild;
      } else {
        return scan;
      }
    }
    return null;
  }
  protected void removeNode
    ( MonkeyPuzzleNode node, boolean zap) {
    if (zap) {
      node.walkTheGreenMile();
    }
    MonkeyPuzzleNode parent = node.parent;
    if ( node.leftChild == nowhere ) {
      parent.replaceChild(node, node.rightChild);
    } else if ( node.rightChild == nowhere ) {
      parent.replaceChild(node, node.leftChild);
    } else {
      MonkeyPuzzleNode proxy
        = ( node.leftChild.size < node.rightChild.size)
        ? lastInSubTree(node.leftChild) 
        : firstInSubTree(node.rightChild);
      removeNode(proxy, false); 
      //checkTree("post-proxy-delete 
      //of proxy " + proxy.v.toString());
      rebuildLocalTree
        ( parent, node, node.leftChild
        , proxy, node.rightChild);
      return;
    }
    if (zap) {
      node.v = null;
      node.parent 
        = node.leftChild 
        = node.rightChild 
        = null;
      node.size = 0;
    }
    //checkTree("mid-delete");
    adjustWeights(parent,-1);
    fixTreeAfterDelete(parent);
  }
  protected MonkeyPuzzleNode lastInSubTree
    ( MonkeyPuzzleNode node ) {
    while (node.rightChild!=nowhere) { 
      node = node.rightChild;
    }
    return node;
  }
  protected MonkeyPuzzleNode firstInSubTree
    ( MonkeyPuzzleNode node ) { 
    while (node.leftChild!=nowhere) { 
      node = node.leftChild;
    } 
    return node;
  }
  private void adjustWeights
    ( MonkeyPuzzleNode node, int delta ) {
    for ( ; node != nowhere
          ; node=node.parent ) {
      node.size += delta;
    }
  }
  private void fixTreeAfterDelete
    ( MonkeyPuzzleNode node ) {
    if (node==nowhere) return;
    MonkeyPuzzleNode parent 
      = node.parent;
    if ( parent == nowhere ) return;
    do {
      MonkeyPuzzleNode sister =
        ( parent.leftChild == node )
        ? parent.rightChild : parent.leftChild;
      if (sister != nowhere) {
        MonkeyPuzzleNode leftNiece 
          = sister.leftChild;
        MonkeyPuzzleNode rightNiece
          = sister.rightChild;
        MonkeyPuzzleNode niece 
          = (leftNiece.size < rightNiece.size) 
          ? rightNiece : leftNiece;
        if ( niece != nowhere 
             && niece.size > node.size) {
          promote(niece);	
        }
      }
      node   = parent;
      parent = node.parent;
    } while ( parent != nowhere );
  }
  public T get(int index) {
    if (index<0 || size()<=index) {
      throw new IllegalArgumentException(
        "Cannot access node with rank " + index
        + " in a tree where the valid" 
        + " rank range is 0.." + (size()-1));
    }
    return getNodeAtIndex(root, index).v;
  }
  private MonkeyPuzzleNode getNodeAtIndex
    ( MonkeyPuzzleNode node, int index ) {
    while (1<node.size ) {
      int leftCount = node.leftChild.size; 
      if (index<leftCount) {
        node=node.leftChild;
      } else if (leftCount<index) {
        node   = node.rightChild;
        index -= leftCount+1;
      } else {
        return node;
      }
    }
    return node;
  }
  @Override 
  public BucketIterator<T> getBucketIterator() {
    return new MonkeyPuzzleIterator();
  }
  @Override 
  public IterableBucket<T> newBucket(int size) {
    return new MonkeyPuzzleContainer<T>
               ( comparator );
  } 
}
