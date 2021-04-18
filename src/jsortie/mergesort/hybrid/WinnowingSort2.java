package jsortie.mergesort.hybrid;

import jsortie.RangeSorter;
import jsortie.helper.ShiftHelper;
import jsortie.janitors.insertion.twoway.InsertionSort2Way;

public class WinnowingSort2
  extends WinnowingSort {
  static int blockSize = 32;
  static int flipLimit = 5;  //*must* be < blockSize/4
  protected RangeSorter blockSorter
    = new InsertionSort2Way();
  protected ShiftHelper shifter 
    = new ShiftHelper();
  protected RangeSorter optimisticInnerSorter;
  
  public WinnowingSort2 
    ( RangeSorter innerSorter) {
    super(innerSorter);
  }
  @Override
  public String toString()
  {
    return this.getClass().getSimpleName() 
      + "(" + innerSorter.toString() + ")";
  }	
  @Override
  protected void winnow 
    ( int[] vArray, int start, int stop
    , WinnowingResult w ) {
    int leftLength = 0;
    w.isLeftSorted = true;
    int blockStart=start;
    for (; blockStart<stop; blockStart+=blockSize) {
      int blockStop = blockStart+blockSize;
      if (stop<blockStop) blockStop = stop;
      blockSorter.sortRange
        ( vArray, blockStart, blockStop );
      int blockLength = blockStop-blockStart;
      if (leftLength==0) {
        leftLength = blockLength;
      } else {
        int i;
        for (i=0; i<blockLength; ++i) {
          if ( vArray[start+leftLength-i-1]
             < vArray[blockStart+i]) {
            break;
          }
        }
        if ( i < flipLimit ) {
          shifter.moveBackElementsToFront
          ( vArray, start+leftLength-i
          , blockStop-blockLength+i, blockStop );
          if ( w.isLeftSorted 
               && vArray[start+leftLength]
                < vArray[start+leftLength-1]) {
            w.isLeftSorted = false;
          }
          leftLength 	+= blockLength-i-i;
        } else {
          shifter.moveBackElementsToFront
          ( vArray, start+leftLength
          , blockStop-blockLength, blockStop);
          w.isLeftSorted = false;
          leftLength 	+= blockLength;
        }
      }
    }
    w.startOfChaff = start+leftLength;
  }
}
