package jsortie.quicksort.multiway.external;

import jsortie.helper.RangeSortHelper;

public class ExternalEgalitarianPartitioner 
  implements ExternalMultiPivotPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int[] externalMultiPartitionRange
    ( int[] vSource, int start, int stop
    , int[] pivotIndices, int[] vDest, int destStart) {
    int vPivot     = vSource[pivotIndices[pivotIndices.length/2]];
    int destStop   = destStart + (stop-start);
    int writeLess  = destStart; //post-incremented
    int writeEqual = destStop;  //pre-decremented
    int writeMore  = start;     //post-incremented
    for (int scan=start; scan<stop; ++scan) {
      int v = vSource[scan];
      if (v==vPivot) {
        --writeEqual;
        vDest[writeEqual] = v;
      } else {
        vDest[writeLess]   = v;
        vSource[writeMore] = v;
        int less = (v<vPivot) ? 1 : 0;
        writeLess += less;
        writeMore += 1 - less;
      }
    }
    int write = writeLess;
    //items equal to vPivot are at vDest[writeEqual..destStop-1]
    //(there should always be at least one (a copy of the pivot itself))
    //and are in reverse order; move, and reverse those on the right,
    //2. move those equal items on the right, into the
    //   unused portion of vDest, between write and endEqual
    //   reversing their order on the way;
    //3. if there are any equal items still in reverse order,
    //   between write inclusive and endEqual exclusive,
    //   reverse them
    int countEqual = destStop-writeEqual;
    int stopMoves  = write+countEqual;
    if (writeEqual<stopMoves) {
      stopMoves = writeEqual;
    }
    int endEqual = destStop;
    if (write<stopMoves) {
      do {
        --endEqual;
        vDest[write] = vDest[endEqual];
        ++write;
      } while (write<stopMoves);
    }
    if (stopMoves==writeEqual && writeEqual+1<endEqual) {
      RangeSortHelper.reverseRange(vDest, write, endEqual);
      write = endEqual;
    }
    //4. last of all, copy across the items that were more than the pivot,
    //   which were left behind at the left of the source array
    int readMore=start; 
    for (write=endEqual; write<stop; ++write) {
      vDest[write] = vSource[readMore];
      ++readMore;
    }
    return new int[] { destStart, writeLess, endEqual, destStop };
  }
}
