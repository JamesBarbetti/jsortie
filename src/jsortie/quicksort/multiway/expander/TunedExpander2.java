package jsortie.quicksort.multiway.expander;

import jsortie.helper.ShiftHelper;
import jsortie.quicksort.multiway.expander.holierthanthou.HolierThanThouExpander2;
import jsortie.quicksort.partitioner.branchavoiding.TunedPartitioner;
import jsortie.quicksort.partitioner.interfaces.SinglePivotPartitioner;

public class TunedExpander2
  extends HolierThanThouExpander2 {
  protected ShiftHelper            shifter = new ShiftHelper();
  protected SinglePivotPartitioner spp = new TunedPartitioner();
  
  @Override
  public int[] expandPartitions
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int startRight, int stop) {
    //Note: This is busted. It needs fixing.
    int sampleStart = stopLeft;
    int pivotIndex1 = pivotIndices[pivotIndices.length/3];
    int vP          = vArray[pivotIndex1];
    int pivotIndex2 = pivotIndices[2*pivotIndices.length/3];
    //move items known to be <= vQ, excluding vQ itself, 
    //left, and shift vP to the far right of those moved items
    shifter.moveBackElementsToFront(vArray, start, sampleStart, pivotIndex2 ); 
    //note that the range [unSortedStart..unsortedStop-1] 
    //will *include* vP at [unSortedStart] and vQ at [unSortedStop-1]
    int unsortedStart     = start + (pivotIndex2 - sampleStart - 1);  //-1 because vP is to be included
    int betweenStart      = start + (pivotIndex1 - sampleStart);
    vArray[betweenStart]  = vArray[unsortedStart]; //swap last value that was between vP and vQ 
    vArray[unsortedStart] = vP;                    //with vP
    //move items >= v2, including v2 itself, right
    shifter.moveFrontElementsToBack(vArray, pivotIndex2, startRight, stop); //includes V
    int unsortedStop = stop + 1 - (startRight - pivotIndex2); //index of the first item *after* the place vQ is now (+1 for after)
    int split1  = spp.partitionRange(vArray, unsortedStart, unsortedStop-1, unsortedStart ); //partition with vP, leaving vQ out
    pivotIndex2 = spp.partitionRange(vArray, split1+1, unsortedStop, unsortedStop-1);        //and then with vQ
    //>vQ and vQ already where they should be, but values <vP (and vP) itself need to move left 
    //past the previously sorted items that were between vP and vQ
    shifter.moveBackElementsToFront( vArray, betweenStart, unsortedStart, split1+1); //<vP, and vP move left
    pivotIndex1 = betweenStart + (split1-unsortedStart);
    return new int[] { start, pivotIndex1, pivotIndex1+1, pivotIndex2, pivotIndex2+1, stop };
  }
  @Override
  public int[] expandPartitionsToLeft
    ( int[] vArray, int start, int stopLeft
    , int[] pivotIndices, int stop) {
    return expandPartitions
           ( vArray, start, stopLeft
           , pivotIndices, stop, stop);
  }
  @Override
  public int[] expandPartitionsToRight
    ( int[] vArray, int start
    , int[] pivotIndices, int scan, int stop ) {
    return expandPartitions
           ( vArray, start, start
           , pivotIndices, scan, stop);
  }
}
