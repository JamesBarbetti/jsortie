package jsortie.object.quicksort.expander;

import java.util.Comparator;

import jsortie.object.quicksort.helper.ObjectShiftHelper;
import jsortie.object.quicksort.partitioner.SingletonObjectPartitioner;

public class SingletonObjectExpander<T> 
  implements PartitionObjectExpander<T> {
  SingletonObjectPartitioner<T> party 
    = new SingletonObjectPartitioner<T>();
  ObjectShiftHelper<T> shifty
    = new ObjectShiftHelper<T>();
  @Override
  public int expandPartition
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stopLeft
    , int hole, int startRight, int stop ) {
    T vPivot     = vArray[hole];
    //Fake some sentinels
    T vLiftLeft  = vArray[stopLeft];
    T vLiftRight = vArray[startRight-1];
    vArray[stopLeft] = vPivot;
    vArray[startRight-1] = vPivot;
    int lhs = start-1;
    do {
      ++lhs;
    } while (comparator.compare(vArray[lhs],vPivot)<0);
    int rhs = stop;
    do {
      --rhs;
    } while (comparator.compare(vPivot,vArray[rhs])<0);
    while (lhs<stopLeft && startRight<=rhs) {
      T vTemp = vArray[lhs];
      vArray[lhs] = vArray[rhs];
      vArray[rhs] = vTemp;
      do {
        ++lhs;
      } while (comparator.compare(vArray[lhs],vPivot)<0);
      do {
        --rhs;
      } while (comparator.compare(vPivot,vArray[rhs])<0);
    }
    //Remove the sentinels that we set up earlier,
    //restoring the items we overwrote with them then
    vArray[stopLeft]     = vLiftLeft;
    vArray[startRight-1] = vLiftRight;
    
    //Now, either lhs==stopLeft or rhs<startRight
    //(though *both* would be nice!)
    if (lhs<stopLeft) {
      vArray[hole] = vArray[lhs];
      vArray[lhs]  = vPivot;
      lhs = party.partitionRange
            (comparator, vArray
            , lhs, stopLeft, lhs);
      shifty.moveFrontElementsToBack
      ( vArray, lhs, stopLeft, hole);
      hole = lhs + (hole - stopLeft);
    } else if (startRight<=rhs) {
      vArray[hole] = vArray[rhs];
      vArray[rhs]  = vPivot;
      rhs = party.partitionRange
            ( comparator, vArray
            , startRight, rhs+1, rhs);
      shifty.moveBackElementsToFront
      ( vArray, hole+1, startRight, rhs+1 );
      hole = rhs - (startRight - hole - 1);
    }
    return hole;
  }
}
