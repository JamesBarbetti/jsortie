package jsortie.quicksort.multiway.partitioner.geometric;

import jsortie.quicksort.collector.SampleCollector;

public class GeometricPartitioner7 
  extends GeometricPartitionerBase {
  public GeometricPartitioner7() {
    super(7);
  }
  public GeometricPartitioner7(SampleCollector collector) {
    super(7, collector);
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
    int hole6 = pivotIndices[5];
    int hole7 = pivotIndices[6];
    int v1 = vArray[hole1];
    int v2 = vArray[hole2];
    int v3 = vArray[hole3];
    int v4 = vArray[hole4];
    int v5 = vArray[hole5];
    int v6 = vArray[hole6];
    int v7 = vArray[hole7];
    for (; scan<stop; ++scan) {
      if ( vArray[scan] < v7 ) {
        int v = vArray[scan];
        vArray[scan] = vArray[hole7+1];
        if ( v < v6 ) {
          vArray[hole7] = vArray[hole6+1];
          if ( v < v5 ) {
            vArray[hole6] = vArray[hole5+1];
            if ( v < v4 ) {
              vArray[hole5] = vArray[hole4+1];
              if ( v < v3 ) {
                vArray[hole4] = vArray[hole3+1];
                if ( v < v2) {
                  vArray[hole3] = vArray[hole2+1];
                  if ( v < v1) {
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
                vArray[hole4] = v;
              }
              ++hole4;
            } else {
              vArray[hole5] = v;
            }
            ++hole5;
          } else {
            vArray[hole6] = v;
          }
          ++hole6;
        } else {
          vArray[hole7] = v;
        }
        ++hole7;
      }
    }
    vArray[hole1]   = v1;
    vArray[hole2]   = v2;
    vArray[hole3]   = v3;
    vArray[hole4]   = v4;
    vArray[hole5]   = v5;
    vArray[hole6]   = v6;
    vArray[hole7]   = v7;
    return new int[] 
      { start,   hole1, hole1+1, hole2
      , hole2+1, hole3, hole3+1, hole4
      , hole4+1, hole5, hole5+1, hole6
      , hole6+1, hole7, hole7+1, stop };
  }
}