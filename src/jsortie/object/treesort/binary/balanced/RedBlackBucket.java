package jsortie.object.treesort.binary.balanced;

import java.util.Comparator;

import jsortie.object.bucket.SortingBucket;
import jsortie.object.treesort.TPLTally;

public class RedBlackBucket<T> 
  implements SortingBucket<T> {
  //	
  //Notes: 
  //
  //1. this is not a full red-black tree (as it does not 
  //   support remove(), contains() or clear(), among other things!).
  //2. the tree is *also* a linked list (each node has a link to its
  //   predecessor and successor, to speed up in-order traversal).
  //
  protected class RedBlackNode {
    T            v;
    RedBlackNode leftChild;
    RedBlackNode rightChild;
    RedBlackNode parent;
    boolean      isRed;
    public RedBlackNode() {}
    public RedBlackNode(T value) {
      v=value;
    }
    public double getTotalPathLength() { 
      return getTPLTally(this).totalPathLength;
    }
    public TPLTally getTPLTally(RedBlackNode node) {
      if (node==null) return TPLTally.nilTPLTally;
      TPLTally left  = getTPLTally(node.leftChild);
      TPLTally right = getTPLTally(node.rightChild);
      TPLTally total = new TPLTally();
      total.nodeCount
        = left.nodeCount + right.nodeCount + 1;
      total.totalPathLength 
        = left.totalPathLength + right.totalPathLength
      	+ total.nodeCount - 1;
      return total;
    }
  }
  protected Comparator<? super T> comparator;
  protected RedBlackNode  root = null;
  public RedBlackBucket(Comparator<? super T> c) {
    comparator = c;
  }
  
  @Override
  public void append(T v) {
    if (root==null) {
      root = new RedBlackNode(v);
    } else {
      RedBlackNode scan=root;
      boolean goLeft;
      RedBlackNode parent;
      do {
        goLeft = comparator.compare
                 ( v, scan.v ) < 0;
        parent = scan;
        scan 
          = goLeft 
            ? scan.leftChild 
            : scan.rightChild;
      } while (scan!=null);
      RedBlackNode newNode 
        = new RedBlackNode(v);
      newNode.isRed  = true;
      newNode.parent = parent;
      if (goLeft) {
        parent.leftChild = newNode;
      } else {
        parent.rightChild = newNode;
      }
      if (parent.isRed) {
        fixAboveRedNode(newNode);
      }
    }
  }
  protected void fixAboveRedNode(RedBlackNode node) {
    RedBlackNode parent = node.parent;
    do {
      //Here, node.isRed and parent.isRed are both true
      RedBlackNode grandparent = parent.parent;
      RedBlackNode uncle;
      
      //if uncle is null, or black, a single or double rotation is
      //enough to fix the problem, and we can bail out
      if (grandparent==null) {
        parent.isRed = false;
        return;
      } else if (parent == grandparent.leftChild) {
        uncle = grandparent.rightChild;
        if ( uncle==null || !uncle.isRed ) {
          if (node == parent.rightChild) {
            node        = node.parent;
            rotateLeft(node);
            parent      = node.parent;
          }
          parent.isRed      = false;
          grandparent.isRed = true;
          rotateRight(grandparent);
          return;
        }
      } else {
        uncle = grandparent.leftChild;
        if ( uncle==null || !uncle.isRed ) {
          if ( node == parent.leftChild) {
            node        = node.parent;
            rotateRight(node);
            parent      = node.parent;
          }
          parent.isRed      = false;
          grandparent.isRed = true;
          rotateLeft(grandparent);
          return;
        }
      }
      
      //but, if uncle is also red, we flip the colors of
      //parent, uncle, and grandparent.  But if the great-grandparent
      //is red that still leaves us with red-below-red, hence
      //the ...  while expression.
      parent.isRed      = false;
      uncle.isRed       = false;
      grandparent.isRed = true;
      node              = grandparent; //so, node.isRed will now be true.
      parent            = node.parent;
    } while (parent!=null && parent.isRed);
  }
  protected void rotateLeft
    ( RedBlackNode demoted ) {
    if (demoted==root) {
      RedBlackNode right = root.rightChild;
      root.rightChild    = right.leftChild;
      if (right.leftChild!=null) {
        right.leftChild.parent = root;
      }
      root.parent            = right;
      right.leftChild        = root;
      right.parent           = null;
      root                   = right;
      root.isRed             = false;
    } else {
      RedBlackNode parent    = demoted.parent;
      RedBlackNode promoted  = demoted.rightChild;
      if (demoted==parent.leftChild) {
        parent.leftChild     = promoted;
      } else {
        parent.rightChild    = promoted;
      }
      promoted.parent        = parent;
      demoted.parent         = promoted;
      if (promoted.leftChild!=null) {
    	  promoted.leftChild.parent = demoted;
      }
      demoted.rightChild     = promoted.leftChild;
      promoted.leftChild     = demoted;      
    }
  }
  protected void rotateRight ( RedBlackNode demoted ) {
    if (demoted==root) {
      RedBlackNode left         = root.leftChild;
      root.leftChild            = left.rightChild;
      if (left.rightChild!=null) {
        left.rightChild.parent  = root;
      }
      root.parent               = left;
      left.rightChild           = root;
      left.parent               = null;
      root                      = left;
      root.isRed                = false;
    } else {
      RedBlackNode parent       = demoted.parent;
      RedBlackNode promoted     = demoted.leftChild;
      if (demoted==parent.rightChild) {
        parent.rightChild       = promoted;
      } else {
        parent.leftChild        = promoted;
      }
      promoted.parent           = parent;
      demoted.parent            = promoted;
      if (promoted.rightChild!=null) {
        promoted.rightChild.parent = demoted;
      }
      demoted.leftChild         = promoted.rightChild;
      promoted.rightChild       = demoted;      
    }
  }
  @Override
  public int emit ( T[] vArray, int start ) {
    return emitSubTree(root, vArray, start);
  }
  protected int emitSubTree 
    ( RedBlackNode top, T[] vArray, int start ) {
    while (top!=null) {
      start = emitSubTree(top.leftChild, vArray, start);
      vArray[start] = top.v;
      ++start;
      top = top.rightChild;
    }
    return start;
  }
  @Override
  public SortingBucket<T> newBucket(int size) {
    return new RedBlackBucket<T>(comparator);
  }
  @Override
  public void clear() {
    root          = null;
  }
  public double getTotalPathLength() {
    return root.getTotalPathLength();
  }
}
