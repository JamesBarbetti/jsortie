package jsortie.object.quicksort.multiway.partitioner.permuting;

import java.util.Comparator;

public class TrackAndPermuteObjectPartitionerWithBellsOn<T> 
  extends TrackAndPermuteObjectPartitioner<T> {
  @SuppressWarnings("unchecked")
  protected void mapItemsToPartitions
    ( Comparator<? super T> comparator
    , T[] vArray, int start, int stop
    , Object[] vPivots, int pivotCount
    , int[] partitionCounts, int[] itemToPartition) {
    Object[] vImplicitTree        
      = new Object[pivotCount];
    int[] leafIndexToPartition 
      = new int[pivotCount+1];
    constructImplicitTree( vPivots, pivotCount, 0
      , vImplicitTree, leafIndexToPartition, 0);
	  
    int i;
    int diff;
    for (i=start; i<stop; ++i) {
      T   v = (T) vArray[i];
      int x = 0;  //index of position in implicit tree 
      do {        //binary search, for the partition 
                  //in which v belongs
    	diff = comparator.compare(v, (T) vImplicitTree[x]);
    	if (diff==0) {
    	  x += x + 1;
    	  while (x<pivotCount) {
            x += x + 2;
    	  }
    	  break;
    	}
        x += x + (( diff < 0) ? 1 : 2);
      } while (x<pivotCount);
      int q = leafIndexToPartition[x-pivotCount];
      if (diff==0) ++q;
      ++partitionCounts[q];
      itemToPartition[i-start]=q;
    }
  }
  private int constructImplicitTree
    ( Object[] vFlatPivots, int pivotCount, int r
    , Object[] vTreePivots
    , int[] leafIndexToPartition, int w ) {
    if (w<pivotCount) {
      r = constructImplicitTree 
          ( vFlatPivots, pivotCount, r
          , vTreePivots, leafIndexToPartition, w + w + 1 );
      vTreePivots [w]         = vFlatPivots [r];
      ++r;
      r = constructImplicitTree 
          ( vFlatPivots, pivotCount, r
          , vTreePivots, leafIndexToPartition, w + w + 2 );
    } else {      
      leafIndexToPartition[w-pivotCount] = r*2;
    }		
    return r;
  }
}
