package jsortie.quicksort.multiway.partitioner.permuting;

public class TrackAndPermutePartitionerWithBellsOn 
extends TrackAndPermutePartitioner {
  protected void mapItemsToPartitions 
    ( int[] vArray, int start, int stop
    , int[] vPivots, int pivotCount
    , int[] partitionCounts, int[] itemToPartition) {
    int[] vImplicitTree        = new int[pivotCount];
    int[] leafIndexToPartition = new int[pivotCount+1];
    constructImplicitTree( vPivots, pivotCount, 0
      , vImplicitTree, leafIndexToPartition, 0);
    int i;
    for (i=start; i<stop; ++i) {
      int v     = vArray[i];
      int x     = 0;  //index of position 
                      //in implicit tree 
      int equal = 0;  //1 if equal to last 
                      //thing compared to, else 0
      do { //binary search for the partition 
           //in which v belongs
        if (v==vImplicitTree[x]) {
          equal = 1;
          x += x + 1;
          while (x<pivotCount) {
            x += x + 2;
          }
          break;
        }
        x += x + (( v < vImplicitTree[x] ) ? 1 : 2);
      } while (x<pivotCount);
      int q = leafIndexToPartition[x-pivotCount] 
            + equal;
      ++partitionCounts[q];
      itemToPartition[i-start]=q;
    }
  }
  protected int constructImplicitTree
    ( int[] vFlatPivots, int pivotCount, int r
    , int[] vTreePivots
    , int[] leafIndexToPartition, int w ) {
    if (w<pivotCount) {
      r = constructImplicitTree 
          ( vFlatPivots, pivotCount, r
          , vTreePivots, leafIndexToPartition
          , w + w + 1 );
      vTreePivots [w] = vFlatPivots [r];
      ++r;
      r = constructImplicitTree 
          ( vFlatPivots, pivotCount, r
          , vTreePivots, leafIndexToPartition
          , w + w + 2 );
    } else {      
      leafIndexToPartition[w-pivotCount] = r*2;
    }		
    return r;
  }
}
