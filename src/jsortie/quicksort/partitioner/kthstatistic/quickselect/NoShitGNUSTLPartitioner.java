package jsortie.quicksort.partitioner.kthstatistic.quickselect;

public class NoShitGNUSTLPartitioner 
  extends GNUSTLPartitioner {
  public NoShitGNUSTLPartitioner() {
    minSizeForPartition = 5;
  }
  @Override
  protected int partition
    ( int[] vArray, int start, int stop ) {
    int d; //whichever of a, b, c references the median
           //of vArray[a], vArray[b], vArray[c]
    {
      int count  = stop-start;
      int b      = start + (count >> 1);
      int offset = (count >> 2);
      int a = b - offset;
      int c = b + offset; 
      if (vArray[a]<=vArray[b]) {
        if (vArray[b]<=vArray[c]) {
          d = b;
        } else if (vArray[a]<=vArray[c]) {
          d = c;
        } else {
          d = a;
        }
      } else if (vArray[a]<=vArray[c]) {
        d = a;
      } else if (vArray[b]<=vArray[c]) {
        d = c;
      } else {
        d = b;
      }
    }
    int vPivot    = vArray[d];
    vArray[d]     = vArray[start];
    vArray[start] = vPivot;
    //unguarded_partition
    int scanRight = stop; 
    int scanLeft = start;
    int vLeft;
    int vRight;
    for (;;) {
      do {
        ++scanLeft;
        vLeft = vArray[scanLeft];
      } while (vLeft<vPivot);
      do {
        --scanRight;
        vRight = vArray[scanRight];
      } while (vPivot<vRight);
      if (scanRight<=scanLeft) {
        vArray[start]     = vArray[scanRight];
        vArray[scanRight] = vPivot;
        return scanRight;
      }
      vArray[scanLeft]  = vRight;
      vArray[scanRight] = vLeft;
    }
  }
}
