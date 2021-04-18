package jsortie.object.quicksort.multiway.selector;

import java.util.Comparator;

import jsortie.object.ObjectRangeSorter;
import jsortie.object.heapsort.ObjectHeapSort;
import jsortie.object.quicksort.multiway.MultiPivotObjectUtils;

public class GeometricPivotObjectSelector<T> 
  implements MultiPivotObjectSelector<T>
{
  protected MultiPivotObjectUtils<T> utils;
  protected ObjectRangeSorter<T>     innerSort;
  protected int                      pivotCount;
  public String toString() {
    return this.getClass().getSimpleName()  
           + "(" + innerSort.toString()
           + ", "+ pivotCount + ")";
  }
  public GeometricPivotObjectSelector(int pivotCount) {
    this.utils      = new MultiPivotObjectUtils<T>();
    this.innerSort  = new ObjectHeapSort<T>();
    this.pivotCount = pivotCount;
  }
  @Override public int[] selectPivotIndices
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop) {
    int count       = stop - start;
    int sampleCount = getSampleSize(count);
    int sampleStart = start + (stop-start-sampleCount)/2;
    int sampleStop  = sampleStart + sampleCount;
    utils.collectPositionalSample
      ( vArray, start, stop, sampleStart, sampleStop );
    innerSort.sortRange
      ( comparator, vArray, sampleStart, sampleStop );
    int indices[] = new int[pivotCount];
    for (int i=pivotCount-1; 0<=i; --i) {
      sampleCount = sampleCount/2;
      if (sampleCount < i) {
        sampleCount=i;
      }
      indices[i] = sampleStart + sampleCount;
    }
    return indices;
  }
  protected int getSampleSize(int count) {
    return (int)Math.sqrt(count);
  }
}
