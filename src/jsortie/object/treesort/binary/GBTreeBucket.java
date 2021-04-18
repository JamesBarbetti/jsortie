package jsortie.object.treesort.binary;

import java.util.Comparator;

import jsortie.object.NullComparator;
import jsortie.object.bucket.SortingBucket;

public class GBTreeBucket<T> implements SortingBucket<T> {
  static double log2 = Math.log(2);
  double maxFactor = 2.0 / log2; //short for maxImbalanceFactorDivLog2 
  double maxImbalanceOffset = 2.0;
  double maxDepth = 2;   //maximum depth
  int    count = 0;
  GBTreeNode<T>         root = null;
  GBTreeNodeChain<T>    chain = new GBTreeNodeChain<T>();
  Comparator<? super T> comparator = null;
    
  public GBTreeBucket(Comparator<? super T> comp) {
	comparator = comp;
  }

  public GBTreeBucket() {
	comparator = new NullComparator<T>();
  }

@Override
  public void append(T v) {
    ++count;
    if ((count&(count-1))==0) {
      if (count==1) {
        root = new GBTreeNode<T>(v);
        maxDepth = maxImbalanceOffset;
        return;
      } 
      maxDepth += maxImbalanceOffset;
    }
    chain.clear();
    addBelow(v, root, (int)maxDepth);
    if (!chain.isEmpty()) {
      root = chain.extractToBalancedTree(count);
    }
  }

  private void addBelow(T v, GBTreeNode<T> parent, int d) {
    if (comparator.compare(v,parent.value)<0) {
      if ( parent.less == null ) {
        parent.less = new GBTreeNode<T>(v);
        if (d<=0) {
          chain.addTopNodeOnLeft(parent.less);
        }
      } else {
        addBelow(v, parent.less, d-1);
      }
      if (!chain.isEmpty()) {
        if ( Math.log(chain.getCount()) 
             * maxFactor + maxImbalanceOffset
             <= chain.getHeight() ) {
          parent.less 
            = chain.extractToBalancedTree();
          chain.clear();
        } else {
          chain.addTopNodeOnRight(parent);
          chain.addSubTreeOnRight(parent.more);
        }
      }
    } else {
      if ( parent.more == null ) {
        parent.more = new GBTreeNode<T>(v);
        if (d<=0) chain.addTopNodeOnRight(parent.more);
      } else {
        addBelow(v, parent.more, d-1);        
      }
      if (!chain.isEmpty()) {
        if ( Math.log(chain.getCount()) 
             * maxFactor + maxImbalanceOffset
             <= chain.getHeight() ) {
          parent.more 
            = chain.extractToBalancedTree();
          chain.clear();
    	} else {
          chain.addTopNodeOnLeft(parent);
          chain.addSubTreeOnLeft(parent.less);
    	}
      }
    }
  }

  @Override
  public int emit(T[] vArray, int start) {
    return root.emit(vArray, start);
  }

  @Override
  public SortingBucket<T> newBucket(int size) {
    return new GBTreeBucket<T>();
  }

  public int size() {
    return count;
  }

  @Override
  public void clear() {
    root  = null;
    count = 0;
  }
}
