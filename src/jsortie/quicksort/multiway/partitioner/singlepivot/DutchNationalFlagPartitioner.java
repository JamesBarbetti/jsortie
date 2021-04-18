package jsortie.quicksort.multiway.partitioner.singlepivot;

public class DutchNationalFlagPartitioner
  implements TernarySinglePivotPartitioner {
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }	
  @Override
  public int[] multiPartitionRange 
    ( int[] vArray, int start, int stop
    , int[] pivotIndices) {
    int cOn2    = pivotIndices.length/2;
    int iMiddle = pivotIndices[cOn2];
    int vPivot  = vArray[iMiddle];
    int i = start-1; //last < element
    int j = start; //unexamined element
    int k = stop;  //last > element
    while ( j < k  ) {
      int v = vArray[j];
      if ( vPivot < v ) {
        --k;
        vArray[j] = vArray[k];
        vArray[k] = v; 
      } else {
        if ( v < vPivot ) {
          ++i;
          vArray[j] = vArray[i];
          vArray[i] = v;
        }
        ++j;
      }
    }
    return new int[] { start, i+1, k, stop };
  }
  @Override
  public int getPivotCount() {
    return 1;
  }
}
