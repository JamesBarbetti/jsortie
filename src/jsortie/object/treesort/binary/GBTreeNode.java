package jsortie.object.treesort.binary;

import jsortie.object.treesort.TPLTally;

public class GBTreeNode <T> {
  protected T             value;
  protected GBTreeNode<T> less;
  protected GBTreeNode<T> more;
		
  public GBTreeNode () {}
		
  public GBTreeNode ( T v ) {
    value = v;
  }
  
  public int emit(T [] vArray, int pos) { //Note: this is *destructive*
    GBTreeNode<T> scan = this;
    while ( scan!=null ) {
      while ( scan.less != null && scan.less.more == null ) {
        //This trick makes the emit destructive, but reduces the stack usage somewhat
        //
        //      demoted                   promoted
        //     /      \                     /   \
        //  promoted   y       -->         x   demoted
        //  /      \                            /    \
        // x       null                       null    y
        //
        //The point is that descent to the right is iterative (no procedural recursion
        //is needed to descend to the right, so less stack is required).
        //
        GBTreeNode<T> demoted = scan;
        scan          = scan.less;
        scan.more     = demoted;
        demoted.less  = null;
      }
      if ( scan.less!=null ) {
        pos=scan.less.emit( vArray, pos );
      }
      vArray[pos] = scan.value;
      scan        = scan.more;
      ++pos;
    }
    return pos;
  }
  
  public GBTreeNode<T> rebalance() {
    return (new GBTreeNodeChain<T>(this)).extractToBalancedTree();
  }
  
  public TPLTally getTPLTally(GBTreeNode<T> node) {
    if (node==null) return TPLTally.nilTPLTally;
    TPLTally left  = getTPLTally(node.less);
    TPLTally right = getTPLTally(node.more);
    TPLTally total = new TPLTally();
    total.nodeCount       = left.nodeCount + right.nodeCount + 1;
    total.totalPathLength = left.totalPathLength + right.totalPathLength
      	+ total.nodeCount - 1;
    return total;
  }

  public double getTotalPathLength() {
    return getTPLTally(this).totalPathLength;
  }
}
