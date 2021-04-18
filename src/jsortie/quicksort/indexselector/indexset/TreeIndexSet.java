package jsortie.quicksort.indexselector.indexset;

import java.util.TreeSet;

public class TreeIndexSet 
  implements IndexSet {
  protected TreeSet<Integer> realSet 
    = new TreeSet<Integer>();
  public boolean contains( int i) {
    return realSet.contains(i); 
  } 
  public boolean merge ( int i) { 
    return realSet.add(i); 
  }
  public int emit ( int[] dest, int w ) {
    for ( Integer i : realSet) {
      dest[w] = i;
      ++w;
    }
    return w;
  }
  public int emitInRangeAndNotInSet 
    ( int start, int stop
    , int[] dest, int w) {
    int scan=start;
    for (Integer i: realSet) { 
      //for i (ordered iteration) in tree set
      int iCopy = i.intValue();
      if ( stop<=iCopy) break; //or: throw!
      for ( ; scan < iCopy; ++scan, ++w ) {
        dest[w] = scan;	
      }
      scan = iCopy + 1;
    }
    for (; scan<stop; ++scan, ++w) {
      dest[w] = scan;
    }
    return w;
  }
  public boolean emitsInSortedOrder() {
    return true;
  }
}
