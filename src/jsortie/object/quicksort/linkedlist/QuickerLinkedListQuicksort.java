package jsortie.object.quicksort.linkedlist;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;

public class QuickerLinkedListQuicksort<T>
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
  protected Node newNode(T v, int rank) {
    //this is declared for the sake of 
    //the StableLinkedListQuicksort subclass
    return new Node(v);    
  }
  public void sortSubList
    ( Comparator<? super T> comparator
    , Node before, Node after, Node dummy) {
    //entry: before is the node *before* 
    //       the place sorting is to start
    //       after  is the node *after* the place 
    //       sorting is to end (may be null)
    while (before.next!=after) {
      //Three-way split
      Node pivotNode  = before.next;
      T    vPivot     = pivotNode.value;
      Node leftTail   = before;
      Node midTail    = pivotNode;
      Node rightTail  = dummy;
      int  leftCount  = 0;
      int  rightCount = 0;
      for ( Node scan=pivotNode.next
          ; scan!=after; scan=scan.next) {
        int diff = comparator.compare
                   ( scan.value, vPivot );
        if (diff<0) {
          leftTail.next  = scan;
          leftTail       = scan;
          ++leftCount;
        } else if (0<diff) {
          rightTail.next = scan;
          rightTail      = scan;
          ++rightCount;
        } else {
          midTail.next   = scan;
          midTail        = scan;
        }
      }
      //Relink, recursively sort the smaller 
      //sublist, continue sorting the larger
      leftTail.next  = pivotNode;
      if (rightCount!=0) {
        midTail.next   = dummy.next; 
        rightTail.next = after;
        if (leftCount<rightCount) {
          sortSubList ( comparator
            , before, pivotNode, dummy);
          before = midTail;
        } else {
          sortSubList( comparator
            , midTail, after, dummy);
          after = pivotNode;
        }
      } else {
        midTail.next = after;
        after        = pivotNode;
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
        , head, null, new Node());
      emitList(head, vArray, start, stop);
    }
  }  
  public Node buildInputList
    ( T[] vArray, int start, int stop ) {
    int count = stop - start;
    Node head = newNode(null,-1);
    Node tail = head;
    double step = count;
    for (; 2.0 <= step; step *= 0.5) {
      for ( double sweep = start + step*.5
          ; sweep < stop; sweep += step) {
        int i = (int)Math.floor(sweep);
        tail = tail.next 
            = newNode (vArray[i], i);
      }
    }
    if (1.0<step) {
      int i = start-1;
      for ( double sweep = start+step
          ; sweep < stop+1.0; sweep += step) {
        int j = (int)Math.floor(sweep);
        for (++i; i<j; ++i) {
          tail = tail.next 
              = newNode (vArray[i], i);
        }
      }
    } else {
      tail = tail.next 
        = newNode (vArray[start], start);
    }
    tail.next = null;
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
  //This is a naive (but stable) version, 
  //that builds an "in-order" rather than a 
  //"breadth-first" input list.  See 
  //StableLinkedListQuicksort, which achieves
  //stability another way.
  public Node vanillaInputList(
    T[] vArray, int start, int stop) {
    Node head = new Node();
    Node tail = head;
    for (int i=start; i<stop; ++i) {
      tail = tail.next = newNode(vArray[i], i);
    }
    tail.next = null;
    return head;
  }
}
