package jsortie.object.treesort.binary.balanced;

import java.util.Comparator;

import jsortie.object.bucket.SortingBucket;

public class MonkeyPuzzleBucket<T>
  implements SortingBucket<T> {
  //
  //Technically this is *not* a weight-balanced tree (because it makes 
  //no guarantees about the ratio of child weight to parent weight,
  //see https://en.wikipedia.org/wiki/Weight-balanced_tree).
  //
  //Re-balancing occurs "up the insertion path" when, for an
  //ancestor, a, a's subtree size exceeds that of a's "great aunt".
  //The difference between the two sizes is the reduction in the total
  //path length of the tree, so *every* such re-balance is favorable.
  //Note that there may be *more* than one re-balance for an insertion.
  //
  protected class MonkeyPuzzleNode {
    T                v;
    MonkeyPuzzleNode leftChild;
    MonkeyPuzzleNode rightChild;
    MonkeyPuzzleNode parent;
    int              size; 
    public MonkeyPuzzleNode() {
      //Only used for... nowhere
      v = null;
      leftChild = rightChild = parent = null;
    }
    public MonkeyPuzzleNode
      ( T value ) {
      v            = value;
      size         = 1;
      leftChild    = nowhere;
      rightChild   = nowhere;
      parent       = nowhere;
    }
    public MonkeyPuzzleNode
      ( T value
      , MonkeyPuzzleNode left
      , MonkeyPuzzleNode right) {
      v            = value;
      size         = left.size + right.size + 1;
      leftChild    = left;
      rightChild   = right;
      parent       = nowhere;
      if (left!=nowhere) {
        left.parent  = this;
      }
      if (right!=nowhere) {
        right.parent = this;
      }
    }
    public String toString() {
      if (v==null) {
        return "null";
      } else {
        return v.toString();
      }
    }
    public int getTotalPathLength() {
      if (size<2) return 0;
      return size - 1
             + leftChild.getTotalPathLength() 
             + rightChild.getTotalPathLength() ;
    }
    public void replaceChild(MonkeyPuzzleNode node
      , MonkeyPuzzleNode replacement) {
      if (leftChild==node) {
        leftChild = replacement;
      } else if ( rightChild==node ){
        rightChild = replacement;
      } else if ( this==nowhere) {
        root = replacement;
      }
      replacement.parent = this;
    }
    public void walkTheGreenMile() {
      //Called when a node is being removed.
      //To be overridden in subclasses 
      //(that want to fix up iterators that
      //reference a node being removed).
    }
  }  
  MonkeyPuzzleNode nowhere 
    = new MonkeyPuzzleNode();
    //Functions like a dereferenceable null 
    //(Heh! Sneaky, eh?  So we can ask size of
    //empty subtrees without checking for null).

  protected Comparator<? super T> comparator;
  protected MonkeyPuzzleNode root = nowhere;
  public MonkeyPuzzleBucket
    ( Comparator<? super T> comparator ) {
    this.comparator = comparator;
  }
  protected MonkeyPuzzleNode newNode(T v) {
    return new MonkeyPuzzleNode(v);
  }
  protected MonkeyPuzzleNode newNode
    ( T v, MonkeyPuzzleNode left
    , MonkeyPuzzleNode right ) {
    return new MonkeyPuzzleNode(v, left, right);
  }
  @Override 
  public void append(T v) {
    MonkeyPuzzleNode node = newNode(v);
    if (root==nowhere) {
      makeRoot(node);
    } else {
      MonkeyPuzzleNode scan=root;
      MonkeyPuzzleNode parent;
      boolean goLeft;
      do {
        ++scan.size;
        goLeft   = (comparator.compare(v,scan.v)<0);
        parent   = scan;
        scan     = goLeft ? scan.leftChild : scan.rightChild;
      } while (scan!=nowhere);
      if (goLeft) {
        parent.leftChild = node;
      } else {
        parent.rightChild = node;
      }
      node.parent = parent;
      fixTreeAfterInsert(parent);
    }
  }
  protected void makeRoot(MonkeyPuzzleNode node) {
    root          = node;
    root.parent   = nowhere;
  }
  protected void fixTreeAfterInsert
    ( MonkeyPuzzleNode node ) {
    MonkeyPuzzleNode parent      = node.parent;
    MonkeyPuzzleNode grandparent = parent.parent;
    if ( parent != nowhere 
          && grandparent != nowhere ) {
      do {
        if ( grandparent.size - parent.size 
           < node.size ) {
         node = promote(node);
         parent = node.parent;
         if ( parent == nowhere ) {
           return;
         }
        } else {
          node        = parent;
          parent      = grandparent;
        }
        grandparent = parent.parent;
      } while ( grandparent != nowhere );
    }
  }
  protected MonkeyPuzzleNode promote(MonkeyPuzzleNode node) {
    //System.out.println("Promoting " + node.toString());
    MonkeyPuzzleNode parent       = node.parent;
    MonkeyPuzzleNode grandparent  = parent.parent;
    MonkeyPuzzleNode greatgrandma = grandparent.parent;
    if (grandparent.leftChild==parent) {
      if (parent.leftChild==node) {
        grandparent.leftChild = parent.rightChild;
        parent.rightChild.parent = grandparent;
        return rebuildLocalTree
               ( greatgrandma, grandparent, node
               , parent, grandparent);
      } else {
        parent.rightChild = node.leftChild;
        node.leftChild.parent = parent;
        grandparent.leftChild = node.rightChild;
        node.rightChild.parent = grandparent;
        return rebuildLocalTree
               ( greatgrandma, grandparent, parent
               , node, grandparent);
      }
    } else {
      if (parent.leftChild==node) {
        parent.leftChild = node.rightChild;
        node.rightChild.parent = parent;
        grandparent.rightChild = node.leftChild;
        node.leftChild.parent = grandparent;
        return rebuildLocalTree
               ( greatgrandma, grandparent, grandparent
               , node, parent);
      } else {
        //System.out.println("Case 4");
        grandparent.rightChild  = parent.leftChild;
        grandparent.rightChild.parent = grandparent;
        return rebuildLocalTree
               ( greatgrandma, grandparent, grandparent
               , parent, node);
      }
    }
  }
  protected MonkeyPuzzleNode rebuildLocalTree
    ( MonkeyPuzzleNode below
    , MonkeyPuzzleNode former,  MonkeyPuzzleNode a
    , MonkeyPuzzleNode b, MonkeyPuzzleNode c) {
    if (below==nowhere) {
      root = b;
    } else if (below.leftChild==former) {
      below.leftChild = b;
    } else {
      below.rightChild = b;
    }
    /*System.out.println
      ( "a=" + a.toString() + ", b=" + b.toString() 
      + ", c=" + c.toString());*/
    b.parent     = below;
    b.leftChild  = a;
    a.size       = a.leftChild.size + a.rightChild.size + 1;
    a.parent     = b;
    //System.out.println("a.size = " + a.size);
    b.rightChild = c;
    c.size       = c.leftChild.size + c.rightChild.size + 1;
    //System.out.println("c.size = " + c.size);
    c.parent     = b;
    b.size       = a.size + c.size + 1;
    //System.out.println("b.size = " + b.size);
    return b;
  }
  protected void dumpTree(String s) {
    System.out.println("--=------------" + s);
    dumpTree(root, "", root.size+5);
    System.out.println("");
  }
  protected int dumpTree
    ( MonkeyPuzzleNode top, String string, int max ) {
    if (top.size<1) {
      return max;
    }
    if (0<max && 0<top.leftChild.size) {
      max=dumpTree(top.leftChild, string+"  ", max);
    }
    if (0<max && top.v!=null) {
      System.out.println(string + top.v.toString());
    }
    --max;
    if (0<max && 0<top.rightChild.size) { 
      max=dumpTree(top.rightChild, string+"  ", max);
    }
    return max;
  }
  @Override
  public int emit(T[] vArray, int start) {
    return emitSubTree(root, vArray, start);
  }
  protected int emitSubTree
    ( MonkeyPuzzleNode node, T[] vArray, int w ) {
    while ( node != nowhere && 0 < node.size ) {
      w = emitSubTree
          ( node.leftChild, vArray, w );
      vArray[w] = node.v;
      ++w;
      node = node.rightChild;
    }
    return w;
  }
  @Override
  public SortingBucket<T> newBucket(int size) {
    return new MonkeyPuzzleBucket<T>
               ( comparator );
  }
  public int size() {
    return root.size;
  }
  @Override
  public void clear() {
    root = nowhere;
  }
  public double  getTotalPathLength() {
    return root.getTotalPathLength(); 
  }
}
