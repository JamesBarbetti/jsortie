package jsortie.object.treesort.binary.balanced;

import java.util.Comparator;

import jsortie.object.bucket.IterableBucket;

public class IterableAVLBucket<T> 
	extends AVLBucket<T>
	implements IterableBucket<T> {
  public IterableAVLBucket
    ( Comparator<? super T> comp ) {
    super(comp);
  }

  @Override
  public BucketIterator<T> getBucketIterator() {
    class AVLIterator implements BucketIterator<T> {
      protected AVLNode currentNode;
	  protected AVLNode nextNode;
		
      public AVLIterator () {
        currentNode = firstInTree(root);
        nextNode    = successorInTree(currentNode);
      }

      @Override 
      public boolean hasNext() {
        return nextNode!=null;
      }
    
      @Override public T next() {
        AVLNode prevNode = currentNode;
        currentNode = nextNode;
        nextNode = successorInTree(currentNode);
        return prevNode.v;
      }
    }
    return new AVLIterator();
  }
  
  protected AVLNode successorInTree(AVLNode node) {
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
  
  protected AVLNode firstInTree(AVLNode node) { 
    //assumes node not null
    while (node.leftChild!=null) { node = node.leftChild; }
    return node;
  }
  
  @Override
  public IterableBucket<T> newBucket(int size) {
    return new IterableAVLBucket<T>(comparator);
  }
}
