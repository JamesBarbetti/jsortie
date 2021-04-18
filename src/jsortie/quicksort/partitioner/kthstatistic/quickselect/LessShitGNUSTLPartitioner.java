package jsortie.quicksort.partitioner.kthstatistic.quickselect;

public class LessShitGNUSTLPartitioner 
  extends GNUSTLPartitioner {
  public LessShitGNUSTLPartitioner() {
    minSizeForPartition = 5;
  }
  @Override
  protected int partition
    ( int[] vArray, int start, int stop ) {
    int d; //whichever of a, b, c references the median
           //of vArray[a], vArray[b], vArray[c]
    {
      int a = start+1;
      int b = start+(stop-start)/2;
      int c = stop-2; //Not -1, -2. Ha!
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
