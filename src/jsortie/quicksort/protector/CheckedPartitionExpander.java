package jsortie.quicksort.protector;

import jsortie.exception.SortingFailureException;
import jsortie.helper.DumpRangeHelper;
import jsortie.helper.SinglePivotPartitionHelper;
import jsortie.quicksort.expander.PartitionExpander;

public class CheckedPartitionExpander 
  implements PartitionExpander {
  protected SinglePivotPartitionHelper 
    helper = new SinglePivotPartitionHelper();
  protected PartitionExpander          inner;
  public CheckedPartitionExpander
    ( PartitionExpander innerExpander ) {
    this.inner = innerExpander;
  }
  public String toString() {
    return this.getClass().getSimpleName() 
            + "(" + inner.toString() + ")";
  }
  @Override
  public int expandPartition
    ( int[] vArray, int start, int stopLeft
    , int hole, int startRight, int stop) {
    int   vPivot = vArray[hole];
    int[] copy   = helper.copyRange
                   ( vArray, start, stop );
    int[] copy2   = helper.copyRange
                   ( vArray, start, stop );
    int   split  = inner.expandPartition
                   ( vArray, start, stopLeft, hole, startRight, stop );
    try {
      helper.checkPartition(toString(), vArray, start, split, stop);
      helper.checkRangeIsPermutationOf(toString(), vArray, start, stop, copy);
    }
    catch (SortingFailureException x) {
      x.appendMessage("Asked to expand subrange" 
       + " [" + stopLeft + ".." + (startRight-1) + "]"
       + " partitioned around [" + hole + "]=" + vPivot);
      if ( startRight - stopLeft < 20 ) {
        x.appendMessage("- hole was originally at " + hole);
        x.appendMessage("- Inner range was" 
          + DumpRangeHelper.rangeToString
            ( "", copy2, stopLeft-start, startRight-start )); 
      }
      if ( stop - start < 50 ) {
        x.appendMessage("- Left range was "
          + DumpRangeHelper.rangeToString
            ("", copy2, 0, stopLeft-start));
        x.appendMessage("- Right range was "
          + DumpRangeHelper.rangeToString
            ("", copy2, startRight-start, stop-start));
        x.appendMessage(" - Output was "
          + DumpRangeHelper.rangeToString
            ("", vArray, start, stop));
      }
      throw x;
    }
    return split;
  }
  public int expandPartitionToLeft
    ( int[] vArray, int start, int stopLeft, int hole) {
    int[] copy   = helper.copyRange(vArray, start, hole+1);
    int   split  = inner.expandPartition(vArray, start, stopLeft, hole, hole+1, hole+1);
    helper.checkPartition(toString(), vArray, start, split, hole+1);
    helper.checkRangeIsPermutationOf(toString(), vArray, start, hole+1, copy);
    return split;
  }
  public int expandPartitionToRight
    ( int[] vArray, int hole, int startRight, int stop ) {
    int[] copy   = helper.copyRange(vArray, hole, stop);
    int   split  = inner.expandPartition(vArray, hole, hole, hole, startRight, stop);
    helper.checkPartition(toString(), vArray, hole, split, stop);
    helper.checkRangeIsPermutationOf(toString(), vArray, hole, stop, copy);
    return split;
  }
}
