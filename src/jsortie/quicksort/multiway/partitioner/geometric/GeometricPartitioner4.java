package jsortie.quicksort.multiway.partitioner.geometric;

import jsortie.quicksort.collector.SampleCollector;

public class GeometricPartitioner4 
  extends GeometricPartitionerBase {
  public GeometricPartitioner4() {
    super(4);
  }
  public GeometricPartitioner4(SampleCollector collector) {
    super(4, collector);
  }
  @Override
  public int[] partitionRangeWithGeometricPivots
    ( int[] vArray, int start, int scan, int stop
    , int[] pivotIndices) {
    int hole1 = pivotIndices[0];
    int hole2 = pivotIndices[1];
    int hole3 = pivotIndices[2];  
    int hole4 = pivotIndices[3]; //fourth hole
      
    int v1 = vArray[hole1];
    int v2 = vArray[hole2];
    int v3 = vArray[hole3]; 
    int v4 = vArray[hole4]; //fourth pivot
    
    for (;scan<stop; ++scan) {
      if ( vArray[scan] < v4 ) { //compare with fourth pivot
        int v = vArray[scan];
        vArray[scan] = vArray[hole4+1]; //shift fourth partition
        if ( v < v3 ) {
          vArray[hole4] = vArray[hole3+1];
          if ( v < v2 ) {
            vArray[hole3] = vArray[hole2+1];
            if ( v < v1 ) {
              vArray[hole2] = vArray[hole1+1];
              vArray[hole1] = v;
              ++hole1;
            } else {
              vArray[hole2] = v;
            }
            ++hole2;
          } else {
            vArray[hole3] = v;
          }
          ++hole3;
        } else {
          vArray[hole4] = v; //place in fourth hole
        }
        ++hole4; //increment fourth hole pointer
      }
    }
    vArray[hole1] = v1;
    vArray[hole2] = v2;
    vArray[hole3] = v3;
    vArray[hole4] = v4; //fourth hole
    return new int[] 
      { start,   hole1,  hole1+1,  hole2
      , hole2+1, hole3,  hole3+1,  hole4
      , hole4+1, stop };  //fifth partition
  }
}
