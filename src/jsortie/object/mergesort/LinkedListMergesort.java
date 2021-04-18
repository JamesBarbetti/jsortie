package jsortie.object.mergesort;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;

public class LinkedListMergesort<T> 
  implements ObjectRangeSorter<T> {
  public String toString() {
    return this.getClass().getSimpleName();
  }
  protected class Node {
    public T      value;
    public Node   next;
    public Node()    {            }
    public Node(T v) { value = v; }
  }
  protected Node sortSubList
    ( Comparator<? super T> comparator
    , Node before, int count) {
    //assumes: 1 < count
    //returns: the first node 
    //in the sorted sublist (or
    //after if sublist... empty)
    int leftCount = count/2;
    Node lastOnLeft;
    if (leftCount==1) {
      lastOnLeft = before.next;
      //System.out.println("left leaf " + lastOnLeft.value );
    } else {
      lastOnLeft 
        = sortSubList
          ( comparator, before, leftCount );
    }
    Node left = before.next;
    int rightCount = count - leftCount;
    //System.out.println("rightCount==" + rightCount);
    Node lastOnRight;
    if (rightCount==1) {
      lastOnRight = lastOnLeft.next;
      //System.out.println("right leaf " + lastOnRight.value );
    } else {
      lastOnRight 
        = sortSubList
          ( comparator, lastOnLeft, rightCount);
    } 
    Node right = lastOnLeft.next;
    Node afterLastOnRight = lastOnRight.next;
    Node tail = before;
    for (;;) {
      //System.out.println
      //  ( "merging " + left.value + " and " + right.value );
      if ( comparator.compare 
           ( left.value, right.value ) <= 0 ) {
        tail.next = left;
        tail = left;
        left = left.next;
        --leftCount;
        if (leftCount==0) {
          tail.next = right;
          tail = lastOnRight;
          return tail;
        }
      } else {
        tail.next = right;
        tail  = right;
        right = right.next;
        --rightCount;
        if (rightCount==0) {
          tail.next = left;
          tail = lastOnLeft;
          tail.next = afterLastOnRight;
          return tail;
        }
      }
    } 
  }  
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    if (start+1<stop) {
      Node head = buildInputList
                  ( vArray, start, stop );
      sortSubList(comparator
        , head, stop-start);
      emitList(head, vArray, start, stop);
    }
  }  
  public Node buildInputList
    ( T[] vArray, int start, int stop ) {
    Node head = new Node(null);
    Node tail = head;
    for (int i=start;i<stop;++i) {
      tail.next = new Node(vArray[i]);
      tail = tail.next;
    }
    return head;
  }
  public void emitList
    ( Node beforeFirst, T[] vArray
        , int start, int stop) {
    Node scan = beforeFirst.next;
    for ( int i=start 
        ; i<stop
        ; ++i, scan=scan.next) {
      vArray[i] = scan.value;
    }
  }  
}
