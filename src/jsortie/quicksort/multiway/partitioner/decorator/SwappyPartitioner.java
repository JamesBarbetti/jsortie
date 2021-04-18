package jsortie.quicksort.multiway.partitioner.decorator;

import jsortie.helper.RangeSortHelper;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.adapter.SingleToMultiPartitioner;
import jsortie.quicksort.partitioner.bidirectional.centrepivot.CentrePivotPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class SwappyPartitioner
  extends PartitionerDecorator {
  public SwappyPartitioner() {
    super(new SingleToMultiPartitioner
    		  ( new CentrePivotPartitioner()));
  }
  public SwappyPartitioner
    ( SinglePivotPartitioner singlePivotPartitioner ) {
    super(singlePivotPartitioner);
  }
  public SwappyPartitioner
    ( MultiPivotPartitioner multiPivotPartitioner) {
    super(multiPivotPartitioner); 
  }
  public String toString() {
    return this.getClass().getSimpleName() + "(" + partitioner.toString() + ")";
  }
  @Override
  public int[] shrinkPartitions
    ( int[] vArray, int start, int stop, int[] partitions ) {
    for (int p=0; p<partitions.length; p+=2) {
      int lhs       = partitions[p];
      int localStop = partitions[p+1];
      int rhs       = localStop-1;
      if (lhs<rhs) {
        RangeSortHelper.compareAndSwapIntoOrder(vArray, lhs, rhs);
        for (++lhs,--rhs;lhs<=rhs;++lhs,--rhs) {
          boolean swappedLeft  = RangeSortHelper.compareAndSwapIntoOrder(vArray, lhs-1, lhs);
          boolean swappedRight = RangeSortHelper.compareAndSwapIntoOrder(vArray, rhs, rhs+1);
          if (swappedLeft && swappedRight) {
            //Do nothing
          } else {
            RangeSortHelper.compareAndSwapIntoOrder(vArray, lhs, rhs);
          }
        }
        int lastSwap  = lhs;
        int firstSwap = rhs;
        for (;lhs<localStop;++lhs,--rhs) {
          if (RangeSortHelper.compareAndSwapIntoOrder(vArray, lhs-1, lhs))
            lastSwap  = lhs;
          if (RangeSortHelper.compareAndSwapIntoOrder(vArray, rhs, rhs+1))
            firstSwap = rhs;				
        }
        partitions[p]   = firstSwap;
        partitions[p+1] = lastSwap;
      }
    }
    return partitions;
  }	
}
