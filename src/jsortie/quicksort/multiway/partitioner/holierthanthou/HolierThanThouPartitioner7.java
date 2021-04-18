package jsortie.quicksort.multiway.partitioner.holierthanthou;

import jsortie.quicksort.multiway.partitioner.FixedCountPivotPartitioner;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class HolierThanThouPartitioner7 implements FixedCountPivotPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop, int[] pivotIndices ) {
	int desiredPivotCount = getPivotCount();
	if (pivotIndices.length < desiredPivotCount ) {
      return MultiPivotUtils.fakePartitions
             ( vArray, start, stop, pivotIndices
             , desiredPivotCount );
	}
    if (!MultiPivotUtils.tryToMovePivotsAside
         ( vArray, pivotIndices
         , new int[] { start, start+1, start+2, start+3
                     , start+4, start+5, start+6 } )) {
      return MultiPivotUtils.dummyPartitions
             ( vArray, start, stop, 7 );
    }		
    //Partition Boundaries
    int b1 = start;
    int b2 = start+1;
    int b3 = start+2;
    int b4 = start+3;
    int b5 = start+4;
    int b6 = start+5;
    int b7 = start+6;
    //Pivots
    int v1 = vArray[b1]; 
    int v2 = vArray[b2];
    int v3 = vArray[b3];
    int v4 = vArray[b4];
    int v5 = vArray[b5];
    int v6 = vArray[b6];
    int v7 = vArray[b7];	   
    for (int scan=b7+1; scan<stop; ++scan) {
      int v = vArray[scan];
      if ( v < v4 ) {
        if ( v < v2 ) {
          if ( v < v1 ) {
            vArray[b1] = v;
            ++b1;
            vArray[b2] = vArray[b1];
          } else {
            vArray[b2] = v;
          }
          ++b2;
          vArray[b3] = vArray[b2];
          ++b3;
          vArray[b4]   = vArray[b3];
        } else if ( v < v3) {
          vArray[b3] = v;
          ++b3;
          vArray[b4]   = vArray[b3];
        } else {
          vArray[b4] = v;
        }
        ++b4;
        vArray[b5]   = vArray[b4];
        ++b5;
        vArray[b6]   = vArray[b5];
        ++b6;
        vArray[b7]   = vArray[b6];
        ++b7;
        vArray[scan] = vArray[b7];
      } else if ( v < v6 ) {
        if ( v < v5 ) {
          vArray[b5] = v;
          ++b5;
          vArray[b6] = vArray[b5];
        } else {
          vArray[b6] = v;
        }
        ++b6;
        vArray[b7]   = vArray[b6];
        ++b7;
      	vArray[scan] = vArray[b7];
      } else if ( v < v7 ) {
        vArray[b7] = v;
        ++b7;
        vArray[scan] = vArray[b7];
      }
    }
    //Write pivots back
    vArray[b1] = v1;
    vArray[b2] = v2;
    vArray[b3] = v3;
    vArray[b4] = v4;
    vArray[b5] = v5;
    vArray[b6] = v6;
    vArray[b7] = v7; //write extra pivot back		
    return new int[] 
      { start, b1, b1+1, b2, b2+1, b3, b3+1, b4
      , b4+1,  b5, b5+1, b6, b6+1, b7, b7+1, stop };
    }
  @Override public int getPivotCount() {
    return 7;
  }
}
