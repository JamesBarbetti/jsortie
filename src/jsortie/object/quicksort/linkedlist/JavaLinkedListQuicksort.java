package jsortie.object.quicksort.linkedlist;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import jsortie.object.ObjectRangeSorter;

public class JavaLinkedListQuicksort<T>
  implements ObjectRangeSorter<T> {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  private void sortList
    ( Comparator<? super T> comparator
    , LinkedList<T> input, LinkedList<T> output) {
    Stack<LinkedList<T>> stack 
      = new Stack<LinkedList<T>>();
    Stack<T> pivotStack = new Stack<T>();
    while (!input.isEmpty()) {
      Iterator<T> iterator = input.iterator();
      T vPivot = iterator.next();
      iterator.remove();
      LinkedList<T> right = new LinkedList<T>();
      while (iterator.hasNext()) {
        T v = iterator.next();
        if ( 0 < comparator.compare( v, vPivot) ) {
          iterator.remove();
          right.add(v);
        }
      }
      if (input.size() <= right.size()) {
        //recursively (smaller) process the left partition,
        //then iteratively process the larger right partition
        sortList(comparator, input, output);
        output.add(vPivot);
        input = right;
      } else {
        //push the pivot, and smaller right partition onto 
        //a stack (but not the call stack!) and iteratively
        //process the larger left partition; we'll come back to
        //the small right partitions later (processing them 
        //from "inside" to "outside").
        pivotStack.push(vPivot);
        stack.push(right);
      }
      while (input.isEmpty() && !stack.isEmpty()) {
        output.add( pivotStack.pop());
        input = stack.pop();
      }
    }
  }
  @Override
  public void sortRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    if (start+1<stop) {
      LinkedList<T> input  = new LinkedList<T>();
      for (int i=start; i<stop; ++i) {
        input.add(vArray[i]);
      }
      LinkedList<T> output = new LinkedList<T>();
      sortList(comparator, input, output);
      Iterator<T> iterator = output.iterator();
      for (int i=start; i<stop; ++i) {
        vArray[i] = iterator.next();
      }
    }
  }
}
