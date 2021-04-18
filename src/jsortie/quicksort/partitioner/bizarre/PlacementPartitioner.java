package jsortie.quicksort.partitioner.bizarre;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class PlacementPartitioner 
  implements SinglePivotPartitioner {
  @Override
  public int partitionRange
  ( int[] vArray, int start, int stop, int pivotIndex ) {
    int vPivot = vArray[pivotIndex];
    int destination = start;
    for (int scan=start; scan<stop; ++scan) {
      destination += ( vArray[scan] < vPivot ) ? 1 : 0;
    }
    vArray [ pivotIndex  ] = vArray [ destination ];
    vArray [ destination ] = vPivot;
    int rhs = stop;
    int v2;
    for (int lhs=start; lhs<destination; ++lhs) {
      int v1 = vArray[lhs];
      if ( vPivot <= v1) {
        do {
          --rhs;
          v2 = vArray[rhs];
        } while ( vPivot <= v2 );
        vArray[lhs] = v2;
        vArray[rhs] = v1;
      }
    }
    return destination;
  }
}
