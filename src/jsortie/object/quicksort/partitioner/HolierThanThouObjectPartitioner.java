package jsortie.object.quicksort.partitioner;

import java.util.Comparator;

public class HolierThanThouObjectPartitioner<T>
  implements SinglePivotObjectPartitioner<T>  {
  @Override public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override 
  public int partitionRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int pivotIndex) {
    T vPivot = vArray[pivotIndex];
    //1. Skip over stuff on left less than the vPivot
    int hole = start;
    while ( comparator.compare
            ( vArray[hole], vPivot) < 0 ) {
      ++hole;
    }
    //2. Swap hole element to the place the pivot was
    vArray[pivotIndex] = vArray[hole];
    vArray[hole]       = vPivot;
    //3. Partition up to the pivot's original index 
    //   (moving elements <= vPivot left)
    int middle=start + (stop-start)/2;
    for (int scan=hole;scan<middle;++scan) {
      T v = vArray[scan];
      if (comparator.compare(v, vPivot)<=0) {
        vArray[hole] = v;
        ++hole;
        vArray[scan] = vArray[hole];
      }
    }
    //4. 
    for (int scan=middle;scan<stop;++scan) {
      T v = vArray[scan];
      if (comparator.compare(v, vPivot)<0) {
        vArray[hole] = v;
        ++hole;
        vArray[scan] = vArray[hole];
      }
    }
    vArray[hole] = vPivot;
    return hole;
  }
}
