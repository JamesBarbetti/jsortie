package jsortie.object.treesort.binary.balanced;

import java.util.Comparator;
import java.util.SortedSet;

public class MonkeyPuzzleSet<T>
  extends MonkeyPuzzleContainer<T> 
  implements SortedSet<T> {
  public MonkeyPuzzleSet 
    ( Comparator<? super T> comparator) {
    super(comparator);
  }
  @Override
  public boolean add(T v) {
    return super.merge(v);
  }
  @Override
  public Comparator<? super T> comparator() {
    return comparator;
  }
  @Override
  public T first() {
    if (root==nowhere) {
      return null;
    } 
    MonkeyPuzzleNode node = root;
    while ( node.leftChild != nowhere ) {
      node = node.leftChild;
    }
    return node.v;
  }
  @Override
  public T last() {
    if ( root == nowhere) {
      return null;
    } 
    MonkeyPuzzleNode node = root;
    while (node.rightChild != nowhere) {
      node = node.rightChild;
    }
    return node.v;
  }
  @Override
  public SortedSet<T> headSet ( T toElement ) {
    MonkeyPuzzleSet<T> subset 
      = new MonkeyPuzzleSet<T>(comparator);
    subset.addLeftSet(root, toElement);
    return subset;
  }
  protected void addLeftSet
    ( MonkeyPuzzleNode node, T toElement ) {
    while ( node != nowhere ) {
      int diff = comparator.compare
                 ( node.v, toElement );
      if (diff<=0) {
        addSubTree(node.leftChild);
        if (diff<0) {
          add(node.v);
        }
        node = node.rightChild;
      } else {
        node = node.leftChild;
      }
    }
  }
  private void addSubTree
    ( MonkeyPuzzleNode node ) {
    while ( node != nowhere ) {
      addSubTree(node.leftChild);
      add(node.v);
      node = node.rightChild;
    }
  }
  @Override
  public SortedSet<T> subSet
    ( T fromElement, T toElement ) {
    MonkeyPuzzleSet<T> subset 
      = new MonkeyPuzzleSet<T>(comparator);
    subset.addLeftSet(root, toElement);
    return subset.tailSet(fromElement);
  }
  @Override
  public SortedSet<T> tailSet
    ( T fromElement ) {
    MonkeyPuzzleSet<T> subset 
      = new MonkeyPuzzleSet<T>(comparator);
    subset.addRightSet(root, fromElement);
    return subset;
  }
  private void addRightSet
    ( MonkeyPuzzleNode node, T fromElement ) {
    while ( node != nowhere ) {
      int diff = comparator.compare
                 ( node.v,  fromElement );
      if (0<=diff) {
        addSubTree(node.rightChild);
        if (0<diff) {
          add(node.v);
        }
        node = node.leftChild;
      } else {
        node = node.rightChild;
      }
    }
  }
}
