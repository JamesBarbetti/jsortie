package jsortie.object.quicksort.multiway.partitioner.centrifugal;

import java.util.Comparator;

import jsortie.object.quicksort.multiway.MultiPivotObjectUtils;
import jsortie.object.quicksort.multiway.partitioner.FixedCountPivotObjectPartitioner;

public class CentrifugalObjectPartitioner2<T>
  implements FixedCountPivotObjectPartitioner<T> {
  protected MultiPivotObjectUtils<T> utils 
    = new MultiPivotObjectUtils<T>();
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int getPivotCount() {
    return 2;
  }
  @Override
  public int[] multiPartitionRange
    ( Comparator<? super T> c, T[] vArray
    , int start, int stop, int[] pivotIndices) {
    if (stop-start<4) {
      return utils.dummyPartitions
             ( c, vArray
             , start, stop
             , getPivotCount() );
    }
    int leftHole1   = start; 
    int lhs         = start;
    int rhs         = stop-1;    
    if (!utils.tryToMovePivotsAside
         ( vArray,  pivotIndices
         , new int[] { leftHole1, rhs })) {
      return utils.fakePartitions
             ( c, vArray, start, stop
             , pivotIndices
             , getPivotCount() );
    }
    T vP = vArray[leftHole1];
    T vQ = vArray[rhs];
    T vLeft;
    T vRight;
    for (;;) {
      for (;;) {
        ++lhs;
        vLeft = vArray[lhs];
        if (rhs<=lhs) {
          vArray[leftHole1]  = vP;
          vArray[rhs]        = vQ;
          return new int[] { start, leftHole1, leftHole1+1
                           , rhs,   rhs+1,     stop };
        } else if ( c.compare(vLeft , vP ) < 0 ) {
          vArray[leftHole1] = vLeft;
          ++leftHole1;
          vArray[lhs] = vArray[leftHole1];
        } else if ( c.compare(vQ , vLeft) <= 0 ) {
          break;
        }
      }
      vArray[rhs] = vLeft;
      do {
        --rhs;
        vRight = vArray[rhs];
        if (rhs<=lhs) {
          vArray[leftHole1] = vP;
          vArray[lhs]       = vQ;
          return new int[] { start, leftHole1,  leftHole1+1
                           , lhs,   lhs+1,      stop };
        }
      } while ( c.compare( vQ , vRight ) < 0 );
      if ( c.compare ( vRight ,  vP ) < 0 ) {
        vArray[leftHole1] = vRight;  
        ++leftHole1;
        vArray[lhs] = vArray[leftHole1];
      } else {
        vArray[lhs] = vRight;
      }
    } 
  }

}
