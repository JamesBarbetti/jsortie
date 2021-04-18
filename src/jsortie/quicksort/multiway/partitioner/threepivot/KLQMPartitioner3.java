package jsortie.quicksort.multiway.partitioner.threepivot;

import jsortie.helper.RangeSortHelper;
import jsortie.quicksort.multiway.partitioner.FixedCountPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class KLQMPartitioner3 
   implements FixedCountPivotPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int getPivotCount() {
    return 3;
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    MultiPivotUtils.movePivotsAside
      ( vArray, pivotIndices
      , new int[] { start, start + 1, stop - 1 });
    int vP = vArray[start]; // The three pivots, vP
    int vQ = vArray[start + 1]; // <= vQ
    int vR = vArray[stop - 1]; // <= vR
    int a = start + 2; //the place the next value < vP will go
    int b = a; // the left-hand scanning pointer
    int c = stop - 2; // the right-hand scanning pointer
    int d = c; // the place the next value found > vR is to go
    int vTemp;
    for (;;) {
      while (vArray[b] < vQ) { 
        //like LomutoPartitioner on left
        if (vArray[b] < vP) {
          vTemp = vArray[a];
          vArray[a] = vArray[b];
          vArray[b] = vTemp;
          ++a;
        }
        ++b;
      }
      while (vQ < vArray[c]) { 
        //like LomutoMirrorPartitioner on right
        if (vR < vArray[c]) {
          vTemp = vArray[c];
          vArray[c] = vArray[d];
          vArray[d] = vTemp;
          --d;
        }
        --c;
      }
      if (c <= b) {
        break; // Exit the loop, if the b and c 
               //pointers have crossed
      }
      if (vR < vArray[b]) {
        if (vArray[c] < vP) {
          vTemp = vArray[a];
          vArray[a] = vArray[b];
          vArray[b] = vTemp;
          vTemp = vArray[a];
          vArray[a] = vArray[c];
          vArray[c] = vTemp;
          ++a;
        } else {
          vTemp = vArray[b];
          vArray[b] = vArray[c];
          vArray[c] = vTemp;
        }
        vTemp = vArray[c];
        vArray[c] = vArray[d];
        vArray[d] = vTemp;
        --d;
      } else if (vArray[c] < vP) {
        vTemp = vArray[a];
        vArray[a] = vArray[b];
        vArray[b] = vTemp;
        vTemp = vArray[a];
        vArray[a] = vArray[c];
        vArray[c] = vTemp;
        ++a;
      } else {
        vTemp = vArray[b];
        vArray[b] = vArray[c];
        vArray[c] = vTemp;
      }
      ++b;
      --c;
    }
    --a;
    --b;
    ++c;
    ++d;
    RangeSortHelper.swapElements(vArray, start + 1, a);
    RangeSortHelper.swapElements(vArray, a, b);
    --a;
    RangeSortHelper.swapElements(vArray, start, a);
    RangeSortHelper.swapElements(vArray, d, stop - 1);
    return new int[] 
      { start, a, a + 1, b, b + 1, d, d + 1, stop };
  }
}
