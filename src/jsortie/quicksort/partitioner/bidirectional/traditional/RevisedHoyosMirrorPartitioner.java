package jsortie.quicksort.partitioner.bidirectional.traditional;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class RevisedHoyosMirrorPartitioner implements SinglePivotPartitioner
{
  @Override
  public String toString() { return this.getClass().getSimpleName(); }	
  
  @Override
  public int partitionRange(int vArray[], int start, int stop, int pivotIndex) {
    int vPivot = vArray[pivotIndex];
    int lhs    = start-1;
    int rhs    = stop;

    do { --rhs; } while ( vPivot < vArray[rhs]);
    if ( pivotIndex < rhs ) {
      int vLifted = vArray[rhs];
      do { ++lhs; } while ( vArray[lhs] < vPivot );
      if ( lhs < pivotIndex ) {
        do {
          vArray[rhs] = vArray[lhs];
          do { --rhs; } while ( vPivot < vArray[rhs] );
          if (rhs==pivotIndex) {
        	vArray[lhs] = vLifted;
        	return partitionRangeFromRight(vArray, lhs, rhs);
          }
          vArray[lhs] = vArray[rhs];
          do { ++lhs; } while ( vArray[lhs] < vPivot );
        } while (lhs<pivotIndex);
      }
      vArray[rhs]=vPivot;
      vArray[lhs]=vLifted;
    }
    return partitionRangeFromRight(vArray, lhs, rhs);
  }
  
  //
  //Note: firstLeft Stop is *out of the range*, and lastRight is *in the range*
  //This is NOT the convention used in most of the classes in the JSortie package.
  //
  protected int partitionRangeFromRight(int[] vArray, int firstLeft, int lastRight) {
    int v = vArray[lastRight];
	do { ++firstLeft; } while ( vArray[firstLeft] < v );
    if (firstLeft<lastRight ) {
      vArray[lastRight] = vArray[firstLeft]; 
      vArray[firstLeft] = v;
      do { --lastRight; } while ( v < vArray[lastRight] );
      if (firstLeft<lastRight) {
        do {
          vArray[firstLeft] = vArray[lastRight]; 
          do { ++firstLeft; } while ( vArray[firstLeft] < v );
          if (firstLeft>=lastRight) {
	        vArray[lastRight]=v;
            return lastRight;
          }
          vArray[lastRight] = vArray[firstLeft]; 
          do { --lastRight; } while ( v < vArray[lastRight] );
        } while (firstLeft<lastRight);
        lastRight = firstLeft;
      } //leftStop<rightStart
    } //leftStop<rightStart
    vArray[lastRight] = v;
    return lastRight;
  }	  
}
