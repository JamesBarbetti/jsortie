package jsortie.helper;

public class RangeSortHelper {	
  public static void swapElements
    ( int[] vArray, int left, int right ) {
    int vTemp = vArray[left];
    vArray[left] = vArray[right];
    vArray[right] = vTemp;
  }	
  public static boolean compareAndSwapIntoOrder
    ( int[] vArray, int left, int right ) {
    boolean swapped 
      = ( vArray[right] < vArray[left] );
    if (swapped) {
      swapElements(vArray, left, right);
    }
    return swapped;
  }		
  public static void reverseRange
    ( int[] vArray, int start, int stop ) {
    --stop;
    if (start<stop) {
      do { 
        int vTemp = vArray[start];
        vArray[start]  = vArray[stop];
        vArray[stop]  = vTemp;
        ++start;
        --stop;
      } while (start<stop);
    }
  }
  public static void stableReverseRange
    ( int[] vArray, int start, int stop ) {
    if (stop-start<2) {
      return;
    }
    reverseRange(vArray, start, stop);
    for (int i = start+1; i<stop; ++i) {
      if (vArray[i-1]!=vArray[i]) {
        reverseRange(vArray, start, i);
        start = i;
      }
    }
  }
  public static void copyRange
    ( int[] src, int start, int stop
    , int[] dest, int w ) {
    int offset = w-start;
    for (int i=start; i<stop; ++i) {
      dest[ i + offset ] = src[i];
    }
  }
  public static void copyRangeRightToLeft
    ( int[] src, int start, int stop
    , int[] dest, int w) {
    int offset = w-start;
    for (int i=stop-1; start<=i; --i) {
      dest[ i + offset ] = src[i];
    }
  }
}
