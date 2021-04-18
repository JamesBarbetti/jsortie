package jsortie.quicksort.protector;

import jsortie.helper.SinglePivotPartitionHelper;
import jsortie.quicksort.expander.PartitionExpander;

public class CheckingPartitionExpander
  implements PartitionExpander {
  protected SinglePivotPartitionHelper
    helper = new SinglePivotPartitionHelper();
  protected PartitionExpander          inner;
  public CheckingPartitionExpander
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
    //it is legal (!) For scanLeft to be <= than start, 
    //or for scanRight to be >=stop.
    //In those cases the expander should do nothing
    if (vArray==null) {
       throw new IllegalArgumentException
                 ( "vArray may not be null");
    }
    if (start<0) {
    throw new IndexOutOfBoundsException
        ( "start(" + start + ")" 
        + " may not be less than zero");
    }
    if (vArray.length<stop) {
      throw new IllegalArgumentException
        ( "stop(" + stop + ") may not be greater than"
        + " the array length(" + vArray.length + ")");
    }
    if (stop<start) {
      throw new IndexOutOfBoundsException
        ( "stop(" + stop + ") may not be"
        + " less than start (" + start + ")");
    }
    if (hole<stopLeft) {
      throw new IllegalArgumentException
        ( "stopLeft(" + stopLeft + ") must be"
        + " <= hole (" + hole + ")");
    }
    if (startRight<=hole) {
      throw new IllegalArgumentException
        ( "hole(" + hole + ") must be"
        + " < startRight (" + startRight + ")");
    }
    helper.checkPartition
      ( "input to "  + inner.toString()
      , vArray, stopLeft, hole, startRight);
    return inner.expandPartition
           ( vArray, start, stopLeft, hole
           , startRight, stop);
  }
}
