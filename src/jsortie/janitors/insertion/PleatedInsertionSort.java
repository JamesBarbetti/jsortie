package jsortie.janitors.insertion;

public class PleatedInsertionSort
  extends OrigamiInsertionSort {
  protected int pleatSize;
  public PleatedInsertionSort(int maximumDistance) {
    pleatSize = maximumDistance;
  }
  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    if (stop-start<=pleatSize) {
      super.sortRange ( vArray,  start, stop );
    } else {
      for (int blockStart=start; blockStart<stop; blockStart+=pleatSize) {
        int blockStop = blockStart + pleatSize;
        if (stop<blockStop) {
          blockStop = stop;
        }
        setUpSentinelsUnstable(vArray, blockStart, blockStop);
        --blockStop;
        for (int sweep = blockStart + 1; sweep<blockStop; ++sweep) {
          int v = vArray[sweep];
          int scan;
          for ( scan=sweep-1 ; start<=scan && v<vArray[scan]; --scan) {
            vArray[scan+1] = vArray[scan];
          }
          vArray[scan+1] = v;
        }
      }
    }
  }
}
