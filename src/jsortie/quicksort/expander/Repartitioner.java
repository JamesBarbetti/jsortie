package jsortie.quicksort.expander;

public class Repartitioner
  implements PartitionExpander {
  PartitionExpander inner;
  public Repartitioner
    ( PartitionExpander realExpander ) {
    inner = realExpander;
  }
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int expandPartition
    ( int[] vArray, int start, int stopLeft
    , int hole, int startRight, int stop) {
    return inner.expandPartition
           ( vArray, start, hole
           , hole, hole+1, stop );
  }
}
