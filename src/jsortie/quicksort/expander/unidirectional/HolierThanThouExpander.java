package jsortie.quicksort.expander.unidirectional;
import jsortie.quicksort.expander.PartitionExpander;

public class HolierThanThouExpander 
  implements PartitionExpander {
  //This is the partition-expander 
  //counterpart of HolierThanThouPartitioner
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int expandPartition
    ( int[] vArray, int start, int stopLeft
    , int hole, int startRight, int stop) {
    hole = expandPartitionToLeft 
           ( vArray, start, stopLeft, hole );
    return expandPartitionToRight
           ( vArray, hole, startRight, stop );
  }
  public int expandPartitionToLeft
    ( int[] vArray, int start
    , int stopLeft, int hole ) {
    int vPivot = vArray[hole];
    for (int scan=stopLeft-1;start<=scan;--scan) {
      int v = vArray[scan];
      if ( vPivot <= v) {
        vArray[hole] = v;
        --hole;				
        vArray[scan] = vArray[hole];
      }
    }
    vArray[hole] = vPivot;
    return hole;
  }
  public int expandPartitionToRight
    ( int[] vArray, int hole
    , int startRight, int stop ) {
    int vPivot = vArray[hole];
    for (int scan=startRight;scan<stop;++scan) {
      int v = vArray[scan];
      if ( v<= vPivot ) {
        vArray[hole] = v;
        ++hole;
        vArray[scan] = vArray[hole];
      }
    }
    vArray[hole] = vPivot;
    return hole;
  }
}
