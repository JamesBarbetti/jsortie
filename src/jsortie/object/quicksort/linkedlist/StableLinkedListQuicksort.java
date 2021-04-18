package jsortie.object.quicksort.linkedlist;

import java.util.Comparator;

public class StableLinkedListQuicksort<T>
  extends QuickerLinkedListQuicksort<T> {
  protected class StableNode extends Node {
    int rank;
    public StableNode
      ( T v, int originalRank ) {
      super(v);
      rank = originalRank;
    }
    @SuppressWarnings("unchecked")
    public StableNode getNext() {
      return (StableNode)next;
    }
  }
  protected Node newNode(T v, int rank) {
    return new StableNode(v, rank);
  }  
  protected void sortSubList
    ( Comparator<? super T> comparator
    , StableNode before , StableNode after
    , int count) {
    do {
      StableNode pivotNode  
        = before.getNext();
      T    vPivot     = pivotNode.value;
      int  pivotRank  = pivotNode.rank;
      Node leftTail   = before;
      Node rightTail  = pivotNode;
      int  leftCount  = 0;
      for ( StableNode scan=pivotNode.getNext()
          ; scan!=after
          ; scan=scan.getNext()) {
        int diff = comparator.compare
                   ( scan.value, vPivot );
        if (diff==0) {
          diff = ( scan.rank < pivotRank ) ? -1 : 1;
        }
        if (diff<0) {
          leftTail.next  = scan;
          leftTail       = scan;
          ++leftCount;
        } else {
          rightTail.next = scan;
          rightTail      = scan;
        } 
      }
      leftTail.next  = pivotNode;
      rightTail.next = after;
      int rightCount = count - leftCount - 1;
      if (leftCount<rightCount) {
        if (1<leftCount) {
          sortSubList ( comparator, before
                      , pivotNode, leftCount);
        }
        before = pivotNode;
        count = rightCount;
      } else {
        if (1<rightCount) {
          sortSubList ( comparator, pivotNode
                      , after, rightCount);
        }
        after = pivotNode;
        count = leftCount;
      }
    } while (1<count);
  }
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    if (start+1<stop) {
      @SuppressWarnings("unchecked")
      StableNode head 
        = (StableNode)buildInputList
          (vArray, start, stop );
      sortSubList(comparator, head, null, stop-start);
      emitList(head, vArray, start, stop);
    }
  }
}
