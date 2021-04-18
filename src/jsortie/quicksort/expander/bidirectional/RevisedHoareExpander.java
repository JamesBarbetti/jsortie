package jsortie.quicksort.expander.bidirectional;

import jsortie.helper.ShiftHelper;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.bidirectional.traditional.RevisedHoarePartitioner;

public class RevisedHoareExpander 
  implements PartitionExpander {
  RevisedHoarePartitioner party 
    = new RevisedHoarePartitioner();
  ShiftHelper shifty
    = new ShiftHelper();
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int expandPartition
    ( int[] vArray, int start, int stopLeft
    , int hole, int startRight, int stop) {
        //Shift stuff already compared with pivot out
    int leftSampleSize  = hole - stopLeft;
    shifty.moveBackElementsToFront
      ( vArray, start, stopLeft, hole );
    int rightSampleSize = startRight - hole - 1;
    shifty.moveFrontElementsToBack
      ( vArray, hole+1, startRight, stop );
    start += leftSampleSize;
    stop  -= rightSampleSize;
    hole = party.partitionRange
           ( vArray, start, stop, hole );
    shifty.moveFrontElementsToBack
      ( vArray, start-leftSampleSize, start, hole );
    shifty.moveBackElementsToFront
      ( vArray, hole+1, stop, stop+rightSampleSize );
    return hole;
  }
}
