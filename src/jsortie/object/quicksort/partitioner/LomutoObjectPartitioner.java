package jsortie.object.quicksort.partitioner;

import java.util.Comparator;

public class LomutoObjectPartitioner <T>
implements SinglePivotObjectPartitioner<T> {
  @Override
  public String toString() {
    return this.getClass().getSimpleName().toString();
  }  
  @Override
  public int partitionRange
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , int pivotIndex) {
    T vPivot           = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    int lhs            = start;
    for (int scan=start+1; scan<stop; ++scan) {
      T vScan = vArray[scan];
      if (comparator.compare(vScan, vPivot) < 0) {
        ++lhs;
        vArray[scan] = vArray[lhs];
        vArray[lhs]  = vScan;
      }
    }
    vArray[start] = vArray[lhs];
    vArray[lhs  ] = vPivot;
    return lhs;
  }
}
