package jsortie.quicksort.protector;

import jsortie.quicksort.indexselector.IndexSelector;

public class CheckingIndexSelector 
  implements IndexSelector {
  IndexSelector inner;
  public CheckingIndexSelector
    (IndexSelector innerSelector) {
    inner = innerSelector;
  }
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + inner.toString() + ")";
  }
  @Override
  public int[] selectIndices
    ( int start, int stop, int count ) {
    if (start<0) {
      throw new IllegalArgumentException
        ( toString() + ".selectIndices" 
        + " cannot be passed a start ("
        + start + ") less than zero.");
    } else if (stop<=start) {
      throw new IllegalArgumentException
        ( toString() + ".selectIndices" 
        + " cannot be passed a stop ("
        + stop + ") less than or equal" 
        + " to start (" + start + ").");
    } else if (stop-start<count) {
      throw new IllegalArgumentException
        ( toString() + ".selectIndices" 
        + " cannot be passed a count ("
        + count + ") that is more than "
        + " the difference (" + (stop-start)
        + ") between start and stop.");
    }
    return inner.selectIndices
           ( start,  stop, count );
  }
}
