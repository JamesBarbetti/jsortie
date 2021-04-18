package jsortie.quicksort.multiway.partitioner.kangaroo;

import java.util.Arrays;

import jsortie.quicksort.multiway.expander.kangaroo.SkippyCentripetalExpander2;
import jsortie.quicksort.multiway.primitives.MultiPivotUtils;

public class SkippyCentripetalPartitioner2 
  extends SkippyPartitioner2 {  
  public SkippyCentripetalPartitioner2 () {
    mpx = new SkippyCentripetalExpander2();
  }
  @Override
  public int[] multiPartitionRange 
    ( int[] vArray, int start, int stop
    , int[] pivotIndices) {
    int c = pivotIndices.length;
    int m = stop-start;
    int[] partitions;
    if (pivotCount<=c) {
      if (c<=m) {
        if (!MultiPivotUtils.areAllPivotsTheSame 
             ( vArray, pivotIndices ) ) { //nominal case
          int middleStart = start + (m-c) / 2;
          int middleStop  = middleStart + c;
          pivotIndices    
            = moveEvenlySpacedPivotsToMiddle
              ( vArray, pivotIndices, pivotCount
              , middleStart, middleStop);
          partitions
            = mpx.expandPartitions
              ( vArray, start, middleStart
              , pivotIndices, middleStop, stop );
        } else { //duplicate pivots
            partitions 
              = MultiPivotUtils
                .partitionRangeWithOnePivot 
                ( vArray, start, stop
                , pivotIndices[0], spx);
        }
      } else { //number of candidates >= size of input
        partitions 
          = MultiPivotUtils.fakePartitions
            ( vArray, start, stop
            , pivotIndices, pivotCount);
      }
    } else { //not enough candidates
      partitions 
        = MultiPivotUtils.fakePartitions
          ( vArray, start, stop
          , pivotIndices, pivotCount);
    }
    return MultiPivotUtils.ensurePartitionCount
           ( partitions, partitionBoundaryCount );
  }
  protected int[] moveEvenlySpacedPivotsToMiddle
    ( int[] vArray, int[] pivotIndices
    , int p /* # of pivots wanted */
    , int middleStart, int middleStop ) {
    int c = pivotIndices.length; 
            //# of candidates (it is assumed: p<c) 
    int[] correctedPivotIndices = new int [c]; 
    for (int i=0; i<c; ++i) {
  	  correctedPivotIndices[i] = middleStart+i;
    }
    //collect the c pivot candidate items, 
    //to the middle of array range, 
    //vArray[middleStart] through 
    //vArray[middleStop-1] inclusive, 
    //in ascending order. For the time being, 
    //we'll use correctedPivotIndices to track 
    //where the items that were in the 
    //those (c) elements of the array *went*.
    for (int w=0; w<c; ++w) { //w is 0-based
      int vTemp = vArray[middleStart+w];  
                  //the item being swapped out
      int r     = pivotIndices[w]; //r is start-based
      while (middleStart<=r && r<middleStart+w) {
        r  = correctedPivotIndices[r-middleStart];
        //todo: should shorten the chain here, 
        //      to avoid nasty worst-case running time. 
        //      But does this ever execute more than once?
        //      Need to think about it.
      }
      //swap w-th pivot candidate into place
      vArray[middleStart+w]    = vArray[r]; 
      //and write vTemp into the place it came from
      vArray[r]                = vTemp;     
      correctedPivotIndices[w] = r;
    }
    for (int w=p-1;0<=w;--w) {
      correctedPivotIndices[w] 
        = middleStart - 1
        + (int) Math.floor
          ( (double)(c+1)*(double)(w+1)/(double)(p+1));
    }
    return (p==c)
      ? correctedPivotIndices 
      : Arrays.copyOf
        ( correctedPivotIndices, p );
  }    
}
