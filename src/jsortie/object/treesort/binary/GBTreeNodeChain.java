package jsortie.object.treesort.binary;

public class GBTreeNodeChain<T> {
  private GBTreeNode<T> first;
  private GBTreeNode<T> last;
  private int                        countInChain;
  private int                        heightOfTree;

  public GBTreeNodeChain() {
  }
  
  public GBTreeNodeChain(GBTreeNode<T> root) {
    addSubTreeOnRight(root);
    if (last!=null) last.more = null;
    if (first!=null) first.less = null;
  }

  public void addSubTreeOnRight(GBTreeNode<T> node) {
    //note: does *not* set node.more to null; leaves it intact.
    while (node!=null) {
      addSubTreeOnRight(node.less);
      if (first==null) {
        first = node;
      } else {
        last.more = node;
        node.less = last;          
      }
      ++countInChain;
      last = node;
      node = node.more;
    }
  }

  public void addSubTreeOnLeft(GBTreeNode<T> node) {
	//note: does *not* set node.less to null; leaves it intact.
    while (node!=null) {
      addSubTreeOnLeft(node.more);
      if (last==null) {
        last = node;
      } else {
        first.less = node;
        node.more  = first;
      }
      ++countInChain;
      first = node;
      node  = node.less;
    }
  }
  
  public void addTopNodeOnRight(GBTreeNode<T> node) {
	//note: does *not* set node.more to null; leaves it intact.
    if (first==null) {
      first = node;  
    } else {
      last.more = node;
      node.less = last;
    }
    ++countInChain;
    ++heightOfTree;
    last = node;
  }

  public void addTopNodeOnLeft(GBTreeNode<T> node) {
    //note: does *not* set node.less to null; leaves it intact.
    if (last==null) {
      last = node;
    } else {
      first.less = node;
      node.more  = first;
    }
    ++countInChain;
    ++heightOfTree;
    first = node;
  }  
  
  public GBTreeNode<T> extractToBalancedTree() {
    //assumed: countInChain is non-zero, first and last are both non-null
	//System.out.println("count " + this.countInChain + ", height " + this.heightOfTree);
    first.less = null;
    last.more  = null;
    /*
    String text = "data {";
    for (GeneralBalancedTreeNode<T> sweep = first; sweep!=null; sweep=sweep.more) {
      text+= " " + sweep.value.toString();			
    }
	System.out.println(text + "}");
	*/
    return extractToBalancedTree(this.countInChain);
  }

  public GBTreeNode<T> extractToBalancedTree(int countInTree) {
    GBTreeNode<T> here = null; 
    if (0<countInTree) {
      GBTreeNode<T> left = extractToBalancedTree(countInTree/2);
      here      = first;
      first     = (first!=null) ? first.more : null;
      here.less = left;
      here.more = extractToBalancedTree(countInTree - countInTree/2 - 1);
    }
    return here;
  }

  public void clear() {
    first = last = null;
    countInChain = 0;
    heightOfTree = 0;
  }

  public boolean isEmpty() {
    return countInChain==0;
  }
  
  public int getCount() {
	return countInChain;
  }

  public int getHeight() {
	return heightOfTree;
  }

}
