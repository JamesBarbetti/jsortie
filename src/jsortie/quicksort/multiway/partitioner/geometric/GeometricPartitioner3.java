package jsortie.quicksort.multiway.partitioner.geometric;

import jsortie.quicksort.collector.SampleCollector;

public class GeometricPartitioner3 
  extends GeometricPartitionerBase {	
  public GeometricPartitioner3() {
    super(3);
  }
  public GeometricPartitioner3
    ( SampleCollector collector ) {
    super(3, collector);
  }
  @Override
  public int[] partitionRangeWithGeometricPivots
    ( int[] vArray, int start, int scan, int stop
    , int[] pivotIndices) {
    int leftHole   = pivotIndices[0];
    int middleHole = pivotIndices[1];
    int rightHole  = pivotIndices[2];
    int vP = vArray[leftHole];
    int vQ = vArray[middleHole];
    int vR = vArray[rightHole];
    for (;scan<stop; ++scan) {
      if ( vArray[scan] < vR ) {
        int v = vArray[scan];
        vArray[scan]      = vArray[rightHole+1];
        if ( v < vQ ) {
          vArray[rightHole] = vArray[middleHole+1];
          if ( v < vP ) {
            vArray[middleHole] = vArray[leftHole+1];
            vArray[leftHole]   = v;
            ++leftHole;
          } else {
            vArray[middleHole] = v;
          }
          ++middleHole;
        } else {
          vArray[rightHole] = v;
        }
        ++rightHole;
      }
    }
    vArray[leftHole]   = vP;
    vArray[middleHole] = vQ;
    vArray[rightHole]  = vR;
    return new int[] 
      { start, leftHole,  leftHole+1,  middleHole
      , middleHole+1, rightHole, rightHole+1, stop };  
  }
}
