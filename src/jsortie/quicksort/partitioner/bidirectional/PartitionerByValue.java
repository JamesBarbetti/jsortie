package jsortie.quicksort.partitioner.bidirectional;

public class PartitionerByValue {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public int partitionRangeByValue(int[] vArray, int start, int stop, int v) {
  	int left = start;
    while ( left<stop && vArray[left] < v    ) {
      ++left;
    }
    int right = stop;
    while ( left<right && v < vArray[right-1] ) {
      --right;
    }
    if (left<right) {
      int vx = vArray[left];
      for (;;) {
        do { --right; } while ( left < right && v < vArray[right] );
        if (right<=left) break;
        vArray[left]=vArray[right];
        do { ++left; } while ( left < right && vArray[left] < v  );
        if (right<=left) break;
        vArray[right]=vArray[left];
      }
      vArray[right] = vx;
    }
    return right;
  }
}
