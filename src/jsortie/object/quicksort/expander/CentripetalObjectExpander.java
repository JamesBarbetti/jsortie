package jsortie.object.quicksort.expander;

import java.util.Comparator;

public class CentripetalObjectExpander<T>
  extends HolierThanThouObjectExpander<T> {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int expandPartition
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stopLeft
    , int hole, int startRight, int stop) {
    int onLeft  = stopLeft - start;
    int onRight = stop - startRight;
    int uniform;
    if (onLeft<=0 || onRight<=0) {
      uniform = 0;
    } else {
      uniform  = (onLeft < onRight) ? onLeft : onRight;
      hole     = expandUniformly 
                 ( comparator, vArray
                 , stopLeft - uniform, stopLeft, hole
                 , startRight, startRight + uniform);
      onLeft  -= uniform;
      onRight -= uniform;
    }
    if ( 0 < onLeft ) {
      hole = expandPartitionToLeft
             ( comparator, vArray
             , start, stopLeft-uniform, hole);
    } else if ( 0 < onRight ) {
      hole = expandPartitionToRight
             ( comparator, vArray
             , hole, startRight+uniform, stop);
    }
    return hole;
  }
  public int expandUniformly
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stopLeft
    , int hole, int startRight, int stop) {
    T vPivot    = vArray[hole];
    int scanLeft  = stopLeft-1;
    int scanRight = startRight;
    for (; scanRight < stop; --scanLeft, ++scanRight) {
      T vLeft  = vArray[scanLeft];
      T vRight = vArray[scanRight];
      if ( comparator.compare ( vLeft ,  vPivot) <= 0 ) {
        if (comparator.compare ( vRight ,  vPivot) < 0 ) { 
          // vLeft stays left, and vRight goes right
          vArray[hole] = vRight;
          ++hole;
          vArray[scanRight] = vArray[hole];
        }
      } else if (comparator.compare (vRight , vPivot) < 0) { 
        // vRight <= vPivot < vLeft, so swap them
        vArray[scanLeft] = vRight;
        vArray[scanRight] = vLeft;
      } else { 
        // vRight stays right, and vLeft goes left
        vArray[hole] = vLeft;
        --hole;
        vArray[scanLeft] = vArray[hole];
      }
    }
    vArray[hole] = vPivot;
    return hole;
  }
}
