package jsortie.quicksort.indexselector.indexset;

public interface IndexSet {
  boolean contains ( int i );
  boolean merge    ( int i );
  int     emit     ( int[] dest, int w );
  int     emitInRangeAndNotInSet ( int start, int stop
                                 , int[] dest, int w);
  boolean emitsInSortedOrder();
}
