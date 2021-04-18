package jsortie.quicksort.partitioner.kthstatistic.quickselect;

import jsortie.janitors.insertion.InsertionSort;
import jsortie.quicksort.partitioner.interfaces.KthStatisticPartitioner;
import jsortie.quicksort.partitioner.kthstatistic.heap.KislitsynPartitioner;

public class GNUSTLPartitioner 
  implements KthStatisticPartitioner {
  protected int minSizeForPartition = 4;
  protected KislitsynPartitioner lastResort 
    = new KislitsynPartitioner(); 
  //
  //This is a Kth Statistic Partitioner *equivalent* to 
  //the one used in the GNU Standard Template Library 
  //(when you're doin' stuff sequentially, anyway).
  //See: https://gcc.gnu.org/onlinedocs/libstdc++/
  //     latest-doxygen/a00521_source.html
  //
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public void partitionRangeExactly
    ( int[] vArray, int start, int stop
    , int targetIndex ) {
    //nth_element
    if (stop-start<2) {
      return;
    }
    //lg
    int m = stop - start;
    int maxDepth = 2;
    for (int i=4; i<m; i+=i) {
      maxDepth+=2;
    }
    introSelect ( vArray, start, stop
                , targetIndex, maxDepth );
  }
  protected void introSelect
    ( int[] vArray, int start, int stop
    , int targetIndex, int maxDepth) {
    while ( minSizeForPartition <= stop - start ) {
      if (maxDepth==0) {
        lastResort.partitionRangeExactly
          ( vArray, start, stop, targetIndex );
        return;
      }
      --maxDepth;
      int split = partition(vArray, start, stop);
      if (split < targetIndex) {
        start = split + 1; 
      } else if ( targetIndex < split ) {
        stop = split;
      } else {
        return;
      }
    }
    InsertionSort.sortSmallRange
      ( vArray, start, stop );
  }
  protected int partition
    ( int[] vArray, int start, int stop ) {
    int d; //whichever of a, b, c references the median
           //of vArray[a], vArray[b], vArray[c]
    {
      //This bit is in braces because subclasses tend to
      //replace it (it is "braced for replacement").
      int a = start+1;
      int b = start+(stop-start)/2;
      int c = stop-1;
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
      //The last item has to be compared with 
      //the pivot (again!), because we only swapped
      //the pivot into place: we didn't otherwise order
      //vArray[a], vArray[b], vArray[c]
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
