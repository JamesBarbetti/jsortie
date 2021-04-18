package jsortie.quicksort.multiway.partitioner;

import jsortie.quicksort.multiway.partitioner.singlepivot.InertialPartitioner;

public class AdaptivePartitioner 
  implements MultiPivotPartitioner {	
  MultiPivotPartitioner  inner;
  MultiPivotPartitioner  inertial 
    = new InertialPartitioner(2);
	
  public AdaptivePartitioner
    ( MultiPivotPartitioner inner ) {
    this.inner = inner;
  }
	
  @Override
  public String toString() {
    return this.getClass().getSimpleName() 
      + "(" + inner.toString() + ")";
  }

  @Override
  public int[] multiPartitionRange
    ( int[] vArray, int start, int stop
    , int[] pivotIndices ) {
    int pivotCount = pivotIndices.length;
    int left       = start;
    int right      = stop-1;
	
    int iFirst   = pivotIndices[0];
    int vLowest  = vArray[iFirst];
    int iLast    = iFirst;
    int vHighest = vArray[pivotIndices[pivotCount-1]];

    if ( pivotCount == 1 ) {
      return inner.multiPartitionRange
             ( vArray, start, stop, pivotIndices );
    } else if ( vLowest == vHighest) {
      return inertial.multiPartitionRange
             ( vArray, start, stop, pivotIndices );
    }
		
    //Get first and last pivot indices
    for (int i=1; i<pivotCount; ++i) {
      if ( pivotIndices[i] < iFirst ) {
        iFirst = pivotIndices[i];
      }
      else if ( iLast < pivotIndices[i] ) {
        iLast = pivotIndices[i];
      }
    }
		
    int vCopyLeft  = vArray[iFirst];
    int vCopyRight = vArray[iLast];
    vArray[iFirst] = vLowest;
    vArray[iLast]  = vHighest;
		
    //
    //Skip over leading elements on the left <vLowest
    //and leading elements on the right that >vHighest
    //(helps with mostly in-order input).
    //
    //Also, if first element kept on left >vHighest,
    //and last element kept on right <vLowest, swap them
	//and skip some more.
    //(helps with mostly reverse-ordered input).
    //
    for (;;++left, --right) {
      while (vArray[left] < vLowest) {
        ++left;
      }
      while (vHighest < vArray[right]) {
        --right;
      }
      if ( left==iFirst || right == iLast ) break;
      if ( vLowest     <= vArray[right] )    break;
      if ( vArray[left] <= vHighest )        break;
      int v = vArray[left];
      vArray[left] = vArray[right];
      vArray[right] = v;
    }
		
    vArray[iFirst] = vCopyLeft;
    vArray[iLast]  = vCopyRight;
		
    int innerAnswer[] 
      = inner.multiPartitionRange
        ( vArray, left, right+1, pivotIndices );
    innerAnswer[0]                    = start;
    innerAnswer[innerAnswer.length-1] = stop;
    return innerAnswer;
  }
}
