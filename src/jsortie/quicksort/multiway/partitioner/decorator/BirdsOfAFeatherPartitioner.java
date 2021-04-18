package jsortie.quicksort.multiway.partitioner.decorator;

import jsortie.helper.EgalitarianPartitionerHelper;
import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;

public class BirdsOfAFeatherPartitioner 
  extends PartitionerDecorator {
  EgalitarianPartitionerHelper helper 
    = new EgalitarianPartitionerHelper();
  public String toString() {
    return this.getClass().getSimpleName();
  }
  public BirdsOfAFeatherPartitioner
    ( MultiPivotPartitioner multiPartitioner ) {
    super(multiPartitioner);
  }
  @Override
  public int[] shrinkPartitions
    ( int[] vArray, int start, int stop
    , int[] partitions) {
    int w = 2;
    int lastPartition = partitions.length - 2;
    if (0 < lastPartition) {
      //shrink first partition (moving values 
      //== to the item to its right, to the right edge)
      partitions[1] 
        = helper.swapEqualToRight
          (vArray, partitions[0], partitions[1]
          , vArray[partitions[1]]);
      int p;
      for (p = 2; p < lastPartition
                  && partitions[p+1] < stop ; p += 2) {
        int lhs = partitions[p];
        int rhs = partitions[p + 1];
        int vLeft = vArray[partitions[p] - 1];
        int vRight = vArray[partitions[p + 1]];
        if (lhs + 1 < rhs && vLeft < vRight) {
          lhs = helper.swapEqualToLeft
                ( vArray, lhs, rhs, vLeft );
          rhs = helper.swapEqualToRight
                ( vArray, lhs, rhs, vRight );
          if (lhs + 1 < rhs) { 
            //Keep the (possibly shrunken) partition
            //if anything is left in it
            partitions[w] = lhs;
            partitions[w + 1] = rhs;
            w += 2;
          }
        }
      }
      //shrink last partition (moving values == 
      //to the item to its left, to the left edge)
      partitions[p] 
        = helper.swapEqualToLeft
          ( vArray, partitions[p], partitions[p + 1]
          , vArray[partitions[p] - 1]);
      if (partitions[p] + 1 < partitions[p + 1]) {
        partitions[w] = partitions[p];
        partitions[w + 1] = partitions[p + 1];
        w += 2;
      }
    }
    return survivingPartitions(partitions, w);
  }
}
