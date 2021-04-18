package jsortie.quicksort.multiway.partitioner.decorator;

import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;

public class HSortDecorator extends PartitionerDecorator {
  public HSortDecorator(MultiPivotPartitioner multiPartitioner) {
    super(multiPartitioner);
  }
  @Override
  public int[] shrinkPartitions
    ( int[] vArray, int rStart, int rStop, int[] partitions ) {
    //Note: appears to be broken
    int w = 0;
    for (int p=0; p<partitions.length; p+=2) {
      int lhs = partitions[p];
      int rhs = partitions[p+1];
      if ( lhs+1 < rhs ) {
        int i = rhs-1;
        do {
          for (int h = lhs; h<i; ++h, --i) {
            int vLeft  = vArray[h];
            int vRight = vArray[i];
            int vLess  = (vLeft <= vRight) ? vLeft : vRight;
            int vMore  = vLeft + vRight - vLess;
            //Trick.  For types that aren't overflow-safe you'd need
            //vMore = (vLeft <= vRight) ? vRight : vLeft
            //a second conditional move, instead.
            vArray[h] = vLess;
            vArray[i] = vMore;
          }
        } while (lhs<i);
        partitions[w]   = lhs+1;
        partitions[w+1] = rhs;
        w              += 2;
      }
    }
    return survivingPartitions(partitions,w);
  }
}
