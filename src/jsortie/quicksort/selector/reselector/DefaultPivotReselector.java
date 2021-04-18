package jsortie.quicksort.selector.reselector;

public class DefaultPivotReselector 
  implements SinglePivotReselector {
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public int selectPivotIndexGivenHint
    ( int[] vArray, int start, int hint, int stop ) {
    return hint;
  }
}
