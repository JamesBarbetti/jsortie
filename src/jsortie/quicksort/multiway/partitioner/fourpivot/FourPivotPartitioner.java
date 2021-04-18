package jsortie.quicksort.multiway.partitioner.fourpivot;

import jsortie.helper.EgalitarianPartitionerHelper;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class FourPivotPartitioner
  implements MultiPivotPartitioner {
  EgalitarianPartitionerHelper helper 
    = new EgalitarianPartitionerHelper();
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }  
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    //note: fixed comparison order: v2, v1, v3, v4
    MultiPivotUtils.movePivotsAside
    ( vArray, pivotIndices
    , new int[]  { start, start+1, stop-2, stop-1 } );
    int b1 = start + 1;
    int b2 = start + 2;
    int b3 = b2; 
    int b4 = stop  - 3;
    int b5 = stop  - 2;
    int v1 = vArray [ start  ]; 
    int v2 = vArray [ b1     ];
    int v3 = vArray [ b5     ];
    int v4 = vArray [ stop-1 ];
    do {
      int v = vArray[b3];
      if ( v <= v2) {
        vArray[b3] = vArray[b2];
        if ( v < v1 ) {
          vArray[b2] = vArray[b1];
          vArray[b1] = v;
          ++b1;
        } else {
          vArray[b2] = v;
        }
        ++b2;
        ++b3;
      } else if (v3 < v) {
        vArray[b3] = vArray[b4];
        if ( v4 < v ) {
          vArray[b4] = vArray[b5];
          vArray[b5] = v;
          --b5;
        } else {
          vArray[b4] = v;
        }
        --b4;
      } else {
        ++b3;
      }
    } while (b3<=b4);		
    ++b4;
    ++b5;
    return  helper.fudgeBoundaries 
      ( vArray, new int[] { start, b1, b1, b2, b2, b4
                          , b4, b5, b5, stop } );
  }
}
