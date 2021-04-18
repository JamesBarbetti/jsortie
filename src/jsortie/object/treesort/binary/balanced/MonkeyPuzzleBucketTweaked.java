package jsortie.object.treesort.binary.balanced;

import java.util.Comparator;


public class MonkeyPuzzleBucketTweaked<T>
  extends MonkeyPuzzleBucket<T> {
  public MonkeyPuzzleBucketTweaked
    ( Comparator<? super T> comparator ) {
   super(comparator);
  }
  protected MonkeyPuzzleNode rebuildLocalTree
    ( MonkeyPuzzleNode below
    , MonkeyPuzzleNode former,  MonkeyPuzzleNode a
    , MonkeyPuzzleNode b, MonkeyPuzzleNode c) {
    MonkeyPuzzleNode a2 
      = (a==nowhere) 
        ? nowhere 
        : newNode
            ( a.v, replacementFor(a.leftChild)
            , replacementFor(a.rightChild));
    MonkeyPuzzleNode c2
      = (c==nowhere)
         ? nowhere
         : newNode
            ( c.v, replacementFor(c.leftChild)
            , replacementFor(c.rightChild));
    MonkeyPuzzleNode b2
      = (b==nowhere)
        ? nowhere 
        : newNode(b.v, a2, c2);
    if (below.leftChild==former) {
      below.leftChild = b2;
    } else if (below==nowhere) {
      root = b2;
    } else {
      below.rightChild = b2;
    }
    b2.parent = below;
    return b2;
  }
  protected MonkeyPuzzleNode replacementFor
    ( MonkeyPuzzleNode old ) {
    if (old==nowhere) {
      return nowhere;
    } else {
      MonkeyPuzzleNode rep 
        = newNode
          ( old.v, old.leftChild, old.rightChild);
      //null out the replaced node (to help out
      //garbage identification).
      old.v = null;
      old.leftChild = old.rightChild = old.parent = null;
      return rep;
    }
  }
}
