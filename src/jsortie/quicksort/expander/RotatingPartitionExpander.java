package jsortie.quicksort.expander;

import jsortie.helper.ShiftHelper;
import jsortie.quicksort.partitioner.bidirectional.PartitionerByValue;

public class RotatingPartitionExpander 
  extends    PartitionerByValue
  implements PartitionExpander {
  protected ShiftHelper shifter = new ShiftHelper();
  @Override
  public int expandPartition
    ( int[] vArray,int start, int stopLeft, int hole
    , int scanRight, int stop) {
    if (start<stopLeft) {
      hole = expandPartitionToLeft
             ( vArray, start, stopLeft, hole );
    }
    if (scanRight<stop) {
      hole = expandPartitionToRight
             ( vArray, hole, scanRight, stop );
    }
    return hole;
  }
  public int expandPartitionToLeft
    ( int[] vArray, int start, int stopLeft, int hole) {
    int split = partitionRangeByValue
                ( vArray, start, stopLeft, vArray[hole] );
    if (split<stopLeft)
    {
      shifter.moveBackElementsToFront
      ( vArray, split, stopLeft-1, hole+1 );
      split += (hole-stopLeft);
      return split;
    } else {
      return hole;
    }
  }
  public int expandPartitionToRight
   ( int vArray[], int hole, int scan, int stop ) {
    int split = partitionRangeByValue
                ( vArray, scan, stop, vArray[hole] );
    if (scan<split)
    {
      shifter.moveFrontElementsToBack
              ( vArray, hole, scan, split );
      split -= (scan-hole);
      return split;
    } else {
      return hole;
    }
  }	
}
