package jsortie.quicksort.protector;

import jsortie.RangeSorter;

public class CheckingRangeSorter implements RangeSorter
{
  protected RangeSorter innerSorter;
	
  public CheckingRangeSorter(RangeSorter innerSorter) {
    this.innerSorter = innerSorter;
  }	
	
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "(" + innerSorter.toString() + ")";
  }
	
  @Override
  public void sortRange(int[] vArray, int start, int stop) {
    if (vArray==null) {
      throw new NullPointerException("null array passed to sortRange");
    }
    if (start < 0)  {
      throw new IndexOutOfBoundsException("start index (" + start 
                + ") passed to sortRange was less than zero");
    }
    if (vArray.length <= start) {
      throw new IndexOutOfBoundsException("start index (" + start 
                + ") passed to sortRange was greater than the length of the array (" + vArray.length + ")");
    }
    if (vArray.length < stop) {
      throw new IndexOutOfBoundsException("stop index (" + stop 
				+ ") passed to sortRange was greater than the length of the array (" + vArray.length + ")");
    }
    innerSorter.sortRange(vArray, start, stop);
  }
}
