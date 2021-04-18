package jsortie.quicksort.multiway.partitioner.geometric;

import jsortie.quicksort.collector.SampleCollector;

public class GeometricPartitioner2 
  extends    GeometricPartitionerBase {	
  public GeometricPartitioner2() {
    super(2);
  }
  public GeometricPartitioner2
    ( SampleCollector collector ) {
    super(2, collector);
  }
  @Override
  public int[] partitionRangeWithGeometricPivots
    ( int[] vArray, int start, int scan
    , int stop, int[] pivotIndices) {
    int leftHole   = pivotIndices[0];
    int rightHole  = pivotIndices[1];
    int vP = vArray[leftHole];
    int vQ = vArray[rightHole];
    for (;scan<stop; ++scan) {
      if ( vArray[scan] < vQ ) {
        int v = vArray[scan];
        vArray[scan] = vArray[rightHole+1];
        if ( v < vP ) {
          vArray[rightHole] = vArray[leftHole+1];
          vArray[leftHole] = v;
          ++leftHole;
        } else {
          vArray[rightHole] = v;
        }
        ++rightHole;
      }
    }
    vArray[leftHole]  = vP;
    vArray[rightHole] = vQ;
    return new int[] 
      { start,      leftHole
      , leftHole+1, rightHole
      , rightHole+1, stop };
  }
}
