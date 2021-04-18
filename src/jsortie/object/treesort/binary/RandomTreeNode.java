package jsortie.object.treesort.binary;
import java.util.Comparator;

public class RandomTreeNode<T> 
  extends GBTreeNode<T>
{
  protected int tiebreak;
  public RandomTreeNode () {}
  public RandomTreeNode ( T v, int t) {
    super(v);
    tiebreak = t;
  }  
  public void accept 
    ( Comparator<? super T> comparator
    , RandomTreeNode<T> x)
  {
    //
    //Note: this uses structural rather than procedural recursion
    //      (a necessity, since the tree may not be balanced and might -
    //       in the worst case - have depth equal to its node count).
    //
    RandomTreeNode<T> target = this;
    boolean goLeft;
    RandomTreeNode<T> parent;
    do {
      parent = target;
      int diff = comparator.compare
                 ( x.value, target.value );
      if (diff==0) {
        diff = x.tiebreak - target.tiebreak;
      }
      goLeft = (diff < 0);
      target = (RandomTreeNode<T>) 
               ( goLeft ? target.less : target.more );
    } while (target!=null);
    if (goLeft) {
      parent.less = x;
    } else {
      parent.more = x;
    }
  }
}
