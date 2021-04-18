package jsortie.helper;

public class BranchAvoidingEgalitarianPartitionerHelper
  extends FancierEgalitarianPartitionerHelper {
  @Override
  public int moveEqualOrLessToLeft
    ( int[] vArray, int start, int stop, int vPivot ) {
    while (start<stop && vArray[start]<=vPivot) {
      ++start;
    }
    int scan=start+1;
    if (scan<stop) {
      int vFirstTooBig = vArray[start];
      do
      {
        int v = vArray[scan];
        vArray[start] = v;
        start += (v<=vPivot) ? 1 : 0;
        vArray[scan] = vArray[start];
        ++scan;
      } while (scan<stop);
      vArray[start]=vFirstTooBig;
    }  
    return start;
  }
  @Override
  public int moveUnequalToLeft
    ( int[] vArray, int start, int stop, int vPivot ) {
    while (start<stop && vArray[start]!=vPivot) {
      ++start;
    }
    int scan=start+1;
    if (scan<stop) {
      int vFirstEqual = vArray[start];
      do
      {
        int v         = vArray[scan];
        vArray[start] = v;
        start        += (v!=vPivot) ? 1 : 0;
        vArray[scan]  = vArray[start];
        ++scan;
      } while (scan<stop);
      vArray[start]=vFirstEqual;
    }
    return start;
  }
  @Override
  public int moveEqualOrGreaterToRight
    ( int[] vArray, int start, int stop, int vPivot ) {
    --stop;
    while (start<=stop && vPivot<=vArray[stop]) {
      --stop;
    }
    if (start<stop) {
      int vFirstTooSmall = vArray[stop];
      int scan = stop-1;
      do
      {
        int v = vArray[scan];
        vArray[stop] = v;
        stop -= (vPivot<=v) ? 1 : 0;
        vArray[scan] = vArray[stop];
        --scan;
      } while (start<=scan);
      vArray[stop] = vFirstTooSmall;
    }
    return stop+1;
  }
  @Override
  public int moveUnequalToRight
    ( int[] vArray, int start, int stop, int vPivot ) {
    --stop;
    while (start<=stop && vPivot!=vArray[stop]) {
      --stop;
    }
    if (start<stop) {
      int vFirstTooSmall = vArray[stop];
      int scan = stop-1;
      do
      {
        int v = vArray[scan];
        vArray[stop] = v;
        stop        -= (vPivot!=v) ? 1 : 0;
        vArray[scan] = vArray[stop];
        --scan;
      } while (start<=scan);
      vArray[stop] = vFirstTooSmall;
    }
    return stop+1;
  }
  public int[] fudgeBoundaries
    ( int[] vArray, int[] partitions ) {
    int partitionCount = partitions.length;
    int start = partitions[0];
    int stop  = partitions[partitions.length-1];
    for (int i=0; i<partitionCount; i+=2) {
      int a = partitions[i];
      int b = partitions[i+1];
      if ( start<a && a<b && b<stop 
             && vArray[a-1] == vArray[b] ) {
        b = a;
      } else {
        if ( 0<i && a<stop && partitions[i-1]<a
             && vArray[a-1] == vArray[a] ) {
          int vMin = vArray[a];
          a = moveEqualOrLessToLeft(vArray, a, b, vMin);
        }
        if ( a<b && b<stop 
             && vArray[b-1] == vArray[b]) {
          int vMax = vArray[b];
          b = moveEqualOrGreaterToRight(vArray, a, b, vMax);
        }
      }
      partitions[i]   = a;
      partitions[i+1] = b;
    }
    return partitions;
  }
}
