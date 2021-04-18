package jsortie.heapsort.topdown;

public class DualHeap {
  protected int[]   vArray;
  protected int     start;
  protected int     stop;
  protected int     targetIndex;
  protected int     targetLess1;
  protected int     targetPlus1;
  protected MaxHeap maxHeap;
  protected MinHeap minHeap;
  public DualHeap
    ( int[] vArrayToUse, int rangeStart
    , int targetIndexToAimFor, int rangeStop ) {
    vArray      = vArrayToUse;
    start       = rangeStart;
    stop        = rangeStop;
    targetIndex = targetIndexToAimFor;
    targetLess1 = targetIndex - 1;
    targetPlus1 = targetIndex + 1;
    maxHeap = new MaxHeap
                  ( vArray, start, targetPlus1 );
    minHeap = new MinHeap
                  ( vArray, targetIndex, stop );
    while ( siftOut 
            ( targetIndex, targetIndex ) ) {
    }
  }
  private boolean siftOut 
    ( int x, int y) {
    boolean didAnything = false;
    int x2 = x + x - targetPlus1;
    int y2 = y + y - targetLess1;
    if ( start<=x2 && y2<stop ) {
      int xMax = x2 - ((start<x2 
        && vArray[x2] < vArray[x2-1]) ? 1 : 0);
      int yMin = y2 + ((y2+1<stop 
        && vArray[y2+1] < vArray[y2]) ? 1 : 0);
      if ( vArray[yMin] < vArray[xMax] ) {
        didAnything = true;
        siftOut ( xMax, yMin );
        int xMin = x2 - ((x2==xMax) ? 1 : 0);
        int yMax = y2 + ((y2==yMin) ? 1 : 0);
        if ( start<=xMin && yMax<stop ) {
          if ( vArray[yMax] < vArray[xMin] ) {
            siftOut ( xMin, yMax );
          }
        }
      }
    }
    int v1 = vArray[x];
    int v2 = vArray[y];
    vArray[x] = v2;
    vArray[y] = v1;
    maxHeap.pushDownItemAt(x);
    minHeap.pushDownItemAt(y); 
    return didAnything 
      || vArray[x]!=v2 
      || vArray[y]!=v1; 
  }
}
