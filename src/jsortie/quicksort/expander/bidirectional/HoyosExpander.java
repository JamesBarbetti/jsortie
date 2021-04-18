package jsortie.quicksort.expander.bidirectional;

import jsortie.quicksort.expander.PartitionExpander;

public class HoyosExpander
  implements PartitionExpander {
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int expandPartition
    ( int[] vArray, int start, int stopLeft
    , int hole, int startRight, int stop) {
    //Expand from Centre
    int vPivot = vArray[hole];
    int vLeft;
    int vRight;
    --startRight;
    for (;;) {
      do {
        --stopLeft;
        if ( stopLeft < start ) {
          //Expand to right
          for ( int scan=startRight+1
              ; scan<stop; ++scan ) {
            vRight = vArray[scan];
            if ( vRight <= vPivot ) {
              vArray[hole] = vRight;
              ++hole;
              vArray[scan] = vArray[hole]; 
            }
          }
          vArray[hole] = vPivot;
          return hole;
        }
        vLeft = vArray[stopLeft];
      } while ( vLeft < vPivot );
      do {
        ++startRight;
        if ( stop <= startRight ) {
          vArray[hole] = vLeft;
          --hole;
          vArray[stopLeft] = vArray[hole];
          for ( int scan=stopLeft-1
               ; start<=scan; --scan) {
            vLeft = vArray[scan];
            if ( vPivot <= vLeft ) {
              vArray[hole] = vLeft;
              --hole;
              vArray[scan] = vArray[hole];
            }
          }
          vArray[hole] = vPivot;
          return hole;
        }
        vRight = vArray[startRight];
      } while ( vPivot < vRight );
      vArray[stopLeft]   = vRight;
      vArray[startRight] = vLeft;
    }
  }
}
