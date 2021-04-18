package jsortie.object.treesort.binary.balanced;

import java.util.Comparator;

import jsortie.exception.SortingFailureException;
import jsortie.object.bucket.SortingBucket;
import jsortie.object.treesort.TPLTally;

public class AVLBucket<T> 
  implements SortingBucket<T> {
  protected class AVLNode {
    T       v;          
    AVLNode leftChild;
    AVLNode rightChild;
    AVLNode parent; //Not strictly necessary 
                    //(but: makes for simpler code)
    int     balance;
    //Note: In both of the following constructors, In C++, 
    //you would also want to initialize all the AVLNode members to null,
    //and initialize height to 0.
    public AVLNode() {}
    public AVLNode(T value) {
      v=value;
    }
    public double getTotalPathLength() { 
      return getTPLTally(this).totalPathLength;
    }
    public TPLTally getTPLTally(AVLNode node) {
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
  protected AVLNode  root = null;
  public AVLBucket(Comparator<? super T> comp) {
    comparator = comp;
  }
  @Override
  public void append(T addMe) {
    if (root==null) {
      root = new AVLNode(addMe);
    } else {
      AVLNode below=root;
      AVLNode node=new AVLNode(addMe);
      addNodeBelow(node,below);
    }
  }
  protected boolean addNodeBelow
    ( AVLNode newNode, AVLNode belowNode ) {
    int diff = comparator.compare
               ( newNode.v, belowNode.v );
    boolean heightChanged; 
    if (diff<0) {
      if (belowNode.leftChild==null) {
        newNode.parent      = belowNode;
        belowNode.leftChild = newNode;
        heightChanged       = (belowNode.rightChild == null);
        belowNode.balance   = (heightChanged ? -1 : 0);
      } else { 
        if ( heightChanged 
              = addNodeBelow
                ( newNode, belowNode.leftChild ) ) {
          --belowNode.balance;
          if (belowNode.balance == -2) {
            if (0 < belowNode.leftChild.balance) {
              rotateLeft(belowNode.leftChild);
            }
            rotateRight(belowNode);
            heightChanged = false;
          } else if (belowNode.balance == 0) {
            heightChanged = false;
          }
        }
      }
    } else {
      if (belowNode.rightChild==null) {
        newNode.parent       = belowNode;
        belowNode.rightChild = newNode;
        heightChanged        = (belowNode.leftChild == null);
        belowNode.balance    = (heightChanged ? 1 : 0);
      } else if ( heightChanged 
                  = addNodeBelow
                    ( newNode, belowNode.rightChild ) ) {
        ++belowNode.balance;
        if (belowNode.balance == 2) {
          if (belowNode.rightChild.balance < 0 ) {
            rotateRight(belowNode.rightChild);
          }
          rotateLeft(belowNode);
          heightChanged = false;
        } else if (belowNode.balance == 0) {
          heightChanged = false;
        }
      }
    }
    return heightChanged;
  }
  protected void rotateLeft(AVLNode demoted) {
    AVLNode parent   = demoted.parent;
    AVLNode promoted = demoted.rightChild;
    //From:                   To:
    //
    //  parent                parent
    //    |                      |
    //  demoted               promoted
    //   /   \                  /   \
    //  x     promoted     demoted   z
    //          /  \        /  \
    //         y    z      x    y
    //
    
    demoted.rightChild = promoted.leftChild;
    if (demoted.rightChild!=null) {
      demoted.rightChild.parent = demoted;
    }
    promoted.leftChild = demoted;
    //See https://cs.stackexchange.com/questions/48861/
    //    balance-factor-changes-after-local-rotations-in-avl-tree
    demoted.balance    = demoted.balance - 1 
                       - ((0<promoted.balance) ? promoted.balance : 0); 
    promoted.balance   = promoted.balance - 1 
                       + ((demoted.balance<0) ? demoted.balance : 0);
    replaceChild(parent, demoted, promoted );
  }
  protected void rotateRight(AVLNode demoted) {
    AVLNode parent      = demoted.parent;
    AVLNode promoted    = demoted.leftChild;
    demoted.leftChild   = promoted.rightChild;
    if (demoted.leftChild!=null) {
      demoted.leftChild.parent = demoted;
    }
    promoted.rightChild = demoted;
    demoted.balance     = demoted.balance + 1 
                        - ((promoted.balance < 0 ) ? promoted.balance :  0);
    promoted.balance    = promoted.balance + 1 
                        + ((0<demoted.balance) ? demoted.balance : 0);
    replaceChild(parent, demoted, promoted);    
  }
  protected void replaceChild
    ( AVLNode parent
    , AVLNode demoted
    , AVLNode promoted ) {
    if (parent==null) {
      root = promoted;
    } else if (parent.leftChild == demoted) {
      parent.leftChild  = promoted;
    } else if (parent.rightChild == demoted) {
      parent.rightChild = promoted;
    } else {
      throw new SortingFailureException(
       "could not replace" + demoted.v.toString()
        + " under " + parent.v.toString() 
        + " with " + promoted.v.toString());
    }
    promoted.parent = parent;
    demoted.parent  = promoted;
  }
  @Override
  public int emit(T[] vArray, int start) {
    return emitSubTree(root, vArray, start);
  }
  protected int emitSubTree
    ( AVLNode top, T[] vArray, int start ) {
    while (top!=null) {
      start = emitSubTree
              ( top.leftChild, vArray, start );
      vArray[start] = top.v;
      ++start;
      top = top.rightChild;
    }
    return start;
  }
  @Override
  public SortingBucket<T> newBucket(int size) {
    return new AVLBucket<T>(comparator);
  }
  @Override
  public void clear() {
    root          = null;
  }
}
