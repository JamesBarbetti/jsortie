package jsortie.object;

import java.util.Comparator;

public class ObjectRangeSortHelper {
  public static void swapElements
    ( Object[] vArray, int left, int right ) {
    Object vTemp = vArray[left];
    vArray[left] = vArray[right];
    vArray[right] = vTemp;
  }	
  public static void reverseRange
    ( Object[] vArray, int i, int j ) {
    j--;
    while (i<j) {
      swapElements(vArray, i, j);
      ++i;
      --j;
    }
  }
  public static void copyRange 
    ( Object[] vSourceArray, int sourceStart, int sourceStop
    , Object[] vDestArray, int destStart) {
    int offset = destStart - sourceStart;
    if (offset==0 && vSourceArray==vDestArray) return;
    for ( int i=sourceStart, j=destStart
        ; i<sourceStop; ++i, ++j) {
      vDestArray[j] = vSourceArray[i];
    }
  }
  public static <T> int moveNullsOutToLeft
    ( T[] vArray, int start, int stop ) {
    int w = stop;
    for (w=stop-1; start<=w; --w) {
      if ( vArray[w] == null ) {
        break;
      }
    }
    for (int i=w-1; start<=i; --i) {
      vArray[w] = vArray[i];
      w -= ( vArray[i] == null ) ? 0 : 1;
    }
    for (int i=w; start<=i; --i) {
      vArray[i] = null;
    }
    return w+1;
  }
  public static <T> void compareAndSwapIntoOrder
    ( Comparator<? super T> comparator, T[] vArray
    , int i, int j) {
    if ( 0 < comparator.compare(vArray[i], vArray[j]) ) {
      T vTemp = vArray[i];
      vArray[i] = vArray[j];
      vArray[j] = vTemp;
    }
  }
}
