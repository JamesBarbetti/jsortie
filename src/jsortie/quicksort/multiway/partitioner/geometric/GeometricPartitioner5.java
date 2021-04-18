package jsortie.quicksort.multiway.partitioner.geometric;

import jsortie.quicksort.collector.SampleCollector;

public class GeometricPartitioner5
  extends GeometricPartitionerBase {
  public GeometricPartitioner5() {
    super(5);
  }
  public GeometricPartitioner5(SampleCollector collector) {
    super(5, collector);
  }
  @Override
  public int[] partitionRangeWithGeometricPivots
    ( int[] vArray, int start, int scan, int stop
    , int[] pivotIndices) {
    int hole1 = pivotIndices[0];
    int hole2 = pivotIndices[1];
    int hole3 = pivotIndices[2];  
    int hole4 = pivotIndices[3]; 
    int hole5 = pivotIndices[4];
      
    int v1 = vArray[hole1];
    int v2 = vArray[hole2];
    int v3 = vArray[hole3]; 
    int v4 = vArray[hole4]; 
    int v5 = vArray[hole5]; //fifth pivot
    
    for (;scan<stop; ++scan) {
      if ( vArray[scan] < v5 ) { //compare with fifth pivot
        int v = vArray[scan];
        vArray[scan]      = vArray[hole5+1]; //shift fifth partition
        if ( v < v4 ) {
          vArray[hole5] = vArray[hole4+1];
          if ( v < v3 ) {
            vArray[hole4] = vArray[hole3+1];
            if ( v < v2 ) {
              vArray[hole3] = vArray[hole2+1];
              if ( v < v1 ) {
                vArray[hole2] = vArray[hole1+1];
                vArray[hole1] = v;
                ++hole1;
              } else {
                vArray[hole2]=v;
              }
              ++hole2;
            } else {
              vArray[hole3] = v;
            }
            ++hole3;
          } else {
            vArray[hole4] = v;
          }
          ++hole4;
        } else {
          vArray[hole5] = v; //place in fifth hole
        }
        ++hole5; //increment fourth hole pointer
      }
    }
    vArray[hole1] = v1;
    vArray[hole2] = v2;
    vArray[hole3] = v3;
    vArray[hole4] = v4; 
    vArray[hole5] = v5; //fifth hole

    return new int[] 
      { start,   hole1,  hole1+1,  hole2
      , hole2+1, hole3,  hole3+1,  hole4
      , hole4+1, hole5,  hole5+1,  stop };  //sixth partition
  }
}