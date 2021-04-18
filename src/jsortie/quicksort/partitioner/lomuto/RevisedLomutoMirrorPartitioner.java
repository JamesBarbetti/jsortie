package jsortie.quicksort.partitioner.lomuto;

import jsortie.helper.RangeSortHelper;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class RevisedLomutoMirrorPartitioner implements SinglePivotPartitioner
{
  @Override
  public String toString() { return this.getClass().getSimpleName(); }
	
  @Override
  public int partitionRange(int [] vArray, int start, int stop, int pivot) 
  { 
    RangeSortHelper.swapElements(vArray, pivot, stop-1);
    int v = vArray[stop-1];
    int nextRight = stop - 2;

    while (start<=nextRight && v<vArray[nextRight]) { --nextRight; } //skip stuff on right that can stay

    for (int i=start; i<=nextRight; ++i)
    {
      if (v<vArray[i])
      {
         RangeSortHelper.swapElements(vArray, i, nextRight);
         --nextRight;
         while (v<vArray[nextRight]) { --nextRight; } //skip stuff on right that can stay
	  }			
    }
		
    RangeSortHelper.swapElements(vArray, nextRight+1, stop-1);
    return nextRight+1;
  }
}
