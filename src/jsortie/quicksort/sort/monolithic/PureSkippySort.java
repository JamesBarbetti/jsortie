package jsortie.quicksort.sort.monolithic;

import jsortie.RangeSorter;
import jsortie.quicksort.partitioner.branchavoiding.SkippyPartitioner;

public class PureSkippySort 
  extends SkippyPartitioner 
  implements RangeSorter {
  @Override
  public void sortRange
    ( int[] vArray, int start, int stop ) {
    int count = stop - start;
    do
    {
      int pivotIndex     = start + count / 2;
      int vPivot         = vArray[pivotIndex];
      vArray[pivotIndex] = vArray[start];
      int hole           = start;
      int scan           = start+1;
    
      for (; scan<stop; ++scan) {
        int        v = vArray[scan];
        vArray[hole] = v;
        hole        += (v <= vPivot) ? 1 : 0;
        vArray[scan] = vArray[hole];
      }
      vArray[hole] = vPivot;
      
      if ( hole-start <= stop-hole ) {
        if (start+1<hole) {
          sortRange(vArray, start, hole);
        }
        start = hole + 1;
      } else {
        if (hole+2 < stop) {
         sortRange(vArray, hole+1, stop);
        }
        stop = hole;
      }
      count = stop - start;
    } while (1 < count);
  }
}