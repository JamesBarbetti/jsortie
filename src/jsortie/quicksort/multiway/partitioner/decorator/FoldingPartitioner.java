package jsortie.quicksort.multiway.partitioner.decorator;

import jsortie.quicksort.multiway.partitioner.MultiPivotPartitioner;
import jsortie.quicksort.multiway.partitioner.adapter.SingleToMultiPartitioner;
import jsortie.quicksort.partitioner.branchavoiding.BalancedSkippyPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class FoldingPartitioner 
  extends PartitionerDecorator {
  public FoldingPartitioner() {
    super(new SingleToMultiPartitioner
              (new BalancedSkippyPartitioner()));
  }
  public FoldingPartitioner 
    ( SinglePivotPartitioner singlePivotPartitioner ) {
    super(singlePivotPartitioner);
  }
  public FoldingPartitioner
    ( MultiPivotPartitioner multiPartitioner ) {
    super(multiPartitioner);
  }
  @Override
  public int[] shrinkPartitions
    ( int[] vArray, int rStart, int rStop
    , int[] partitions) {
    int w = 0;
    for (int p=0; p<partitions.length; p+=2) {
      int lhs = partitions [ p    ];
      int rhs = partitions [ p + 1];
      if ( rhs<=lhs+1 || rStop<=lhs ) {
        continue;
      }
      setUpSentinelsUnstable
        ( vArray, lhs, rhs );
      //now, vArray[lhs] is the minimum 
      //of the range [lhs..rhs-1] inclusive
      //and, vArray[rhs-1] is the maximum 
      //of the range.
      if ( vArray[rhs-1] <= vArray[lhs] ) {
        //Then... all items in 
        //partition must be equal.
        //This case is checked for because 
        //otherwise the ++lhs and --rhs
        //loops would need boundary checks.
        continue; 
      } else {
        //minimum < maximum so:
        //we don't need boundary
        //checking for these two 
        //telescoping loops.
        do {
          ++lhs;
        } while ( vArray[lhs-1] == vArray[lhs] );
        do {
          --rhs;
        } while ( vArray[rhs-1] == vArray[rhs] );
      }
      if ( rhs<=lhs+1) {
        continue;
      }
      partitions[w]   = lhs;
      partitions[w+1] = rhs;
      w+=2;
    }
    return survivingPartitions(partitions,w);
  }
  public static void setUpSentinelsUnstable
    ( int[] vArray, int start, int stop ) {
    int last = stop-1;
    int lhs = start;
    int rhs = last;
    do {
      int vLeft  = vArray[lhs];
      int vRight = vArray[rhs];
      if (vRight<vLeft) {
        vArray[lhs] = vRight;
        vArray[rhs] = vLeft;
      }
      --rhs;
      ++lhs;
      if (rhs<=lhs) {
        lhs=start;
      }
    } while (start<rhs);
    lhs = start + (stop-start)/2;
    rhs = stop -1;
    do {
      int vLeft  = vArray[lhs];
      int vRight = vArray[rhs];
      if (vRight<vLeft) {
        vArray[lhs] = vRight;
        vArray[rhs] = vLeft;
      }
      --rhs;
      ++lhs;
      if (rhs<=lhs) {
        rhs=last;
      }
    } while (lhs<last);
  }
  
}
