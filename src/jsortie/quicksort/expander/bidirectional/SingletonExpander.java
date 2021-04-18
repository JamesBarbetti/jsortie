package jsortie.quicksort.expander.bidirectional;

import jsortie.helper.ShiftHelper;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.bidirectional.traditional.SingletonPartitioner;

public class SingletonExpander 
  implements PartitionExpander {
  SingletonPartitioner party 
    = new SingletonPartitioner();
  ShiftHelper shifty
    = new ShiftHelper();
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int expandPartition
    ( int[] vArray, int start, int stopLeft
    , int hole, int startRight, int stop ) {
    int vPivot     = vArray[hole];
    //Fake some sentinels
    int vLiftLeft  = vArray[stopLeft];
    int vLiftRight = vArray[startRight-1];
    vArray[stopLeft] = vPivot;
    vArray[startRight-1] = vPivot;
    int lhs = start-1;
    do {
      ++lhs;
    } while (vArray[lhs]<vPivot);
    int rhs = stop;
    do {
      --rhs;
    } while (vPivot<vArray[rhs]);
    while (lhs<stopLeft && startRight<=rhs) {
      int vTemp = vArray[lhs];
      vArray[lhs] = vArray[rhs];
      vArray[rhs] = vTemp;
      do {
        ++lhs;
      } while (vArray[lhs]<vPivot);
      do {
        --rhs;
      } while (vPivot<vArray[rhs]);
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
      lhs 
        = party.partitionRange
          ( vArray, lhs, stopLeft, lhs );
      shifty.moveFrontElementsToBack
        ( vArray, lhs, stopLeft, hole );
      hole = lhs + (hole - stopLeft);
    } else if (startRight<=rhs) {
      vArray[hole] = vArray[rhs];
      vArray[rhs]  = vPivot;
      rhs 
        = party.partitionRange
          ( vArray, startRight, rhs+1, rhs );
      shifty.moveBackElementsToFront
        ( vArray, hole+1, startRight, rhs+1 );
      hole = rhs - (startRight - hole - 1);
    }
    return hole;
  }
}
