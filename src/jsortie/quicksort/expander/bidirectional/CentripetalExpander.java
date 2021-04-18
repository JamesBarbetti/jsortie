package jsortie.quicksort.expander.bidirectional;

import jsortie.quicksort.expander.unidirectional.HolierThanThouExpander;

public class CentripetalExpander 
  extends HolierThanThouExpander {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int expandPartition
    ( int[] vArray, int start, int stopLeft
    , int hole, int startRight, int stop) {
    int onLeft  = stopLeft - start;
    int onRight = stop - startRight;
    int uniform;
    if (onLeft<=0 || onRight<=0) {
      uniform = 0;
    } else {
      uniform  = (onLeft < onRight) 
               ? onLeft : onRight;
      hole     = expandUniformly 
                 ( vArray, stopLeft - uniform
                 , stopLeft, hole, startRight
                 , startRight + uniform);
      onLeft  -= uniform;
      onRight -= uniform;
    }
    if ( 0 < onLeft ) {
      hole = expandPartitionToLeft
             ( vArray, start
             , stopLeft-uniform, hole );
    } else if ( 0 < onRight ) {
      hole = expandPartitionToRight
             ( vArray, hole
             , startRight+uniform, stop );
    }
    return hole;
  }
  public int expandUniformly
    ( int[] vArray, int start
    , int stopLeft, int hole
    , int startRight, int stop) {
    int vPivot    = vArray[hole];
    int scanLeft  = stopLeft-1;
    int scanRight = startRight;
    for (; scanRight < stop
         ; --scanLeft, ++scanRight) {
      int vLeft  = vArray[scanLeft];
      int vRight = vArray[scanRight];
      if (vLeft <= vPivot) {
        if (vRight < vPivot) { 
          //vLeft can stay left, 
          //and vRight must go left
          vArray[hole] = vRight;
          ++hole;
          vArray[scanRight] = vArray[hole];
        }
      } else if (vRight < vPivot) { 
        //vLeft must go right, 
        //and vRight must go left, 
        //so swap them
        vArray[scanLeft] = vRight;
        vArray[scanRight] = vLeft;
      } else { 
        //vRight can stay right, 
        //and vLeft must go right
        vArray[hole] = vLeft;
        --hole;
        vArray[scanLeft] = vArray[hole];
      }
    }
    vArray[hole] = vPivot;
    return hole;
  }
}
