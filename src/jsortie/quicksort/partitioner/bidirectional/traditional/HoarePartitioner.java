package jsortie.quicksort.partitioner.bidirectional.traditional;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class HoarePartitioner 
  implements SinglePivotPartitioner {
  //This is a Java version of the original, 
  //Hoare Quicksort partitioner, of 1959, 
  //*not* the better known Scowen Quickersort 
  //partitioner of 1965.
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
  @Override
  public int partitionRange
    ( int vArray[], int start, int stop
    , int pivotIndex) { 
    if (stop<=start+1) {
      return start;
    }
    int vPivot         = vArray[pivotIndex];
    vArray[pivotIndex] = vArray[start];
    vArray[start]      = vPivot;
    pivotIndex         = start;
    int lhs            = start;
    int rhs            = stop;
    int vTemp;        
    do {
      do {
        --rhs;
      } while ( lhs<rhs && vPivot <= vArray[rhs] );
      vTemp              = vArray[pivotIndex];
      vArray[pivotIndex] = vArray[rhs];
      vArray[rhs]        = vTemp;
      pivotIndex         = rhs;     
      do {
        ++lhs;
        if (rhs<=lhs) {
          return pivotIndex;
        }
      } while ( vArray[lhs] <= vPivot );
      vTemp              = vArray[pivotIndex];
      vArray[pivotIndex] = vArray[lhs];
      vArray[lhs]        = vTemp;
      pivotIndex         = lhs;
    } while (lhs<rhs);
    return pivotIndex;
  }
}
