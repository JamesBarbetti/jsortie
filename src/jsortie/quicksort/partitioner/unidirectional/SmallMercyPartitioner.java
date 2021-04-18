package jsortie.quicksort.partitioner.unidirectional;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class SmallMercyPartitioner 
  implements SinglePivotPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int partitionRange
    ( int[] vArray, int start, int stop, int pivotIndex ) {
    int vPivot = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    int iHole = start;
    int iStop = start+2;
    int iScan = start+1;
    while (iScan<stop) {
      for (; iScan<iStop; ++iScan) {
        int v = vArray[iScan];
        if ( v < vPivot ) {
          vArray[iHole] = v;
          ++iHole;
          vArray[iScan] = vArray[iHole];
        }
      }
      //Here, iHole-start == # in left partition
      //      iScan-iHole-1 == # in right partition
      //So stop at: iScan-iHole-iHole+start+iScan
      //            (iScan-iHole)*2 + start
      iStop = (iScan-iHole);
      iStop += iStop;
      iStop += start;
      if (stop<iStop) {
        iStop = stop;
      }
      for (; iScan<iStop; ++iScan) {
        //equal items go left
        int v = vArray[iScan];
        if ( v <= vPivot ) {
          vArray[iHole] = v;
          ++iHole;
          vArray[iScan] = vArray[iHole];
        }
      }
      //Again, iHole-start == # in left partition
      //       iScan-iHole-1 == # in right partition
      //So stop at: iHole-start-iScan+iHole+2+iScan
      iStop = iHole+iHole-start+2;
      if (stop<iStop) {
        iStop = stop;
      }
    } while (iScan<stop);
    vArray[iHole] = vPivot;
    return iHole;
  }
}
