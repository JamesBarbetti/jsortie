package jsortie.quicksort.multiway.partitioner.singlepivot;

public class SkippyDutchPartitioner 
  implements TernarySinglePivotPartitioner {
  //A Male Kangaroo has two penises, but 
  //it only uses one at a time.
  //This partitioner has two floating holes, 
  //but only uses one pivot.
  //Speaking of holes, a Female Kangaroo has
  //(wait for it!)... three vaginas.
  //They're all one-way streets.  
  //One's a birth canal... The other two...
  //aren't.
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    int pivotIndex 
      = pivotIndices[pivotIndices.length/2];
    return partitionRangeWithOnePivot
           ( vArray, start, stop, pivotIndex );
  }
  public int[] partitionRangeWithOnePivot
    ( int[] vArray, int start
    , int stop, int   pivotIndex) {
    int vPivot = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    int hole1   = start;
    int hole2   = start+1;
    int vLifted = vArray[hole2];
    for ( int scan=start+2
        ; scan<stop; ++scan ) {
      int v = vArray[scan];
      vArray[hole1] = v;
      hole1 += ( v < vPivot ) ? 1 : 0;
      vArray[hole2] = vArray[hole1];
      hole2 += ( v <= vPivot ) ? 1 : 0;
      vArray[scan] = vArray[hole2];
    }
    if ( vLifted < vPivot ) {
      vArray[hole1] = vLifted;
      vArray[hole2] = vPivot;
      return new int[] 
        { start, hole1+1, hole2+1, stop };
    } else {
      vArray[hole1] = vPivot;
      vArray[hole2] = vLifted;
      hole2 += ( vLifted == vPivot ) ? 1 : 0;
      return new int[] 
        { start, hole1, hole2, stop };
    }
  }
  @Override
  public int getPivotCount() {
    return 1;
  }
}
