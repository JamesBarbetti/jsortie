package jsortie.quicksort.multiway.partitioner.kangaroo;

import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class SkippyCrossoverPartitioner3 
  extends SkippyCrossoverPartitioner2 {
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop, int[] pivotIndices) {
    int p = pivotIndices.length;
    if (p<3 || stop-start<10) {
      return super.multiPartitionRange(vArray,  start, stop, pivotIndices);
    }
    int middle = start + (stop-start) / 2;
    MultiPivotUtils.movePivotsAside
      ( vArray, pivotIndices, new int[] {start, middle, stop-1} );
    int a = kx.expandPartitionToRight(vArray, start, start+1, middle);
    int c = kx.expandPartitionToLeft(vArray, middle+1, stop-2, stop-1);
    int b = kx.expandPartitionToLeft(vArray, a+1, middle, middle);
    b = kx.expandPartitionToRight(vArray, b, middle+1, c);
    return null;
  }
}
