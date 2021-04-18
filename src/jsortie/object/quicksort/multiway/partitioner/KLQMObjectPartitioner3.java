package jsortie.object.quicksort.multiway.partitioner;

import java.util.Comparator;

import jsortie.object.ObjectRangeSortHelper;
import jsortie.object.quicksort.multiway.MultiPivotObjectUtils;

public class KLQMObjectPartitioner3<T> 
  implements FixedCountPivotObjectPartitioner<T> {
  protected MultiPivotObjectUtils<T> utils
    = new MultiPivotObjectUtils<T>();
  @Override
  public int getPivotCount() {
    return 3;
  } 
  @Override
  public int[] multiPartitionRange
    ( Comparator<? super T> comp, T[] vArray
    , int start, int stop, int[] pivotIndices) {
    utils.movePivotsAside
      ( vArray, pivotIndices
      , new int[] { start, start + 1, stop - 1 });
    T vP = vArray[start]; // The three pivots, vP
    T vQ = vArray[start + 1]; // <= vQ
    T vR = vArray[stop - 1]; // <= vR
    int a = start + 2; // the place the next value found < vP is to go
    int b = a; // the left-hand scanning pointer
    int c = stop - 2; // the right-hand scanning pointer
    int d = c; // the place the next value found > vR is to go
    T vTemp;
    for (;;) {
      while ( comp.compare ( vArray[b] , vQ) < 0 ) { 
        // like LomutoPartitioner on left
        if ( comp.compare ( vArray[b] , vP) < 0 ) {
          vTemp = vArray[a];
          vArray[a] = vArray[b];
          vArray[b] = vTemp;
          ++a;
        }
        ++b;
      }
      while (comp.compare(vQ , vArray[c]) < 0 ) { 
        // like LomutoMirrorPartitioner on right
        if (comp.compare(vR , vArray[c]) < 0 ) {
          vTemp = vArray[c];
          vArray[c] = vArray[d];
          vArray[d] = vTemp;
          --d;
        }
        --c;
      }
      if (c <= b) {
        break; // Exit the loop, if the b and c 
               // pointers have crossed
      }
      if (comp.compare(vR , vArray[b]) < 0 ) {
        if (comp.compare(vArray[c] , vP) < 0 ) {
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
      } else if ( comp.compare
                  ( vArray[c] , vP )
                  < 0 ) {
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
    ObjectRangeSortHelper.swapElements
      ( vArray, start + 1, a );
    ObjectRangeSortHelper.swapElements
      ( vArray, a, b );
    --a;
    ObjectRangeSortHelper.swapElements
      ( vArray, start, a );
    ObjectRangeSortHelper.swapElements
      ( vArray, d, stop - 1 );
    return new int[] 
      { start, a, a + 1, b
      , b + 1, d, d + 1, stop };
  }
}
