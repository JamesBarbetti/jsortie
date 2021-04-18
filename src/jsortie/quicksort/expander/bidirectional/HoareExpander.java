package jsortie.quicksort.expander.bidirectional;

import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.expander.unidirectional.LomutoExpander;

public class HoareExpander
  implements PartitionExpander {
  PartitionExpander finisher 
    = new LomutoExpander();
  //
  //If you wanted to suffer, you could set
  //finisher to:
  //  new PartitionerToExpander
  //      (new HoareExpander())
  // 
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int expandPartition
    ( int[] vArray, int start, int stopLeft
    , int hole, int startRight, int stop ) {
    //swap pivot out
    int vPivot       = vArray[hole];
    vArray[hole]     = vArray[stopLeft];
    vArray[stopLeft] = vArray[start];
    vArray[start]    = vPivot;
    ++stopLeft;
    --startRight;
    int v;
    for (;;) {
      //hole is: last element on left
      do {
        --stopLeft;
        if (stopLeft <= start) {
          vArray[start] = vArray[hole];
          vArray[hole]  = vPivot;
          return finisher.expandPartition
            ( vArray, start, start
            , hole, startRight+1, stop);
        }
        v = vArray[stopLeft];
      } while ( v < vPivot );
      vArray[stopLeft] = vArray[hole];
      vArray[hole]     = v;
      //hole is: first element on right
      do {
        ++startRight;
        if (stop <= startRight) {
          --stopLeft;
          --hole;
          vArray[start]      = vArray[stopLeft];
          vArray[stopLeft] = vArray[hole];
          vArray[hole]     = vPivot;
          return finisher.expandPartition
            ( vArray, start, stopLeft
            , hole, stop, stop);
        }
        v = vArray[startRight];
      } while ( vPivot < v );
      vArray[startRight] = vArray[hole];
      vArray[hole]       = v;
      //hole is: last element on left
    }
  }
}
