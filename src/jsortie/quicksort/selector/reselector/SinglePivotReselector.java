package jsortie.quicksort.selector.reselector;

public interface SinglePivotReselector {
  public int selectPivotIndexGivenHint
  ( int[] vArray, int start
  , int hint, int stop );
}
