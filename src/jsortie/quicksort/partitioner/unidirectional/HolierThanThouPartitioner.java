package jsortie.quicksort.partitioner.unidirectional;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class HolierThanThouPartitioner 
  implements SinglePivotPartitioner {
  //This is a cross between a Lomuto and a Hoyos
  //partitioner. It uses Lomuto partitioner's
  //"one-way scanning", and the Hoyos partitioner's
  //"floating hole" technique, for exchanging items
  //between partitions.
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int partitionRange
    ( int[] vArray, int start, int stop
    , int pivotIndex) {
    int vPivot            = vArray [ pivotIndex ];
    vArray [ pivotIndex ] = vArray [ start      ];
    int last              = stop -  1;
    int hole              = start + 1;
    while (hole<last && vArray[hole]<vPivot) {
      ++hole;
    }
    --hole;
    vArray[start] = vArray[hole];
    vArray[hole]  = vPivot;
    while (vPivot < vArray[last]) {
      --last;
    }
    for (int scan=hole+1;scan<=last;++scan) {
      int v = vArray[scan];
      if ( v <= vPivot ) {
        vArray[hole] = v;
        ++hole;
        vArray[scan] = vArray[hole];
      }
    }
    vArray[hole] = vPivot;
    return hole;
  }
}