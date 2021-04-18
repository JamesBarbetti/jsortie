package jsortie.quicksort.protector;

import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class CheckingSinglePivotPartitioner implements SinglePivotPartitioner
{
  protected SinglePivotPartitioner innerPartitioner;

  public CheckingSinglePivotPartitioner(SinglePivotPartitioner innerPartitioner) {
    this.innerPartitioner = innerPartitioner;
  }
	
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "(" + innerPartitioner.toString() + ")";
  }	
	
  @Override
  public int partitionRange(int[] vArray, int start, int stop, int pivotIndex) {
    if (vArray==null) {
      throw new NullPointerException("null array passed to sortRange");
    }
    if (start < 0) {
      throw new IndexOutOfBoundsException("start index (" + start 
				+ ") passed to partitionRange was less than zero");
    }
    if (vArray.length <= start) {
      throw new IndexOutOfBoundsException("start index (" + start 
				+ ") passed to partitionRange was greater than the length of the array (" + vArray.length + ")");
    }
    if (vArray.length < stop) {
      throw new IndexOutOfBoundsException("stop index (" + stop 
				+ ") passed to partitionRange was greater than the length of the array (" + vArray.length + ")");
    }
    if ( pivotIndex < start || stop <= pivotIndex ) {
      throw new IndexOutOfBoundsException("pivotIndex (" + stop 
                + ") passed to partitionRange was outside the legal range (" + start 
                + " to " + (stop-1) + " inclusive)");
    }
	return innerPartitioner.partitionRange(vArray, start, stop, pivotIndex);		
  }
}
