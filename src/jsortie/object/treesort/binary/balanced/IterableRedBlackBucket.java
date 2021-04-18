package jsortie.object.treesort.binary.balanced;

import java.util.Comparator;

import jsortie.object.bucket.IterableBucket;

public class IterableRedBlackBucket<T> 
  extends RedBlackBucket<T> 
  implements IterableBucket<T> {
  public IterableRedBlackBucket
    ( Comparator<? super T> comp ) {
    super(comp);
  }
  @Override
  public BucketIterator<T> getBucketIterator() {
    class RedBlackIterator 
      implements BucketIterator<T> {
      protected RedBlackNode currentNode;
      protected RedBlackNode nextNode;
      public RedBlackIterator () {
        currentNode = firstInTree(root);
        nextNode    = successorInTree(currentNode);
      }
      @Override 
      public boolean hasNext() {
        return nextNode!=null;
      }
      @Override public T next() {
        RedBlackNode prevNode = currentNode;
        currentNode = nextNode;
        nextNode = successorInTree(currentNode);
        return prevNode.v;
      }
    }
    return new RedBlackIterator();
  }
  protected RedBlackNode successorInTree
    ( RedBlackNode node ) {
    if (node.rightChild!=null) {
      return firstInTree(node.rightChild);
    }
    while (node.parent!=null) {
      if (node.parent.leftChild == node) {
        return node.parent;
      }
      node = node.parent;
      if (node.rightChild!=null) {
        return firstInTree(node.rightChild);
      }
    }
    return null;
  }
  protected RedBlackNode firstInTree
    ( RedBlackNode node ) { 
    //assumes node not null
    while (node.leftChild!=null) { 
      node = node.leftChild; }
    return node;
  }
  @Override
  public IterableBucket<T> newBucket(int size) {
    return new IterableRedBlackBucket<T>(comparator);
  }
}