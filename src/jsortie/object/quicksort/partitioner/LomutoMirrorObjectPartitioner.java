package jsortie.object.quicksort.partitioner;

import java.util.Comparator;

public class LomutoMirrorObjectPartitioner<T>
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
    vArray[pivotIndex] = vArray[stop-1];
    int rhs            = stop-1;
    for (int scan=rhs-1; start<=scan; --scan) {
      T vScan = vArray[scan];
      if (comparator.compare(vPivot,vScan) < 0) {
        --rhs;
        vArray[scan] = vArray[rhs];
        vArray[rhs]  = vScan;
      }
    }
    vArray[stop-1] = vArray[rhs];
    vArray[rhs  ]  = vPivot;
    return rhs;
  }
}
