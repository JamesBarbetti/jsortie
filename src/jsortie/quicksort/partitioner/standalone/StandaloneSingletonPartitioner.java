package jsortie.quicksort.partitioner.standalone;

import jsortie.helper.RangeSortHelper;
import jsortie.janitors.insertion.InsertionSort;
import jsortie.quicksort.partitioner.interfaces.StandAlonePartitioner;

public class StandaloneSingletonPartitioner implements StandAlonePartitioner {
  protected void orderFirstSecondAndLast(int[] vArray, int start, int stop) {
    RangeSortHelper.compareAndSwapIntoOrder(vArray, start, start+1);
    if (RangeSortHelper.compareAndSwapIntoOrder(vArray, start+1, stop-1))
    {
      RangeSortHelper.compareAndSwapIntoOrder(vArray, start, start+1);
    }	
  }
  @Override
  public int[] multiPartitionRange(int[] vArray, int start, int stop) { 
    if (stop-start<3) {
      InsertionSort.sortSmallRange(vArray, start, stop);
      return new int[] { start, start };
    }
    orderFirstSecondAndLast(vArray, start, stop);
    int lhs   = start+1;
    int rhs   = stop;
    int v     = vArray[lhs];
    
    do { ++lhs; } while (vArray[lhs]<v);  
    do { --rhs; } while (v<vArray[rhs]);
    if (lhs==rhs) {
      return new int[] { start, start };
    }
    do  {
      RangeSortHelper.swapElements( vArray, lhs, rhs);
      do { --rhs; } while (v<vArray[rhs]); 
      if (rhs <= lhs) {
        vArray[start+1] = vArray[lhs];
        return new int[] { start, lhs, lhs+1, stop };  
      }
      do { ++lhs; } while (vArray[lhs]<v && lhs<rhs);  
    } while (lhs<rhs);
    vArray[start+1] = vArray[rhs];
    vArray[rhs]     = v;
    return new int[] { start, rhs, rhs+1, stop };      
  }
}