package jsortie.quicksort.expander.adapter;

import jsortie.helper.ShiftHelper;
import jsortie.quicksort.expander.PartitionExpander;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class PartitionerToExpander 
  implements PartitionExpander {
  protected SinglePivotPartitioner party;
  protected ShiftHelper shifter;
  @Override
  public String toString() {
    return this.getClass().getSimpleName()
    + "(" + party.toString() + ")";
  }
  public PartitionerToExpander
    ( SinglePivotPartitioner partitioner ) {
    party   = partitioner;
    shifter = new ShiftHelper();
  }
  //
  //Algorithm:
  //Initially                     U1 L2 v H2 U3
  //Shift partitioned bits out    L2 U1 v U3 H2  
  //Partition the middle          L2 L1+L3 v H1+H3 H2
  //Shift partitioned bits back   L1+L3 L2 v H2 H1+H3
  //
  //The *assumption* is that L2 and H2 are small subranges
  //(that together they are less a third of the items); 
  //and that items near the current pivot probably... 
  //belong roughly where they already are, and 
  //in roughly the (relative) order they're already in.
  @Override
  public int expandPartition 
    ( int[] vArray, int start, int stopLeft
    , int pivotIndex, int startRight, int stop) {
    int knownLow  = pivotIndex-stopLeft;
    int knownHigh = startRight-pivotIndex-1;
    shifter.moveBackElementsToFront
    ( vArray, start, pivotIndex-knownLow, pivotIndex );
    shifter.moveFrontElementsToBack
    ( vArray, pivotIndex+1, pivotIndex+1+knownHigh, stop );
    pivotIndex = party.partitionRange
    ( vArray, start+knownLow, stop-knownHigh, pivotIndex);
    shifter.moveFrontElementsToBack
    ( vArray, start, start+knownLow, pivotIndex );
    shifter.moveBackElementsToFront
    ( vArray, pivotIndex+1, stop-knownHigh, stop );
    return pivotIndex;
  }
}
